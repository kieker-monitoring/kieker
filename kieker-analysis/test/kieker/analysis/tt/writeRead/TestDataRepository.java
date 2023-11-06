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

package kieker.analysis.tt.writeRead;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kieker.analysis.tt.writeRead.filesystem.KiekerLogDirFilter;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.BranchingRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.misc.EmptyRecord;

import kieker.test.analysis.util.plugin.filter.flow.BookstoreEventRecordFactory;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class TestDataRepository {

	private static final String DEFAULT_EVENTS_SESSION_ID = "Mn51D97t0";
	private static final String DEFAULT_EVENTS_HOSTNAME = "srv-LURS0EMw";
	private static final int DEFAULT_EVENTS_NUMBER = 5; // just a basic test with (potentially) at bit more than a hand full of records

	public TestDataRepository() {
		super();
	}

	public List<IMonitoringRecord> newTestUnknownRecords() {
		final List<IMonitoringRecord> someEvents = new ArrayList<>();
		someEvents.add(new EmptyRecord()); // event with known type
		someEvents.add(new BranchingRecord(-1, -1, -1)); // event with unknown type
		someEvents.add(new EmptyRecord()); // event with known type
		someEvents.add(new BranchingRecord(-1, -1, -1)); // event with unknown type
		return someEvents;
	}

	public List<IMonitoringRecord> newTestEventRecords() {
		final List<IMonitoringRecord> someEvents = new ArrayList<>();

		final BeforeOperationEvent testBeforeOperationEvent = new BeforeOperationEvent(22L, 11L, 101, "BeOpEv", "BeforeOperationEvent");
		final AfterOperationEvent testAfterOperationEvent = new AfterOperationEvent(6L, 8L, 120, "AfOpEv", "AfterOperationEvent");
		final AfterOperationFailedEvent testAfterOperationFailedEvent = new AfterOperationFailedEvent(10L, 12L, 150, "AfOpFaEv", "AfterOperationFailedEvent",
				"cause");

		someEvents.add(testBeforeOperationEvent);
		someEvents.add(testAfterOperationEvent);
		someEvents.add(testAfterOperationFailedEvent);

		return someEvents;
	}

	/**
	 * @return 10 (9+1) records
	 */
	public List<IMonitoringRecord> newTestRecords() {
		final List<IMonitoringRecord> records = new ArrayList<>();
		for (int i = 0; i < DEFAULT_EVENTS_NUMBER; i = records.size()) {
			final AbstractTraceEvent[] events = BookstoreEventRecordFactory.validSyncTraceAdditionalCallEventsGap(
					i, i, DEFAULT_EVENTS_SESSION_ID, DEFAULT_EVENTS_HOSTNAME).getTraceEvents();
			records.addAll(Arrays.asList(events));
		}
		records.add(new EmptyRecord()); // this record used to cause problems (#475)
		return records;
	}

	public String[] getAbsoluteMonitoringLogDirNames(final File folder) {
		final String[] monitoringLogs = folder.list(new KiekerLogDirFilter());
		if (monitoringLogs == null) {
			throw new IllegalArgumentException("The given folder does not contain any Kieker log files: " + folder);
		}
		for (int i = 0; i < monitoringLogs.length; i++) { // transform relative to absolute path
			monitoringLogs[i] = folder.getAbsolutePath() + File.separator + monitoringLogs[i]; // NOPMD (UseStringBufferForStringAppends)
		}
		return monitoringLogs;
	}
}
