package com.eksiir.StreamingDataManager.Consumer.CarrierListener;

/**
 * Input of <link>ConsumerController</link>.
 * Listens for data from a stream carrier.
 *
 * Created by bbehzadi on 1/9/14.
 */
public interface CarrierListener {
    /**
     * Start getting the streaming data from the carrier and
     * publish them to the streaming data processor.
     */
    void start();

    /**
     * Stop getting the streaming data from the carrier.
     */
    void stop();

    /**
     * Report info and metrics on the streaming data.
     *
     * @return the reported info and metrics
     */
    String report();
}
