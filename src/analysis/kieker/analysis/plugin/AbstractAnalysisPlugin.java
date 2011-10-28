package kieker.analysis.plugin;

import kieker.analysis.plugin.configuration.IInputPort;
import kieker.analysis.plugin.configuration.IOutputPort;

public abstract class AbstractAnalysisPlugin extends AbstractPlugin
		implements IAnalysisPlugin {

	@Override
	final protected void registerInputPort(String name, IInputPort port) {
		super.registerInputPort(name, port);
	}

	@Override
	final protected void registerOutputPort(String name, IOutputPort port) {
		super.registerOutputPort(name, port);
	}
}
