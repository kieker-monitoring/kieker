/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.util.bookstore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kieker.analysis.plugin.filter.flow.TraceEventRecords;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

import kieker.test.analysis.util.plugin.filter.flow.BookstoreEventRecordFactory;

/**
 * @author Jan Waller
 *
 * @since 1.6
 */
public final class BookstoreEventRecordFactoryStarter {

	/**
	 * Private constructor to avoid instantiation. *
	 */
	private BookstoreEventRecordFactoryStarter() {
		// nothing to see here ...
	}

	/**
	 * This is the main method of this starter.
	 *
	 * @param args
	 *            The command line arguments. They have currently no effect.
	 */
	public static void main(final String[] args) {
		final IMonitoringController ctrl = MonitoringController.getInstance();

		long firstTimestamp = 7676876;
		final long firstTimestampDelta = 1000;
		final String sessionId = "BwvCqdyhw2";
		final String hostname = "srv0";
		long traceId = 688434;

		final List<IMonitoringRecord> allRecords = new ArrayList<IMonitoringRecord>();

		final TraceEventRecords validSyncTraceBeforeAfterEvents = BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(firstTimestamp, traceId, sessionId,
				hostname);
		allRecords.add(new TraceMetadata(traceId, traceId, sessionId, hostname, TraceMetadata.NO_PARENT_TRACEID, TraceMetadata.NO_PARENT_ORDER_INDEX));
		allRecords.addAll(Arrays.asList(validSyncTraceBeforeAfterEvents.getTraceEvents()));
		firstTimestamp += firstTimestampDelta;
		traceId++;
		final TraceEventRecords validSyncTraceAdditionalCallEvents = BookstoreEventRecordFactory.validSyncTraceAdditionalCallEvents(firstTimestamp, traceId,
				sessionId, hostname);
		allRecords.add(new TraceMetadata(traceId, traceId, sessionId, hostname, TraceMetadata.NO_PARENT_TRACEID, TraceMetadata.NO_PARENT_ORDER_INDEX));
		allRecords.addAll(Arrays.asList(validSyncTraceAdditionalCallEvents.getTraceEvents()));
		firstTimestamp += firstTimestampDelta;
		traceId++;
		final TraceEventRecords validSyncTraceAdditionalCallEventsGap = BookstoreEventRecordFactory.validSyncTraceAdditionalCallEventsGap(firstTimestamp, traceId,
				sessionId, hostname);
		allRecords.add(new TraceMetadata(traceId, traceId, sessionId, hostname, TraceMetadata.NO_PARENT_TRACEID, TraceMetadata.NO_PARENT_ORDER_INDEX));
		allRecords.addAll(Arrays.asList(validSyncTraceAdditionalCallEventsGap.getTraceEvents()));

		for (final IMonitoringRecord r : allRecords) {
			ctrl.newMonitoringRecord(r);
		}

		// ctrl.terminateMonitoring();
	}
}
