package kieker.test.monitoring.junit.core.util;

import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.writer.DummyWriter;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 */
public class DefaultConfigurationFactory {
	public static final String writerName = DummyWriter.class.getName();

	public static Configuration createDefaultConfigurationWithDummyWriter() {
		final Configuration configuration = Configuration.createDefaultConfiguration();
		configuration.setProperty(Configuration.CONTROLLER_NAME, "jUnit");
		configuration.setProperty(Configuration.WRITER_CLASSNAME, writerName);
		configuration.setProperty(Configuration.PREFIX + "jUnit", "true");
		configuration.setProperty(writerName + ".jUnit", "true");
		return configuration;
	}
}
