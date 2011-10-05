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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.tools.currentTimeEventGenerator.CurrentTimeEventGenerator;
import kieker.tools.currentTimeEventGenerator.TimestampEvent;

public class TestCurrentTimeEventGenerator extends TestCase {

	/**
	 * 
	 */
	public void testFirstRecordGeneratesEvent() {
		compareInputAndOutput(1000, new long[] { 15 }, new long[] { 15 });
	}

	/**
	 * 
	 */
	public void testSecondWithinBoundNoEvent() {
		final long resolution = 1000;
		final long firstT = 15;
		final long secondT = (firstT + resolution) - 1;
		compareInputAndOutput(resolution, new long[] { firstT, secondT }, new long[] { firstT });
	}

	/**
	 * 
	 */
	public void testSecondOnBoundEvent() {
		final long resolution = 1000;
		final long firstT = 15;
		final long secondT = firstT + resolution;
		compareInputAndOutput(resolution, new long[] { firstT, secondT }, new long[] { firstT, secondT });
	}

	/**
	 * 
	 */
	public void testSecondBeyondBoundEvent() {
		final long resolution = 1000;
		final long firstT = 15;
		final long secondT = firstT + resolution + 1;
		compareInputAndOutput(resolution, new long[] { firstT, secondT }, new long[] { firstT, firstT + resolution });
	}

	/**
	 * 
	 */
	public void testTwoWithinBoundOnlyOneEvent() {
		final long resolution = 10;
		final long firstT = 5;
		final long secondT = firstT + 1;
		final long thirdT = secondT + 4;
		final long fourthT = firstT + resolution; // triggers next event
		compareInputAndOutput(resolution, new long[] { firstT, secondT, thirdT, fourthT }, new long[] { firstT, fourthT });
	}

	public void testGapIntermediateEvents() {
		final long resolution = 6;
		final long firstT = 5;
		final long secondT = firstT + (5 * resolution) + 1;
		compareInputAndOutput(resolution, new long[] { firstT, secondT }, new long[] { firstT, firstT + resolution, firstT + (2 * resolution),
				firstT + (3 * resolution), firstT + (4 * resolution), firstT + (5 * resolution) });
	}

	/**
	 * Creates a {@link CurrentTimeEventGenerator} with the given time
	 * resolution, input the sequence of input timestamps and compares the
	 * sequence of generated timer events with the given sequence of expected
	 * output timer events.
	 * 
	 * @param timerResolution
	 * @param inputTimestamps
	 * @param expectedOutputTimerEvents
	 */
	private void compareInputAndOutput(final long timerResolution, final long[] inputTimestamps, final long[] expectedOutputTimerEvents) {
		final CurrentTimeEventGenerator filter = new CurrentTimeEventGenerator(timerResolution);

		final List<Long> receivedTimestamps = new ArrayList<Long>();

		filter.getCurrentTimeOutputPort().subscribe(new AbstractInputPort<TimestampEvent>("") {
			@Override
			public void newEvent(final TimestampEvent event) {
				receivedTimestamps.add(event.getTimestamp());
			}
		});

		for (final long timestamp : inputTimestamps) {
			filter.newTimestamp(timestamp);
		}

		final Long[] receivedTimestampsArr = receivedTimestamps.toArray(new Long[receivedTimestamps.size()]);

		if (expectedOutputTimerEvents.length != receivedTimestamps.size()) {
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
