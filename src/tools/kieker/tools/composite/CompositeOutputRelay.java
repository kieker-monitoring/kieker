/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.tools.composite;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

/**
 * 
 * @author Markus Fischer
 * @since 1.10
 * 
 *        Simple implementation of a Filterplugin to relay outgoing
 *        messages for the AbstractCompositeFilterPlugin
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
