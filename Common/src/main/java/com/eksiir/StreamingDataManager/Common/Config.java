package com.eksiir.StreamingDataManager.Common;

import com.eksiir.StreamingDataManager.Common.Model.StreamingDataCarrier;
import com.eksiir.StreamingDataManager.Common.Model.StreamingDataManagerConstants;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

import java.util.Iterator;

/**
 * Common configuration and property management.
 *
 * XMLConfiguration.setSchemaValidation(true) is set.  i.e ALL the xml level validations are delegated to XSD.
 * That includes checking for mandatory key,value pairs presence.
 *
 * Created by bbehzadi on 1/28/14.
 */
public class Config {
    protected final XMLConfiguration config;
    private StreamingDataCarrier streamingDataCarrier;
    private String kinesisAwsRegion;
    private String kinesisEndPoint;
    private String kinesisCredentialsFileName;
    private String kinesisStreamName;

    protected Config(final XMLConfiguration config) {
        this.config = config;
    }

    /**
     * Adds the given properties to the existing XMLConfiguration.
     *
     * @param properties to be added
     */
    public void addProperties(PropertiesConfiguration properties) {
        for (Iterator<String> keyIterator = properties.getKeys(); keyIterator.hasNext();) {
            String key = keyIterator.next();
            Object value = properties.getProperty(key);
            config.setProperty(key, value);
        }
    }

    /**
     * @return The configured streaming data carrier or the default.
     */
    public StreamingDataCarrier getStreamingDataCarrier() {
        if (streamingDataCarrier != null)
            return streamingDataCarrier;

        final String streamingDataCarrierName = config.getString(StreamingDataManagerConstants.STREAMING_DATA_CARRIER);
        streamingDataCarrier = Utils.getEnumFromStringValue(StreamingDataCarrier.class, streamingDataCarrierName);
        return streamingDataCarrier;
    }

    /**
     * @return The configured Kinesis AWS region or the default.
     */
    public String getKinesisAwsRegion() {
        if (kinesisAwsRegion != null)
            return kinesisAwsRegion;

        kinesisAwsRegion = config.getString(StreamingDataManagerConstants.KINESIS_REGION);
        return kinesisAwsRegion;
    }

    /**
     * @return The configured AWS Kinesis endpoint or the default.
     */
    public String getKinesisEndPoint() {
        if (kinesisEndPoint != null)
            return kinesisEndPoint;

        kinesisEndPoint = config.getString(StreamingDataManagerConstants.KINESIS_END_POINT);
        return kinesisEndPoint;
    }

    /**
     * @return The configured Kinesis credentials property filename or the default.
     */
    public String getKinesisCredentialsFileName() {
        if (kinesisCredentialsFileName != null)
            return kinesisCredentialsFileName;

        kinesisCredentialsFileName = config.getString(StreamingDataManagerConstants.KINESIS_CREDENTIALS_PROPERTIES_FILE_NAME);
        return kinesisCredentialsFileName;
    }

    /**
     * @return The configured Kinesis stream name.
     */
    public String getKinesisStreamName() {
        if (kinesisStreamName != null)
            return kinesisStreamName;

        kinesisStreamName = config.getString(StreamingDataManagerConstants.KINESIS_STREAM_NAME);
        return kinesisStreamName;
    }
}
