package kieker.analysis.plugin;

import kieker.analysis.configuration.Configuration;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.OutputPort;

/**
 * This class should be used as a base for every plugin used within
 * <i>Kieker</i>. Every plugin should follow the following behavior:<br>
 * <ul>
 * <li>It should create and register its ports (input + output).
 * <li>The input ports should do whatever with the data and pass them to their output ports if necessary.
 * </ul>
 * 
 * @author Nils Christian Ehmke
 */
public abstract class AbstractAnalysisPlugin extends AbstractPlugin implements IAnalysisPlugin {

	public AbstractAnalysisPlugin(final Configuration configuration) {
		super(configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	final protected void registerInputPort(final String name, final AbstractInputPort port) {
		super.registerInputPort(name, port);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	final protected void registerOutputPort(final String name, final OutputPort port) {
		super.registerOutputPort(name, port);
	}
}
