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
public class TestMonitoringControllerStateConstruction extends TestCase {
	public void testConstructionFromConfig() {
		final String name = "TheName";
		final IMonitoringConfiguration config = DefaultConfigurationFactory
				.createDefaultConfigWithDummyWriter(name);

		{
			/* Test with default values */
			final IMonitoringControllerState state = new MonitoringControllerState(
					config);
			Assert.assertEquals("monitoringEnabled values differ", config.isMonitoringEnabled(), state.isMonitoringEnabled());
			Assert.assertEquals("debugEnabled values differ", config.isDebugEnabled(), state.isDebugEnabled());
			Assert.assertEquals("hostName values differ", config.getHostName(), state.getHostName());
		}
		
		{
			/* Change values and try again */
			config.setDebugEnabled(!config.isDebugEnabled());
			config.setMonitoringEnabled(!config.isMonitoringEnabled());
			config.setHostName(config.getHostName()+"__");
			
			final IMonitoringControllerState state = new MonitoringControllerState(
					config);
			Assert.assertEquals("monitoringEnabled values differ", config.isMonitoringEnabled(), state.isMonitoringEnabled());
			Assert.assertEquals("debugEnabled values differ", config.isDebugEnabled(), state.isDebugEnabled());
			Assert.assertEquals("hostName values differ", config.getHostName(), state.getHostName());
		}
	}
}
