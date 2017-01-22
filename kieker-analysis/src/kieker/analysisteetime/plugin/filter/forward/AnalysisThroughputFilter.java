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

package kieker.analysisteetime.plugin.filter.forward;

import java.util.concurrent.TimeUnit;

import kieker.analysis.display.PlainText;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * An instance of this class computes the throughput in terms of the number of objects received per time unit.
 *
 * @author Jan Waller, Lars Bluemke
 *
 * @since 1.8
 */
public class AnalysisThroughputFilter extends AbstractStage {

	private final InputPort<IMonitoringRecord> recordsInputPort = this.createInputPort();
	private final InputPort<Long> timestampsInputPort = this.createInputPort();
	private final OutputPort<IMonitoringRecord> recordsOutputPort = this.createOutputPort();
	private final OutputPort<Long> recordsCountOutputPort = this.createOutputPort();

	private long numPassedElements;
	private long lastTimestampInNs;

	private final PlainText plainTextDisplayObject = new PlainText();

	/**
	 * Default constructor.
	 */
	public AnalysisThroughputFilter() {
		// empty default constructor
	}

	@Override
	protected void execute() {
		int failt = 0;

		final IMonitoringRecord record = this.recordsInputPort.receive();
		if (record != null) {
			this.numPassedElements++;
			this.recordsOutputPort.send(record);
		} else {
			failt++;
		}

		final Long timestampInNs = this.timestampsInputPort.receive();
		if (timestampInNs != null) {
			final long duration = timestampInNs - this.lastTimestampInNs;
			final StringBuilder sb = new StringBuilder(256);
			sb.append(this.numPassedElements);
			sb.append(" objects within ");
			sb.append(duration);
			sb.append(' ');
			sb.append(TimeUnit.NANOSECONDS.toString());
			this.plainTextDisplayObject.setText(sb.toString());
			this.recordsCountOutputPort.send(this.numPassedElements);
			this.resetTimestamp(timestampInNs);

			// Manually terminate the stage when the records input port received a termination signal.
			if (this.recordsInputPort.isClosed()) {
				this.terminateStage();
			}
		} else {
			failt++;
		}

		// Enable TeeTime to suspend the stage for some time when no input is received.
		if (failt == 2) {
			this.returnNoElement();
		}
	}

	@Override
	public void onStarting() throws Exception { // NOPMD
		super.onStarting();
		this.resetTimestamp(System.nanoTime());
	}

	private void resetTimestamp(final Long timestampInNs) {
		this.numPassedElements = 0;
		this.lastTimestampInNs = timestampInNs;
	}

	public InputPort<IMonitoringRecord> getRecordsInputPort() {
		return this.recordsInputPort;
	}

	public InputPort<Long> getTimestampsInputPort() {
		return this.timestampsInputPort;
	}

	public OutputPort<IMonitoringRecord> getRecordsOutputPort() {
		return this.recordsOutputPort;
	}

	public OutputPort<Long> getRecordsCountOutputPort() {
		return this.recordsCountOutputPort;
	}

}
