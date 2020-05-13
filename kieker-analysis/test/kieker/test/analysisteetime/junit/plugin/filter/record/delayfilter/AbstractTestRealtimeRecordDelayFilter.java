/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

import kieker.test.common.junit.AbstractKiekerTest;

import teetime.framework.Configuration;
import teetime.framework.Execution;
import teetime.framework.termination.TerminationCondition;
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
public abstract class AbstractTestRealtimeRecordDelayFilter extends AbstractKiekerTest {

	private static final long START_TIME_SECONDS = 246561L;
	private final long[] eventTimeOffsetsSeconds;
	private final long[] expectedThroughputListOffsetSecondsInterval5Secs;

	private final double accelerationFactor;

	private final List<IMonitoringRecord> inputRecords = new ArrayList<>();

	private InitialElementProducer<IMonitoringRecord> recordProducer; // NOPMD (could be replaced by a local variable)
	private Counter<IMonitoringRecord> preDelayCounter;
	private RealtimeRecordDelayFilter delayFilter; // NOPMD (could be replaced by a local variable)
	private Counter<IMonitoringRecord> postDelayCounter;
	private CollectorSink<IMonitoringRecord> recordCollectorSink;
	private AnalysisThroughputFilter throughputStage; // NOPMD (could be replaced by a local variable)
	private Clock clock; // NOPMD (could be replaced by a local variable)
	private CollectorSink<Long> throughputCollectorSink;

	private RealtimeRecordDelayFilterConfig testConfig;

	/**
	 *
	 * @param eventTimeOffsetsSeconds
	 *            points in time for which to generate an event (relative to start;
	 *            in seconds)
	 * @param expectedThroughputListOffsetSecondsInterval5Secs
	 *            expected number of events per intervals (of length 5 seconds;
	 *            relative to start time; in seconds)
	 * @param accelerationFactor
	 *            factor to be passed to the {@link RealtimeRecordDelayFilter}
	 */
	public AbstractTestRealtimeRecordDelayFilter(final long[] eventTimeOffsetsSeconds,
			final long[] expectedThroughputListOffsetSecondsInterval5Secs, final double accelerationFactor) {
		this.eventTimeOffsetsSeconds = eventTimeOffsetsSeconds.clone();
		this.expectedThroughputListOffsetSecondsInterval5Secs = expectedThroughputListOffsetSecondsInterval5Secs
				.clone();
		this.accelerationFactor = accelerationFactor;
	}

	/**
	 * Initializes records, filters and the configuration for this test.
	 */
	@Before
	public void initializeTestConfiguration() {
		// Create a list of records from the given list of time stamps
		long currentTimeSeconds;
		for (final long eventDelaySeconds : this.eventTimeOffsetsSeconds) {
			currentTimeSeconds = START_TIME_SECONDS + eventDelaySeconds;
			final EmptyRecord r = new EmptyRecord();
			r.setLoggingTimestamp(TimeUnit.NANOSECONDS.convert(currentTimeSeconds, TimeUnit.SECONDS));
			this.inputRecords.add(r);
		}

		// Initialize stages and test configuration
		this.recordProducer = new InitialElementProducer<>(this.inputRecords);
		this.preDelayCounter = new Counter<>();
		this.delayFilter = new RealtimeRecordDelayFilter(TimeUnit.NANOSECONDS, this.accelerationFactor);
		final TerminationCondition terminationCondition = new TerminationCondition() {
			@Override
			public boolean isMet() {
				return AbstractTestRealtimeRecordDelayFilter.this.throughputStage.getRecordsInputPort().isClosed(); // NOPMD
			}
		};
		this.clock = new Clock(terminationCondition);
		this.clock.setInitialDelayInMs(5000);
		this.clock.setIntervalDelayInMs(5000);
		this.throughputStage = new AnalysisThroughputFilter();
		this.throughputStage.declareActive();
		this.postDelayCounter = new Counter<>();
		this.recordCollectorSink = new CollectorSink<>();
		this.throughputCollectorSink = new CollectorSink<>();

		this.testConfig = new RealtimeRecordDelayFilterConfig(this.recordProducer, this.preDelayCounter,
				this.delayFilter, this.clock, this.throughputStage, this.postDelayCounter, this.recordCollectorSink,
				this.throughputCollectorSink);
	}

	private final void checkTiming() {
		final List<Long> throughput = this.throughputCollectorSink.getElements();
		final long[] actualArray = new long[throughput.size()];
		for (int i = 0; i < actualArray.length; i++) {
			actualArray[i] = throughput.get(i);
		}
		Assert.assertArrayEquals("Unexpected throughput", this.expectedThroughputListOffsetSecondsInterval5Secs,
				actualArray);
	}

	private final void checkRelayedRecords() {
		final List<IMonitoringRecord> relayedEvents = this.recordCollectorSink.getElements();
		Assert.assertEquals(this.inputRecords, relayedEvents);
	}

	/**
	 * Tests if the records are delayed correctly by checking if the expected
	 * numbers of records arrive the expected time interval.
	 */
	@Test
	public void testNormal() {
		Assert.assertEquals(0, this.recordCollectorSink.getElements().size());

		new Execution<Configuration>(this.testConfig).executeBlocking();

		// Make sure that all events have been provided to the delay filter (otherwise
		// the test make no sense)
		Assert.assertEquals("Test invalid: Unexpected number of events provided TO the delay filter",
				this.inputRecords.size(), this.preDelayCounter.getNumElementsPassed());

		// Make sure that all events have been passed through the delay filter
		Assert.assertEquals("Unexpected number of events relayed by the delay filter", this.inputRecords.size(),
				this.postDelayCounter.getNumElementsPassed());

		// Make sure the records arrived in the expected intervals
		this.checkTiming();

		// Make sure that exactly the right objects have been passed
		this.checkRelayedRecords();
	}

	/**
	 * Test configuration for the {@link RealtimeRecordDelayFilter}.
	 *
	 * @author Lars Bluemke
	 *
	 * @since 1.13
	 */
	private static class RealtimeRecordDelayFilterConfig extends Configuration {

		public RealtimeRecordDelayFilterConfig(final InitialElementProducer<IMonitoringRecord> recordProducer,
				final Counter<IMonitoringRecord> preDelayCounter, final RealtimeRecordDelayFilter delayFilter,
				final Clock clock, final AnalysisThroughputFilter throughputStage,
				final Counter<IMonitoringRecord> postDelayCounter,
				final CollectorSink<IMonitoringRecord> recordCollectorSink,
				final CollectorSink<Long> throughputCollectorSink) {

			this.connectPorts(recordProducer.getOutputPort(), preDelayCounter.getInputPort());
			this.connectPorts(preDelayCounter.getOutputPort(), delayFilter.getInputPort());
			this.connectPorts(delayFilter.getOutputPort(), postDelayCounter.getInputPort());
			this.connectPorts(postDelayCounter.getOutputPort(), throughputStage.getRecordsInputPort());
			this.connectPorts(throughputStage.getRecordsOutputPort(), recordCollectorSink.getInputPort());
			this.connectPorts(clock.getOutputPort(), throughputStage.getTimestampsInputPort());
			this.connectPorts(throughputStage.getRecordsCountOutputPort(), throughputCollectorSink.getInputPort());
		}
	}
}
