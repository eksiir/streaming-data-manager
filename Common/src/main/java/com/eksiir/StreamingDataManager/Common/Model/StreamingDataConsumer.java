package com.eksiir.StreamingDataManager.Common.Model;

/**
 * The consumers of the streaming data.
 *
 * Created by bbehzadi on 1/9/14.
 */
public enum StreamingDataConsumer {
    APPLICATION,                // analytics application
    CARRIER_OUT,                // immediate output from carrier
    JDBC_CONNECTION,
    KINESIS_DYNAMODB_CONNECTOR,
    KINESIS_REDSHIFT_CONNECTOR,
    KINESIS_S3_CONNECTOR,
    MAP_REDUCE,                 // e.g. AWS EMR
    ROLLING_LOG_FILE,
    SPARK;

    /**
     * @param consumer
     * @return true only if it is a Kinesis connector
     */
    public static boolean isKinesisConnector(StreamingDataConsumer consumer) {
        switch (consumer) {
            case KINESIS_DYNAMODB_CONNECTOR:
            case KINESIS_REDSHIFT_CONNECTOR:
            case KINESIS_S3_CONNECTOR:
                return true;
        }
        return false;
    }
}
