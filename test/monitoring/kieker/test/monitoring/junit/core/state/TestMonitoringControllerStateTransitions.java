/**
 * 
 */
package kieker.test.monitoring.junit.core.state;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.monitoring.core.configuration.IMonitoringConfiguration;
import kieker.monitoring.core.state.IMonitoringControllerState;
import kieker.monitoring.core.state.MonitoringControllerState;
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
		final IMonitoringControllerState state = new MonitoringControllerState(
				config);

		{ /* Check values when enabled */
			Assert.assertEquals("Unexpected monitoringEnabled value", true,
					state.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", false,
					state.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false,
					state.isMonitoringTerminated());
		}

		{
			/* Change to disabled */
			Assert.assertTrue("disableMonitoring returned false",
					state.disableMonitoring());
		}

		{ /* Check values when disabled */
			Assert.assertEquals("Unexpected monitoringEnabled value", false,
					state.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", true,
					state.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false,
					state.isMonitoringTerminated());
		}
	}

	public void testMonitoringDisabledToEnabled() {
		final String name = "TheName";
		final IMonitoringConfiguration config = DefaultConfigurationFactory
				.createDefaultConfigWithDummyWriter(name);
		config.setMonitoringEnabled(false); // make sure that monitoring enabled
		final IMonitoringControllerState state = new MonitoringControllerState(
				config);

		{ /* Check values when disabled */
			Assert.assertEquals("Unexpected monitoringEnabled value", false,
					state.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", true,
					state.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false,
					state.isMonitoringTerminated());
		}

		{
			/* Change to enabled */
			Assert.assertTrue("enableMonitoring returned false",
					state.enableMonitoring());
		}

		{ /* Check values when enabled */
			Assert.assertEquals("Unexpected monitoringEnabled value", true,
					state.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", false,
					state.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", false,
					state.isMonitoringTerminated());
		}
	}

	public void testMonitoringEnabledToTerminated() {
		final String name = "TheName";
		final IMonitoringConfiguration config = DefaultConfigurationFactory
				.createDefaultConfigWithDummyWriter(name);
		config.setMonitoringEnabled(true); // make sure that monitoring enabled
		final IMonitoringControllerState state = new MonitoringControllerState(
				config);

		/** Check values when enabled covered by other tests */

		/* Change to terminated */
		state.terminateMonitoring();

		{ /* Check values when terminated */
			Assert.assertEquals("Unexpected monitoringEnabled value", false,
					state.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", false,
					state.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", true,
					state.isMonitoringTerminated());
		}
	}

	public void testMonitoringDisabledToTerminated() {
		final String name = "TheName";
		final IMonitoringConfiguration config = DefaultConfigurationFactory
				.createDefaultConfigWithDummyWriter(name);
		config.setMonitoringEnabled(false); // make sure that monitoring enabled
		final IMonitoringControllerState state = new MonitoringControllerState(
				config);

		/** Check values when disabled covered by other tests */

		/* Change to terminated */
		state.terminateMonitoring();

		{ /* Check values when terminated */
			Assert.assertEquals("Unexpected monitoringEnabled value", false,
					state.isMonitoringEnabled());
			Assert.assertEquals("Unexpected monitoringDisabled value", false,
					state.isMonitoringDisabled());
			Assert.assertEquals("Unexpected monitoringTerminated value", true,
					state.isMonitoringTerminated());
		}
	}

	public void testMonitoringTerminatedToEnabledMustFail() {
		final String name = "TheName";
		final IMonitoringConfiguration config = DefaultConfigurationFactory
				.createDefaultConfigWithDummyWriter(name);
		final IMonitoringControllerState state = new MonitoringControllerState(
				config);

		/** Check values when disabled covered by other tests */

		/* Change to terminated */
		Assert.assertTrue("Failed to enableMonitoring",
				state.enableMonitoring());
		state.terminateMonitoring();
		Assert.assertFalse("Must not transition from terminated to enabled",
				state.enableMonitoring());
	}

	public void testMonitoringTerminatedToDisabledMustFail() {
		final String name = "TheName";
		final IMonitoringConfiguration config = DefaultConfigurationFactory
				.createDefaultConfigWithDummyWriter(name);
		final IMonitoringControllerState state = new MonitoringControllerState(
				config);

		/** Check values when disabled covered by other tests */

		/* Change to terminated */
		Assert.assertTrue("Failed to disableMonitoring",
				state.disableMonitoring());
		state.terminateMonitoring();
		Assert.assertFalse("Must not transition from terminated to disabled",
				state.disableMonitoring());
	}
}
