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

package kieker.tools.traceAnalysis.plugins.flow;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import kieker.analysis.plugin.port.InputPort;
import kieker.analysis.plugin.port.OutputPort;
import kieker.analysis.plugin.port.Plugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.record.flow.TraceEvent;
import kieker.tools.traceAnalysis.plugins.AbstractTraceIdFilter;

/**
 * Allows to filter {@link TraceEvent} objects based on their {@link TraceEvent#getTraceId()}s.<br>
 * 
 * This class has exactly one input port and one output port. If the received object
 * contains the defined traceID, the object is delivered unmodified to the output port.
 * 
 * @author Andre van Hoorn
 */
@Plugin(
		outputPorts = { @OutputPort(name = TraceIdFilter.OUTPUT_PORT_NAME, description = "Trace event output", eventTypes = { TraceEvent.class })
		})
public class TraceIdFilter extends AbstractTraceIdFilter {
	public static final String INPUT_PORT_NAME = "inputTraceEvent";
	public static final String OUTPUT_PORT_NAME = "defaultOutput";

	public static final String CONFIG_SELECTED_TRACES = TraceIdFilter.class.getName() + ".selectedTraces";

	public TraceIdFilter(final Configuration configuration, final Map<String, AbstractRepository> repositories) {
		// TODO: Initialize trace IDs from the variable.
		super(configuration, repositories, null);
	}

	/**
	 * Creates a filter instance that only passes {@link TraceEvent} objects <i>e</i>
	 * whose traceId (<i>e.traceId</i>) is element of the set <i>selectedTraces</i>.
	 * 
	 * @param selectedTraces
	 */
	public TraceIdFilter(final Set<Long> selectedTraces) {
		super(new Configuration(null), new HashMap<String, AbstractRepository>(), selectedTraces);
	}

	@InputPort(description = "Trace event input", eventTypes = { TraceEvent.class })
	public void inputTraceEvent(final Object data) {
		final TraceEvent event = (TraceEvent) data;
		if (super.passId(event.getTraceId())) {
			super.deliver(TraceIdFilter.OUTPUT_PORT_NAME, data);
		}
	}

	@Override
	protected String getConfigurationPropertySelectedTraces() {
		return TraceIdFilter.CONFIG_SELECTED_TRACES;
	}
}
