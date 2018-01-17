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

package kieker.test.analysisteetime.junit.plugin.filter.select;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysisteetime.plugin.filter.select.traceidfilter.TraceIdFilter;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;

import kieker.test.analysis.util.plugin.filter.flow.BookstoreEventRecordFactory;
import kieker.test.common.junit.AbstractKiekerTest;

import teetime.framework.AbstractStage;
import teetime.framework.test.StageTester;

/**
 * @author Andre van Hoorn, Lars Bluemke
 *
 * @since 1.5
 */
public class TestTraceIdFilter extends AbstractKiekerTest {

	private static final String SESSION_ID = "sv7w1ifhK";
	private static final String HOSTNAME = "srv098";

	/**
	 * Empty default constructor
	 */
	public TestTraceIdFilter() {
		// empty default constructor
	}

	/**
	 * Given a TraceIdFilter that passes traceIds included in a set <i>idsToPass</i>,
	 * assert that a {@link AbstractTraceEvent} object <i>event</i> with traceId not element of
	 * <i>idsToPass</i> is NOT passed through the filter.
	 */
	@Test
	public void testAssertIgnoreTraceId() {
		final long firstTimestamp = 42353; // any number fits
		final long traceIdNotToPass = 11L; // (must NOT be element of idsToPass)

		final SortedSet<Long> idsToPass = new TreeSet<Long>();
		idsToPass.add(1 + traceIdNotToPass);
		idsToPass.add(2 + traceIdNotToPass);

		final TraceIdFilter traceidFilter = new TraceIdFilter(false, idsToPass.toArray(new Long[idsToPass.size()]));
		final AbstractStage owningStage = traceidFilter.getMonitoringRecordsCombinedInputPort().getOwningStage();
		final List<IMonitoringRecord> acceptedTraceidOutputs = new ArrayList<IMonitoringRecord>();
		final List<IMonitoringRecord> notAcceptedTraceidOutputs = new ArrayList<IMonitoringRecord>();

		final AbstractTraceEvent[] traceEvents = BookstoreEventRecordFactory
				.validSyncTraceBeforeAfterEvents(firstTimestamp, traceIdNotToPass, TestTraceIdFilter.SESSION_ID, TestTraceIdFilter.HOSTNAME)
				.getTraceEvents();

		for (final AbstractTraceEvent e : traceEvents) {
			Assert.assertTrue("Testcase invalid", !idsToPass.contains(e.getTraceId()));
		}

		StageTester.test(owningStage)
				.and().send(traceEvents).to(traceidFilter.getMonitoringRecordsCombinedInputPort())
				.and().receive(acceptedTraceidOutputs).from(traceidFilter.getMatchingTraceIdOutputPort())
				.and().receive(notAcceptedTraceidOutputs).from(traceidFilter.getMismatchingTraceIdOutputPort())
				.start();

		if (!acceptedTraceidOutputs.isEmpty()) {
			final long passedId = ((AbstractTraceEvent) acceptedTraceidOutputs.get(0)).getTraceId();
			Assert.fail("Filter passed trace with ID " + passedId + " although traceId not element of " + idsToPass);
		}

		Assert.assertSame(notAcceptedTraceidOutputs.get(0), traceEvents[0]);
	}

