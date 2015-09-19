/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.tools.bridge.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.reflections.Reflections;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.tools.bridge.IServiceListener;
import kieker.tools.bridge.LookupEntity;
import kieker.tools.bridge.ServiceContainer;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.IServiceConnector;
import kieker.tools.bridge.connector.ServiceConnectorFactory;
import kieker.tools.bridge.connector.http.HTTPConnector;
import kieker.tools.bridge.connector.jms.JMSClientConnector;
import kieker.tools.bridge.connector.jms.JMSEmbeddedConnector;
import kieker.tools.bridge.connector.tcp.TCPClientConnector;
import kieker.tools.bridge.connector.tcp.TCPMultiServerConnector;
import kieker.tools.bridge.connector.tcp.TCPSingleServerConnector;
import kieker.tools.util.CLIHelpFormatter;

/**
 * The command line server of the KDB.
 *
 * @author Reiner Jung
 * @since 1.8
 */
public final class CLIServerMain {

	/**
	 * The logger used in the server.
	 */
	private static final Log LOG = LogFactory.getLog(CLIServerMain.class);

	private static final String CMD_TYPE = "t";
	private static final String CMD_TYPE_LONG = "type";

	private static final String CMD_HOST = "h";
	private static final String CMD_HOST_LONG = "host";

	private static final String CMD_PORT = "p";
	private static final String CMD_PORT_LONG = "port";

	private static final String CMD_USER = "u";
	private static final String CMD_USER_LONG = "user";

	private static final String CMD_PASSWORD = "w";
	private static final String CMD_PASSWORD_LONG = "password";

	private static final String CMD_URL = "l";
	private static final String CMD_URL_LONG = "url";

	private static final String CMD_CONTEXT = "k";
	private static final String CMD_CONTEXT_LONG = "context";

	private static final String CMD_KIEKER_CONFIGURATION = "c";
	private static final String CMD_KIEKER_CONFIGURATION_LONG = "configuration";

	private static final String CMD_LIBRARIES = "L";
	private static final String CMD_LIBRARIES_LONG = "libraries";

	private static final String CMD_DAEMON = "d";
	private static final String CMD_DAEMON_LONG = "daemon";

	private static final String CMD_STATS = "s";
	private static final String CMD_STATS_LONG = "stats";

	private static final String CMD_VERBOSE = "v";
	private static final String CMD_VERBOSE_LONG = "verbose";

	private static final String CMD_MAP_FILE = "m";
	private static final String CMD_MAP_FILE_LONG = "map";

	private static final String DAEMON_FILE = "daemon.pidfile";

	private static final String JAVA_TMP_DIR = "java.io.tmpdir";

	private static final String CLI_CONNECTOR = "kieker.tools.bridge.connector";

	private static boolean verbose;
	private static boolean stats;
	private static long startTime;
	private static long deltaTime;

	private static Options options;
	private static CommandLine commandLine;

	private static URLClassLoader classLoader;
	private static ServiceContainer container;

	private CLIServerMain() {
		// private default constructor
	}

