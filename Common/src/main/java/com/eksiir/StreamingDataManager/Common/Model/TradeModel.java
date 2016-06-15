package com.eksiir.StreamingDataManager.Common.Model;

import java.io.IOException;

/**
 * Object model for streaming trades.
 *
 * Created by bbehzadi on 1/29/14.
 */
public class TradeModel extends DataModel {
    public TradeModel() {
    }

    /**
     * Serialize the TradeModel object to a delimited String.
     *
     * @return delimited String presentation of the TradeModel.
     */
    @Override
    public String toDelimitedString() {
        return null;        // TODO
    }

    /**
     * Deserialize the given byte array to the TradeModel object.
     *
     * @param bytes byte array
     * @return TradeModel object
     * @throws java.io.IOException
     */
    @Override
    public DataModel toDataModelObject(final byte[] bytes) throws IOException {
        return null;        // TODO
    }
}
