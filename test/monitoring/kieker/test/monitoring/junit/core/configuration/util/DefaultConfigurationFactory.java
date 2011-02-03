package kieker.test.monitoring.junit.core.configuration.util;

import java.util.Properties;

import kieker.monitoring.core.configuration.ConfigurationProperties;
import kieker.monitoring.core.configuration.MonitoringConfiguration;
import kieker.monitoring.writer.DummyWriter;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 */
public class DefaultConfigurationFactory {
	public static final String defaultName = "jUnit-Default-Configuration";
	public static final String writerName = DummyWriter.class.getName();

	public static MonitoringConfiguration createDefaultConfigWithDummyWriter() {
		final Properties properties = ConfigurationProperties.getDefaultProperties();
		properties.setProperty(ConfigurationProperties.MONITORING_DATA_WRITER_CLASSNAME, writerName);
		properties.setProperty(writerName + ".jUnit", "true");
		return MonitoringConfiguration.createConfiguration(defaultName, properties);
	}
}
