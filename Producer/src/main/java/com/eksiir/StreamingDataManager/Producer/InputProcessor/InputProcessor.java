package com.eksiir.StreamingDataManager.Producer.InputProcessor;

/**
 * Input of <link>ProducerController</link>.
 * Handles the continuous flow of the streaming data from a source.
 *
 * Created by bbehzadi on 1/9/14.
 */
public interface InputProcessor {
    /**
     * Start getting the streaming data from the input source and
     * publish them to the stream carrier.
     */
    void start();

    /**
     * Stop getting the streaming data from the input source.
     */
    void stop();

    /**
     * Report info and metrics on the streaming data.
     *
     * @return the reported info and metrics
     */
    String report();
}
