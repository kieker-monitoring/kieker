/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

	private InputPort<IMonitoringRecord> monitoringRecordsCombinedInputPort;

	private OutputPort<IMonitoringRecord> matchingTraceIdOutputPort;
	private OutputPort<IMonitoringRecord> mismatchingTraceIdOutputPort;

	private final MultipleInstanceOfFilter<IMonitoringRecord> instanceOfFilter;
	private final TraceMetadataTraceIdFilter traceMetadataFilter;
	private final TraceEventTraceIdFilter traceEventFilter;
	private final OperationExecutionTraceIdFilter operationExecutionFilter;

	private final Merger<IMonitoringRecord> matchingMerger;
	private final Merger<IMonitoringRecord> mismatchingMerger;

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
		this.instanceOfFilter = new MultipleInstanceOfFilter<IMonitoringRecord>();
		this.traceMetadataFilter = new TraceMetadataTraceIdFilter(acceptAllTraces, selectedTraceIds);
		this.traceEventFilter = new TraceEventTraceIdFilter(acceptAllTraces, selectedTraceIds);
		this.operationExecutionFilter = new OperationExecutionTraceIdFilter(acceptAllTraces, selectedTraceIds);

		this.matchingMerger = new Merger<IMonitoringRecord>();
		this.mismatchingMerger = new Merger<IMonitoringRecord>();

		this.monitoringRecordsCombinedInputPort = this.instanceOfFilter.getInputPort();
		this.matchingTraceIdOutputPort = this.matchingMerger.getOutputPort();
		this.mismatchingTraceIdOutputPort = this.mismatchingMerger.getOutputPort();

		// Connecting the internal filters
		this.monitoringRecordsCombinedInputPort = this.instanceOfFilter.getInputPort();

		this.connectPorts(this.instanceOfFilter.getOutputPortForType(TraceMetadata.class), this.traceMetadataFilter.getInputPort());
		this.connectPorts(this.instanceOfFilter.getOutputPortForType(AbstractTraceEvent.class), this.traceEventFilter.getInputPort());
		this.connectPorts(this.instanceOfFilter.getOutputPortForType(OperationExecutionRecord.class), this.operationExecutionFilter.getInputPort());

		this.connectPorts(this.traceMetadataFilter.getMatchingTraceIdOutputPort(), this.matchingMerger.getNewInputPort());
		this.connectPorts(this.traceEventFilter.getMatchingTraceIdOutputPort(), this.matchingMerger.getNewInputPort());
		this.connectPorts(this.operationExecutionFilter.getMatchingTraceIdOutputPort(), this.matchingMerger.getNewInputPort());

		this.connectPorts(this.traceMetadataFilter.getMismatchingTraceIdOutputPort(), this.mismatchingMerger.getNewInputPort());
		this.connectPorts(this.traceEventFilter.getMismatchingTraceIdOutputPort(), this.mismatchingMerger.getNewInputPort());
		this.connectPorts(this.operationExecutionFilter.getMismatchingTraceIdOutputPort(), this.mismatchingMerger.getNewInputPort());

		this.matchingTraceIdOutputPort = this.matchingMerger.getOutputPort();
		this.mismatchingTraceIdOutputPort = this.mismatchingMerger.getOutputPort();
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
