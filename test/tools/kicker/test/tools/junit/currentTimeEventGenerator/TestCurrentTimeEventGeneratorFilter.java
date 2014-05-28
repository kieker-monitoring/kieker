/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.test.tools.junit.currentTimeEventGenerator;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kicker.analysis.AnalysisController;
import kicker.analysis.IAnalysisController;
import kicker.analysis.exception.AnalysisConfigurationException;
import kicker.analysis.plugin.filter.forward.ListCollectionFilter;
import kicker.analysis.plugin.reader.list.ListReader;
import kicker.common.configuration.Configuration;
import kicker.common.record.misc.EmptyRecord;
import kicker.common.record.misc.TimestampRecord;
import kicker.test.common.junit.AbstractKickerTest;
import kicker.tools.currentTimeEventGenerator.CurrentTimeEventGenerationFilter;

/**
 * Each test is executed for both input ports, {@link CurrentTimeEventGenerationFilter#inputTimestamp(Long)} and
 * {@link CurrentTimeEventGenerationFilter#inputRecord(kicker.common.record.IMonitoringRecord)}.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.3
 */
public class TestCurrentTimeEventGeneratorFilter extends AbstractKickerTest {

	/**
	 * Default constructor.
	 */
	public TestCurrentTimeEventGeneratorFilter() {
		// empty default constructor
	}

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
	 *             If the internally assembled analysis configuration is somehow invalid.
	 * @throws IllegalStateException
	 *             If the internally assembled analysis is in an invalid state.
	 */
	private void compareInputAndOutput(final long timerResolution, final long[] inputTimestamps, final long[] expectedOutputTimerEvents,
			final boolean rawTimestamp) throws IllegalStateException, AnalysisConfigurationException {
		final IAnalysisController controller = new AnalysisController();

		final ListReader<Object> reader = new ListReader<Object>(new Configuration(), controller);
		final Configuration filterConfiguration = new Configuration();
		filterConfiguration.setProperty(CurrentTimeEventGenerationFilter.CONFIG_PROPERTY_NAME_TIME_RESOLUTION, Long.toString(timerResolution));
		final CurrentTimeEventGenerationFilter filter = new CurrentTimeEventGenerationFilter(filterConfiguration, controller);

		final ListCollectionFilter<TimestampRecord> sinkRecord = new ListCollectionFilter<TimestampRecord>(new Configuration(), controller);
		final ListCollectionFilter<Long> sinkLong = new ListCollectionFilter<Long>(new Configuration(), controller);

		if (rawTimestamp) {
			controller.connect(reader, ListReader.OUTPUT_PORT_NAME, filter, CurrentTimeEventGenerationFilter.INPUT_PORT_NAME_NEW_TIMESTAMP);
		} else {
			controller.connect(reader, ListReader.OUTPUT_PORT_NAME, filter, CurrentTimeEventGenerationFilter.INPUT_PORT_NAME_NEW_RECORD);
		}
		controller.connect(filter, CurrentTimeEventGenerationFilter.OUTPUT_PORT_NAME_CURRENT_TIME_RECORD, sinkRecord, ListCollectionFilter.INPUT_PORT_NAME);
		controller.connect(filter, CurrentTimeEventGenerationFilter.OUTPUT_PORT_NAME_CURRENT_TIME_VALUE, sinkLong, ListCollectionFilter.INPUT_PORT_NAME);

		for (final long timestamp : inputTimestamps) {
			if (rawTimestamp) { // pass raw timestamp as long
				reader.addObject(timestamp);
			} else { // wrap timestamp in dummy record
				final EmptyRecord r = new EmptyRecord();
				r.setLoggingTimestamp(timestamp);
				reader.addObject(r);
			}
		}

		controller.run();

		final List<TimestampRecord> listRecords = sinkRecord.getList();
		final List<Long> listLongs = sinkLong.getList();
		final long[] receivedTimestampsArr = new long[listRecords.size()];
		final long[] receivedRawTimestampsArr = new long[listLongs.size()];
		for (int i = 0; i < receivedTimestampsArr.length; i++) {
			receivedTimestampsArr[i] = listRecords.get(i).getTimestamp();
			receivedRawTimestampsArr[i] = listLongs.get(i);
		}

		Assert.assertArrayEquals(receivedTimestampsArr, receivedRawTimestampsArr);

		if (expectedOutputTimerEvents.length != sinkRecord.size()) {
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
}
