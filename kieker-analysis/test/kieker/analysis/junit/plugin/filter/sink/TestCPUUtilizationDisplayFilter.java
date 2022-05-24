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

package kieker.analysis.junit.plugin.filter.sink;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.generic.sink.display.CPUUtilizationDisplaySink;
import kieker.common.record.system.CPUUtilizationRecord;

import kieker.test.common.junit.AbstractKiekerTest;

import teetime.framework.test.StageTester;

/**
 * Test cases for {@link CPUUtilizationDisplaySink}.
 *
 * @author Lars Bluemke
 *
 * @since 1.13
 */
public class TestCPUUtilizationDisplayFilter extends AbstractKiekerTest {

	// Constructor arguments for filter
	private static final int NUMBER_OF_ENTRIES = 3;
	private static final TimeUnit RECORDS_TIME_UNIT = TimeUnit.MILLISECONDS;
	private static final Number[] WARNING_INTERVALS = { 1, 2, 3 };

	// Record data
	private static final long TIMESTAMP = 1L;
	private static final String HOSTNAME = "test_host";
	private static final String CPU_ID = "cpu_1";
	private static final double USER = 2.0;
	private static final double SYSTEM = 3.0;
	private static final double WAIT = 4.0;
	private static final double NICE = 5.0;
	private static final double IRQ = 6.0;
	private static final double TOTAL_UTILISATION = 7.0;
	private static final double IDLE = 8.0;

	private CPUUtilizationDisplaySink cpuUtilFilter;
	private final CPUUtilizationRecord record = new CPUUtilizationRecord(TIMESTAMP, HOSTNAME, CPU_ID, USER, SYSTEM, WAIT, NICE, IRQ, TOTAL_UTILISATION, IDLE);
	private final String id = this.record.getHostname() + " - " + this.record.getCpuID();

	/**
	 * Empty default constructor.
	 */
	public TestCPUUtilizationDisplayFilter() {
		// empty default constructor
	}

	/**
	 * Initializes a new filter before each test.
	 */
	@Before
	public void initializeNewFilter() {
		this.cpuUtilFilter = new CPUUtilizationDisplaySink(NUMBER_OF_ENTRIES, WARNING_INTERVALS, RECORDS_TIME_UNIT);
	}

	/**
	 * Tests if the meter gauge received the correct values.
	 */
	@Test
	public void meterGaugeValueShouldBeCorrect() {
		StageTester.test(this.cpuUtilFilter).and().send(this.record).to(this.cpuUtilFilter.getInputPort()).start();

		Assert.assertThat(this.cpuUtilFilter.getMeterGauge().getValue(this.id).doubleValue() / 100, Is.is(TOTAL_UTILISATION));
	}

	/**
	 * Tests if the xy-plot received the correct values.
	 */
	@Test
	public void xyPlotEntriesShouldBeCorrect() {
		StageTester.test(this.cpuUtilFilter).and().send(this.record).to(this.cpuUtilFilter.getInputPort()).start();

		final Date date = new Date(TimeUnit.MILLISECONDS.convert(this.record.getLoggingTimestamp(), RECORDS_TIME_UNIT));
		final String minutesAndSeconds = date.toString().substring(14, 19);

		final double actualUser = this.cpuUtilFilter.getXYPlot().getEntries(this.id + " - " + CPUUtilizationDisplaySink.USER).get(minutesAndSeconds).doubleValue();
		final double actualSystem = this.cpuUtilFilter.getXYPlot().getEntries(this.id + " - " + CPUUtilizationDisplaySink.SYSTEM).get(minutesAndSeconds)
				.doubleValue();
		final double actualNice = this.cpuUtilFilter.getXYPlot().getEntries(this.id + " - " + CPUUtilizationDisplaySink.NICE).get(minutesAndSeconds).doubleValue();
		final double actualIrq = this.cpuUtilFilter.getXYPlot().getEntries(this.id + " - " + CPUUtilizationDisplaySink.IRQ).get(minutesAndSeconds).doubleValue();
		final double actualTotalUtilization = this.cpuUtilFilter.getXYPlot().getEntries(this.id + " - " + CPUUtilizationDisplaySink.TOTAL_UTILIZATION)
				.get(minutesAndSeconds).doubleValue();
		final double actualIdle = this.cpuUtilFilter.getXYPlot().getEntries(this.id + " - " + CPUUtilizationDisplaySink.IDLE).get(minutesAndSeconds).doubleValue();

		Assert.assertThat(actualUser / 100, Is.is(USER));
		Assert.assertThat(actualSystem / 100, Is.is(SYSTEM));
		Assert.assertThat(actualNice / 100, Is.is(NICE));
		Assert.assertThat(actualIrq / 100, Is.is(IRQ));
		Assert.assertThat(actualTotalUtilization / 100, Is.is(TOTAL_UTILISATION));
		Assert.assertThat(actualIdle / 100, Is.is(IDLE));
	}
}
