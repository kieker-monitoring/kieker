package kieker.monitoring.core.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import kieker.monitoring.core.MonitoringController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
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
 * @author Andre van Hoorn, Jan Waller
 */
public final class ConfigurationProperties {
	private static final Log log = LogFactory.getLog(ConfigurationProperties.class);
	
	//TODO: if this changes, the default config file has to be adjusted!
	//      Ideally it would be created using this file!
	//TODO: rename the constants to better names!
	public final static String PREFIX = "kieker.monitoring.";
	public final static String KIEKER_CUSTOM_PROPERTIES_LOCATION_CLASSPATH = "META-INF/" + PREFIX + "properties";
	public final static String KIEKER_DEFAULT_PROPERTIES_LOCATION_CLASSPATH = KIEKER_CUSTOM_PROPERTIES_LOCATION_CLASSPATH + ".default";
	// config values
	public final static String KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME = PREFIX + "configuration";
	// these should be declared in the file KIEKER_DEFAULT_PROPERTIES_LOCATION_CLASSPATH
	public final static String MONITORING_DATA_WRITER_CLASSNAME = PREFIX + "writer";
	public final static String DEBUG_ENABLED = PREFIX + "debug";
	public final static String MONITORING_ENABLED = PREFIX + "enabled";
	public final static String PERIODIC_SENSORS_EXECUTOR_POOL_SIZE = PREFIX + "periodicSensorsExecutorPoolSize";
	public final static String INITIAL_EXPERIMENT_ID = PREFIX + "initialExperimentId";
	
	/**
	 * Returns a properties map with the default configuration.
	 * 
	 * @return
	 */
	final static Properties defaultProperties() {
		//try {
			final Properties defaultProps = loadPropertiesFromResource(KIEKER_DEFAULT_PROPERTIES_LOCATION_CLASSPATH, null);
			/*if (defaultProps == null) {
				ConfigurationProperties.log.error("Default properties file '" + KIEKER_DEFAULT_PROPERTIES_LOCATION_CLASSPATH + "' not found");
				return null;
			}*/
			return defaultProps;
		/*} catch (final IOException ex) {
			ConfigurationProperties.log.error("Failed to load default properties from '" + KIEKER_DEFAULT_PROPERTIES_LOCATION_CLASSPATH + "'", ex);
			return null;
		}*/
	}
	
	/**
	 * Returns an empty properties map with the default configuration.
	 * 
	 * @return
	 */
	public final static Properties getDefaultProperties() {
		return new Properties(defaultProperties());
	}
	
	
	/**
	 * Returns the properties loaded from file propertiesFn.
	 * 
	 * @param propertiesFn
	 * @param defaultValues
	 * @return
	 */
	final static Properties loadPropertiesFromFile(final String propertiesFn, final Properties defaultValues) {
		try {
			final Properties properties = new Properties(defaultValues);
			properties.load(new FileInputStream(propertiesFn));
			return properties;
		} catch (final FileNotFoundException ex) {
			ConfigurationProperties.log.warn("File '" + propertiesFn + "' not found");
		} catch (final Exception ex) {
			ConfigurationProperties.log.error("Error reading file '" + propertiesFn + "'", ex);
		}
		return new Properties(defaultValues);
	}

	/**
	 * Returns the properties loaded from the resource name or null if the resource could not be found.
	 * 
	 * @param propertiesFn
	 * @param defaultValues
	 * @return
	 */
	final static Properties loadPropertiesFromResource(final String propertiesFn, final Properties defaultValues) {
		final InputStream is = MonitoringController.class.getClassLoader().getResourceAsStream(propertiesFn);
		if (is == null) {
			ConfigurationProperties.log.warn("File '" + propertiesFn + "' not found in classpath");
			return defaultValues;
		} else {
			try {
				final Properties properties = new Properties(defaultValues);
				properties.load(is);
				return properties;
			} catch (final Exception ex) {
				ConfigurationProperties.log.error("Error reading file '" + propertiesFn + "'", ex);
			}
		}
		return new Properties(defaultValues);
	}
	
	public final static Properties getPropertiesStartingWith(final String prefix, final Properties properties, final Properties defaultValues) {
		final Properties props = new Properties(defaultValues);
		for (String property : properties.stringPropertyNames()) {
			if (property.startsWith(prefix)) {
				props.setProperty(property, properties.getProperty(property));
			}
		}
		return props;
	}
	
	public final static String getStringProperty(final Properties properties, final String key) {
		final String s = properties.getProperty(key);
		return (s == null) ? "" : s;
	}
	
	public final static boolean getBooleanProperty(final Properties properties, final String key) {
		return Boolean.parseBoolean(getStringProperty(properties, key));
	}
	
	public final static int getIntProperty(final Properties properties, final String key) {
		final String s = getStringProperty(properties, key);
		try {
			return Integer.parseInt(s);
		} catch (final NumberFormatException ex) {
			ConfigurationProperties.log.warn("Error parsing configuration property '" + key + "', found value '" + s + "', using default value 0");
			return 0;
		}
	}
}
