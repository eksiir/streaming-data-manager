package com.eksiir.StreamingDataManager.Producer.CarrierPublisher.Kinesis;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.kinesis.AmazonKinesisClient;
import com.amazonaws.services.kinesis.model.DescribeStreamResult;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.amazonaws.services.kinesis.model.PutRecordResult;
import com.eksiir.StreamingDataManager.Producer.CarrierPublisher.CarrierPublisher;
import com.eksiir.StreamingDataManager.Producer.ProducerConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.ServiceUnavailableException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Output of <link>ProducerController</link>.
 * Publish data to AWS Kinesis.
 *
 * It assumes that the stream is already created.
 *
 * Created by bbehzadi on 1/9/14.
 */
public class KinesisPublisher implements CarrierPublisher {
    private final Log log = LogFactory.getLog(KinesisPublisher.class);
    private final ProducerConfig producerConfig;
    private final AmazonKinesisClient client;
    private final PutRecordRequest putRecordRequest;
    private final String basePartitionName;
    private volatile long publishedRecordsCount = 0L;
    private final static String KINESIS_SERVICE = "kinesis";
    private final static int numRecordCountToReport = 100;

    public KinesisPublisher(final ProducerConfig producerConfig) throws Exception {
        if (producerConfig == null)
            throw new NullPointerException("producerConfig");

        this.producerConfig = producerConfig;
        final String streamName = producerConfig.getKinesisStreamName();
        client = createAmazonKinesisClient(streamName);                 // all its methods are blocking
        basePartitionName = createBasePartitionName(streamName);
        putRecordRequest = createPutRecordRequest(streamName);
    }

    /**
     * Checks all the properties needed to create an AWS Kinesis client and validates the given stream
     * to be in ACTIVE status before it returns.
     *
     * @param streamName name of the stream
     * @return AWS Kinesis client object - all its methods are blocking
     * @throws IOException, ServiceUnavailableException
     */
    private AmazonKinesisClient createAmazonKinesisClient(final String streamName)
            throws IOException, ServiceUnavailableException {

        final String awsRegion = producerConfig.getKinesisAwsRegion();
        final String kinesisEndPoint = producerConfig.getKinesisEndPoint();
        final String credentialsPropertiesFileName = producerConfig.getKinesisCredentialsFileName();
        final InputStream credentialsPropertiesInputStream =
                getClass().getClassLoader().getResourceAsStream(credentialsPropertiesFileName);
        final AWSCredentials credentials = new PropertiesCredentials(credentialsPropertiesInputStream);
        final ClientConfiguration configuration = new ClientConfiguration();    // TODO for now taking all defaults

        final AmazonKinesisClient kinesisClient = new AmazonKinesisClient(credentials, configuration);
        kinesisClient.setEndpoint(kinesisEndPoint, KINESIS_SERVICE, awsRegion);

        checkStreamActiveStatus(kinesisClient, streamName);
        return kinesisClient;
    }

    /**
     * Throw exception if the given stream is not in ACTIVE state.
     *
     * @param client the streams AmazonKinesisClient
     * @param streamName name of the stream
     * @throws NullPointerException, ServiceUnavailableException
     */
    private void checkStreamActiveStatus(final AmazonKinesisClient client, final String streamName)
            throws NullPointerException, ServiceUnavailableException {

        DescribeStreamResult describeStreamResult = client.describeStream(streamName);
        if (describeStreamResult == null)
            throw new NullPointerException("describeStreamResult");

        String streamStatus = describeStreamResult.getStreamDescription().getStreamStatus();
        if (!streamStatus.equals("ACTIVE"))
            throw new ServiceUnavailableException(streamName + " stream status (" + streamStatus + ") not ACTIVE");

        log.info(describeStreamResult.toString());
    }

    /**
     * Changing the way partition names are assigned changes the way records are distributed across the shards.
     *
     * @param streamName
     * @return base name of the partition key which can be further made unique e.g. by appending the record count.
     */
    private String createBasePartitionName(final String streamName) {
        return streamName + "-Partition-";
    }

    /**
     * Create a <link>PutRecordRequest</link> object which will be recycled for every record.
     *
     * @param streamName
     * @return A prepared <link>PutRecordRequest</link> object to be completed per record needs.
     */
    private PutRecordRequest createPutRecordRequest(final String streamName) {
        PutRecordRequest putRequest = new PutRecordRequest();
        putRequest.setStreamName(streamName);
        putRequest.setSequenceNumberForOrdering("0");      // initial seq#
        return putRequest;
    }

    private long getPublishedRecordsCount() {
        return publishedRecordsCount;
    }

    /**
     * Publish the given data to Kinesis which will be available for only 24 hours for consumption.
     *
     * @param data byte stream to be published
     */
    @Override
    public void publish(final byte[] data) {
        putRecordRequest.setPartitionKey(basePartitionName + String.valueOf(publishedRecordsCount));
        putRecordRequest.setData(ByteBuffer.wrap(data));

        try {
            PutRecordResult result = client.putRecord(putRecordRequest);
            if (result == null)
                throw new NullPointerException("result");

            publishedRecordsCount++;
            log.debug("Record# " + publishedRecordsCount + " " + result.getShardId() + " seq# " + result.getSequenceNumber());
            if ((publishedRecordsCount % numRecordCountToReport) == 0) {
                log.info(report());
            }
        } catch (Exception e) {
            log.error("Failed to publish", e);
        }
    }

    /**
     * Report info and metrics on the streaming data.
     *
     * @return the reported info and metrics
     */
    @Override
    public String report() {
        return getPublishedRecordsCount() + " records published to Kinesis.";
    }
}
