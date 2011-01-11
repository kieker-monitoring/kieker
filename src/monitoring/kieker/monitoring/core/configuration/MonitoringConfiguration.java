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
 * <p>
 * 
 * Use the factory methods to create instances:
 * 
 * <ul>
 * <li>{@link #createConfiguration(String, String)}</li>
 * <li>{@link #createDefaultConfiguration(String, IMonitoringLogWriter)}</li>
 * <li>{@link #createDefaultConfiguration(String, Class, String)}</li>
 * <li>{@link #createSingletonConfiguration()}</li>
 * </ul>
 * 
 * 
 * @author Andre van Hoorn
 */
public final class MonitoringConfiguration implements IMonitoringConfiguration {

	public static final String KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME =
			"kieker.monitoring.configuration";

	public static final String KIEKER_CUSTOM_PROPERTIES_LOCATION_CLASSPATH =
			"META-INF/kieker.monitoring.properties";

	public static final String KIEKER_CUSTOM_PROPERTIES_LOCATION_DEFAULT =
			"META-INF/kieker.monitoring.properties.default";

	private static final String LOCAL_HOST_NAME;

	public static final int DEFAULT_ASYNC_RECORD_QUEUE_SIZE = 8000;
	public static final boolean DEFAULT_ASYNC__BLOCK_ON_FULL_QUEUE = false;

	public final static int DEFAULT_EXECUTOR_THREAD_POOL_SIZE = 1;

	private static final Log log = LogFactory
			.getLog(MonitoringConfiguration.class);

	/**
	 * Name of the singleton instance
	 */
	private final static String SINGLETON_INSTANCE_NAME = "SINGLETON";

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
	 * @param configurationName
	 * @param configurationFile
	 * @return
	 * @throws IOException
	 */
	public static MonitoringConfiguration createConfiguration(
			final String configurationName, final String configurationFile)
			throws IOException {
		final Properties props = MonitoringConfiguration
				.loadPropertiesFromFile(configurationFile);
		final MonitoringConfiguration retVal = new MonitoringConfiguration(
				configurationName, props,
				/* Do not consider jvm arguments */
				false);

		return retVal;
	}

	/**
	 * Creates a default configuration with the given monitoring log writer.
	 * 
	 * @param configurationName
	 * @return
	 */
	public static MonitoringConfiguration createDefaultConfiguration(
			final String configurationName,
			final IMonitoringLogWriter monitoringLogWriter) {
		final Properties defaultProps = ConfigurationProperty
				.defaultProperties();
		final MonitoringConfiguration retVal = new MonitoringConfiguration(
				configurationName, defaultProps, monitoringLogWriter,
				/* Do not consider jvm arguments */
				false);
		return retVal;
	}

	/**
	 * Creates a default configuration with the given monitoring log writer.
	 * 
	 * @param configurationName
	 * @return
	 */
	public static MonitoringConfiguration createDefaultConfiguration(
			final String configurationName,
			final Class<? extends IMonitoringLogWriter> logWriterClass,
			final String initString) {
		final IMonitoringLogWriter logWriter =
				MonitoringConfiguration
						.loadWriterByClassAndInitString(
								initString,
								Integer.parseInt(ConfigurationProperty.ASYNC__RECORD_QUEUE_SIZE
										.getDefaultValue()), logWriterClass);
		final MonitoringConfiguration retVal = MonitoringConfiguration
				.createDefaultConfiguration(configurationName, logWriter);
		return retVal;
	}

	/**
	 * Creates a default configuration using the {@link AsyncFsWriter} with the
	 * given properties. You way set the value storagePathBaseDir to the default
	 * temporary directory using
	 * <code>System.getProperty("java.io.tmpdir")</code>.
	 * 
	 * @see #DEFAULT_ASYNC_RECORD_QUEUE_SIZE
	 * @see #DEFAULT_ASYNC__BLOCK_ON_FULL_QUEUE
	 * @see AsyncFsWriter
	 * 
	 * @param configurationName
	 * @param storagePathBaseDir
	 * @param asyncRecordQueueSize
	 * @param blockOnFullQueue
	 * @return
	 */
	public static MonitoringConfiguration createDefaultConfigurationAsyncFSWriter(
			final String configurationName, final String storagePathBaseDir,
			final int asyncRecordQueueSize, final boolean blockOnFullQueue) {
		final IMonitoringLogWriter monitoringLogWriter =
				new AsyncFsWriter(storagePathBaseDir, configurationName,
						asyncRecordQueueSize, blockOnFullQueue);
		return MonitoringConfiguration.createDefaultConfiguration(
				configurationName, monitoringLogWriter);
	}

	// TODO: Factory methods for SyncFSWriter, SyncDBWriter, AsyncDBWriter

	/**
	 * Creates the configuration for the singleton controller instance. Note
	 * that the {@link MonitoringConfiguration} returned by this method is not a
	 * singleton instance, i.e., each call returns an equal but not same
	 * {@link MonitoringConfiguration}.
	 * 
	 * @return
	 */
	public static MonitoringConfiguration createSingletonConfiguration() {
		final Properties singletonProperties = MonitoringConfiguration
				.loadSingletonProperties();
		final MonitoringConfiguration retVal = new MonitoringConfiguration(
				MonitoringConfiguration.SINGLETON_INSTANCE_NAME,
				singletonProperties,
				/* Consider jvm arguments */
				true);
		return retVal;
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
		MonitoringConfiguration.log.info("Loading configuration from file '"
				+ propertiesFn + "' ...");
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
			MonitoringConfiguration.log.warn("Failed to load resource '" + name
					+ "'");
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
		MonitoringConfiguration.log
				.info("Locating configuration file for singleton instance ...");
		try {
			/* 1. Searching for configuration file location passed to JVM */
			MonitoringConfiguration.log
					.info("1. Searching for JVM argument '"
							+ MonitoringConfiguration.KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME
							+ "' ...");
			if (System
					.getProperty(MonitoringConfiguration.KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME) != null) {
				configurationFile =
						System
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
				MonitoringConfiguration.log
						.info("2. No JVM argument; Trying to find configuration file '"
								+ MonitoringConfiguration.KIEKER_CUSTOM_PROPERTIES_LOCATION_CLASSPATH
								+ "' in classpath ...");
				configurationFile =
						MonitoringConfiguration.KIEKER_CUSTOM_PROPERTIES_LOCATION_CLASSPATH;
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
					MonitoringConfiguration.log
							.info("3. No configuration file in classpath; Using default configuration ...");
					configurationFile =
							MonitoringConfiguration.KIEKER_CUSTOM_PROPERTIES_LOCATION_DEFAULT;
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

	private String hostName = MonitoringConfiguration.LOCAL_HOST_NAME; // default

	private final String instanceName;

	private boolean monitoringEnabled;

	private IMonitoringLogWriter monitoringLogWriter;

	private int periodicSensorsExecutorPoolSize;

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
		this.initWriter(properties, considerSystemProperties, this.instanceName);
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
		final boolean debugEnabled = MonitoringConfiguration
				.loadBooleanConfigurationProperty(props,
						ConfigurationProperty.DEBUG_ENABLED,
						considerSystemProperties);
		this.setDebugEnabled(debugEnabled);

		/* Sets whether monitoring is enabled or disabled */
		final boolean monitoringEnabled = MonitoringConfiguration
				.loadBooleanConfigurationProperty(props,
						ConfigurationProperty.MONITORING_ENABLED,
						considerSystemProperties);
		this.setMonitoringEnabled(monitoringEnabled);

		/* Sets the thread pool size of the executor for periodic sensors */
		final int periodicSensorsExecutorPoolSize =
				MonitoringConfiguration
						.loadIntConfigurationProperty(
								props,
								ConfigurationProperty.PERIODIC_SENSORS_EXECUTOR_POOL_SIZE,
								considerSystemProperties);
		this.setPeriodicSensorsExecutorPoolSize(periodicSensorsExecutorPoolSize);
		
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
			final boolean considerSystemProperties,
			final String configurationName) {
		/* Set the monitoring log writer */
		final IMonitoringLogWriter monitoringLogWriter =
				MonitoringConfiguration
						.loadWriter(props, considerSystemProperties,
								configurationName);
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

	@Override
	public int getPeriodicSensorsExecutorPoolSize() {
		return this.periodicSensorsExecutorPoolSize;
	}

	@Override
	public void setPeriodicSensorsExecutorPoolSize(
			final int periodicSensorsExecutorPoolSize) {
		this.periodicSensorsExecutorPoolSize = periodicSensorsExecutorPoolSize;
	}

	/**
	 * 
	 * @param props
	 * @param propertyName
	 * @param defaultValue
	 * @param considerSystemProperties
	 * @return
	 */
	private static boolean loadBooleanConfigurationProperty(
			final Properties props, final ConfigurationProperty property,
			final boolean considerSystemProperties) {
		final String stringValue = MonitoringConfiguration
				.loadStringConfigurationProperty(props, property,
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
	private static int loadIntConfigurationProperty(final Properties props,
			final ConfigurationProperty property,
			final boolean considerSystemProperties)
			throws NumberFormatException {
		final String stringValue = MonitoringConfiguration
				.loadStringConfigurationProperty(props, property,
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
	private static String loadStringConfigurationProperty(
			final Properties props, final ConfigurationProperty property,
			final boolean considerSystemProperties) {
		String propertyValue;
		if (considerSystemProperties && property.hasJVMArgument()
				&& (System.getProperty(property.getJVMArgumentName()) != null)) {
			/* We use the present virtual machine parameter value */
			propertyValue = System.getProperty(property.getJVMArgumentName());
		} else if (property.getPropertyName() != null) {
			/* we use the value from the properties map */
			propertyValue = props.getProperty(property.getPropertyName());
		} else {
			/* Un-named property with a default value */
			propertyValue = property.getDefaultValue();
		}

		if ((propertyValue == null)
				|| (!property.isAllowEmpty() && propertyValue.isEmpty())) {
			propertyValue = property.getDefaultValue();
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
	private static IMonitoringLogWriter loadWriter(final Properties props,
			final boolean considerSystemProperties,
			final String configurationName) {
		IMonitoringLogWriter monitoringLogWriter = null;

		try {
			final String monitoringDataWriterClassname =
					MonitoringConfiguration
							.loadStringConfigurationProperty(
									props,
									ConfigurationProperty.MONITORING_DATA_WRITER_CLASSNAME,
									considerSystemProperties);
			final String monitoringDataWriterInitString =
					MonitoringConfiguration
							.loadStringConfigurationProperty(
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
				monitoringLogWriter = MonitoringConfiguration.loadSyncFSWriter(
						props, considerSystemProperties, configurationName);
			} else if (monitoringDataWriterClassname
					.equals(ConfigurationFileConstants.WRITER_ASYNCFS)) {
				/* Asynchronous file system writer */
				monitoringLogWriter = MonitoringConfiguration
						.loadAsyncFSWriter(props, considerSystemProperties,
								configurationName);
			} else if (monitoringDataWriterClassname
					.equals(ConfigurationFileConstants.WRITER_SYNCDB)) {
				/* Synchronous database writer */
				monitoringLogWriter = MonitoringConfiguration.loadSyncDBWriter(
						props, considerSystemProperties);
			} else if (monitoringDataWriterClassname
					.equals(ConfigurationFileConstants.WRITER_ASYNCDB)) {
				/* Asynchronous database writer */
				monitoringLogWriter = MonitoringConfiguration
						.loadAsyncDBWriter(props, considerSystemProperties);
			} else {
				/* Load the writer by its classname */
				monitoringLogWriter = MonitoringConfiguration
						.loadWriterByClassnameAndInitProps(props,
								considerSystemProperties,
								monitoringDataWriterClassname,
								monitoringDataWriterInitString);
			}
		} catch (final Exception e) {
			MonitoringConfiguration.log.error(
					"Error loading monitoring data writer", e);
			return null;
		}
		return monitoringLogWriter;
	}

	/**
	 * Returns an instance of the monitoring log writer class and init String as
	 * specified in the given properties and (if specified) considering JVM
	 * arguments.
	 * 
	 * @param props
	 * @param considerSystemProperties
	 * @param monitoringDataWriterClassname
	 * @param monitoringDataWriterInitString
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	private static IMonitoringLogWriter loadWriterByClassnameAndInitProps(
			final Properties props, final boolean considerSystemProperties,
			final String monitoringDataWriterClassname,
			final String monitoringDataWriterInitString)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, Exception {
		IMonitoringLogWriter monitoringLogWriter;
		final int asyncRecordQueueSize = MonitoringConfiguration
				.loadIntConfigurationProperty(props,
						ConfigurationProperty.ASYNC__RECORD_QUEUE_SIZE,
						considerSystemProperties);
		monitoringLogWriter = MonitoringConfiguration
				.loadWriterByClassnameAndInitString(
						monitoringDataWriterClassname,
						monitoringDataWriterInitString, asyncRecordQueueSize);
		return monitoringLogWriter;
	}

	/**
	 * Returns an instance of the monitoring log writer class initialized based
	 * on the given init String.
	 * 
	 * @param monitoringDataWriterClassname
	 * @param monitoringDataWriterInitString
	 * @param asyncRecordQueueSize
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	private static IMonitoringLogWriter loadWriterByClassnameAndInitString(
			final String monitoringDataWriterClassname,
			final String monitoringDataWriterInitString,
			final int asyncRecordQueueSize) throws InstantiationException {
		Class<?> logWriterClass = null;
		try {
			logWriterClass = Class.forName(monitoringDataWriterClassname);
		} catch (final ClassNotFoundException e) {
			MonitoringConfiguration.log.error(
					"Failed to load writer class for name"
							+ monitoringDataWriterClassname, e);
		}
		return MonitoringConfiguration.loadWriterByClassAndInitString(
				monitoringDataWriterInitString, asyncRecordQueueSize,
				logWriterClass);
	}

	/**
	 * 
	 * @param monitoringDataWriterInitString
	 * @param asyncRecordQueueSize
	 * @param logWriterClass
	 * @return the writer, null if an error occurred.
	 */
	private static IMonitoringLogWriter loadWriterByClassAndInitString(
			final String monitoringDataWriterInitString,
			final int asyncRecordQueueSize, final Class<?> logWriterClass) {
		IMonitoringLogWriter monitoringLogWriter;
		try {
			monitoringLogWriter = (IMonitoringLogWriter) logWriterClass
					.newInstance();
		} catch (final Exception e) {
			MonitoringConfiguration.log.error(
					"Failed to create instance of writer class", e);
			return null;
		}

		// TODO: this is still a dirty hack with the asyncRecordQueueSize
		// property!
		if (!monitoringLogWriter.init(monitoringDataWriterInitString
				+ " | asyncRecordQueueSize=" + asyncRecordQueueSize)) {
			MonitoringConfiguration.log
					.error("init(..) failed for writer instance of class"
							+ logWriterClass.getName());
			monitoringLogWriter = null;
		}
		return monitoringLogWriter;
	}

	/**
	 * Returns an instance of the asynchronous database writer initialized based
	 * on the given properties and (if specified) considering JVM arguments.
	 * 
	 * @param props
	 * @param considerSystemProperties
	 * @return
	 */
	private static IMonitoringLogWriter loadAsyncDBWriter(
			final Properties props, final boolean considerSystemProperties) {
		IMonitoringLogWriter monitoringLogWriter;
		final String dbDriverClassname = MonitoringConfiguration
				.loadStringConfigurationProperty(props,
						ConfigurationProperty.DB__DRIVER_CLASSNAME,
						considerSystemProperties);
		final String dbConnectionAddress = MonitoringConfiguration
				.loadStringConfigurationProperty(props,
						ConfigurationProperty.DB_CONNECTION_ADDRESS,
						considerSystemProperties);
		final String dbTableName = MonitoringConfiguration
				.loadStringConfigurationProperty(props,
						ConfigurationProperty.DB__TABLE_NAME,
						considerSystemProperties);
		final boolean setInitialExperimentIdBasedOnLastId =
				MonitoringConfiguration
						.loadBooleanConfigurationProperty(
								props,
								ConfigurationProperty.DB__SET_INITIAL_EXP_ID_BASED_ON_LAST,
								considerSystemProperties);
		final int asyncRecordQueueSize = MonitoringConfiguration
				.loadIntConfigurationProperty(props,
						ConfigurationProperty.ASYNC__RECORD_QUEUE_SIZE,
						considerSystemProperties);
		final boolean asyncBlockOnFullQueue = MonitoringConfiguration
				.loadBooleanConfigurationProperty(props,
						ConfigurationProperty.ASYNC__BLOCK_ON_FULL_QUEUE,
						considerSystemProperties);
		monitoringLogWriter = new AsyncDbWriter(dbDriverClassname,
				dbConnectionAddress, dbTableName,
				setInitialExperimentIdBasedOnLastId, asyncRecordQueueSize,
				asyncBlockOnFullQueue);
		return monitoringLogWriter;
	}

	/**
	 * Returns an instance of the synchronous database writer initialized based
	 * on the given properties and (if specified) considering JVM arguments.
	 * 
	 * @param props
	 * @param considerSystemProperties
	 * @return
	 */
	private static IMonitoringLogWriter loadSyncDBWriter(
			final Properties props, final boolean considerSystemProperties) {
		IMonitoringLogWriter monitoringLogWriter;
		final String dbDriverClassname = MonitoringConfiguration
				.loadStringConfigurationProperty(props,
						ConfigurationProperty.DB__DRIVER_CLASSNAME,
						considerSystemProperties);
		final String dbConnectionAddress = MonitoringConfiguration
				.loadStringConfigurationProperty(props,
						ConfigurationProperty.DB_CONNECTION_ADDRESS,
						considerSystemProperties);
		final String dbTableName = MonitoringConfiguration
				.loadStringConfigurationProperty(props,
						ConfigurationProperty.DB__TABLE_NAME,
						considerSystemProperties);
		final boolean setInitialExperimentIdBasedOnLastId =
				MonitoringConfiguration
						.loadBooleanConfigurationProperty(
								props,
								ConfigurationProperty.DB__SET_INITIAL_EXP_ID_BASED_ON_LAST,
								considerSystemProperties);
		monitoringLogWriter = new SyncDbWriter(dbDriverClassname,
				dbConnectionAddress, dbTableName,
				setInitialExperimentIdBasedOnLastId);
		return monitoringLogWriter;
	}

	/**
	 * Returns an instance of the asynchronous file system writer initialized
	 * based on the given properties and (if specified) considering JVM
	 * arguments.
	 * 
	 * @param props
	 * @param considerSystemProperties
	 * @return
	 */
	private static IMonitoringLogWriter loadAsyncFSWriter(
			final Properties props, final boolean considerSystemProperties,
			final String storagePathPostfix) {
		IMonitoringLogWriter monitoringLogWriter;
		final boolean storeInJavaIOTmpDir = MonitoringConfiguration
				.loadBooleanConfigurationProperty(props,
						ConfigurationProperty.FS_WRITER__STORE_IN_JAVAIOTMPDIR,
						considerSystemProperties);
		final String storagePathBaseDir;
		if (storeInJavaIOTmpDir) {
			storagePathBaseDir = System.getProperty("java.io.tmpdir");
		} else {
			storagePathBaseDir = MonitoringConfiguration
					.loadStringConfigurationProperty(props,
							ConfigurationProperty.FS_WRITER__CUSTOM_STORAGE_PATH,
							considerSystemProperties);
		}
		final int asyncRecordQueueSize = MonitoringConfiguration
				.loadIntConfigurationProperty(props,
						ConfigurationProperty.ASYNC__RECORD_QUEUE_SIZE,
						considerSystemProperties);
		final boolean asyncBlockOnFullQueue = MonitoringConfiguration
				.loadBooleanConfigurationProperty(props,
						ConfigurationProperty.ASYNC__BLOCK_ON_FULL_QUEUE,
						considerSystemProperties);
		monitoringLogWriter =
				new AsyncFsWriter(storagePathBaseDir,
						storagePathPostfix, asyncRecordQueueSize,
						asyncBlockOnFullQueue);
		return monitoringLogWriter;
	}

	/**
	 * Returns an instance of the synchronous file system writer initialized
	 * based on the given properties and (if specified) considering JVM
	 * arguments.
	 * 
	 * @param props
	 * @param considerSystemProperties
	 * @return
	 */
	private static IMonitoringLogWriter loadSyncFSWriter(
			final Properties props, final boolean considerSystemProperties,
			final String storagePathPostfix) {
		IMonitoringLogWriter monitoringLogWriter;
		final boolean storeInJavaIOTmpDir = MonitoringConfiguration
				.loadBooleanConfigurationProperty(props,
						ConfigurationProperty.FS_WRITER__STORE_IN_JAVAIOTMPDIR,
						considerSystemProperties);
		final String filenameBase;
		if (storeInJavaIOTmpDir) {
			filenameBase = System.getProperty("java.io.tmpdir");
		} else {
			filenameBase = MonitoringConfiguration
					.loadStringConfigurationProperty(props,
							ConfigurationProperty.FS_WRITER__CUSTOM_STORAGE_PATH,
							considerSystemProperties);
		}
		monitoringLogWriter =
				new SyncFsWriter(filenameBase, storagePathPostfix);
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

	private void setMonitoringLogWriter(
			final IMonitoringLogWriter monitoringLogWriter) {
		this.monitoringLogWriter = monitoringLogWriter;
	}
}
