/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.stage.events;

import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * An instance of this class computes the throughput in terms of the number of records logged within the monitoring instance per time unit.
 *
 * @author Andre van Hoorn, Jan Waller, Lars Bluemke
 *
 * @since 1.8
 */
public class MonitoringThroughputFilter extends AbstractConsumerStage<IMonitoringRecord> {
	private final long intervalSize;

	private long currentInterval = -1;
	private long recordsInInterval;

	private final OutputPort<IMonitoringRecord> uncountedRecordsOutputPort = this.createOutputPort();
	private final OutputPort<Long> throughputOutputPort = this.createOutputPort();
	private final OutputPort<IMonitoringRecord> relayedRecordsOutputPort = this.createOutputPort();

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param intervalSize
	 *            Determines the interval size. Choose a proper time unit.
	 */
	public MonitoringThroughputFilter(final long intervalSize) {
		this.intervalSize = intervalSize;
	}

	@Override
	protected void execute(final IMonitoringRecord record) {
		// we assume a more or less linear order of incoming records
		final long timestamp = record.getLoggingTimestamp();
		final long interval = timestamp / this.intervalSize;

		if (interval < this.currentInterval) { // do not count records earlier than the current interval
			this.uncountedRecordsOutputPort.send(record);
		} else {
			if (interval > this.currentInterval) { // we enter a new interval
				if (this.currentInterval != -1) { // close all previous intervals if not the first interval
					this.throughputOutputPort.send(this.recordsInInterval);
					for (long i = this.currentInterval + 1; i < interval; i++) {
						this.throughputOutputPort.send(0L);
					}
				}
				this.currentInterval = interval;
				this.recordsInInterval = 0;
			}
			this.recordsInInterval = this.recordsInInterval + 1;
		}

		this.relayedRecordsOutputPort.send(record);
	}

	public OutputPort<IMonitoringRecord> getUncountedRecordsOutputPort() {
		return this.uncountedRecordsOutputPort;
	}

	public OutputPort<Long> getThroughputOutputPort() {
		return this.throughputOutputPort;
	}

	public OutputPort<IMonitoringRecord> getRelayedRecordsOutputPort() {
		return this.relayedRecordsOutputPort;
	}
}
