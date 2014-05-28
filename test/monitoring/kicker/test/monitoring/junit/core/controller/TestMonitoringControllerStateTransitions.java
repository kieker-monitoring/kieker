/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.test.monitoring.junit.core.controller;

import org.junit.Assert;
import org.junit.Test;

import kicker.common.configuration.Configuration;
import kicker.monitoring.core.configuration.ConfigurationFactory;
import kicker.monitoring.core.controller.IMonitoringController;
import kicker.monitoring.core.controller.MonitoringController;
import kicker.test.common.junit.AbstractKickerTest;
import kicker.test.monitoring.util.DefaultConfigurationFactory;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.3
 */
public class TestMonitoringControllerStateTransitions extends AbstractKickerTest { // NOCS
	@Test
	public void testMonitoringEnabledToDisabled() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		configuration.setProperty(ConfigurationFactory.MONITORING_ENABLED, "true");
		final IMonitoringController kicker = MonitoringController.createInstance(configuration);
		{ // Check values when enabled // NOCS
			Assert.assertEquals("Unexpected monitoringEnabled value", true, kicker.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false, kicker.isMonitoringTerminated());
		}
		{// NOCS
			// Change to disabled
			Assert.assertTrue("disableMonitoring returned false", kicker.disableMonitoring());
		}
		{// Check values when disabled // NOCS
			Assert.assertEquals("Unexpected monitoringEnabled value", false, kicker.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false, kicker.isMonitoringTerminated());
		}
		kicker.terminateMonitoring();
	}

	@Test
	public void testMonitoringDisabledToEnabled() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		configuration.setProperty(ConfigurationFactory.MONITORING_ENABLED, "false");
		final IMonitoringController kicker = MonitoringController.createInstance(configuration);
		{ // Check values when disabled // NOCS
			Assert.assertEquals("Unexpected monitoringEnabled value", false, kicker.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false, kicker.isMonitoringTerminated());
		}
		{// NOCS
			// Change to enabled
			Assert.assertTrue("enableMonitoring returned false", kicker.enableMonitoring());
		}
		{// Check values when enabled // NOCS
			Assert.assertEquals("Unexpected monitoringEnabled value", true, kicker.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false, kicker.isMonitoringTerminated());
		}
		kicker.terminateMonitoring();
	}

	@Test
	public void testMonitoringEnabledToTerminated() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		configuration.setProperty(ConfigurationFactory.MONITORING_ENABLED, "true");
		final IMonitoringController kicker = MonitoringController.createInstance(configuration);
		// Change to terminated
		kicker.terminateMonitoring();
		{ // Check values when terminated // NOCS
			Assert.assertEquals("Unexpected monitoringEnabled value", false, kicker.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", true, kicker.isMonitoringTerminated());
		}
		kicker.terminateMonitoring();
	}

	@Test
	public void testMonitoringDisabledToTerminated() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		configuration.setProperty(ConfigurationFactory.MONITORING_ENABLED, "false");
		final IMonitoringController kicker = MonitoringController.createInstance(configuration);
		// Change to terminated
		kicker.terminateMonitoring();
		{ // Check values when terminated // NOCS
			Assert.assertEquals("Unexpected monitoringEnabled value", false, kicker.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", true, kicker.isMonitoringTerminated());
		}
		kicker.terminateMonitoring();
	}

	@Test
	public void testMonitoringTerminatedToEnabledMustFail() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		final IMonitoringController kicker = MonitoringController.createInstance(configuration);
		// Change to terminated
		Assert.assertTrue("Failed to enableMonitoring", kicker.enableMonitoring());
		kicker.terminateMonitoring();
		Assert.assertFalse("Must not transition from terminated to enabled", kicker.enableMonitoring());
		kicker.terminateMonitoring();
	}

	@Test
	public void testMonitoringTerminatedToDisabledMustFail() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		final IMonitoringController kicker = MonitoringController.createInstance(configuration);
		// Change to terminated
		Assert.assertTrue("Failed to disableMonitoring", kicker.disableMonitoring());
		kicker.terminateMonitoring();
		Assert.assertTrue("Transition to Disabled is always possible", kicker.disableMonitoring());
		kicker.terminateMonitoring();
	}
}
