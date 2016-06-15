package com.eksiir.StreamingDataManager.Consumer.CarrierListener.Kinesis;

import com.amazonaws.services.cloudfront.model.InvalidArgumentException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.InvalidStateException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ShutdownException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ThrottlingException;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorCheckpointer;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownReason;
import com.amazonaws.services.kinesis.model.Record;
import com.eksiir.StreamingDataManager.Consumer.ConsumerConfig;
import com.eksiir.StreamingDataManager.Consumer.StreamingDataProcessor.StreamingDataProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;

/**
 * Record processor for <link>KinesisListenerFactory</link>  push model.
 *
 * Multi-threading:
 * For every shard in the stream at least one thread will call the <link>IRecordProcessor</link> methods.
 *
 * Created by bbehzadi on 1/24/14.
 */
public class KinesisWorkerRecordProcessor implements IRecordProcessor {
    private final Log log = LogFactory.getLog(KinesisWorkerRecordProcessor.class);
    private final int numRetries;
    private final int maxRecordsNotCheckPointed;
    private final long checkPointIntervalMillis;
    private final long backOffTimeMillis;
    private final String characterSet;
    private final CharsetDecoder decoder;
    private final StreamingDataProcessor streamingDataProcessor;
    private String shardId;

    private volatile long checkPointCount = 0L;
    private volatile int recordsNotCheckPointed = 0;
    private volatile long nextCheckpointTimeInMillis = 0L;

    public KinesisWorkerRecordProcessor(final ConsumerConfig consumerConfig,
                                        final StreamingDataProcessor streamingDataProcessor)
            throws NullPointerException, InvalidArgumentException {

        if (consumerConfig == null)
            throw new NullPointerException("consumerConfig");

        numRetries = consumerConfig.getNumRetries();
        maxRecordsNotCheckPointed = consumerConfig.getMaxRecordsNotCheckPointed();
        checkPointIntervalMillis = consumerConfig.getCheckPointIntervalMillis();
        backOffTimeMillis = consumerConfig.getBackOffTimeMillis();
        characterSet = consumerConfig.getCharacterSet();
        decoder = Charset.forName(characterSet).newDecoder();

        if (streamingDataProcessor == null)
            throw new NullPointerException("streamingDataProcessor");

        this.streamingDataProcessor = streamingDataProcessor;

        log.info("Created Kinesis record processor, numRetries=" + numRetries +
                " maxRecordsNotCheckPointed=" + maxRecordsNotCheckPointed +
                " checkPointIntervalMillis=" + checkPointIntervalMillis +
                " backOffTimeMillis=" + backOffTimeMillis +
                " characterSet=" + characterSet);
    }

    /**
     * Called by the Kinesis client lib to initialize the record processor of the shard.
     *
     * @param shardId Kinesis shard id
     */
    @Override
    public void initialize(final String shardId) {
        this.shardId = shardId;
        nextCheckpointTimeInMillis = System.currentTimeMillis() + checkPointIntervalMillis;
        log.info("Initialized record processor for shardId " + shardId);
    }

    /**
     * Called by the Kinesis client lib providing the list of <link>Record</link> received from
     * the Kinesis shard for further processing.
     *
     * @param records List of records received from the Kinesis shard
     * @param checkpointer Record check pointer
     */
    @Override
    public void processRecords(final List<Record> records, final IRecordProcessorCheckpointer checkpointer) {
        log.info("Processing " + records.size() + " records from " + shardId);
        for (Record record : records) {
            boolean processedSuccessfully = false;
            String data = null;
            for (int i = 0; i < numRetries; i++) {
                try {
                    if (log.isDebugEnabled()) {
                        data = decoder.decode(record.getData()).toString();
                        log.debug(record.getSequenceNumber() + ", " + record.getPartitionKey() + ", " + data);
                    }
                    streamingDataProcessor.process(record.getData().array());
                    recordsNotCheckPointed++;

                    processedSuccessfully = true;
                    break;
                } catch (CharacterCodingException e) {
                    log.error("Malformed data: '" + data + "'", e);
                    break;
                } catch (Throwable t) {
                    log.warn("Caught throwable while processing record " + record, t);
                }

                // backoff if we encounter an exception.
                try {
                    Thread.sleep(backOffTimeMillis);
                } catch (InterruptedException e) {
                    log.debug("Interrupted while backing off", e);
                }
            }

            if (!processedSuccessfully) {
                log.error("Couldn't process record '" + record + "' received from shard " + shardId + " skipping it");
            }
        }

        checkpoint(checkpointer, false);
    }

