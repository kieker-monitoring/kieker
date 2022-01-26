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

package kieker.test.analysis.junit.plugin.filter.record;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.AnalysisThroughputFilter;
import kieker.analysis.plugin.filter.forward.CountingFilter;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.filter.record.RealtimeRecordDelayFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.analysis.plugin.reader.timer.TimeReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * This test is for the class {@link RealtimeRecordDelayFilter}.
 *
 * @author Andre van Hoorn
 *
 * @since 1.6
 */
public abstract class AbstractTestRealtimeRecordDelayFilter extends AbstractKiekerTest {

	private static final long START_TIME_SECONDS = 246561L;
	private final long[] eventTimeOffsetsSeconds;
	// intervals of length INTERVAL_SIZE_NANOS relative to start time
	private final long[] expectedThroughputListOffsetSecondsInterval5Secs;

	private final double accelerationFactor;

	private IAnalysisController analysisController;

	/** List of all {@link EmptyRecord}s to be read by the {@link #simpleListReader}. */
	private final List<EmptyRecord> inputRecords = new ArrayList<>();

	/** Provides the list of {@link IMonitoringRecord}s to be delayed. */
	private ListReader<IMonitoringRecord> simpleListReader;

	/** Provides the (current) number of {@link IMonitoringRecord}s provided by the {@link #simpleListReader}. */
	private CountingFilter countingFilterReader;

	/** Provides the (current) number of {@link IMonitoringRecord}s provided by the {@link #delayFilter}. */
	private CountingFilter countingFilterDelayed;

	/** Simply collects all delayed {@link IMonitoringRecord}s. */
	private ListCollectionFilter<EmptyRecord> sinkPlugin;

	/** Simply collects all throughputs. */
	private ListCollectionFilter<Long> sinkThroughput;

	/**
	 *
	 * @param eventTimeOffsetsSeconds
	 *            points in time for which to generate an event (relative to start; in seconds)
	 * @param expectedThroughputListOffsetSecondsInterval5Secs
	 *            expected number of events per intervals (of length 5 seconds; relative to start time; in seconds)
	 * @param accelerationFactor
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
		this.simpleListReader = new ListReader<>(readerConfiguration, this.analysisController);

		// Timer
		final Configuration timerConfig = new Configuration();
		timerConfig.setProperty(TimeReader.CONFIG_PROPERTY_NAME_DELAY_NS, "5000000000");
		timerConfig.setProperty(TimeReader.CONFIG_PROPERTY_NAME_UPDATE_INTERVAL_NS, "5000000000");
		timerConfig.setProperty(TimeReader.CONFIG_PROPERTY_NAME_NUMBER_IMPULSES, String.valueOf(this.expectedThroughputListOffsetSecondsInterval5Secs.length));
		final TimeReader timeReader = new TimeReader(timerConfig, this.analysisController);

		// Counting filter (before delay)
		this.countingFilterReader = new CountingFilter(new Configuration(), this.analysisController);
		this.analysisController.connect(this.simpleListReader, ListReader.OUTPUT_PORT_NAME,
				this.countingFilterReader, CountingFilter.INPUT_PORT_NAME_EVENTS);

		// Delay filter
		final Configuration delayFilterConfiguration = new Configuration();
		delayFilterConfiguration.setProperty(RealtimeRecordDelayFilter.CONFIG_PROPERTY_NAME_ACCELERATION_FACTOR, Double.toString(this.accelerationFactor));

		final RealtimeRecordDelayFilter delayFilter = new RealtimeRecordDelayFilter(delayFilterConfiguration, this.analysisController);
		this.analysisController.connect(this.countingFilterReader, CountingFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS,
				delayFilter, RealtimeRecordDelayFilter.INPUT_PORT_NAME_RECORDS);

		// The CountingThroughputFilter to be tested
		final Configuration throughputFilterConfiguration = new Configuration();
		final AnalysisThroughputFilter throughputFilter = new AnalysisThroughputFilter(throughputFilterConfiguration, this.analysisController);
		this.analysisController.connect(delayFilter, RealtimeRecordDelayFilter.OUTPUT_PORT_NAME_RECORDS,
				throughputFilter, AnalysisThroughputFilter.INPUT_PORT_NAME_OBJECTS);
		this.analysisController.connect(timeReader, TimeReader.OUTPUT_PORT_NAME_TIMESTAMPS,
				throughputFilter, AnalysisThroughputFilter.INPUT_PORT_NAME_TIME);

		// Throughput sink
		this.sinkThroughput = new ListCollectionFilter<>(new Configuration(), this.analysisController);
		this.analysisController.connect(throughputFilter, AnalysisThroughputFilter.OUTPUT_PORT_NAME_THROUGHPUT,
				this.sinkThroughput, ListCollectionFilter.INPUT_PORT_NAME);
		// final TeeFilter tf = new TeeFilter(new Configuration(), this.analysisController);
		// this.analysisController.connect(this.sinkThroughput, ListCollectionFilter.OUTPUT_PORT_NAME, tf, TeeFilter.INPUT_PORT_NAME_EVENTS);

		// Counting filter (after delay)
		this.countingFilterDelayed = new CountingFilter(new Configuration(), this.analysisController);
		this.analysisController.connect(throughputFilter, AnalysisThroughputFilter.OUTPUT_PORT_NAME_RELAYED_OBJECTS,
				this.countingFilterDelayed, CountingFilter.INPUT_PORT_NAME_EVENTS);

		// Sink
		this.sinkPlugin = new ListCollectionFilter<>(new Configuration(), this.analysisController);
		this.analysisController.connect(this.countingFilterDelayed, CountingFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS,
				this.sinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);
	}

	private List<Entry<Long, Integer>> passEventListToReader(final ListReader<IMonitoringRecord> reader) {
		long currentTimeSeconds;
		int curNumRecords = 0;

		final List<Entry<Long, Integer>> eventList = new ArrayList<>(this.eventTimeOffsetsSeconds.length);

		for (final long eventDelaySeconds : this.eventTimeOffsetsSeconds) {
			curNumRecords++;
			currentTimeSeconds = START_TIME_SECONDS + eventDelaySeconds;
			final Entry<Long, Integer> curEntry = new SimpleImmutableEntry<>(eventDelaySeconds, curNumRecords);
			eventList.add(curEntry);
			final EmptyRecord r = new EmptyRecord();
			r.setLoggingTimestamp(TimeUnit.NANOSECONDS.convert(currentTimeSeconds, TimeUnit.SECONDS));
			this.inputRecords.add(r);
			reader.addObject(r);
		}

		return eventList;
	}

	private final void checkTiming() throws InterruptedException {
		final List<Long> throughput = this.sinkThroughput.getList();
		final long[] actualArray = new long[throughput.size()];
		for (int i = 0; i < actualArray.length; i++) {
			actualArray[i] = throughput.get(i);
		}
		Assert.assertArrayEquals("Unexpected throughput", actualArray, this.expectedThroughputListOffsetSecondsInterval5Secs);
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
