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

package kieker.test.analysisteetime.junit.plugin.filter.record.delayfilter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysisteetime.plugin.filter.forward.AnalysisThroughputFilter;
import kieker.analysisteetime.plugin.filter.record.delayfilter.RealtimeRecordDelayFilter;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;

import teetime.framework.Configuration;
import teetime.framework.Execution;
import teetime.stage.Clock;
import teetime.stage.CollectorSink;
import teetime.stage.Counter;
import teetime.stage.InitialElementProducer;

/**
 * This test is for the class {@link RealtimeRecordDelayFilter}.
 *
 * @author Andre van Hoorn, Lars Erik Bluemke
 *
 * @since 1.6
 */
public abstract class AbstractTestRealtimeRecordDelayFilter {
	private static final long START_TIME_SECONDS = 246561L;
	private final long[] eventTimeOffsetsSeconds;
	// intervals of length INTERVAL_SIZE_NANOS relative to start time
	private final long[] expectedThroughputListOffsetSecondsInterval5Secs;

	private final double accelerationFactor;

	/** List of all {@link EmptyRecord}s to be read by the {@link #simpleListReader}. */
	private final List<IMonitoringRecord> inputRecords = new ArrayList<IMonitoringRecord>();

	private InitialElementProducer<IMonitoringRecord> recordProducer = null;
	private Counter<IMonitoringRecord> preDelayCounter = null;
	private RealtimeRecordDelayFilter delayFilter = null;
	private Clock clock = null;
	private AnalysisThroughputFilter throughputStage = null;
	private Counter<IMonitoringRecord> postDelayCounter = null;
	private CollectorSink<IMonitoringRecord> recordCollectorSink = null;
	private CollectorSink<Long> throughputCollectorSink = null;

	private RealtimeRecordDelayFilterTestConfiguration testConfiguration = null;

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

	public List<EmptyRecord> createRecordListFromOffsetList(final long[] eventTimeOffsetsSeconds) {
		long currentTimeSeconds;
		final List<EmptyRecord> inputRecords = new ArrayList<EmptyRecord>();

		for (final long eventDelaySeconds : eventTimeOffsetsSeconds) {
			currentTimeSeconds = START_TIME_SECONDS + eventDelaySeconds;
			final EmptyRecord r = new EmptyRecord();
			r.setLoggingTimestamp(TimeUnit.NANOSECONDS.convert(currentTimeSeconds, TimeUnit.SECONDS));
			inputRecords.add(r);
		}

		return inputRecords;
	}

	@Before
	public void initializeTestConfiguration() {
		// Create a list of records from the given list of time stamps.
		long currentTimeSeconds;

		for (final long eventDelaySeconds : this.eventTimeOffsetsSeconds) {
			currentTimeSeconds = START_TIME_SECONDS + eventDelaySeconds;
			final EmptyRecord r = new EmptyRecord();
			r.setLoggingTimestamp(TimeUnit.NANOSECONDS.convert(currentTimeSeconds, TimeUnit.SECONDS));
			System.out.println("Initialisation - Recordtime = " + r.getLoggingTimestamp());
			this.inputRecords.add(r);
		}

		// Initialize stages and test configuration
		this.recordProducer = new InitialElementProducer<IMonitoringRecord>(this.inputRecords);
		this.preDelayCounter = new Counter<IMonitoringRecord>();
		this.delayFilter = new RealtimeRecordDelayFilter(TimeUnit.NANOSECONDS, this.accelerationFactor, 2L);
		this.clock = new Clock();
		this.clock.setInitialDelayInMs(5000);
		this.clock.setIntervalDelayInMs(5000);
		this.throughputStage = new AnalysisThroughputFilter();
		this.throughputStage.declareActive();
		this.postDelayCounter = new Counter<IMonitoringRecord>();
		this.recordCollectorSink = new CollectorSink<IMonitoringRecord>();
		this.throughputCollectorSink = new CollectorSink<Long>();

		this.testConfiguration = new RealtimeRecordDelayFilterTestConfiguration(this.recordProducer, this.preDelayCounter, this.delayFilter, this.clock,
				this.throughputStage,
				this.postDelayCounter, this.recordCollectorSink, this.throughputCollectorSink);
	}

	private final void checkTiming() throws InterruptedException {
		final List<Long> throughput = this.throughputCollectorSink.getElements();
		final long[] actualArray = new long[throughput.size()];
		for (int i = 0; i < actualArray.length; i++) {
			actualArray[i] = throughput.get(i);
		}
		Assert.assertArrayEquals("Unexpected throughput", this.expectedThroughputListOffsetSecondsInterval5Secs, actualArray);
	}

	private final void checkRelayedRecords() {
		final List<IMonitoringRecord> relayedEvents = this.recordCollectorSink.getElements();
		Assert.assertEquals(this.inputRecords, relayedEvents);
	}

	@Test
	public void testNormal() throws InterruptedException {
		Assert.assertEquals(0, this.recordCollectorSink.getElements().size());

		new Execution<Configuration>(this.testConfiguration).executeBlocking();

		// Make sure that all events have been provided to the delay filter (otherwise the test make no sense)
		Assert.assertEquals("Test invalid: Unexpected number of events provided TO the delay filter", this.inputRecords.size(),
				this.preDelayCounter.getNumElementsPassed());

		// Make sure that all events have been passed through the delay filter
		Assert.assertEquals("Unexpected number of events relayed by the delay filter", this.inputRecords.size(), this.postDelayCounter.getNumElementsPassed());

		this.checkTiming();

		// Make sure that exactly the right objects have been passed
		this.checkRelayedRecords();
	}
}
