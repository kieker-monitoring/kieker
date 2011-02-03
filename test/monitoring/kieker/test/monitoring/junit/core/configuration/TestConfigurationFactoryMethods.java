package kieker.test.monitoring.junit.core.configuration;

import java.util.Properties;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.monitoring.core.configuration.ConfigurationProperties;
import kieker.monitoring.core.configuration.IMonitoringConfiguration;
import kieker.monitoring.core.configuration.MonitoringConfiguration;
import kieker.monitoring.writer.DummyWriter;

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
 * Tests whether the factory methods of {@link MonitoringConfiguration} return
 * instances and performs basic checks on these.
 * 
 * @author Andre van Hoorn, Jan Waller
 */
public class TestConfigurationFactoryMethods extends TestCase {

	/**
	 * Tests {@link MonitoringConfiguration#createConfiguration(String, Properties)}.
	 * 
	 * Creates a configuration based on the default configuration file
	 * {@link ConfigurationProperties#KIEKER_CUSTOM_PROPERTIES_LOCATION_DEFAULT} contained in the Kieker library jar.
	 */
	public void testCreationDefaultPropertiesFileInJar() {
		final Properties properties = ConfigurationProperties.getDefaultProperties();
		final IMonitoringConfiguration config = MonitoringConfiguration.createConfiguration("ConfigName", properties);
		Assert.assertNotNull("Config is null", config);
		Assert.assertNotNull("writer is null", config.getMonitoringLogWriter());
		// The following values must be the default values:
		final Properties defaultProperties = ConfigurationProperties.getDefaultProperties();
		Assert.assertEquals("Wrong default value for " + ConfigurationProperties.MONITORING_DATA_WRITER_CLASSNAME + " property", 
				ConfigurationProperties.getStringProperty(defaultProperties, ConfigurationProperties.MONITORING_DATA_WRITER_CLASSNAME),
				config.getMonitoringLogWriter().getClass().getName());
		Assert.assertEquals("Wrong default value for " + ConfigurationProperties.DEBUG_ENABLED + " property", 
				ConfigurationProperties.getBooleanProperty(defaultProperties, ConfigurationProperties.DEBUG_ENABLED),
				config.isDebugEnabled());
		Assert.assertEquals("Wrong default value for " + ConfigurationProperties.MONITORING_ENABLED + " property", 
				ConfigurationProperties.getBooleanProperty(defaultProperties, ConfigurationProperties.MONITORING_ENABLED),
				config.isMonitoringEnabled());
		Assert.assertEquals("Wrong default value for " + ConfigurationProperties.PERIODIC_SENSORS_EXECUTOR_POOL_SIZE + " property", 
				ConfigurationProperties.getIntProperty(defaultProperties, ConfigurationProperties.PERIODIC_SENSORS_EXECUTOR_POOL_SIZE),
				config.getPeriodicSensorsExecutorPoolSize());
		Assert.assertEquals("Wrong default value for " + ConfigurationProperties.INITIAL_EXPERIMENT_ID + " property", 
				ConfigurationProperties.getIntProperty(defaultProperties, ConfigurationProperties.INITIAL_EXPERIMENT_ID),
				config.getInitialExperimentId());
	}
	
	/**
	 * Tests {@link MonitoringConfiguration#createConfiguration(String, Properties)}
	 */
	public void testCreationDefaultWithOwnWriter() {
		final Properties properties = ConfigurationProperties.getDefaultProperties();
		properties.setProperty(ConfigurationProperties.MONITORING_DATA_WRITER_CLASSNAME, DummyWriter.class.getName());
		final IMonitoringConfiguration config = MonitoringConfiguration.createConfiguration("ConfigName", properties);
		Assert.assertNotNull("Config is null", config);
		Assert.assertNotNull("writer is null", config.getMonitoringLogWriter());
		// The following values must be the default values:
		final Properties defaultProperties = ConfigurationProperties.getDefaultProperties();
		Assert.assertEquals("Wrong default value for " + ConfigurationProperties.DEBUG_ENABLED + " property", 
				ConfigurationProperties.getBooleanProperty(defaultProperties, ConfigurationProperties.DEBUG_ENABLED),
				config.isDebugEnabled());
		Assert.assertEquals("Wrong default value for " + ConfigurationProperties.MONITORING_ENABLED + " property", 
				ConfigurationProperties.getBooleanProperty(defaultProperties, ConfigurationProperties.MONITORING_ENABLED),
				config.isMonitoringEnabled());
		Assert.assertEquals("Wrong default value for " + ConfigurationProperties.PERIODIC_SENSORS_EXECUTOR_POOL_SIZE + " property", 
				ConfigurationProperties.getIntProperty(defaultProperties, ConfigurationProperties.PERIODIC_SENSORS_EXECUTOR_POOL_SIZE),
				config.getPeriodicSensorsExecutorPoolSize());
		Assert.assertEquals("Wrong default value for " + ConfigurationProperties.INITIAL_EXPERIMENT_ID + " property", 
				ConfigurationProperties.getIntProperty(defaultProperties, ConfigurationProperties.INITIAL_EXPERIMENT_ID),
				config.getInitialExperimentId());
	}

	/**
	 * Tests {@link MonitoringConfiguration#createSingletonConfiguration()}.
	 */
	public void testCreationSingletonConfiguration() {
		final IMonitoringConfiguration config = MonitoringConfiguration.createSingletonConfiguration();
		Assert.assertNotNull("Config is null", config);
		Assert.assertNotNull("writer is null", config.getMonitoringLogWriter());
	}
	
	/**
	 * Tests {@link MonitoringConfiguration#createSingletonConfiguration()}.
	 * 
	 * FIXME: "Missing test: Should test combinations of JVM-Params, Filenames, etc."
	 */
	public void testCreationSingletonConfigurationVariants() {}


	/**
	 * Tests {@link MonitoringConfiguration#createConfiguration(String, String)}.
	 * 
	 * FIXME: "File should be included correctly"
	 */
	public void testCreationCustomFileConfiguration() {
		final String EXAMPLE_CONFIG_FILE_IN_TRUNK = "META-INF/kieker.monitoring.properties.test";
		final IMonitoringConfiguration config = MonitoringConfiguration.createConfiguration("ConfigName", EXAMPLE_CONFIG_FILE_IN_TRUNK);
		Assert.assertNotNull("Config is null", config);
		Assert.assertNotNull("writer is null", config.getMonitoringLogWriter());
	}
}
