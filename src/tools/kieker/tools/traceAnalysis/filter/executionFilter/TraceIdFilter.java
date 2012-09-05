/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.filter.executionFilter;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.filter.AbstractTraceIdFilter;
import kieker.tools.traceAnalysis.systemModel.Execution;

/**
 * Allows to filter Execution objects based on their trace Id.<br>
 * 
 * This class has exactly one input port named and one output port. It receives only objects inheriting from the class {@link Execution}. If the received object
 * contains the defined traceID, the object is delivered unmodified to the output port.
 * 
 * @author Andre van Hoorn
 */
@Plugin(description = "A filter allowing to filter incoming execution objects based on their trace ID",
		outputPorts = @OutputPort(name = TraceIdFilter.OUTPUT_PORT_NAME_MATCH, description = "Forwards executions with matching trace IDs", eventTypes = { Execution.class }))
public class TraceIdFilter extends AbstractTraceIdFilter {

	public static final String INPUT_PORT_NAME_EXECUTION = "executions";
	public static final String OUTPUT_PORT_NAME_MATCH = "executionsMatchingId";

	public static final String CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES = "selectedAll";
	public static final String CONFIG_PROPERTY_NAME_SELECTED_TRACES = "selectedTraces";

	public TraceIdFilter(final Configuration configuration) {
		super(configuration);
	}

	@InputPort(name = INPUT_PORT_NAME_EXECUTION, description = "Receives execution events to be selected by trace ID", eventTypes = { Execution.class })
	public void inputExecution(final Execution execution) {
		if (super.passId(execution.getTraceId())) {
			super.deliver(OUTPUT_PORT_NAME_MATCH, execution);
		}
	}

	@Override
	protected String getConfigurationPropertySelectAllTraces() {
		return CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES;
	}

	@Override
	protected String getConfigurationPropertySelectedTraces() {
		return CONFIG_PROPERTY_NAME_SELECTED_TRACES;
	}
}
