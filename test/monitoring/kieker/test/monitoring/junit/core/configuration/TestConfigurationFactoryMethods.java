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
import kieker.monitoring.core.controller.MonitoringController;
import kieker.test.monitoring.junit.util.DefaultConfigurationFactory;

import org.junit.Test;

/**
 * Tests whether the factory methods of {@link Configuration} return
 * instances and performs basic checks on these.
 * 
 * @author Andre van Hoorn, Jan Waller
 */
public class TestConfigurationFactoryMethods extends TestCase { // NOCS

	private static final String EXAMPLE_CONFIG_FILE_IN_TRUNK = "test/monitoring/META-INF/kieker.monitoring.properties.test";

	private void executeTestValues(final Configuration configuration) {
		Assert.assertNotNull("Configuration is null", configuration);
		// Monitoring controller
		Assert.assertNotNull(Configuration.MONITORING_ENABLED + " must not be empty", configuration.getProperty(Configuration.MONITORING_ENABLED)); // NOPMD
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
	@Test
	public void testCreationDefaultConfigurationWithDummyWriter() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		this.executeTestValues(configuration);
		Assert.assertEquals("Writer must be " + DefaultConfigurationFactory.WRITER_NAME, DefaultConfigurationFactory.WRITER_NAME,
				configuration.getStringProperty(Configuration.WRITER_CLASSNAME));
	}

	/**
	 * Tests {@link Configuration#createSingletonConfiguration()}.
	 */
	@Test
	public void testCreationSingletonConfiguration() { // NOPMD (asster in method)
		this.executeTestValues(Configuration.createSingletonConfiguration());
	}

	/**
	 * Tests {@link Configuration#createDefaultConfiguration()}.
	 */
	@Test
	public void testCreationDefaultConfiguration() {
		final Configuration configuration = Configuration.createDefaultConfiguration();
		this.executeTestValues(configuration);
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
	 * Test combinations of JVM-Params, Filenames, etc.
	 */
	@Test
	public void testCreationSingletonConfigurationVariants() {
		// check no config parameters are set
		Assert.assertNull(System.getProperty(Configuration.CUSTOM_PROPERTIES_LOCATION_JVM));
		Assert.assertNull(System.getProperty(Configuration.CONTROLLER_NAME));
		Assert.assertNull(MonitoringController.class.getClassLoader().getResourceAsStream(Configuration.CUSTOM_PROPERTIES_LOCATION_CLASSPATH));
		{ // NOCS (Block to check the default singleton configuration)
			final Configuration configuration = Configuration.createSingletonConfiguration();
			Assert.assertEquals("KIEKER-SINGLETON", configuration.getStringProperty(Configuration.CONTROLLER_NAME));
		}
		/*
		 * { // NOCS (adding properties file in default location)
		 * final String fn = "build/tests/" + Configuration.CUSTOM_PROPERTIES_LOCATION_CLASSPATH;
		 * try {
		 * final PrintWriter pw = new PrintWriter(new FileOutputStream(fn, false));
		 * pw.println(Configuration.CONTROLLER_NAME + "=KIEKER-SINGLETON-PROPERTIES-FILE-DEFAULT");
		 * pw.close();
		 * } catch (final FileNotFoundException e) {
		 * Assert.fail("Failed to create file " + fn);
		 * }
		 * final Configuration configuration = Configuration.createSingletonConfiguration();
		 * Assert.assertEquals("KIEKER-SINGLETON-PROPERTIES-FILE-DEFAULT", configuration.getStringProperty(Configuration.CONTROLLER_NAME));
		 * }
		 */
		{ // NOCS (adding properties file in custom location)
			System.setProperty(Configuration.CUSTOM_PROPERTIES_LOCATION_JVM, TestConfigurationFactoryMethods.EXAMPLE_CONFIG_FILE_IN_TRUNK);
			final Configuration configuration = Configuration.createSingletonConfiguration();
			Assert.assertEquals("KIEKER-TEST", configuration.getStringProperty(Configuration.CONTROLLER_NAME));
		}
		{ // NOCS (adding JVM property)
			final String ctrlName = "KIEKER-TEST-JVMPARAM";
			System.setProperty(Configuration.CONTROLLER_NAME, ctrlName);
			final Configuration configuration = Configuration.createSingletonConfiguration();
			Assert.assertEquals(ctrlName, configuration.getStringProperty(Configuration.CONTROLLER_NAME));
		}
		// clean up after us
		System.clearProperty(Configuration.CUSTOM_PROPERTIES_LOCATION_JVM);
		System.clearProperty(Configuration.CONTROLLER_NAME);
	}

	/**
	 * Tests {@link Configuration#createConfigurationFromFile(String)}.
	 */
	@Test
	public void testCreationfromFile() {
		final Configuration configuration = Configuration.createConfigurationFromFile(TestConfigurationFactoryMethods.EXAMPLE_CONFIG_FILE_IN_TRUNK);
		this.executeTestValues(configuration);
		Assert.assertEquals("KIEKER-TEST", configuration.getProperty(Configuration.CONTROLLER_NAME));
	}
}
