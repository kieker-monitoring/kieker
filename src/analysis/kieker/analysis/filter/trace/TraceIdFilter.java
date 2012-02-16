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

package kieker.analysis.filter.trace;

import java.util.Set;
import java.util.TreeSet;

import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.legacy.OperationExecutionRecord;

/**
 * Allows to filter Traces about their traceIds.
 * 
 * This class has exactly one input port and one output port. If the received object
 * contains the defined traceID, the object is delivered unmodified to the output port.
 * 
 * @author Andre van Hoorn, Jan Waller
 */
@Plugin(outputPorts = {
	@OutputPort(name = TraceIdFilter.OUTPUT_PORT_NAME, description = "accepted traces", eventTypes = { AbstractTraceEvent.class, OperationExecutionRecord.class }),
	@OutputPort(name = TraceIdFilter.OUTPUT_PORT_NAME_NOT, description = "rejected traces", eventTypes = { AbstractTraceEvent.class, OperationExecutionRecord.class })

})
public final class TraceIdFilter extends AbstractAnalysisPlugin {
	public static final String INPUT_PORT_NAME = "input-combined";
	public static final String INPUT_PORT_NAME_FLOW = "input-TraceEvent";
	public static final String INPUT_PORT_NAME_OER = "input-OperationExecutionRecord";
	public static final String OUTPUT_PORT_NAME = "output";
	public static final String OUTPUT_PORT_NAME_NOT = "output-not";

	public static final String CONFIG_SELECT_ALL_TRACES = TraceIdFilter.class.getName() + ".acceptAllTraces";
	public static final String CONFIG_SELECTED_TRACES = TraceIdFilter.class.getName() + ".selectedTraceIds";

	private final boolean acceptAllTraces;
	private final Set<Long> selectedTraceIds;

	public TraceIdFilter(final Configuration configuration) {
		super(configuration);
		this.acceptAllTraces = configuration.getBooleanProperty(TraceIdFilter.CONFIG_SELECT_ALL_TRACES);
		this.selectedTraceIds = new TreeSet<Long>();
		for (final String id : configuration.getStringArrayProperty(TraceIdFilter.CONFIG_SELECTED_TRACES)) {
			this.selectedTraceIds.add(Long.parseLong(id));
		}
	}

	@Override
	protected final Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(TraceIdFilter.CONFIG_SELECT_ALL_TRACES, Boolean.TRUE.toString());
		configuration.setProperty(TraceIdFilter.CONFIG_SELECTED_TRACES, ""); // example might be "0|1|2|3|4"
		return configuration;
	}

	@Override
	public final Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(TraceIdFilter.CONFIG_SELECT_ALL_TRACES, Boolean.toString(this.acceptAllTraces));
		configuration.setProperty(TraceIdFilter.CONFIG_SELECTED_TRACES, Configuration.toProperty(this.selectedTraceIds.toArray()));
		return configuration;
	}

	private final boolean acceptId(final long traceId) {
		if (this.acceptAllTraces || this.selectedTraceIds.contains(traceId)) {
			return true;
		}
		return false;
	}

	@InputPort(name = TraceIdFilter.INPUT_PORT_NAME, description = "combined input", eventTypes = { AbstractTraceEvent.class, OperationExecutionRecord.class })
	public void inputCombined(final IMonitoringRecord record) {
		if (record instanceof OperationExecutionRecord) {
			this.inputOperationExecutionRecord((OperationExecutionRecord) record);
		} else { // if (record instanceof AbstractTraceEvent) { // assume it is AbstractTraceEvent
			this.inputTraceEvent((AbstractTraceEvent) record);
		} // else discard it, we should never have gotten it anyhow
	}

	@InputPort(name = TraceIdFilter.INPUT_PORT_NAME_FLOW, description = "TraceEvent input", eventTypes = { AbstractTraceEvent.class })
	public void inputTraceEvent(final AbstractTraceEvent event) {
		if (this.acceptId(event.getTraceId())) {
			super.deliver(TraceIdFilter.OUTPUT_PORT_NAME, event);
		} else {
			super.deliver(TraceIdFilter.OUTPUT_PORT_NAME_NOT, event);
		}
	}

	@InputPort(name = TraceIdFilter.INPUT_PORT_NAME_OER, description = "TraceEvent input", eventTypes = { OperationExecutionRecord.class })
	public void inputOperationExecutionRecord(final OperationExecutionRecord record) {
		if (this.acceptId(record.getTraceId())) {
			super.deliver(TraceIdFilter.OUTPUT_PORT_NAME, record);
		} else {
			super.deliver(TraceIdFilter.OUTPUT_PORT_NAME_NOT, record);
		}
	}
}
