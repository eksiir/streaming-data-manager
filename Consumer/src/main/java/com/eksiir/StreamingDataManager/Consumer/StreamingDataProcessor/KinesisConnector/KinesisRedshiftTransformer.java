package com.eksiir.StreamingDataManager.Consumer.StreamingDataProcessor.KinesisConnector;

import com.amazonaws.services.cloudfront.model.InvalidArgumentException;
import com.amazonaws.services.kinesis.connectors.interfaces.ITransformer;
import com.amazonaws.services.kinesis.model.Record;
import com.eksiir.StreamingDataManager.Common.Model.*;
import com.eksiir.StreamingDataManager.Consumer.ConsumerConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

/**
 * Kinesis to Redshift transformer:
 *
 *            |------ toClass() ------|    |--------- fromClass() --------|
 * Kinesis -> byte stream -> data model -> delimited String -> byte stream -> Redshift
 *
 * Created by bbehzadi on 1/30/14.
 */
public class KinesisRedshiftTransformer<T extends DataModel> implements ITransformer<T, byte[]> {
    private final Log log = LogFactory.getLog(KinesisRedshiftTransformer.class);
    private T dataModel;

    public KinesisRedshiftTransformer(final ConsumerConfig consumerConfig) throws InvalidArgumentException {
        StreamingDataModel streamingDataModel = consumerConfig.getStreamingDataModel();
        switch (streamingDataModel) {
            case ORDER:
                dataModel = (T)new OrderModel();
                break;

            case TRADE:
                dataModel = (T)new TradeModel();
                break;

            case RATE:
                dataModel = (T)new RateModel();
                break;

            default:
                throw new InvalidArgumentException(streamingDataModel.toString());
        }
        dataModel.setDelimiter(consumerConfig.getStreamingDataDelimiter());
        dataModel.setCharacterSet(consumerConfig.getCharacterSet());
    }

    /**
     * Kinesis deserializer: Transform byte array data part of Kinesis <link>Record</link> into a data model object.
     *
     * @param record raw record from the Kinesis stream
     * @return data model object
     * @throws java.io.IOException could not convert the record to a T
     */
    @Override
    public T toClass(final Record record) throws IOException {
        return (T)dataModel.toDataModelObject(record.getData().array());
    }

    /**
     * Redshift serializer: Transform the data model object into delimited string to be used by Redshift COPY
     * command and then serialize it to byte array.
     *
     * @param dataModel data model object
     * @return data byte array of the delimited string
     */
    @Override
    public byte[] fromClass(final T dataModel) throws IOException {
        return dataModel.toDelimitedString().getBytes();
    }
}
