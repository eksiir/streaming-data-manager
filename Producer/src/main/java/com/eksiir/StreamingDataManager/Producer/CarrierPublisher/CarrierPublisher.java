package com.eksiir.StreamingDataManager.Producer.CarrierPublisher;

/**
 * Output of <link>ProducerController</link>.
 * Publish data to a stream carrier.
 *
 * Created by bbehzadi on 1/9/14.
 */
public interface CarrierPublisher {
    /**
     * Publish the given data to the stream carrier.
     *
     * @param data byte stream to be published
     */
    public void publish(byte[] data);

    /**
     * Report info and metrics on the streaming data.
     *
     * @return the reported info and metrics
     */
    String report();
}
