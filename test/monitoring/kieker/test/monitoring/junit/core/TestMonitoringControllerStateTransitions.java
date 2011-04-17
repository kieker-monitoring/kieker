package kieker.test.monitoring.junit.core;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.monitoring.core.MonitoringControllerFactory;
import kieker.monitoring.core.MonitoringController;
import kieker.monitoring.core.configuration.Configuration;
import kieker.test.monitoring.junit.core.util.DefaultConfigurationFactory;

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
public class TestMonitoringControllerStateTransitions extends TestCase {
	public void testMonitoringEnabledToDisabled() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		configuration.setProperty(Configuration.MONITORING_ENABLED, "true");
		final MonitoringController kieker = MonitoringControllerFactory.createInstance(configuration);
		{ /* Check values when enabled */
			Assert.assertEquals("Unexpected monitoringEnabled value", true, kieker.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", false, kieker.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false, kieker.isMonitoringTerminated());
		}
		{
			/* Change to disabled */
			Assert.assertTrue("disableMonitoring returned false", kieker.disableMonitoring());
		}
		{ /* Check values when disabled */
			Assert.assertEquals("Unexpected monitoringEnabled value", false, kieker.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", true, kieker.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false, kieker.isMonitoringTerminated());
		}
		kieker.terminateMonitoring();
	}

	public void testMonitoringDisabledToEnabled() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		configuration.setProperty(Configuration.MONITORING_ENABLED, "false");
		final MonitoringController kieker = MonitoringControllerFactory.createInstance(configuration);
		{ /* Check values when disabled */
			Assert.assertEquals("Unexpected monitoringEnabled value", false, kieker.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", true, kieker.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false, kieker.isMonitoringTerminated());
		}
		{
			/* Change to enabled */
			Assert.assertTrue("enableMonitoring returned false", kieker.enableMonitoring());
		}
		{ /* Check values when enabled */
			Assert.assertEquals("Unexpected monitoringEnabled value", true, kieker.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", false, kieker.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false, kieker.isMonitoringTerminated());
		}
		kieker.terminateMonitoring();
	}

	public void testMonitoringEnabledToTerminated() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		configuration.setProperty(Configuration.MONITORING_ENABLED, "true");
		final MonitoringController kieker = MonitoringControllerFactory.createInstance(configuration);
		/* Change to terminated */
		kieker.terminateMonitoring();
		{ /* Check values when terminated */
			Assert.assertEquals("Unexpected monitoringEnabled value", false, kieker.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", false, kieker.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", true, kieker.isMonitoringTerminated());
		}
		kieker.terminateMonitoring();
	}

	public void testMonitoringDisabledToTerminated() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		configuration.setProperty(Configuration.MONITORING_ENABLED, "false");
		final MonitoringController kieker = MonitoringControllerFactory.createInstance(configuration);
		/* Change to terminated */
		kieker.terminateMonitoring();
		{ /* Check values when terminated */
			Assert.assertEquals("Unexpected monitoringEnabled value", false, kieker.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", false, kieker.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", true, kieker.isMonitoringTerminated());
		}
		kieker.terminateMonitoring();
	}

	public void testMonitoringTerminatedToEnabledMustFail() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		final MonitoringController kieker = MonitoringControllerFactory.createInstance(configuration);
		/* Change to terminated */
		Assert.assertTrue("Failed to enableMonitoring", kieker.enableMonitoring());
		kieker.terminateMonitoring();
		Assert.assertFalse("Must not transition from terminated to enabled", kieker.enableMonitoring());
		kieker.terminateMonitoring();
	}

	public void testMonitoringTerminatedToDisabledMustFail() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		final MonitoringController kieker = MonitoringControllerFactory.createInstance(configuration);
		/* Change to terminated */
		Assert.assertTrue("Failed to disableMonitoring", kieker.disableMonitoring());
		kieker.terminateMonitoring();
		Assert.assertFalse("Must not transition from terminated to disabled", kieker.disableMonitoring());
		kieker.terminateMonitoring();
	}
}
