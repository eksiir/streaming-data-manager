package com.eksiir.StreamingDataManager.Common.Model;

import java.io.IOException;

/**
 * Object model for streaming rates.
 *
 * Created by bbehzadi on 1/29/14.
 */
public class RateModel extends DataModel {
    public RateModel() {
    }

    /**
     * Serialize the RateModel object to a delimited String.
     *
     * @return delimited String presentation of the data model.
     */
    @Override
    public String toDelimitedString() {
        return null;        // TODO
    }

    /**
     * Deserialize the given byte array to the RateModel object.
     *
     * @param bytes byte array
     * @return RateModel object
     * @throws java.io.IOException
     */
    @Override
    public DataModel toDataModelObject(final byte[] bytes) throws IOException {
        return null;        // TODO
    }
}
