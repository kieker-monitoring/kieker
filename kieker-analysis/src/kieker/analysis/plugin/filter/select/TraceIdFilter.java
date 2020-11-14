/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
import kieker.common.record.flow.trace.TraceMetadata;

/**
 * Allows to filter Traces about their traceIds.
 *
 * This class has exactly one input port and one output port. If the received object
 * contains the defined traceID, the object is delivered unmodified to the output port.
 *
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.2
 */
@Plugin(description = "A filter allowing to filter incoming objects based on their trace ID",
		outputPorts = {
			@OutputPort(name = TraceIdFilter.OUTPUT_PORT_NAME_MATCH, description = "Forwards events with matching trace IDs", eventTypes = {
				AbstractTraceEvent.class, TraceMetadata.class, OperationExecutionRecord.class }),
			@OutputPort(name = TraceIdFilter.OUTPUT_PORT_NAME_MISMATCH, description = "Forwards events with trace IDs not matching", eventTypes = {
				AbstractTraceEvent.class, TraceMetadata.class, OperationExecutionRecord.class })
		},
		configuration = {
			@Property(name = TraceIdFilter.CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES, defaultValue = "true"),
			@Property(name = TraceIdFilter.CONFIG_PROPERTY_NAME_SELECTED_TRACES, defaultValue = "")
		})
public final class TraceIdFilter extends AbstractFilterPlugin {
	/** The name of the input port accepting flow records. */
	public static final String INPUT_PORT_NAME_FLOW = "monitoringRecordsFlow";
	/** The name of the input port accepting execution records. */
	public static final String INPUT_PORT_NAME_EXECUTION = "monitoringRecordsExecution";
	/** The name of the input port accepting both types of records. */
	public static final String INPUT_PORT_NAME_COMBINED = "monitoringRecordsCombined";
	/** The name of the output port delivering the records with matching IDs. */
	public static final String OUTPUT_PORT_NAME_MATCH = "recordsMatchingId";
	/** The name of the output port delivering the records with the non matching IDs. */
	public static final String OUTPUT_PORT_NAME_MISMATCH = "recordsNotMatchingId";

	/** The name of the property determining whether to accept all traces, regardless of the given trace IDs. */
	public static final String CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES = "acceptAllTraces";
	/** The name of the property determining which trace IDs should be accepted by this filter. */
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
		this.selectedTraceIds = new TreeSet<>();
		for (final String id : configuration.getStringArrayProperty(CONFIG_PROPERTY_NAME_SELECTED_TRACES)) {
			this.selectedTraceIds.add(Long.parseLong(id));
		}
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
		return (this.acceptAllTraces || this.selectedTraceIds.contains(traceId));
	}

	/**
	 * This method represents an input port for both operation execution and flow records.
	 *
	 * @param record
	 *            The next record.
	 */
	@InputPort(name = INPUT_PORT_NAME_COMBINED, description = "Receives execution and trace events to be selected by trace ID",
			eventTypes = { ITraceRecord.class, TraceMetadata.class, OperationExecutionRecord.class })
	public void inputCombined(final IMonitoringRecord record) {
		if (record instanceof OperationExecutionRecord) {
			this.inputOperationExecutionRecord((OperationExecutionRecord) record);
		} else if ((record instanceof ITraceRecord) || (record instanceof TraceMetadata)) {
			this.inputTraceEvent((IFlowRecord) record);
		} // else discard it, we should never have gotten it anyhow
	}

	/**
	 * This method represents an input port for flow records.
	 *
	 * @param record
	 *            The next record.
	 */
	@InputPort(name = INPUT_PORT_NAME_FLOW, description = "Receives trace events to be selected by trace ID",
			eventTypes = { ITraceRecord.class, TraceMetadata.class })
	public void inputTraceEvent(final IFlowRecord record) {
		final long traceId;

		if (record instanceof TraceMetadata) {
			traceId = ((TraceMetadata) record).getTraceId();
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

	/**
	 * This method represents an input port for operation execution records.
	 *
	 * @param record
	 *            The next record.
	 */
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
