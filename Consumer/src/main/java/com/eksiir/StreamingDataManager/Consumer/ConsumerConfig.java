package com.eksiir.StreamingDataManager.Consumer;

import com.amazonaws.services.cloudfront.model.InvalidArgumentException;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;
import com.eksiir.StreamingDataManager.Common.Config;
import com.eksiir.StreamingDataManager.Common.Model.StreamingDataConsumer;
import com.eksiir.StreamingDataManager.Common.Model.StreamingDataManagerConstants;
import com.eksiir.StreamingDataManager.Common.Model.StreamingDataModel;
import com.eksiir.StreamingDataManager.Common.Utils;
import org.apache.commons.configuration.XMLConfiguration;

/**
 * Consumer configuration and properties.
 *
 * XMLConfiguration.setSchemaValidation(true) is set.  i.e ALL the xml level validations are delegated to XSD.
 * That includes checking for mandatory key,value pairs presence.
 *
 * Created by bbehzadi on 1/28/14.
 */
public class ConsumerConfig extends Config {
    private StreamingDataModel streamingDataModel;
    private StreamingDataConsumer consumerProcessor;
    private String uniqAppName;
    private String redshiftCredentialsFileName;
    private String redshiftTableName;
    private String kinesisConnectorType;
    private String kinesisConnectorS3Bucket;
    private Character streamingDataDelimiter;
    private InitialPositionInStream initialPositionInStream;
    private int numRetries = -1;
    private int maxRecordsNotCheckPointed = -1;
    private long checkPointIntervalMillis = -1L;
    private long backOffTimeMillis = -1L;
    private String minRecordsPerBuffer;
    private String minBytesPerBuffer;
    private String characterSet;
    private String s3EndPoint;
    private String redshiftEndPoint;
    private String redshiftUrl;
    private String redshiftUserName;
    private String redshiftPassword;

    public ConsumerConfig(final XMLConfiguration config) {
        super(config);
    }

    /**
     * @return The configured streaming data model.
     */
    public StreamingDataModel getStreamingDataModel() {
        if (streamingDataModel != null)
            return streamingDataModel;

        final String streamingDataModelProp = config.getString(StreamingDataManagerConstants.STREAMING_DATA_MODEL);
        streamingDataModel = Utils.getEnumFromStringValue(StreamingDataModel.class, streamingDataModelProp);
        return streamingDataModel;
    }

    /**
     * @return The configured streaming data delimiter or the default.
     */
    public Character getStreamingDataDelimiter() {
        if (streamingDataDelimiter != null)
            return streamingDataDelimiter;

        streamingDataDelimiter = config.getString(StreamingDataManagerConstants.STREAMING_DATA_DELIMITER).charAt(0);
        return streamingDataDelimiter;
    }

    /**
     * @return The configured streaming data processor.
     */
    public StreamingDataConsumer getStreamingConsumerProcessor() {
        if (consumerProcessor != null)
            return consumerProcessor;

        final String consumerProcessorProp = config.getString(StreamingDataManagerConstants
                .STREAMING_DATA_CONSUMER_PROCESSOR);
        consumerProcessor = Utils.getEnumFromStringValue(StreamingDataConsumer.class, consumerProcessorProp);
        return consumerProcessor;
    }

    /**
     * @return The configured unique consumer application name.
     */
    public String getUniqAppName() {
        if (uniqAppName != null)
            return uniqAppName;

        uniqAppName = config.getString(StreamingDataManagerConstants.KINESIS_CONSUMER_UNIQ_APP_NAME);
        return uniqAppName;
    }

    /**
     * @return The configured initial position in the stream or the default.
     */
    public InitialPositionInStream getInitialPositionInStream() {
        if (initialPositionInStream != null)
            return initialPositionInStream;

        final String initPosInStream = config.getString(StreamingDataManagerConstants.KINESIS_CONSUMER_INIT_POSITION_IN_STREAM);
        initialPositionInStream = Utils.getEnumFromStringValue(InitialPositionInStream.class, initPosInStream);
        return initialPositionInStream;
    }

