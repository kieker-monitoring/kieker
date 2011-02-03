package kieker.test.monitoring.junit.core.configuration;

import java.util.Properties;

import junit.framework.Assert;
import junit.framework.TestCase;

import kieker.monitoring.core.configuration.ConfigurationProperties;
import kieker.monitoring.core.configuration.IMonitoringConfiguration;
import kieker.test.monitoring.junit.core.configuration.util.DefaultConfigurationFactory;


/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 the Kieker Project
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
 * @author Andre van Hoorn, Jan Waller
 */
public class TestDefaultConfiguration extends TestCase {

	/**
	 * Creates a default configuration with a DummyWriter and checks
	 * the default values.
	 */
	public void testDefaultConfigurationHasCorrectDefaultValues() {
		final IMonitoringConfiguration config = DefaultConfigurationFactory.createDefaultConfigWithDummyWriter();
		Assert.assertSame("Get returned other name than initialized before", DefaultConfigurationFactory.defaultName, config.getName());
		Assert.assertEquals("Get returned other writer than initialized before", DefaultConfigurationFactory.writerName, config.getMonitoringLogWriter().getClass().getName());
		//Since default values are only stored in a file it may be futile to test them?
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
}
