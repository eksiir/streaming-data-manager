package com.eksiir.StreamingDataManager.Producer;

import com.eksiir.StreamingDataManager.Common.Model.StreamingDataManagerConstants;
import org.junit.Test;

import java.util.Properties;

/**
 * Tests the producer config.
 */
public class TestProducerConfig {

    @Test
    public void testProducerConfig() {
        // TODO use producer config
//        ProducerConfig config = new ProducerConfig();
//        ProducerConfig.saveConfig(getProperties());
//
//        assertEquals(ProducerConfig.getStreamingDataCarrier(), StreamingDataCarrier.KINESIS);
//        assertEquals(ProducerConfig.getKinesisStreamName(), "Orders");
//        assertEquals(ProducerConfig.getKinesisEndPoint(), "https://kinesis.us-east-1.amazonaws.com");
//        assertEquals(ProducerConfig.getKinesisCredentialsFileName(), "AwsCredentials.properties");
//        assertEquals(ProducerConfig.getLogTailerFileName(), "/tmp/orders.log");
//        assertEquals(ProducerConfig.getLogTailerMillisDelay(), 0);
//        assertEquals(ProducerConfig.getStreamingDataSource(), StreamingDataSource.LOG_FILE);
//        assertEquals(ProducerConfig.getKinesisAwsRegion(),"us-east-1");
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.put(StreamingDataManagerConstants.KINESIS_STREAM_NAME, "Orders");
        props.put(StreamingDataManagerConstants.LOG_TAILER_FILE_NAME, "/tmp/orders.log");
        props.put(StreamingDataManagerConstants.KINESIS_CREDENTIALS_PROPERTIES_FILE_NAME, "AwsCredentials.properties");
        return props;
    }

}
