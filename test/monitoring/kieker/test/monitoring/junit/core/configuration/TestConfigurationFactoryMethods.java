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
		Assert.assertNotNull(Configuration.CONTROLLER_NAME + " must not be empty", configuration.getProperty(Configuration.CONTROLLER_NAME));
		// HostName may be empty!
		// Assert.assertNotNull(Configuration.HOST_NAME + " must not be empty",
		// configuration.getProperty(Configuration.HOST_NAME));
		Assert.assertNotNull(Configuration.EXPERIMENT_ID + " must not be empty", configuration.getProperty(Configuration.EXPERIMENT_ID));
		Assert.assertNotNull(Configuration.AUTO_SET_LOGGINGTSTAMP + " must not be empty", configuration.getProperty(Configuration.AUTO_SET_LOGGINGTSTAMP));
		Assert.assertNotNull(Configuration.MONITORING_ENABLED + " must not be empty", configuration.getProperty(Configuration.MONITORING_ENABLED));
		Assert.assertNotNull(Configuration.WRITER_CLASSNAME + " must not be empty", configuration.getProperty(Configuration.WRITER_CLASSNAME));
		Assert.assertNotNull(Configuration.PERIODIC_SENSORS_EXECUTOR_POOL_SIZE + " must not be empty",
				configuration.getProperty(Configuration.PERIODIC_SENSORS_EXECUTOR_POOL_SIZE));
		// TODO: add other enforced values!
	}

	/**
	 * Tests {@link Configuration#createSingletonConfiguration()}.
	 */
	public void testCreationDefaultConfigurationWithDummyWriter() {
		final Configuration configuration = DefaultConfigurationFactory.createDefaultConfigurationWithDummyWriter();
		this.testValues(configuration);
		Assert.assertEquals("Writer must be " + DefaultConfigurationFactory.writerName, DefaultConfigurationFactory.writerName,
				configuration.getStringProperty(Configuration.WRITER_CLASSNAME));
	}

	/**
	 * Tests {@link Configuration#createSingletonConfiguration()}.
	 */
	public void testCreationSingletonConfiguration() {
		this.testValues(Configuration.createSingletonConfiguration());
	}

	/**
	 * Tests {@link Configuration#createDefaultConfiguration()}.
	 */
	public void testCreationDefaultConfiguration() {
		this.testValues(Configuration.createDefaultConfiguration());
	}

	/**
	 * Tests {@link Configuration#createSingletonConfiguration()}.
	 * 
	 * FIXME: "Missing test: Should test combinations of JVM-Params, Filenames, etc. How to do that?
	 */
	public void testCreationSingletonConfigurationVariants() {
	}

	/**
	 * Tests {@link Configuration#createConfigurationFromFile(String)}.
	 * 
	 * FIXME: "File should be included correctly and it should be tested if the correct values are set"
	 */
	public void testCreationfromFile() {
		final String EXAMPLE_CONFIG_FILE_IN_TRUNK = "META-INF/kieker.monitoring.properties.test";
		this.testValues(Configuration.createConfigurationFromFile(EXAMPLE_CONFIG_FILE_IN_TRUNK));
	}
}
