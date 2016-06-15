package com.eksiir.StreamingDataManager.Common;

/**
 * Both the producer and consumer controller should implement this interface.
 *
 * Created by bbehzadi on 2/12/14.
 */
public interface StreamingDataController {
    /**
     * Take the action the controller is task to do.
     *
     * @throws Exception
     */
    void takeAction() throws Exception;

    /**
     * Produce informative data on the controller's action.
     *
     * @return reporting data
     */
    String report();
}
