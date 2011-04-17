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
public class TestControllerConstruction extends TestCase {

	/**
	 * 
	 */
	public void testConstructionFromConfig() {

		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		{
			// Test with default values
			final MonitoringController kieker = MonitoringControllerFactory.createInstance(configuration);
			Assert.assertEquals("monitoring should not be terminated",
					false,
					kieker.isMonitoringTerminated());
			Assert.assertEquals("monitoringEnabled values differ",
					configuration.getBooleanProperty(Configuration.MONITORING_ENABLED),
					kieker.isMonitoringEnabled());
			Assert.assertEquals("monitoringDisabled values differ",
					!configuration.getBooleanProperty(Configuration.MONITORING_ENABLED),
					kieker.isMonitoringDisabled());
			Assert.assertSame("log writers differ",
					configuration.getStringProperty(Configuration.WRITER_CLASSNAME),
					kieker.getMonitoringWriter().getClass().getName());
			kieker.terminateMonitoring();
		}

		{
			configuration.setProperty(Configuration.MONITORING_ENABLED,
					Boolean.toString(!configuration.getBooleanProperty(Configuration.MONITORING_ENABLED)));
			final MonitoringController kieker = MonitoringControllerFactory.createInstance(configuration);
			Assert.assertEquals("monitoring should not be terminated",
					false,
					kieker.isMonitoringTerminated());
			Assert.assertEquals("monitoringEnabled values differ",
					configuration.getBooleanProperty(Configuration.MONITORING_ENABLED),
					kieker.isMonitoringEnabled());
			Assert.assertEquals("monitoringDisabled values differ",
					!configuration.getBooleanProperty(Configuration.MONITORING_ENABLED),
					kieker.isMonitoringDisabled());
			Assert.assertSame("log writers differ",
					configuration.getStringProperty(Configuration.WRITER_CLASSNAME),
					kieker.getMonitoringWriter().getClass().getName());
			kieker.terminateMonitoring();
		}
	}

	/**
	 * Make sure that {@link ControllerState#getInstance()} always returns the same instance.
	 */
	public void testSingletonGetterOnlyOneInstance() {
		Assert.assertSame("singleton getter returned different objects", MonitoringController.getInstance(), MonitoringController.getInstance());
		Assert.assertEquals("monitoring should not be terminated",
				false,
				MonitoringController.getInstance().isMonitoringTerminated());
	}
}
