/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

import java.io.File;
import java.net.URISyntaxException;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.timer.SystemNanoTimer;
import kieker.monitoring.writer.filesystem.AsciiFileWriter;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.monitoring.util.DefaultConfigurationFactory;

/**
 * Tests whether the factory methods of {@link Configuration} return
 * instances and performs basic checks on these.
 *
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.3
 */
public class TestConfigurationFactoryMethods extends AbstractKiekerTest {

	private static final String EXAMPLE_CONFIG_FILE_IN_TRUNK;

	static {
		String tempPath;
		try {
			tempPath = new File(TestConfigurationFactoryMethods.class.getResource("/META-INF/kieker.monitoring.test.properties").toURI()).getAbsolutePath();
		} catch (final URISyntaxException e) {
			tempPath = null; // NOPMD (null assignment could lead to an exception - but this is a unit test)
		}

		EXAMPLE_CONFIG_FILE_IN_TRUNK = tempPath;
	}

	/**
	 * Default constructor.
	 */
	public TestConfigurationFactoryMethods() {
		// empty default constructor
	}

	@SuppressWarnings("deprecation")
	private void executeTestValues(final Configuration configuration) {
		Assert.assertNotNull("Configuration is null", configuration);
		// Monitoring controller
		Assert.assertNotNull(ConfigurationFactory.MONITORING_ENABLED + " must not be empty", configuration.getProperty(ConfigurationFactory.MONITORING_ENABLED));
		Assert.assertNotNull(ConfigurationFactory.CONTROLLER_NAME + " must not be empty", configuration.getProperty(ConfigurationFactory.CONTROLLER_NAME));
		// HostName may be empty!
		Assert.assertNotNull(ConfigurationFactory.EXPERIMENT_ID + " must not be empty", configuration.getProperty(ConfigurationFactory.EXPERIMENT_ID));
		Assert.assertNotNull(ConfigurationFactory.USE_SHUTDOWN_HOOK + " must not be empty", configuration.getProperty(ConfigurationFactory.USE_SHUTDOWN_HOOK));
		Assert.assertNotNull(ConfigurationFactory.DEBUG + " must not be empty", configuration.getProperty(ConfigurationFactory.DEBUG));
		// JMX controller
		Assert.assertNotNull(ConfigurationFactory.ACTIVATE_JMX + " must not be empty", configuration.getProperty(ConfigurationFactory.ACTIVATE_JMX));
		// Writer controller
		Assert.assertNotNull(ConfigurationFactory.AUTO_SET_LOGGINGTSTAMP + " must not be empty",
				configuration.getProperty(ConfigurationFactory.AUTO_SET_LOGGINGTSTAMP));
		Assert.assertNotNull(ConfigurationFactory.METADATA + " must not be empty",
				configuration.getProperty(ConfigurationFactory.METADATA));
		Assert.assertNotNull(ConfigurationFactory.WRITER_CLASSNAME + " must not be empty", configuration.getProperty(ConfigurationFactory.WRITER_CLASSNAME));
		// TimeSource controller
		Assert.assertNotNull(ConfigurationFactory.TIMER_CLASSNAME + " must not be empty", configuration.getProperty(ConfigurationFactory.TIMER_CLASSNAME));
		// Sampling controller
		Assert.assertNotNull(ConfigurationFactory.PERIODIC_SENSORS_EXECUTOR_POOL_SIZE + " must not be empty",
				configuration.getProperty(ConfigurationFactory.PERIODIC_SENSORS_EXECUTOR_POOL_SIZE));
		// Probe controller
		Assert.assertNotNull(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED + " must not be empty",
				configuration.getProperty(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED));
		Assert.assertNotNull(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE + " must not be empty",
				configuration.getProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE));
		Assert.assertNotNull(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE_UPDATE + " must not be empty",
				configuration.getProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE_UPDATE));
		Assert.assertNotNull(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE_READ_INTERVALL + " must not be empty",
				configuration.getProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE_READ_INTERVALL));
	}

	/**
	 * Tests {@link Configuration#createSingletonConfiguration()}.
	 */
	@Test
	public void testCreationDefaultConfigurationWithDummyWriter() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		this.executeTestValues(configuration);
		Assert.assertEquals("Writer must be " + DefaultConfigurationFactory.WRITER_NAME, DefaultConfigurationFactory.WRITER_NAME,
				configuration.getStringProperty(ConfigurationFactory.WRITER_CLASSNAME));
	}

	/**
	 * Tests {@link Configuration#createSingletonConfiguration()}.
	 */
	@Test
	public void testCreationSingletonConfiguration() { // NOPMD (asster in method)
		this.executeTestValues(ConfigurationFactory.createSingletonConfiguration());
	}

	/**
	 * Tests {@link Configuration#createDefaultConfiguration()}.
	 */
	@Test
	public void testCreationDefaultConfiguration() {
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		this.executeTestValues(configuration);
		// check for correct default values of required parameters
		// Monitoring controller
		Assert.assertEquals(true, configuration.getBooleanProperty(ConfigurationFactory.MONITORING_ENABLED));
		Assert.assertEquals("KIEKER", configuration.getStringProperty(ConfigurationFactory.CONTROLLER_NAME));
		Assert.assertEquals("", configuration.getStringProperty(ConfigurationFactory.HOST_NAME));
		Assert.assertEquals(1, configuration.getIntProperty(ConfigurationFactory.EXPERIMENT_ID));
		// JMX controller
		Assert.assertEquals(false, configuration.getBooleanProperty(ConfigurationFactory.ACTIVATE_JMX));
		// Writer controller
		Assert.assertEquals(true, configuration.getBooleanProperty(ConfigurationFactory.METADATA));
		Assert.assertEquals(true, configuration.getBooleanProperty(ConfigurationFactory.AUTO_SET_LOGGINGTSTAMP));
		Assert.assertThat(configuration.getStringProperty(ConfigurationFactory.WRITER_CLASSNAME), CoreMatchers.is(AsciiFileWriter.class.getName()));
		// TimeSource controller
		Assert.assertThat(configuration.getStringProperty(ConfigurationFactory.TIMER_CLASSNAME), CoreMatchers.is(SystemNanoTimer.class.getName()));
		// Sampling controller
		Assert.assertEquals(1, configuration.getIntProperty(ConfigurationFactory.PERIODIC_SENSORS_EXECUTOR_POOL_SIZE));
		// Probe controller
		Assert.assertEquals(false, configuration.getBooleanProperty(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED));
		Assert.assertEquals("META-INF/kieker.monitoring.adaptiveMonitoring.conf",
				configuration.getPathProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE));
		Assert.assertEquals(false, configuration.getBooleanProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE_UPDATE));
		Assert.assertEquals(30, configuration.getIntProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE_READ_INTERVALL));
	}

	/**
	 * Tests {@link Configuration#createSingletonConfiguration()}.
	 *
	 * Test combinations of JVM-Params, Filenames, etc.
	 */
	@Test
	public void testCreationSingletonConfigurationVariants() {
		// check no config parameters are set
		Assert.assertNull(System.getProperty(ConfigurationFactory.CUSTOM_PROPERTIES_LOCATION_JVM));
		Assert.assertNull(System.getProperty(ConfigurationFactory.CONTROLLER_NAME));
		Assert.assertNull(MonitoringController.class.getClassLoader().getResourceAsStream(ConfigurationFactory.CUSTOM_PROPERTIES_LOCATION_CLASSPATH));
		{ // NOCS (Block to check the default singleton configuration)
			final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
			Assert.assertEquals("KIEKER-SINGLETON", configuration.getStringProperty(ConfigurationFactory.CONTROLLER_NAME));
		}

		// { // NOCS (adding properties file in default location)
		// final String fn = "build/tests/" + Configuration.CUSTOM_PROPERTIES_LOCATION_CLASSPATH;
		// final PrintWriter pw = new PrintWriter(new FileOutputStream(fn, false));
		// pw.println(Configuration.CONTROLLER_NAME + "=KIEKER-SINGLETON-PROPERTIES-FILE-DEFAULT");
		// pw.close();
		// final Configuration configuration = Configuration.createSingletonConfiguration();
		// Assert.assertEquals("KIEKER-SINGLETON-PROPERTIES-FILE-DEFAULT", configuration.getStringProperty(Configuration.CONTROLLER_NAME));
		// }

		{ // NOCS (adding properties file in custom location)
			System.setProperty(ConfigurationFactory.CUSTOM_PROPERTIES_LOCATION_JVM, TestConfigurationFactoryMethods.EXAMPLE_CONFIG_FILE_IN_TRUNK);
			final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
			Assert.assertEquals("KIEKER-TEST", configuration.getStringProperty(ConfigurationFactory.CONTROLLER_NAME));
		}
		{ // NOCS (adding JVM property)
			final String ctrlName = "KIEKER-TEST-JVMPARAM";
			System.setProperty(ConfigurationFactory.CONTROLLER_NAME, ctrlName);
			final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
			Assert.assertEquals(ctrlName, configuration.getStringProperty(ConfigurationFactory.CONTROLLER_NAME));
		}
		// clean up after us
		System.clearProperty(ConfigurationFactory.CUSTOM_PROPERTIES_LOCATION_JVM);
		System.clearProperty(ConfigurationFactory.CONTROLLER_NAME);
	}

	/**
	 * Tests {@link Configuration#createConfigurationFromFile(String)}.
	 */
	@Test
	public void testCreationfromFile() {
		final Configuration configuration = ConfigurationFactory.createConfigurationFromFile(TestConfigurationFactoryMethods.EXAMPLE_CONFIG_FILE_IN_TRUNK);
		this.executeTestValues(configuration);
		Assert.assertEquals("KIEKER-TEST", configuration.getStringProperty(ConfigurationFactory.CONTROLLER_NAME));
	}
}
