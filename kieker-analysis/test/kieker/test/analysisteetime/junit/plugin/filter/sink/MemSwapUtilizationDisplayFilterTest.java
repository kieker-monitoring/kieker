package kieker.test.analysisteetime.junit.plugin.filter.sink;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysisteetime.plugin.filter.sink.MemSwapUtilizationDisplayFilter;
import kieker.common.record.system.MemSwapUsageRecord;

import teetime.framework.test.StageTester;

/**
 * Test cases for {@link MemSwapUtilizationDisplayStage}
 *
 * @author Lars Erik Bluemke
 */
public class MemSwapUtilizationDisplayFilterTest {

	private MemSwapUtilizationDisplayFilter memSwapUtilFilter = null;

	private static final int NUMBER_OF_ENTRIES = 3;
	private static final TimeUnit RECORDS_TIME_UNIT = TimeUnit.MILLISECONDS;

	private static final long TIMESTAMP = 1L;
	private static final String HOST_NAME = "test_host";
	private static final long MEM_TOTAL = 2097152000L;
	private static final long MEM_USED = 1572864000L;
	private static final long MEM_FREE = 524288000L;
	private static final long SWAP_TOTAL = 20971520L;
	private static final long SWAP_USED = 15728640L;
	private static final long SWAR_FREE = 5242880L;

	private final MemSwapUsageRecord record = new MemSwapUsageRecord(TIMESTAMP, HOST_NAME, MEM_TOTAL, MEM_USED, MEM_FREE, SWAP_TOTAL, SWAP_USED, SWAR_FREE);
	private final String id = this.record.getHostname();

	@Before
	public void initializeNewFilter() {
		this.memSwapUtilFilter = new MemSwapUtilizationDisplayFilter(NUMBER_OF_ENTRIES, RECORDS_TIME_UNIT);
	}

	@Test
	public void xyPlotEntriesShouldBeCorrect() {
		StageTester.test(this.memSwapUtilFilter).and().send(this.record).to(this.memSwapUtilFilter.getInputPort()).start();

		final Date date = new Date(TimeUnit.MILLISECONDS.convert(this.record.getLoggingTimestamp(), RECORDS_TIME_UNIT));
		final String minutesAndSeconds = date.toString().substring(14, 19);

		final long actualMemTotal = this.memSwapUtilFilter.getXYPlot().getEntries(this.id + " - " + MemSwapUtilizationDisplayFilter.MEM_TOTAL).get(minutesAndSeconds)
				.longValue();
		final long actualMemUsed = this.memSwapUtilFilter.getXYPlot().getEntries(this.id + " - " + MemSwapUtilizationDisplayFilter.MEM_USED).get(minutesAndSeconds)
				.longValue();
		final long actualMemFree = this.memSwapUtilFilter.getXYPlot().getEntries(this.id + " - " + MemSwapUtilizationDisplayFilter.MEM_FREE).get(minutesAndSeconds)
				.longValue();
		final long actualSwapTotal = this.memSwapUtilFilter.getXYPlot().getEntries(this.id + " - " + MemSwapUtilizationDisplayFilter.SWAP_TOTAL)
				.get(minutesAndSeconds).longValue();
		final long actualSwapUsed = this.memSwapUtilFilter.getXYPlot().getEntries(this.id + " - " + MemSwapUtilizationDisplayFilter.SWAP_USED).get(minutesAndSeconds)
				.longValue();
		final long actualSwapFree = this.memSwapUtilFilter.getXYPlot().getEntries(this.id + " - " + MemSwapUtilizationDisplayFilter.SWAP_FREE).get(minutesAndSeconds)
				.longValue();

		// Converting back form MB to Byte
		Assert.assertThat(actualMemTotal * 1048576, Is.is(MEM_TOTAL));
		Assert.assertThat(actualMemUsed * 1048576, Is.is(MEM_USED));
		Assert.assertThat(actualMemFree * 1048576, Is.is(MEM_FREE));
		Assert.assertThat(actualSwapTotal * 1048576, Is.is(SWAP_TOTAL));
		Assert.assertThat(actualSwapUsed * 1048576, Is.is(SWAP_USED));
		Assert.assertThat(actualSwapFree * 1048576, Is.is(SWAR_FREE));

	}

	@Test
	public void memPieChartShouldBeCorrect() {
		StageTester.test(this.memSwapUtilFilter).and().send(this.record).to(this.memSwapUtilFilter.getInputPort()).start();

		final long actualMemFree = this.memSwapUtilFilter.getMemPieChart().getValue(this.id + " - " + MemSwapUtilizationDisplayFilter.MEM_FREE).longValue();
		final long actualMemUsed = this.memSwapUtilFilter.getMemPieChart().getValue(this.id + " - " + MemSwapUtilizationDisplayFilter.MEM_USED).longValue();

		Assert.assertThat(actualMemFree, Is.is(MEM_FREE));
		Assert.assertThat(actualMemUsed, Is.is(MEM_USED));

	}

	@Test
	public void swapPieChartShouldBeCorrect() {
		StageTester.test(this.memSwapUtilFilter).and().send(this.record).to(this.memSwapUtilFilter.getInputPort()).start();

		final long actualSwapFree = this.memSwapUtilFilter.getSwapPieChart().getValue(this.id + " - " + MemSwapUtilizationDisplayFilter.SWAP_FREE).longValue();
		final long actualSwapUsed = this.memSwapUtilFilter.getSwapPieChart().getValue(this.id + " - " + MemSwapUtilizationDisplayFilter.SWAP_USED).longValue();

		Assert.assertThat(actualSwapFree, Is.is(SWAR_FREE));
		Assert.assertThat(actualSwapUsed, Is.is(SWAP_USED));
	}

}
