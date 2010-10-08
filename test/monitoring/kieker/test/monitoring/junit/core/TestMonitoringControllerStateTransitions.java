/**
 * 
 */
package kieker.test.monitoring.junit.core;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.monitoring.core.IMonitoringController;
import kieker.monitoring.core.MonitoringController;
import kieker.monitoring.core.configuration.IMonitoringConfiguration;
import kieker.test.monitoring.junit.core.configuration.util.DefaultConfigurationFactory;

/**
 * @author Andre van Hoorn
 * 
 */
public class TestMonitoringControllerStateTransitions extends TestCase {
	public void testMonitoringEnabledToDisabled() {
		final String name = "TheName";
		final IMonitoringConfiguration config = DefaultConfigurationFactory
				.createDefaultConfigWithDummyWriter(name);
		config.setMonitoringEnabled(true); // make sure that monitoring enabled
		final IMonitoringController ctrl = new MonitoringController(config);

		{ /* Check values when enabled */
			Assert.assertEquals("Unexpected monitoringEnabled value", true,
					ctrl.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", false,
					ctrl.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false,
					ctrl.isMonitoringTerminated());
		}

		{
			/* Change to disabled */
			Assert.assertTrue("disableMonitoring returned false",
					ctrl.disableMonitoring());
		}

		{ /* Check values when disabled */
			Assert.assertEquals("Unexpected monitoringEnabled value", false,
					ctrl.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", true,
					ctrl.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false,
					ctrl.isMonitoringTerminated());
		}
	}

	public void testMonitoringDisabledToEnabled() {
		final String name = "TheName";
		final IMonitoringConfiguration config = DefaultConfigurationFactory
				.createDefaultConfigWithDummyWriter(name);
		config.setMonitoringEnabled(false); // make sure that monitoring enabled
		final IMonitoringController ctrl = new MonitoringController(config);

		{ /* Check values when disabled */
			Assert.assertEquals("Unexpected monitoringEnabled value", false,
					ctrl.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", true,
					ctrl.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false,
					ctrl.isMonitoringTerminated());
		}

		{
			/* Change to enabled */
			Assert.assertTrue("enableMonitoring returned false",
					ctrl.enableMonitoring());
		}

		{ /* Check values when enabled */
			Assert.assertEquals("Unexpected monitoringEnabled value", true,
					ctrl.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", false,
					ctrl.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false,
					ctrl.isMonitoringTerminated());
		}
	}

	public void testMonitoringEnabledToTerminated() {
		final String name = "TheName";
		final IMonitoringConfiguration config = DefaultConfigurationFactory
				.createDefaultConfigWithDummyWriter(name);
		config.setMonitoringEnabled(true); // make sure that monitoring enabled
		final IMonitoringController ctrl = new MonitoringController(config);

		/** Check values when enabled covered by other tests */

		/* Change to terminated */
		ctrl.terminateMonitoring();

		{ /* Check values when terminated */
			Assert.assertEquals("Unexpected monitoringEnabled value", false,
					ctrl.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", false,
					ctrl.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", true,
					ctrl.isMonitoringTerminated());
		}
	}

	public void testMonitoringDisabledToTerminated() {
		final String name = "TheName";
		final IMonitoringConfiguration config = DefaultConfigurationFactory
				.createDefaultConfigWithDummyWriter(name);
		config.setMonitoringEnabled(false); // make sure that monitoring enabled
		final IMonitoringController ctrl = new MonitoringController(config);

		/** Check values when disabled covered by other tests */

		/* Change to terminated */
		ctrl.terminateMonitoring();

		{ /* Check values when terminated */
			Assert.assertEquals("Unexpected monitoringEnabled value", false,
					ctrl.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", false,
					ctrl.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", true,
					ctrl.isMonitoringTerminated());
		}
	}

	public void testMonitoringTerminatedToEnabledMustFail() {
		final String name = "TheName";
		final IMonitoringConfiguration config = DefaultConfigurationFactory
				.createDefaultConfigWithDummyWriter(name);
		final IMonitoringController ctrl = new MonitoringController(config);

		/** Check values when disabled covered by other tests */

		/* Change to terminated */
		Assert.assertTrue("Failed to enableMonitoring", ctrl.enableMonitoring());
		ctrl.terminateMonitoring();
		Assert.assertFalse("Must not transition from terminated to enabled",
				ctrl.enableMonitoring());
	}

	public void testMonitoringTerminatedToDisabledMustFail() {
		final String name = "TheName";
		final IMonitoringConfiguration config = DefaultConfigurationFactory
				.createDefaultConfigWithDummyWriter(name);
		final IMonitoringController ctrl = new MonitoringController(config);

		/** Check values when disabled covered by other tests */

		/* Change to terminated */
		Assert.assertTrue("Failed to disableMonitoring",
				ctrl.disableMonitoring());
		ctrl.terminateMonitoring();
		Assert.assertFalse("Must not transition from terminated to disabled",
				ctrl.disableMonitoring());
	}
}