    /**
     * @return The configured number of record processor retries or the default.
     *
     * @throws InvalidArgumentException
     */
    public int getNumRetries() throws InvalidArgumentException {
        if (numRetries > 0)
            return numRetries;

        numRetries = config.getInt(StreamingDataManagerConstants.KINESIS_CONSUMER_NUM_RETRIES);
        if (numRetries <= 0)
            throw new InvalidArgumentException(StreamingDataManagerConstants.KINESIS_CONSUMER_NUM_RETRIES + "=" + numRetries);

        return numRetries;
    }

    /**
     * @return The configured max number of records not checkpointed or the default.
     *
     * @throws InvalidArgumentException
     */
    public int getMaxRecordsNotCheckPointed() throws InvalidArgumentException {
        if (maxRecordsNotCheckPointed > 0)
            return maxRecordsNotCheckPointed;

        maxRecordsNotCheckPointed = config.getInt(StreamingDataManagerConstants.KINESIS_CONSUMER_MAX_RECORDS_NOT_CHECK_POINTED);
        if (maxRecordsNotCheckPointed <= 0)
            throw new InvalidArgumentException(StreamingDataManagerConstants.KINESIS_CONSUMER_MAX_RECORDS_NOT_CHECK_POINTED +
                                               "=" + maxRecordsNotCheckPointed);
        return maxRecordsNotCheckPointed;
    }

    /**
     * @return The configured checkpoint interval in milliseconds or the default.
     *
     * @throws InvalidArgumentException
     */
    public long getCheckPointIntervalMillis() throws InvalidArgumentException {
        if (checkPointIntervalMillis > 0L)
            return checkPointIntervalMillis;

        checkPointIntervalMillis = config.getLong(StreamingDataManagerConstants
                .KINESIS_CONSUMER_CHECKPOINT_INTERVAL_MILLIS);
        if (checkPointIntervalMillis <= 0L)
            throw new InvalidArgumentException(StreamingDataManagerConstants.KINESIS_CONSUMER_CHECKPOINT_INTERVAL_MILLIS +
                                               "=" + checkPointIntervalMillis);
        return checkPointIntervalMillis;
    }

    /**
     * @return The configured record processor back off time in milliseconds or the default.
     *
     * @throws InvalidArgumentException
     */
    public long getBackOffTimeMillis() throws InvalidArgumentException {
        if (backOffTimeMillis > 0L)
            return backOffTimeMillis;

        backOffTimeMillis = config.getLong(StreamingDataManagerConstants.KINESIS_CONSUMER_BACK_OFF_TIME_MILLIS);
        if (backOffTimeMillis <= 0L)
            throw new InvalidArgumentException(StreamingDataManagerConstants.KINESIS_CONSUMER_BACK_OFF_TIME_MILLIS +
                                               "=" + backOffTimeMillis);
        return backOffTimeMillis;
    }

    /**
     * @return The configured min record count per carrier buffer or the default.
     *
     * @throws InvalidArgumentException
     */
    public String getMinRecordsPerBuffer() throws InvalidArgumentException {
        if (minRecordsPerBuffer != null)
            return minRecordsPerBuffer;

        Long minRecPerBuff = config.getLong(StreamingDataManagerConstants.KINESIS_CONSUMER_MIN_RECORDS_PER_BUFFER);
        if (minRecPerBuff <= 0L)
            throw new InvalidArgumentException(StreamingDataManagerConstants.KINESIS_CONSUMER_MIN_RECORDS_PER_BUFFER +
                                               "=" + minRecPerBuff);
        minRecordsPerBuffer = minRecPerBuff.toString();
        return minRecordsPerBuffer;
    }

