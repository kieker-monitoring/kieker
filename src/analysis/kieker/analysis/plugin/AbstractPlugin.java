package kieker.analysis.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import kieker.analysis.configuration.Configuration;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.OutputPort;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

public abstract class AbstractPlugin {
	private static final Log LOG = LogFactory.getLog(AbstractPlugin.class);

	// TODO: should probably be ConcurrentHashMap ? also in many other cases?
	private final Map<String, AbstractInputPort> inputPorts = new HashMap<String, AbstractInputPort>();
	private final Map<String, OutputPort> outputPorts = new HashMap<String, OutputPort>();

	protected final Configuration configuration;

	/**
	 * Each Plugin requires a constructor with a single Configuration object!
	 */
	public AbstractPlugin(final Configuration configuration) {
		try {
			// somewhat dirty hack...
			final Properties defaultProps = this.getDefaultProperties(); // NOPMD
			if (defaultProps != null) {
				configuration.setDefaultProperties(defaultProps);
			}
		} catch (final IllegalAccessException ex) {
			AbstractPlugin.LOG.error("Unable to set plugin default properties");
		}
		this.configuration = configuration;
	}

	protected abstract Properties getDefaultProperties();

	public final AbstractInputPort getInputPort(final String name) {
		return this.inputPorts.get(name);
	}

	public final OutputPort getOutputPort(final String name) {
		return this.outputPorts.get(name);
	}

	protected void registerInputPort(final String name, final AbstractInputPort port) {
		this.inputPorts.put(name, port);
	}

	protected void registerOutputPort(final String name, final OutputPort port) {
		this.outputPorts.put(name, port);
	}

	public final AbstractInputPort[] getAllInputPorts() {
		return this.inputPorts.values().toArray(new AbstractInputPort[0]);
	}

	public final OutputPort[] getAllOutputPorts() {
		return this.outputPorts.values().toArray(new OutputPort[0]);
	}
}
