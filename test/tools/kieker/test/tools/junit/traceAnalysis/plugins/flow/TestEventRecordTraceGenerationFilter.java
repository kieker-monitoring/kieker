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

package kieker.test.tools.junit.traceAnalysis.plugins.flow;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.test.analysis.junit.plugin.SimpleSinkPlugin;
import kieker.test.tools.junit.traceAnalysis.util.BookstoreEventRecordFactory;
import kieker.tools.traceAnalysis.plugins.flow.EventRecordTrace;
import kieker.tools.traceAnalysis.plugins.flow.EventRecordTraceGenerationFilter;

import org.junit.Test;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public class TestEventRecordTraceGenerationFilter extends TestCase {

	// TODO: Continue this test

	/**
	 * Creates an {@link EventRecordTraceGenerationFilter} with the given parameter.
	 * 
	 * @param maxTraceDurationMillis
	 * @return
	 */
	private static EventRecordTraceGenerationFilter createFilter(final long maxTraceDurationMillis) {
		final Configuration cfg = new Configuration();
		cfg.setProperty(EventRecordTraceGenerationFilter.CONFIG_MAX_TRACE_DURATION_MILLIS, Long.toString(maxTraceDurationMillis));
		return new EventRecordTraceGenerationFilter(cfg);
	}

	@Test
	public void testTraceShorterThanMaxDurationPasses() {
		final long traceId = 978668l; // NOCS (MagicNumberCheck)
		final long startTime = 86756587l; // NOCS (MagicNumberCheck)

		final List<AbstractTraceEvent> bookstoreTrace = BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(startTime, traceId);
		Assert.assertEquals("Test invalid", startTime, bookstoreTrace.get(0).getTimestamp());
		final long traceDuration = bookstoreTrace.get(bookstoreTrace.size() - 1).getTimestamp() - startTime;

		final EventRecordTraceGenerationFilter traceFilter = TestEventRecordTraceGenerationFilter.createFilter(traceDuration + 1);
		final SimpleSinkPlugin sinkPlugin = new SimpleSinkPlugin(new Configuration());

		Assert.assertTrue(sinkPlugin.getList().isEmpty());
		AbstractPlugin.connect(traceFilter, EventRecordTraceGenerationFilter.OUTPUT_PORT_NAME, sinkPlugin, SimpleSinkPlugin.INPUT_PORT_NAME);

		for (final AbstractTraceEvent e : bookstoreTrace) {
			traceFilter.inputTraceEvent(e);
		}
		traceFilter.terminate(false); // terminate w/o error; otherwise end of trace not triggered

		// Make sure that 1 trace generated
		Assert.assertEquals("No trace passed filter", sinkPlugin.getList().size(), 1); // NOCS (MagicNumberCheck)
		final EventRecordTrace outputTrace = (EventRecordTrace) sinkPlugin.getList().get(0);
		final List<AbstractTraceEvent> outputEvents = outputTrace.eventList();

		// Now, make sure that this trace is as expected
		Assert.assertEquals("Unexpected length of trace", bookstoreTrace.size(), outputEvents.size());

		Assert.assertTrue("Expecting event lists to be equal", Arrays.equals(bookstoreTrace.toArray(), outputEvents.toArray()));
	}
}