    /**
     * @return The configured min bytes per carrier buffer or the default.
     *
     * @throws InvalidArgumentException
     */
    public String getMinBytesPerBuffer() throws InvalidArgumentException {
        if (minBytesPerBuffer != null)
            return minBytesPerBuffer;

        Long minBytesPerBuf = config.getLong(StreamingDataManagerConstants.KINESIS_CONSUMER_MIN_BYTES_PER_BUFFER);
        if (minBytesPerBuf <= 0L)
            throw new InvalidArgumentException(StreamingDataManagerConstants.KINESIS_CONSUMER_MIN_BYTES_PER_BUFFER +
                                               "=" + minBytesPerBuf);
        minBytesPerBuffer = minBytesPerBuf.toString();
        return minBytesPerBuffer;
    }

    /**
     * @return The configured character set or the default.
     */
    public String getCharacterSet() {
        if (characterSet != null)
            return characterSet;

        characterSet = config.getString(StreamingDataManagerConstants.KINESIS_CONSUMER_CHARACTER_SET);
        return characterSet;
    }

    /**
     * @return The configured AWS S3 endpoint or the default.
     */
    public String getS3EndPoint() {
        if (s3EndPoint != null)
            return s3EndPoint;

        s3EndPoint = config.getString(StreamingDataManagerConstants.S3_END_POINT);
        return s3EndPoint;
    }

    /**
     * @return The configured AWS Redshift endpoint or the default.
     */
    public String getRedshiftEndPoint() {
        if (redshiftEndPoint != null)
            return redshiftEndPoint;

        redshiftEndPoint = config.getString(StreamingDataManagerConstants.REDSHIFT_END_POINT);
        return redshiftEndPoint;
    }

    /**
     * @return The configured Redshift credentials file name.
     */
    public String getRedshiftCredentialsPropertiesFileName() {
        if (redshiftCredentialsFileName != null)
            return redshiftCredentialsFileName;

        redshiftCredentialsFileName = config.getString(StreamingDataManagerConstants.REDSHIFT_CREDENTIALS_FILE_NAME);
        return redshiftCredentialsFileName;
    }

    /**
     * @return The configured Redshift URL.
     */
    public String getRedshiftUrl() {
        if (redshiftUrl != null)
            return redshiftUrl;

        redshiftUrl = config.getString(StreamingDataManagerConstants.REDSHIFT_URL);
        return redshiftUrl;
    }

    /**
     * @return The configured Redshift user name.
     */
    public String getRedshiftUserName() {
        if (redshiftUserName != null)
            return redshiftUserName;

        redshiftUserName = config.getString(StreamingDataManagerConstants.REDSHIFT_USERNAME);
        return redshiftUserName;
    }

    /**
     * @return The configured Redshift user password.
     */
    public String getRedshiftPassword() {
        if (redshiftPassword != null)
            return redshiftPassword;

        redshiftPassword = config.getString(StreamingDataManagerConstants.REDSHIFT_PASSWORD);
        return redshiftPassword;

    }

    /**
     * @return The configured Redshift table name.
     */
    public String getRedshiftTableName() {
        if (redshiftTableName != null)
            return redshiftTableName;

        redshiftTableName = config.getString(StreamingDataManagerConstants.REDSHIFT_TABLE_NAME);
        return redshiftTableName;
    }

    /**
     * @return The configured Kinesis connector type.
     */
    public String getKinesisConnectorType() {
        if (kinesisConnectorType != null)
            return kinesisConnectorType;

        kinesisConnectorType = config.getString(StreamingDataManagerConstants.KINESIS_CONNECTOR_TYPE);
        return kinesisConnectorType;
    }

    /**
     * @return The configured S3 bucket for Kinesis connector.
     */
    public String getKinesisConnectorS3Bucket() {
        if (kinesisConnectorS3Bucket != null)
            return kinesisConnectorS3Bucket;

        kinesisConnectorS3Bucket = config.getString(StreamingDataManagerConstants.KINESIS_CONNECTOR_S3BUCKET);
        return kinesisConnectorS3Bucket;
    }
}
