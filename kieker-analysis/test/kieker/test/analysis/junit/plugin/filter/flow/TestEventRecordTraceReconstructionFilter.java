/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.analysis.junit.plugin.filter.flow;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.flow.EventRecordTraceReconstructionFilter;
import kieker.analysis.plugin.filter.flow.TraceEventRecords;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.flow.trace.operation.constructor.AfterConstructorFailedEvent;
import kieker.common.record.flow.trace.operation.constructor.BeforeConstructorEvent;
import kieker.common.record.flow.trace.operation.constructor.object.AfterConstructorFailedObjectEvent;
import kieker.common.record.flow.trace.operation.constructor.object.BeforeConstructorObjectEvent;
import kieker.common.record.flow.trace.operation.object.AfterOperationFailedObjectEvent;
import kieker.common.record.flow.trace.operation.object.BeforeOperationObjectEvent;

import kieker.test.analysis.util.plugin.filter.flow.BookstoreEventRecordFactory;
import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.6
 */
public class TestEventRecordTraceReconstructionFilter extends AbstractKiekerTest { // NOPMD,NOCS (test class without constructor)

	private static final String TEST_INVALID = "Test invalid";
	private static final String SESSION_ID = "8yWpCvrJ2";
	private static final String HOSTNAME = "srv55";
	private static final long TRACE_ID = 978668L;
	private static final long START_TIME = 86756587L;

	/**
	 * Creates an {@link EventRecordTraceGenerationFilter} with the given parameter.
	 *
	 * @param maxTraceDuration
	 * @param maxTraceTimeout
	 * @return
	 * @throws AnalysisConfigurationException
	 *             If the internally assembled analysis configuration is somehow invalid.
	 * @throws IllegalStateException
	 *             If the internally assembled analysis is in an invalid state.
	 */
	private void runTest(final TraceEventRecords records, final long maxTraceDuration, final long maxTraceTimeout) throws IllegalStateException,
	AnalysisConfigurationException {
		final IAnalysisController controller = new AnalysisController();

		final ListReader<Object> reader = new ListReader<>(new Configuration(), controller);

		final Configuration configuration = new Configuration();
		configuration.setProperty(EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION, Long.toString(maxTraceDuration));
		configuration.setProperty(EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_TIMEOUT, Long.toString(maxTraceTimeout));
		final EventRecordTraceReconstructionFilter traceFilter = new EventRecordTraceReconstructionFilter(configuration, controller);

		final ListCollectionFilter<TraceEventRecords> sinkPlugin = new ListCollectionFilter<>(new Configuration(), controller);
		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.connect(reader, ListReader.OUTPUT_PORT_NAME, traceFilter, EventRecordTraceReconstructionFilter.INPUT_PORT_NAME_TRACE_RECORDS);
		controller.connect(traceFilter, EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_VALID, sinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);

		reader.addObject(records.getTraceMetadata());
		for (final AbstractTraceEvent e : records.getTraceEvents()) {
			reader.addObject(e);
		}
		controller.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, controller.getState());

