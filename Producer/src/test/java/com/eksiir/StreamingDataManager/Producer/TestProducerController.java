package com.eksiir.StreamingDataManager.Producer;

import com.eksiir.StreamingDataManager.Common.Model.ControllerAction;
import com.eksiir.StreamingDataManager.Common.StreamingDataController;
import org.apache.commons.configuration.XMLConfiguration;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * Tests consumer controller and reference implementation of producer controller.
 */
public class TestProducerController {

    @Test
    public void testConsumerController() {
        StreamingDataController controller = null;
        try {
            ProducerControllerFactory producerControllerFactory = new ProducerControllerFactory();
            XMLConfiguration configuration = mock(XMLConfiguration.class);
            producerControllerFactory.changeDefaultConfig(configuration);
            ControllerAction action = ControllerAction.START;
            producerControllerFactory.setControllerAction(action);
            controller = producerControllerFactory.newController();
        } catch (Exception e) {
            //fail("Exception while creating controller from consumer factory.Exception message " + e.getMessage());
            //TODO: refactor this after the config module is integrated.
        }

        /*
        assertNotNull(controller);

        Thread thread = new Thread(new RunConsumerController(controller));
        thread.setDaemon(true);
        thread.start();

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
    }

    private static class RunConsumerController implements Runnable {
        private StreamingDataController controller;

        RunConsumerController(StreamingDataController controller) {
            this.controller = controller;
        }

        @Override
        public void run() {
            try {
                controller.takeAction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
