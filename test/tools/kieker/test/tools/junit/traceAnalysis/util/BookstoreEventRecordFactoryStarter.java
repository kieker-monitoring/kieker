package kieker.test.tools.junit.traceAnalysis.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kieker.analysis.plugin.filter.flow.TraceEventRecords;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.Trace;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

import kieker.test.analysis.util.plugin.filter.flow.BookstoreEventRecordFactory;

public final class BookstoreEventRecordFactoryStarter {

	private BookstoreEventRecordFactoryStarter() {
		// nothing to see here ...
	}

	public static void main(final String[] args) {
		final IMonitoringController ctrl = MonitoringController.getInstance();

		long firstTimestamp = 7676876;
		final long firstTimestampDelta = 1000;
		final String sessionId = "BwvCqdyhw2";
		final String hostname = "srv0";
		long traceId = 688434;

		final List<IMonitoringRecord> allRecords = new ArrayList<IMonitoringRecord>();

		final TraceEventRecords validSyncTraceBeforeAfterEvents =
				BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(firstTimestamp, traceId, sessionId, hostname);
		allRecords.add(new Trace(traceId, traceId, sessionId, hostname, Trace.NO_PARENT_TRACEID, Trace.NO_PARENT_ORDER_INDEX));
		allRecords.addAll(Arrays.asList(validSyncTraceBeforeAfterEvents.getTraceEvents()));
		firstTimestamp += firstTimestampDelta;
		traceId++;
		final TraceEventRecords validSyncTraceAdditionalCallEvents =
				BookstoreEventRecordFactory.validSyncTraceAdditionalCallEvents(firstTimestamp, traceId, sessionId, hostname);
		allRecords.add(new Trace(traceId, traceId, sessionId, hostname, Trace.NO_PARENT_TRACEID, Trace.NO_PARENT_ORDER_INDEX));
		allRecords.addAll(Arrays.asList(validSyncTraceAdditionalCallEvents.getTraceEvents()));
		firstTimestamp += firstTimestampDelta;
		traceId++;
		final TraceEventRecords validSyncTraceAdditionalCallEventsGap =
				BookstoreEventRecordFactory.validSyncTraceAdditionalCallEventsGap(firstTimestamp, traceId, sessionId, hostname);
		allRecords.add(new Trace(traceId, traceId, sessionId, hostname, Trace.NO_PARENT_TRACEID, Trace.NO_PARENT_ORDER_INDEX));
		allRecords.addAll(Arrays.asList(validSyncTraceAdditionalCallEventsGap.getTraceEvents()));

		// TODO: currently not all of the trace generation methods in this class are used

		for (final IMonitoringRecord r : allRecords) {
			ctrl.newMonitoringRecord(r);
		}

		// ctrl.terminateMonitoring();
	}
}
