package kieker.test.monitoring.junit.core;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.controller.StateController;
import kieker.test.monitoring.junit.util.DefaultConfigurationFactory;

/**
 * @author Andre van Hoorn, Jan Waller
 */
public class TestControllerConstruction extends TestCase {

	/**
	 * 
	 */
	public void testConstructionFromConfig() {

		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		{
			// Test with default values
			final IMonitoringController kieker = MonitoringController.createInstance(configuration);
			Assert.assertEquals("monitoring should not be terminated",
					false,
					kieker.isMonitoringTerminated());
			Assert.assertEquals("monitoringEnabled values differ",
					configuration.getBooleanProperty(Configuration.MONITORING_ENABLED),
					kieker.isMonitoringEnabled());
//			Assert.assertSame("log writers differ",
//					configuration.getStringProperty(Configuration.WRITER_CLASSNAME),
//					kieker.getMonitoringWriter().getClass().getName());
			kieker.terminateMonitoring();
		}

		{
			configuration.setProperty(Configuration.MONITORING_ENABLED,
					Boolean.toString(!configuration.getBooleanProperty(Configuration.MONITORING_ENABLED)));
			final IMonitoringController kieker = MonitoringController.createInstance(configuration);
			Assert.assertEquals("monitoring should not be terminated",
					false,
					kieker.isMonitoringTerminated());
			Assert.assertEquals("monitoringEnabled values differ",
					configuration.getBooleanProperty(Configuration.MONITORING_ENABLED),
					kieker.isMonitoringEnabled());
//			Assert.assertSame("log writers differ",
//					configuration.getStringProperty(Configuration.WRITER_CLASSNAME),
//					kieker.getMonitoringWriter().getClass().getName());
			kieker.terminateMonitoring();
		}
	}

	/**
	 * Make sure that {@link StateController#getInstance()} always returns the same instance.
	 */
	public void testSingletonGetterOnlyOneInstance() {
		Assert.assertSame("singleton getter returned different objects", MonitoringController.getInstance(), MonitoringController.getInstance());
		Assert.assertEquals("monitoring should not be terminated",
				false,
				MonitoringController.getInstance().isMonitoringTerminated());
	}
}