	/**
	 * CLI server main.
	 *
	 * @param args
	 *            command line arguments
	 */
	public static void main(final String[] args) {
		Configuration configuration;

		int exitCode = 0;
		CLIServerMain.declareOptions();
		try {
			// parse the command line arguments
			commandLine = new BasicParser().parse(options, args);

			// verbosity setup
			verbose = commandLine.hasOption(CMD_VERBOSE);

			// statistics
			stats = commandLine.hasOption(CMD_STATS);

			// daemon mode
			if (commandLine.hasOption(CMD_DAEMON)) {
				System.out.close();
				System.err.close();
				CLIServerMain.getPidFile().deleteOnExit();
			}

			// Find libraries and setup mapping
			final ConcurrentMap<Integer, LookupEntity> lookupEntityMap = ServiceConnectorFactory.createLookupEntityMap(CLIServerMain.createRecordMap());

			// Kieker setup
			if (commandLine.hasOption(CMD_KIEKER_CONFIGURATION)) {
				configuration = ConfigurationFactory.createConfigurationFromFile(commandLine.getOptionValue("c"));
			} else {
				configuration = ConfigurationFactory.createSingletonConfiguration();
			}

			// reconfigure kieker configuration
			if (commandLine.hasOption(CMD_PORT)) {
				if (commandLine.hasOption(CMD_TYPE)) {
					final String type = commandLine.getOptionValue(CMD_TYPE);
					if ("jms-embedded".equals(type)) {
						configuration.setProperty(JMSEmbeddedConnector.PORT, commandLine.getOptionValue(CMD_PORT));
					} else if ("tcp-single-server".equals(type)) {
						configuration.setProperty(TCPSingleServerConnector.PORT, commandLine.getOptionValue(CMD_PORT));
					} else if ("tcp-server".equals(type)) {
						configuration.setProperty(TCPMultiServerConnector.PORT, commandLine.getOptionValue(CMD_PORT));
					} else if ("tcp-client".equals(type)) {
						configuration.setProperty(TCPClientConnector.PORT, commandLine.getOptionValue(CMD_PORT));
					} else if ("http-rest".equals(type)) {
						configuration.setProperty(HTTPConnector.PORT, commandLine.getOptionValue(CMD_PORT));
					}
				}
			}
			if (commandLine.hasOption(CMD_HOST)) {
				configuration.setProperty(TCPClientConnector.HOSTNAME, commandLine.getOptionValue(CMD_HOST));
			}
			if (commandLine.hasOption(CMD_USER)) {
				configuration.setProperty(JMSClientConnector.USERNAME, commandLine.getOptionValue(CMD_USER));
			}
			if (commandLine.hasOption(CMD_PASSWORD)) {
				configuration.setProperty(JMSClientConnector.PASSWORD, commandLine.getOptionValue(CMD_PASSWORD));
			}
			if (commandLine.hasOption(CMD_URL)) {
				configuration.setProperty(JMSClientConnector.URI, commandLine.getOptionValue(CMD_URL));
				configuration.setProperty(HTTPConnector.REST_URL, commandLine.getOptionValue(CMD_URL));
			}
			if (commandLine.hasOption(CMD_CONTEXT)) {
				configuration.setProperty(HTTPConnector.CONTEXT, commandLine.getOptionValue(CMD_CONTEXT));
			}
			if (commandLine.hasOption(CMD_TYPE)) {
				final Reflections reflections = new Reflections("kieker.tools.bridge.connector");
				final Set<Class<?>> connectors = reflections.getTypesAnnotatedWith(kieker.tools.bridge.connector.ConnectorProperty.class);

				for (final Class<?> connector : connectors) {
					if (connector.getAnnotation(kieker.tools.bridge.connector.ConnectorProperty.class).cmdName().equals(commandLine.getOptionValue(CMD_TYPE))) {
						configuration.setProperty(CLI_CONNECTOR, connector.getCanonicalName());
						break;
					}
				}
			}

			// start service depending on type
			final IServiceConnector connector = CLIServerMain.createService(configuration, lookupEntityMap);
			CLIServerMain.getLog().info("Service " + connector.getClass().getAnnotation(kieker.tools.bridge.connector.ConnectorProperty.class).name());
			CLIServerMain.runService(configuration, connector);

		} catch (final ParseException e) {
			CLIServerMain.usage("Parsing failed.  Reason: " + e.getMessage());
			exitCode = 4;
		} catch (final IOException e) {
			CLIServerMain.usage("Mapping file read error: " + e.getMessage());
			exitCode = 1;
		} catch (final CLIConfigurationErrorException e) {
			CLIServerMain.usage("Configuration error: " + e.getMessage());
			exitCode = 2;
		} catch (final ConnectorDataTransmissionException e) {
			CLIServerMain.usage("Communication error: " + e.getMessage());
			exitCode = 3;
		}
		// finally {
		// // The URLClassLoader does not have a close method in Java 1.5
		// // if (classLoader != null) {
		// // try {
		// // classLoader.close();
		// // } catch (final IOException e) {
		// // LOG.error("Classloader failed on close.");
		// // exitCode = 5;
		// // }
		// // }
		// }
		System.exit(exitCode);
	}

