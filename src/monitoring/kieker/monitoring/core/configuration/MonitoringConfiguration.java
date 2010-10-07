package kieker.monitoring.core.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import kieker.monitoring.core.MonitoringController;
import kieker.monitoring.writer.IMonitoringLogWriter;
import kieker.monitoring.writer.database.AsyncDbWriter;
import kieker.monitoring.writer.database.SyncDbWriter;
import kieker.monitoring.writer.filesystem.AsyncFsWriter;
import kieker.monitoring.writer.filesystem.SyncFsWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
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
 * ==================================================
 */
/**
 * Used to define the initial configuration of a monitoring controller.
 * 
 * @author Andre van Hoorn
 */
public final class MonitoringConfiguration implements IMonitoringConfiguration {

	private static final String KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME = "kieker.monitoring.configuration";

	private static final String KIEKER_CUSTOM_PROPERTIES_LOCATION_CLASSPATH = "META-INF/kieker.monitoring.properties";

	private static final String KIEKER_CUSTOM_PROPERTIES_LOCATION_DEFAULT = "META-INF/kieker.monitoring.properties.default";

	private static final String LOCAL_HOST_NAME;

	private static final Log log = LogFactory
			.getLog(MonitoringConfiguration.class);

	/**
	 * Name of the singleton instance
	 */
	private final static String SINGLETON_INSTANCE_NAME = "Singleton Instance";

	static {
		String hostname = "<UNKNOWN>";
		try {
			hostname = java.net.InetAddress.getLocalHost().getHostName();
		} catch (final Exception ex) {
			MonitoringConfiguration.log.warn("Failed to get hostname", ex);
		} finally {
			LOCAL_HOST_NAME = hostname;
		}
	}

	/**
	 * Creates a new configuration based on the given configuration file.
	 * 
	 * @param name
	 * @param configurationFile
	 * @return
	 * @throws IOException
	 */
	public static MonitoringConfiguration createConfiguration(
			final String name, final String configurationFile)
			throws IOException {
		final Properties props = MonitoringConfiguration
				.loadPropertiesFromFile(configurationFile);
		return new MonitoringConfiguration(name, props,
		/* Do not consider jvm arguments */
		false);
	}

	/**
	 * Creates a default configuration.
	 * 
	 * @param name
	 * @return
	 */
	public static MonitoringConfiguration createDefaultConfiguration(
			final String name) {
		final Properties defaultProps = ConfigurationProperty
				.defaultProperties();
		return new MonitoringConfiguration(name, defaultProps,
		/* Do not consider jvm arguments */
		false);
	}

	/**
	 * Creates a default configuration with the given monitoring log writer.
	 * 
	 * @param name
	 * @return
	 */
	public static MonitoringConfiguration createDefaultConfiguration(
			final IMonitoringLogWriter monitoringLogWriter, final String name) {
		final Properties defaultProps = ConfigurationProperty
				.defaultProperties();
		return new MonitoringConfiguration(name, defaultProps,
				monitoringLogWriter,
				/* Do not consider jvm arguments */
				false);
	}

	/**
	 * Creates the configuration for the singleton controller instance.
	 * 
	 * @return
	 */
	public static MonitoringConfiguration createSingletonConfiguration() {
		final Properties singletonProperties = MonitoringConfiguration
				.loadSingletonProperties();
		return new MonitoringConfiguration(
				MonitoringConfiguration.SINGLETON_INSTANCE_NAME,
				singletonProperties,
				/* Consider jvm arguments */
				true);
	}

	/**
	 * Returns the name of the localhost.
	 * 
	 * @return
	 */
	public final static String getLocalHostName() {
		return MonitoringConfiguration.LOCAL_HOST_NAME;
	}

	/**
	 * Returns the properties loaded from file propertiesFn.
	 * 
	 * @param propertiesFn
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static Properties loadPropertiesFromFile(final String propertiesFn)
			throws FileNotFoundException, IOException {
		final InputStream is = new FileInputStream(propertiesFn);
		final Properties prop = new Properties();
		prop.load(is);
		return prop;
	}

	/**
	 * Returns the properties loaded from the resource name or null if the
	 * resource could not be found.
	 * 
	 * @param classLoader
	 * @param name
	 * @return
	 * @throws IOException
	 */
	private static Properties loadPropertiesFromResource(final String name)
			throws IOException {
		final InputStream is = MonitoringController.class.getClassLoader()
				.getResourceAsStream(name);
		if (is == null) {
			return null;
		}
		final Properties prop = new Properties();

		prop.load(is);
		return prop;
	}

