package kieker.analysis.plugin;

import java.util.HashMap;
import java.util.Map;

import kieker.analysis.plugin.configuration.IInputPort;
import kieker.analysis.plugin.configuration.IOutputPort;

public abstract class AbstractPlugin {

	private final Map<String, IInputPort> inputPorts = new HashMap<String, IInputPort>();
	private final Map<String, IOutputPort> outputPorts = new HashMap<String, IOutputPort>();

	public AbstractPlugin() {

	}

	final public IInputPort getInputPort(final String name) {
		return this.inputPorts.get(name);
	}

	final public IOutputPort getOutputPort(final String name) {
		return this.outputPorts.get(name);
	}

	protected void registerInputPort(final String name,
			final IInputPort port) {
		this.inputPorts.put(name, port);
	}

	protected void registerOutputPort(final String name,
			final IOutputPort port) {
		this.outputPorts.put(name, port);
	}
}
