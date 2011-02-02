package kieker.test.monitoring.junit.core.configuration;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2010 the Kieker Project
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

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.monitoring.core.configuration.ConfigurationProperties;
import kieker.monitoring.core.configuration.IMonitoringConfiguration;
import kieker.monitoring.core.configuration.MonitoringConfiguration;
import kieker.monitoring.writer.DummyLogWriter;
import kieker.test.monitoring.junit.core.configuration.util.DefaultConfigurationFactory;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public class TestDefaultConfiguration extends TestCase {

	/**
	 * Creates a default configuration with a {@link DummyLogWriter} and checks
	 * the values of {@link MonitoringConfiguration#isDebugEnabled()} and
	 * {@link MonitoringConfiguration#isMonitoringEnabled()}, as well as the
	 * configuration name are correctly set to the default value.
	 */
	public void testDefaultConfigurationHasDefaultDebugAndMonitoringEnabledValuesAndCorrectName() {
		final String configName = "DefaultConfig";
		final IMonitoringConfiguration config = DefaultConfigurationFactory
				.createDefaultConfigWithDummyWriter(configName);
		Assert.assertEquals("Wrong default value for debug property",
				ConfigurationProperties.DEBUG_ENABLED.getDefaultValue(),
				Boolean.toString(config.isDebugEnabled()));
		Assert.assertEquals(
				"Wrong default value for monitoringEnable property",
				ConfigurationProperties.MONITORING_ENABLED.getDefaultValue(),
				Boolean.toString(config.isMonitoringEnabled()));
		Assert.assertEquals("Wrong configuration name", configName,
				config.getName());
	}
}
