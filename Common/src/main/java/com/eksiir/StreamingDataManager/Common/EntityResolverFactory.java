package com.eksiir.StreamingDataManager.Common;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * Entity resolver factory.
 */
public class EntityResolverFactory {

    private static final String CONSUMER_XSD = "ConsumerConfigSchema.xsd";
    private static final String PRODUCER_XSD = "ProducerConfigSchema.xsd";

    /**
     * Factory to hand out instances of entity resolver based on the configuration type.
     *
     * @param type
     * @return
     */
    public static EntityResolver getEntityResolver(ConfigType type) {
        EntityResolver entityResolver = null;
        switch (type) {
            case CONSUMER: entityResolver = consumerConfigEntityResolver;
                           break;
            case PRODUCER: entityResolver = producerConfigEntityResolver;
                           break;
            default: throw new IllegalArgumentException("config type " + type);
        }
        return entityResolver;
    }

    private static EntityResolver producerConfigEntityResolver = new EntityResolver() {
        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            return new InputSource(PRODUCER_XSD);
        }
    };

    private static EntityResolver consumerConfigEntityResolver = new EntityResolver() {
        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            return new InputSource(CONSUMER_XSD);
        }
    };
}
