package kieker.analysis.reader;

import kieker.analysis.configuration.Configuration;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.OutputPort;

// TODO rename to AbstractReaderPlugin ?
/**
 * This class should be used as a base for every reader used within <i>Kieker</i>. Every reader should follow the following behaviour:<br>
 * <ul>
 * <li>It should create and register its ports (only output).
 * <li>The readed data should be send to the output ports.
 * </ul>
 * 
 * @author Nils Christian Ehmke
 */
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