		// Make sure that 1 trace generated
		Assert.assertEquals("No trace passed filter", 1, sinkPlugin.getList().size());
		Assert.assertEquals(records, sinkPlugin.getList().get(0));
	}

	private void runTestFailed(final TraceEventRecords records, final long maxTraceDuration, final long maxTraceTimeout) throws IllegalStateException,
	AnalysisConfigurationException {
		final IAnalysisController controller = new AnalysisController();

		final ListReader<Object> reader = new ListReader<>(new Configuration(), controller);

		final Configuration configuration = new Configuration();
		configuration.setProperty(EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION, Long.toString(maxTraceDuration));
		configuration.setProperty(EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_TIMEOUT, Long.toString(maxTraceTimeout));
		final EventRecordTraceReconstructionFilter traceFilter = new EventRecordTraceReconstructionFilter(configuration, controller);

		final ListCollectionFilter<TraceEventRecords> sinkPlugin = new ListCollectionFilter<>(new Configuration(), controller);
		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.connect(reader, ListReader.OUTPUT_PORT_NAME, traceFilter, EventRecordTraceReconstructionFilter.INPUT_PORT_NAME_TRACE_RECORDS);
		controller.connect(traceFilter, EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_VALID, sinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);

		reader.addObject(records.getTraceMetadata());
		for (final AbstractTraceEvent e : records.getTraceEvents()) {
			reader.addObject(e);
		}
		controller.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, controller.getState());

		// Make sure that no trace is generated
		Assert.assertEquals("There should be no trace", 0, sinkPlugin.getList().size());
	}

	private void runTestFailedInterleaved(final TraceEventRecords trace1, final TraceEventRecords trace2, final long maxTraceDuration, final long maxTraceTimeout)
			throws IllegalStateException, AnalysisConfigurationException {
		final IAnalysisController controller = new AnalysisController();

		final ListReader<Object> reader = new ListReader<>(new Configuration(), controller);

		final Configuration configuration = new Configuration();
		configuration.setProperty(EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION, Long.toString(maxTraceDuration));
		configuration.setProperty(EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_TIMEOUT, Long.toString(maxTraceTimeout));
		final EventRecordTraceReconstructionFilter traceFilter = new EventRecordTraceReconstructionFilter(configuration, controller);

		final ListCollectionFilter<TraceEventRecords> sinkPlugin = new ListCollectionFilter<>(new Configuration(), controller);
		Assert.assertTrue(sinkPlugin.getList().isEmpty());
		final ListCollectionFilter<TraceEventRecords> sinkPluginFailed = new ListCollectionFilter<>(new Configuration(), controller);
		Assert.assertTrue(sinkPluginFailed.getList().isEmpty());

		controller.connect(reader, ListReader.OUTPUT_PORT_NAME, traceFilter, EventRecordTraceReconstructionFilter.INPUT_PORT_NAME_TRACE_RECORDS);
		controller.connect(traceFilter, EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_VALID, sinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);
		controller.connect(traceFilter, EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_INVALID, sinkPluginFailed, ListCollectionFilter.INPUT_PORT_NAME);

		reader.addObject(trace1.getTraceMetadata());
		reader.addObject(trace2.getTraceMetadata());
		final AbstractTraceEvent[] events1 = trace1.getTraceEvents();
		final AbstractTraceEvent[] events2 = trace2.getTraceEvents();
		for (int i = 0; i < events1.length; i++) {
			reader.addObject(events1[i]);
			reader.addObject(events2[i]);
		}
		controller.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, controller.getState());
		// Make sure that only one of the two traces is generated
		Assert.assertEquals("There should be no trace", 1, sinkPlugin.getList().size());
	}

	private void runTestEventBasedTraceRepair(final TraceEventRecords records, final long maxTraceDuration, final long maxTraceTimeout)
			throws IllegalStateException,
			AnalysisConfigurationException {
		final IAnalysisController controller = new AnalysisController();

		final ListReader<Object> reader = new ListReader<>(new Configuration(), controller);

		final Configuration configuration = new Configuration();
		configuration.setProperty(EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION, Long.toString(maxTraceDuration));
		configuration.setProperty(EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_TIMEOUT, Long.toString(maxTraceTimeout));
		configuration.setProperty(EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_REPAIR_EVENT_BASED_TRACES, "true");
		final EventRecordTraceReconstructionFilter traceFilter = new EventRecordTraceReconstructionFilter(configuration, controller);

		final ListCollectionFilter<TraceEventRecords> sinkPlugin = new ListCollectionFilter<>(new Configuration(), controller);
		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.connect(reader, ListReader.OUTPUT_PORT_NAME, traceFilter, EventRecordTraceReconstructionFilter.INPUT_PORT_NAME_TRACE_RECORDS);
		controller.connect(traceFilter, EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_VALID, sinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);

		reader.addObject(records.getTraceMetadata());
		for (final AbstractTraceEvent e : records.getTraceEvents()) {
			reader.addObject(e);
		}
		controller.run();

		Assert.assertEquals(AnalysisController.STATE.TERMINATED, controller.getState());
		Assert.assertEquals("No trace passed filter", 1, sinkPlugin.getList().size());
		Assert.assertTrue("Repair failed", records.getTraceEvents().length < sinkPlugin.getList().get(0).getTraceEvents().length);
		Assert.assertEquals("Did not repair all BeforeEvents", sinkPlugin.getList().get(0).getTraceEvents().length, 10);
	}

	@Test
	public void testTraceMaxLong() throws IllegalStateException, AnalysisConfigurationException {
		final TraceEventRecords bookstoreTrace = BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(START_TIME, TRACE_ID, SESSION_ID, HOSTNAME);
		Assert.assertEquals(TEST_INVALID, START_TIME, bookstoreTrace.getTraceEvents()[0].getTimestamp());
		this.runTest(bookstoreTrace, Long.MAX_VALUE, Long.MAX_VALUE);
	}

	@Test
	public void testTraceShorterThanMaxDurationPasses() throws IllegalStateException, AnalysisConfigurationException {
		final TraceEventRecords bookstoreTrace = BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(START_TIME, TRACE_ID, SESSION_ID, HOSTNAME);
		Assert.assertEquals(TEST_INVALID, START_TIME, bookstoreTrace.getTraceEvents()[0].getTimestamp());
		final long traceDuration = bookstoreTrace.getTraceEvents()[bookstoreTrace.getTraceEvents().length - 1].getTimestamp() - START_TIME;
		this.runTest(bookstoreTrace, traceDuration + 1, Long.MAX_VALUE);
	}

	@Test
	public void testTraceShorterThanMaxTimeoutPasses() throws IllegalStateException, AnalysisConfigurationException {
		final TraceEventRecords bookstoreTrace = BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(START_TIME, TRACE_ID, SESSION_ID, HOSTNAME);
		Assert.assertEquals(TEST_INVALID, START_TIME, bookstoreTrace.getTraceEvents()[0].getTimestamp());
		this.runTest(bookstoreTrace, Long.MAX_VALUE, 100);
	}

	@Test
	public void testTraceLongerThanMaxDurationPasses() throws IllegalStateException, AnalysisConfigurationException {
		final TraceEventRecords bookstoreTrace = BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(START_TIME, TRACE_ID, SESSION_ID, HOSTNAME);
		Assert.assertEquals(TEST_INVALID, START_TIME, bookstoreTrace.getTraceEvents()[0].getTimestamp());
		final long traceDuration = bookstoreTrace.getTraceEvents()[bookstoreTrace.getTraceEvents().length - 1].getTimestamp() - START_TIME;
		this.runTestFailed(bookstoreTrace, traceDuration - 5, Long.MAX_VALUE);
	}

	@Test
	public void testTraceLongerThanMaxTimeoutPasses() throws IllegalStateException, AnalysisConfigurationException {
		final TraceEventRecords bookstoreTrace1 = BookstoreEventRecordFactory.validSyncTraceAdditionalCallEventsGap(START_TIME, TRACE_ID, SESSION_ID, HOSTNAME);
		final TraceEventRecords bookstoreTrace2 = BookstoreEventRecordFactory.validSyncTraceAdditionalCallEventsGap(START_TIME, TRACE_ID + 1, SESSION_ID, HOSTNAME);
		Assert.assertEquals(TEST_INVALID, START_TIME, bookstoreTrace1.getTraceEvents()[0].getTimestamp());
		this.runTestFailedInterleaved(bookstoreTrace1, bookstoreTrace2, Long.MAX_VALUE, 1);
	}

	@Test
	public void testEventBasedTraceRepairWithoutAnyAfterEvent() throws IllegalStateException, AnalysisConfigurationException {
		final TraceEventRecords brokenTrace = this.brokenEventsWithoutAfterEvents(START_TIME, TRACE_ID, SESSION_ID, HOSTNAME);
		Assert.assertEquals(TEST_INVALID, START_TIME, brokenTrace.getTraceEvents()[0].getTimestamp());
		this.runTestEventBasedTraceRepair(brokenTrace, Long.MAX_VALUE, Long.MAX_VALUE);
	}

	@Test
	public void testEventBasedTraceRepairWithAfterEventAtEnd() throws IllegalStateException, AnalysisConfigurationException {
		final TraceEventRecords brokenTrace = this.brokenEventsWithAfterEventsAtEnd(START_TIME, TRACE_ID, SESSION_ID, HOSTNAME);
		Assert.assertEquals(TEST_INVALID, START_TIME, brokenTrace.getTraceEvents()[0].getTimestamp());
		this.runTestEventBasedTraceRepair(brokenTrace, Long.MAX_VALUE, Long.MAX_VALUE);
	}

	public TraceEventRecords brokenEventsWithoutAfterEvents(final long firstTimestamp, final long traceId, final String sessionId,
			final String hostname) {
		int curOrderIndex = 0;
		final int objectID = 5166;

		final BeforeConstructorObjectEvent entry0; // NOCS
		final BeforeConstructorEvent entry1; // NOCS
		final BeforeOperationObjectEvent entry2; // NOCS
		final BeforeOperationEvent entry3; // NOCS
		final BeforeOperationEvent entry4; // NOCS

		entry0 = new BeforeConstructorObjectEvent(firstTimestamp, traceId, curOrderIndex++, "opSignature0", "myClass0", objectID);
		entry1 = new BeforeConstructorEvent(firstTimestamp + 1, traceId, curOrderIndex++, "opSignature1", "myClass1");
		entry2 = new BeforeOperationObjectEvent(firstTimestamp + 2, traceId, curOrderIndex++, "opSignature2", "myClass2", objectID + 1);
		entry3 = new BeforeOperationEvent(firstTimestamp + 3, traceId, curOrderIndex++, "opSignature3", "myClass3");
		entry4 = new BeforeOperationEvent(firstTimestamp + 4, traceId, curOrderIndex++, "opSignature4", "myClass4");

		final TraceMetadata trace = new TraceMetadata(traceId, -1, sessionId, hostname, -1, -1);
		final AbstractTraceEvent[] events = new AbstractTraceEvent[] {
			entry0,
			entry1,
			entry2,
			entry3,
			entry4,
		};
		return new TraceEventRecords(trace, events);
	}

	public TraceEventRecords brokenEventsWithAfterEventsAtEnd(final long firstTimestamp, final long traceId, final String sessionId,
			final String hostname) {
		int curOrderIndex = 0;
		final int objectID = 5166;

		final BeforeConstructorObjectEvent entry0; // NOCS
		final BeforeConstructorEvent entry1; // NOCS
		final BeforeOperationObjectEvent entry2; // NOCS
		final BeforeOperationEvent entry3; // NOCS
		final BeforeOperationEvent entry4; // NOCS

		final AfterConstructorFailedObjectEvent exit0; // NOCS
		final AfterConstructorFailedEvent exit1; // NOCS
		final AfterOperationFailedObjectEvent exit2; // NOCS
		final AfterOperationFailedEvent exit3; // NOCS

		entry0 = new BeforeConstructorObjectEvent(firstTimestamp, traceId, curOrderIndex++, "opSignature0", "myClass0", objectID);
		entry1 = new BeforeConstructorEvent(firstTimestamp + 1, traceId, curOrderIndex++, "opSignature1", "myClass1");
		entry2 = new BeforeOperationObjectEvent(firstTimestamp + 2, traceId, curOrderIndex++, "opSignature2", "myClass2", objectID + 1);
		entry3 = new BeforeOperationEvent(firstTimestamp + 3, traceId, curOrderIndex++, "opSignature3", "myClass3");
		entry4 = new BeforeOperationEvent(firstTimestamp + 4, traceId, curOrderIndex++, "opSignature4", "myClass4");

		exit3 = new AfterOperationFailedEvent(firstTimestamp + 5, traceId, curOrderIndex++, "opSignature3", "myClass3", "myCause");
		exit2 = new AfterOperationFailedObjectEvent(firstTimestamp + 6, traceId, curOrderIndex++, "opSignature2", "myClass2", "myCause", objectID + 1);
		exit1 = new AfterConstructorFailedEvent(firstTimestamp + 7, traceId, curOrderIndex++, "opSignature1", "myClass1", "myCause");
		exit0 = new AfterConstructorFailedObjectEvent(firstTimestamp + 8, traceId, curOrderIndex++, "opSignature0", "myClass0", "myCause", objectID);

		final TraceMetadata trace = new TraceMetadata(traceId, -1, sessionId, hostname, -1, -1);
		final AbstractTraceEvent[] events = new AbstractTraceEvent[] {
			entry0,
			entry1,
			entry2,
			entry3,
			entry4,
			exit3,
			exit2,
			exit1,
			exit0,
		};
		return new TraceEventRecords(trace, events);
	}
}
