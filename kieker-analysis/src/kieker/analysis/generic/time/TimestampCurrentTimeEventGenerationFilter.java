/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.generic.time;

import kieker.common.record.misc.TimestampRecord;

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
 */
public class TimestampCurrentTimeEventGenerationFilter extends AbstractCurrentTimeEventGenerationFilter<Long> {

	/**
	 * Creates an event generator which generates time events with the given resolution in timeunits.
	 *
	 * @param timerResolution
	 *            The timer resolution used
	 */
	public TimestampCurrentTimeEventGenerationFilter(final long timerResolution) {
		super(timerResolution);
	}

	/**
	 * Evaluates the given timestamp internal current time which may lead to
	 * newly generated events via currentTimeRecordOutputPort.
	 *
	 * @param timestamp
	 *            The next timestamp.
	 */
	@Override
	protected void execute(final Long timestamp) {
		if (timestamp < 0) {
			this.logger.warn("Received timestamp value < 0: " + timestamp);
			return;
		}

		if (this.firstTimestamp == -1) {
			// Initial record
			this.maxTimestamp = timestamp;
			this.firstTimestamp = timestamp;
			this.currentTimeRecordOutputPort.send(new TimestampRecord(timestamp));
			this.currentTimeValueOutputPort.send(timestamp);
			this.mostRecentEventFired = timestamp;
		} else if (timestamp > this.maxTimestamp) {
			this.maxTimestamp = timestamp;
			// Fire timer event(s) if required.
			for (long nextTimerEventAt = this.mostRecentEventFired
					+ this.timerResolution; timestamp >= nextTimerEventAt; nextTimerEventAt = this.mostRecentEventFired + this.timerResolution) {
				this.currentTimeRecordOutputPort.send(new TimestampRecord(nextTimerEventAt));
				this.currentTimeValueOutputPort.send(nextTimerEventAt);
				this.mostRecentEventFired = nextTimerEventAt;
			}
		}
	}

}
