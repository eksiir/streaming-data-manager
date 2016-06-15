package com.eksiir.StreamingDataManager.Consumer.CarrierListener.Kinesis;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;
import com.eksiir.StreamingDataManager.Common.Utils;
import com.eksiir.StreamingDataManager.Consumer.CarrierListener.CarrierListener;
import com.eksiir.StreamingDataManager.Consumer.ConsumerConfig;
import com.eksiir.StreamingDataManager.Consumer.StreamingDataProcessor.StreamingDataProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * listens for data from Kinesis shards and pushes them to the streaming data processor.
 *
 * Created by bbehzadi on 1/27/14.
 */
public class KinesisWorker implements CarrierListener {
    private final Log log = LogFactory.getLog(KinesisListenerFactory.class);
    private final ConsumerConfig consumerConfig;
    private final Worker worker;

    public KinesisWorker(final StreamingDataProcessor rtProcessor, final ConsumerConfig consumerConfig)
    throws UnknownHostException {

        if (consumerConfig == null)
            throw new NullPointerException("consumerConfig");

        this.consumerConfig = consumerConfig;
        final KinesisClientLibConfiguration clientLibConfig = createKinesisClientLibConfiguration();

        final KinesisWorkerRecordProcessorFactory recordProcessorFactory = new KinesisWorkerRecordProcessorFactory();
        recordProcessorFactory.setConsumerConfig(consumerConfig);
        recordProcessorFactory.setStreamingDataProcessor(rtProcessor);

        worker = new Worker(recordProcessorFactory, clientLibConfig);
    }

    private KinesisClientLibConfiguration createKinesisClientLibConfiguration() throws UnknownHostException {
        final String streamName = consumerConfig.getKinesisStreamName();
        final String uniqAppName = consumerConfig.getUniqAppName();
        final InitialPositionInStream initialPositionInStream = consumerConfig.getInitialPositionInStream();

        final String credentialsPropertiesFileName = consumerConfig.getKinesisCredentialsFileName();
        AWSCredentialsProvider credentialsProvider = Utils.getAWSCredentialsProvider(credentialsPropertiesFileName);

        final String workerId = InetAddress.getLocalHost().getCanonicalHostName() + ":" + UUID.randomUUID();
        log.info("Using workerId: " + workerId);

        final KinesisClientLibConfiguration clientLibConf =
                new KinesisClientLibConfiguration(uniqAppName, streamName, credentialsProvider, workerId);

        clientLibConf.withInitialPositionInStream(initialPositionInStream);
        return clientLibConf;
    }

    /**
     * Start getting the streaming data from Kinesis and publish them to the streaming data processor.
     */
    @Override
    public void start() {
        log.info(worker.getApplicationName() + " application starting");
        worker.run();
    }

    /**
     * Stop getting the streaming data from Kinesis.
     */
    @Override
    public void stop() {
        log.info("stop TODO");          // TODO
    }

    /**
     * Report info and metrics on the streaming data.
     *
     * @return the reported info and metrics
     */
    @Override
    public String report() {
        return "report TODO";           // TODO
    }
}

