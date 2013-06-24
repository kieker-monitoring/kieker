/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.tools.bridge.IServiceListener;
import kieker.tools.bridge.ServiceContainer;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.IServiceConnector;
import kieker.tools.bridge.connector.ServiceConnectorFactory;

/**
 * 
 * 
 * @author Reiner Jung
 * @since 1.8
 */
public final class CLIServerMain {

	protected static final Log LOG = LogFactory.getLog(CLIServerMain.class);

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

	private static String CMD_MAP_FILE = "m";
	private static String CMD_MAP_FILE_LONG = "map";

	private static boolean verbose;
	private static boolean stats;
	private static long startTime;
	private static long deltaTime;

	private static Options options;
	private static CommandLine commandLine;

	private CLIServerMain() {
		// private default constructor
	}

	/**
	 * CLS server main.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(final String[] args) {
		CLIServerMain.declareOptions();
		try {
			// parse the command line arguments
			commandLine = new BasicParser().parse(options, args);

			// verbosity setup
			verbose = commandLine.hasOption(CMD_VERBOSE);

			// statistics
			stats = commandLine.hasOption(CMD_STATS);

			// Find libraries and setup mapping
			final Map<Integer, Class<IMonitoringRecord>> recordMap = CLIServerMain.createRecordMap();

			// Kieker setup
			Configuration configuration = null;
			if (commandLine.hasOption("c")) {
				configuration = ConfigurationFactory.createConfigurationFromFile(commandLine.getOptionValue("c"));
			} else {
				configuration = ConfigurationFactory.createSingletonConfiguration();
			}

			// start service depending on type
			if (commandLine.hasOption(CMD_TYPE)) {
				CLIServerMain.runService(new ServiceContainer(configuration, CLIServerMain.createService(recordMap), false));
			}
		} catch (final ParseException e) {
			CLIServerMain.usage("Parsing failed.  Reason: " + e.getMessage(), 20);
		} catch (final IOException e) {
			CLIServerMain.usage("Mapping file read error: " + e.getMessage(), 1);
		} catch (final CLIConfigurationErrorException e) {
			CLIServerMain.usage("Configuration error: " + e.getMessage(), 6);
		} catch (final ConnectorDataTransmissionException e) {
			CLIServerMain.usage("Communication error: " + e.getMessage(), 2);
		}
	}

	/**
	 * Execute the bridge service.
	 * 
	 * @param service
	 *            The service to be executed
	 * @throws ConnectorDataTransmissionException
	 *             if an error occured during connector operations
	 */
	private static void runService(final ServiceContainer service) throws ConnectorDataTransmissionException {
		if (verbose) {
			final String updateInterval = commandLine.getOptionValue(CMD_VERBOSE);
			service.setListenerUpdateInterval((updateInterval != null) ? Integer.parseInt(updateInterval) : 100);
			service.addListener(new IServiceListener() {
				public void handleEvent(final long count, final String message) {
					LOG.info("Received " + count + " records");
				}
			});
		}

		if (stats) {
			startTime = System.nanoTime();
		}
		// run the service
		service.run();

		if (stats) {
			deltaTime = System.nanoTime() - startTime;
		}
		if (verbose) {
			System.out.println("TCP server stopped.");
		}
		if (stats) {
			System.out.println("Execution time: " + deltaTime + " ns  " + TimeUnit.SECONDS.convert(deltaTime, TimeUnit.NANOSECONDS) + " s");
			System.out.println("Time per records: " + (deltaTime / service.getRecordCount()) + " ns/r");
			System.out.println("Records per second: " + (service.getRecordCount() / (double) TimeUnit.SECONDS.convert(deltaTime, TimeUnit.NANOSECONDS)));
		}
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
	private static Map<Integer, Class<IMonitoringRecord>> createRecordMap() throws IOException, CLIConfigurationErrorException {
		if (commandLine.hasOption("L")) {
			final String[] libraries = commandLine.getOptionValues("L");

			if (commandLine.hasOption(CMD_MAP_FILE)) {
				final Map<Integer, Class<IMonitoringRecord>> recordMap = CLIServerMain.readMapping(libraries, commandLine.getOptionValue(CMD_MAP_FILE));
				if (recordMap.size() == 0) {
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
	 * @param recordList
	 *            the map for ids to Kieker records
	 * 
	 * @return a reference to an ServiceContainer
	 * @throws CLIConfigurationErrorException
	 *             if an error occured in setting up a connector or if an unknown service connector was specified
	 */
	private static IServiceConnector createService(final Map<Integer, Class<IMonitoringRecord>> recordList) throws CLIConfigurationErrorException {
		if ("tcp-client".equals(commandLine.getOptionValue("type"))) {
			return CLIServerMain.createTCPClientService(recordList);
		} else if ("tcp-single-server".equals(commandLine.getOptionValue("type"))) {
			return CLIServerMain.createTCPSingleServerService(recordList);
		} else if ("tcp-server".equals(commandLine.getOptionValue("type"))) {
			return CLIServerMain.createTCPMultiServerService(recordList);
		} else if ("jms-client".equals(commandLine.getOptionValue("type"))) {
			return CLIServerMain.createJMSService(recordList);
		} else if ("jms-embedded".equals(commandLine.getOptionValue("type"))) {
			return CLIServerMain.createJMSEmbeddedService(recordList);
		} else {
			throw new CLIConfigurationErrorException("Unknown service type: '" + commandLine.getOptionValue("type") + "'");
		}
	}

	/**
	 * Create a JMSEmbeddedService.
	 * 
	 * @param recordList
	 *            the map for ids to Kieker records
	 * 
	 * @return a reference to an ServiceContainer
	 * @throws CLIConfigurationErrorException
	 *             if no port was specified for the embedded JMS server or the generated URI is malformed
	 */
	private static IServiceConnector createJMSEmbeddedService(final Map<Integer, Class<IMonitoringRecord>> recordList) throws CLIConfigurationErrorException {
		if (commandLine.hasOption("port")) {
			final int port = Integer.parseInt(commandLine.getOptionValue("port"));
			try {
				return ServiceConnectorFactory.createJMSEmbeddedServiceConnector(recordList, port);
			} catch (final URISyntaxException e) {
				throw new CLIConfigurationErrorException("JMS service cannot be started. URI problem.", e);
			}
		} else {
			throw new CLIConfigurationErrorException("Missing port for embedded server.");
		}
	}

	/**
	 * Create a JMSService.
	 * 
	 * @param recordList
	 *            the map for ids to Kieker records
	 * 
	 * @return a reference to an ServiceContainer
	 * @throws CLIConfigurationErrorException
	 *             if no JMS service URL was specified
	 */
	private static IServiceConnector createJMSService(final Map<Integer, Class<IMonitoringRecord>> recordList) throws CLIConfigurationErrorException {
		final String username = commandLine.hasOption("u") ? commandLine.getOptionValue("u") : null;
		final String password = commandLine.hasOption("w") ? commandLine.getOptionValue("w") : null;
		if (commandLine.hasOption("url")) {
			try {
				return ServiceConnectorFactory.createJMSServiceConnector(recordList, username, password, new URI(commandLine.getOptionValue("url")));
			} catch (final URISyntaxException e) {
				throw new CLIConfigurationErrorException(commandLine.getOptionValue("url") + " is not a valid URI. JMS service cannot be started.", e);
			}
		} else {
			throw new CLIConfigurationErrorException("Missing URL for JMS service");
		}
	}

	/**
	 * Create a TCPSingleServerService.
	 * 
	 * @param recordList
	 *            the map for ids to Kieker records
	 * 
	 * @return a reference to an ServiceContainer
	 * @throws CLIConfigurationErrorException
	 *             if no server port was specified
	 */
	private static IServiceConnector createTCPSingleServerService(final Map<Integer, Class<IMonitoringRecord>> recordList) throws CLIConfigurationErrorException {
		if (commandLine.hasOption("port")) {
			final int port = Integer.parseInt(commandLine.getOptionValue("port"));
			final IServiceConnector service = ServiceConnectorFactory.createTCPSingleServerServiceConnector(recordList, port);
			if (verbose) {
				System.out.println("TCP server listening at " + port);
			}
			return service;
		} else {
			throw new CLIConfigurationErrorException("Missing port for tcp server");
		}
	}

	/**
	 * Create a TCPMultiServerService.
	 * 
	 * @param recordList
	 *            the map for ids to Kieker records
	 * 
	 * @return a reference to an ServiceContainer
	 * @throws CLIConfigurationErrorException
	 *             if no server port was specified
	 */
	private static IServiceConnector createTCPMultiServerService(final Map<Integer, Class<IMonitoringRecord>> recordList) throws CLIConfigurationErrorException {
		if (commandLine.hasOption("port")) {
			final int port = Integer.parseInt(commandLine.getOptionValue("port"));
			final IServiceConnector service = ServiceConnectorFactory.createTCPMultiServerServiceConnector(recordList, port);
			if (verbose) {
				System.out.println("TCP server listening at " + port);
			}
			return service;
		} else {
			throw new CLIConfigurationErrorException("Missing port for tcp server");
		}
	}

	/**
	 * Create a TCPCLientService.
	 * 
	 * @param recordList
	 *            the map for ids to Kieker records
	 * 
	 * @return a reference to an ServiceContainer
	 * @throws CLIConfigurationErrorException
	 *             if a host name or port is missing for the client
	 */
	private static IServiceConnector createTCPClientService(final Map<Integer, Class<IMonitoringRecord>> recordList) throws CLIConfigurationErrorException {
		if (commandLine.hasOption("port")) {
			if (commandLine.hasOption("host")) {
				final int port = Integer.parseInt(commandLine.getOptionValue("port"));
				final String hostname = commandLine.getOptionValue("host");
				final IServiceConnector service = ServiceConnectorFactory.createTCPClientServiceConnector(recordList, hostname, port);
				if (verbose) {
					LOG.info("TCP client connected to " + hostname + ":" + port);
				}
				return service;
			} else {
				throw new CLIConfigurationErrorException("Missing hostname for tcp client");
			}
		} else {
			throw new CLIConfigurationErrorException("Missing port for tcp client");
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
	private static void usage(final String message, final int code) {
		System.err.println(message);
		// TODO: we have a custom formatted HelpFormatter somewhere.
		final HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("cli-kieker-service", options, true);
		System.exit(code);
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
	private static Map<Integer, Class<IMonitoringRecord>> readMapping(final String[] libraries, final String filename) throws IOException {
		final Map<Integer, Class<IMonitoringRecord>> map = new ConcurrentHashMap<Integer, Class<IMonitoringRecord>>();
		final URL[] urls = new URL[libraries.length];
		for (int i = 0; i < libraries.length; i++) {
			urls[i] = new File(libraries[i]).toURI().toURL();
		}
		// TODO: Consider using Privileged Action instead
		final URLClassLoader classLoader = new URLClassLoader(urls, CLIServerMain.class.getClassLoader());

		BufferedReader in = null;
		try {
			// TODO: Use the correct encoding
			in = new BufferedReader(new FileReader(filename));

			String line = null;
			do {
				try {
					line = in.readLine();
					if (line != null) {
						final String[] pair = line.split("=");
						if (pair.length == 2) {
							// loadClass returns objects of type Class<?> while we only accept Class<IMonitoringRecord> at the moment. This causes an warning which
							// is suppressed by the SuppressWarning annotation.
							map.put(Integer.parseInt(pair[0]), (Class<IMonitoringRecord>) classLoader.loadClass(pair[1]));
						}
					}
				} catch (final ClassNotFoundException e) {
					LOG.warn("Could not load class", e);
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
		option = new Option(CMD_TYPE, CMD_TYPE_LONG, true, "select the service type: tcp-client, tcp-server, tcp-single-server, jms-client, jms-embedded");
		option.setArgName("type");
		option.setRequired(true);
		options.addOption(option);

		// TCP client
		option = new Option(CMD_HOST, CMD_HOST_LONG, true, "connect to server named <hostname>");
		option.setArgName("hostname");
		options.addOption(option);

		// TCP server
		option = new Option(CMD_PORT, CMD_PORT_LONG, true, "listen at port (tcp-server or jms-embedded) or connect to port (tcp-client)");
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
		option = new Option(CMD_URL, CMD_URL_LONG, true, "URL for JMS server");
		option.setArgName("jms-url");
		option.setType(URL.class);
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
}
