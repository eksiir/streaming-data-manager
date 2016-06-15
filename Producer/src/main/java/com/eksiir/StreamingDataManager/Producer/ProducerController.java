package com.eksiir.StreamingDataManager.Producer;

import com.eksiir.StreamingDataManager.Common.Model.ControllerAction;
import com.eksiir.StreamingDataManager.Common.StreamingDataController;
import com.eksiir.StreamingDataManager.Common.Utils;
import com.eksiir.StreamingDataManager.Producer.CarrierPublisher.CarrierPublisher;
import com.eksiir.StreamingDataManager.Producer.InputProcessor.InputProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Streams the data from the input source to the carrier.
 *
 * Created by bbehzadi on 1/9/14.
 */
public class ProducerController implements StreamingDataController {
    private static final Log LOG = LogFactory.getLog(ProducerController.class);
    private static final String APP_NAME = "StreamingDataProducer";        // this application's name

    private ProducerConfig producerConfig;
    private ControllerAction controllerAction;
    private CarrierPublisher carrierPublisher;
    private InputProcessor inputProcessor;

    /**
     * Producer controller.
     *
     * @param args -help | -config <producer-config>.xml -action [start|stop|restart]"
     */
    public static void main(String[] args) {
        int exitCode = 0;
        try {
            ProducerControllerFactory producerControllerFactory = new ProducerControllerFactory();
            Utils.parseCliOptions(args, producerControllerFactory, APP_NAME);

            ProducerController producerController = (ProducerController) producerControllerFactory.newController();
            producerController.takeAction();
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
                inputProcessor.start();
                break;

            case STOP:
                inputProcessor.stop();
                break;

            case RESTART:
                inputProcessor.stop();
                inputProcessor.start();
                break;

            default:
                throw new UnsupportedOperationException(controllerAction.toString());
        }
    }

    /**
     * Produce informative data on the controller's action.
     *
     * @return reporting data on the <code>inputProcessor</code> and <code>carrierPublisher</code>
     */
    @Override
    public String report() {
        return inputProcessor + " report: " + inputProcessor.report() + "\n" +
               carrierPublisher + " report: " + carrierPublisher.report();
    }

    /**
     * NB. Package scope and not public. To be used only by the respective factory
     *
     * @param producerConfig
     */
    public void setConfig(final ProducerConfig producerConfig) {
        this.producerConfig = producerConfig;
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
     * @param carrierPublisher
     */
    void setCarrierPublisher(final CarrierPublisher carrierPublisher) {
        this.carrierPublisher = carrierPublisher;
    }

    /**
     * NB. Package scope and not public. To be used only by the respective factory
     *
     * @param inputProcessor
     */
    void setInputProcessor(final InputProcessor inputProcessor) {
        this.inputProcessor = inputProcessor;
    }
}
