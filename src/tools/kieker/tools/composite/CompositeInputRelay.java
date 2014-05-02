package kieker.tools.composite;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

/***
 * 
 * @author Markus Fischer, Thomas Düllmann, Tobias Rudolph,
 * 
 *         Simple implementation of a Filterplugin to relay incoming
 *         messages for the AbstractCompositeFilterPlugin
 */
@Plugin(name = "CompositeInputRelay",
		outputPorts = { @OutputPort(name = CompositeInputRelay.INPUTRELAY_OUTPUTPORT, eventTypes = { IMonitoringRecord.class }) })
public class CompositeInputRelay extends AbstractFilterPlugin {

	/**
	 * Name of the Outputport of the relay.
	 */
	public static final String INPUTRELAY_OUTPUTPORT = "input-relay-output";

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component. The component will be registered.
	 */
	public CompositeInputRelay(final Configuration configuration,
			final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	/**
	 * Relays a message to a given outputport.
	 * 
	 * @param outputPortName
	 *            Name of the given outputport
	 * @param object
	 *            message to be relayed
	 */
	public void relayMessage(final String outputPortName, final Object object) {
		super.deliver(outputPortName, object);
	}

	/**
	 * Relays a message to a this components Outputport.
	 * 
	 * @param object
	 *            message to be relayed
	 */
	public void relayMessage(final Object object) {
		super.deliver(INPUTRELAY_OUTPUTPORT, object);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return this.configuration;
	}

}
