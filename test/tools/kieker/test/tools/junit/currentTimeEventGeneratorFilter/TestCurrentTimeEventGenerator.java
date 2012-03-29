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

package kieker.test.tools.junit.currentTimeEventGeneratorFilter;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

import junit.framework.Assert;
import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.misc.EmptyRecord;
import kieker.common.record.misc.TimestampRecord;
import kieker.tools.currentTimeEventGenerator.CurrentTimeEventGenerationFilter;

import org.junit.Test;

/**
 * Each test is executed for both input ports, {@link CurrentTimeEventGenerationFilter#inputTimestamp(Long)} and
 * {@link CurrentTimeEventGenerationFilter#inputRecord(kieker.common.record.IMonitoringRecord)}.
 * 
 * @author Andre van Hoorn
 * 
 */
public class TestCurrentTimeEventGenerator { // NOCS

	@Test
	public void testFirstRecordGeneratesEvent() throws IllegalStateException, AnalysisConfigurationException { // NOPMD (assert in method)
		this.compareInputAndOutput(1000, new long[] { 15 }, new long[] { 15 }, true); // true: raw timestamp
		this.compareInputAndOutput(1000, new long[] { 15 }, new long[] { 15 }, false); // false: wrap in record
	}

	@Test
	public void testSecondWithinBoundNoEvent() throws IllegalStateException, AnalysisConfigurationException { // NOPMD (assert in method)
		final long resolution = 1000;
		final long firstT = 15;
		final long secondT = (firstT + resolution) - 1;
		this.compareInputAndOutput(resolution, new long[] { firstT, secondT }, new long[] { firstT }, true);
		this.compareInputAndOutput(resolution, new long[] { firstT, secondT }, new long[] { firstT }, false);
	}

	@Test
	public void testSecondOnBoundEvent() throws IllegalStateException, AnalysisConfigurationException { // NOPMD (assert in method)
		final long resolution = 1000;
		final long firstT = 15;
		final long secondT = firstT + resolution;
		this.compareInputAndOutput(resolution, new long[] { firstT, secondT }, new long[] { firstT, secondT }, true);
		this.compareInputAndOutput(resolution, new long[] { firstT, secondT }, new long[] { firstT, secondT }, false);
	}

	@Test
	public void testSecondBeyondBoundEvent() throws IllegalStateException, AnalysisConfigurationException { // NOPMD (assert in method)
		final long resolution = 1000;
		final long firstT = 15;
		final long secondT = firstT + resolution + 1;
		this.compareInputAndOutput(resolution, new long[] { firstT, secondT }, new long[] { firstT, firstT + resolution }, true);
		this.compareInputAndOutput(resolution, new long[] { firstT, secondT }, new long[] { firstT, firstT + resolution }, false);
	}

	@Test
	public void testTwoWithinBoundOnlyOneEvent() throws IllegalStateException, AnalysisConfigurationException { // NOPMD (assert in method)
		final long resolution = 10;
		final long firstT = 5;
		final long secondT = firstT + 1;
		final long thirdT = secondT + 4;
		final long fourthT = firstT + resolution; // triggers next event
		this.compareInputAndOutput(resolution, new long[] { firstT, secondT, thirdT, fourthT }, new long[] { firstT, fourthT }, true);
		this.compareInputAndOutput(resolution, new long[] { firstT, secondT, thirdT, fourthT }, new long[] { firstT, fourthT }, false);
	}

	@Test
	public void testGapIntermediateEvents() throws IllegalStateException, AnalysisConfigurationException { // NOPMD (assert in method)
		final long resolution = 6;
		final long firstT = 5;
		final long secondT = firstT + (5 * resolution) + 1;
		this.compareInputAndOutput(resolution, new long[] { firstT, secondT }, new long[] { firstT, firstT + resolution, firstT + (2 * resolution),
			firstT + (3 * resolution), firstT + (4 * resolution), firstT + (5 * resolution), }, true);
		this.compareInputAndOutput(resolution, new long[] { firstT, secondT }, new long[] { firstT, firstT + resolution, firstT + (2 * resolution),
			firstT + (3 * resolution), firstT + (4 * resolution), firstT + (5 * resolution), }, false);
	}

