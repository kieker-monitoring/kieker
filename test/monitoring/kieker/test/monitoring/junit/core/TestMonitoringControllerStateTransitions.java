package kieker.test.monitoring.junit.core;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.test.monitoring.junit.core.util.DefaultConfigurationFactory;

/**
 * @author Andre van Hoorn, Jan Waller
 */
public class TestMonitoringControllerStateTransitions extends TestCase {
	public void testMonitoringEnabledToDisabled() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		configuration.setProperty(Configuration.MONITORING_ENABLED, "true");
		final IMonitoringController kieker = MonitoringController.createInstance(configuration);
		{ /* Check values when enabled */
			Assert.assertEquals("Unexpected monitoringEnabled value", true, kieker.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false, kieker.isMonitoringTerminated());
		}
		{
			/* Change to disabled */
			Assert.assertTrue("disableMonitoring returned false", kieker.disableMonitoring());
		}
		{ /* Check values when disabled */
			Assert.assertEquals("Unexpected monitoringEnabled value", false, kieker.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false, kieker.isMonitoringTerminated());
		}
		kieker.terminateMonitoring();
	}

	public void testMonitoringDisabledToEnabled() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		configuration.setProperty(Configuration.MONITORING_ENABLED, "false");
		final IMonitoringController kieker = MonitoringController.createInstance(configuration);
		{ /* Check values when disabled */
			Assert.assertEquals("Unexpected monitoringEnabled value", false, kieker.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false, kieker.isMonitoringTerminated());
		}
		{
			/* Change to enabled */
			Assert.assertTrue("enableMonitoring returned false", kieker.enableMonitoring());
		}
		{ /* Check values when enabled */
			Assert.assertEquals("Unexpected monitoringEnabled value", true, kieker.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false, kieker.isMonitoringTerminated());
		}
		kieker.terminateMonitoring();
	}

	public void testMonitoringEnabledToTerminated() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		configuration.setProperty(Configuration.MONITORING_ENABLED, "true");
		final IMonitoringController kieker = MonitoringController.createInstance(configuration);
		/* Change to terminated */
		kieker.terminateMonitoring();
		{ /* Check values when terminated */
			Assert.assertEquals("Unexpected monitoringEnabled value", false, kieker.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", true, kieker.isMonitoringTerminated());
		}
		kieker.terminateMonitoring();
	}

	public void testMonitoringDisabledToTerminated() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		configuration.setProperty(Configuration.MONITORING_ENABLED, "false");
		final IMonitoringController kieker = MonitoringController.createInstance(configuration);
		/* Change to terminated */
		kieker.terminateMonitoring();
		{ /* Check values when terminated */
			Assert.assertEquals("Unexpected monitoringEnabled value", false, kieker.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", true, kieker.isMonitoringTerminated());
		}
		kieker.terminateMonitoring();
	}

	public void testMonitoringTerminatedToEnabledMustFail() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		final IMonitoringController kieker = MonitoringController.createInstance(configuration);
		/* Change to terminated */
		Assert.assertTrue("Failed to enableMonitoring", kieker.enableMonitoring());
		kieker.terminateMonitoring();
		Assert.assertFalse("Must not transition from terminated to enabled", kieker.enableMonitoring());
		kieker.terminateMonitoring();
	}

	public void testMonitoringTerminatedToDisabledMustFail() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		final IMonitoringController kieker = MonitoringController.createInstance(configuration);
		/* Change to terminated */
		Assert.assertTrue("Failed to disableMonitoring", kieker.disableMonitoring());
		kieker.terminateMonitoring();
		Assert.assertTrue("Transition to Disabled is always possible", kieker.disableMonitoring());
		kieker.terminateMonitoring();
	}
}
