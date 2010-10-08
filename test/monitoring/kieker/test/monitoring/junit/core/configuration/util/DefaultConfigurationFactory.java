/**
 * 
 */
package kieker.test.monitoring.junit.core.configuration.util;

import kieker.monitoring.core.configuration.MonitoringConfiguration;
import kieker.monitoring.writer.DummyLogWriter;

/**
 * @author Andre van Hoorn
 * 
 */
public class DefaultConfigurationFactory {
	private static final String defaultName = "DEFAULT";

	public static MonitoringConfiguration createDefaultConfigWithDummyWriter(
			final String configName) {
		final DummyLogWriter dummyWriter = new DummyLogWriter();
		final MonitoringConfiguration config = MonitoringConfiguration
				.createDefaultConfiguration(configName, dummyWriter);
		return config;
	}

	public static MonitoringConfiguration createDefaultConfigWithDummyWriter() {
		return DefaultConfigurationFactory
				.createDefaultConfigWithDummyWriter(DefaultConfigurationFactory.defaultName);
	}
}
