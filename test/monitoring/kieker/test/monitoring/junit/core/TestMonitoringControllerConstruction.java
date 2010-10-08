/**
 * 
 */
package kieker.test.monitoring.junit.core;

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

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.monitoring.core.MonitoringController;
import kieker.monitoring.core.configuration.IMonitoringConfiguration;
import kieker.test.monitoring.junit.core.configuration.util.DefaultConfigurationFactory;

/**
 * @author Andre van Hoorn
 * 
 */
public class TestMonitoringControllerConstruction extends TestCase {
	/**
	 * 
	 */
	public void testConstructionFromConfig() {
		final String name = "TheName";
		final IMonitoringConfiguration config = DefaultConfigurationFactory
				.createDefaultConfigWithDummyWriter(name);

		{
			/* Test with default values */
			final MonitoringController ctrl = new MonitoringController(config);
			Assert.assertEquals("monitoringEnabled values differ",
					config.isMonitoringEnabled(), ctrl.isMonitoringEnabled());
			Assert.assertEquals("debugEnabled values differ",
					config.isDebugEnabled(), ctrl.isDebugEnabled());
			Assert.assertEquals("hostName values differ", config.getHostName(),
					ctrl.getHostName());
			Assert.assertSame("log writers differ",
					config.getMonitoringLogWriter(),
					ctrl.getMonitoringLogWriter());
		}

		{
			/* Change values and try again */
			config.setDebugEnabled(!config.isDebugEnabled());
			config.setMonitoringEnabled(!config.isMonitoringEnabled());
			config.setHostName(config.getHostName() + "__");

			final MonitoringController ctrl = new MonitoringController(config);
			Assert.assertEquals("monitoringEnabled values differ",
					config.isMonitoringEnabled(), ctrl.isMonitoringEnabled());
			Assert.assertEquals("debugEnabled values differ",
					config.isDebugEnabled(), ctrl.isDebugEnabled());
			Assert.assertEquals("hostName values differ", config.getHostName(),
					ctrl.getHostName());
			Assert.assertSame("log writers differ",
					config.getMonitoringLogWriter(),
					ctrl.getMonitoringLogWriter());

		}
	}

	/**
	 * Make sure that {@link MonitoringController2#getInstance()} always returns
	 * the same instance.
	 * 
	 */
	public void testSingletonGetterOnlyOneInstance() {
		Assert.assertSame("singleton getter returned different objects",
				MonitoringController.getInstance(),
				MonitoringController.getInstance());
	}
}
