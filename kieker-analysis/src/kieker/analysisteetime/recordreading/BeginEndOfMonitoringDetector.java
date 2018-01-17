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

package kieker.analysisteetime.recordreading;

import kieker.common.record.IMonitoringRecord;

import teetime.stage.basic.AbstractTransformation;

/**
 * @author Nils Christian Ehmke
 */
public final class BeginEndOfMonitoringDetector extends AbstractTransformation<IMonitoringRecord, IMonitoringRecord> {

	private long beginTimestamp = Long.MAX_VALUE;
	private long endTimestamp = 0;

	@Override
	protected void execute(final IMonitoringRecord record) {
		final long loggingTimestamp = record.getLoggingTimestamp();

		if (loggingTimestamp < this.beginTimestamp) {
			this.beginTimestamp = loggingTimestamp;
		}
		if (loggingTimestamp > this.endTimestamp) {
			this.endTimestamp = loggingTimestamp;
		}

		super.getOutputPort().send(record);
	}

	public long getBeginTimestamp() {
		return this.beginTimestamp;
	}

	public long getEndTimestamp() {
		return this.endTimestamp;
	}

}
