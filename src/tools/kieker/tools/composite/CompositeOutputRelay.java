package kieker.tools.composite;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.tools.composite.AbstractCompositeFilterPlugin.PortWrapper;

/**
 * 
 * @author Markus Fischer
 * 
 *         Simple implementation of a Filterplugin to relay outgoing
 *         messages for the AbstractCompositeFilterPlugin
 */
@Plugin(description = "A filter relay Messages to the compositeOutput")
public class CompositeOutputRelay extends AbstractFilterPlugin {

	/**
	 * Name of this components Inputport.
	 */
	public static final String INPUT_PORT_NAME_EVENTS = "composite-output-relay";

	private final AbstractCompositeFilterPlugin outerCompositeFilter;

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component. The component will be registered.
	 * 
	 * @param outerCompositeFilter
	 *            {@link AbstractCompositeFilterPlugin} that contains this OutputRelay
	 */
	public CompositeOutputRelay(final Configuration configuration,
			final IProjectContext projectContext, final AbstractCompositeFilterPlugin outerCompositeFilter) {

		super(configuration, projectContext);
		this.outerCompositeFilter = outerCompositeFilter;
	}

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component. The component will be registered.
	 * 
	 */
	public CompositeOutputRelay(final Configuration configuration,
			final IProjectContext projectContext) {
		super(configuration, projectContext);
		this.outerCompositeFilter = null; // NOPMD, reason: private constructor only for TestPluginConfigurationRetention.java
	}

	/**
	 * Receives Messages.
	 * 
	 * @param object
	 *            inputMessage
	 */
	@InputPort(name = INPUT_PORT_NAME_EVENTS, description = "Receives incoming objects to be logged and forwarded", eventTypes = { Object.class })
	public final void inputEvent(final Object object) {
		for (final PortWrapper pw : this.outerCompositeFilter.getOutputPorts(object)) {
			this.outerCompositeFilter.send(pw.getOutputPortName(), pw.getEventClass().cast(object));
		}
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return null;
	}

}
