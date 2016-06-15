package com.eksiir.StreamingDataManager.Consumer;

import com.eksiir.StreamingDataManager.Common.Model.ControllerAction;
import com.eksiir.StreamingDataManager.Common.StreamingDataController;
import com.eksiir.StreamingDataManager.Common.Utils;
import com.eksiir.StreamingDataManager.Consumer.CarrierListener.CarrierListener;
import com.eksiir.StreamingDataManager.Consumer.StreamingDataProcessor.StreamingDataProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Controller for streaming data from the carrier to the streaming data processor.
 *
 * Created by bbehzadi on 1/9/14.
 */
public class ConsumerController implements StreamingDataController {
    private static final Log LOG = LogFactory.getLog(ConsumerController.class);
    private static final String APP_NAME = "StreamingDataConsumer";          // this application's name

    private ConsumerConfig consumerConfig;
    private ControllerAction controllerAction;
    private CarrierListener carrierListener;
    private StreamingDataProcessor streamingDataProcessor;

    /**
     * Consumer Controller.
     *
     * @param args -help | -config <consumer-config>.xml -action [start|stop|restart]"
     */
    public static void main(String[] args) {
        int exitCode = 0;
        try {
            ConsumerControllerFactory consumerControllerFactory = new ConsumerControllerFactory();
            Utils.parseCliOptions(args, consumerControllerFactory, APP_NAME);

            ConsumerController consumerController = (ConsumerController) consumerControllerFactory.newController();
            consumerController.takeAction();
        } catch (Exception e) {
            exitCode = -1;
            LOG.fatal("Cannot proceed", e);
        }

        System.exit(exitCode);
    }

    /**
     * Take the action the controller is task to do.
     *
     * @throws Exception
     */
    @Override
    public void takeAction() throws Exception {
        switch (controllerAction) {
            case START:
                carrierListener.start();
                break;

            case STOP:
                carrierListener.stop();
                break;

            case RESTART:
                carrierListener.stop();
                carrierListener.start();
                break;

            default:
                throw new UnsupportedOperationException(controllerAction.toString());
        }
    }

    /**
     * Produce informative data on the controller's action.
     *
     * @return reporting data
     */
    @Override
    public String report() {
        return carrierListener + " report: " + carrierListener.report() + "\n" +
               streamingDataProcessor + " report: " + streamingDataProcessor.report();
    }

    public ConsumerConfig getConfig() {
        return consumerConfig;
    }

    /**
     * NB. Package scope and not public. To be used only by the respective factory
     *
     * @param consumerConfig
     */
    public void setConfig(final ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
    }

    /**
     * NB. Package scope and not public. To be used only by the respective factory
     *
     * @param controllerAction
     */
    void setControllerAction(final ControllerAction controllerAction) {
        this.controllerAction = controllerAction;
    }

    /**
     * NB. Package scope and not public. To be used only by the respective factory
     *
     * @param carrierListener
     */
    void setCarrierListener(final CarrierListener carrierListener) {
        this.carrierListener = carrierListener;
    }

    /**
     * NB. Package scope and not public. To be used only by the respective factory
     *
     * @param streamingDataProcessor
     */
    void setStreamingDataProcessor(final StreamingDataProcessor streamingDataProcessor) {
        this.streamingDataProcessor = streamingDataProcessor;
    }
}
