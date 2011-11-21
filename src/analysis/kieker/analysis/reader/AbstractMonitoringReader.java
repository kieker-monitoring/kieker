package kieker.analysis.reader;

import kieker.analysis.configuration.Configuration;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.OutputPort;

// TODO rename to AbstractReaderPlugin ?
/**
 * This class should be used as a base for every reader used within
 * <i>Kieker</i>. Every reader should follow the following behaviour:<br>
 * <ul>
 * <li>It should create and register its ports (only output).
 * <li>The readed data should be send to the output ports.
 * </ul>
 * 
 * @author Nils Christian Ehmke
 */
public abstract class AbstractMonitoringReader extends AbstractPlugin implements IMonitoringReader {

	/**
	 * Each Plugin requires a constructor with a single Configuration object.
	 * 
	 * @param configuration
	 *            The configuration which should be used to initialize the
	 *            object.
	 */
	public AbstractMonitoringReader(final Configuration configuration) {
		super(configuration);
	}

	/**
	 * As classes extending the <i>AbstractMonitoringReader</i> cannot have
	 * input ports, this method doesn't register the given port.
	 * 
	 * @param name
	 *            The name for the port. It is not used.
	 * @param port
	 *            The port to be registered. It is not used.
	 */
	@Override
	final protected void registerInputPort(final String name, final AbstractInputPort port) {
		return;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	final protected void registerOutputPort(final String name, final OutputPort port) {
		super.registerOutputPort(name, port);
	}

}
