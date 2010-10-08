/**
 * 
 */
package kieker.test.monitoring.junit.core.configuration;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.monitoring.core.configuration.ConfigurationProperty;
import kieker.monitoring.core.configuration.IMonitoringConfiguration;
import kieker.monitoring.core.configuration.MonitoringConfiguration;
import kieker.monitoring.writer.DummyLogWriter;

/**
 * Tests whether the factory methods of {@link MonitoringConfiguration} return
 * instances and performs basic checks on these.
 * 
 * @author Andre van Hoorn
 * 
 */
public class TestConfigurationFactoryMethods extends TestCase {

	/**
	 * Tests
	 * {@link MonitoringConfiguration#createDefaultConfiguration(String, kieker.monitoring.writer.IMonitoringLogWriter)}
	 * .
	 */
	public void testCreationDefaultWithOwnWriter() {
		final DummyLogWriter writer = new DummyLogWriter();
		final IMonitoringConfiguration config = MonitoringConfiguration
				.createDefaultConfiguration("ConfigName", writer);
		Assert.assertNotNull("Config is null", config);
		Assert.assertNotNull("writer is null", config.getMonitoringLogWriter());
	}

	public void testCreationDefaultWithWriterByClassAndInitString() {
		final IMonitoringConfiguration config = MonitoringConfiguration
				.createDefaultConfiguration("ConfigName", DummyLogWriter.class,
						"");
		Assert.assertNotNull("Config is null", config);
		Assert.assertNotNull("writer is null", config.getMonitoringLogWriter());
		Assert.assertSame("Wrong writer class", DummyLogWriter.class, config
				.getMonitoringLogWriter().getClass());
	}

	/**
	 * Tests {@link MonitoringConfiguration#createSingletonConfiguration()}.
	 * 
	 * Creates a configuration based on the default configuration file
	 * {@link MonitoringConfiguration#KIEKER_CUSTOM_PROPERTIES_LOCATION_DEFAULT}
	 * contained in the Kieker library jar.
	 * 
	 */
	public void testCreationSingletonFallbackDefaultPropertiesFileInJar() {
		final IMonitoringConfiguration config = MonitoringConfiguration
				.createSingletonConfiguration();
		Assert.assertNotNull("Config is null", config);
		Assert.assertNotNull("writer is null", config.getMonitoringLogWriter());

		/* The following values must be the default values: */
		Assert.assertEquals("Wrong default value for debug property",
				ConfigurationProperty.DEBUG_ENABLED.getDefaultValue(),
				Boolean.toString(config.isDebugEnabled()));
		Assert.assertEquals(
				"Wrong default value for monitoringEnable property",
				ConfigurationProperty.MONITORING_ENABLED.getDefaultValue(),
				Boolean.toString(config.isMonitoringEnabled()));
	}

	private final static String EXAMPLE_CONFIG_FILE_IN_TRUNK = "META-INF/kieker.monitoring.properties";

	/**
	 * Tests {@link MonitoringConfiguration#createSingletonConfiguration()}.
	 * 
	 * Creates a configuration based on the example configuration file contained
	 * in the source and binary release: {@value #EXAMPLE_CONFIG_FILE_IN_TRUNK}.
	 * 
	 */
	public void testCreationCustomConfigFile() {
		IMonitoringConfiguration config;
		try {
			config = MonitoringConfiguration
					.createConfiguration(
							"ExampleConfig",
							TestConfigurationFactoryMethods.EXAMPLE_CONFIG_FILE_IN_TRUNK);
			Assert.assertNotNull("Config is null", config);
			Assert.assertNotNull("writer is null",
					config.getMonitoringLogWriter());

			/* The following values must be the default values: */
			Assert.assertEquals("Wrong default value for debug property",
					ConfigurationProperty.DEBUG_ENABLED.getDefaultValue(),
					Boolean.toString(config.isDebugEnabled()));
			Assert.assertEquals(
					"Wrong default value for monitoringEnable property",
					ConfigurationProperty.MONITORING_ENABLED.getDefaultValue(),
					Boolean.toString(config.isMonitoringEnabled()));
		} catch (final Exception e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Tests {@link MonitoringConfiguration#createSingletonConfiguration()}.
	 * 
	 * Overrides the monitoringEnabled and debugEnabled using JVM args. 
	 */
	public void testCreationSingletonJVMArgsOverrideMonitoringEnabledAndDebug() {
		final boolean monitoringEnabledInvertedDefault = !Boolean
				.parseBoolean(ConfigurationProperty.MONITORING_ENABLED
						.getDefaultValue());
		final boolean debugEnabledInvertedDefault = !Boolean
		.parseBoolean(ConfigurationProperty.DEBUG_ENABLED
				.getDefaultValue());

		System.setProperty(
				ConfigurationProperty.MONITORING_ENABLED.getJVMArgumentName(),
				Boolean.toString(monitoringEnabledInvertedDefault));
		System.setProperty(
				ConfigurationProperty.DEBUG_ENABLED.getJVMArgumentName(),
				Boolean.toString(debugEnabledInvertedDefault));
		
		final IMonitoringConfiguration config = MonitoringConfiguration
				.createSingletonConfiguration();
		Assert.assertNotNull("Config is null", config);
		Assert.assertNotNull("writer is null", config.getMonitoringLogWriter());

		/* Check whether the monitoring enabled and debug value are as desired: */
		Assert.assertEquals(
				"Wrong value for monitoringEnable property",
				monitoringEnabledInvertedDefault,
				config.isMonitoringEnabled());
		Assert.assertEquals(
				"Wrong value for debug property",
				debugEnabledInvertedDefault,
				config.isDebugEnabled());
	}
}