	/**
	 * Returns the properties used to construct the singleton instance. If a
	 * custom configuration file location is passed to the JVM using the
	 * property kieker.monitoring.configuration, this the properties are loaded
	 * from this file. Otherwise, the method searches for a configuration file
	 * META-INF/kieker.monitoring.properties in the classpath, and if this does
	 * not exist, it loads the default properties contained in the Kieker jar.
	 * 
	 * @return
	 */
	private static Properties loadSingletonProperties() {
		Properties prop = null; // = new Properties();
		String configurationFile = null;
		try {
			/* 1. Searching for configuration file location passed to JVM */
			if (System
					.getProperty(MonitoringConfiguration.KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME) != null) {
				configurationFile = System
						.getProperty(MonitoringConfiguration.KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME);
				MonitoringConfiguration.log
						.info("Loading properties from JVM-specified location '"
								+ configurationFile + "'");
				prop = MonitoringConfiguration
						.loadPropertiesFromFile(configurationFile);
			} else {
				/*
				 * 2. No JVM property; Trying to find configuration file in
				 * classpath
				 */
				configurationFile = MonitoringConfiguration.KIEKER_CUSTOM_PROPERTIES_LOCATION_CLASSPATH;
				prop = MonitoringConfiguration
						.loadPropertiesFromResource(configurationFile);
				if (prop != null) { // success
					MonitoringConfiguration.log
							.info("Loading properties from properties file in classpath: "
									+ configurationFile);
					MonitoringConfiguration.log
							.info("You can specify an alternative properties file using the property '"
									+ MonitoringConfiguration.KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME
									+ "'");
				} else {
					/*
					 * 3. No configuration file found in classpath; using
					 * default configuration.
					 */
					configurationFile = MonitoringConfiguration.KIEKER_CUSTOM_PROPERTIES_LOCATION_DEFAULT;
					MonitoringConfiguration.log
							.info("Loading properties from Kieker.Monitoring library jar!"
									+ configurationFile);
					MonitoringConfiguration.log
							.info("You can specify an alternative properties file using the property '"
									+ MonitoringConfiguration.KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME
									+ "'");
					prop = MonitoringConfiguration
							.loadPropertiesFromResource(configurationFile);
				}
			}
		} catch (final Exception ex) {
			MonitoringConfiguration.log.error(
					"Error loading kieker configuration file '"
							+ configurationFile + "'", ex);
			// TODO: introduce static variable 'terminated' or alike
		}
		return prop;
	}

	private boolean debugEnabled;

	private String hostName;

	private final String instanceName;

	private boolean monitoringEnabled;

	private IMonitoringLogWriter monitoringLogWriter;

	/**
	 * Must not be used for construction.
	 */
	private MonitoringConfiguration() {
		this.instanceName = null;
	}

	/**
	 * Constructs a configuration based on the given properties.
	 * 
	 * @param name
	 * @param properties
	 * @param considerSystemProperties
	 */
	private MonitoringConfiguration(final String name,
			final Properties properties, final boolean considerSystemProperties) {
		this.instanceName = name;
		this.initWriter(properties, considerSystemProperties);
		this.initVariables(properties, considerSystemProperties);
	}

	/**
	 * Constructs a configuration based on the given properties and monitoring
	 * log writer.
	 * 
	 * @param name
	 * @param properties
	 * @param monitoringLogWriter
	 * @param considerSystemProperties
	 */
	private MonitoringConfiguration(final String name,
			final Properties properties,
			final IMonitoringLogWriter monitoringLogWriter,
			final boolean considerSystemProperties) {
		this.instanceName = name;
		this.setMonitoringLogWriter(monitoringLogWriter);
		this.initVariables(properties, considerSystemProperties);
	}

	@Override
	public IMonitoringLogWriter createAndSetMonitoringLogWriter(
			final Class<? extends IMonitoringLogWriter> logWriterClass,
			final String initString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return this.instanceName;
	}

	@Override
	public String getHostName() {
		return this.hostName;
	}

	@Override
	public IMonitoringLogWriter getMonitoringLogWriter() {
		return this.monitoringLogWriter;
	}

	/**
	 * Initialize the instance based on the properties. Note, that the
	 * monitoring log writer is not initialized in this method.
	 * 
	 * @see #initWriter(Properties, boolean)
	 * @see #setMonitoringLogWriter(IMonitoringLogWriter)
	 * 
	 * @return true if the initialization was successful, false otherwise
	 */
	private boolean initVariables(final Properties props,
			final boolean considerSystemProperties) {
		/* Set the debug level */
		final boolean debugEnabled = this.loadBooleanProperty(props,
				ConfigurationProperty.DEBUG_ENABLED, considerSystemProperties);
		this.setDebugEnabled(debugEnabled);

		/* Sets whether monitoring is enabled or disabled */
		final boolean monitoringEnabled = this.loadBooleanProperty(props,
				ConfigurationProperty.MONITORING_ENABLED,
				considerSystemProperties);
		this.setMonitoringEnabled(monitoringEnabled);

		return true;
	}

