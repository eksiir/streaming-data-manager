package com.eksiir.StreamingDataManager.Common;

import com.eksiir.StreamingDataManager.Common.Model.ControllerAction;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.XMLConfiguration;

import java.util.Iterator;

/**
 * Both the producer and consumer controller factories should extend this class.
 *
 * Created by bbehzadi on 2/12/14.
 */
public abstract class StreamingDataControllerFactory {
    private final XMLConfiguration config;
    protected ControllerAction controllerAction;

    protected StreamingDataControllerFactory(final String defaultConfigFileName) throws ConfigurationException  {
        config = new XMLConfiguration();
        ConfigurationUtils.enableRuntimeExceptions(config);
        config.setDelimiterParsingDisabled(true);
        config.setAttributeSplittingDisabled(true);
        config.setSchemaValidation(true);
        config.setEntityResolver(EntityResolverFactory.getEntityResolver(getConfigType(defaultConfigFileName)));
        config.load(defaultConfigFileName);           // load the default configuration
    }

    private ConfigType getConfigType(String configName) {
        if(configName.contains("Consumer")) {
            return ConfigType.CONSUMER;
        } else {
            return ConfigType.PRODUCER;
        }
    }

    protected XMLConfiguration getConfig() {
        return config;
    }

    /**
     * Changes the default streaming data controller configuration with the given configuration.
     *
     * @param configuration configuration which contains properties to replace the defaults
     */
    public void changeDefaultConfig(final XMLConfiguration configuration) {
        if (configuration == null || configuration.getKeys() == null)
            return;

        for (Iterator<String> keyIterator = configuration.getKeys(); keyIterator.hasNext();) {
            String key = keyIterator.next();
            Object value = configuration.getProperty(key);
            config.setProperty(key, value);
        }
    }

    /**
     * Set the streaming data controller action.
     *
     * @param action controller action
     */
    public void setControllerAction(final ControllerAction action) {
        this.controllerAction = action;
    }

    /**
     * @return a new instance of <code>StreamingDataController</code>
     * @throws Exception
     */
    public abstract StreamingDataController newController() throws Exception;
}
