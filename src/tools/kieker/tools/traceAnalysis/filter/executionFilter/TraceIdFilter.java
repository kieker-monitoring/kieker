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

package kieker.tools.traceAnalysis.filter.executionFilter;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.systemModel.Execution;

/**
 * Allows to filter Execution objects based on their trace Id.<br>
 * 
 * This class has exactly one input port named and one output port. It receives only objects inheriting from the class {@link Execution}. If the received object
 * contains the defined traceID, the object is delivered unmodified to the output port.
 * 
 * @author Andre van Hoorn
 * 
 * @deprecated To be removed in Kieker 1.8 (Use {@link kieker.analysis.plugin.filter.select.TraceIdFilter instead}).
 * 
 * @since 1.2
 */
@SuppressWarnings("deprecation")
@Deprecated
@Plugin(description = "A filter allowing to filter incoming execution objects based on their trace ID",
		outputPorts = {
			@OutputPort(name = TraceIdFilter.OUTPUT_PORT_NAME_MATCH, description = "Forwards executions with matching trace IDs", eventTypes = { Execution.class })
		})
public class TraceIdFilter extends kieker.tools.traceAnalysis.filter.AbstractTraceIdFilter {

	/** This is the name of the input port receiving new executions. */
	public static final String INPUT_PORT_NAME_EXECUTION = "executions";

	/** This is the name of the output port delivering the accepted executions. */
	public static final String OUTPUT_PORT_NAME_MATCH = "executionsMatchingId";

	public static final String CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES = "selectedAll";
	public static final String CONFIG_PROPERTY_NAME_SELECTED_TRACES = "selectedTraces";

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public TraceIdFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * 
	 * @deprecated To be removed in Kieker 1.8.
	 */
	@Deprecated
	public TraceIdFilter(final Configuration configuration) {
		this(configuration, null);
	}

	/**
	 * This method represents the input port of this filter, processing incoming execution objects.
	 * 
	 * @param execution
	 *            The next execution object.
	 */
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
