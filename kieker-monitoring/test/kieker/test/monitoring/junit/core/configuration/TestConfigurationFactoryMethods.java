/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
import java.net.URL;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationConstants;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.timer.SystemNanoTimer;
import kieker.monitoring.writer.filesystem.FileWriter;

import kieker.test.monitoring.util.DummyWriterConfigurationFactory;

/**
 * Tests whether the factory methods of {@link Configuration} return instances and performs basic checks on these.
 *
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.3
 */
public class TestConfigurationFactoryMethods extends kieker.test.common.junit.AbstractKiekerTest {

	private static final String EXAMPLE_CONFIG_FILE_IN_TRUNK;
	private static final Class<TestConfigurationFactoryMethods> THIS_CLASS = TestConfigurationFactoryMethods.class;

	static {
		String tempPath;
		try {
			final URL fileResource = THIS_CLASS.getResource("/META-INF/kieker.monitoring.test.properties");
			tempPath = new File(fileResource.toURI()).getAbsolutePath();
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
		Assert.assertNotNull(ConfigurationConstants.MONITORING_ENABLED + " must not be empty",
				configuration.getProperty(ConfigurationConstants.MONITORING_ENABLED));
		Assert.assertNotNull(ConfigurationConstants.CONTROLLER_NAME + " must not be empty",
				configuration.getProperty(ConfigurationConstants.CONTROLLER_NAME));
		// HostName may be empty!
		Assert.assertNotNull(ConfigurationConstants.EXPERIMENT_ID + " must not be empty",
				configuration.getProperty(ConfigurationConstants.EXPERIMENT_ID));
		Assert.assertNotNull(ConfigurationConstants.USE_SHUTDOWN_HOOK + " must not be empty",
				configuration.getProperty(ConfigurationConstants.USE_SHUTDOWN_HOOK));
		Assert.assertNotNull(ConfigurationConstants.DEBUG + " must not be empty",
				configuration.getProperty(ConfigurationConstants.DEBUG));
		// JMX controller
		Assert.assertNotNull(ConfigurationConstants.ACTIVATE_JMX + " must not be empty",
				configuration.getProperty(ConfigurationConstants.ACTIVATE_JMX));
		// Writer controller
		Assert.assertNotNull(ConfigurationConstants.AUTO_SET_LOGGINGTSTAMP + " must not be empty",
				configuration.getProperty(ConfigurationConstants.AUTO_SET_LOGGINGTSTAMP));
		Assert.assertNotNull(ConfigurationConstants.META_DATA + " must not be empty",
				configuration.getProperty(ConfigurationConstants.META_DATA));
		Assert.assertNotNull(ConfigurationConstants.WRITER_CLASSNAME + " must not be empty",
				configuration.getProperty(ConfigurationConstants.WRITER_CLASSNAME));
		// TimeSource controller
		Assert.assertNotNull(ConfigurationConstants.TIMER_CLASSNAME + " must not be empty",
				configuration.getProperty(ConfigurationConstants.TIMER_CLASSNAME));
		// Sampling controller
		Assert.assertNotNull(ConfigurationConstants.PERIODIC_SENSORS_EXECUTOR_POOL_SIZE + " must not be empty",
				configuration.getProperty(ConfigurationConstants.PERIODIC_SENSORS_EXECUTOR_POOL_SIZE));
		// Probe controller
		Assert.assertNotNull(ConfigurationConstants.ADAPTIVE_MONITORING_ENABLED + " must not be empty",
				configuration.getProperty(ConfigurationConstants.ADAPTIVE_MONITORING_ENABLED));
		Assert.assertNotNull(ConfigurationConstants.ADAPTIVE_MONITORING_CONFIG_FILE + " must not be empty",
				configuration.getProperty(ConfigurationConstants.ADAPTIVE_MONITORING_CONFIG_FILE));
		Assert.assertNotNull(ConfigurationConstants.ADAPTIVE_MONITORING_CONFIG_FILE_UPDATE + " must not be empty",
				configuration.getProperty(ConfigurationConstants.ADAPTIVE_MONITORING_CONFIG_FILE_UPDATE));
		Assert.assertNotNull(ConfigurationConstants.ADAPTIVE_MONITORING_CONFIG_FILE_READ_INTERVALL + " must not be empty",
				configuration.getProperty(ConfigurationConstants.ADAPTIVE_MONITORING_CONFIG_FILE_READ_INTERVALL));
	}

	/**
	 * Tests {@link Configuration#createSingletonConfiguration()}.
	 */
	@Test
	public void testCreationDefaultConfigurationWithDummyWriter() {
		final Configuration configuration = DummyWriterConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		this.executeTestValues(configuration);
		Assert.assertEquals("Writer must be " + DummyWriterConfigurationFactory.WRITER_NAME,
				DummyWriterConfigurationFactory.WRITER_NAME,
				configuration.getStringProperty(ConfigurationConstants.WRITER_CLASSNAME));
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
		Assert.assertEquals(true, configuration.getBooleanProperty(ConfigurationConstants.MONITORING_ENABLED));
		Assert.assertEquals("KIEKER", configuration.getStringProperty(ConfigurationConstants.CONTROLLER_NAME));
		Assert.assertEquals("", configuration.getStringProperty(ConfigurationConstants.HOST_NAME));
		Assert.assertEquals(1, configuration.getIntProperty(ConfigurationConstants.EXPERIMENT_ID));
		// JMX controller
		Assert.assertEquals(false, configuration.getBooleanProperty(ConfigurationConstants.ACTIVATE_JMX));
		// Writer controller
		Assert.assertEquals(true, configuration.getBooleanProperty(ConfigurationConstants.META_DATA));
		Assert.assertEquals(true, configuration.getBooleanProperty(ConfigurationConstants.AUTO_SET_LOGGINGTSTAMP));
		Assert.assertThat(configuration.getStringProperty(ConfigurationConstants.WRITER_CLASSNAME),
				CoreMatchers.is(FileWriter.class.getName()));
		// TimeSource controller
		Assert.assertThat(configuration.getStringProperty(ConfigurationConstants.TIMER_CLASSNAME),
				CoreMatchers.is(SystemNanoTimer.class.getName()));
		// Sampling controller
		Assert.assertEquals(1, configuration.getIntProperty(ConfigurationConstants.PERIODIC_SENSORS_EXECUTOR_POOL_SIZE));
		// Probe controller
		Assert.assertEquals(false, configuration.getBooleanProperty(ConfigurationConstants.ADAPTIVE_MONITORING_ENABLED));
		Assert.assertEquals("META-INF/kieker.monitoring.adaptiveMonitoring.conf",
				configuration.getPathProperty(ConfigurationConstants.ADAPTIVE_MONITORING_CONFIG_FILE));
		Assert.assertEquals(false,
				configuration.getBooleanProperty(ConfigurationConstants.ADAPTIVE_MONITORING_CONFIG_FILE_UPDATE));
		Assert.assertEquals(30,
				configuration.getIntProperty(ConfigurationConstants.ADAPTIVE_MONITORING_CONFIG_FILE_READ_INTERVALL));
	}

	/**
	 * Tests {@link Configuration#createSingletonConfiguration()}.
	 *
	 * Test combinations of JVM-Params, Filenames, etc.
	 */
	@Test
	public void testCreationSingletonConfigurationVariants() {
		// check no config parameters are set
		Assert.assertNull(System.getProperty(ConfigurationConstants.CUSTOM_PROPERTIES_LOCATION_JVM));
		Assert.assertNull(System.getProperty(ConfigurationConstants.CONTROLLER_NAME));
		Assert.assertNull(THIS_CLASS.getResource("/" + ConfigurationConstants.CUSTOM_PROPERTIES_LOCATION_CLASSPATH));
		{ // NOCS (Block to check the default singleton configuration)
			final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
			Assert.assertEquals("KIEKER-SINGLETON",
					configuration.getStringProperty(ConfigurationConstants.CONTROLLER_NAME));
		}

		// { // NOCS (adding properties file in default location)
		// final String fn = "build/tests/" + Configuration.CUSTOM_PROPERTIES_LOCATION_CLASSPATH;
		// final PrintWriter pw = new PrintWriter(new FileOutputStream(fn, false));
		// pw.println(Configuration.CONTROLLER_NAME + "=KIEKER-SINGLETON-PROPERTIES-FILE-DEFAULT");
		// pw.close();
		// final Configuration configuration = Configuration.createSingletonConfiguration();
		// Assert.assertEquals("KIEKER-SINGLETON-PROPERTIES-FILE-DEFAULT",
		// configuration.getStringProperty(Configuration.CONTROLLER_NAME));
		// }

		{ // NOCS (adding properties file in custom location)
			System.setProperty(ConfigurationConstants.CUSTOM_PROPERTIES_LOCATION_JVM,
					TestConfigurationFactoryMethods.EXAMPLE_CONFIG_FILE_IN_TRUNK);
			final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
			Assert.assertEquals("KIEKER-TEST", configuration.getStringProperty(ConfigurationConstants.CONTROLLER_NAME));
		}
		{ // NOCS (adding JVM property)
			final String ctrlName = "KIEKER-TEST-JVMPARAM";
			System.setProperty(ConfigurationConstants.CONTROLLER_NAME, ctrlName);
			final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
			Assert.assertEquals(ctrlName, configuration.getStringProperty(ConfigurationConstants.CONTROLLER_NAME));
		}
		// clean up after us
		System.clearProperty(ConfigurationConstants.CUSTOM_PROPERTIES_LOCATION_JVM);
		System.clearProperty(ConfigurationConstants.CONTROLLER_NAME);
	}

	/**
	 * Tests {@link Configuration#createConfigurationFromFile(String)}.
	 */
	@Test
	public void testCreationfromFile() {
		final Configuration configuration = ConfigurationFactory
				.createConfigurationFromFile(TestConfigurationFactoryMethods.EXAMPLE_CONFIG_FILE_IN_TRUNK);
		this.executeTestValues(configuration);
		Assert.assertEquals("KIEKER-TEST", configuration.getStringProperty(ConfigurationConstants.CONTROLLER_NAME));
	}
}
