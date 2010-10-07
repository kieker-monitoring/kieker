package kieker.monitoring.core.configuration;

import kieker.monitoring.writer.IMonitoringLogWriter;


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

public final class MonitoringConfiguration implements IMonitoringConfiguration {

	@Override
	public void setDebugEnabled(final boolean debugEnabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDebugEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setMonitoringEnabled(final boolean monitoringEnabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isMonitoringEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IMonitoringLogWriter getMonitoringLogWriter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMonitoringLogWriter(final IMonitoringLogWriter monitoringLogWriter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IMonitoringLogWriter createAndSetMonitoringLogWriter(
			final Class<? extends IMonitoringLogWriter> logWriterClass,
			final String initString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHostName(final String newHostName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getHostName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getConfigurationName() {
		// TODO Auto-generated method stub
		return null;
	}

//	private final static String SINGLETON_INSTANCE_NAME = "Singleton Instance";
//
//	public enum PROPERTY_NAME {
//		PROPERTY_NAME (){
//			
//		}
//		
//	};
//	
//	public static final String PROPERTY_NAME__KIEKER_MONITORING_DB_CONNECTION_ADDRESS = "kieker.monitoring.dbConnectionAddress";
//
//	public static final String KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME = "kieker.monitoring.configuration";
//
//	public static final String KIEKER_CUSTOM_PROPERTIES_LOCATION_CLASSPATH = "META-INF/kieker.monitoring.properties";
//
//	public static final String KIEKER_CUSTOM_PROPERTIES_LOCATION_DEFAULT = "META-INF/kieker.monitoring.properties.default";
//
//	private static final String LOCAL_HOST_NAME;
//	private static final Log log = LogFactory
//			.getLog(MonitoringConfiguration.class);
//	public static final String PROPERTY_NAME__MONITORING_DATA_WRITER = "monitoringDataWriter";
//
//	public static final String PROPERTY_NAME__MONITORING_DATA_WRITER_INIT_STRING = "monitoringDataWriterInitString";
//
//	static {
//		String hostname = "<UNKNOWN>";
//		try {
//			hostname = java.net.InetAddress.getLocalHost().getHostName();
//		} catch (final Exception ex) {
//			MonitoringConfiguration.log.warn("Failed to get hostname", ex);
//		} finally {
//			LOCAL_HOST_NAME = hostname;
//		}
//	}
//
//	public static MonitoringConfiguration createSingletonConfiguration() {
//
//	}
//
//	/**
//	 * Returns the name of the localhost.
//	 * 
//	 * @return
//	 */
//	public final static String getLocalHostName() {
//		return MonitoringConfiguration.LOCAL_HOST_NAME;
//	}
//
//	/**
//	 * Creates a new monitoring configuration based on the given configuration
//	 * file.
//	 * 
//	 * Note, that in this case, no Kieker properties passed to the JVM are
//	 * evaluated.
//	 * 
//	 * @param configurationFn
//	 * @return
//	 */
//	public static MonitoringConfiguration loadConfiguraton(
//			final String instanceName, final String configurationFn)
//			throws FileNotFoundException, IOException {
//		// TODO: to be implemented
//		return null;
//	}
//
//	/**
//	 * Returns the properties loaded from file propertiesFn.
//	 * 
//	 * @param propertiesFn
//	 * @return
//	 * @throws FileNotFoundException
//	 * @throws IOException
//	 */
//	private static Properties loadPropertiesFromFile(final String propertiesFn)
//			throws FileNotFoundException, IOException {
//		final InputStream is = new FileInputStream(propertiesFn);
//		final Properties prop = new Properties();
//		prop.load(is);
//		return prop;
//	}
//
//	/**
//	 * Returns the properties loaded from the resource name or null if the
//	 * resource could not be found.
//	 * 
//	 * @param classLoader
//	 * @param name
//	 * @return
//	 * @throws IOException
//	 */
//	private static Properties loadPropertiesFromResource(final String name)
//			throws IOException {
//		final InputStream is = MonitoringController.class.getClassLoader()
//				.getResourceAsStream(name);
//		if (is == null) {
//			return null;
//		}
//		final Properties prop = new Properties();
//
//		prop.load(is);
//		return prop;
//	}
//
//	/**
//	 * Returns the properties used to construct the singleton instance. If a
//	 * custom configuration file location is passed to the JVM using the
//	 * property kieker.monitoring.configuration, this the properties are loaded
//	 * from this file. Otherwise, the method searches for a configuration file
//	 * META-INF/kieker.monitoring.properties in the classpath, and if this does
//	 * not exist, it loads the default properties contained in the Kieker jar.
//	 * 
//	 * @return
//	 */
//	private static Properties loadSingletonProperties() {
//		Properties prop = null; // = new Properties();
//		String configurationFile = null;
//		try {
//			/* 1. Searching for configuration file location passed to JVM */
//			if (System
//					.getProperty(MonitoringConfiguration.KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME) != null) {
//				configurationFile = System
//						.getProperty(MonitoringConfiguration.KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME);
//				MonitoringConfiguration.log
//						.info("Loading properties from JVM-specified location '"
//								+ configurationFile + "'");
//				prop = MonitoringConfiguration
//						.loadPropertiesFromFile(configurationFile);
//			} else {
//				/*
//				 * 2. No JVM property; Trying to find configuration file in
//				 * classpath
//				 */
//				configurationFile = MonitoringConfiguration.KIEKER_CUSTOM_PROPERTIES_LOCATION_CLASSPATH;
//				prop = MonitoringConfiguration
//						.loadPropertiesFromResource(configurationFile);
//				if (prop != null) { // success
//					MonitoringConfiguration.log
//							.info("Loading properties from properties file in classpath: "
//									+ configurationFile);
//					MonitoringConfiguration.log
//							.info("You can specify an alternative properties file using the property '"
//									+ MonitoringConfiguration.KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME
//									+ "'");
//				} else {
//					/*
//					 * 3. No configuration file found in classpath; using
//					 * default configuration.
//					 */
//					configurationFile = MonitoringConfiguration.KIEKER_CUSTOM_PROPERTIES_LOCATION_DEFAULT;
//					MonitoringConfiguration.log
//							.info("Loading properties from Kieker.Monitoring library jar!"
//									+ configurationFile);
//					MonitoringConfiguration.log
//							.info("You can specify an alternative properties file using the property '"
//									+ MonitoringConfiguration.KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME
//									+ "'");
//					prop = MonitoringConfiguration
//							.loadPropertiesFromResource(configurationFile);
//				}
//			}
//		} catch (final Exception ex) {
//			MonitoringConfiguration.log.error(
//					"Error loading kieker configuration file '"
//							+ configurationFile + "'", ex);
//			// TODO: introduce static variable 'terminated' or alike
//		}
//		return prop;
//	}
//
//	// private final String dbTableName = "turbomon10";
//	private int experimentId = ConfigurationDefaults.EXPERIMENT_ID;
//
//	private volatile String filenamePrefix = ConfigurationDefaults.FS_FN_PREFIX;
//
//	@Override
//	public IMonitoringLogWriter createAndSetMonitoringLogWriter(
//			final Class<? extends IMonitoringLogWriter> logWriterClass,
//			final String initString) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	// private volatile boolean asyncBlockOnFullQueue = false;
//	// // if
//	// // storeInJavaIoTmpdir
//	// // == false
//	// private volatile int asyncRecordQueueSize = 8000;
//	// private volatile String customStoragePath = "/tmp"; // only used as
//	// default
//	// private final String dbConnectionAddress =
//	// "jdbc:mysql://HOSTNAME/DATABASENAME?user=DBUSER&password=DBPASS";
//
//	// private final String dbDriverClassname = "com.mysql.jdbc.Driver";
//
//	/**
//	 * Returns the experiment ID.
//	 * 
//	 * @return
//	 */
//	public final int getExperimentId() {
//		return this.experimentId;
//	}
//
//	@Override
//	public String getHostName() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	// // database only configuration configuration values that are overwritten
//	// by
//	// // kieker.monitoring.properties included in the kieker.monitoring library
//	// private volatile boolean setInitialExperimentIdBasedOnLastId = false;
//
//	// private volatile boolean storeInJavaIoTmpdir = true;
//
//	@Override
//	public IMonitoringLogWriter getMonitoringLogWriter() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	/**
//	 * 
//	 * @param props
//	 * @param propertyName
//	 * @param defaultValue
//	 * @param considerSystemProperties
//	 * @return
//	 */
//	private String loadStringProperty(final Properties props,
//			final String propertyName, final String defaultValue,
//			final boolean considerSystemProperties) {
//		String propertyValue;
//		if (considerSystemProperties
//				&& (System.getProperty(propertyName) != null)) {
//			/* We use the present virtual machine parameter value */
//			propertyValue = System.getProperty(propertyName);
//		} else { // we use the parameter in the properties file
//			propertyValue = props.getProperty(propertyName);
//		}
//
//		if ((propertyValue == null) || propertyValue.isEmpty()) {
//			MonitoringConfiguration.log.info("Missing value for property '"
//					+ propertyName + "' using default value " + defaultValue);
//			propertyValue = defaultValue;
//		}
//
//		return propertyValue;
//	}
//
//	/**
//	 * 
//	 * @param props
//	 * @param propertyName
//	 * @param defaultValue
//	 * @param considerSystemProperties
//	 * @return
//	 */
//	private boolean loadBooleanProperty(final Properties props,
//			final String propertyName, final boolean defaultValue,
//			final boolean considerSystemProperties) {
//		final String stringValue = this.loadStringProperty(props, propertyName,
//				Boolean.toString(defaultValue), considerSystemProperties);
//
//		return Boolean.parseBoolean(stringValue);
//	}
//
//	/**
//	 * 
//	 * @param props
//	 * @param propertyName
//	 * @param defaultValue
//	 * @param considerSystemProperties
//	 * @return
//	 */
//	private int loadIntProperty(final Properties props,
//			final String propertyName, final int defaultValue,
//			final boolean considerSystemProperties)
//			throws NumberFormatException {
//		final String stringValue = this.loadStringProperty(props, propertyName,
//				Integer.toString(defaultValue), considerSystemProperties);
//
//		return Integer.parseInt(stringValue);
//	}
//
//	private final String instanceName;
//
//	/**
//	 * Loads the writer based on the given properties
//	 * 
//	 * @param props
//	 * @return the writer, null if the construction of the writer failed
//	 */
//	private IMonitoringLogWriter loadWriter(final Properties props) {
//		final IMonitoringLogWriter monitoringLogWriter = null;
//
//		/* Load monitoring data writer */
//		final String monitoringDataWriterClassname = props
//				.getProperty(MonitoringConfiguration.PROPERTY_NAME__MONITORING_DATA_WRITER);
//		final String monitoringDataWriterInitString = props
//				.getProperty(MonitoringConfiguration.PROPERTY_NAME__MONITORING_DATA_WRITER_INIT_STRING);
//
//		if ((monitoringDataWriterClassname == null)
//				|| (monitoringDataWriterClassname.isEmpty())) {
//			throw new Exception("Property monitoringDataWriter not set");
//		} else if (monitoringDataWriterClassname
//				.equals(MonitoringController.WRITER_SYNCFS)) {
//			final String filenameBase = this.filenamePrefix;
//			monitoringLogWriter = new SyncFsWriter(filenameBase,
//					this.instanceName);
//		} else if (monitoringDataWriterClassname
//				.equals(MonitoringController.WRITER_ASYNCFS)) {
//			final String filenameBase = this.filenamePrefix;
//			monitoringLogWriter = new AsyncFsWriter(filenameBase,
//					this.instanceName, this.asyncRecordQueueSize,
//					this.asyncBlockOnFullQueue);
//		} else if (monitoringDataWriterClassname
//				.equals(MonitoringController.WRITER_SYNCDB)) {
//			monitoringLogWriter = new SyncDbWriter(this.dbDriverClassname,
//					this.dbConnectionAddress, this.dbTableName,
//					this.setInitialExperimentIdBasedOnLastId);
//		} else if (monitoringDataWriterClassname
//				.equals(MonitoringController.WRITER_ASYNCDB)) {
//			monitoringLogWriter = new AsyncDbWriter(this.dbDriverClassname,
//					this.dbConnectionAddress, this.dbTableName,
//					this.setInitialExperimentIdBasedOnLastId,
//					this.asyncRecordQueueSize, this.asyncBlockOnFullQueue);
//		} else {
//			/* try to load the class by name */
//			monitoringLogWriter = (IMonitoringLogWriter) Class.forName(
//					monitoringDataWriterClassname).newInstance();
//			// add asyncRecordQueueSize
//			monitoringDataWriterInitString += " | asyncRecordQueueSize="
//					+ this.asyncRecordQueueSize;
//			if (!monitoringLogWriter.init(monitoringDataWriterInitString)) {
//				monitoringLogWriter = null;
//				throw new Exception("Initialization of writer failed!");
//			}
//		}
//	}
//
//	/**
//	 * Loads properties from configuration file.
//	 * 
//	 * @return true if the initialization was successful, false otherwise
//	 */
//	private boolean initFromProperties(final Properties props,
//			final boolean considerSystemProperties) {
//
//		if (this.loadWriter(props) == null) {
//			// TODO: handle
//			MonitoringConfiguration.log.error("Failed to load writer");
//			return false;
//		}
//
//		final String dbDriverClassname = this
//				.loadStringProperty(
//						props,
//						MonitoringConfiguration.PROPERTY_NAME__KIEKER_MONITORING_DB_CONNECTION_ADDRESS,
//						ConfigurationDefaults.DB__DRIVER_CLASSNAME,
//						/* Cannot be specified via jvm property: */
//						false);
//
//		final String dbConnectionAddress = this
//				.loadStringProperty(
//						props,
//						MonitoringConfiguration.PROPERTY_NAME__KIEKER_MONITORING_DB_CONNECTION_ADDRESS,
//						ConfigurationDefaults.DB__CONNECTION_ADDRESS,
//						/* Cannot be specified via jvm property: */
//						false);
//
//		String dbDriverClassnameProperty;
//		if (considerSystemProperties && (System.getProperty() != null)) {
//			/* we use the present virtual machine parameter value */
//			dbDriverClassnameProperty = System
//					.getProperty("kieker.monitoring.dbDriverClassname");
//		} else { // we use the parameter in the properties file
//			dbDriverClassnameProperty = props.getProperty("dbDriverClassname");
//		}
//		if ((dbDriverClassnameProperty != null)
//				&& (dbDriverClassnameProperty.length() != 0)) {
//			this.dbDriverClassname = dbDriverClassnameProperty;
//		} else {
//			MonitoringController.log
//					.info("No dbDriverClassname parameter found"
//							+ ". Using default value " + this.dbDriverClassname
//							+ ".");
//		}
//
//		// load property "dbConnectionAddress"
//		String dbConnectionAddressProperty;
//		if (considerSystemProperties
//				&& (System
//						.getProperty(MonitoringConfiguration.PROPERTY_NAME__KIEKER_MONITORING_DB_CONNECTION_ADDRESS) != null)) { // we
//			// use
//			// the
//			// present
//			// virtual
//			// machine
//			// parameter
//			// value
//			dbConnectionAddressProperty = System
//					.getProperty(MonitoringConfiguration.PROPERTY_NAME__KIEKER_MONITORING_DB_CONNECTION_ADDRESS);
//		} else { // we use the parameter in the properties file
//			dbConnectionAddressProperty = props
//					.getProperty("dbConnectionAddress");
//		}
//		if ((dbConnectionAddressProperty != null)
//				&& (dbConnectionAddressProperty.length() != 0)) {
//			this.dbConnectionAddress = dbConnectionAddressProperty;
//		} else {
//			MonitoringController.log
//					.warn("No dbConnectionAddress parameter found"
//							+ ". Using default value "
//							+ this.dbConnectionAddress + ".");
//		}
//
//		// the filenamePrefix (folder where Kieker.Monitoring stores its data)
//		// for monitoring data depends on the properties
//		// kieker.monitoring.storeInJavaIoTmpdir
//		// and kieker.monitoring.customStoragePath
//		// these both parameters may be provided (with higher priority) as java
//		// command line parameters as well (example in the properties file)
//		String storeInJavaIoTmpdirProperty;
//		if (considerSystemProperties
//				&& (System.getProperty("kieker.monitoring.storeInJavaIoTmpdir") != null)) { // we
//																							// use
//																							// the
//																							// present
//																							// virtual
//																							// machine
//																							// parameter
//																							// value
//			storeInJavaIoTmpdirProperty = System
//					.getProperty("kieker.monitoring.storeInJavaIoTmpdir");
//		} else { // we use the parameter in the properties file
//			storeInJavaIoTmpdirProperty = props
//					.getProperty("kieker.monitoring.storeInJavaIoTmpdir");
//		}
//
//		if ((storeInJavaIoTmpdirProperty != null)
//				&& (storeInJavaIoTmpdirProperty.length() != 0)) {
//			if (storeInJavaIoTmpdirProperty.toLowerCase().equals("true")
//					|| storeInJavaIoTmpdirProperty.toLowerCase()
//							.equals("false")) {
//				this.storeInJavaIoTmpdir = storeInJavaIoTmpdirProperty
//						.toLowerCase().equals("true");
//			} else {
//				MonitoringController.log
//						.warn("Bad value for kieker.monitoring.storeInJavaIoTmpdir (or provided via command line) parameter ("
//								+ storeInJavaIoTmpdirProperty
//								+ ")"
//								+ ". Using default value "
//								+ this.storeInJavaIoTmpdir);
//			}
//		} else {
//			MonitoringController.log
//					.warn("No kieker.monitoring.storeInJavaIoTmpdir parameter found"
//							+ " (or provided via command line). Using default value '"
//							+ this.storeInJavaIoTmpdir + "'.");
//		}
//
//		if (this.storeInJavaIoTmpdir) {
//			this.filenamePrefix = System.getProperty("java.io.tmpdir");
//		} else { // only now we consider kieker.monitoring.customStoragePath
//			String customStoragePathProperty;
//			if (considerSystemProperties
//					&& (System
//							.getProperty("kieker.monitoring.customStoragePath") != null)) { // we
//																							// use
//																							// the
//																							// present
//																							// virtual
//																							// machine
//																							// parameter
//																							// value
//				customStoragePathProperty = System
//						.getProperty("kieker.monitoring.customStoragePath");
//			} else { // we use the parameter in the properties file
//				customStoragePathProperty = props
//						.getProperty("kieker.monitoring.customStoragePath");
//			}
//
//			if ((customStoragePathProperty != null)
//					&& (customStoragePathProperty.length() != 0)) {
//				this.filenamePrefix = customStoragePathProperty;
//			} else {
//				MonitoringController.log
//						.warn("No kieker.monitoring.customStoragePath parameter found"
//								+ " (or provided via command line). Using default value '"
//								+ this.customStoragePath + "'.");
//				this.filenamePrefix = this.customStoragePath;
//			}
//		}
//
//		// load property "dbTableNameProperty"
//		String dbTableNameProperty;
//		if (considerSystemProperties
//				&& (System.getProperty("kieker.monitoring.dbTableName") != null)) { // we
//																					// use
//																					// the
//																					// present
//																					// virtual
//																					// machine
//																					// parameter
//																					// value
//			dbTableNameProperty = System
//					.getProperty("kieker.monitoring.dbTableName");
//		} else { // we use the parameter in the properties file
//			dbTableNameProperty = props.getProperty("dbTableName");
//		}
//		if ((dbTableNameProperty != null)
//				&& (dbTableNameProperty.length() != 0)) {
//			this.dbTableName = dbTableNameProperty;
//		} else {
//			MonitoringController.log.warn("No dbTableName  parameter found"
//					+ ". Using default value " + this.dbTableName + ".");
//		}
//
//		// load property "debug"
//		String debugProperty;
//		if (considerSystemProperties
//				&& (System.getProperty("kieker.monitoring.debug") != null)) { // we
//																				// use
//																				// the
//																				// present
//																				// virtual
//																				// machine
//																				// parameter
//																				// value
//			debugProperty = System.getProperty("kieker.monitoring.debug");
//		} else { // we use the parameter in the properties file
//			debugProperty = props.getProperty("debug");
//		}
//		if ((debugProperty != null) && (debugProperty.length() != 0)) {
//			if (debugProperty.toLowerCase().equals("true")
//					|| debugProperty.toLowerCase().equals("false")) {
//				if (debugProperty.toLowerCase().equals("true")) {
//					MonitoringController.log.info("Debug mode enabled");
//					this.debugMode = DebugMode.ENABLED;
//				} else {
//					MonitoringController.log.info("Debug mode disabled");
//					this.debugMode = DebugMode.DISABLED;
//				}
//			} else {
//				MonitoringController.log.warn("Bad value for debug parameter ("
//						+ debugProperty + ")" + ". Using default value "
//						+ this.debugMode.isDebugEnabled());
//			}
//		} else {
//			MonitoringController.log.warn("Could not find debug parameter"
//					+ ". Using default value "
//					+ this.debugMode.isDebugEnabled());
//		}
//
//		// load property "setInitialExperimentIdBasedOnLastId"
//		final String setInitialExperimentIdBasedOnLastIdProperty = props
//				.getProperty("setInitialExperimentIdBasedOnLastId");
//		if ((setInitialExperimentIdBasedOnLastIdProperty != null)
//				&& (setInitialExperimentIdBasedOnLastIdProperty.length() != 0)) {
//			if (setInitialExperimentIdBasedOnLastIdProperty.toLowerCase()
//					.equals("true")
//					|| setInitialExperimentIdBasedOnLastIdProperty
//							.toLowerCase().equals("false")) {
//				this.setInitialExperimentIdBasedOnLastId = setInitialExperimentIdBasedOnLastIdProperty
//						.toLowerCase().equals("true");
//			} else {
//				MonitoringController.log
//						.warn("Bad value for setInitialExperimentIdBasedOnLastId parameter ("
//								+ setInitialExperimentIdBasedOnLastIdProperty
//								+ ")"
//								+ ". Using default value "
//								+ this.setInitialExperimentIdBasedOnLastId);
//			}
//		} else {
//			MonitoringController.log
//					.warn("Could not find setInitialExperimentIdBasedOnLastId parameter"
//							+ ". Using default value "
//							+ this.setInitialExperimentIdBasedOnLastId);
//		}
//
//		// load property "asyncRecordQueueSize"
//		String asyncRecordQueueSizeProperty = null;
//		if (considerSystemProperties
//				&& (System
//						.getProperty("kieker.monitoring.asyncRecordQueueSize") != null)) { // we
//																							// use
//																							// the
//																							// present
//																							// virtual
//																							// machine
//																							// parameter
//																							// value
//			asyncRecordQueueSizeProperty = System
//					.getProperty("kieker.monitoring.asyncRecordQueueSize");
//		} else { // we use the parameter in the properties file
//			asyncRecordQueueSizeProperty = props
//					.getProperty("asyncRecordQueueSize");
//		}
//		if ((asyncRecordQueueSizeProperty != null)
//				&& (asyncRecordQueueSizeProperty.length() != 0)) {
//			int asyncRecordQueueSizeValue = -1;
//			try {
//				asyncRecordQueueSizeValue = Integer
//						.parseInt(asyncRecordQueueSizeProperty);
//			} catch (final NumberFormatException ex) {
//			}
//			if (asyncRecordQueueSizeValue >= 0) {
//				this.asyncRecordQueueSize = asyncRecordQueueSizeValue;
//			} else {
//				MonitoringController.log
//						.warn("Bad value for asyncRecordQueueSize parameter ("
//								+ asyncRecordQueueSizeProperty + ")"
//								+ ". Using default value "
//								+ this.asyncRecordQueueSize);
//			}
//		} else {
//			MonitoringController.log
//					.warn("Could not find asyncRecordQueueSize parameter"
//							+ ". Using default value "
//							+ this.asyncRecordQueueSize);
//		}
//
//		// load property "asyncBlockOnFullQueue"
//		String asyncBlockOnFullQueueProperty = null;
//		if (considerSystemProperties
//				&& (System
//						.getProperty("kieker.monitoring.asyncBlockOnFullQueue") != null)) { // we
//																							// use
//																							// the
//																							// present
//																							// virtual
//																							// machine
//																							// parameter
//																							// value
//			asyncBlockOnFullQueueProperty = System
//					.getProperty("kieker.monitoring.asyncBlockOnFullQueue");
//		} else { // we use the parameter in the properties file
//			asyncBlockOnFullQueueProperty = props
//					.getProperty("asyncBlockOnFullQueue");
//		}
//		if ((asyncBlockOnFullQueueProperty != null)
//				&& (asyncBlockOnFullQueueProperty.length() != 0)) {
//			this.asyncBlockOnFullQueue = Boolean
//					.parseBoolean(asyncBlockOnFullQueueProperty);
//			MonitoringController.log.info("Using asyncBlockOnFullQueue value ("
//					+ asyncBlockOnFullQueueProperty + ")"
//					+ ". Using default value " + this.asyncBlockOnFullQueue);
//		} else {
//			MonitoringController.log
//					.warn("Could not find asyncBlockOnFullQueue"
//							+ ". Using default value "
//							+ this.asyncBlockOnFullQueue);
//		}
//
//		final String monitoringEnabledProperty = props
//				.getProperty("monitoringEnabled");
//		if ((monitoringEnabledProperty != null)
//				&& (monitoringEnabledProperty.length() != 0)) {
//			if (monitoringEnabledProperty.toLowerCase().equals("true")
//					|| monitoringEnabledProperty.toLowerCase().equals("false")) {
//				if (monitoringEnabledProperty.toLowerCase().equals("true")) {
//					this.controllerState.set(ControllerState.ENABLED);
//				} else {
//					this.controllerState.set(ControllerState.DISABLED);
//				}
//			} else {
//				MonitoringController.log
//						.warn("Bad value for monitoringEnabled parameter ("
//								+ monitoringEnabledProperty
//								+ ")"
//								+ ". Using default value "
//								+ this.controllerState.get().equals(
//										ControllerState.ENABLED));
//			}
//
//		} else {
//			MonitoringController.log
//					.warn("Could not find monitoringEnabled parameter"
//							+ ". Using default value "
//							+ this.controllerState.get().equals(
//									ControllerState.ENABLED));
//		}
//
//		if (!this.controllerState.get().equals(ControllerState.ENABLED)) {
//			MonitoringController.log.info("Monitoring is not enabled");
//		}
//	}
//
//	@Override
//	public boolean isDebugEnabled() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean isMonitoringEnabled() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public void setDebugEnabled(final boolean debugEnabled) {
//		// TODO Auto-generated method stub
//
//	}
//
//	/**
//	 * Sets the experiment ID to the given value.
//	 * 
//	 * @param experimentId
//	 */
//	public final void setExperimentId(final int experimentId) {
//		this.experimentId = experimentId;
//	}
//
//	@Override
//	public void setHostName(final String newHostName) {
//	}
//
//	@Override
//	public void setMonitoringEnabled(final boolean monitoringEnabled) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void setMonitoringLogWriter(
//			final IMonitoringLogWriter monitoringLogWriter) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public String getConfigurationName() {
//		return this.instanceName;
//	}
}
