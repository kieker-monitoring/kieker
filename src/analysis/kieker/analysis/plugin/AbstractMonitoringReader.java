package kieker.analysis.plugin;

import kieker.analysis.configuration.Configuration;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.OutputPort;
import kieker.analysis.reader.IMonitoringReader;

// TODO rename to AbstractReaderPlugin ?
public abstract class AbstractMonitoringReader extends AbstractPlugin implements IMonitoringReader {

	public AbstractMonitoringReader(final Configuration configuration) {
		super(configuration);
	}

	@Override
	final protected void registerInputPort(final String name, final AbstractInputPort port) {
		return;
	}

	@Override
	final protected void registerOutputPort(final String name, final OutputPort port) {
		super.registerOutputPort(name, port);
	}
}
