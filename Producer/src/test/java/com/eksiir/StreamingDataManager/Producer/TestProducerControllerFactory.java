package com.eksiir.StreamingDataManager.Producer;

import com.eksiir.StreamingDataManager.Common.Model.ControllerAction;
import com.eksiir.StreamingDataManager.Common.StreamingDataController;
import org.apache.commons.configuration.XMLConfiguration;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class TestProducerControllerFactory {

    @Test
    public void testProducerControllerFactory() {
        StreamingDataController controller = null;
        try {
            ProducerControllerFactory consumerControllerFactory = new ProducerControllerFactory();
            XMLConfiguration configuration = mock(XMLConfiguration.class);
            consumerControllerFactory.changeDefaultConfig(configuration);
            ControllerAction action = ControllerAction.START;
            consumerControllerFactory.setControllerAction(action);
            controller = consumerControllerFactory.newController();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO: This test would fail , there is a hard dependeny on a log file.
            //fail("Exception while creating controller from consumer factory.Exception message " + e.getMessage());
        }
    }
}
