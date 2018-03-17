/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.plugin.filter.select.traceidfilter;

import kieker.analysisteetime.plugin.filter.select.traceidfilter.components.OperationExecutionTraceIdFilter;
import kieker.analysisteetime.plugin.filter.select.traceidfilter.components.TraceEventTraceIdFilter;
import kieker.analysisteetime.plugin.filter.select.traceidfilter.components.TraceMetadataTraceIdFilter;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.TraceMetadata;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.MultipleInstanceOfFilter;
import teetime.stage.basic.merger.Merger;

/**
 * Allows to filter Traces about their traceIds.
 *
 * This class has exactly one input port and two output ports. If the received object
 * contains the defined traceID, the object is delivered unmodified to the matchingTraceIdOutputPort otherwise to the mismatchingTraceIdOutputPort.
 *
 * @author Andre van Hoorn, Jan Waller, Lars Bluemke
 *
 * @since 1.2
 */
public class TraceIdFilter extends CompositeStage {

	private final InputPort<IMonitoringRecord> monitoringRecordsCombinedInputPort;

	private final OutputPort<IMonitoringRecord> matchingTraceIdOutputPort;
	private final OutputPort<IMonitoringRecord> mismatchingTraceIdOutputPort;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param acceptAllTraces
	 *            Determining whether to accept all traces, regardless of the given trace IDs.
	 * @param selectedTraceIds
	 *            Determining which trace IDs should be accepted by this filter.
	 */
	public TraceIdFilter(final boolean acceptAllTraces, final Long[] selectedTraceIds) {
		// Initializing the internal filters
		final MultipleInstanceOfFilter<IMonitoringRecord> instanceOfFilter = new MultipleInstanceOfFilter<>();
		final TraceMetadataTraceIdFilter traceMetadataFilter = new TraceMetadataTraceIdFilter(acceptAllTraces, selectedTraceIds);
		final TraceEventTraceIdFilter traceEventFilter = new TraceEventTraceIdFilter(acceptAllTraces, selectedTraceIds);
		final OperationExecutionTraceIdFilter operationExecutionFilter = new OperationExecutionTraceIdFilter(acceptAllTraces, selectedTraceIds);

		final Merger<IMonitoringRecord> matchingMerger = new Merger<>();
		final Merger<IMonitoringRecord> mismatchingMerger = new Merger<>();

		this.monitoringRecordsCombinedInputPort = instanceOfFilter.getInputPort();
		this.matchingTraceIdOutputPort = matchingMerger.getOutputPort();
		this.mismatchingTraceIdOutputPort = mismatchingMerger.getOutputPort();

		// Connecting the internal filters
		this.connectPorts(instanceOfFilter.getOutputPortForType(TraceMetadata.class), traceMetadataFilter.getInputPort());
		this.connectPorts(instanceOfFilter.getOutputPortForType(AbstractTraceEvent.class), traceEventFilter.getInputPort());
		this.connectPorts(instanceOfFilter.getOutputPortForType(OperationExecutionRecord.class), operationExecutionFilter.getInputPort());

		this.connectPorts(traceMetadataFilter.getMatchingTraceIdOutputPort(), matchingMerger.getNewInputPort());
		this.connectPorts(traceEventFilter.getMatchingTraceIdOutputPort(), matchingMerger.getNewInputPort());
		this.connectPorts(operationExecutionFilter.getMatchingTraceIdOutputPort(), matchingMerger.getNewInputPort());

		this.connectPorts(traceMetadataFilter.getMismatchingTraceIdOutputPort(), mismatchingMerger.getNewInputPort());
		this.connectPorts(traceEventFilter.getMismatchingTraceIdOutputPort(), mismatchingMerger.getNewInputPort());
		this.connectPorts(operationExecutionFilter.getMismatchingTraceIdOutputPort(), mismatchingMerger.getNewInputPort());
	}

	public InputPort<IMonitoringRecord> getMonitoringRecordsCombinedInputPort() {
		return this.monitoringRecordsCombinedInputPort;
	}

	public OutputPort<IMonitoringRecord> getMatchingTraceIdOutputPort() {
		return this.matchingTraceIdOutputPort;
	}

	public OutputPort<IMonitoringRecord> getMismatchingTraceIdOutputPort() {
		return this.mismatchingTraceIdOutputPort;
	}

}
