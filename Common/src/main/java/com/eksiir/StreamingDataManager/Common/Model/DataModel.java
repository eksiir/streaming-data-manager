package com.eksiir.StreamingDataManager.Common.Model;

import java.io.IOException;

/**
 * Base class of all data models.
 *
 * Created by bbehzadi on 1/30/14.
 */
public abstract class DataModel {
    protected Character delimiter;
    protected String characterSet;

    public Character getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(final Character delimiter) {
        this.delimiter = delimiter;
    }

    public String getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(final String characterSet) {
        this.characterSet = characterSet;
    }

    /**
     * Serialize the data model object to a delimited String.
     *
     * @return delimited String presentation of the data model.
     */
    public abstract String toDelimitedString();

    /**
     * Deserialize the given byte array to the data model object.
     *
     * @param bytes byte array
     * @return data model object
     * @throws IOException
     */
    public abstract DataModel toDataModelObject(final byte[] bytes) throws IOException;
}
