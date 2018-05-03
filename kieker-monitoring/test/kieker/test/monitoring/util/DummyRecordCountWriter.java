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

package kieker.test.monitoring.util;

import java.util.concurrent.atomic.AtomicInteger;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.writer.dump.DumpWriter;

/**
 * A writer that simply counts the number of records of type DummyRecord received.
 *
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.3
 */
public final class DummyRecordCountWriter extends DumpWriter {
	private final AtomicInteger numDummyRecords = new AtomicInteger(0);

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this writer.
	 */
	public DummyRecordCountWriter(final Configuration configuration) {
		super(configuration);
	}

	/**
	 * @return the number of records
	 */
	public final int getNumDummyRecords() {
		return this.numDummyRecords.get();
	}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord record) {
		if (record instanceof EmptyRecord) {
			this.numDummyRecords.incrementAndGet();
		}
		super.writeMonitoringRecord(record);
	}
}
