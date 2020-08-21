/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.BooleanConverter;
import com.beust.jcommander.converters.FileConverter;
import com.beust.jcommander.converters.IntegerConverter;

import kieker.common.configuration.Configuration;
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

/**
 * The command line server of the KDB.
 *
 * @author Reiner Jung
 * @since 1.8
 *
 * @deprecated since 1.15 removed in 1.16 replaced by collector
 */
@Deprecated
public final class CLIServerMain { // NOPMD

	/**
	 * The logger used in the server.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CLIServerMain.class);

	private static final String DAEMON_FILE = "daemon.pidfile";

	private static final String JAVA_TMP_DIR = "java.io.tmpdir";

	private static final String CLI_CONNECTOR = "kieker.tools.bridge.connector";

	/** Type selection. */
	@Parameter(names = { "-t",
		"--type" }, required = true, description = "select the service type: tcp-client, tcp-server, tcp-single-server, jms-client, jms-embedded, http-rest")
	private String type;

	/** TCP client. */
	@Parameter(names = { "-h", "--host" }, description = "connect to server named <hostname>")
	private String hostname;

	/** TCP server. */
	@Parameter(names = { "-p",
		"--port" }, description = "listen at port (tcp-server, jms-embedded, or http-rest) or connect to port (tcp-client)", converter = IntegerConverter.class)
	private Integer port;

	/** JMS client. */
	@Parameter(names = { "-u", "--user" }, description = "user name for a JMS service")
	private String username;

	@Parameter(names = { "-w", "--password" }, description = "password for a JMS service")
	private String password;

	@Parameter(names = { "-l", "--url" }, description = "URL for JMS server or HTTP servlet")
	private String url;

	/** HTTP client. */
	@Parameter(names = { "-k", "--context" }, description = "context for the HTTP servlet")
	private String context;

	/** kieker configuration file. */
	@Parameter(names = { "-c",
		"--configuration" }, required = false, description = "kieker configuration file", converter = FileConverter.class)
	private File kiekerConfiguration;

	/** mapping file for TCP and JMS. */
	@Parameter(names = { "-m",
		"--map" }, description = "Class name to id (integer or string) mapping", converter = FileConverter.class)
	private File mappingFile;

	/** libraries. */
	@Parameter(names = { "-L",
		"--libraries" }, required = true, description = "List of library paths separated by :", converter = FileConverter.class)
	private File[] libraries;

	/** verbose. */
	@Parameter(names = { "-v", "--verbose" }, description = "defines the verbosity regarding record logging")
	private Integer verbose;

	/** statistics. */
	@Parameter(names = { "-s", "--stats" }, description = "output performance statistics")
	private boolean statistics;

	/** daemon mode. */
	@Parameter(names = { "-d",
		"--daemon" }, description = "detach from console; TCP server allows multiple connections", converter = BooleanConverter.class)
	private boolean daemonMode;

	private long startTime;
	private long deltaTime;

