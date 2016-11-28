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

package kieker.analysisteetime.plugin.filter.select.timestampfilter;

import kieker.analysisteetime.plugin.filter.select.timestampfilter.components.EventRecordTimestampFilter;
import kieker.analysisteetime.plugin.filter.select.timestampfilter.components.MonitioringRecordTimestampFilter;
import kieker.analysisteetime.plugin.filter.select.timestampfilter.components.OperationExecutionRecordTimestampFilter;
import kieker.analysisteetime.plugin.filter.select.timestampfilter.components.TraceMetadataTimestampFilter;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.flow.IEventRecord;
import kieker.common.record.flow.trace.TraceMetadata;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.InstanceOfFilter;
import teetime.stage.basic.merger.Merger;

/**
 * Allows to filter {@link IMonitoringRecord} objects based on their given timestamps.
 *
 * This class has several specialized internal filters to distinguish different timestamps for different record types. It has one input port and two output ports.
 *
 * If the received record is within the defined timestamps, the object is delivered unmodified to the recordsWithinTimePeriodOutputPort, otherwise to the
 * recordsOutsideTimePeriodOutputPort.
 *
 * @author Andre van Hoorn, Jan Waller, Lars Bluemke
 *
 * @since 1.2
 */
public class TimestampFilter extends CompositeStage {

	private final InputPort<IMonitoringRecord> monitoringRecordsCombinedInputPort;

	private final InstanceOfFilter<IMonitoringRecord, OperationExecutionRecord> instanceOfOperationExecutionRecordFilter;
	private final InstanceOfFilter<IMonitoringRecord, TraceMetadata> instanceOfTraceMetadataFilter;
	private final InstanceOfFilter<IMonitoringRecord, IEventRecord> instanceOfIEventFilter;
	private final InstanceOfFilter<IMonitoringRecord, IMonitoringRecord> instanceOfIMonitoringRecord;

	private final EventRecordTimestampFilter eventRecordTimestampStage;
	private final OperationExecutionRecordTimestampFilter operationExecutionRecordTimestampStage;
	private final TraceMetadataTimestampFilter traceMetadataTimestampStage;
	private final MonitioringRecordTimestampFilter monitoringRecordTimestampStage;

	private final Merger<IMonitoringRecord> recordsWithinTimePeriodMerger;
	private final Merger<IMonitoringRecord> recordsOutsideTimePeriodMerger;

