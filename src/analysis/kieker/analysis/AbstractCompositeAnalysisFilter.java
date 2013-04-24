/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis;

import java.lang.reflect.Constructor;

import kieker.analysis.analysisComponent.AbstractAnalysisComponent;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

/**
 * The {@link AbstractCompositeAnalysisFilter} is the abstract base for further composite filters within <i>Kieker</i>. Do not inherit directly from this class. Use
 * or extend rather {@link ConcurrentCompositeAnalysisFilter}, {@link DistributedCompositeAnalysisFilter} or {@link CompositeAnalysisFilter}.<br>
 * </br>
 * 
 * The handling of in- and outgoing data has to be performed by the concrete classes.<br>
 * </br>
 * 
 * This class is part of the Master's thesis "Development of a Concurrent and Distributed Pipes and Filters Analysis Framework for Kieker".
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.8
 */
// TODO: Check connections within AC
@Plugin(outputPorts = {
	@OutputPort(name = AbstractCompositeAnalysisFilter.OUTPUT_PORT_NAME_EVENTS, eventTypes = Object.class),
	@OutputPort(name = AbstractCompositeAnalysisFilter.INTERNAL_OUTPUT_PORT_NAME_EVENTS, eventTypes = Object.class) })
public abstract class AbstractCompositeAnalysisFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_EVENTS = "receivedEvents";
	public static final String OUTPUT_PORT_NAME_EVENTS = "sentEvents";

	protected static final String INTERNAL_INPUT_PORT_NAME_EVENTS = "internalInputPort";
	protected static final String INTERNAL_OUTPUT_PORT_NAME_EVENTS = "internalOutputPort";

	public AbstractCompositeAnalysisFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	/**
	 * This method creates a new instance of this given component, registers it with this composite filter and returns it for further actions.
	 * 
	 * @param componentClass
	 *            The class of the component to be created.
	 * @param configuration
	 *            The configuration for the component to create.
	 * 
	 * @return The newly created component.
	 * 
	 * @throws Exception
	 *             If something went wrong.
	 */
	@SuppressWarnings("unchecked")
	public <T extends AbstractAnalysisComponent> T createAndRegister(final Class<T> componentClass, final Configuration configuration) throws Exception {
		final Constructor<? extends AbstractAnalysisComponent> constructor = componentClass.getConstructor(Configuration.class, IProjectContext.class);
		final AbstractAnalysisComponent concreteComponent = constructor.newInstance(configuration, this.projectContext);

		concreteComponent.registerWithinComponent(this);

		return (T) concreteComponent;
	}

	@InputPort(name = INPUT_PORT_NAME_EVENTS, eventTypes = Object.class)
	public void inputPort(final Object data) {
		this.handleIncomingData(data);
	}

	@InputPort(name = INTERNAL_INPUT_PORT_NAME_EVENTS, eventTypes = Object.class)
	public void internalInputPort(final Object data) {
		this.handleOutgoingData(data);
	}

	/**
	 * Inheriting classes should implement this method to handle the incoming data. This can either mean that the incoming data is sent directly to the internal
	 * output port or that the incoming data is, for example, stored in an asynchronous queue.
	 * 
	 * @param data
	 *            The incoming data.
	 */
	protected abstract void handleIncomingData(final Object data);

	/**
	 * Inheriting classes should implement this method to handle the outgoing data. This can either mean that the outgoing data is sent directly to the external
	 * output port or that the outgoing data is, for example, sent via network to another system.
	 * 
	 * @param data
	 *            The outgoing data.
	 */
	protected abstract void handleOutgoingData(final Object data);

	public void connectWithOutput(final AbstractPlugin component, final String outputPortName) throws IllegalStateException, AnalysisConfigurationException {
		((AnalysisController) this.projectContext).connect(component, outputPortName, this, INTERNAL_INPUT_PORT_NAME_EVENTS);
	}

	public void connectWithInput(final AbstractPlugin component, final String inputPortName) throws IllegalStateException, AnalysisConfigurationException {
		((AnalysisController) this.projectContext).connect(this, INTERNAL_OUTPUT_PORT_NAME_EVENTS, component, inputPortName);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}
