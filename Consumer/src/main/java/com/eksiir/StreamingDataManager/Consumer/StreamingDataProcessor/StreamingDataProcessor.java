package com.eksiir.StreamingDataManager.Consumer.StreamingDataProcessor;

/**
 * Output of <link>ConsumerController</link>.
 * Processes the continuous flow of the streaming data from a stream carrier.
 *
 * Created by bbehzadi on 1/9/14.
 */
public interface StreamingDataProcessor {
    /**
     * Process the given byte stream.
     *
     * @param data byte stream to be processed
     */
    public void process(byte[] data);

    /**
     * Report info and metrics on the streaming data.
     *
     * @return the reported info and metrics
     */
    String report();
}