	/**
	 * Creates a {@link CurrentTimeEventGenerationFilter} with the given time
	 * resolution, input the sequence of input timestamps and compares the
	 * sequence of generated timer events with the given sequence of expected
	 * output timer events.
	 * 
	 * @param timerResolution
	 * @param inputTimestamps
	 * @param expectedOutputTimerEvents
	 * @throws AnalysisConfigurationException
	 * @throws IllegalStateException
	 */
	private void compareInputAndOutput(final long timerResolution, final long[] inputTimestamps, final long[] expectedOutputTimerEvents, final boolean rawTimestamp)
			throws IllegalStateException, AnalysisConfigurationException {
		final Configuration filterConfiguration = new Configuration();
		filterConfiguration.setProperty(CurrentTimeEventGenerationFilter.CONFIG_PROPERTY_NAME_TIME_RESOLUTION, Long.toString(timerResolution));
		final CurrentTimeEventGenerationFilter filter = new CurrentTimeEventGenerationFilter(filterConfiguration);

		final DstClass dst = new DstClass();
		final AnalysisController controller = new AnalysisController();
		controller.registerFilter(filter);
		controller.registerFilter(dst);
		controller.connect(filter, CurrentTimeEventGenerationFilter.OUTPUT_PORT_NAME_CURRENT_TIME, dst, DstClass.INPUT_PORT_NAME);
		// TODO: Use list reader and actually run the controller

		for (final long timestamp : inputTimestamps) {
			if (rawTimestamp) { // pass raw timestamp as long
				filter.inputTimestamp(timestamp);
			} else { // wrap timestamp in dummy record
				final EmptyRecord r = new EmptyRecord();
				r.setLoggingTimestamp(timestamp);
				filter.inputRecord(r);
			}
		}

		final Long[] receivedTimestampsArr = dst.getList().toArray(new Long[dst.getList().size()]);

		if (expectedOutputTimerEvents.length != dst.getList().size()) {
			Assert.fail("Mismatach in sequence length while comparing timer event sequences" + "Expected: " + Arrays.toString(expectedOutputTimerEvents)
					+ " Found: " + Arrays.toString(receivedTimestampsArr));
		}

		int firstMismatchIdx = -1;
		for (int i = 0; i < expectedOutputTimerEvents.length; i++) {
			if (expectedOutputTimerEvents[i] != receivedTimestampsArr[i]) {
				firstMismatchIdx = i;
				break;
			}
		}

		if (firstMismatchIdx >= 0) {
			Assert.fail("Mismatch at index " + firstMismatchIdx + " while comparing timer event sequences" + "Expected: "
					+ Arrays.toString(expectedOutputTimerEvents) + " Found: " + Arrays.toString(receivedTimestampsArr));
		}
	}

	// TODO: Don't we have a general sink for this already?
	static class DstClass extends AbstractFilterPlugin {

		public static final String INPUT_PORT_NAME = "doJob";
		private final ConcurrentLinkedQueue<Long> receivedTimestamps = new ConcurrentLinkedQueue<Long>();

		public DstClass() {
			super(new Configuration());
		}

		@Override
		protected Configuration getDefaultConfiguration() {
			return null;
		}

		public Configuration getCurrentConfiguration() {
			return null;
		}

		@InputPort(
				name = DstClass.INPUT_PORT_NAME,
				eventTypes = { TimestampRecord.class })
		public void doJob(final Object data) {
			this.receivedTimestamps.add(((TimestampRecord) data).getTimestamp());
		}

		public ConcurrentLinkedQueue<Long> getList() {
			return this.receivedTimestamps;
		}
	}
}