	private ServiceContainer container;

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
		final CLIServerMain main = new CLIServerMain();
		final JCommander commander = new JCommander(main);
		try {
			commander.parse(args);
			main.execute(commander);
		} catch (final ParameterException e) {
			System.err.println(e.getLocalizedMessage()); // NOPMD
			commander.usage();
		}
	}

	private void execute(final JCommander commander) {
		try {
			if (this.daemonMode) {
				System.out.close();
				System.err.close();
				this.getPidFile().deleteOnExit();
			}

			/** Find libraries and setup mapping. */
			final ConcurrentMap<Integer, LookupEntity> lookupEntityMap = ServiceConnectorFactory
					.createLookupEntityMap(this.createRecordMap());

			/** Kieker setup. */
			final Configuration configuration;
			if (this.kiekerConfiguration != null) {
				configuration = ConfigurationFactory
						.createConfigurationFromFile(this.kiekerConfiguration.getAbsolutePath());
			} else {
				configuration = ConfigurationFactory.createSingletonConfiguration();
			}

			// reconfigure kieker configuration
			if ((this.port != null) && (this.type != null)) {
				if ("jms-embedded".equals(this.type)) {
					configuration.setProperty(JMSEmbeddedConnector.PORT, this.port);
				} else if ("tcp-single-server".equals(this.type)) {
					configuration.setProperty(TCPSingleServerConnector.PORT, this.port);
				} else if ("tcp-server".equals(this.type)) {
					configuration.setProperty(TCPMultiServerConnector.PORT, this.port);
				} else if ("tcp-client".equals(this.type)) {
					configuration.setProperty(TCPClientConnector.PORT, this.port);
				} else if ("http-rest".equals(this.type)) {
					configuration.setProperty(HTTPConnector.PORT, this.port);
				}
			}
			if (this.hostname != null) {
				configuration.setProperty(TCPClientConnector.HOSTNAME, this.hostname);
			}
			if (this.username != null) {
				configuration.setProperty(JMSClientConnector.USERNAME, this.username);
			}
			if (this.password != null) {
				configuration.setProperty(JMSClientConnector.PASSWORD, this.password);
			}
			if (this.url != null) {
				configuration.setProperty(JMSClientConnector.URI, this.url);
				configuration.setProperty(HTTPConnector.REST_URL, this.url);
			}
			if (this.context != null) {
				configuration.setProperty(HTTPConnector.CONTEXT, this.context);
			}
			if (this.type != null) {
				final Reflections reflections = new Reflections("kieker.tools.bridge.connector");
				final Set<Class<?>> connectors = reflections
						.getTypesAnnotatedWith(kieker.tools.bridge.connector.ConnectorProperty.class);

				for (final Class<?> connector : connectors) {
					if (connector.getAnnotation(kieker.tools.bridge.connector.ConnectorProperty.class).cmdName()
							.equals(this.type)) {
						configuration.setProperty(CLIServerMain.CLI_CONNECTOR, connector.getCanonicalName());
						break;
					}
				}
			}

			// start service depending on type
			final IServiceConnector connector = this.createService(configuration, lookupEntityMap);
			CLIServerMain.getLogger().info("Service {}", connector.getClass().getAnnotation(kieker.tools.bridge.connector.ConnectorProperty.class).name());
			this.runService(configuration, connector);

		} catch (final IOException e) {
			this.usage(commander, 1, "Mapping file read error: " + e.getMessage());
		} catch (final CLIConfigurationErrorException e) {
			this.usage(commander, 2, "Configuration error: " + e.getMessage());
		} catch (final ConnectorDataTransmissionException e) {
			this.usage(commander, 3, "Communication error: " + e.getMessage());
		}
	}

	/**
	 * Execute the bridge service.
	 *
	 * @param connector
	 *
	 * @throws ConnectorDataTransmissionException
	 *             if an error occurred during connector operations
	 */
	private void runService(final Configuration configuration, final IServiceConnector connector)
			throws ConnectorDataTransmissionException {
		// setup service container
		this.container = new ServiceContainer(configuration, connector, false);

		if (this.verbose != null) {
			this.container.setListenerUpdateInterval(this.verbose); // NOCS
			this.container.addListener(new IServiceListener() {
				@Override
				public void handleEvent(final long count, final String message) {
					CLIServerMain.getLogger().info("Received {} records", count);
				}
			});
		}

		if (this.statistics) {
			this.startTime = System.nanoTime();
		}

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					CLIServerMain.this.shutdown();
				} catch (final ConnectorDataTransmissionException e) {
					CLIServerMain.getLogger().error("Graceful shutdown failed.");
					CLIServerMain.getLogger().error("Cause {}", e.getMessage());
				}
			}
		});

		/** run the service. */
		this.container.run();

		if (this.statistics) {
			this.deltaTime = System.nanoTime() - this.startTime;
		}
		if (this.verbose != null) {
			CLIServerMain.getLogger().info("Server stopped.");
		}
		if (this.statistics) {
			CLIServerMain.getLogger().info("Execution time: {} ns {} s", this.deltaTime, TimeUnit.SECONDS.convert(this.deltaTime, TimeUnit.NANOSECONDS));
			CLIServerMain.getLogger().info("Time per records: {} ns/r", (this.deltaTime / this.container.getRecordCount()));
			CLIServerMain.getLogger().info("Records per second: {}",
					(this.container.getRecordCount() / (double) TimeUnit.SECONDS.convert(this.deltaTime, TimeUnit.NANOSECONDS)));
		}
	}

	/**
	 * Hook for the shutdown thread so it can access the container's shutdown
	 * routine.
	 *
	 * @throws ConnectorDataTransmissionException
	 *             if any internal shutdown calls fail
	 */
	protected void shutdown() throws ConnectorDataTransmissionException {
		this.container.shutdown();
	}

	/**
	 * Create a record map of classes implementing IMonitoringRecord interface out
	 * of libraries with such classes and a textual mapping file.
	 *
	 * @return A record map. null is never returned as a call of usage terminates
	 *         the program.
	 * @throws IOException
	 *             if an error occured reading the mapping file
	 * @throws CLIConfigurationErrorException
	 *             if a configuration error occured
	 */
	private ConcurrentMap<Integer, Class<? extends IMonitoringRecord>> createRecordMap()
			throws IOException, CLIConfigurationErrorException {
		if (this.libraries.length > 0) {
			if (this.mappingFile != null) {
				final ConcurrentMap<Integer, Class<? extends IMonitoringRecord>> recordMap = this.readMapping();
				if (recordMap.isEmpty()) {
					throw new CLIConfigurationErrorException(
							"At least one mapping must be specified in the mapping file.");
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
	 *             if an error occured in setting up a connector or if an unknown
	 *             service connector was specified
	 * @throws ConnectorDataTransmissionException
	 */
	private IServiceConnector createService(final Configuration configuration,
			final ConcurrentMap<Integer, LookupEntity> lookupEntityMap)
			throws CLIConfigurationErrorException, ConnectorDataTransmissionException {
		try {
			return this.createConnector(
					ClassLoader.getSystemClassLoader()
							.loadClass(configuration.getStringProperty(CLIServerMain.CLI_CONNECTOR)),
					configuration, lookupEntityMap);
		} catch (final ClassNotFoundException e) {
			throw new CLIConfigurationErrorException("Specified bridge connector "
					+ configuration.getStringProperty(CLIServerMain.CLI_CONNECTOR) + " not found.", e);
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
	private IServiceConnector createConnector(final Class<?> connector, final Configuration configuration,
			final ConcurrentMap<Integer, LookupEntity> lookupEntityMap) throws CLIConfigurationErrorException {
		try {
			return (IServiceConnector) connector.getConstructor(Configuration.class, ConcurrentMap.class)
					.newInstance(configuration, lookupEntityMap);
		} catch (final IllegalArgumentException e) {
			throw new CLIConfigurationErrorException(
					"Broken implementation of the selected connector " + connector.getCanonicalName(), e);
		} catch (final SecurityException e) {
			throw new CLIConfigurationErrorException("Cannot access included classes " + connector.getCanonicalName(),
					e);
		} catch (final InstantiationException e) {
			throw new CLIConfigurationErrorException("Instantiation failed for " + connector.getCanonicalName(), e);
		} catch (final IllegalAccessException e) {
			throw new CLIConfigurationErrorException("Access prohibited to class " + connector.getCanonicalName(), e);
		} catch (final InvocationTargetException e) {
			throw new CLIConfigurationErrorException(
					"Broken implementation of the selected connector " + connector.getCanonicalName(), e);
		} catch (final NoSuchMethodException e) {
			throw new CLIConfigurationErrorException(
					"Broken implementation of the selected connector " + connector.getCanonicalName(), e);
		}
	}

	/**
	 * Read the CLI server Kieker classes to id mapping file.
	 *
	 * @return a complete IMonitoringRecord to id mapping
	 * @throws IOException
	 *             If one or more of the given library URLs is somehow invalid or
	 *             one of the given files could not be accessed.
	 */
	@SuppressWarnings("unchecked")
	private ConcurrentMap<Integer, Class<? extends IMonitoringRecord>> readMapping() throws IOException {
		final ConcurrentMap<Integer, Class<? extends IMonitoringRecord>> map = new ConcurrentHashMap<>();
		final URL[] urls = new URL[this.libraries.length];
		for (int i = 0; i < this.libraries.length; i++) {
			urls[i] = this.libraries[i].toURI().toURL();
		}

		final PrivilegedClassLoaderAction action = new PrivilegedClassLoaderAction(urls);
		final URLClassLoader classLoader = AccessController.doPrivileged(action);

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(this.mappingFile), "UTF-8"));
			String line = null;
			do {
				try {
					line = in.readLine();
					if (line != null) {
						final String[] pair = line.split("=");
						if (pair.length == 2) {
							/**
							 * loadClass returns objects of type Class<?> while we only accept Class<?
							 * extends IMonitoringRecord> at the moment. This causes an warning which is
							 * suppressed by the SuppressWarning annotation.
							 */
							map.put(Integer.parseInt(pair[0]),
									(Class<IMonitoringRecord>) classLoader.loadClass(pair[1]));
						}
					}
				} catch (final ClassNotFoundException e) {
					CLIServerMain.getLogger().warn("Could not load class", e);
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
	 * Print out the server usage and an additional message describing the cause of
	 * the failure. Finally terminate the server.
	 *
	 * @param exitCode
	 *            exit code
	 * @param commander
	 *            jcommander
	 *
	 * @param message
	 *            the message to be printed
	 * @param code
	 *            the exit code
	 */
	//@SuppressFBWarnings(justification = "exit necessary here to indicate cause to shell")
	private void usage(final JCommander commander, final int exitCode, final String message) {
		final StringBuilder out = new StringBuilder();
		commander.usage(out, message);
		CLIServerMain.getLogger().error(out.toString());
		System.exit(exitCode);
	}

	/**
	 * Check for pid file.
	 *
	 * @return A pid file object
	 * @throws IOException
	 *             if file definition fails or the file already exists
	 */
	private File getPidFile() throws IOException {
		File pidFile = null;
		if (System.getProperty(CLIServerMain.DAEMON_FILE) != null) {
			pidFile = new File(System.getProperty(CLIServerMain.DAEMON_FILE));
		} else {
			if (new File(System.getProperty(CLIServerMain.JAVA_TMP_DIR)).exists()) {
				pidFile = new File(System.getProperty(CLIServerMain.JAVA_TMP_DIR) + "/kdb.pid");
			} else {
				throw new IOException(
						"Java temp directory " + System.getProperty(CLIServerMain.JAVA_TMP_DIR) + " does not exist.");
			}
		}
		if (pidFile.exists()) {
			throw new IOException("PID file " + pidFile.getCanonicalPath()
					+ " already exists, indicating the service is already running.");
		}

		return pidFile;
	}

	/**
	 * Return the logger.
	 *
	 * @return Returns the logger
	 */
	public static Logger getLogger() {
		return CLIServerMain.LOGGER;
	}
}