	private final OutputPort<IMonitoringRecord> recordsWithinTimePeriodOutputPort;
	private final OutputPort<IMonitoringRecord> recordsOutsideTimePeriodOutputPort;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param ignoreBeforeTimestamp
	 *            The lower limit for the time stamps of the records.
	 * @param ignoreAfterTimestamp
	 *            The upper limit for the time stamps of the records.
	 */
	public TimestampFilter(final long ignoreBeforeTimestamp, final long ignoreAfterTimestamp) {

		// Instantiate internal stages
		this.instanceOfOperationExecutionRecordFilter = new InstanceOfFilter<IMonitoringRecord, OperationExecutionRecord>(OperationExecutionRecord.class);
		this.instanceOfTraceMetadataFilter = new InstanceOfFilter<IMonitoringRecord, TraceMetadata>(TraceMetadata.class);
		this.instanceOfIEventFilter = new InstanceOfFilter<IMonitoringRecord, IEventRecord>(IEventRecord.class);
		this.instanceOfIMonitoringRecord = new InstanceOfFilter<IMonitoringRecord, IMonitoringRecord>(IMonitoringRecord.class);

		this.eventRecordTimestampStage = new EventRecordTimestampFilter(ignoreBeforeTimestamp, ignoreAfterTimestamp);
		this.operationExecutionRecordTimestampStage = new OperationExecutionRecordTimestampFilter(ignoreBeforeTimestamp, ignoreAfterTimestamp);
		this.traceMetadataTimestampStage = new TraceMetadataTimestampFilter(ignoreBeforeTimestamp, ignoreAfterTimestamp);
		this.monitoringRecordTimestampStage = new MonitioringRecordTimestampFilter(ignoreBeforeTimestamp, ignoreAfterTimestamp);

		this.recordsWithinTimePeriodMerger = new Merger<IMonitoringRecord>();
		this.recordsOutsideTimePeriodMerger = new Merger<IMonitoringRecord>();

		// Define input and output ports of composite stage
		this.monitoringRecordsCombinedInputPort = this.instanceOfOperationExecutionRecordFilter.getInputPort();
		this.recordsWithinTimePeriodOutputPort = this.recordsWithinTimePeriodMerger.getOutputPort();
		this.recordsOutsideTimePeriodOutputPort = this.recordsOutsideTimePeriodMerger.getOutputPort();

		// Connect InstanceOfFilters with specific TimestampStages and each other
		this.connectPorts(this.instanceOfOperationExecutionRecordFilter.getMatchedOutputPort(), this.operationExecutionRecordTimestampStage.getInputPort());
		this.connectPorts(this.instanceOfOperationExecutionRecordFilter.getMismatchedOutputPort(), this.instanceOfTraceMetadataFilter.getInputPort());
		this.connectPorts(this.instanceOfTraceMetadataFilter.getMatchedOutputPort(), this.traceMetadataTimestampStage.getInputPort());
		this.connectPorts(this.instanceOfTraceMetadataFilter.getMismatchedOutputPort(), this.instanceOfIEventFilter.getInputPort());
		this.connectPorts(this.instanceOfIEventFilter.getMatchedOutputPort(), this.eventRecordTimestampStage.getInputPort());
		this.connectPorts(this.instanceOfIEventFilter.getMismatchedOutputPort(), this.instanceOfIMonitoringRecord.getInputPort());
		this.connectPorts(this.instanceOfIMonitoringRecord.getMatchedOutputPort(), this.monitoringRecordTimestampStage.getInputPort());

		// Connect specific TimestampStages with Mergers
		this.connectPorts(this.eventRecordTimestampStage.getRecordWithinTimePeriodOutputPort(), this.recordsWithinTimePeriodMerger.getNewInputPort());
		this.connectPorts(this.eventRecordTimestampStage.getRecordOutsideTimePeriodOutputPort(), this.recordsOutsideTimePeriodMerger.getNewInputPort());
		this.connectPorts(this.operationExecutionRecordTimestampStage.getRecordWithinTimePeriodOutputPort(), this.recordsWithinTimePeriodMerger.getNewInputPort());
		this.connectPorts(this.operationExecutionRecordTimestampStage.getRecordOutsideTimePeriodOutputPort(), this.recordsOutsideTimePeriodMerger.getNewInputPort());
		this.connectPorts(this.traceMetadataTimestampStage.getRecordWithinTimePeriodOutputPort(), this.recordsWithinTimePeriodMerger.getNewInputPort());
		this.connectPorts(this.traceMetadataTimestampStage.getRecordOutsideTimePeriodOutputPort(), this.recordsOutsideTimePeriodMerger.getNewInputPort());
		this.connectPorts(this.monitoringRecordTimestampStage.getRecordWithinTimePeriodOutputPort(), this.recordsWithinTimePeriodMerger.getNewInputPort());
		this.connectPorts(this.monitoringRecordTimestampStage.getRecordOutsideTimePeriodOutputPort(), this.recordsOutsideTimePeriodMerger.getNewInputPort());

	}

	/**
	 * Returns the input port for the records.
	 *
	 * @return The input port.
	 */
	public InputPort<IMonitoringRecord> getMonitoringRecordsCombinedInputPort() {
		return this.monitoringRecordsCombinedInputPort;
	}

	/**
	 * Returns the output port for the records whose timestamps are within the defined time period.
	 *
	 * @return The recordsWithinTimePeriod output port.
	 */
	public OutputPort<IMonitoringRecord> getRecordsWithinTimePeriodOutputPort() {
		return this.recordsWithinTimePeriodOutputPort;
	}

	/**
	 * Returns the output port for the records whose timestamps are outside the defined time period.
	 *
	 * @return The recordsOutsideTimePeriod output port.
	 */
	public OutputPort<IMonitoringRecord> getRecordsOutsideTimePeriodOutputPort() {
		return this.recordsOutsideTimePeriodOutputPort;
	}

}
