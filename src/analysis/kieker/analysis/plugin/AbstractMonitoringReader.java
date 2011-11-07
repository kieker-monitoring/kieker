package kieker.analysis.plugin;

import kieker.analysis.plugin.configuration.IInputPort;
import kieker.analysis.plugin.configuration.IOutputPort;
import kieker.analysis.reader.IMonitoringReader;

public abstract class AbstractMonitoringReader extends AbstractPlugin
		implements IMonitoringReader {

	@Override
	final protected void registerInputPort(final String name, final IInputPort port) {
		return;
	}

	@Override
	final protected void registerOutputPort(final String name, final IOutputPort port) {
		super.registerOutputPort(name, port);
	}
}
