package kieker.test.analysisteetime.junit.plugin.filter.sink;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysisteetime.plugin.filter.sink.GCDisplayFilter;
import kieker.common.record.jvm.GCRecord;

import teetime.framework.test.StageTester;

/**
 * Test cases for {@link GCDisplayFilter}
 *
 * @author Lars Erik Bluemke
 */
public class GCDisplayFilterTest {

	private GCDisplayFilter gcDisplayFilter = null;

	private static final int NUMBER_OF_ENTRIES = 3;
	private static final TimeUnit RECORDS_TIME_UNIT = TimeUnit.MILLISECONDS;

	private static final long TIMESTAMP = 1L;
	private static final String HOST_NAME = "test_host";
	private static final String VM_NAME = "test_vm";
	private static final String GC_NAME = "test_gc";
	private static final long COLLECTION_COUNT = 2;
	private static final long COLLECTION_TIME_MS = 3;

	private final GCRecord record = new GCRecord(TIMESTAMP, HOST_NAME, VM_NAME, GC_NAME, COLLECTION_COUNT, COLLECTION_TIME_MS);

	@Before
	public void initializeNewFilter() {
		this.gcDisplayFilter = new GCDisplayFilter(NUMBER_OF_ENTRIES, RECORDS_TIME_UNIT);
	}

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
