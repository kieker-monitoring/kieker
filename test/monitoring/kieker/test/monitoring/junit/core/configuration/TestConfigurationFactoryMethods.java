/***************************************************************************
 * Copyright 2011 by
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

package kieker.test.monitoring.junit.core.configuration;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.monitoring.core.configuration.Configuration;
import kieker.test.monitoring.junit.util.DefaultConfigurationFactory;

/**
 * Tests whether the factory methods of {@link Configuration} return
 * instances and performs basic checks on these.
 * 
 * @author Andre van Hoorn, Jan Waller
 */
public class TestConfigurationFactoryMethods extends TestCase {

	private void testValues(final Configuration configuration) {
		Assert.assertNotNull("Configuration is null", configuration);
		// Monitoring controller
		Assert.assertNotNull(Configuration.MONITORING_ENABLED + " must not be empty", configuration.getProperty(Configuration.MONITORING_ENABLED));
		Assert.assertNotNull(Configuration.CONTROLLER_NAME + " must not be empty", configuration.getProperty(Configuration.CONTROLLER_NAME));
		// HostName may be empty!
		Assert.assertNotNull(Configuration.EXPERIMENT_ID + " must not be empty", configuration.getProperty(Configuration.EXPERIMENT_ID));
		// JMX controller
		Assert.assertNotNull(Configuration.ACTIVATE_JMX + " must not be empty", configuration.getProperty(Configuration.ACTIVATE_JMX));
		// Writer controller
		Assert.assertNotNull(Configuration.AUTO_SET_LOGGINGTSTAMP + " must not be empty", configuration.getProperty(Configuration.AUTO_SET_LOGGINGTSTAMP));
		Assert.assertNotNull(Configuration.WRITER_CLASSNAME + " must not be empty", configuration.getProperty(Configuration.WRITER_CLASSNAME));
		// TimeSource controller
		Assert.assertNotNull(Configuration.TIMER_CLASSNAME + " must not be empty", configuration.getProperty(Configuration.TIMER_CLASSNAME));
		// Sampling controller
		Assert.assertNotNull(Configuration.PERIODIC_SENSORS_EXECUTOR_POOL_SIZE + " must not be empty",
				configuration.getProperty(Configuration.PERIODIC_SENSORS_EXECUTOR_POOL_SIZE));
	}

	/**
	 * Tests {@link Configuration#createSingletonConfiguration()}.
	 */
	public void testCreationDefaultConfigurationWithDummyWriter() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		testValues(configuration);
		Assert.assertEquals("Writer must be " + DefaultConfigurationFactory.writerName, DefaultConfigurationFactory.writerName,
				configuration.getStringProperty(Configuration.WRITER_CLASSNAME));
	}

	/**
	 * Tests {@link Configuration#createSingletonConfiguration()}.
	 */
	public void testCreationSingletonConfiguration() {
		testValues(Configuration.createSingletonConfiguration());
	}

	/**
	 * Tests {@link Configuration#createDefaultConfiguration()}.
	 */
	public void testCreationDefaultConfiguration() {
		final Configuration configuration = Configuration.createDefaultConfiguration();
		testValues(configuration);
		// check for correct default values of required parameters
		// Monitoring controller
		Assert.assertEquals(true, configuration.getBooleanProperty(Configuration.MONITORING_ENABLED));
		Assert.assertEquals("KIEKER", configuration.getStringProperty(Configuration.CONTROLLER_NAME));
		Assert.assertEquals("", configuration.getStringProperty(Configuration.HOST_NAME));
		Assert.assertEquals(1, configuration.getIntProperty(Configuration.EXPERIMENT_ID));
		// JMX controller
		Assert.assertEquals(false, configuration.getBooleanProperty(Configuration.ACTIVATE_JMX));
		// Writer controller
		Assert.assertEquals(true, configuration.getBooleanProperty(Configuration.AUTO_SET_LOGGINGTSTAMP));
		Assert.assertEquals("kieker.monitoring.writer.filesystem.AsyncFsWriter", configuration.getStringProperty(Configuration.WRITER_CLASSNAME));
		// TimeSource controller
		Assert.assertEquals("kieker.monitoring.timer.DefaultSystemTimer", configuration.getStringProperty(Configuration.TIMER_CLASSNAME));
		// Sampling controller
		Assert.assertEquals(1, configuration.getIntProperty(Configuration.PERIODIC_SENSORS_EXECUTOR_POOL_SIZE));
	}

	/**
	 * Tests {@link Configuration#createSingletonConfiguration()}.
	 * 
	 * FIXME: "Missing test: Should test combinations of JVM-Params, Filenames, etc. How to do that?
	 * See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/130
	 */
	public void testCreationSingletonConfigurationVariants() {}

	/**
	 * Tests {@link Configuration#createConfigurationFromFile(String)}.
	 */
	public void testCreationfromFile() {
		final String EXAMPLE_CONFIG_FILE_IN_TRUNK = "test/monitoring/META-INF/kieker.monitoring.properties.test";
		final Configuration configuration = Configuration.createConfigurationFromFile(EXAMPLE_CONFIG_FILE_IN_TRUNK);
		testValues(configuration);
		Assert.assertEquals("KIEKER-TEST", configuration.getProperty(Configuration.CONTROLLER_NAME));
	}
}
