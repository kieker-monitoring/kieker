/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package kieker.tools.log.replayer.teetime;

import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * This stage controls the flow of event.
 *
 * @author Reiner Jung
 * @since 1.16
 *
 */
public class ReplayControlStage extends AbstractConsumerStage<IMonitoringRecord> {

	private long last;
	private final long unitAdjustment;
	private final OutputPort<IMonitoringRecord> outputPort = this.createOutputPort(IMonitoringRecord.class);
	private final Long delayFactor;
	private final Long showRecordCount;
	private int count;

	/**
	 * Create a delay stage.
	 *
	 * @param unitAdjustment
	 *            divisor for time values.
	 * @param delayFactor
	 *            delay factor
	 * @param showRecordCount
	 *            show record count
	 */
	public ReplayControlStage(final long unitAdjustment, final Long delayFactor, final Long showRecordCount) {
		this.unitAdjustment = unitAdjustment;
		this.delayFactor = delayFactor == null ? 1 : delayFactor;
		this.showRecordCount = showRecordCount;
	}

	@Override
	protected void execute(final IMonitoringRecord event) {
		if (this.showRecordCount != null) {
			this.count++;
			if (this.count % this.showRecordCount == 0) {
				this.logger.info("Read {} records.", this.count);
			}
		}
		if (this.last == 0) {
			this.last = event.getLoggingTimestamp();
		}
		final long now = event.getLoggingTimestamp();
		try {
			final long delay = (now - this.last) / this.unitAdjustment / this.delayFactor;
			if (delay > 0) {
				Thread.sleep(delay);
			}
			this.last = now;
			this.outputPort.send(event);
		} catch (final InterruptedException e) {
			this.logger.info("ReplayControlStage got interrupted.");
			this.workCompleted();
		}
	}

	public OutputPort<IMonitoringRecord> getOutputPort() {
		return this.outputPort;
	}

}
