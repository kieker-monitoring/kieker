/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.analysisteetime.junit.plugin.filter.sink;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.sink.display.ThreadsStatusDisplayFilter;
import kieker.common.record.jvm.ThreadsStatusRecord;

import kieker.test.common.junit.AbstractKiekerTest;

import teetime.framework.test.StageTester;

/**
 * Test cases for {@link ThreadsStatusDisplayFilter}.
 *
 * @author Lars Bluemke
 *
 * @since 1.13
 */
public class TestThreadsStatusDisplayFilter extends AbstractKiekerTest {

	private static final int NUMBER_OF_ENTRIES = 3;
	private static final TimeUnit RECORDS_TIME_UNIT = TimeUnit.MILLISECONDS;

	private static final long TIMESTAMP = 1L;
	private static final String HOST_NAME = "test_host";
	private static final String VM_NAME = "test_vm";
	private static final long THREAD_COUNT = 2;
	private static final long DAEMON_THREAD_COUNT = 1;
	private static final long PEAK_THREAD_COUNT = 4;
	private static final long TOTAL_STARTED_THREAD_COUNT = 3;

	private ThreadsStatusDisplayFilter threadsStatusFilter;
	private final ThreadsStatusRecord record = new ThreadsStatusRecord(TIMESTAMP, HOST_NAME, VM_NAME, THREAD_COUNT, DAEMON_THREAD_COUNT, PEAK_THREAD_COUNT,
			TOTAL_STARTED_THREAD_COUNT);

	/**
	 * Empty default constructor.
	 */
	public TestThreadsStatusDisplayFilter() {
		// empty default constructor
	}

	/**
	 * Initializes a new filter before each test.
	 */
	@Before
	public void initializeNewFilter() {
		this.threadsStatusFilter = new ThreadsStatusDisplayFilter(NUMBER_OF_ENTRIES, RECORDS_TIME_UNIT);
	}

	/**
	 * Tests if the xy-plot received the correct values.
	 */
	@Test
	public void xyPlotEntriesShouldBeCorrect() {
		StageTester.test(this.threadsStatusFilter).and().send(this.record).to(this.threadsStatusFilter.getInputPort()).start();

		final Date date = new Date(TimeUnit.MILLISECONDS.convert(this.record.getLoggingTimestamp(), RECORDS_TIME_UNIT));
		final String minutesAndSeconds = date.toString().substring(14, 19);

		final String id = this.record.getHostname() + " - " + this.record.getVmName();

		final long actualThreadCount = this.threadsStatusFilter.getXYPlot().getEntries(id + " - " + ThreadsStatusDisplayFilter.THREADS).get(minutesAndSeconds)
				.longValue();
		final long actualTotalStartedThreadCount = this.threadsStatusFilter.getXYPlot().getEntries(id + " - " + ThreadsStatusDisplayFilter.TOTAL_THREADS)
				.get(minutesAndSeconds).longValue();
		final long actualPeakThreadCount = this.threadsStatusFilter.getXYPlot().getEntries(id + " - " + ThreadsStatusDisplayFilter.PEAK_THREADS)
				.get(minutesAndSeconds).longValue();
		final long actualDaemonThreadCount = this.threadsStatusFilter.getXYPlot().getEntries(id + " - " + ThreadsStatusDisplayFilter.DAEMON_THREADS)
				.get(minutesAndSeconds).longValue();

		Assert.assertThat(actualThreadCount, Is.is(THREAD_COUNT));
		Assert.assertThat(actualTotalStartedThreadCount, Is.is(TOTAL_STARTED_THREAD_COUNT));
		Assert.assertThat(actualPeakThreadCount, Is.is(PEAK_THREAD_COUNT));
		Assert.assertThat(actualDaemonThreadCount, Is.is(DAEMON_THREAD_COUNT));

	}

}
