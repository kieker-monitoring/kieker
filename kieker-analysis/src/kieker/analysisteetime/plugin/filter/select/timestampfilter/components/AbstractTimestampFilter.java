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

package kieker.analysisteetime.plugin.filter.select.timestampfilter.components;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Allows to filter monitoring records objects based on their given timestamps.
 *
 * If the received record is within the defined timestamps, the object is delivered unmodified to the nameWithinPeriodOutputPort otherwise to the
 * nameOutsidePeriodOutputPort.
 *
 * In a concrete P&F architecture this stage should be used behind TeeTime's InstanceOfFilter to forward records to either
 * {@link TraceMetadataTimestampFilter}, {@link OperationExecutionRecordTimestampFilter} or {@link MonitioringRecordTimestampFilter}.
 *
 * @author Andre van Hoorn, Jan Waller, Lars Bluemke
 *
 * @since 1.2
 *
 * @param <T>
 *            Generic type of incoming records. Subclasses use concrete types.
 */
public abstract class AbstractTimestampFilter<T> extends AbstractConsumerStage<T> {

	protected final OutputPort<T> recordWithinTimePeriodOutputPort = this.createOutputPort();
	protected final OutputPort<T> recordOutsideTimePeriodOutputPort = this.createOutputPort();

	private final long ignoreBeforeTimestamp;
	private final long ignoreAfterTimestamp;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param ignoreBeforeTimestamp
	 *            The lower limit for the time stamps of the records.
	 * @param ignoreAfterTimestamp
	 *            The upper limit for the time stamps of the records.
	 */
	public AbstractTimestampFilter(final long ignoreBeforeTimestamp, final long ignoreAfterTimestamp) {
		this.ignoreBeforeTimestamp = ignoreBeforeTimestamp;
		this.ignoreAfterTimestamp = ignoreAfterTimestamp;
	}

	/**
	 * A simple helper method which checks whether the given timestamp is in the configured limits.
	 *
	 * @param timestamp
	 *            The timestamp to be checked.
	 * @return true if and only if the given timestamp is between or equals ignoreBeforeTimestamp and ignoreAfterTimestamp.
	 */
	protected final boolean inRange(final long timestamp) {
		return (timestamp >= this.ignoreBeforeTimestamp) && (timestamp <= this.ignoreAfterTimestamp);
	}

	@Override
	protected void execute(final T record) {
		if (this.inRange(this.getRecordSpecificTimestamp(record))) {
			this.recordWithinTimePeriodOutputPort.send(record);
		} else {
			this.recordOutsideTimePeriodOutputPort.send(record);
		}
	}

	/**
	 * Returns the most accurate timestamp available for each record. This might be record.getTimestamp or record.getLoggingTimestamp.
	 *
	 * @param record
	 *            An EventRecord, OperationExecutionRecord, TraceMetadata or MonitoringRecord for example.
	 * @return Timestamp of the given record.
	 */
	protected abstract long getRecordSpecificTimestamp(T record);

	public OutputPort<T> getRecordWithinTimePeriodOutputPort() {
		return this.recordWithinTimePeriodOutputPort;
	}

	public OutputPort<T> getRecordOutsideTimePeriodOutputPort() {
		return this.recordOutsideTimePeriodOutputPort;
	}

}
