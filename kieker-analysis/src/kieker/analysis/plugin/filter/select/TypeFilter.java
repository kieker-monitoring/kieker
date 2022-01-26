/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.plugin.filter.select;

import java.util.LinkedList;
import java.util.List;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

/**
 * This filter has exactly one input port and one output port.
 *
 * Only the specified objects are forwarded to the output port. All other
 * objects are forwarded to the output-not port.
 *
 * @author Jan Waller
 *
 * @since 1.5
 */
@Plugin(description = "Filters incoming objects based on their type", outputPorts = {
	@OutputPort(name = TypeFilter.OUTPUT_PORT_NAME_TYPE_MATCH, eventTypes = Object.class,
			description = "Forwards events matching the configured types"),
	@OutputPort(name = TypeFilter.OUTPUT_PORT_NAME_TYPE_MISMATCH, eventTypes = {},
			description = "Forwards events not matching the configured types") },
		configuration = {
			@Property(name = TypeFilter.CONFIG_PROPERTY_NAME_TYPES, defaultValue = "java.lang.Object") })
public final class TypeFilter extends AbstractFilterPlugin {

	/**
	 * The name of the input port receiving new events.
	 */
	public static final String INPUT_PORT_NAME_EVENTS = "events";

	/**
	 * The name of the output port where the incoming matching objects will be sent
	 * to.
	 */
	public static final String OUTPUT_PORT_NAME_TYPE_MATCH = "eventsMatchingType";

	/**
	 * The name of the output port where the incoming objects will be sent to, which
	 * do not match the configured types.
	 */
	public static final String OUTPUT_PORT_NAME_TYPE_MISMATCH = "eventsNotMatchingType";

	/**
	 * The name of the property determining which types will be filtered.
	 */
	public static final String CONFIG_PROPERTY_NAME_TYPES = "types";

	private final Class<?>[] acceptedClasses;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public TypeFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		final String[] classes = configuration.getStringArrayProperty(CONFIG_PROPERTY_NAME_TYPES);
		final List<Class<?>> listOfClasses = new LinkedList<>();
		for (final String clazz : classes) {
			try {
				listOfClasses.add(Class.forName(clazz));
			} catch (final ClassNotFoundException ex) {
				this.logger.warn("Failed to add class {} to the filter.", clazz, ex);
			}
		}
		this.acceptedClasses = listOfClasses.toArray(new Class<?>[listOfClasses.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		final String[] acceptedClassesConfig = new String[this.acceptedClasses.length];
		for (int i = 0; i < acceptedClassesConfig.length; i++) {
			acceptedClassesConfig[i] = this.acceptedClasses[i].getName();
		}
		configuration.setProperty(CONFIG_PROPERTY_NAME_TYPES, Configuration.toProperty(acceptedClassesConfig));
		return configuration;
	}

	/**
	 * This method represents the input port for the incoming objects.
	 *
	 * @param event
	 *            The new incoming object.
	 */
	@InputPort(name = INPUT_PORT_NAME_EVENTS, eventTypes = Object.class, description = "all objects with matching types are forwarded")
	public final void inputEvents(final Object event) {
		final Class<?> eventClass = event.getClass();
		for (final Class<?> clazz : this.acceptedClasses) {
			if (clazz.isAssignableFrom(eventClass)) {
				super.deliver(OUTPUT_PORT_NAME_TYPE_MATCH, event);
				return; // only deliver once!
			}
		}
		super.deliver(OUTPUT_PORT_NAME_TYPE_MISMATCH, event);
	}
}
