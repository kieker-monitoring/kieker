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

package kieker.test.analysisteetime.junit.plugin.filter.forward;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import kieker.analysisteetime.plugin.filter.forward.AnalysisThroughputFilter2;
import kieker.analysisteetime.plugin.filter.record.delayfilter.RealtimeRecordDelayFilter;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;

import teetime.framework.Configuration;
import teetime.framework.Execution;
import teetime.stage.Clock;
import teetime.stage.CollectorSink;
import teetime.stage.InitialElementProducer;

/**
 * @author LarsBlumke
 *
 * @since 1.13
 */
public class TestAnalysisThroughputFilter2 {

	private static final long START_TIME_SECONDS = 246561L;
	private static final long[] EVENT_TIME_OFFSETS_SECONDS = { 0L, 1L, 2L, 7L, 17L, 19L }; // relative to the start time
	// intervals of length INTERVAL_SIZE_NANOS relative to start time
	private static final long[] EXPECTED_THROUGHPUT_LIST_OFFSET_SECS_INTERVAL_5SECS = {
		3L, // i.e., in interval (0,5(
		1L, // i.e., in interval (5,10(
		0L, // i.e., in interval (10,15(
		2L, // i.e., in interval (15,20(
	};

	private AnalysisThroughputFilterConfig testConfig;

	@Before
	public void initializeConfig() {
		long currentTimeSeconds;
		final List<EmptyRecord> inputRecords = new ArrayList<EmptyRecord>();

		for (final long eventDelaySeconds : EVENT_TIME_OFFSETS_SECONDS) {
			currentTimeSeconds = START_TIME_SECONDS + eventDelaySeconds;
			final EmptyRecord r = new EmptyRecord();
			r.setLoggingTimestamp(TimeUnit.NANOSECONDS.convert(currentTimeSeconds, TimeUnit.SECONDS));
			inputRecords.add(r);
		}

		this.testConfig = new AnalysisThroughputFilterConfig(inputRecords);
	}

	@Test
	public void throughputsShouldBeCorrect() {
		new Execution<Configuration>(this.testConfig).executeBlocking();

		for (final long tp : this.testConfig.getThroughputs()) {
			System.out.println(tp);
		}
	}

}

class AnalysisThroughputFilterConfig extends Configuration {
	private final AnalysisThroughputFilter2 throughputFilter;

	public AnalysisThroughputFilterConfig(final List<EmptyRecord> inputRecords) {
		final InitialElementProducer<EmptyRecord> recordProducer = new InitialElementProducer<EmptyRecord>(inputRecords);
		final RealtimeRecordDelayFilter delayFilter = new RealtimeRecordDelayFilter(TimeUnit.NANOSECONDS, 1, 2L);
		final Clock clock = new Clock();
		clock.setInitialDelayInMs(5000);
		clock.setIntervalDelayInMs(5000);
		clock.declareActive();
		this.throughputFilter = new AnalysisThroughputFilter2();
		this.throughputFilter.declareActive();
		final CollectorSink<IMonitoringRecord> recordCollectorSink = new CollectorSink<IMonitoringRecord>();

		this.connectPorts(recordProducer.getOutputPort(), delayFilter.getInputPort());
		this.connectPorts(delayFilter.getOutputPort(), this.throughputFilter.getInputPort());
		this.connectPorts(clock.getOutputPort(), this.throughputFilter.getTriggerInputPort());
		this.connectPorts(this.throughputFilter.getOutputPort(), recordCollectorSink.getInputPort());

	}

	protected List<Long> getThroughputs() {
		return this.throughputFilter.getThroughputs();

	}
}
