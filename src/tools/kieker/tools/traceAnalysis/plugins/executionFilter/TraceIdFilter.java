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

package kieker.tools.traceAnalysis.plugins.executionFilter;

import java.util.Iterator;
import java.util.Set;

import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.port.AInputPort;
import kieker.analysis.plugin.port.AOutputPort;
import kieker.analysis.plugin.port.APlugin;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.systemModel.Execution;

/**
 * Allows to filter Execution objects based on their traceId.<br>
 * 
 * This class has exactly one input port named "in" and one output ports named
 * "out". It receives only objects inheriting from the class {@link Execution}.
 * If the received object contains the defined traceID, the object is delivered
 * unmodified to the output port.
 * 
 * @author Andre van Hoorn
 */
@APlugin(
		outputPorts = {
			@AOutputPort(name = TraceIdFilter.OUTPUT_PORT_NAME, description = "Execution output", eventTypes = { Execution.class })
		})
public class TraceIdFilter extends AbstractAnalysisPlugin {

	public static final String INPUT_PORT_NAME = "newExecution";
	public static final String OUTPUT_PORT_NAME = "outputPort";
	public static final String CONFIG_SELECTED_TRACES = TraceIdFilter.class.getName() + ".selectedTraces";
	private final Set<Long> selectedTraces;

	public TraceIdFilter(final Configuration configuration) {
		super(configuration);
		// TODO: Initialize from the variable.
		this.selectedTraces = null;
	}

	/**
	 * Creates a filter instance that only passes Execution objects <i>e</i>
	 * whose traceId (<i>e.traceId</i>) is element of the set <i>selectedTraces</i>.
	 * 
	 * @param selectedTraces
	 */
	public TraceIdFilter(final Set<Long> selectedTraces) {
		super(new Configuration(null));
		this.selectedTraces = selectedTraces;
	}

	@AInputPort(description = "Execution input", eventTypes = { Execution.class })
	public void newExecution(final Object data) {
		final Execution execution = (Execution) data;
		if ((this.selectedTraces != null) && !this.selectedTraces.contains(execution.getTraceId())) {
			// not interested in this trace
			return;
		}
		super.deliver(TraceIdFilter.OUTPUT_PORT_NAME, data);
	}

	@Override
	public boolean execute() {
		return true; // do nothing
	}

	@Override
	public void terminate(final boolean error) {
		// do nothing
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration defaultConfiguration = new Configuration();
		// TODO: Provide default properties.
		return defaultConfiguration;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration(null);

		if (this.selectedTraces != null) {
			final String selectedTracesArr[] = new String[this.selectedTraces.size()];
			final Iterator<Long> iter = this.selectedTraces.iterator();
			int i = 0;
			while (iter.hasNext()) {
				selectedTracesArr[i++] = iter.next().toString();
			}
			configuration.setProperty(TraceIdFilter.CONFIG_SELECTED_TRACES, Configuration.toProperty(selectedTracesArr));
		}
		return configuration;
	}
}
