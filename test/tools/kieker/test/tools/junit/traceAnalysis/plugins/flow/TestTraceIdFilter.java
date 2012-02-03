/***************************************************************************
 * Copyright 2011 by
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

import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.common.record.flow.TraceEvent;
import kieker.test.tools.junit.traceAnalysis.util.BookstoreEventRecordFactory;
import kieker.test.tools.junit.traceAnalysis.util.SimpleSinkPlugin;
import kieker.tools.traceAnalysis.plugins.flow.TraceIdFilter;

import org.junit.Test;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public class TestTraceIdFilter extends TestCase {

	/**
	 * Given a TraceIdFilter that passes traceIds included in a set <i>idsToPass</i>,
	 * assert that a {@link TraceEvent} object <i>event</i> with traceId not element of
	 * <i>idsToPass</i> is NOT passed through the filter.
	 */
	@Test
	public void testAssertIgnoreTraceId() {
		final long firstTimestamp = 42353; // any number fits // NOCS (MagicNumberCheck)
		final long traceIdNotToPass = 11l; // (must NOT be element of idsToPass) // NOCS (MagicNumberCheck)

		final NavigableSet<Long> idsToPass = new TreeSet<Long>();
		idsToPass.add(1 + traceIdNotToPass); // NOCS (MagicNumberCheck)
		idsToPass.add(2 + traceIdNotToPass); // NOCS (MagicNumberCheck)

		final TraceIdFilter filter = new TraceIdFilter(idsToPass);
		final SimpleSinkPlugin sinkPlugin = new SimpleSinkPlugin();

		final List<TraceEvent> trace =
				BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(firstTimestamp, traceIdNotToPass); // NOCS (MagicNumberCheck)

		Assert.assertTrue(sinkPlugin.getList().isEmpty());
		AbstractPlugin.connect(filter, TraceIdFilter.OUTPUT_PORT_NAME, sinkPlugin, SimpleSinkPlugin.INPUT_PORT_NAME);

		for (final TraceEvent e : trace) {
			Assert.assertTrue("Testcase invalid", !idsToPass.contains(e.getTraceId()));
			filter.inputTraceEvent(e);
		}

		if (!sinkPlugin.getList().isEmpty()) {
			final long passedId = ((TraceEvent) sinkPlugin.getList().get(0)).getTraceId();
			Assert.fail(
					"Filter passed trace with ID " + passedId + " although traceId not element of " + idsToPass);
		}
	}

	/**
	 * Given a TraceIdFilter that passes traceIds included in a set <i>idsToPass</i>,
	 * assert that a {@link TraceEvent} object <i>event</i> with traceId not element of
	 * <i>idsToPass</i> IS passed through the filter.
	 */
	@Test
	public void testAssertPassTraceId() {
		final long firstTimestamp = 53222; // any number fits // NOCS (MagicNumberCheck)
		final long traceIdToPass = 11l; // (must be element of idsToPass) // NOCS (MagicNumberCheck)

		final NavigableSet<Long> idsToPass = new TreeSet<Long>();
		idsToPass.add(0 + traceIdToPass); // NOCS (MagicNumberCheck)
		idsToPass.add(1 + traceIdToPass); // NOCS (MagicNumberCheck)

		final TraceIdFilter filter = new TraceIdFilter(idsToPass);
		final SimpleSinkPlugin sinkPlugin = new SimpleSinkPlugin();

		final List<TraceEvent> trace =
				BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(firstTimestamp, traceIdToPass); // NOCS (MagicNumberCheck)

		Assert.assertTrue(sinkPlugin.getList().isEmpty());
		AbstractPlugin.connect(filter, TraceIdFilter.OUTPUT_PORT_NAME, sinkPlugin, SimpleSinkPlugin.INPUT_PORT_NAME);

		for (final TraceEvent e : trace) {
			Assert.assertTrue("Testcase invalid", idsToPass.contains(e.getTraceId()));
			filter.inputTraceEvent(e);
			Assert.assertTrue("Expected event " + e + " to pass the filter", sinkPlugin.getList().contains(e));
		}
		// Somehow redundant but records MIGHT be generated randomly ;-)
		Assert.assertEquals("Unexpected number of output records", sinkPlugin.getList().size(), trace.size());
	}
}
