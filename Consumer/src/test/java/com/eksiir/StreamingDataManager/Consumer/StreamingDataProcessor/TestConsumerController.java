package com.eksiir.StreamingDataManager.Consumer.StreamingDataProcessor;

import com.eksiir.StreamingDataManager.Common.StreamingDataController;
import org.junit.Test;

/**
 * Test case and also reference implementation of using consumer controller.
 */
public class TestConsumerController {

    @Test
    public void testConsumerController() {
        // TODO: create a configuration pojo and use that.

//        StreamingDataController controller = null;
//        try {
//            ConsumerControllerFactory consumerControllerFactory = new ConsumerControllerFactory();
//            XMLConfiguration configuration = mock(XMLConfiguration.class);
//            consumerControllerFactory.changeDefaultConfig(configuration);
//            ControllerAction action = ControllerAction.START;
//            consumerControllerFactory.setControllerAction(action);
//            // controller = consumerControllerFactory.newController();
//        } catch (Exception e) {
//            fail("Exception while creating controller from consumer factory.Exception message " + e.getMessage());
//        }
//        assertNotNull(controller);
//
//        Thread thread = new Thread(new RunConsumerController(controller));
//        thread.setDaemon(true);
//        thread.start();
//
//        try {
//            TimeUnit.SECONDS.sleep(2);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
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
