package com.eksiir.StreamingDataManager.Common;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.cloudfront.model.InvalidArgumentException;
import com.eksiir.StreamingDataManager.Common.Model.ControllerAction;
import org.apache.commons.cli.*;
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;

/**
 * General utilities.
 *
 * Created by bbehzadi on 1/27/14.
 */
public class Utils {
    private final static Log LOG = LogFactory.getLog(Utils.class);

    /**
     * Given an enum name returns the corresponding enum field.
     *
     * @param enumClass class of the enum type
     * @param enumName enum name, irrespective of case and surrounding white spaces
     * @param <T> enum type
     * @return corresponding enum
     * @throws NullPointerException
     * @throws IllegalArgumentException
     */
    public static <T extends Enum<T>> T getEnumFromStringValue(Class<T> enumClass, String enumName)
            throws NullPointerException, IllegalArgumentException {

        if (enumClass == null)
            throw new NullPointerException("enumClass");

        if (enumName == null)
            throw new NullPointerException("enumName");

        return Enum.valueOf(enumClass, enumName.trim().toUpperCase());
    }

    /**
     * Attemps to get an <link>AWSCredentialsProvider</link> by first trying to get it from
     * IMDS.  If that fails uses the given file in the CLASSPATH.
     *
     * @param credentialsPropertiesFileName credentials property file name
     * @return An <link>AWSCredentialsProvider</link> object
     */
    public static AWSCredentialsProvider getAWSCredentialsProvider(final String credentialsPropertiesFileName) {
        // ensure the JVM will refresh the cached IP values of AWS resources (e.g. service endpoints).
        java.security.Security.setProperty("networkaddress.cache.ttl" , "60");

        AWSCredentialsProvider credentialsProvider = null;
        try {
            credentialsProvider = new InstanceProfileCredentialsProvider();

            // Verify we can fetch credentials from the provider
            credentialsProvider.getCredentials();
            LOG.info("Obtained credentials from the IMDS.");
        } catch (AmazonClientException e) {
            LOG.info("Unable to obtain credentials from the IMDS, trying classpath properties");
            credentialsProvider = new ClasspathPropertiesFileCredentialsProvider(credentialsPropertiesFileName);

            // Verify we can fetch credentials from the provider
            credentialsProvider.getCredentials();
            LOG.info("Obtained credentials from the properties file.");
        }

        return credentialsProvider;
    }

    /**
     * Parses the CLI options passed to main().  At error the whole applicaiton will exit.
     *
     * @param args passed to main()
     * @param controllerFactory streaming data controller factory
     * @param appName application name
     */
    public static void parseCliOptions(final String[] args,
                                       final StreamingDataControllerFactory controllerFactory,
                                       final String appName) {
        if (appName == null)
            throw new NullPointerException("appName");

        if (controllerFactory == null)
            throw new NullPointerException("controllerFactory");

        String configFileName = null;
        ControllerAction controllerAction = null;

        Option help = new Option("help", false, "print this message");
        Option config = OptionBuilder.withArgName("file").hasArgs(1).withDescription("configuration xml file").
                                      create("config");
        Option action = OptionBuilder.withArgName("start|stop|restart").hasArgs(1).withDescription("desired action").
                                      create("action");

        Options cliOptions = new Options();
        cliOptions.addOption(help);
        cliOptions.addOption(config);
        cliOptions.addOption(action);

        try {
            CommandLineParser parser = new GnuParser();
            CommandLine cmd = parser.parse(cliOptions, args);
            if (cmd.hasOption("help")) {
                showHelpAndExit(appName, cliOptions, 0);
            }

            if (cmd.hasOption("config")) {
                configFileName = cmd.getOptionValue("config");

                final XMLConfiguration configuration = new XMLConfiguration();
                ConfigurationUtils.enableRuntimeExceptions(configuration);
                configuration.setDelimiterParsingDisabled(true);
                configuration.setAttributeSplittingDisabled(true);
//                configuration.setSchemaValidation(true);            // TODO-2
                configuration.setFileName(configFileName);
                configuration.clear();
                configuration.load();

                controllerFactory.changeDefaultConfig(configuration);
            }

            if (cmd.hasOption("action")) {
                String actionOptionValue = cmd.getOptionValue("action").toUpperCase();
                switch (actionOptionValue) {
                    case "START":
                        controllerAction = ControllerAction.START;
                        break;
                    case "STOP":
                        controllerAction = ControllerAction.STOP;
                        break;
                    case "RESTART":
                        controllerAction = ControllerAction.RESTART;
                        break;
                    default:
                        throw new InvalidArgumentException("Invalid action = " + actionOptionValue);
                }

                controllerFactory.setControllerAction(controllerAction);
            }

            // config option is not required as the default will be applied.

            if (controllerAction == null)
                throw new ParseException("action option is required");
        } catch (Exception e) {
            LOG.fatal("CLI error", e);
            showHelpAndExit(appName, cliOptions, -1);
        }

        LOG.info("configFileName=" + configFileName + " controllerAction=" + controllerAction);
    }

    private static void showHelpAndExit(final String appName, Options cliOptions, int exitCode) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setWidth(128);
        helpFormatter.printHelp(appName, cliOptions, true);
        System.exit(exitCode);
    }

    public static InputStream getInputStreamFromResource(String resourceName) {
        return Utils.class.getResourceAsStream(resourceName);
    }
}
