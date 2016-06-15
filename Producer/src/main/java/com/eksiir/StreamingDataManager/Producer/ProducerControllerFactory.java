package com.eksiir.StreamingDataManager.Producer;

import com.eksiir.StreamingDataManager.Common.ConfigType;
import com.eksiir.StreamingDataManager.Common.Model.StreamingDataCarrier;
import com.eksiir.StreamingDataManager.Common.Model.StreamingDataSource;
import com.eksiir.StreamingDataManager.Common.StreamingDataController;
import com.eksiir.StreamingDataManager.Common.StreamingDataControllerFactory;
import com.eksiir.StreamingDataManager.Producer.CarrierPublisher.CarrierPublisher;
import com.eksiir.StreamingDataManager.Producer.CarrierPublisher.Kinesis.KinesisPublisher;
import com.eksiir.StreamingDataManager.Producer.InputProcessor.InputProcessor;
import com.eksiir.StreamingDataManager.Producer.InputProcessor.LogFileProcessor.LogFileTailer;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This factory is used by the applications which use the streaming data producer as a library and need an
 * instance of the producer.
 *
 * Created by bbehzadi on 2/12/14.
 */
public class ProducerControllerFactory extends StreamingDataControllerFactory {

    private final Log log = LogFactory.getLog(ProducerControllerFactory.class);

    private static final String DEFAULT_PRODUCER_CONFIG = "DefaultProducerConfig.xml";
    private final ProducerConfig producerConfig;
    private CarrierPublisher carrierPublisher;
    private InputProcessor inputProcessor;

    public ProducerControllerFactory() throws ConfigurationException {
        super(DEFAULT_PRODUCER_CONFIG);
        producerConfig = new ProducerConfig(getConfig());
    }

    /**
     * @return a new instance of <code>StreamingDataController</code>
     * @throws Exception
     */
    @Override
    public StreamingDataController newController() throws Exception {
        if (controllerAction == null)
            throw new NullPointerException("controllerAction");

        ProducerController producerController = new ProducerController();
        producerController.setConfig(producerConfig);
        producerController.setControllerAction(controllerAction);

        carrierPublisher = getCarrierPublisher();
        if (carrierPublisher == null)
            throw new NullPointerException("carrierPublisher");
        producerController.setCarrierPublisher(carrierPublisher);

        inputProcessor = getInputSource(carrierPublisher);
        if (inputProcessor == null)
            throw new NullPointerException("inputProcessor");
        producerController.setInputProcessor(inputProcessor);

        return producerController;
    }

    /**
     * Returns a <link>CarrierPublisher</link> object.
     *
     * @return an instance of <link>CarrierPublisher</link>
     * @throws Exception
     */
    private CarrierPublisher getCarrierPublisher() throws Exception {
        final StreamingDataCarrier streamingDataCarrier = producerConfig.getStreamingDataCarrier();

        switch (streamingDataCarrier) {
            case KINESIS:
                return new KinesisPublisher(producerConfig);

            case KAFKA:
                log.error("Kafka carrier not supported yet");
                break;

            default:
                log.error("Invalid streaming data carrier: " + streamingDataCarrier);
                break;
        }

        return null;
    }

    /**
     * Returns an <link>InputProcessor</link> object based on the given <link>CarrierPublisher</link>.
     *
     * @param carrierPublisher the chosen carrier publisher
     * @return an instance of <link>InputProcessor</link>
     * @throws Exception
     */
    private InputProcessor getInputSource(final CarrierPublisher carrierPublisher) throws Exception {
        final StreamingDataSource streamingDataSource = producerConfig.getStreamingDataSource();
        switch (streamingDataSource) {
            case LOG_FILE:
                return new LogFileTailer(carrierPublisher, producerConfig);

            case EMITTER:
                log.error("Emitter input source not supported yet");
                break;

            case JDBC_CONNECTION:
                log.error("JDBC connection input source not supported yet");
                break;

            default:
                log.error("Invalid streaming data source: " + streamingDataSource);
                break;
        }

        return null;
    }
}
