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

package kieker.analysis.junit.plugin.filter.sink;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.sink.display.GCDisplayFilter;
import kieker.common.record.jvm.GCRecord;

import kieker.test.common.junit.AbstractKiekerTest;

import teetime.framework.test.StageTester;

/**
 * Test cases for {@link GCDisplayFilter}.
 *
 * @author Lars Bluemke
 *
 * @since 1.13
 */
public class TestGCDisplayFilter extends AbstractKiekerTest {

	private static final int NUMBER_OF_ENTRIES = 3;
	private static final TimeUnit RECORDS_TIME_UNIT = TimeUnit.MILLISECONDS;

	private static final long TIMESTAMP = 1L;
	private static final String HOST_NAME = "test_host";
	private static final String VM_NAME = "test_vm";
	private static final String GC_NAME = "test_gc";
	private static final long COLLECTION_COUNT = 2;
	private static final long COLLECTION_TIME_MS = 3;

	private GCDisplayFilter gcDisplayFilter;
	private final GCRecord record = new GCRecord(TIMESTAMP, HOST_NAME, VM_NAME, GC_NAME, COLLECTION_COUNT, COLLECTION_TIME_MS);

	/**
	 * Empty default constructor.
	 */
	public TestGCDisplayFilter() {
		// empty default constructor
	}

	/**
	 * Initializes a new filter before each test.
	 */
	@Before
	public void initializeNewFilter() {
		this.gcDisplayFilter = new GCDisplayFilter(NUMBER_OF_ENTRIES, RECORDS_TIME_UNIT);
	}

	/**
	 * Tests if the xy-plot received the correct values.
	 */
	@Test
	public void xyPlotEntriesShouldBeCorrect() {
		StageTester.test(this.gcDisplayFilter).and().send(this.record).to(this.gcDisplayFilter.getInputPort()).start();

		final Date date = new Date(TimeUnit.MILLISECONDS.convert(this.record.getLoggingTimestamp(), RECORDS_TIME_UNIT));
		final String minutesAndSeconds = date.toString().substring(14, 19);

		final String id = this.record.getHostname() + " - " + this.record.getVmName() + " - " + this.record.getGcName();

		final long actualCollectionCount = this.gcDisplayFilter.getXYPlot().getEntries(id + " - " + GCDisplayFilter.COLLECTION_COUNT).get(minutesAndSeconds)
				.longValue();
		final long actualCollectionTime = this.gcDisplayFilter.getXYPlot().getEntries(id + " - " + GCDisplayFilter.COLLECTION_TIME).get(minutesAndSeconds)
				.longValue();

		Assert.assertThat(actualCollectionCount, Is.is(COLLECTION_COUNT));
		Assert.assertThat(actualCollectionTime, Is.is(COLLECTION_TIME_MS));

	}

}
