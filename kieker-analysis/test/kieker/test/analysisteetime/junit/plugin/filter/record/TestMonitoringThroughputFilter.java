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

package kieker.test.analysisteetime.junit.plugin.filter.record;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysisteetime.plugin.filter.record.MonitoringThroughputFilter;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;

import teetime.framework.test.StageTester;

/**
 * This test is for the class {@link MonitoringThroughputFilter}.
 *
 * @author Henry Grow, Lars Bluemke
 *
 * @since 1.9
 */
public class TestMonitoringThroughputFilter {

	private MonitoringThroughputFilter monitoringThroughputFilter = null;
	// List for the count
	private List<Long> outputThroughputs = null;
	// List for all relayed records
	private List<IMonitoringRecord> outputRelayedRecords = null;
	// List for uncounted records
	private List<IMonitoringRecord> outputUncountedRecords = null;

	/**
	 * Default constructor.
	 */
	public TestMonitoringThroughputFilter() {
		// empty default constructor
	}

	/**
	 * This method prepares the test setup.
	 */
	@Before
	public void before() {
		this.monitoringThroughputFilter = new MonitoringThroughputFilter(10L);
		this.outputThroughputs = new ArrayList<Long>();
		this.outputRelayedRecords = new ArrayList<IMonitoringRecord>();
		this.outputUncountedRecords = new ArrayList<IMonitoringRecord>();
	}

	/**
	 * A simple test for the counting filter.
	 */
	@Test
	public void testNormal() {

		final List<IMonitoringRecord> inputRecords = new ArrayList<IMonitoringRecord>();

		// adding 20 records with timestamps from 10 to 29
		for (long i = 10; i < 30; i++) {
			final EmptyRecord r = new EmptyRecord();
			r.setLoggingTimestamp(i);
			inputRecords.add(r);
		}

		// adding record with timestamp 19ns
		final EmptyRecord r1 = new EmptyRecord();
		r1.setLoggingTimestamp(19L);
		inputRecords.add(r1);

		// adding 5 records with timestamps from 30 to 34
		for (long i = 30; i < 35; i++) {
			final EmptyRecord r2 = new EmptyRecord();
			r2.setLoggingTimestamp(i);
			inputRecords.add(r2);
		}

		// adding record with timestamp 40ns
		final EmptyRecord r3 = new EmptyRecord();
		r3.setLoggingTimestamp(40L);
		inputRecords.add(r3);

		StageTester.test(this.monitoringThroughputFilter)
				.and().send(inputRecords).to(this.monitoringThroughputFilter.getInputPort())
				.and().receive(this.outputThroughputs).from(this.monitoringThroughputFilter.getThroughputOutputPort())
				.and().receive(this.outputRelayedRecords).from(this.monitoringThroughputFilter.getRelayedRecordsOutputPort())
				.and().receive(this.outputUncountedRecords).from(this.monitoringThroughputFilter.getUncountedRecordsOutputPort())
				.start();

		this.checkCorrectCount();

		this.checkCorrectRelayedCount();

		this.checkCorrectUncountedCount();
	}

	private void checkCorrectCount() {
		final long firstCount = this.outputThroughputs.get(0); // first count of interval
		Assert.assertEquals("First Count should be 10 at the end of the test, but counted " + firstCount, 10,
				firstCount);
		final long secondCount = this.outputThroughputs.get(1); // second count of interval
		Assert.assertEquals("second Count should be 10 at the end of the test, but counted " + secondCount, 10,
				secondCount);
		final long thirdCount = this.outputThroughputs.get(2); // second count of interval
		Assert.assertEquals("third Count should be 5 at the end of the test, but counted " + thirdCount, 5,
				thirdCount);
	}

	private void checkCorrectRelayedCount() {
		final long count = this.outputRelayedRecords.size();
		Assert.assertEquals("Count should be 27 at the end of the test, but counted " + count, 27,
				count);
	}

	private void checkCorrectUncountedCount() {
		final long count = this.outputUncountedRecords.size();
		Assert.assertEquals("Count should be 1 at the end of the test, but counted " + count, 1,
				count);
	}
}
