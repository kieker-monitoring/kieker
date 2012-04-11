/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.test.monitoring.junit.core;

import junit.framework.Assert;

import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.test.monitoring.junit.util.DefaultConfigurationFactory;

/**
 * @author Andre van Hoorn, Jan Waller
 */
public class TestControllerConstruction { // NOCS

	@Test
	public void testConstructionFromConfig() {

		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		{// Test with default values // NOCS
			final IMonitoringController kieker = MonitoringController.createInstance(configuration);
			Assert.assertEquals("monitoring should not be terminated", false, kieker.isMonitoringTerminated());
			Assert.assertEquals("monitoringEnabled values differ", configuration.getBooleanProperty(ConfigurationFactory.MONITORING_ENABLED),
					kieker.isMonitoringEnabled());
			kieker.terminateMonitoring();
		}
		{// NOCS
			configuration
					.setProperty(ConfigurationFactory.MONITORING_ENABLED,
							Boolean.toString(!configuration.getBooleanProperty(ConfigurationFactory.MONITORING_ENABLED)));
			final IMonitoringController kieker = MonitoringController.createInstance(configuration);
			Assert.assertEquals("monitoring should not be terminated", false, kieker.isMonitoringTerminated());
			Assert.assertEquals("monitoringEnabled values differ", configuration.getBooleanProperty(ConfigurationFactory.MONITORING_ENABLED),
					kieker.isMonitoringEnabled());
			kieker.terminateMonitoring();
		}
	}

	/**
	 * Make sure that {@link MonitoringController#getInstance()} always returns the same instance.
	 */
	@Test
	public void testSingletonGetterOnlyOneInstance() {
		Assert.assertSame("singleton getter returned different objects", MonitoringController.getInstance(), MonitoringController.getInstance());
		Assert.assertEquals("monitoring should not be terminated", false, MonitoringController.getInstance().isMonitoringTerminated());
	}
}
