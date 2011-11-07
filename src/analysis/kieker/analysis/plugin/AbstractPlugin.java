package kieker.analysis.plugin;

import java.util.HashMap;
import java.util.Map;

import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.OutputPort;

public abstract class AbstractPlugin {

	private final Map<String, AbstractInputPort> inputPorts = new HashMap<String, AbstractInputPort>();
	private final Map<String, OutputPort> outputPorts = new HashMap<String, OutputPort>();

	public AbstractPlugin() {

	}

	final public AbstractInputPort getInputPort(final String name) {
		return this.inputPorts.get(name);
	}

	final public OutputPort getOutputPort(final String name) {
		return this.outputPorts.get(name);
	}

	protected void registerInputPort(final String name,
			final AbstractInputPort port) {
		this.inputPorts.put(name, port);
	}

	protected void registerOutputPort(final String name,
			final OutputPort port) {
		this.outputPorts.put(name, port);
	}
}
