package kieker.test.monitoring.junit.core;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.monitoring.core.MonitoringController;
import kieker.monitoring.core.configuration.IMonitoringConfiguration;
import kieker.test.monitoring.junit.core.configuration.util.DefaultConfigurationFactory;

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
 * @author Andre van Hoorn, Jan Waller
 */
public class TestMonitoringControllerConstruction extends TestCase {
	
	/**
	 * 
	 */
	public void testConstructionFromConfig() {

		{
			// Test with default values
			final IMonitoringConfiguration config = DefaultConfigurationFactory.createDefaultConfigWithDummyWriter();
			final MonitoringController ctrl = new MonitoringController(config);
			Assert.assertEquals("monitoring should not be terminated", false, ctrl.isMonitoringTerminated());
			Assert.assertEquals("monitoringEnabled values differ", config.isMonitoringEnabled(), ctrl.isMonitoringEnabled());
			Assert.assertEquals("monitoringDisabled values differ", !config.isMonitoringEnabled(), ctrl.isMonitoringDisabled());
			Assert.assertEquals("debugEnabled values differ", config.isDebugEnabled(), ctrl.isDebugEnabled());
			Assert.assertEquals("hostName values differ", config.getHostName(), ctrl.getHostName());
			Assert.assertSame("log writers differ", config.getMonitoringLogWriter(), ctrl.getMonitoringLogWriter());
			ctrl.terminateMonitoring();
		}

		{
			// Don't use the same Configuration twice! Change values and try again!
			final IMonitoringConfiguration config = DefaultConfigurationFactory.createDefaultConfigWithDummyWriter();
			config.setDebugEnabled(!config.isDebugEnabled());
			config.setMonitoringEnabled(!config.isMonitoringEnabled());
			config.setHostName(config.getHostName() + "__");

			final MonitoringController ctrl = new MonitoringController(config);
			Assert.assertEquals("monitoring should not be terminated", false, ctrl.isMonitoringTerminated());
			Assert.assertEquals("monitoringEnabled values differ", config.isMonitoringEnabled(), ctrl.isMonitoringEnabled());
			Assert.assertEquals("monitoringDisabled values differ", !config.isMonitoringEnabled(), ctrl.isMonitoringDisabled());
			Assert.assertEquals("debugEnabled values differ", config.isDebugEnabled(), ctrl.isDebugEnabled());
			Assert.assertEquals("hostName values differ", config.getHostName(), ctrl.getHostName());
			Assert.assertSame("log writers differ", config.getMonitoringLogWriter(), ctrl.getMonitoringLogWriter());
			ctrl.terminateMonitoring();
		}
	}

	/**
	 * Make sure that {@link MonitoringController#getInstance()} always returns
	 * the same instance.
	 * 
	 */
	public void testSingletonGetterOnlyOneInstance() {
		Assert.assertSame("singleton getter returned different objects", MonitoringController.getInstance(), MonitoringController.getInstance());
	}
}