	/**
	 * Initializes the monitoring log writer based on the properties.
	 * 
	 * @param props
	 * @param considerSystemProperties
	 * @return
	 */
	private boolean initWriter(final Properties props,
			final boolean considerSystemProperties) {
		/* Set the monitoring log writer */
		final IMonitoringLogWriter monitoringLogWriter = this.loadWriter(props,
				considerSystemProperties);
		if (monitoringLogWriter == null) {
			MonitoringConfiguration.log.error("Failed to load writer");
			return false;
		} else {
			this.monitoringLogWriter = monitoringLogWriter;
		}
		return true;
	}

	@Override
	public boolean isDebugEnabled() {
		return this.debugEnabled;
	}

	@Override
	public boolean isMonitoringEnabled() {
		return this.monitoringEnabled;
	}

	/**
	 * 
	 * @param props
	 * @param propertyName
	 * @param defaultValue
	 * @param considerSystemProperties
	 * @return
	 */
	private boolean loadBooleanProperty(final Properties props,
			final ConfigurationProperty property,
			final boolean considerSystemProperties) {
		final String stringValue = this.loadStringProperty(props, property,
				considerSystemProperties);

		return Boolean.parseBoolean(stringValue);
	}

	/**
	 * 
	 * @param props
	 * @param property
	 * @param considerSystemProperties
	 * @return
	 * @throws NumberFormatException
	 */
	private int loadIntProperty(final Properties props,
			final ConfigurationProperty property,
			final boolean considerSystemProperties)
			throws NumberFormatException {
		final String stringValue = this.loadStringProperty(props, property,
				considerSystemProperties);

		return Integer.parseInt(stringValue);
	}

	/**
	 * 
	 * @param props
	 * @param property
	 * @param considerSystemProperties
	 * @return
	 */
	private String loadStringProperty(final Properties props,
			final ConfigurationProperty property,
			final boolean considerSystemProperties) {
		String propertyValue;
		if (considerSystemProperties && property.hasJVMArgument()
				&& (System.getProperty(property.getJVMArgumentName()) != null)) {
			/* We use the present virtual machine parameter value */
			propertyValue = System.getProperty(property.getJVMArgumentName());
		} else {
			/* we use the value from the properties map */
			propertyValue = props.getProperty(property.getPropertyName());
		}

		if ((propertyValue == null) || propertyValue.isEmpty()) {
			propertyValue = property.defaultValue();
			MonitoringConfiguration.log.info("Missing value for property '"
					+ property.getPropertyName() + "' using default value "
					+ propertyValue);
		}

		return propertyValue;
	}

