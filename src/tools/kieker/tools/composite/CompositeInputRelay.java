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
