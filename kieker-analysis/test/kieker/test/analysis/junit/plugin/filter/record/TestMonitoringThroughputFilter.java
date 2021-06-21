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

package kieker.test.analysis.junit.plugin.filter.record;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.CountingFilter;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.filter.record.MonitoringThroughputFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.misc.EmptyRecord;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * This test is for the class {@link MonitoringThroughputFilter}.
 *
 * @author Henry Grow
 *
 * @since 1.9
 */
public class TestMonitoringThroughputFilter extends AbstractKiekerTest {

	private IAnalysisController analysisController;

	private ListReader<EmptyRecord> simpleListReader;

	// sink for all relayed records
	private ListCollectionFilter<Long> sinkPluginRelayedRecords;

	// sink for uncounted records
	private ListCollectionFilter<Long> sinkPluginUncountedRecords;

	// sink the count
	private ListCollectionFilter<Long> sinkPluginCount;

	/**
	 * Default constructor.
	 */
	public TestMonitoringThroughputFilter() {
		// empty default constructor
	}

	/**
	 * This method prepares the test setup.
	 *
	 * @throws IllegalStateException
	 *             If something went wrong during the test setup (should not happen).
	 * @throws AnalysisConfigurationException
	 *             If something went wrong during the test setup (should not happen).
	 */
	@Before
	public void before() throws IllegalStateException, AnalysisConfigurationException {
		final MonitoringThroughputFilter monitoringThroughputFilter;
		final CountingFilter relayedRecordsCountingFilter;
		final CountingFilter uncountedRecordsCountingFilter;
		final Configuration configuration;

		this.analysisController = new AnalysisController();

		configuration = new Configuration();
		configuration.setProperty(MonitoringThroughputFilter.CONFIG_PROPERTY_NAME_INTERVAL_SIZE, "10");
		configuration.setProperty(MonitoringThroughputFilter.CONFIG_PROPERTY_NAME_TIMEUNIT, TimeUnit.NANOSECONDS.name());

		// ListReader
		this.simpleListReader = new ListReader<>(new Configuration(), this.analysisController);

		// MonitoringThroughPut
		monitoringThroughputFilter = new MonitoringThroughputFilter(configuration, this.analysisController);
		this.analysisController.connect(this.simpleListReader, ListReader.OUTPUT_PORT_NAME, monitoringThroughputFilter,
				MonitoringThroughputFilter.INPUT_PORT_NAME_RECORDS);

		// uncounted Records CountingFilter
		uncountedRecordsCountingFilter = new CountingFilter(configuration, this.analysisController);
		this.analysisController.connect(monitoringThroughputFilter, MonitoringThroughputFilter.OUTPUT_PORT_NAME_UNCOUNTED_RECORDS,
				uncountedRecordsCountingFilter, CountingFilter.INPUT_PORT_NAME_EVENTS);

		// relayed Records CountingFilter
		relayedRecordsCountingFilter = new CountingFilter(configuration, this.analysisController);
		this.analysisController.connect(monitoringThroughputFilter, MonitoringThroughputFilter.OUTPUT_PORT_NAME_RELAYED_RECORDS,
				relayedRecordsCountingFilter, CountingFilter.INPUT_PORT_NAME_EVENTS);

		// sink for uncounted records
		this.sinkPluginUncountedRecords = new ListCollectionFilter<>(configuration, this.analysisController);
		this.analysisController.connect(uncountedRecordsCountingFilter, CountingFilter.OUTPUT_PORT_NAME_COUNT,
				this.sinkPluginUncountedRecords, ListCollectionFilter.INPUT_PORT_NAME);

		// sink for all relayed records
		this.sinkPluginRelayedRecords = new ListCollectionFilter<>(configuration, this.analysisController);
		this.analysisController.connect(relayedRecordsCountingFilter, CountingFilter.OUTPUT_PORT_NAME_COUNT,
				this.sinkPluginRelayedRecords, ListCollectionFilter.INPUT_PORT_NAME);

		// sink for the count
		this.sinkPluginCount = new ListCollectionFilter<>(configuration, this.analysisController);
		this.analysisController.connect(monitoringThroughputFilter, MonitoringThroughputFilter.OUTPUT_PORT_NAME_THROUGHPUT,
				this.sinkPluginCount, ListCollectionFilter.INPUT_PORT_NAME);
	}

	/**
	 * A simple test for the counting filter.
	 *
	 * @throws IllegalStateException
	 *             If the test setup is somehow invalid (should not happen).
	 * @throws AnalysisConfigurationException
	 *             If the test setup is somehow invalid (should not happen).
	 */
	@Test
	public void testNormal() throws IllegalStateException, AnalysisConfigurationException {
		final List<EmptyRecord> records = new ArrayList<>();

		// adding 20 records with timestamps from 10 to 29
		for (long i = 10; i < 30; i++) {
			final EmptyRecord r = new EmptyRecord();
			r.setLoggingTimestamp(i);
			records.add(r);
		}

		// adding record with timestamp 19ns
		final EmptyRecord r1 = new EmptyRecord();
		r1.setLoggingTimestamp(19L);
		records.add(r1);

		// adding 5 records with timestamps from 30 to 34
		for (long i = 30; i < 35; i++) {
			final EmptyRecord r2 = new EmptyRecord();
			r2.setLoggingTimestamp(i);
			records.add(r2);
		}

		// adding record with timestamp 40ns
		final EmptyRecord r3 = new EmptyRecord();
		r3.setLoggingTimestamp(40L);
		records.add(r3);

		this.simpleListReader.addAllObjects(records);

		this.analysisController.run();

		Assert.assertEquals(AnalysisController.STATE.TERMINATED, this.analysisController.getState());

		this.checkCorrectCount();

		this.checkCorrectRelayedCount();

		this.checkCorrectUncountedCount();
	}

	private void checkCorrectCount() {
		final List<Long> counts = this.sinkPluginCount.getList();
		final long firstCount = counts.get(0); // first count of interval
		Assert.assertEquals("First Count should be 10 at the end of the test, but counted " + firstCount, 10,
				firstCount);
		final long secondCount = counts.get(1); // second count of interval
		Assert.assertEquals("second Count should be 10 at the end of the test, but counted " + secondCount, 10,
				secondCount);
		final long thirdCount = counts.get(2); // second count of interval
		Assert.assertEquals("third Count should be 5 at the end of the test, but counted " + thirdCount, 5,
				thirdCount);
	}

	private void checkCorrectRelayedCount() {
		final List<Long> counts = this.sinkPluginRelayedRecords.getList();
		final long count = counts.get(counts.size() - 1);
		Assert.assertEquals("Count should be 27 at the end of the test, but counted " + count, 27,
				count);
	}

	private void checkCorrectUncountedCount() {
		final List<Long> counts = this.sinkPluginUncountedRecords.getList();
		final long count = counts.get(0);
		Assert.assertEquals("Count should be 1 at the end of the test, but counted " + count, 1,
				count);
	}
}
