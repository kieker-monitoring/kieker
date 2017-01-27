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

package kieker.test.tools.junit.writeRead;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.BranchingRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.misc.EmptyRecord;

import kieker.test.analysis.util.plugin.filter.flow.BookstoreEventRecordFactory;
import kieker.test.tools.junit.writeRead.filesystem.KiekerLogDirFilter;

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
		final List<IMonitoringRecord> someEvents = new ArrayList<IMonitoringRecord>();
		final EmptyRecord EVENT0_KNOWN_TYPE = new EmptyRecord();
		final BranchingRecord EVENT1_UNKNOWN_TYPE = new BranchingRecord(-1, -1, -1);
		final EmptyRecord EVENT2_KNOWN_TYPE = new EmptyRecord();
		final BranchingRecord EVENT3_UNKNOWN_TYPE = new BranchingRecord(-1, -1, -1);

		someEvents.add(EVENT0_KNOWN_TYPE);
		someEvents.add(EVENT1_UNKNOWN_TYPE);
		someEvents.add(EVENT2_KNOWN_TYPE);
		someEvents.add(EVENT3_UNKNOWN_TYPE);
		return someEvents;
	}

	public List<IMonitoringRecord> newTestEventRecords() {
		final List<IMonitoringRecord> someEvents = new ArrayList<IMonitoringRecord>();
		final Object[] testValues1 = { 22L, 11L, 101, "BeOpEv", "BeforeOperationEvent" };
		final Object[] testValues2 = { 6L, 8L, 120, "AfOpEv", "AfterOperationEvent" };
		final Object[] testValues3 = { 10L, 12L, 150, "AfOpFaEv", "AfterOperationFailedEvent", "cause" };

		final BeforeOperationEvent testBeforeOperationEvent = new BeforeOperationEvent(testValues1);
		final AfterOperationEvent testAfterOperationEvent = new AfterOperationEvent(testValues2);
		final AfterOperationFailedEvent testAfterOperationFailedEvent = new AfterOperationFailedEvent(testValues3);

		someEvents.add(testBeforeOperationEvent);
		someEvents.add(testAfterOperationEvent);
		someEvents.add(testAfterOperationFailedEvent);

		return someEvents;
	}

	public List<IMonitoringRecord> newTestRecords() {
		final List<IMonitoringRecord> records = new ArrayList<IMonitoringRecord>();
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
