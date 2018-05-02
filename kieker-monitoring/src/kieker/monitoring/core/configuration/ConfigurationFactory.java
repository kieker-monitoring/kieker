/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.core.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;

/**
 * A ConfigurationFactory for kieker.monitoring.
 *
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.3
 */
public final class ConfigurationFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationFactory.class);

	/**
	 * Private constructor to avoid instantiation.
	 */
	private ConfigurationFactory() {}

	// factory methods

	/**
	 * Creates the configuration for the singleton controller instance. Note that the {@link Properties} returned by
	 * this method are not a singleton instance, i.e., each call returns an equal but not same set of
	 * {@link Properties}.
	 *
	 * @return the configuration for the singleton controller
	 */
	public static final Configuration createSingletonConfiguration() {
		LOGGER.debug("Searching for JVM argument '{}' ...", ConfigurationKeys.CUSTOM_PROPERTIES_LOCATION_JVM);
		final Configuration defaultConfiguration = ConfigurationFactory.defaultConfiguration();
		// ignore default default-name and set to KIEKER-SINGLETON
		defaultConfiguration.setProperty(ConfigurationKeys.CONTROLLER_NAME, "KIEKER-SINGLETON");
		// Searching for configuration file location passed to JVM
		String configurationFile = System.getProperty(ConfigurationKeys.CUSTOM_PROPERTIES_LOCATION_JVM);
		final Configuration loadConfiguration;
		if (configurationFile != null) {
			LOGGER.info("Loading configuration from JVM-specified location: '{}'", configurationFile);
			loadConfiguration = ConfigurationFactory.loadConfigurationFromFile(configurationFile, defaultConfiguration);
		} else {
			// No JVM property; Trying to find configuration file in classpath
			configurationFile = ConfigurationKeys.CUSTOM_PROPERTIES_LOCATION_CLASSPATH;
			LOGGER.info("Loading properties from properties file in classpath: '{}'", configurationFile);
			loadConfiguration = ConfigurationFactory.loadConfigurationFromResource(configurationFile,
					defaultConfiguration);
		}
		// 1.JVM-params -> 2.properties file -> 3.default properties file
		return ConfigurationFactory.getSystemPropertiesStartingWith(ConfigurationKeys.PREFIX, loadConfiguration);
	}

	/**
	 * Returns an empty properties map with a fallback on the default configuration.
	 *
	 * @return default configuration
	 */
	public static final Configuration createDefaultConfiguration() {
		return new Configuration(ConfigurationFactory.defaultConfiguration());
	}

	/**
	 * Creates a new configuration based on the given properties file with fallback on the default values. If the file
	 * does not exists, a warning is logged and an empty configuration with fallback on the default configuration is
	 * returned.
	 *
	 * @param configurationFile
	 *            The file which contains the configuration.
	 *
	 * @return The created Configuration
	 */
	public static final Configuration createConfigurationFromFile(final String configurationFile) {
		return ConfigurationFactory.loadConfigurationFromFile(configurationFile,
				ConfigurationFactory.defaultConfiguration());
	}

	/**
	 * Returns a properties map with the default configuration.
	 *
	 * @return The created Configuration
	 */
	private static final Configuration defaultConfiguration() {
		return ConfigurationFactory.loadConfigurationFromResource(ConfigurationKeys.DEFAULT_PROPERTIES_LOCATION_CLASSPATH, null);
	}

	/**
	 * Returns the properties loaded from file propertiesFn with fallback on the default values. If the file does not
	 * exists, a warning is logged and an empty configuration with fallback on the default configuration is returned.
	 *
	 * @param propertiesFn
	 *            The file which contains the properties.
	 * @param defaultValues
	 *            The configuration containing the default values.
	 *
	 * @return The created Configuration
	 */
	private static final Configuration loadConfigurationFromFile(final String propertiesFn,
			final Configuration defaultValues) {
		final Configuration properties = new Configuration(defaultValues);
		InputStream is = null; // NOPMD (null)
		try {
			try {
				is = new FileInputStream(propertiesFn);
			} catch (final FileNotFoundException ex) {
				// if not found as absolute path try within the classpath
				final URL resourceUrl = ConfigurationFactory.loadKiekerPropertiesFile(propertiesFn);
				if (resourceUrl == null) {
					LOGGER.warn("File '{}' not found", propertiesFn);
					return new Configuration(defaultValues);
				}
				is = resourceUrl.openStream();
			}
			properties.load(is);
			return properties;
		} catch (final IOException ex) {
			LOGGER.error("Error reading file '{}'", propertiesFn, ex);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (final IOException ex) {
					LOGGER.warn("Failed to close FileInputStream", ex);
				}
			}
		}
		return new Configuration(defaultValues);
	}

	/**
	 * Returns the properties loaded from the resource name with fallback on the default values. If the file does not
	 * exists, a warning is logged and an empty configuration with fallback on the default configuration is returned.
	 *
	 * @param propertiesFn
	 *            The resource name which contains the properties.
	 * @param defaultValues
	 *            The configuration containing the default values.
	 *
	 * @return The created Configuration
	 */
	private static final Configuration loadConfigurationFromResource(final String propertiesFn,
			final Configuration defaultValues) {
		final URL resourceUrl = ConfigurationFactory.loadKiekerPropertiesFile(propertiesFn);
		if (resourceUrl == null) {
			LOGGER.warn("File '{}' not found in classpath", propertiesFn);
		} else {
			try (final InputStream is = resourceUrl.openStream()) {
				final Configuration properties = new Configuration(defaultValues);
				properties.load(is);
				return properties;
			} catch (final IOException ex) {
				LOGGER.error("Error reading file '{}'", propertiesFn, ex);
			}
		}
		return new Configuration(defaultValues);
	}

	/**
	 * @param propertiesFileName
	 *            the relative file name within the class path
	 * @return A {@link java.net.URL} object or null if no resource with this name is found
	 */
	private static URL loadKiekerPropertiesFile(final String propertiesFileName) {
		String resourceName = propertiesFileName;
		if (!resourceName.startsWith("/")) {
			// Class.getResource(..) requires a "/" at the beginning to load non-class resources
			resourceName = "/" + resourceName;
		}
		return ConfigurationFactory.class.getResource(resourceName);
	}

	/**
	 * Returns the system properties starting with the given prefix.
	 *
	 * @param prefix
	 *            The prefix to search for.
	 * @param defaultValues
	 *            The configuration containing the default values.
	 *
	 * @return The created Configuration
	 */
	private static final Configuration getSystemPropertiesStartingWith(final String prefix,
			final Configuration defaultValues) {
		final Configuration configuration = new Configuration(defaultValues);
		final Properties properties = System.getProperties();
		final Enumeration<?> keys = properties.propertyNames();
		while (keys.hasMoreElements()) {
			final String property = (String) keys.nextElement();
			if (property.startsWith(prefix)) {
				configuration.setProperty(property, properties.getProperty(property));
			}
		}
		return configuration;
	}
}
