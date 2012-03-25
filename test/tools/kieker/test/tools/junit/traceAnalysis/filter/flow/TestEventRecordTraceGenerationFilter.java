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

package kieker.test.tools.junit.traceAnalysis.filter.flow;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.analysis.AnalysisController;
import kieker.common.configuration.Configuration;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.Trace;
import kieker.test.analysis.junit.plugin.SimpleSinkPlugin;
import kieker.test.tools.junit.traceAnalysis.util.BookstoreEventRecordFactory;
import kieker.tools.traceAnalysis.filter.flow.EventRecordTrace;
import kieker.tools.traceAnalysis.filter.flow.EventRecordTraceGenerationFilter;

import org.junit.Test;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public class TestEventRecordTraceGenerationFilter extends TestCase {

	// TODO: Continue this test in terms of timing constellations

	private static final String SESSION_ID = "8yWpCvrJ2";
	private static final String HOSTNAME = "srv55";

	/**
	 * Creates an {@link EventRecordTraceGenerationFilter} with the given parameter.
	 * 
	 * @param maxTraceDurationMillis
	 * @return
	 */
	private static EventRecordTraceGenerationFilter createFilter(final long maxTraceDurationMillis) {
		final Configuration cfg = new Configuration();
		cfg.setProperty(EventRecordTraceGenerationFilter.CONFIG_PROPERTY_NAME_MAX_TRACE_DURATION_MILLIS, Long.toString(maxTraceDurationMillis));
		return new EventRecordTraceGenerationFilter(cfg);
	}

	@Test
	public void testTraceShorterThanMaxDurationPasses() {
		final long traceId = 978668l; // NOCS (MagicNumberCheck)
		final long startTime = 86756587l; // NOCS (MagicNumberCheck)

		final EventRecordTrace bookstoreTrace = BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(startTime, traceId,
				TestEventRecordTraceGenerationFilter.SESSION_ID, TestEventRecordTraceGenerationFilter.HOSTNAME);
		Assert.assertEquals("Test invalid", startTime, bookstoreTrace.eventList().get(0).getTimestamp());
		final long traceDuration = bookstoreTrace.eventList().get(bookstoreTrace.eventList().size() - 1).getTimestamp() - startTime;
		final AnalysisController controller = new AnalysisController();

		final EventRecordTraceGenerationFilter traceFilter = TestEventRecordTraceGenerationFilter.createFilter(traceDuration + 1);
		final SimpleSinkPlugin sinkPlugin = new SimpleSinkPlugin(new Configuration());

		Assert.assertTrue(sinkPlugin.getList().isEmpty());

		controller.registerFilter(traceFilter);
		controller.registerFilter(sinkPlugin);

		controller.connect(traceFilter, EventRecordTraceGenerationFilter.OUTPUT_PORT_NAME_TRACE, sinkPlugin, SimpleSinkPlugin.INPUT_PORT_NAME);

		traceFilter.inputTraceEvent(new Trace(traceId, traceId, TestEventRecordTraceGenerationFilter.SESSION_ID, TestEventRecordTraceGenerationFilter.HOSTNAME,
				Trace.NO_PARENT_TRACEID, Trace.NO_PARENT_ORDER_INDEX));
		for (final AbstractTraceEvent e : bookstoreTrace) {
			traceFilter.inputTraceEvent(e);
		}
		traceFilter.terminate(false); // terminate w/o error; otherwise end of trace not triggered

		// Make sure that 1 trace generated
		Assert.assertEquals("No trace passed filter", sinkPlugin.getList().size(), 1); // NOCS (MagicNumberCheck)
		final EventRecordTrace outputTrace = (EventRecordTrace) sinkPlugin.getList().get(0);
		final List<AbstractTraceEvent> outputEvents = outputTrace.eventList();

		Assert.assertEquals("Unexpected trace ID", traceId, outputTrace.getTraceId());
		Assert.assertEquals("Unexpected session ID", TestEventRecordTraceGenerationFilter.SESSION_ID, outputTrace.getSessionId());
		Assert.assertEquals("Unexpected session ID", TestEventRecordTraceGenerationFilter.HOSTNAME, outputTrace.getHostname());

		// Now, make sure that this trace is as expected
		Assert.assertEquals("Unexpected length of trace", bookstoreTrace.eventList().size(), outputEvents.size());

		Assert.assertTrue("Expecting event lists to be equal", Arrays.equals(bookstoreTrace.eventList().toArray(), outputEvents.toArray()));
	}
}
