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

package kieker.analysis.plugin.filter.composite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import kieker.analysis.IAnalysisController;
import kieker.analysis.IProjectContext;
import kieker.analysis.configuration.GlobalConfigurationRegistry;
import kieker.analysis.configuration.IConfigurationRegistry;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

/**
 * Abstract class that makes it possible to encapsulate filters in a composite plugin.
 *
 * @author Markus Fischer, Thomas Düllmann
 * @since 1.10
 */
public abstract class AbstractCompositeFilterPlugin extends AbstractFilterPlugin {

	/**
	 * Controller where the plugins are attached to.
	 */
	protected final IAnalysisController controller;

	/**
	 * Relay that maps incoming ports to the first input port within the composite plugin.
	 */
	protected final CompositeInputRelay inputRelay;

	/**
	 * Relay that maps outgoing ports of inner filters to the output port of the composite plugin.
	 */
	protected final CompositeOutputRelay outputRelay;

	/**
	 * Configuration registry that maps properties of the composite plugin to the properties of the inner plugins.
	 */
	protected final IConfigurationRegistry configRegistry = GlobalConfigurationRegistry.getInstance();

	/**
	 * Constructor.
	 *
	 * @param configuration
	 *            Configuration of the composite plugin
	 * @param projectContext
	 *            context of the composite plugin
	 */
	public AbstractCompositeFilterPlugin(final Configuration configuration,
			final IProjectContext projectContext) {
		super(configuration, projectContext);
		this.controller = (IAnalysisController) projectContext;

		final Configuration compositeInputConfiguration = new Configuration();
		this.inputRelay = new CompositeInputRelay(compositeInputConfiguration, this.controller);

		final Configuration compositeOutputConfiguration = new Configuration();
		this.outputRelay = new CompositeOutputRelay(compositeOutputConfiguration, this.controller, this);
	}

	/**
	 * Returns a List of PortWrapper containing all ports suitable for the method input to broadcast.
	 *
	 * @param object
	 *            for which the returned OutputPorts need to be suited
	 * @return List of PortWrappers containing the EventClass and the Portname
	 */
	public List<PortWrapper> getOutputPorts(final Object object) {
		final List<PortWrapper> out = new ArrayList<PortWrapper>();

		// find all ports within the implementing class that have the same eventtype as the inputobject
		for (final OutputPort op : this.getClass().getAnnotation(Plugin.class).outputPorts()) {
			for (final Class<?> clazz : op.eventTypes()) {
				final boolean eventClassIsValid = clazz.isAssignableFrom(object.getClass());
				if (eventClassIsValid) {
					out.add(new PortWrapper(op.name(), clazz));
				}
			}
		}
		return out;
	}

	/**
	 * Method to broadcast an Object to the given outputPort.
	 *
	 * @param outputPortName
	 *            The name of the OutputPort to be broadcasted to
	 * @param data
	 *            The object to be sent
	 * @return true if and only if the given output port does exist and if the data is not null and if it suits the port's event types.
	 */
	public boolean send(final String outputPortName, final Object data) {
		return this.deliver(outputPortName, data);
	}

	/**
	 * Update the current configuration of a FilterPlugin. The given configurations properties will be updated if
	 * suitable properties were given to the Configuration of the implementing class. The properties have to be named
	 * "simpleClassName.propertyName".
	 *
	 * @param config
	 *            The Configuration that is updated
	 * @param clazz
	 *            The class of the corresponding FilterPlugin
	 * @return an Updated Configuration
	 */
	public Configuration updateConfiguration(final Configuration config, final Class<? extends AbstractFilterPlugin> clazz) {
		for (final Entry<Object, Object> entry : this.configuration.getPropertiesStartingWith(clazz.getSimpleName()).entrySet()) {
			final String propertyName = (String) entry.getKey();
			final String propertyValue = (String) entry.getValue();
			config.setProperty(propertyName.replaceFirst(clazz.getSimpleName() + ".", ""), propertyValue);
		}
		return config;
	}
}
