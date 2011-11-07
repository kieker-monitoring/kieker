package kieker.analysis.plugin;

import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.OutputPort;
import kieker.analysis.reader.IMonitoringReader;

public abstract class AbstractMonitoringReader extends AbstractPlugin
		implements IMonitoringReader {

	@Override
	final protected void registerInputPort(final String name, final AbstractInputPort port) {
		return;
	}

	@Override
	final protected void registerOutputPort(final String name, final OutputPort port) {
		super.registerOutputPort(name, port);
	}
}
