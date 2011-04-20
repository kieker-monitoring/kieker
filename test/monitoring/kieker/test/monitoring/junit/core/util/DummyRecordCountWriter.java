package kieker.test.monitoring.junit.core.util;

import java.util.concurrent.atomic.AtomicInteger;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.core.controller.ITimeSourceController;
import kieker.monitoring.writer.DummyWriter;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
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
 * ==================================================
 */
/**
 * A writer that simply counts the number of records of type DummyRecord received.
 * 
 * @author Andre van Hoorn, Jan Waller 
 */
public final class DummyRecordCountWriter extends DummyWriter {
	private final AtomicInteger numDummyRecords = new AtomicInteger(0);

	public DummyRecordCountWriter(final ITimeSourceController ctrl, final Configuration configuration) {
		super(configuration);
	}

	/**
	 * @return the number of records
	 */
	public final int getNumDummyRecords() {
		return this.numDummyRecords.get();
	}

	@Override
	public final boolean newMonitoringRecord(final IMonitoringRecord record) {
		if (record instanceof DummyRecord) {
			this.numDummyRecords.incrementAndGet();
		}
		return super.newMonitoringRecord(record);
	}
}
