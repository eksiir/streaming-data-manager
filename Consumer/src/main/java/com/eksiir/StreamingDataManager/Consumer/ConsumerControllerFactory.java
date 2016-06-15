package com.eksiir.StreamingDataManager.Consumer;

import com.eksiir.StreamingDataManager.Common.ConfigType;
import com.eksiir.StreamingDataManager.Common.Model.StreamingDataCarrier;
import com.eksiir.StreamingDataManager.Common.Model.StreamingDataConsumer;
import com.eksiir.StreamingDataManager.Common.StreamingDataController;
import com.eksiir.StreamingDataManager.Common.StreamingDataControllerFactory;
import com.eksiir.StreamingDataManager.Consumer.CarrierListener.CarrierListener;
import com.eksiir.StreamingDataManager.Consumer.CarrierListener.Kinesis.KinesisListenerFactory;
import com.eksiir.StreamingDataManager.Consumer.StreamingDataProcessor.CarrierOut;
import com.eksiir.StreamingDataManager.Consumer.StreamingDataProcessor.KinesisConnector.KinesisRedshiftConnector;
import com.eksiir.StreamingDataManager.Consumer.StreamingDataProcessor.StreamingDataProcessor;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This factory is used by the applications which use the streaming data consumer as a library and need an
 * instance of the consumer.
 *
 * Created by bbehzadi on 2/12/14.
 */
public class ConsumerControllerFactory extends StreamingDataControllerFactory {

    private final Log log = LogFactory.getLog(ConsumerControllerFactory.class);

    private static final String DEFAULT_CONSUMER_CONFIG = "DefaultConsumerConfig.xml";
    private final ConsumerConfig consumerConfig;
    private CarrierListener carrierListener;
    private StreamingDataProcessor streamingDataProcessor;

    public ConsumerControllerFactory() throws ConfigurationException {
        super(DEFAULT_CONSUMER_CONFIG);
        consumerConfig = new ConsumerConfig(getConfig());
    }

    /**
     * @return a new instance of <code>StreamingDataController</code>
     * @throws Exception
     */
    @Override
    public StreamingDataController newController() throws Exception {
        if (controllerAction == null)
            throw new NullPointerException("controllerAction");

        ConsumerController consumerController = new ConsumerController();
        consumerController.setConfig(consumerConfig);
        consumerController.setControllerAction(controllerAction);

        streamingDataProcessor = getStreamingDataProcessor();
        if (streamingDataProcessor == null)
            throw new NullPointerException("streamingDataProcessor");
        consumerController.setStreamingDataProcessor(streamingDataProcessor);

        carrierListener = getCarrierListener(streamingDataProcessor);
        if (carrierListener == null)
            throw new NullPointerException("carrierListener");
        consumerController.setCarrierListener(carrierListener);

        return consumerController;
    }

    /**
     * Returns an <link>StreamingDataProcessor</link> object.
     *
     * @return an instance of <link>StreamingDataProcessor</link>
     */
    private StreamingDataProcessor getStreamingDataProcessor() {
        final StreamingDataConsumer rtProcessor = consumerConfig.getStreamingConsumerProcessor();

        switch (rtProcessor) {
            case APPLICATION:
                log.error("Generic application streaming data processor not supported yet");
                break;

            case CARRIER_OUT:
                return new CarrierOut();

            case JDBC_CONNECTION:
                log.error("JDBC connection streaming data processor not supported yet");
                break;

            case KINESIS_DYNAMODB_CONNECTOR:
                log.error("Upsert to DynamoDB not supported yet");
                break;

            case KINESIS_REDSHIFT_CONNECTOR:
                return new KinesisRedshiftConnector(consumerConfig);

            case KINESIS_S3_CONNECTOR:
                log.error("Writing to S3 files not supported yet");
                break;

            case MAP_REDUCE:
                log.error("MapReduce streaming data processor not supported yet");
                break;

            case ROLLING_LOG_FILE:
                log.error("Rolling log file as the streaming data processor not supported yet");
                break;

            case SPARK:
                log.error("Spark streaming data processor not supported yet");
                break;

            default:
                log.error("Invalid streaming data processor: " + rtProcessor);
                break;
        }

        return null;
    }

    /**
     * Returns a <link>CarrierListener</link> object based on the given <link>StreamingDataProcessor</link>.
     *
     * @param streamingDataProcessor the chosen streaming data processor
     * @return an instance of <link>CarrierListener</link>
     * @throws Exception
     */
    private CarrierListener getCarrierListener(final StreamingDataProcessor streamingDataProcessor) throws Exception {
        final StreamingDataCarrier streamingDataCarrier = consumerConfig.getStreamingDataCarrier();
        switch (streamingDataCarrier) {
            case KINESIS:
                KinesisListenerFactory factory = new KinesisListenerFactory(consumerConfig);
                factory.setStreamingDataProcessor(streamingDataProcessor);
                return factory.newKinesisListener();

            case KAFKA:
                log.error("Kafka carrier not supported yet");
                break;

            default:
                log.error("Invalid streaming data carrier: " + streamingDataCarrier);
                break;
        }

        return null;
    }
}
