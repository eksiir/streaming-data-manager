package com.eksiir.StreamingDataManager.Consumer.StreamingDataProcessor;

import com.eksiir.StreamingDataManager.Common.Model.StreamingDataManagerConstants;
import org.junit.Test;

import java.util.Properties;

/**
 * Test the ConsumerConfig.
 */
public class TestConsumerConfig {

    @Test
    public void testConsumerConfig() {
        // TODO use consumer config
//        ConsumerConfig consumerConfig = new ConsumerConfig();
//        ConsumerConfig.saveConfig(getProperties());
//
//        assertEquals(ConsumerConfig.getBackOffTimeMillis(),3000);
//        assertEquals(ConsumerConfig.getCharacterSet(),"UTF-8");
//        assertEquals(ConsumerConfig.getCheckPointIntervalMillis(), 60000);
//        assertEquals(ConsumerConfig.getInitialPositionInStream(),InitialPositionInStream.LATEST);
//        assertEquals(ConsumerConfig.getKinesisConnectorS3Bucket(), "streaming-data");
//        assertEquals(ConsumerConfig.getMaxRecordsNotCheckPointed(), 1000);
//        assertEquals(ConsumerConfig.getMinBytesPerBuffer(), "1048576");
//        assertEquals(ConsumerConfig.getNumRetries(), 10);
//        assertEquals(ConsumerConfig.getRedshiftCredentialsPropertiesFileName(), "RedshiftCredentials.properties");
//        assertEquals(ConsumerConfig.getRedshiftEndPoint(), "https://redshift.us-east-1.amazonaws.com");
//        assertEquals(ConsumerConfig.getRedshiftTableName(), "kinesis_test");
//        assertEquals(ConsumerConfig.getS3EndPoint(), "https://s3.amazonaws.com");
//        assertEquals(ConsumerConfig.getStreamingConsumerProcessor(), StreamingDataConsumer.CARRIER_OUT);
//        assertEquals(ConsumerConfig.getStreamingDataDelimiter(),new Character('!'));
//        assertEquals(ConsumerConfig.getStreamingDataModel(), StreamingDataModel.ORDER);
//        assertEquals(ConsumerConfig.getUniqAppName(), "Orders-CARRIER_OUT-LATEST");
//        assertEquals(ConsumerConfig.getKinesisAwsRegion(), "us-east-1");
//        assertEquals(ConsumerConfig.getKinesisCredentialsFileName(), "AwsCredentials.properties");
//        assertEquals(ConsumerConfig.getKinesisEndPoint(), "https://kinesis.us-east-1.amazonaws.com");
//        assertEquals(ConsumerConfig.getKinesisStreamName(), "Orders");
//        assertEquals(ConsumerConfig.getStreamingDataCarrier(), StreamingDataCarrier.KINESIS);
    }

    private Properties getProperties() {
        String streamName = "Orders";
        String rtProc = "CARRIER_OUT";
        String initPositionInStream = "LATEST";

        Properties props = new Properties();
        props.put(StreamingDataManagerConstants.STREAMING_DATA_MODEL, "ORDER");
        props.put(StreamingDataManagerConstants.STREAMING_DATA_DELIMITER, "!");
        props.put(StreamingDataManagerConstants.STREAMING_DATA_CONSUMER_PROCESSOR, rtProc);
        props.put(StreamingDataManagerConstants.KINESIS_STREAM_NAME, streamName);
        props.put(StreamingDataManagerConstants.KINESIS_CREDENTIALS_PROPERTIES_FILE_NAME, "AwsCredentials.properties");
        props.put(StreamingDataManagerConstants.KINESIS_CONSUMER_INIT_POSITION_IN_STREAM, initPositionInStream);
        props.put(StreamingDataManagerConstants.KINESIS_CONSUMER_MIN_RECORDS_PER_BUFFER, "1");
        props.put(StreamingDataManagerConstants.KINESIS_CONSUMER_UNIQ_APP_NAME,
                streamName + "-" + rtProc + "-" + initPositionInStream);
        props.put(StreamingDataManagerConstants.KINESIS_CONNECTOR_TYPE, "Redshift");
        props.put(StreamingDataManagerConstants.KINESIS_CONNECTOR_S3BUCKET, "streaming-data");
        props.put(StreamingDataManagerConstants.REDSHIFT_CREDENTIALS_FILE_NAME, "RedshiftCredentials.properties");
        props.put(StreamingDataManagerConstants.REDSHIFT_TABLE_NAME, "kinesis_test");
        return props;
    }
}
