package com.eksiir.StreamingDataManager.Consumer.StreamingDataProcessor;

/**
 * Output of <link>ConsumerController</link>.
 *
 * This is the simplest streaming data processor which simply output the continuous flow of
 * the streaming data from a stream carrier to stdout.  Ideal for testing the carrier.
 *
 * Created by bbehzadi on 1/10/14.
 */
public class CarrierOut implements StreamingDataProcessor {
    private volatile long recordCount = 0L;

    private long getRecordCount() {
        return recordCount;
    }

    /**
     * Display the given byte stream to stdout.
     *
     * @param bytes byte stream to be displayed.
     */
    @Override
    public void process(final byte[] bytes) {
        System.out.println(recordCount + " " + new String(bytes));
        recordCount++;
    }

    /**
     * Report info and metrics on the streaming data.
     *
     * @return the reported info and metrics
     */
    @Override
    public String report() {
        return getRecordCount() + " records displayed.";
    }
}
