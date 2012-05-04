/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.flow.EventRecordTraceReconstructionFilter;
import kieker.analysis.plugin.filter.flow.TraceEventRecords;
import kieker.common.configuration.Configuration;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.Trace;

import kieker.test.analysis.util.plugin.filter.SimpleSinkFilter;
import kieker.test.analysis.util.plugin.filter.flow.BookstoreEventRecordFactory;

/**
 * @author Andre van Hoorn, Jan Waller
 */
public class TestEventRecordTraceReconstructionFilter { // NOCS (test class without constructor)

	private static final String SESSION_ID = "8yWpCvrJ2";
	private static final String HOSTNAME = "srv55";

	// TODO add tests with other trace durations ...

	/**
	 * Creates an {@link EventRecordTraceGenerationFilter} with the given parameter.
	 * 
	 * @param maxTraceDuration
	 * @return
	 */
	private static EventRecordTraceReconstructionFilter createFilter(final long maxTraceDuration) {
		final Configuration cfg = new Configuration();
		cfg.setProperty(EventRecordTraceReconstructionFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION, Long.toString(maxTraceDuration));
		return new EventRecordTraceReconstructionFilter(cfg);
	}

	@Test
	public void testTraceShorterThanMaxDurationPasses() throws IllegalStateException, AnalysisConfigurationException {
		final long traceId = 978668L;
		final long startTime = 86756587L;

		final TraceEventRecords bookstoreTrace = BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(startTime, traceId, SESSION_ID, HOSTNAME);
		Assert.assertEquals("Test invalid", startTime, bookstoreTrace.getTraceEvents()[0].getTimestamp());
		final long traceDuration = bookstoreTrace.getTraceEvents()[bookstoreTrace.getTraceEvents().length - 1].getTimestamp() - startTime;
		final AnalysisController controller = new AnalysisController();

		final EventRecordTraceReconstructionFilter traceFilter = TestEventRecordTraceReconstructionFilter.createFilter(traceDuration + 1);
		final SimpleSinkFilter<TraceEventRecords> sinkPlugin = new SimpleSinkFilter<TraceEventRecords>(new Configuration());

		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.registerFilter(traceFilter);
		controller.registerFilter(sinkPlugin);

		controller.connect(traceFilter, EventRecordTraceReconstructionFilter.OUTPUT_PORT_NAME_TRACE_VALID, sinkPlugin, SimpleSinkFilter.INPUT_PORT_NAME);

		traceFilter.newEvent(new Trace(traceId, traceId, SESSION_ID, HOSTNAME, Trace.NO_PARENT_TRACEID, Trace.NO_PARENT_ORDER_INDEX));
		for (final AbstractTraceEvent e : bookstoreTrace.getTraceEvents()) {
			traceFilter.newEvent(e);
		}
		traceFilter.terminate(false); // terminate w/o error; otherwise end of trace not triggered

		// Make sure that 1 trace generated
		Assert.assertEquals("No trace passed filter", sinkPlugin.getList().size(), 1);
		final TraceEventRecords outputTrace = sinkPlugin.getList().get(0);
		final AbstractTraceEvent[] outputEvents = outputTrace.getTraceEvents();

		Assert.assertEquals("Unexpected trace ID", traceId, outputTrace.getTrace().getTraceId());
		Assert.assertEquals("Unexpected session ID", SESSION_ID, outputTrace.getTrace().getSessionId());
		Assert.assertEquals("Unexpected session ID", HOSTNAME, outputTrace.getTrace().getHostname());

		// Now, make sure that this trace is as expected
		Assert.assertEquals("Unexpected length of trace", bookstoreTrace.getTraceEvents().length, outputEvents.length);

		Assert.assertTrue("Expecting event lists to be equal", Arrays.equals(bookstoreTrace.getTraceEvents(), outputEvents));
	}
}