    /**
     * Inform the Amazon Kinesis client lib of how far this thread has progressed
     * in processing the records in the shard.
     *
     * No checkpointing will happen if less than <code>maxRecordsNotCheckPointed</code> have been processed
     * or if we are not at the <code>nextCheckpointTimeInMillis</code>.
     *
     * @param checkpointer
     * @param force If true immediately and unconditionally perform checkpointing.
     */
    private void checkpoint(final IRecordProcessorCheckpointer checkpointer, final boolean force) {
        if (skipCheckpoint(force)) {
            return;
        }

        for (int i = 0; i < numRetries; i++) {
            try {
                checkpointer.checkpoint();

                checkPointCount++;
                nextCheckpointTimeInMillis = System.currentTimeMillis() + checkPointIntervalMillis;
                log.info(shardId + " checkpoint# " + checkPointCount + " succeeded, after consuming " +
                         recordsNotCheckPointed + " records");
                recordsNotCheckPointed = 0;
                break;
            } catch (ShutdownException se) {
                // Ignore checkpoint if the processor instance has been shutdown (fail over).
                log.info("Caught shutdown exception, skipping checkpoint.", se);
                break;
            } catch (ThrottlingException e) {
                // Backoff and re-attempt checkpoint upon transient failures
                if (i >= (numRetries - 1)) {
                    log.error("Checkpoint failed after " + (i + 1) + "attempts.", e);
                    break;
                } else {
                    log.info("Transient issue when checkpointing, attempt " + (i + 1) + " of " + numRetries, e);
                }
            } catch (InvalidStateException e) {
                // This indicates an issue with the DynamoDB table (check for table, provisioned IOPS).
                log.error("Cannot save checkpoint to the DynamoDB table used by the Amazon Kinesis Client Library.", e);
                break;
            }
            try {
                Thread.sleep(backOffTimeMillis);
            } catch (InterruptedException e) {
                log.debug("Interrupted while backing off", e);
            }
        }
    }

    private boolean skipCheckpoint(final boolean force) {
        if (force)
            return false;

        if ((recordsNotCheckPointed >= maxRecordsNotCheckPointed) ||
            (System.currentTimeMillis() >= nextCheckpointTimeInMillis)) {

            return false;
        }

        return true;
    }

    /**
     * Called by the Kinesis client lib to shut down record processing on the shard
     * invoked by shutting down the shard (or the whole steam) from the management
     * console or the CLI.  The DynamoDB table corresponding to the unique application
     * name should be removed as well.
     *
     * @param checkpointer Record check pointer
     * @param reason Reason for shutting down the shard processing
     */
    @Override
    public void shutdown(final IRecordProcessorCheckpointer checkpointer, final ShutdownReason reason) {
        String msg = "Shutting down shard " + shardId + " record processor reason: ";
        switch (reason) {
            case TERMINATE:

                /*
                 * Must checkpoint after reaching end of shard, so we can start processing data from child shards.
                 */
                checkpoint(checkpointer, true);
                msg = msg + "TERMINATE";
                break;

            case ZOMBIE:
                msg = msg + "ZOMBIE";
                break;

            default:
                msg = msg + "UNKNOWN";
                break;
        }
        log.info(msg);
    }
}
