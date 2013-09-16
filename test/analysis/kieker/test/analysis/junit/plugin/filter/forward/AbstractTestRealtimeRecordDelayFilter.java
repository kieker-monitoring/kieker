/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.analysis.junit.plugin.filter.forward;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.CountingFilter;
import kieker.analysis.plugin.filter.forward.CountingThroughputFilter;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.filter.realtime.RealtimeRecordDelayFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;
import kieker.common.util.ImmutableEntry;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * This test is for the class {@link RealtimeRecordDelayFilter}.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.6
 */
public abstract class AbstractTestRealtimeRecordDelayFilter extends AbstractKiekerTest {

	// private static final Log LOG = LogFactory.getLog(TestRealtimeRecordDelayFilter.class);

	private static final long INTERVAL_SIZE_NANOS = TimeUnit.NANOSECONDS.convert(5, TimeUnit.SECONDS);

	private static final long START_TIME_SECONDS = 246561L;
	private final long[] eventTimeOffsetsSeconds;
	// intervals of length INTERVAL_SIZE_NANOS relative to start time
	private final long[] expectedThroughputListOffsetSecondsInterval5Secs;

	private final double accelerationFactor;

	private IAnalysisController analysisController;

	/**
	 * List of all {@link EmptyRecord}s to be read by the {@link #simpleListReader}.
	 */
	private final List<EmptyRecord> inputRecords = new ArrayList<EmptyRecord>();

	/** Provides the list of {@link IMonitoringRecord}s to be delayed. */
	private ListReader<IMonitoringRecord> simpleListReader;

	/** The filter actually tested. */
	private RealtimeRecordDelayFilter delayFilter; // NOPMD (SingularField) // We want to have all filters declared here

	/** Provides the (current) number of {@link IMonitoringRecord}s provided by the {@link #simpleListReader}. */
	private CountingFilter countingFilterReader;

	/** Provides the (current) number of {@link IMonitoringRecord}s provided by the {@link #delayFilter}. */
	private CountingFilter countingFilterDelayed;

	/** Records the number of records provided by the {@link RealtimeRecordDelayFilter}. */
	private CountingThroughputFilter throughputFilter;

	/** Simply collects all delayed {@link IMonitoringRecord}s. */
	private ListCollectionFilter<EmptyRecord> sinkPlugin;

	/**
	 * 
	 * @param eventTimeOffsetsSeconds
	 *            points in time for which to generate an event (relative to start; in seconds)
	 * @param expectedThroughputListOffsetSecondsInterval5Secs
	 *            expected number of events per intervals (of length 5 seconds; relative to start time; in seconds)
	 * @param acceleration
	 *            factor to be passed to the {@link RealtimeRecordDelayFilter}
	 */
	public AbstractTestRealtimeRecordDelayFilter(final long[] eventTimeOffsetsSeconds, final long[] expectedThroughputListOffsetSecondsInterval5Secs,
			final double accelerationFactor) {
		this.eventTimeOffsetsSeconds = eventTimeOffsetsSeconds.clone();
		this.expectedThroughputListOffsetSecondsInterval5Secs = expectedThroughputListOffsetSecondsInterval5Secs.clone();
		this.accelerationFactor = accelerationFactor;
	}

	@Before
	public void before() throws IllegalStateException, AnalysisConfigurationException {
		// Analysis controller
		this.analysisController = new AnalysisController();

		// Reader
		final Configuration readerConfiguration = new Configuration();
		readerConfiguration.setProperty(ListReader.CONFIG_PROPERTY_NAME_AWAIT_TERMINATION, Boolean.FALSE.toString());
		this.simpleListReader = new ListReader<IMonitoringRecord>(readerConfiguration, this.analysisController);

		// Counting filter (before delay)
		this.countingFilterReader = new CountingFilter(new Configuration(), this.analysisController);
		this.analysisController.connect(this.simpleListReader, ListReader.OUTPUT_PORT_NAME,
				this.countingFilterReader, CountingFilter.INPUT_PORT_NAME_EVENTS);

		// Delay filter
		final Configuration delayFilterConfiguration = new Configuration();
		delayFilterConfiguration.setProperty(RealtimeRecordDelayFilter.CONFIG_PROPERTY_NAME_ACCELERATION_FACTOR, Double.toString(this.accelerationFactor));
		this.delayFilter = new RealtimeRecordDelayFilter(delayFilterConfiguration, this.analysisController);
		this.analysisController.connect(this.countingFilterReader, CountingFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS,
				this.delayFilter, RealtimeRecordDelayFilter.INPUT_PORT_NAME_RECORDS);

		// The CountingThroughputFilter to be tested
		final Configuration throughputFilterConfiguration = new Configuration();
		throughputFilterConfiguration.setProperty(CountingThroughputFilter.CONFIG_PROPERTY_NAME_INTERVAL_SIZE, Long.toString(INTERVAL_SIZE_NANOS));
		// We use the following property because this is way easier to test:
		throughputFilterConfiguration.setProperty(CountingThroughputFilter.CONFIG_PROPERTY_NAME_INTERVALS_BASED_ON_1ST_TSTAMP, Boolean.TRUE.toString());
		this.throughputFilter = new CountingThroughputFilter(throughputFilterConfiguration, this.analysisController);
		this.analysisController.connect(this.delayFilter, RealtimeRecordDelayFilter.OUTPUT_PORT_NAME_RECORDS,
				this.throughputFilter, CountingThroughputFilter.INPUT_PORT_NAME_OBJECTS); // NOT: INPUT_PORT_NAME_RECORDS because we need "real time"

		// Counting filter (after delay)
		this.countingFilterDelayed = new CountingFilter(new Configuration(), this.analysisController);
		this.analysisController.connect(this.throughputFilter, CountingThroughputFilter.OUTPUT_PORT_NAME_RELAYED_OBJECTS,
				this.countingFilterDelayed, CountingFilter.INPUT_PORT_NAME_EVENTS);

		// Sink plugin
		this.sinkPlugin = new ListCollectionFilter<EmptyRecord>(new Configuration(), this.analysisController);
		this.analysisController.connect(this.countingFilterDelayed, CountingFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS,
				this.sinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);
	}

