/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.monitoring.junit.core.controller;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.monitoring.util.DefaultConfigurationFactory;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.3
 */
public class TestMonitoringControllerStateTransitions extends AbstractKiekerTest { // NOCS

	/**
	 * Test if disabling enabled monitoring works.
	 */
	@Test
	public void testMonitoringEnabledToDisabled() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		configuration.setProperty(ConfigurationFactory.MONITORING_ENABLED, "true");
		final IMonitoringController kieker = MonitoringController.createInstance(configuration);

		// Check values when enabled
		Assert.assertEquals("Unexpected monitoringEnabled value", true, kieker.isMonitoringEnabled());
		Assert.assertEquals("Unexpected monitoringTerminated value", false, kieker.isMonitoringTerminated());

		// Change to disabled
		Assert.assertTrue("disableMonitoring returned false", kieker.disableMonitoring());

		// Check values when disabled
		Assert.assertEquals("Unexpected monitoringEnabled value", false, kieker.isMonitoringEnabled());
		Assert.assertEquals("Unexpected monitoringTerminated value", false, kieker.isMonitoringTerminated());

		kieker.terminateMonitoring();
	}

	/**
	 * Test if enabling monitoring when starting disabled works.
	 */
	@Test
	public void testMonitoringDisabledToEnabled() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		configuration.setProperty(ConfigurationFactory.MONITORING_ENABLED, "false");
		final IMonitoringController kieker = MonitoringController.createInstance(configuration);
		// Check values when disabled
		Assert.assertEquals("Unexpected monitoringEnabled value", false, kieker.isMonitoringEnabled());
		Assert.assertEquals("Unexpected monitoringTerminated value", false, kieker.isMonitoringTerminated());

		// Change to enabled
		Assert.assertTrue("enableMonitoring returned false", kieker.enableMonitoring());

		// Check values when enabled
		Assert.assertEquals("Unexpected monitoringEnabled value", true, kieker.isMonitoringEnabled());
		Assert.assertEquals("Unexpected monitoringTerminated value", false, kieker.isMonitoringTerminated());

		kieker.terminateMonitoring();
	}

	/**
	 * Test if terminating an enabled controller works.
	 */
	@Test
	public void testMonitoringEnabledToTerminated() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		configuration.setProperty(ConfigurationFactory.MONITORING_ENABLED, "true");
		final IMonitoringController kieker = MonitoringController.createInstance(configuration);
		// Change to terminated
		kieker.terminateMonitoring();

		// Check values when terminated
		Assert.assertEquals("Unexpected monitoringEnabled value", false, kieker.isMonitoringEnabled());
		Assert.assertEquals("Unexpected monitoringTerminated value", true, kieker.isMonitoringTerminated());

		kieker.terminateMonitoring();
	}

	/**
	 * Test if terminating an disabled controller works.
	 */
	@Test
	public void testMonitoringDisabledToTerminated() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		configuration.setProperty(ConfigurationFactory.MONITORING_ENABLED, "false");
		final IMonitoringController kieker = MonitoringController.createInstance(configuration);
		// Change to terminated
		kieker.terminateMonitoring();
		{ // Check values when terminated // NOCS
			Assert.assertEquals("Unexpected monitoringEnabled value", false, kieker.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", true, kieker.isMonitoringTerminated());
		}
		kieker.terminateMonitoring();
	}

	/**
	 * Test if enabling of a terminated controller fails (as expected).
	 */
	@Test
	public void testMonitoringTerminatedToEnabledMustFail() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		final IMonitoringController kieker = MonitoringController.createInstance(configuration);
		// Change to terminated
		Assert.assertTrue("Failed to enableMonitoring", kieker.enableMonitoring());
		kieker.terminateMonitoring();
		Assert.assertFalse("Must not transition from terminated to enabled", kieker.enableMonitoring());
		kieker.terminateMonitoring();
	}

	/**
	 * Test if disabling of a terminated controller fails (as expected).
	 */
	@Test
	public void testMonitoringTerminatedToDisabledMustFail() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		final IMonitoringController kieker = MonitoringController.createInstance(configuration);
		// Change to terminated
		Assert.assertTrue("Failed to disableMonitoring", kieker.disableMonitoring());
		kieker.terminateMonitoring();
		Assert.assertTrue("Transition to Disabled is always possible", kieker.disableMonitoring());
		kieker.terminateMonitoring();
	}
}
