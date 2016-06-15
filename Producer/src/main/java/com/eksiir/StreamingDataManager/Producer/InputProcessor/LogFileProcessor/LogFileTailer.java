package com.eksiir.StreamingDataManager.Producer.InputProcessor.LogFileProcessor;

import com.amazonaws.services.cloudfront.model.InvalidArgumentException;
import com.eksiir.StreamingDataManager.Producer.CarrierPublisher.CarrierPublisher;
import com.eksiir.StreamingDataManager.Producer.InputProcessor.InputProcessor;
import com.eksiir.StreamingDataManager.Producer.ProducerConfig;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.Executor;

/**
 * Input of <link>ProducerController</link>.
 * Handles the continuous flow of the streaming data from a text line oriented log tailer source.
 *
 * Created by bbehzadi on 1/9/14.
 */
public class LogFileTailer extends TailerListenerAdapter implements InputProcessor {
    private final Log log = LogFactory.getLog(LogFileTailer.class);
    private final ProducerConfig producerConfig;
    private final CarrierPublisher carrierPublisher;
    private final String logFileName;
    private final Tailer tailer;
    private final Executor executor;
    private volatile boolean started = false;
    private volatile long tailedLineCount = 0L;

    public LogFileTailer(final CarrierPublisher carrierPublisher, final ProducerConfig producerConfig)
            throws NullPointerException, FileNotFoundException, InvalidArgumentException {

        if (producerConfig == null)
            throw new NullPointerException("producerConfig");

        this.producerConfig = producerConfig;

        if (carrierPublisher == null)
            throw new NullPointerException("carrierPublisher");

        this.carrierPublisher = carrierPublisher;

        logFileName = producerConfig.getLogTailerFileName();
        final File logFile = new File(logFileName);
        if (!logFile.exists())
            throw new FileNotFoundException(logFileName);

        if (!logFile.isFile())
            throw new InvalidArgumentException(logFileName + " is not a file");

        if (!logFile.canRead())
            throw new InvalidArgumentException("Cannot read from " + logFile);

        // The delay between checks of the file for new content in milliseconds.
        final int millisecDelay = producerConfig.getLogTailerMillisDelay();

        log.debug("Tailing " + logFileName + " every " + millisecDelay + " millisec");

        /*
         * Constructor options not used.
         * . whether to close and reopen the file between reading chunks
         * . specify buffer size
         */
        tailer = new Tailer(logFile, this, millisecDelay, false /* from beginning */);      // is a Runnable
        executor = new Executor() {
            @Override
            public void execute(final Runnable command) {
                command.run();
            }
        };
    }

    private long getTailedLineCount() {
        return tailedLineCount;
    }

    /**
     * Start getting the streaming data from the log file and
     * publish them to the stream carrier.
     */
    @Override
    public void start() {
        if (started) {
            log.info("Already started.");
            return;
        }

        executor.execute(tailer);
        started = true;
    }

    /**
     * Stop getting the streaming data from the log file.
     */
    @Override
    public void stop() {
        if (!started) {
            log.error("Not started yet.");
            return;
        }

        tailer.stop();
        started = false;
    }

    /**
     * Report info and metrics on the streaming data.
     *
     * @return the reported info and metrics
     */
    @Override
    public String report() {
        return getTailedLineCount() + " lines tailed.";
    }

    /**
     * The tailer will call this method during construction, giving the listener a method of stopping the tailer.
     *
     * @param tailer the tailer.
     */
    @Override
    public void init(final Tailer tailer) {
        super.init(tailer);
    }

    /**
     * This method is called if the tailed file is not found.
     * Can happen during log rotation or if the file is removed.
     */
    @Override
    public void fileNotFound() {
        super.fileNotFound();
        log.error(logFileName + " does not exist anymore!");    // what TODO next?
    }

    /**
     * Called if a file rotation is detected.
     * <p/>
     * This method is called before the file is reopened, and fileNotFound may be called if the new file has not yet
     * been created.
     */
    @Override
    public void fileRotated() {
        super.fileRotated();
        log.info(logFileName + " rotated.");
    }

    /**
     * Handles a line from a Tailer.
     *
     * @param line the line.
     */
    @Override
    public void handle(final String line) {
        super.handle(line);
        carrierPublisher.publish(line.getBytes());
    }

    /**
     * Handles an Exception.
     *
     * @param ex the exception.
     */
    @Override
    public void handle(final Exception ex) {
        super.handle(ex);
        log.error("TailerListenerAdapter", ex);
    }
}
