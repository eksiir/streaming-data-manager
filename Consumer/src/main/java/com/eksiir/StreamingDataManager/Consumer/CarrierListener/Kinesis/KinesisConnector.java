package com.eksiir.StreamingDataManager.Consumer.CarrierListener.Kinesis;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.cloudsearch.model.InvalidTypeException;
import com.amazonaws.services.kinesis.connectors.KinesisConnectorConfiguration;
import com.amazonaws.services.kinesis.connectors.KinesisConnectorExecutorBase;
import com.amazonaws.services.kinesis.connectors.KinesisConnectorRecordProcessorFactory;
import com.amazonaws.services.kinesis.connectors.interfaces.IKinesisConnectorPipeline;
import com.eksiir.StreamingDataManager.Common.Model.DataModel;
import com.eksiir.StreamingDataManager.Common.Utils;
import com.eksiir.StreamingDataManager.Consumer.CarrierListener.CarrierListener;
import com.eksiir.StreamingDataManager.Consumer.ConsumerConfig;
import com.eksiir.StreamingDataManager.Consumer.StreamingDataProcessor.StreamingDataProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.UUID;

/**
 * Input of <link>ConsumerController</link>. Connects Kinesis to various streaming data pipeline processors based on
 * the streaming data model.
 *
 * @param <T> streaming data model
 *
 * Created by bbehzadi on 1/27/14.
 */
public class KinesisConnector<T extends DataModel> extends KinesisConnectorExecutorBase<T, byte[]> implements CarrierListener {
    private final Log log = LogFactory.getLog(KinesisConnector.class);
    private final ConsumerConfig consumerConfig;
    private final IKinesisConnectorPipeline<T, byte[]> pipeline;
    private final KinesisConnectorConfiguration kinesisConnectorConfiguration;

    public KinesisConnector(final StreamingDataProcessor streamingDataProcessor, final ConsumerConfig consumerConfig)
            throws NullPointerException, InvalidTypeException, UnknownHostException {

        if (consumerConfig == null)
            throw new NullPointerException("config");

        this.consumerConfig = consumerConfig;
        pipeline = (IKinesisConnectorPipeline<T, byte[]>) streamingDataProcessor;
        if (!pipeline.getClass().isAssignableFrom(streamingDataProcessor.getClass()))
            throw new InvalidTypeException("streamingDataProcessor " + streamingDataProcessor.getClass().getSimpleName());

        final String credentialsPropertiesFileName = consumerConfig.getKinesisCredentialsFileName();
        final AWSCredentialsProvider credentialsProvider = Utils.getAWSCredentialsProvider(credentialsPropertiesFileName);

        final String workerId = InetAddress.getLocalHost().getCanonicalHostName() + ":" + UUID.randomUUID();
        final Properties properties = getKinesisConnectorProperties(workerId);
        kinesisConnectorConfiguration = new KinesisConnectorConfiguration(properties, credentialsProvider);

        super.initialize(kinesisConnectorConfiguration);
        log.info("Initialized with streaming data processor " + pipeline.getClass().getSimpleName());
    }

    private Properties getKinesisConnectorProperties(String workerId) {
        Properties props = new Properties();
        props.put(KinesisConnectorConfiguration.PROP_KINESIS_ENDPOINT, consumerConfig.getKinesisEndPoint());
        props.put(KinesisConnectorConfiguration.PROP_APP_NAME, consumerConfig.getUniqAppName());

        String streamName = consumerConfig.getKinesisStreamName();
        props.put(KinesisConnectorConfiguration.PROP_KINESIS_INPUT_STREAM, streamName);
        props.put(KinesisConnectorConfiguration.PROP_BUFFER_RECORD_COUNT_LIMIT, consumerConfig.getMinRecordsPerBuffer());

        props.put(KinesisConnectorConfiguration.PROP_REDSHIFT_ENDPOINT, consumerConfig.getRedshiftEndPoint());
        props.put(KinesisConnectorConfiguration.PROP_REDSHIFT_DATA_TABLE, consumerConfig.getRedshiftTableName());
        props.put(KinesisConnectorConfiguration.PROP_REDSHIFT_DATA_DELIMITER, consumerConfig.getStreamingDataDelimiter());
        props.put(KinesisConnectorConfiguration.PROP_REDSHIFT_URL, consumerConfig.getRedshiftUrl());
        props.put(KinesisConnectorConfiguration.PROP_REDSHIFT_USERNAME, consumerConfig.getRedshiftUserName());
        props.put(KinesisConnectorConfiguration.PROP_REDSHIFT_PASSWORD, consumerConfig.getRedshiftPassword());

        props.put(KinesisConnectorConfiguration.PROP_S3_ENDPOINT, consumerConfig.getS3EndPoint());
        props.put(KinesisConnectorConfiguration.PROP_S3_BUCKET, consumerConfig.getKinesisConnectorS3Bucket());

        props.put(KinesisConnectorConfiguration.PROP_WORKER_ID, workerId);

        return props;
    }

    /**
     * Each connector depends on the implementation of <link>KinesisConnectorRecordProcessor</link>
     * to manage the pipeline.
     *
     * @return factory to create <link>KinesisConnectorRecordProcessor</link>
     */
    @Override
    public KinesisConnectorRecordProcessorFactory<T, byte[]> getKinesisConnectorRecordProcessorFactory() {
        return new KinesisConnectorRecordProcessorFactory<>(pipeline, kinesisConnectorConfiguration);
    }

    /**
     * Start getting the streaming data from the carrier and publish them to the streaming data processor.
     */
    @Override
    public void start() {
        run();
    }

    /**
     * Stop getting the streaming data from the carrier.
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
