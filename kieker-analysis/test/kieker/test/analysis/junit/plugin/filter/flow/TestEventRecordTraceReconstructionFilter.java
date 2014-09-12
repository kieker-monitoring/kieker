/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

import kieker.test.analysis.util.plugin.filter.flow.BookstoreEventRecordFactory;
import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.6
 */
public class TestEventRecordTraceReconstructionFilter extends AbstractKiekerTest { // NOCS (test class without constructor)

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

		final ListReader<Object> reader = new ListReader<Object>(new Configuration(), controller);

		final Configuration configuration = new Configuration();
		configuration.setProperty(EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION, Long.toString(maxTraceDuration));
		configuration.setProperty(EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_TIMEOUT, Long.toString(maxTraceTimeout));
		final EventRecordTraceReconstructionFilter traceFilter = new EventRecordTraceReconstructionFilter(configuration, controller);

		final ListCollectionFilter<TraceEventRecords> sinkPlugin = new ListCollectionFilter<TraceEventRecords>(new Configuration(), controller);
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

		final ListReader<Object> reader = new ListReader<Object>(new Configuration(), controller);

		final Configuration configuration = new Configuration();
		configuration.setProperty(EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION, Long.toString(maxTraceDuration));
		configuration.setProperty(EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_TIMEOUT, Long.toString(maxTraceTimeout));
		final EventRecordTraceReconstructionFilter traceFilter = new EventRecordTraceReconstructionFilter(configuration, controller);

		final ListCollectionFilter<TraceEventRecords> sinkPlugin = new ListCollectionFilter<TraceEventRecords>(new Configuration(), controller);
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

		final ListReader<Object> reader = new ListReader<Object>(new Configuration(), controller);

		final Configuration configuration = new Configuration();
		configuration.setProperty(EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION, Long.toString(maxTraceDuration));
		configuration.setProperty(EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_TIMEOUT, Long.toString(maxTraceTimeout));
		final EventRecordTraceReconstructionFilter traceFilter = new EventRecordTraceReconstructionFilter(configuration, controller);

		final ListCollectionFilter<TraceEventRecords> sinkPlugin = new ListCollectionFilter<TraceEventRecords>(new Configuration(), controller);
		Assert.assertTrue(sinkPlugin.getList().isEmpty());
		final ListCollectionFilter<TraceEventRecords> sinkPluginFailed = new ListCollectionFilter<TraceEventRecords>(new Configuration(), controller);
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
		// System.out.println(sinkPlugin.getList().toString());
		// System.out.println(sinkPluginFailed.getList().toString());
		// Make sure that only one of the two traces is generated
		Assert.assertEquals("There should be no trace", 1, sinkPlugin.getList().size());
	}

	@Test
	public void testTraceMaxLong() throws IllegalStateException, AnalysisConfigurationException {
		final TraceEventRecords bookstoreTrace = BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(START_TIME, TRACE_ID, SESSION_ID, HOSTNAME);
		Assert.assertEquals("Test invalid", START_TIME, bookstoreTrace.getTraceEvents()[0].getTimestamp());
		this.runTest(bookstoreTrace, Long.MAX_VALUE, Long.MAX_VALUE);
	}

	@Test
	public void testTraceShorterThanMaxDurationPasses() throws IllegalStateException, AnalysisConfigurationException {
		final TraceEventRecords bookstoreTrace = BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(START_TIME, TRACE_ID, SESSION_ID, HOSTNAME);
		Assert.assertEquals("Test invalid", START_TIME, bookstoreTrace.getTraceEvents()[0].getTimestamp());
		final long traceDuration = bookstoreTrace.getTraceEvents()[bookstoreTrace.getTraceEvents().length - 1].getTimestamp() - START_TIME;
		this.runTest(bookstoreTrace, traceDuration + 1, Long.MAX_VALUE);
	}

	@Test
	public void testTraceShorterThanMaxTimeoutPasses() throws IllegalStateException, AnalysisConfigurationException {
		final TraceEventRecords bookstoreTrace = BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(START_TIME, TRACE_ID, SESSION_ID, HOSTNAME);
		Assert.assertEquals("Test invalid", START_TIME, bookstoreTrace.getTraceEvents()[0].getTimestamp());
		this.runTest(bookstoreTrace, Long.MAX_VALUE, 100);
	}

	@Test
	public void testTraceLongerThanMaxDurationPasses() throws IllegalStateException, AnalysisConfigurationException {
		final TraceEventRecords bookstoreTrace = BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(START_TIME, TRACE_ID, SESSION_ID, HOSTNAME);
		Assert.assertEquals("Test invalid", START_TIME, bookstoreTrace.getTraceEvents()[0].getTimestamp());
		final long traceDuration = bookstoreTrace.getTraceEvents()[bookstoreTrace.getTraceEvents().length - 1].getTimestamp() - START_TIME;
		this.runTestFailed(bookstoreTrace, traceDuration - 5, Long.MAX_VALUE);
	}

	@Test
	public void testTraceLongerThanMaxTimeoutPasses() throws IllegalStateException, AnalysisConfigurationException {
		final TraceEventRecords bookstoreTrace1 = BookstoreEventRecordFactory.validSyncTraceAdditionalCallEventsGap(START_TIME, TRACE_ID, SESSION_ID, HOSTNAME);
		final TraceEventRecords bookstoreTrace2 = BookstoreEventRecordFactory.validSyncTraceAdditionalCallEventsGap(START_TIME, TRACE_ID + 1, SESSION_ID, HOSTNAME);
		Assert.assertEquals("Test invalid", START_TIME, bookstoreTrace1.getTraceEvents()[0].getTimestamp());
		this.runTestFailedInterleaved(bookstoreTrace1, bookstoreTrace2, Long.MAX_VALUE, 1);
	}
}
