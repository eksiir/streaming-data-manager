package com.eksiir.StreamingDataManager.Consumer.CarrierListener.Kinesis;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorFactory;
import com.eksiir.StreamingDataManager.Consumer.ConsumerConfig;
import com.eksiir.StreamingDataManager.Consumer.StreamingDataProcessor.StreamingDataProcessor;

/**
 * Record processor factory for <link>KinesisListenerFactory</link> push model.
 *
 * Created by bbehzadi on 1/24/14.
 */
public class KinesisWorkerRecordProcessorFactory implements IRecordProcessorFactory {
    private ConsumerConfig consumerConfig;
    private StreamingDataProcessor streamingDataProcessor;

    public void setConsumerConfig(final ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
    }

    public void setStreamingDataProcessor(final StreamingDataProcessor streamingDataProcessor) {
        this.streamingDataProcessor = streamingDataProcessor;
    }

    /**
     * @return a new instance of <link>KinesisWorkerRecordProcessor</link>
     */
    @Override
    public IRecordProcessor createProcessor() {
        return new KinesisWorkerRecordProcessor(consumerConfig, streamingDataProcessor);
    }
}
