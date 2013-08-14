package kieker.analysis.plugin.reader;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.common.configuration.Configuration;

@Plugin(outputPorts = {
	@OutputPort(
			name = KeepAnalysisAliveReader.OUTPUT_PORT_NAME_EVENTS,
			eventTypes = { Object.class }) },
		configuration =
		@Property(name = KeepAnalysisAliveReader.CONFIG_PROPERTY_NAME_KEEP_ALIVE_TIME_MS,
				defaultValue = KeepAnalysisAliveReader.CONFIG_PROPERTY_VALUE_KEEP_ALIVE_TIME_MS))
public class KeepAnalysisAliveReader extends AbstractReaderPlugin {

	public static final String OUTPUT_PORT_NAME_EVENTS = "outputPort";

	public static final String CONFIG_PROPERTY_NAME_KEEP_ALIVE_TIME_MS = "keepAliveTimeMS";
	public static final String CONFIG_PROPERTY_VALUE_KEEP_ALIVE_TIME_MS = "10000L";

	private final long keepAliveTimeMS;

	public KeepAnalysisAliveReader(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.keepAliveTimeMS = configuration.getLongProperty(CONFIG_PROPERTY_NAME_KEEP_ALIVE_TIME_MS);
	}

	public boolean read() {
		try {
			Thread.sleep(this.keepAliveTimeMS);
		} catch (final InterruptedException e) {
			return false;
		}
		return true;
	}

	public void terminate(final boolean error) {
		// No code necessary;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_KEEP_ALIVE_TIME_MS, Long.toString(this.keepAliveTimeMS));

		return configuration;
	}

}
