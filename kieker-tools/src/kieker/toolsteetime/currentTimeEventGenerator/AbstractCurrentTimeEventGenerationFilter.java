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

package kieker.toolsteetime.currentTimeEventGenerator;

import kieker.common.record.misc.TimestampRecord;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Generates time events with a given resolution based on the timestamps of
 * incoming {@link kieker.common.record.IMonitoringRecord}s.
 *
 * <ol>
 * <li>The first record received immediately leads to a new {@link TimestampRecord} with the given timestamp.</li>
 * <li>The timestamp of the first record is stored as {@link #firstTimestamp} and future events are generated at {@link #firstTimestamp} + i *
 * {@link #timerResolution}.</li>
 * <li>Future {@link kieker.common.record.IMonitoringRecord} may lead to future {@link TimestampRecord} as follows:
 * <ol>
 * <li>A newly incoming {@link kieker.common.record.IMonitoringRecord} with logging timestamp {@literal tstamp} leads to the new timer events satisfying
 * {@link #firstTimestamp} + i * {@link #timerResolution} {@literal <} {@literal tstamp}.</li>
 * </ol>
 * </li>
 * </ol>
 *
 * It is guaranteed that the generated timestamps are in ascending order.
 *
 * @author Andre van Hoorn, Lars Bluemke
 *
 * @since 1.3
 *
 * @param <T>
 *            Type parameter for this abstract class concrete implementations may use Long or IMonitoringRecord.
 */
public abstract class AbstractCurrentTimeEventGenerationFilter<T> extends AbstractConsumerStage<T> {

	/**
	 * Timestamp of the record that was received first. Notice, that this is not
	 * necessarily the lowest timestamp.
	 */
	protected volatile long firstTimestamp = -1;

	/**
	 * Maximum timestamp received so far.
	 */
	protected volatile long maxTimestamp = -1;

	/**
	 * The timestamp of the most recent timer event.
	 */
	protected volatile long mostRecentEventFired = -1;

	/**
	 * The timer resolution used.
	 */
	protected final long timerResolution;

	/**
	 * The output port for {@link TimestampRecord}s.
	 */
	protected final OutputPort<TimestampRecord> currentTimeRecordOutputPort = this.createOutputPort();

	/**
	 * The output port for {@link Long} timestamps.
	 */
	protected final OutputPort<Long> currentTimeValueOutputPort = this.createOutputPort();

	/**
	 * Creates an event generator which generates time events with the given resolution in timeunits.
	 *
	 * @param timerResolution
	 *            The timer resolution used
	 */
	public AbstractCurrentTimeEventGenerationFilter(final long timerResolution) {
		this.timerResolution = timerResolution;
	}

	/**
	 * This method represents the input port for new records or raw timestamps.
	 *
	 * @param element
	 *            The incoming record or timestamp.
	 */
	@Override
	protected abstract void execute(final T element);

	public OutputPort<TimestampRecord> getCurrentTimeRecordOutputPort() {
		return this.currentTimeRecordOutputPort;
	}

	public OutputPort<Long> getCurrentTimeValueOutputPort() {
		return this.currentTimeValueOutputPort;
	}

}
