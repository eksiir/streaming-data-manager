package com.eksiir.StreamingDataManager.Consumer.StreamingDataProcessor.KinesisConnector;

import com.amazonaws.services.kinesis.connectors.KinesisConnectorConfiguration;
import com.amazonaws.services.kinesis.connectors.impl.AllPassFilter;
import com.amazonaws.services.kinesis.connectors.impl.BasicMemoryBuffer;
import com.amazonaws.services.kinesis.connectors.interfaces.*;
import com.amazonaws.services.kinesis.connectors.redshift.RedshiftBasicEmitter;
import com.eksiir.StreamingDataManager.Common.Model.DataModel;
import com.eksiir.StreamingDataManager.Consumer.ConsumerConfig;
import com.eksiir.StreamingDataManager.Consumer.StreamingDataProcessor.StreamingDataProcessor;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Kinesis Redshift pipeline to process various streaming data models.
 *
 * @param <T> streaming data model
 *
 * Created by bbehzadi on 1/29/14.
 */
public class KinesisRedshiftConnector<T extends DataModel>
        implements IKinesisConnectorPipeline<T, byte[]>, StreamingDataProcessor {

    private final Log log = LogFactory.getLog(KinesisRedshiftConnector.class);
    private final ConsumerConfig consumerConfig;

    public KinesisRedshiftConnector(final ConsumerConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
        try {
            if (consumerConfig == null)
                throw new NullPointerException("config");

            Class.forName("org.postgresql.Driver");
            addRedshiftCredentialsToConsumerConfig();
        } catch (ClassNotFoundException e) {
            log.error("Could not load PostgreSQL driver", e);
        } catch (Exception e) {
            log.error("Failed to instantiate Kinesis Redshift pipeline", e);
        }
        log.info("Loaded PostgreSQL driver");
    }

    private void addRedshiftCredentialsToConsumerConfig() throws ConfigurationException, IOException {
        PropertiesConfiguration redshiftCredentialsProps = new PropertiesConfiguration();
        String redshiftCredentialsFileName = consumerConfig.getRedshiftCredentialsPropertiesFileName();
        InputStream redshiftCredentialsInputStream =
                getClass().getClassLoader().getResourceAsStream(redshiftCredentialsFileName);
        redshiftCredentialsProps.load(redshiftCredentialsInputStream);
        redshiftCredentialsInputStream.close();

        consumerConfig.addProperties(redshiftCredentialsProps);
    }

    /**
     * Kinesis connector streaming data emitter callback which defines a method that makes client calls to other
     * AWS services and persists the records stored in the buffer. The records can also be sent to another Amazon
     * Kinesis stream.
     *
     * @param kinesisConnectorConfiguration kinesis connector configuration
     * @return Redshift streaming data emitter callback
     */
    @Override
    public IEmitter<byte[]> getEmitter(final KinesisConnectorConfiguration kinesisConnectorConfiguration) {
        return new RedshiftBasicEmitter(kinesisConnectorConfiguration);
    }

    /**
     * Kinesis connector buffer allocator callback which defines the method for buffering the set of records to be
     * processed. It specifies the size limit (number of records) and total byte count.
     *
     * @param kinesisConnectorConfiguration kinesis connector configuration
     * @return Redshift buffer allocator callback
     */
    @Override
    public IBuffer<T> getBuffer(final KinesisConnectorConfiguration kinesisConnectorConfiguration) {
        return new BasicMemoryBuffer<T>(kinesisConnectorConfiguration);
    }

    /**
     * Kinesis connector byte stream transformer callback which defines the transformation of records from the
     * Amazon Kinesis stream in order to suit the user-defined data model. Includes methods for custom
     * serializer/deserializers.
     *
     * @param kinesisConnectorConfiguration kinesis connector configuration
     * @return Redshift byte stream transformer callback
     */
    @Override
    public ITransformer<T, byte[]> getTransformer(final KinesisConnectorConfiguration kinesisConnectorConfiguration) {
        return new KinesisRedshiftTransformer<T>(consumerConfig);
    }

    /**
     * Kinesis connector streaming filter callback for excluding irrelevant records from the processing.
     *
     * @param kinesisConnectorConfiguration kinesis connector configuration
     * @return Redshift streaming filter callback
     */
    @Override
    public IFilter<T> getFilter(final KinesisConnectorConfiguration kinesisConnectorConfiguration) {
        return new AllPassFilter<T>();
    }

    /**
     * Process the given byte stream. Not used as the processing is done by the <code>pipeline</code>.
     *
     * @param data byte stream to be processed
     */
    @Override
    public void process(final byte[] data) {
    }

    /**
     * Report info and metrics on the streaming data.
     *
     * @return the reported info and metrics
     */
    @Override
    public String report() {
        return null;    // TODO
    }
}
