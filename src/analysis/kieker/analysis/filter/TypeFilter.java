/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.analysis.filter;

import java.util.LinkedList;
import java.util.List;

import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * This filter has exactly one input port and one output port.
 * 
 * Only the specified objects are forwarded to the output port.
 * 
 * @author Jan Waller
 */
@Plugin(outputPorts = @OutputPort(name = TypeFilter.OUTPUT_PORT_NAME, eventTypes = {}, description = "all objects with matching types are forwarded"))
public final class TypeFilter extends AbstractAnalysisPlugin {
	private static final Log LOG = LogFactory.getLog(TypeFilter.class);

	public static final String OUTPUT_PORT_NAME = "output";
	public static final String INPUT_PORT_NAME = "input";
	public static final String CONFIG_TYPES = TypeFilter.class.getName() + ".Types";

	private final Class<?>[] acceptedClasses;

	public TypeFilter(final Configuration configuration) {
		super(configuration);
		final String[] classes = configuration.getStringArrayProperty(TypeFilter.CONFIG_TYPES);
		final List<Class<?>> listOfClasses = new LinkedList<Class<?>>();
		for (final String clazz : classes) {
			try {
				listOfClasses.add(Class.forName(clazz));
			} catch (final ClassNotFoundException ex) {
				TypeFilter.LOG.warn("Failed to add class " + clazz + " to the filter.");
			}
		}
		if (listOfClasses.size() > 0) {
			this.acceptedClasses = listOfClasses.toArray(new Class<?>[listOfClasses.size()]);
		} else {
			this.acceptedClasses = new Class<?>[] { Object.class };
		}
	}

	@Override
	protected final Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(TypeFilter.CONFIG_TYPES, "java.lang.Object");
		return configuration;
	}

	@Override
	public final Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(TypeFilter.CONFIG_TYPES, Configuration.toProperty(this.acceptedClasses));
		return configuration;
	}

	@InputPort(name = CountingFilter.INPUT_PORT_NAME, eventTypes = {}, description = "all objects with matching types are forwarded")
	public final void newEvent(final Object event) {
		for (final Class<?> clazz : this.acceptedClasses) {
			if (clazz.isAssignableFrom(event.getClass())) {
				super.deliver(CountingFilter.OUTPUT_PORT_NAME, event);
				break; // only deliver once!
			}
		}
	}
}
