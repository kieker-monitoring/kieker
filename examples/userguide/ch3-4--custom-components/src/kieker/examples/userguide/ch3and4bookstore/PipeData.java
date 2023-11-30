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

package kieker.examples.userguide.ch3and4bookstore;

import kieker.common.record.IMonitoringRecord;

public class PipeData {

	private final long loggingTimestamp;
	private final Object[] recordData;
	private final Class<? extends IMonitoringRecord> recordType;

	public PipeData(final long loggingTimestamp, final Object[] recordData, final Class<? extends IMonitoringRecord> recordType) {
		this.loggingTimestamp = loggingTimestamp;
		this.recordData = recordData; // in real settings we would clone
		this.recordType = recordType;
	}

	public final long getLoggingTimestamp() {
		return this.loggingTimestamp;
	}

	public final Object[] getRecordData() {
		return this.recordData; // in real settings we would clone
	}

	public Class<? extends IMonitoringRecord> getRecordType() {
		return this.recordType;
	}
}
