/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.time.events;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;
import kieker.common.record.misc.TimestampRecord;

import kieker.test.common.junit.AbstractKiekerTest;

import teetime.framework.Configuration;
import teetime.framework.Execution;
import teetime.stage.CollectorSink;
import teetime.stage.InitialElementProducer;

/**
 * Each test is executed for both concrete implementations of AbstractCurrentTimeEventGenerationFilter, {@link TimestampCurrentTimeEventGenerationFilter} and
 * {@link RecordCurrentTimeEventGenerationFilterConfig}.
 *
 * @author Andre van Hoorn, Lars Bluemke
 *
 * @since 1.3
 */
public class TestCurrentTimeEventGeneratorFilter extends AbstractKiekerTest {

	/**
	 * Default constructor.
	 */
	public TestCurrentTimeEventGeneratorFilter() {
		// empty default constructor
	}

	@Test
	public void testFirstRecordGeneratesEvent() { // NOPMD (assert in method)
		this.compareInputAndOutput(1000, new long[] { 15 }, new long[] { 15 }, true); // true: raw timestamp
		this.compareInputAndOutput(1000, new long[] { 15 }, new long[] { 15 }, false); // false: wrap in record
	}

	@Test
	public void testSecondWithinBoundNoEvent() { // NOPMD (assert in method)
		final long resolution = 1000;
		final long firstT = 15;
		final long secondT = (firstT + resolution) - 1;
		this.compareInputAndOutput(resolution, new long[] { firstT, secondT }, new long[] { firstT }, true);
		this.compareInputAndOutput(resolution, new long[] { firstT, secondT }, new long[] { firstT }, false);
	}

	@Test
	public void testSecondOnBoundEvent() { // NOPMD (assert in method)
		final long resolution = 1000;
		final long firstT = 15;
		final long secondT = firstT + resolution;
		this.compareInputAndOutput(resolution, new long[] { firstT, secondT }, new long[] { firstT, secondT }, true);
		this.compareInputAndOutput(resolution, new long[] { firstT, secondT }, new long[] { firstT, secondT }, false);
	}

	@Test
	public void testSecondBeyondBoundEvent() { // NOPMD (assert in method)
		final long resolution = 1000;
		final long firstT = 15;
		final long secondT = firstT + resolution + 1;
		this.compareInputAndOutput(resolution, new long[] { firstT, secondT }, new long[] { firstT, firstT + resolution }, true);
		this.compareInputAndOutput(resolution, new long[] { firstT, secondT }, new long[] { firstT, firstT + resolution }, false);
	}

	@Test
	public void testTwoWithinBoundOnlyOneEvent() { // NOPMD (assert in method)
		final long resolution = 10;
		final long firstT = 5;
		final long secondT = firstT + 1;
		final long thirdT = secondT + 4;
		final long fourthT = firstT + resolution; // triggers next event
		this.compareInputAndOutput(resolution, new long[] { firstT, secondT, thirdT, fourthT }, new long[] { firstT, fourthT }, true);
		this.compareInputAndOutput(resolution, new long[] { firstT, secondT, thirdT, fourthT }, new long[] { firstT, fourthT }, false);
	}

	@Test
	public void testGapIntermediateEvents() { // NOPMD (assert in method)
		final long resolution = 6;
		final long firstT = 5;
		final long secondT = firstT + (5 * resolution) + 1;
		this.compareInputAndOutput(resolution, new long[] { firstT, secondT }, new long[] { firstT, firstT + resolution, firstT + (2 * resolution),
			firstT + (3 * resolution), firstT + (4 * resolution), firstT + (5 * resolution), }, true);
		this.compareInputAndOutput(resolution, new long[] { firstT, secondT }, new long[] { firstT, firstT + resolution, firstT + (2 * resolution),
			firstT + (3 * resolution), firstT + (4 * resolution), firstT + (5 * resolution), }, false);
	}

