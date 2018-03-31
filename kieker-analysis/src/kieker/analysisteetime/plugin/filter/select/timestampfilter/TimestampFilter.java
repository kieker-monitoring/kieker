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
		final InstanceOfFilter<IMonitoringRecord, OperationExecutionRecord> instanceOfOperationExecutionRecordFilter = new InstanceOfFilter<>(
				OperationExecutionRecord.class);
		final InstanceOfFilter<IMonitoringRecord, TraceMetadata> instanceOfTraceMetadataFilter = new InstanceOfFilter<>(
				TraceMetadata.class);
		final InstanceOfFilter<IMonitoringRecord, IEventRecord> instanceOfIEventFilter = new InstanceOfFilter<>(IEventRecord.class);
		final InstanceOfFilter<IMonitoringRecord, IMonitoringRecord> instanceOfIMonitoringRecord = new InstanceOfFilter<>(
				IMonitoringRecord.class);

		final EventRecordTimestampFilter eventRecordTimestampStage = new EventRecordTimestampFilter(ignoreBeforeTimestamp, ignoreAfterTimestamp);
		final OperationExecutionRecordTimestampFilter operationExecutionRecordTimestampStage = new OperationExecutionRecordTimestampFilter(ignoreBeforeTimestamp,
				ignoreAfterTimestamp);
		final TraceMetadataTimestampFilter traceMetadataTimestampStage = new TraceMetadataTimestampFilter(ignoreBeforeTimestamp, ignoreAfterTimestamp);
		final MonitioringRecordTimestampFilter monitoringRecordTimestampStage = new MonitioringRecordTimestampFilter(ignoreBeforeTimestamp, ignoreAfterTimestamp);

		final Merger<IMonitoringRecord> recordsWithinTimePeriodMerger = new Merger<>();
		final Merger<IMonitoringRecord> recordsOutsideTimePeriodMerger = new Merger<>();

		// Define input and output ports of composite stage
		this.monitoringRecordsCombinedInputPort = this.createInputPort(instanceOfOperationExecutionRecordFilter.getInputPort());
		this.recordsWithinTimePeriodOutputPort = this.createOutputPort(recordsWithinTimePeriodMerger.getOutputPort());
		this.recordsOutsideTimePeriodOutputPort = this.createOutputPort(recordsOutsideTimePeriodMerger.getOutputPort());

		// Connect InstanceOfFilters with specific TimestampStages and each other
		this.connectPorts(instanceOfOperationExecutionRecordFilter.getMatchedOutputPort(), operationExecutionRecordTimestampStage.getInputPort());
		this.connectPorts(instanceOfOperationExecutionRecordFilter.getMismatchedOutputPort(), instanceOfTraceMetadataFilter.getInputPort());
		this.connectPorts(instanceOfTraceMetadataFilter.getMatchedOutputPort(), traceMetadataTimestampStage.getInputPort());
		this.connectPorts(instanceOfTraceMetadataFilter.getMismatchedOutputPort(), instanceOfIEventFilter.getInputPort());
		this.connectPorts(instanceOfIEventFilter.getMatchedOutputPort(), eventRecordTimestampStage.getInputPort());
		this.connectPorts(instanceOfIEventFilter.getMismatchedOutputPort(), instanceOfIMonitoringRecord.getInputPort());
		this.connectPorts(instanceOfIMonitoringRecord.getMatchedOutputPort(), monitoringRecordTimestampStage.getInputPort());

		// Connect specific TimestampStages with Mergers
		this.connectPorts(eventRecordTimestampStage.getRecordWithinTimePeriodOutputPort(), recordsWithinTimePeriodMerger.getNewInputPort());
		this.connectPorts(eventRecordTimestampStage.getRecordOutsideTimePeriodOutputPort(), recordsOutsideTimePeriodMerger.getNewInputPort());
		this.connectPorts(operationExecutionRecordTimestampStage.getRecordWithinTimePeriodOutputPort(), recordsWithinTimePeriodMerger.getNewInputPort());
		this.connectPorts(operationExecutionRecordTimestampStage.getRecordOutsideTimePeriodOutputPort(), recordsOutsideTimePeriodMerger.getNewInputPort());
		this.connectPorts(traceMetadataTimestampStage.getRecordWithinTimePeriodOutputPort(), recordsWithinTimePeriodMerger.getNewInputPort());
		this.connectPorts(traceMetadataTimestampStage.getRecordOutsideTimePeriodOutputPort(), recordsOutsideTimePeriodMerger.getNewInputPort());
		this.connectPorts(monitoringRecordTimestampStage.getRecordWithinTimePeriodOutputPort(), recordsWithinTimePeriodMerger.getNewInputPort());
		this.connectPorts(monitoringRecordTimestampStage.getRecordOutsideTimePeriodOutputPort(), recordsOutsideTimePeriodMerger.getNewInputPort());
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
