package com.eksiir.StreamingDataManager.Consumer.CarrierListener.Kinesis;

import com.amazonaws.services.cloudfront.model.InvalidArgumentException;
import com.eksiir.StreamingDataManager.Common.Model.*;
import com.eksiir.StreamingDataManager.Consumer.CarrierListener.CarrierListener;
import com.eksiir.StreamingDataManager.Consumer.ConsumerConfig;
import com.eksiir.StreamingDataManager.Consumer.StreamingDataProcessor.StreamingDataProcessor;

/**
 * Factory to create Kinesis <link>CarrierListener</link>.
 *
 * Created by bbehzadi on 1/9/14.
 */
public class KinesisListenerFactory {
    private final ConsumerConfig consumerConfig;
    private final StreamingDataConsumer streamingDataConsumer;
    private StreamingDataProcessor streamingDataProcessor;

    public KinesisListenerFactory(final ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
        streamingDataConsumer = consumerConfig.getStreamingConsumerProcessor();
    }

    /**
     * Set the <link>StreamingDataProcessor</link>.
     *
     * @param streamingDataProcessor the given <link>StreamingDataProcessor</link>
     */
    public void setStreamingDataProcessor(final StreamingDataProcessor streamingDataProcessor) {
        this.streamingDataProcessor = streamingDataProcessor;
    }

    /**
     * @return the created Kinesis <link>CarrierListener</link>
     * @throws Exception
     */
    public CarrierListener newKinesisListener() throws Exception {
        if (streamingDataProcessor == null)
            throw new NullPointerException("streamingDataProcessor not set");

        if (StreamingDataConsumer.isKinesisConnector(streamingDataConsumer)) {
            StreamingDataModel streamingDataModel = consumerConfig.getStreamingDataModel();
            switch (streamingDataModel) {
                case ORDER:
                    return new KinesisConnector<OrderModel>(streamingDataProcessor, consumerConfig);

                case TRADE:
                    return new KinesisConnector<TradeModel>(streamingDataProcessor, consumerConfig);

                case RATE:
                    return new KinesisConnector<RateModel>(streamingDataProcessor, consumerConfig);

                default:
                    throw new InvalidArgumentException(streamingDataModel.toString());
            }
        }

        return new KinesisWorker(streamingDataProcessor, consumerConfig);
    }
}
