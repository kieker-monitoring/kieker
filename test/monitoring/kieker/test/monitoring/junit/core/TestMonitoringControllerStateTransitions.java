package kieker.test.monitoring.junit.core;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.monitoring.core.IMonitoringController;
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
public class TestMonitoringControllerStateTransitions extends TestCase {
	public void testMonitoringEnabledToDisabled() {
		final IMonitoringConfiguration config = DefaultConfigurationFactory.createDefaultConfigWithDummyWriter();
		config.setMonitoringEnabled(true); // make sure that monitoring is enabled
		final IMonitoringController ctrl = new MonitoringController(config);
		{ /* Check values when enabled */
			Assert.assertEquals("Unexpected monitoringEnabled value", true, ctrl.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", false, ctrl.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false, ctrl.isMonitoringTerminated());
		}
		{
			/* Change to disabled */
			Assert.assertTrue("disableMonitoring returned false", ctrl.disableMonitoring());
		}
		{ /* Check values when disabled */
			Assert.assertEquals("Unexpected monitoringEnabled value", false, ctrl.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", true, ctrl.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false, ctrl.isMonitoringTerminated());
		}
		ctrl.terminateMonitoring();
	}

	public void testMonitoringDisabledToEnabled() {
		final IMonitoringConfiguration config = DefaultConfigurationFactory.createDefaultConfigWithDummyWriter();
		config.setMonitoringEnabled(false); // make sure that monitoring enabled
		final IMonitoringController ctrl = new MonitoringController(config);
		{ /* Check values when disabled */
			Assert.assertEquals("Unexpected monitoringEnabled value", false, ctrl.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", true, ctrl.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false, ctrl.isMonitoringTerminated());
		}
		{
			/* Change to enabled */
			Assert.assertTrue("enableMonitoring returned false", ctrl.enableMonitoring());
		}
		{ /* Check values when enabled */
			Assert.assertEquals("Unexpected monitoringEnabled value", true, ctrl.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", false, ctrl.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false, ctrl.isMonitoringTerminated());
		}
		ctrl.terminateMonitoring();
	}

	public void testMonitoringEnabledToTerminated() {
		final IMonitoringConfiguration config = DefaultConfigurationFactory.createDefaultConfigWithDummyWriter();
		config.setMonitoringEnabled(true); // make sure that monitoring enabled
		final IMonitoringController ctrl = new MonitoringController(config);
		/* Change to terminated */
		ctrl.terminateMonitoring();
		{ /* Check values when terminated */
			Assert.assertEquals("Unexpected monitoringEnabled value", false, ctrl.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", false, ctrl.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", true, ctrl.isMonitoringTerminated());
		}
		ctrl.terminateMonitoring();
	}

	public void testMonitoringDisabledToTerminated() {
		final IMonitoringConfiguration config = DefaultConfigurationFactory.createDefaultConfigWithDummyWriter();
		config.setMonitoringEnabled(false); // make sure that monitoring enabled
		final IMonitoringController ctrl = new MonitoringController(config);
		/* Change to terminated */
		ctrl.terminateMonitoring();
		{ /* Check values when terminated */
			Assert.assertEquals("Unexpected monitoringEnabled value", false, ctrl.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", false, ctrl.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", true, ctrl.isMonitoringTerminated());
		}
		ctrl.terminateMonitoring();
	}

	public void testMonitoringTerminatedToEnabledMustFail() {
		final IMonitoringConfiguration config = DefaultConfigurationFactory.createDefaultConfigWithDummyWriter();
		final IMonitoringController ctrl = new MonitoringController(config);
		/* Change to terminated */
		Assert.assertTrue("Failed to enableMonitoring", ctrl.enableMonitoring());
		ctrl.terminateMonitoring();
		Assert.assertFalse("Must not transition from terminated to enabled", ctrl.enableMonitoring());
		ctrl.terminateMonitoring();
	}

	public void testMonitoringTerminatedToDisabledMustFail() {
		final IMonitoringConfiguration config = DefaultConfigurationFactory.createDefaultConfigWithDummyWriter();
		final IMonitoringController ctrl = new MonitoringController(config);
		/* Change to terminated */
		Assert.assertTrue("Failed to disableMonitoring", ctrl.disableMonitoring());
		ctrl.terminateMonitoring();
		Assert.assertFalse("Must not transition from terminated to disabled", ctrl.disableMonitoring());
		ctrl.terminateMonitoring();
	}
}