	/**
	 * Creates a {@link TimestampCurrentTimeEventGenerationFilter} or
	 * {@link RecordCurrentTimeEventGenerationFilter}with the given time
	 * resolution, input the sequence of input timestamps and compares the
	 * sequence of generated timer events with the given sequence of expected
	 * output timer events.
	 *
	 * @param timerResolution
	 * @param inputTimestamps
	 * @param expectedOutputTimerEvents
	 */
	private void compareInputAndOutput(final long timerResolution, final long[] inputTimestamps, final long[] expectedOutputTimerEvents,
			final boolean rawTimestamp) {
		final List<TimestampRecord> recordOutputs;
		final List<Long> timestampOutputs;

		// Create a Configuration with CurrentTimeEventGenerationFilter which takes raw timestamps or monitoring records as input
		if (rawTimestamp) {
			final TimestampCurrentTimeEventGenerationFilterConfig rawTimestampConfig = new TimestampCurrentTimeEventGenerationFilterConfig(timerResolution,
					inputTimestamps);
			final Execution<TimestampCurrentTimeEventGenerationFilterConfig> execution = new Execution<>(
					rawTimestampConfig);
			execution.executeBlocking();

			recordOutputs = rawTimestampConfig.getRecordOutputs();
			timestampOutputs = rawTimestampConfig.getTimestampOutputs();

		} else {
			final RecordCurrentTimeEventGenerationFilterConfig recordConfig = new RecordCurrentTimeEventGenerationFilterConfig(timerResolution, inputTimestamps);
			final Execution<RecordCurrentTimeEventGenerationFilterConfig> execution = new Execution<>(recordConfig);
			execution.executeBlocking();

			recordOutputs = recordConfig.getRecordOutputs();
			timestampOutputs = recordConfig.getTimestampOutputs();
		}

		final long[] receivedTimestampsArr = new long[recordOutputs.size()];
		final long[] receivedRawTimestampsArr = new long[timestampOutputs.size()];
		for (int i = 0; i < receivedTimestampsArr.length; i++) {
			receivedTimestampsArr[i] = recordOutputs.get(i).getTimestamp();
			receivedRawTimestampsArr[i] = timestampOutputs.get(i);
		}

		Assert.assertArrayEquals(receivedTimestampsArr, receivedRawTimestampsArr);

		if (expectedOutputTimerEvents.length != recordOutputs.size()) {
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

	/**
	 * Test configuration for the {@link TimestampCurrentTimeEventGenerationFilter}.
	 *
	 * @author Lars Bluemke
	 *
	 * @since 1.13
	 */
	private static class TimestampCurrentTimeEventGenerationFilterConfig extends Configuration {
		private final CollectorSink<TimestampRecord> recordCollector = new CollectorSink<>();
		private final CollectorSink<Long> timestampCollector = new CollectorSink<>();

		public TimestampCurrentTimeEventGenerationFilterConfig(final long timerResolution, final long[] inputTimestamps) {
			final InitialElementProducer<Long> timestampProducer;
			final TimestampCurrentTimeEventGenerationFilter timestampCurrentTimeGenerationFilter = new TimestampCurrentTimeEventGenerationFilter(timerResolution);

			final List<Long> inputs = new LinkedList<>();

			// pass raw timestamp as long
			for (final long timestamp : inputTimestamps) {
				inputs.add(timestamp);
			}

			timestampProducer = new InitialElementProducer<>(inputs);

			this.connectPorts(timestampProducer.getOutputPort(), timestampCurrentTimeGenerationFilter.getInputPort());
			this.connectPorts(timestampCurrentTimeGenerationFilter.getCurrentTimeRecordOutputPort(), this.recordCollector.getInputPort());
			this.connectPorts(timestampCurrentTimeGenerationFilter.getCurrentTimeValueOutputPort(), this.timestampCollector.getInputPort());
		}

		public List<TimestampRecord> getRecordOutputs() {
			return this.recordCollector.getElements();
		}

		public List<Long> getTimestampOutputs() {
			return this.timestampCollector.getElements();
		}
	}

	/**
	 * Test configuration for the {@link RecordCurrentTimeEventGenerationFilter}.
	 *
	 * @author Lars Bluemke
	 *
	 * @since 1.13
	 */
	private static class RecordCurrentTimeEventGenerationFilterConfig extends Configuration {
		private final CollectorSink<TimestampRecord> recordCollector = new CollectorSink<>();
		private final CollectorSink<Long> timestampCollector = new CollectorSink<>();

		public RecordCurrentTimeEventGenerationFilterConfig(final long timerResolution, final long[] inputTimestamps) {

			final InitialElementProducer<IMonitoringRecord> recordProducer;
			final RecordCurrentTimeEventGenerationFilter recordCurrentTimeGenerationFilter = new RecordCurrentTimeEventGenerationFilter(timerResolution);

			final List<IMonitoringRecord> inputs = new LinkedList<>();

			// wrap timestamp in dummy record
			for (final long timestamp : inputTimestamps) {
				final EmptyRecord r = new EmptyRecord();
				r.setLoggingTimestamp(timestamp);
				inputs.add(r);
			}

			recordProducer = new InitialElementProducer<>(inputs);

			this.connectPorts(recordProducer.getOutputPort(), recordCurrentTimeGenerationFilter.getInputPort());
			this.connectPorts(recordCurrentTimeGenerationFilter.getCurrentTimeRecordOutputPort(), this.recordCollector.getInputPort());
			this.connectPorts(recordCurrentTimeGenerationFilter.getCurrentTimeValueOutputPort(), this.timestampCollector.getInputPort());
		}

		public List<TimestampRecord> getRecordOutputs() {
			return this.recordCollector.getElements();
		}

		public List<Long> getTimestampOutputs() {
			return this.timestampCollector.getElements();
		}
	}
}