	/**
	 * Loads the writer based on the given properties
	 * 
	 * @param props
	 * @return the writer, null if the construction of the writer failed
	 */
	private IMonitoringLogWriter loadWriter(final Properties props,
			final boolean considerSystemProperties) {
		IMonitoringLogWriter monitoringLogWriter = null;

		try {
			final String monitoringDataWriterClassname = this
					.loadStringProperty(
							props,
							ConfigurationProperty.MONITORING_DATA_WRITER_CLASSNAME,
							considerSystemProperties);
			final String monitoringDataWriterInitString = this
					.loadStringProperty(
							props,
							ConfigurationProperty.MONITORING_DATA_WRITER_INIT_STRING,
							considerSystemProperties);

			if ((monitoringDataWriterClassname == null)
					|| (monitoringDataWriterClassname.isEmpty())) {
				MonitoringConfiguration.log
						.error("Property monitoringDataWriter not set");
				return null;
			} else if (monitoringDataWriterClassname
					.equals(ConfigurationFileConstants.WRITER_SYNCFS)) {
				/* Synchronous file system writer */
				final String filenameBase = this.loadStringProperty(props,
						ConfigurationProperty.FS_FN_PREFIX,
						considerSystemProperties);
				monitoringLogWriter = new SyncFsWriter(filenameBase,
						this.instanceName);
			} else if (monitoringDataWriterClassname
					.equals(ConfigurationFileConstants.WRITER_ASYNCFS)) {
				/* Asynchronous file system writer */
				final String filenameBase = this.loadStringProperty(props,
						ConfigurationProperty.FS_FN_PREFIX,
						considerSystemProperties);
				final int asyncRecordQueueSize = this.loadIntProperty(props,
						ConfigurationProperty.ASYNC__RECORD_QUEUE_SIZE,
						considerSystemProperties);
				final boolean asyncBlockOnFullQueue = this.loadBooleanProperty(
						props,
						ConfigurationProperty.ASYNC__BLOCK_ON_FULL_QUEUEU,
						considerSystemProperties);
				monitoringLogWriter = new AsyncFsWriter(filenameBase,
						this.instanceName, asyncRecordQueueSize,
						asyncBlockOnFullQueue);
			} else if (monitoringDataWriterClassname
					.equals(ConfigurationFileConstants.WRITER_SYNCDB)) {
				/* Synchronous database writer */
				final String dbDriverClassname = this.loadStringProperty(props,
						ConfigurationProperty.DB__DRIVER_CLASSNAME,
						considerSystemProperties);
				final String dbConnectionAddress = this.loadStringProperty(
						props, ConfigurationProperty.DB_CONNECTION_ADDRESS,
						considerSystemProperties);
				final String dbTableName = this.loadStringProperty(props,
						ConfigurationProperty.DB__TABLE_NAME,
						considerSystemProperties);
				final boolean setInitialExperimentIdBasedOnLastId = this
						.loadBooleanProperty(
								props,
								ConfigurationProperty.DB__SET_INITIAL_EXP_ID_BASED_ON_LAST,
								considerSystemProperties);
				monitoringLogWriter = new SyncDbWriter(dbDriverClassname,
						dbConnectionAddress, dbTableName,
						setInitialExperimentIdBasedOnLastId);
			} else if (monitoringDataWriterClassname
					.equals(ConfigurationFileConstants.WRITER_ASYNCDB)) {
				/* Asynchronous database writer */
				final String dbDriverClassname = this.loadStringProperty(props,
						ConfigurationProperty.DB__DRIVER_CLASSNAME,
						considerSystemProperties);
				final String dbConnectionAddress = this.loadStringProperty(
						props, ConfigurationProperty.DB_CONNECTION_ADDRESS,
						considerSystemProperties);
				final String dbTableName = this.loadStringProperty(props,
						ConfigurationProperty.DB__TABLE_NAME,
						considerSystemProperties);
				final boolean setInitialExperimentIdBasedOnLastId = this
						.loadBooleanProperty(
								props,
								ConfigurationProperty.DB__SET_INITIAL_EXP_ID_BASED_ON_LAST,
								considerSystemProperties);
				final int asyncRecordQueueSize = this.loadIntProperty(props,
						ConfigurationProperty.ASYNC__RECORD_QUEUE_SIZE,
						considerSystemProperties);
				final boolean asyncBlockOnFullQueue = this.loadBooleanProperty(
						props,
						ConfigurationProperty.ASYNC__BLOCK_ON_FULL_QUEUEU,
						considerSystemProperties);
				monitoringLogWriter = new AsyncDbWriter(dbDriverClassname,
						dbConnectionAddress, dbTableName,
						setInitialExperimentIdBasedOnLastId,
						asyncRecordQueueSize, asyncBlockOnFullQueue);
			} else {
				/* Load the writer by its classname */
				final int asyncRecordQueueSize = this.loadIntProperty(props,
						ConfigurationProperty.ASYNC__RECORD_QUEUE_SIZE,
						considerSystemProperties);
				monitoringLogWriter = (IMonitoringLogWriter) Class.forName(
						monitoringDataWriterClassname).newInstance();
				// add asyncRecordQueueSize
				// TODO: this is still a dirty hack!
				if (!monitoringLogWriter.init(monitoringDataWriterInitString
						+ " | asyncRecordQueueSize=" + asyncRecordQueueSize)) {
					monitoringLogWriter = null;
					throw new Exception("Initialization of writer failed!");
				}
			}
		} catch (final Exception e) {
			MonitoringConfiguration.log.error(
					"Error loading monitoring data writer", e);
			return null;
		}
		return monitoringLogWriter;
	}

	@Override
	public void setDebugEnabled(final boolean debugEnabled) {
		this.debugEnabled = debugEnabled;
	}

	@Override
	public void setHostName(final String hostName) {
		this.hostName = hostName;
	}

	@Override
	public void setMonitoringEnabled(final boolean monitoringEnabled) {
		this.monitoringEnabled = monitoringEnabled;
	}

	@Override
	public void setMonitoringLogWriter(
			final IMonitoringLogWriter monitoringLogWriter) {
		this.monitoringLogWriter = monitoringLogWriter;
	}
}
