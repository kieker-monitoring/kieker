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

package kieker.analysis.plugin.filter.select;

import java.util.Set;
import java.util.TreeSet;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.ITraceRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.Trace;

/**
 * Allows to filter Traces about their traceIds.
 * 
 * This class has exactly one input port and one output port. If the received object
 * contains the defined traceID, the object is delivered unmodified to the output port.
 * 
 * @author Andre van Hoorn, Jan Waller
 */
@Plugin(description = "A filter allowing to filter incoming objects based on their trace ID",
		outputPorts = {
			@OutputPort(name = TraceIdFilter.OUTPUT_PORT_NAME_MATCH, description = "Forwards events with matching trace IDs", eventTypes = {
				AbstractTraceEvent.class, Trace.class, OperationExecutionRecord.class }),
			@OutputPort(name = TraceIdFilter.OUTPUT_PORT_NAME_MISMATCH, description = "Forwards events with trace IDs not matching", eventTypes = {
				AbstractTraceEvent.class, Trace.class, OperationExecutionRecord.class })
		},
		configuration = {
			@Property(name = TraceIdFilter.CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES, defaultValue = "true"),
			@Property(name = TraceIdFilter.CONFIG_PROPERTY_NAME_SELECTED_TRACES, defaultValue = "")
		})
public final class TraceIdFilter extends AbstractFilterPlugin {
	public static final String INPUT_PORT_NAME_FLOW = "monitoringRecordsFlow";
	public static final String INPUT_PORT_NAME_EXECUTION = "monitoringRecordsExecution";
	public static final String INPUT_PORT_NAME_COMBINED = "monitoringRecordsCombined";

	public static final String OUTPUT_PORT_NAME_MATCH = "recordsMatchingId";
	public static final String OUTPUT_PORT_NAME_MISMATCH = "recordsNotMatchingId";

	public static final String CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES = "acceptAllTraces";
	public static final String CONFIG_PROPERTY_NAME_SELECTED_TRACES = "selectedTraceIds";

	private final boolean acceptAllTraces;
	private final Set<Long> selectedTraceIds;

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

		this.acceptAllTraces = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES);
		this.selectedTraceIds = new TreeSet<Long>();
		for (final String id : configuration.getStringArrayProperty(CONFIG_PROPERTY_NAME_SELECTED_TRACES)) {
			this.selectedTraceIds.add(Long.parseLong(id));
		}
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
	 * {@inheritDoc}
	 */
	@Override
	public final Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES, Boolean.toString(this.acceptAllTraces));
		configuration.setProperty(CONFIG_PROPERTY_NAME_SELECTED_TRACES, Configuration.toProperty(this.selectedTraceIds.toArray()));
		return configuration;
	}

	private final boolean acceptId(final long traceId) {
		if (this.acceptAllTraces || this.selectedTraceIds.contains(traceId)) {
			return true;
		}
		return false;
	}

	@InputPort(name = INPUT_PORT_NAME_COMBINED, description = "Receives execution and trace events to be selected by trace ID",
			eventTypes = { ITraceRecord.class, Trace.class, OperationExecutionRecord.class })
	public void inputCombined(final IMonitoringRecord record) {
		if (record instanceof OperationExecutionRecord) {
			this.inputOperationExecutionRecord((OperationExecutionRecord) record);
		} else if ((record instanceof ITraceRecord) || (record instanceof Trace)) {
			this.inputTraceEvent((IFlowRecord) record);
		} // else discard it, we should never have gotten it anyhow
	}

	@InputPort(name = INPUT_PORT_NAME_FLOW, description = "Receives trace events to be selected by trace ID",
			eventTypes = { ITraceRecord.class, Trace.class })
	public void inputTraceEvent(final IFlowRecord record) {
		final long traceId;

		if (record instanceof Trace) {
			traceId = ((Trace) record).getTraceId();
		} else if (record instanceof AbstractTraceEvent) {
			traceId = ((ITraceRecord) record).getTraceId();
		} else {
			// should not happen given the accepted type
			return;
		}

		if (this.acceptId(traceId)) {
			super.deliver(OUTPUT_PORT_NAME_MATCH, record);
		} else {
			super.deliver(OUTPUT_PORT_NAME_MISMATCH, record);
		}
	}

	@InputPort(name = INPUT_PORT_NAME_EXECUTION, description = "Receives execution events to be selected by trace ID",
			eventTypes = { OperationExecutionRecord.class })
	public void inputOperationExecutionRecord(final OperationExecutionRecord record) {
		if (this.acceptId(record.getTraceId())) {
			super.deliver(OUTPUT_PORT_NAME_MATCH, record);
		} else {
			super.deliver(OUTPUT_PORT_NAME_MISMATCH, record);
		}
	}
}
