package com.eksiir.StreamingDataManager.Producer;

import com.amazonaws.services.cloudfront.model.InvalidArgumentException;
import com.eksiir.StreamingDataManager.Common.Config;
import com.eksiir.StreamingDataManager.Common.Model.StreamingDataManagerConstants;
import com.eksiir.StreamingDataManager.Common.Model.StreamingDataSource;
import com.eksiir.StreamingDataManager.Common.Utils;
import org.apache.commons.configuration.XMLConfiguration;

/**
 * Producer configuration and property management.
 *
 * XMLConfiguration.setSchemaValidation(true) is set.  i.e ALL the xml level validations are delegated to XSD.
 * That includes checking for mandatory key,value pairs presence.
 *
 * Created by bbehzadi on 1/28/14.
 */
public class ProducerConfig extends Config {
    private String logFileName;
    private StreamingDataSource streamingDataSource;
    private int logTailerMillisDelay = -1;

    public ProducerConfig(final XMLConfiguration config) {
        super(config);
    }

    /**
     * @return The configured streaming data source or the default.
     */
    public StreamingDataSource getStreamingDataSource() {
        if (streamingDataSource != null)
            return streamingDataSource;

        final String streamingDataSourceProp = config.getString(StreamingDataManagerConstants.STREAMING_DATA_SOURCE);
        streamingDataSource = Utils.getEnumFromStringValue(StreamingDataSource.class, streamingDataSourceProp);
        return streamingDataSource;
    }

    /**
     * @return The configured log filename to be tailed.
     */
    public String getLogTailerFileName() {
        if (logFileName != null)
            return logFileName;

        logFileName = config.getString(StreamingDataManagerConstants.LOG_TAILER_FILE_NAME);
        return logFileName;
    }

    /**
     * @return The configured delay between checks of the file for new content in milliseconds or the default.
     * @throws InvalidArgumentException
     */
    public int getLogTailerMillisDelay() throws InvalidArgumentException {
        if (logTailerMillisDelay > 0)
            return logTailerMillisDelay;

        logTailerMillisDelay = config.getInt(StreamingDataManagerConstants.LOG_TAILER_MILLIS_DELAY);
        if (logTailerMillisDelay < 0)
            throw new InvalidArgumentException(StreamingDataManagerConstants.LOG_TAILER_MILLIS_DELAY + " < 0");

        return logTailerMillisDelay;
    }
}
