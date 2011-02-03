package kieker.monitoring.core.configuration;

import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import kieker.monitoring.writer.IMonitoringWriter;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
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
 * Use the factory methods to create instances:
 * 
 * <ul>
 * <li>{@link #createSingletonConfiguration()}</li>
 * <li>{@link #createConfiguration(String, String)}</li>
 * <li>{@link #createConfiguration(String, Properties)}</li>
 * </ul>
 * 
 * 
 * @author Andre van Hoorn
 */
public final class MonitoringConfiguration implements IMonitoringConfiguration {
	private static final Log log = LogFactory.getLog(MonitoringConfiguration.class);

	/**
	 * Name of the singleton instance
	 */
	private final static String SINGLETON_INSTANCE_NAME = "SINGLETON";
	public final static String LOCAL_HOST_NAME;
	static {
		String hostname = "<UNKNOWN>";
		try {
			hostname = java.net.InetAddress.getLocalHost().getHostName();
		} catch (final UnknownHostException ex) {
			MonitoringConfiguration.log.warn("Failed to retrieve hostname");
		} finally {
			LOCAL_HOST_NAME = hostname;
		}
	}
	private String hostName = MonitoringConfiguration.LOCAL_HOST_NAME;;
	private final String instanceName;
	private final IMonitoringWriter monitoringLogWriter;
	private boolean monitoringEnabled;
	private boolean debugEnabled;
	private int initialExperimentId;
	private int periodicSensorsExecutorPoolSize;
	
	/**
	 * Creates the configuration for the singleton controller instance. Note
	 * that the {@link MonitoringConfiguration} returned by this method is not a
	 * singleton instance, i.e., each call returns an equal but not same
	 * {@link MonitoringConfiguration}.
	 * 
	 * @return
	 */
	public final static MonitoringConfiguration createSingletonConfiguration() {
		String configurationFile = "";
		final Properties properties;
		MonitoringConfiguration.log.debug("Searching for JVM argument '" + ConfigurationProperties.KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME + "' ...");
		if (System.getProperty(ConfigurationProperties.KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME) != null) {
			// Searching for configuration file location passed to JVM
			configurationFile = System.getProperty(ConfigurationProperties.KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME);
			MonitoringConfiguration.log.info("Loading configuration from JVM-specified location: " + configurationFile);
			properties = ConfigurationProperties.getPropertiesStartingWith(
					ConfigurationProperties.PREFIX, System.getProperties(), 
					ConfigurationProperties.loadPropertiesFromFile(
							configurationFile, ConfigurationProperties.defaultProperties()));
		} else {
			// No JVM property; Trying to find configuration file in classpath
			configurationFile = ConfigurationProperties.KIEKER_CUSTOM_PROPERTIES_LOCATION_CLASSPATH;
			MonitoringConfiguration.log.info("Loading properties from properties file in classpath: " + configurationFile);
			properties = ConfigurationProperties.getPropertiesStartingWith(
					ConfigurationProperties.PREFIX, System.getProperties(), 
					ConfigurationProperties.loadPropertiesFromResource(
							configurationFile, ConfigurationProperties.defaultProperties()));
		}
		return new MonitoringConfiguration(MonitoringConfiguration.SINGLETON_INSTANCE_NAME, properties);
	}
	
	/**
	 * Creates a new configuration based on the given configuration file.
	 * 
	 * @param configurationName
	 * @param configurationFile
	 * @return
	 */
	public final static MonitoringConfiguration createConfiguration(final String configurationName, final String configurationFile) {
		final Properties properties = ConfigurationProperties.loadPropertiesFromFile(configurationFile, ConfigurationProperties.defaultProperties());
		return new MonitoringConfiguration(configurationName, properties);
	}
	
	/**
	 * Creates a new configuration based on given properties.
	 * 
	 * @param properties
	 * @param properties
	 * @return
	 */
	public final static MonitoringConfiguration createConfiguration(final String configurationName, final Properties properties) {
		return new MonitoringConfiguration(configurationName, properties);
	}
	
	/**
	 * Constructs a configuration based on the given properties.
	 * 
	 * @param name
	 * @param properties
	 * @param considerSystemProperties
	 */
	private MonitoringConfiguration(final String name, final Properties properties) {
		this.instanceName = name;
		// Set Variables
		setDebugEnabled(ConfigurationProperties.getBooleanProperty(properties, ConfigurationProperties.DEBUG_ENABLED));
		setMonitoringEnabled(ConfigurationProperties.getBooleanProperty(properties, ConfigurationProperties.MONITORING_ENABLED));
		setPeriodicSensorsExecutorPoolSize(ConfigurationProperties.getIntProperty(properties, ConfigurationProperties.PERIODIC_SENSORS_EXECUTOR_POOL_SIZE));
		setInitialExperimentId(ConfigurationProperties.getIntProperty(properties, ConfigurationProperties.INITIAL_EXPERIMENT_ID));
		// Set Writer
		final String writerClassname = ConfigurationProperties.getStringProperty(properties, ConfigurationProperties.MONITORING_DATA_WRITER_CLASSNAME);
		IMonitoringWriter monitoringLogWriter = null;
		try {
			// search for correct Constructor -> 1 parameter of type Properties
			monitoringLogWriter = IMonitoringWriter.class.cast(Class.forName(writerClassname).getConstructor(Properties.class).newInstance(properties));
		} catch (final NoSuchMethodException ex) {
			MonitoringConfiguration.log.error("Writer Class '" + writerClassname + "' has to implement a constructor that accepts a single set of configuration Properties");
		} catch (final NoClassDefFoundError ex) {
			MonitoringConfiguration.log.error("Writer Class '" + writerClassname + "' not found");
		} catch (final ClassNotFoundException ex) {
			MonitoringConfiguration.log.error("Writer Class '" + writerClassname + "' not found");
		} catch (final Throwable ex) {
			MonitoringConfiguration.log.error("Failed to load writer class for name " + writerClassname, ex);
		}
		this.monitoringLogWriter =  monitoringLogWriter;
	}
	
	@Override
	public final void setDebugEnabled(final boolean debugEnabled) {
		this.debugEnabled = debugEnabled;
	}

	@Override
	public final boolean isDebugEnabled() {
		return debugEnabled;
	}

	@Override
	public final void setMonitoringEnabled(final boolean monitoringEnabled) {
		this.monitoringEnabled = monitoringEnabled;
	}

	@Override
	public final boolean isMonitoringEnabled() {
		return monitoringEnabled;
	}

	@Override
	public final void setPeriodicSensorsExecutorPoolSize(final int poolSize) {
		periodicSensorsExecutorPoolSize = poolSize;
	}

	@Override
	public final int getPeriodicSensorsExecutorPoolSize() {
		return periodicSensorsExecutorPoolSize;
	}
	
	@Override
	public void setInitialExperimentId(int initialExperimentId) {
		this.initialExperimentId = initialExperimentId;
	}

	@Override
	public int getInitialExperimentId() {
		return initialExperimentId;
	}

	@Override
	public final void setHostName(final String newHostName) {
		hostName = newHostName;
	}

	@Override
	public final String getHostName() {
		return hostName;
	}

	@Override
	public final String getName() {
		return instanceName;
	}

	@Override
	public final IMonitoringWriter getMonitoringLogWriter() {
		return monitoringLogWriter;
	}
}