	/**
	 * Execute the bridge service.
	 *
	 * @param connector
	 *
	 * @throws ConnectorDataTransmissionException
	 *             if an error occured during connector operations
	 */
	private static void runService(final Configuration configuration, final IServiceConnector connector) throws ConnectorDataTransmissionException {
		// setup service container
		container = new ServiceContainer(configuration, connector, false);

		if (verbose) {
			final String updateIntervalParam = commandLine.getOptionValue(CMD_VERBOSE);
			container.setListenerUpdateInterval((updateIntervalParam != null) ? Long.parseLong(updateIntervalParam) // NOCS
					: ServiceContainer.DEFAULT_LISTENER_UPDATE_INTERVAL); // NOCS
			container.addListener(new IServiceListener() {
				@Override
				public void handleEvent(final long count, final String message) {
					CLIServerMain.getLog().info("Received " + count + " records");
				}
			});
		}

		if (stats) {
			startTime = System.nanoTime();
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					CLIServerMain.shutdown();
				} catch (final ConnectorDataTransmissionException e) {
					CLIServerMain.getLog().error("Graceful shutdown failed.");
					CLIServerMain.getLog().error("Cause " + e.getMessage());
				}
			}
		});

		// run the service
		container.run();

		if (stats) {
			deltaTime = System.nanoTime() - startTime;
		}
		if (verbose) {
			CLIServerMain.getLog().info("Server stopped.");
		}
		if (stats) {
			CLIServerMain.getLog().info("Execution time: " + deltaTime + " ns  " + TimeUnit.SECONDS.convert(deltaTime, TimeUnit.NANOSECONDS) + " s");
			CLIServerMain.getLog().info("Time per records: " + (deltaTime / container.getRecordCount()) + " ns/r");
			CLIServerMain.getLog().info("Records per second: " + (container.getRecordCount() / (double) TimeUnit.SECONDS.convert(deltaTime, TimeUnit.NANOSECONDS)));
		}
	}

	/**
	 * Hook for the shutdwon thread so it can access the container's shutdown routine.
	 *
	 * @throws ConnectorDataTransmissionException
	 *             if any internal shutdown calls fail
	 */
	protected static void shutdown() throws ConnectorDataTransmissionException {
		container.shutdown();
	}

	/**
	 * Create a record map of classes implementing IMonitoringRecord interface out of libraries with such classes and a textual mapping file.
	 *
	 * @return A record map. null is never returned as a call of usage terminates the program.
	 * @throws IOException
	 *             if an error occured reading the mapping file
	 * @throws CLIConfigurationErrorException
	 *             if a configuration error occured
	 */
	private static ConcurrentMap<Integer, Class<? extends IMonitoringRecord>> createRecordMap() throws IOException, CLIConfigurationErrorException {
		if (commandLine.hasOption("L")) {
			final String[] libraries = commandLine.getOptionValues("L");

			if (commandLine.hasOption(CMD_MAP_FILE)) {
				final ConcurrentMap<Integer, Class<? extends IMonitoringRecord>> recordMap = CLIServerMain.readMapping(libraries,
						commandLine.getOptionValue(CMD_MAP_FILE));
				if (recordMap.isEmpty()) {
					throw new CLIConfigurationErrorException("At least one mapping must be specified in the mapping file.");
				}
				return recordMap;
			} else {
				throw new CLIConfigurationErrorException("Mapping file is required.");
			}
		} else {
			throw new CLIConfigurationErrorException("At least one library reference is required.");
		}
	}

	/**
	 * Interpret command line type option.
	 *
	 * @param lookupEntityMap
	 *            the map for ids to Kieker records
	 *
	 * @return a reference to an ServiceContainer
	 * @throws CLIConfigurationErrorException
	 *             if an error occured in setting up a connector or if an unknown service connector was specified
	 * @throws ConnectorDataTransmissionException
	 */
	private static IServiceConnector createService(final Configuration configuration, final ConcurrentMap<Integer, LookupEntity> lookupEntityMap)
			throws CLIConfigurationErrorException, ConnectorDataTransmissionException {
		try {
			return CLIServerMain.createConnector(ClassLoader.getSystemClassLoader().loadClass(configuration.getStringProperty(CLI_CONNECTOR)), configuration,
					lookupEntityMap);
		} catch (final ClassNotFoundException e) {
			throw new CLIConfigurationErrorException("Specified bridge connector " + configuration.getStringProperty(CLI_CONNECTOR) + " not found.", e);
		}
	}

	/**
	 * Create a connector instance for the given class.
	 *
	 * @param connector
	 *            the connector class
	 * @param configuration
	 *            configuration for the connector
	 * @param lookupEntityMap
	 *            lookup map
	 * @return o connector instance
	 * @throws CLIConfigurationErrorException
	 *             when the instantiation fails
	 */
	private static IServiceConnector createConnector(final Class<?> connector, final Configuration configuration,
			final ConcurrentMap<Integer, LookupEntity> lookupEntityMap) throws CLIConfigurationErrorException {
		try {
			return (IServiceConnector) connector.getConstructor(Configuration.class, ConcurrentMap.class).newInstance(configuration, lookupEntityMap);
		} catch (final IllegalArgumentException e) {
			throw new CLIConfigurationErrorException("Broken implementation of the selected connector " + connector.getCanonicalName(), e);
		} catch (final SecurityException e) {
			throw new CLIConfigurationErrorException("Cannot access included classes " + connector.getCanonicalName(), e);
		} catch (final InstantiationException e) {
			throw new CLIConfigurationErrorException("Instantiation failed for " + connector.getCanonicalName(), e);
		} catch (final IllegalAccessException e) {
			throw new CLIConfigurationErrorException("Access prohibited to class " + connector.getCanonicalName(), e);
		} catch (final InvocationTargetException e) {
			throw new CLIConfigurationErrorException("Broken implementation of the selected connector " + connector.getCanonicalName(), e);
		} catch (final NoSuchMethodException e) {
			throw new CLIConfigurationErrorException("Broken implementation of the selected connector " + connector.getCanonicalName(), e);
		}
	}

	/**
	 * Print out the server usage and an additional message describing the cause of the failure. Finally terminate the server.
	 *
	 * @param message
	 *            the message to be printed
	 * @param code
	 *            the exit code
	 */
	private static void usage(final String message) {
		CLIServerMain.getLog().error(message);
		final HelpFormatter formatter = new CLIHelpFormatter();
		formatter.printHelp("cli-kieker-service", options, true);
	}

	/**
	 * Read the CLI server Kieker classes to id mapping file.
	 *
	 * @param libraries
	 *            array representing a list of library files (*.jar)
	 * @param filename
	 *            the path of the mapping file.
	 *
	 * @return a complete IMonitoringRecord to id mapping
	 * @throws IOException
	 *             If one or more of the given library URLs is somehow invalid or one of the given files could not be accessed.
	 */
	@SuppressWarnings("unchecked")
	private static ConcurrentMap<Integer, Class<? extends IMonitoringRecord>> readMapping(final String[] libraries, final String filename) throws IOException {
		final ConcurrentMap<Integer, Class<? extends IMonitoringRecord>> map = new ConcurrentHashMap<Integer, Class<? extends IMonitoringRecord>>();
		final URL[] urls = new URL[libraries.length];
		for (int i = 0; i < libraries.length; i++) {
			urls[i] = new File(libraries[i]).toURI().toURL();
		}

		final PrivilegedClassLoaderAction action = new PrivilegedClassLoaderAction(urls);
		classLoader = AccessController.doPrivileged(action);

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
			String line = null;
			do {
				try {
					line = in.readLine();
					if (line != null) {
						final String[] pair = line.split("=");
						if (pair.length == 2) {
							// loadClass returns objects of type Class<?> while we only accept Class<? extends IMonitoringRecord> at the moment. This causes an
							// warning which
							// is suppressed by the SuppressWarning annotation.
							map.put(Integer.parseInt(pair[0]), (Class<IMonitoringRecord>) classLoader.loadClass(pair[1]));
						}
					}
				} catch (final ClassNotFoundException e) {
					CLIServerMain.getLog().warn("Could not load class", e);
				}
			} while (line != null);
		} finally {
			if (in != null) {
				in.close();
			}
		}

		return map;
	}

	/**
	 * Compile the options for the CLI server.
	 *
	 * @return The composed options for the CLI server
	 */
	private static Options declareOptions() {
		options = new Options();
		Option option;

		// Type selection
		option = new Option(CMD_TYPE, CMD_TYPE_LONG, true,
				"select the service type: tcp-client, tcp-server, tcp-single-server, jms-client, jms-embedded, http-rest");
		option.setArgName("type");
		option.setRequired(true);
		options.addOption(option);

		// TCP client
		option = new Option(CMD_HOST, CMD_HOST_LONG, true, "connect to server named <hostname>");
		option.setArgName("hostname");
		options.addOption(option);

		// TCP server
		option = new Option(CMD_PORT, CMD_PORT_LONG, true, "listen at port (tcp-server, jms-embedded, or http-rest) or connect to port (tcp-client)");
		option.setArgName("number");
		option.setType(Number.class);
		options.addOption(option);

		// JMS client
		option = new Option(CMD_USER, CMD_USER_LONG, true, "user name for a JMS service");
		option.setArgName("username");
		options.addOption(option);
		option = new Option(CMD_PASSWORD, CMD_PASSWORD_LONG, true, "password for a JMS service");
		option.setArgName("password");
		options.addOption(option);
		option = new Option(CMD_URL, CMD_URL_LONG, true, "URL for JMS server or HTTP servlet");
		option.setArgName("jms-url");
		option.setType(URL.class);
		options.addOption(option);

		// HTTP client
		option = new Option(CMD_CONTEXT, CMD_CONTEXT_LONG, true, "context for the HTTP servlet");
		option.setArgName("context");
		options.addOption(option);

		// kieker configuration file
		option = new Option(CMD_KIEKER_CONFIGURATION, CMD_KIEKER_CONFIGURATION_LONG, true, "kieker configuration file");
		option.setArgName("configuration");
		options.addOption(option);

		// mapping file for TCP and JMS
		option = new Option(CMD_MAP_FILE, CMD_MAP_FILE_LONG, true, "Class name to id (integer or string) mapping");
		option.setArgName("map-file");
		option.setType(File.class);
		option.setRequired(true);
		options.addOption(option);

		// libraries
		option = new Option(CMD_LIBRARIES, CMD_LIBRARIES_LONG, true, "List of library paths separated by " + File.pathSeparatorChar);
		option.setArgName("paths");
		option.setType(File.class);
		option.setRequired(true);
		option.setValueSeparator(File.pathSeparatorChar);
		options.addOption(option);

		// verbose
		option = new Option(CMD_VERBOSE, CMD_VERBOSE_LONG, true, "output processing information");
		option.setRequired(false);
		option.setOptionalArg(true);
		options.addOption(option);

		// statistics
		option = new Option(CMD_STATS, CMD_STATS_LONG, false, "output performance statistics");
		option.setRequired(false);
		options.addOption(option);

		// daemon mode
		option = new Option(CMD_DAEMON, CMD_DAEMON_LONG, false, "detach from console; TCP server allows multiple connections");
		option.setRequired(false);
		options.addOption(option);

		return options;
	}

	/**
	 * Check for pid file.
	 *
	 * @return A pid file object
	 * @throws IOException
	 *             if file definition fails or the file already exists
	 */
	private static File getPidFile() throws IOException {
		File pidFile = null;
		if (System.getProperty(DAEMON_FILE) != null) {
			pidFile = new File(System.getProperty(DAEMON_FILE));
		} else {
			if (new File(System.getProperty(JAVA_TMP_DIR)).exists()) {
				pidFile = new File(System.getProperty(JAVA_TMP_DIR) + "/kdb.pid");
			} else {
				throw new IOException("Java temp directory " + System.getProperty(JAVA_TMP_DIR) + " does not exist.");
			}
		}
		if (pidFile.exists()) {
			throw new IOException("PID file " + pidFile.getCanonicalPath() + " already exists, indicating the service is already running.");
		}

		return pidFile;
	}

	/**
	 * Return the logger.
	 *
	 * @return Returns the logger
	 */
	public static Log getLog() {
		return LOG;
	}
}
