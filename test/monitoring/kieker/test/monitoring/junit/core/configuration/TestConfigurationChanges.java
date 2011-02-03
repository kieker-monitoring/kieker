package kieker.test.monitoring.junit.core.configuration;

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

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.monitoring.core.configuration.IMonitoringConfiguration;
import kieker.test.monitoring.junit.core.configuration.util.DefaultConfigurationFactory;

/**
 * @author Andre van Hoorn, Jan Waller
 */
public class TestConfigurationChanges extends TestCase {
	public void testSetIsDebugEnabled() {
		final IMonitoringConfiguration config = DefaultConfigurationFactory.createDefaultConfigWithDummyWriter();

		/* Enable debug and check */
		config.setDebugEnabled(true);
		Assert.assertEquals("Invalid value of field debug (default->true)", true, config.isDebugEnabled());

		/* Disable debug and check */
		config.setDebugEnabled(false);
		Assert.assertEquals("Invalid value of field debug (true->false)", false, config.isDebugEnabled());

		/* And one more time: Enable debug and check */
		config.setDebugEnabled(true);
		Assert.assertEquals("Invalid value of field debug (false->true)", true, config.isDebugEnabled());
	}

	public void testSetIsMonitoringEnabled() {
		final IMonitoringConfiguration config = DefaultConfigurationFactory.createDefaultConfigWithDummyWriter();

		/* Enable monitoring and check */
		config.setMonitoringEnabled(true);
		Assert.assertEquals("Invalid value of field monitoringEnabled (default->true)", true, config.isMonitoringEnabled());

		/* Disable monitoring and check */
		config.setMonitoringEnabled(false);
		Assert.assertEquals("Invalid value of field monitoringEnabled (true->false)", false, config.isMonitoringEnabled());

		/* And one more time: Enable monitoring and check */
		config.setMonitoringEnabled(true);
		Assert.assertEquals("Invalid value of field monitoringEnabled (false->true)", true, config.isMonitoringEnabled());
	}
	
	public void testSetGetPeriodicSensorsExecutorPoolSize() {
		final IMonitoringConfiguration config = DefaultConfigurationFactory.createDefaultConfigWithDummyWriter();

		/* Set PeriodicSensorsExecutorPoolSize and check */
		config.setPeriodicSensorsExecutorPoolSize(0);
		Assert.assertEquals("Invalid value of field periodicSensorsExecutorPoolSize (default->0)", 0, config.getPeriodicSensorsExecutorPoolSize());

		/* Set PeriodicSensorsExecutorPoolSize and check */
		config.setPeriodicSensorsExecutorPoolSize(10);
		Assert.assertEquals("Invalid value of field periodicSensorsExecutorPoolSize (0->10)", 10, config.getPeriodicSensorsExecutorPoolSize());
	}

	public void testSetGetInitialExperimentId() {
		final IMonitoringConfiguration config = DefaultConfigurationFactory.createDefaultConfigWithDummyWriter();

		/* Set PeriodicSensorsExecutorPoolSize and check */
		config.setInitialExperimentId(0);
		Assert.assertEquals("Invalid value of field initialExperimentId (default->0)", 0, config.getInitialExperimentId());

		/* Set PeriodicSensorsExecutorPoolSize and check */
		config.setInitialExperimentId(10);
		Assert.assertEquals("Invalid value of field initialExperimentId (0->10)", 10, config.getInitialExperimentId());
	}

	public void testSetGetHostName() {
		final IMonitoringConfiguration config = DefaultConfigurationFactory.createDefaultConfigWithDummyWriter();

		final String newHostName = config.getHostName() + "__";

		/* Set and get host name */
		config.setHostName(newHostName);
		Assert.assertSame("Get returned other host name than set before", newHostName, config.getHostName());
	}
}
