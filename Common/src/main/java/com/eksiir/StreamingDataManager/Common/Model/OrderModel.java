package com.eksiir.StreamingDataManager.Common.Model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Object model for streaming orders.
 *
 * Created by bbehzadi on 1/29/14.
 */
public class OrderModel extends DataModel {       // TODO this is just for testing for now
    private String record;

    public OrderModel() {
    }

    public OrderModel(final String record) {
        this.record = record;
    }

    /**
     * Serialize this ObjectModel object to a delimited String.
     *
     * @return delimited String presentation of this OrderModel.
     */
    @Override
    public String toDelimitedString() {
        return record;
    }

    /**
     * Deserialize the given byte array to an OrderModel object.
     *
     * @param bytes byte array
     * @return OrderModel object
     * @throws IOException
     */
    @Override
    public DataModel toDataModelObject(final byte[] bytes) throws IOException {
        try {
            return new OrderModel(new String(bytes, characterSet) + "\n");
        } catch (UnsupportedEncodingException e) {
            throw new IOException("UnsupportedEncodingException");
        }
    }
}
