package com.eksiir.StreamingDataManager.Producer;

import com.eksiir.StreamingDataManager.Common.Model.ControllerAction;
import com.eksiir.StreamingDataManager.Common.StreamingDataController;
import org.apache.commons.configuration.XMLConfiguration;
import org.junit.Test;

import static org.junit.Assert.fail;
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
            // TODO: uncomment the following only if a valid AWS Kinesis security token is being used
            // controller = consumerControllerFactory.newController();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception while creating controller from consumer factory.Exception message " + e.getMessage());
        }
    }
}