	private List<Entry<Long, Integer>> passEventListToReader(final ListReader<IMonitoringRecord> reader) {
		long currentTimeSeconds;
		int curNumRecords = 0;

		final List<Entry<Long, Integer>> eventList = new ArrayList<Entry<Long, Integer>>(this.eventTimeOffsetsSeconds.length);

		for (final long eventDelaySeconds : this.eventTimeOffsetsSeconds) {
			curNumRecords++;
			currentTimeSeconds = START_TIME_SECONDS + eventDelaySeconds;
			final Entry<Long, Integer> curEntry = new ImmutableEntry<Long, Integer>(eventDelaySeconds, curNumRecords);
			eventList.add(curEntry);
			final EmptyRecord r = new EmptyRecord();
			r.setLoggingTimestamp(TimeUnit.NANOSECONDS.convert(currentTimeSeconds, TimeUnit.SECONDS));
			this.inputRecords.add(r);
			reader.addObject(r);
		}

		return eventList;
	}

	private final void checkTiming() throws InterruptedException {
		final Collection<Entry<Long, Long>> throughputListFromFilter = this.throughputFilter.getCountsPerInterval();
		final List<Entry<Long, Long>> throughputListFromFilterAndCurrentInterval = new ArrayList<Map.Entry<Long, Long>>();
		{ // We'll need to append the value for the current (pending) interval // NOCS (nested block)
			throughputListFromFilterAndCurrentInterval.addAll(throughputListFromFilter);
			throughputListFromFilterAndCurrentInterval.add(new ImmutableEntry<Long, Long>(
					this.throughputFilter.getLastTimestampInCurrentInterval() + 1, this.throughputFilter.getCurrentCountForCurrentInterval()));
		}

		final long filterStartTimeNanos = throughputListFromFilterAndCurrentInterval.get(0).getKey() - this.throughputFilter.getIntervalSize();

		// Init array with worst-case size! (we actually expect an array of EXPECTED_THROUGHPUT_LIST_OFFSET_SECONDS.size())
		final long[] counts = new long[(int) this.countingFilterReader.getMessageCount()];
		for (final Entry<Long, Long> countAtIntervalEnd : throughputListFromFilterAndCurrentInterval) {
			// relative to filter start time:
			final long curIntervalEndOffsetNanos = countAtIntervalEnd.getKey() - filterStartTimeNanos;
			final long curCount = countAtIntervalEnd.getValue();
			// Example: First value for interval size 500: (500-1) / 500 = 0:
			final int curCountIdx = (int) ((curIntervalEndOffsetNanos - 1) / this.throughputFilter.getIntervalSize());
			counts[curCountIdx] = curCount;
		}

		final int maxArrayLength = Math.max(counts.length, this.expectedThroughputListOffsetSecondsInterval5Secs.length);
		final long[] expectedArrAdoptedLength = new long[maxArrayLength];
		System.arraycopy(this.expectedThroughputListOffsetSecondsInterval5Secs, 0, expectedArrAdoptedLength, 0,
				this.expectedThroughputListOffsetSecondsInterval5Secs.length);
		final long[] actualArrAdoptedLength = new long[maxArrayLength];
		System.arraycopy(counts, 0, actualArrAdoptedLength, 0, counts.length);

		Assert.assertArrayEquals("Unexpected throughput", expectedArrAdoptedLength, actualArrAdoptedLength);
	}

	private final void checkRelayedRecords() {
		final List<EmptyRecord> relayedEvents = this.sinkPlugin.getList();
		Assert.assertEquals(this.inputRecords, relayedEvents);
	}

	@Test
	public void testNormal() throws IllegalStateException, AnalysisConfigurationException, InterruptedException {
		final List<Entry<Long, Integer>> eventList = this.passEventListToReader(this.simpleListReader);
		Assert.assertEquals(0, this.sinkPlugin.size());

		this.analysisController.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, this.analysisController.getState());

		// Make sure that all events have been provided to the delay filter (otherwise the test make no sense)
		Assert.assertEquals("Test invalid: Unexpected number of events provided TO the delay filter", eventList.size(), this.countingFilterReader.getMessageCount());

		// Make sure that all events have been passed through the delay filter
		Assert.assertEquals("Unexpected number of events relayed by the delay filter", eventList.size(), this.countingFilterDelayed.getMessageCount());

		this.checkTiming();

		// Make sure that exactly the right objects have been passed
		this.checkRelayedRecords();
	}
}
