package com.eksiir.StreamingDataManager.Consumer.StreamingDataProcessor;

import com.eksiir.StreamingDataManager.Common.Model.ControllerAction;
import com.eksiir.StreamingDataManager.Common.StreamingDataController;
import com.eksiir.StreamingDataManager.Consumer.ConsumerControllerFactory;
import org.apache.commons.configuration.XMLConfiguration;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class TestConsumerControllerFactory {

    @Test
    public void testConsumerControllerFactory() {
        StreamingDataController controller = null;
        try {
            ConsumerControllerFactory consumerControllerFactory = new ConsumerControllerFactory();
            XMLConfiguration configuration = mock(XMLConfiguration.class);
            consumerControllerFactory.changeDefaultConfig(configuration);
            ControllerAction action = ControllerAction.START;
            consumerControllerFactory.setControllerAction(action);
            controller = consumerControllerFactory.newController();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception while creating controller from consumer factory.Exception message " + e.getMessage());
        }
        assertNotNull(controller);
    }
}