	/**
	 * Given a TraceIdFilter that passes traceIds included in a set <i>idsToPass</i>,
	 * assert that a {@link AbstractTraceEvent} object <i>event</i> with traceId not element of
	 * <i>idsToPass</i> IS passed through the filter.
	 */
	@Test
	public void testAssertPassTraceId() {
		final long firstTimestamp = 53222; // any number fits
		final long traceIdToPass = 11L; // (must be element of idsToPass)

		final SortedSet<Long> idsToPass = new TreeSet<Long>();
		idsToPass.add(0 + traceIdToPass);
		idsToPass.add(1 + traceIdToPass);

		final TraceIdFilter traceidFilter = new TraceIdFilter(false, idsToPass.toArray(new Long[idsToPass.size()]));
		final AbstractStage owningStage = traceidFilter.getMonitoringRecordsCombinedInputPort().getOwningStage();
		final List<IMonitoringRecord> acceptedTraceidOutputs = new ArrayList<IMonitoringRecord>();
		final List<IMonitoringRecord> notAcceptedTraceidOutputs = new ArrayList<IMonitoringRecord>();

		final AbstractTraceEvent[] traceEvents = BookstoreEventRecordFactory
				.validSyncTraceBeforeAfterEvents(firstTimestamp, traceIdToPass, TestTraceIdFilter.SESSION_ID, TestTraceIdFilter.HOSTNAME)
				.getTraceEvents();

		for (final AbstractTraceEvent e : traceEvents) {
			Assert.assertTrue("Testcase invalid", idsToPass.contains(e.getTraceId()));
		}

		StageTester.test(owningStage)
				.and().send(traceEvents).to(traceidFilter.getMonitoringRecordsCombinedInputPort())
				.and().receive(acceptedTraceidOutputs).from(traceidFilter.getMatchingTraceIdOutputPort())
				.and().receive(notAcceptedTraceidOutputs).from(traceidFilter.getMismatchingTraceIdOutputPort())
				.start();

		for (final AbstractTraceEvent e : traceEvents) {
			Assert.assertTrue("Expected event " + e + " to pass the filter", acceptedTraceidOutputs.contains(e));
		}

		Assert.assertSame(acceptedTraceidOutputs.get(0), traceEvents[0]);

		// Somehow redundant but records MIGHT be generated randomly ;-)
		Assert.assertEquals("Unexpected number of output records", acceptedTraceidOutputs.size() + notAcceptedTraceidOutputs.size(), traceEvents.length);
	}

	/**
	 * Given a TraceIdFilter that passes all traceIds, assert that an {@link AbstractTraceEvent} object <i>exec</i> is passed through the filter.
	 */
	@Test
	public void testAssertPassTraceIdWhenPassAll() {
		final long firstTimestamp = 53222; // any number fits
		final long traceIdToPass = 11L; // (must be element of idsToPass)

		final TraceIdFilter traceidFilter = new TraceIdFilter(true, new Long[0]); // i.e. pass all
		final AbstractStage owningStage = traceidFilter.getMonitoringRecordsCombinedInputPort().getOwningStage();
		final List<IMonitoringRecord> acceptedTraceidOutputs = new ArrayList<IMonitoringRecord>();
		final List<IMonitoringRecord> notAcceptedTraceidOutputs = new ArrayList<IMonitoringRecord>();

		final AbstractTraceEvent[] traceEvents = BookstoreEventRecordFactory
				.validSyncTraceBeforeAfterEvents(firstTimestamp, traceIdToPass, TestTraceIdFilter.SESSION_ID, TestTraceIdFilter.HOSTNAME)
				.getTraceEvents();

		StageTester.test(owningStage)
				.and().send(traceEvents).to(traceidFilter.getMonitoringRecordsCombinedInputPort())
				.and().receive(acceptedTraceidOutputs).from(traceidFilter.getMatchingTraceIdOutputPort())
				.and().receive(notAcceptedTraceidOutputs).from(traceidFilter.getMismatchingTraceIdOutputPort())
				.start();

		for (final AbstractTraceEvent e : traceEvents) {
			Assert.assertTrue("Expected event " + e + " to pass the filter", acceptedTraceidOutputs.contains(e));
		}

		Assert.assertSame(acceptedTraceidOutputs.get(0), traceEvents[0]);

		// Somehow redundant but records MIGHT be generated randomly ;-)
		Assert.assertEquals("Unexpected number of output records", acceptedTraceidOutputs.size() + notAcceptedTraceidOutputs.size(), traceEvents.length);
	}
}
