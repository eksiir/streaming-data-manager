package com.eksiir.StreamingDataManager.Common.Model;

/**
 * Constants used to manage the streaming data.
 * e.g. Property key names.
 *
 * Created by bbehzadi on 1/13/14.
 */
public class StreamingDataManagerConstants {
    public static final String STREAMING_DATA_SOURCE = "StreamingData.Source";
    public static final String STREAMING_DATA_CARRIER = "StreamingData.Carrier";
    public static final String STREAMING_DATA_MODEL = "StreamingData.Model";
    public static final String STREAMING_DATA_DELIMITER = "StreamingData.Delimiter";
    public static final String STREAMING_DATA_CONSUMER_PROCESSOR = "StreamingData.ConsumerProcessor";

    public static final String LOG_TAILER_FILE_NAME = "LogTailer.Filename";
    public static final String LOG_TAILER_MILLIS_DELAY = "LogTailer.MillisecDelay";

    public static final String KINESIS_REGION = "Kinesis.Region";
    public static final String KINESIS_END_POINT = "Kinesis.EndPoint";
    public static final String KINESIS_CREDENTIALS_PROPERTIES_FILE_NAME = "Kinesis.CredentialsPropertiesFileName";
    public static final String KINESIS_STREAM_NAME = "Kinesis.StreamName";
    public static final String KINESIS_CONSUMER_UNIQ_APP_NAME = "Kinesis.Consumer.UniqueApplicationName";
    public static final String KINESIS_CONSUMER_INIT_POSITION_IN_STREAM = "Kinesis.Consumer.InitialPositionInStream";
    public static final String KINESIS_CONSUMER_NUM_RETRIES = "Kinesis.Consumer.NumRetries";
    public static final String KINESIS_CONSUMER_MAX_RECORDS_NOT_CHECK_POINTED = "Kinesis.Consumer.MaxRecordsNotCheckPointed";
    public static final String KINESIS_CONSUMER_CHECKPOINT_INTERVAL_MILLIS = "Kinesis.Consumer.CheckpointIntervalMillis";
    public static final String KINESIS_CONSUMER_BACK_OFF_TIME_MILLIS = "Kinesis.Consumer.BackOffTimeMillis";
    public static final String KINESIS_CONSUMER_CHARACTER_SET = "Kinesis.Consumer.CharacterSet";
    public static final String KINESIS_CONSUMER_MIN_RECORDS_PER_BUFFER = "Kinesis.Consumer.MinRecordsPerBuffer";
    public static final String KINESIS_CONSUMER_MIN_BYTES_PER_BUFFER = "Kinesis.Consumer.MinBytesPerBuffer";

    public static final String KINESIS_CONNECTOR_TYPE = "Kinesis.Connector.Type";
    public static final String KINESIS_CONNECTOR_S3BUCKET = "Kinesis.Connector.S3Bucket";

    public static final String REDSHIFT_END_POINT = "Redshift.EndPoint";
    public static final String REDSHIFT_CREDENTIALS_FILE_NAME = "Redshift.CredentialsFileName";
    public static final String REDSHIFT_URL = "redshiftURL";
    public static final String REDSHIFT_USERNAME = "redshiftUsername";
    public static final String REDSHIFT_PASSWORD = "redshiftPassword";
    public static final String REDSHIFT_TABLE_NAME = "Redshift.TableName";

    public static final String S3_END_POINT = "S3.EndPoint";
}
