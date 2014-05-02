package kieker.test.tools.junit.opad.filter;

import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.tools.opad.filter.ExtendedSelfTuningForecastingFilter;
import kieker.tools.opad.record.ForecastMeasurementPair;
import kieker.tools.opad.record.NamedDoubleTimeSeriesPoint;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * 
 * @author Andreas Eberlein, Tobias Rudolph
 * 
 */
public class ExtendedSelfTuningForecastingFilterTest extends AbstractKiekerTest {

	private IAnalysisController analysisController;
	private ListReader<NamedDoubleTimeSeriesPoint> simpleListReader;
	private ListCollectionFilter<ForecastMeasurementPair> listCollectionfilter;
	private ForecastMeasurementPair forecastResult;

	/**
	 * Creates an instance of this class.
	 */
	public ExtendedSelfTuningForecastingFilterTest() {
		// Default constructor
	}

	/**
	 * Setup filter structure for test.
	 * 
	 * @throws Exception
	 *             Exceptions while setting up filter structure
	 */
	@Before
	public void setUp() throws Exception {
		final ExtendedSelfTuningForecastingFilter forecastingFilter;

		this.analysisController = new AnalysisController();
		forecastingFilter = new ExtendedSelfTuningForecastingFilter(new Configuration(), this.analysisController);
		this.simpleListReader = new ListReader<NamedDoubleTimeSeriesPoint>(new Configuration(), this.analysisController);
		this.listCollectionfilter = new ListCollectionFilter<ForecastMeasurementPair>(new Configuration(), this.analysisController);

		this.analysisController.connect(this.simpleListReader, ListReader.OUTPUT_PORT_NAME, forecastingFilter,
				ExtendedSelfTuningForecastingFilter.INPUT_PORT_NAME_TSPOINT);
		this.analysisController.connect(forecastingFilter, ExtendedSelfTuningForecastingFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, this.listCollectionfilter,
				ListCollectionFilter.INPUT_PORT_NAME);
	}

	/**
	 * Adds all values to input reader and forecasts this values in one session.
	 * 
	 * @param timeSeriesPoints
	 *            List of time series points
	 * @throws Exception
	 *             thrown during forecasting
	 */
	private void proceedForecast(final CopyOnWriteArrayList<NamedDoubleTimeSeriesPoint> timeSeriesPoints) throws Exception {
		this.simpleListReader.addAllObjects(timeSeriesPoints);
		this.analysisController.run();
		this.forecastResult = this.listCollectionfilter.getList().get(this.listCollectionfilter.getList().size() - 1);
	}
	
	/**
	 * Tests if returned forecast measurement pair contains correct application name.
	 * 
	 * @throws Exception
	 *             thrown during forecasting
	 */
	@Test
	public void testForecastAppName() throws Exception {
		final CopyOnWriteArrayList<NamedDoubleTimeSeriesPoint> tspl = new CopyOnWriteArrayList<NamedDoubleTimeSeriesPoint>();

		final long inputTime = System.nanoTime();
		final double value = 5000d;

		tspl.add(new NamedDoubleTimeSeriesPoint(inputTime, value, "Dummy.app.name"));
		this.proceedForecast(tspl);

		Assert.assertEquals("Wrong app name set in forecast measurement pair.", "Dummy.app.name", this.forecastResult.getName());
	}

	/**
	 * Tests if returned forecast measurement pair contains correct timestamp.
	 * 
	 * @throws Exception
	 *             thrown during forecasting
	 */
	@Test
	public void testForecastTime() throws Exception {
		final CopyOnWriteArrayList<NamedDoubleTimeSeriesPoint> tspl = new CopyOnWriteArrayList<NamedDoubleTimeSeriesPoint>();

		final long inputTime = System.nanoTime();
		final double value = 5000d;

		tspl.add(new NamedDoubleTimeSeriesPoint(inputTime, value, "Dummy"));
		this.proceedForecast(tspl);

		Assert.assertEquals(inputTime, this.forecastResult.getTime(), 0);
	}

	/**
	 * Tests if returned forecast measurement pair contains correct input value.
	 * 
	 * @throws Exception
	 *             thrown during forecasting
	 */
	@Test
	public void testForecastValue() throws Exception {
		final CopyOnWriteArrayList<NamedDoubleTimeSeriesPoint> tspl = new CopyOnWriteArrayList<NamedDoubleTimeSeriesPoint>();

		final long inputTime = System.nanoTime();
		final double value = 5000d;

		tspl.add(new NamedDoubleTimeSeriesPoint(inputTime, value, "Dummy"));
		this.proceedForecast(tspl);

		Assert.assertEquals(value, this.forecastResult.getValue(), 0);
	}

	/**
	 * Tests if returned forecast measurement pair contains correct forecast value with single input value.
	 * 
	 * @throws Exception
	 *             thrown during forecasting
	 */
	@Test
	public void testSingleForecast() throws Exception {
		final CopyOnWriteArrayList<NamedDoubleTimeSeriesPoint> tspl = new CopyOnWriteArrayList<NamedDoubleTimeSeriesPoint>();

		final long inputTime = System.nanoTime();
		final double value = 5000d;

		tspl.add(new NamedDoubleTimeSeriesPoint(inputTime, value, "Dummy"));
		this.proceedForecast(tspl);

		Assert.assertEquals(value, this.forecastResult.getForecasted(), 0);
	}

	/**
	 * Tests if returned forecast measurement pair contains correct forecast value with multiple input values.
	 * 
	 * @throws Exception
	 *             thrown during forecasting
	 */
	@Test
	public void testMultipleForecasts() throws Exception {
		final CopyOnWriteArrayList<NamedDoubleTimeSeriesPoint> tspl = new CopyOnWriteArrayList<NamedDoubleTimeSeriesPoint>();
		final long inputTime = System.nanoTime();

		tspl.add(new NamedDoubleTimeSeriesPoint(inputTime, 1000d, "Dummy"));
		tspl.add(new NamedDoubleTimeSeriesPoint(inputTime, 500d, "Dummy"));
		tspl.add(new NamedDoubleTimeSeriesPoint(inputTime, 250d, "Dummy"));
		tspl.add(new NamedDoubleTimeSeriesPoint(inputTime, 125d, "Dummy"));

		this.proceedForecast(tspl);
		Assert.assertEquals(468.75, this.forecastResult.getForecasted(), 0);
	}

	/**
	 * Tests if forecaster returns Double.NaN if input is Double.NaN.
	 * 
	 * @throws Exception
	 *             thrown during forecasting
	 */
	@Test
	public void testNaNForecast() throws Exception {
		final CopyOnWriteArrayList<NamedDoubleTimeSeriesPoint> tspl = new CopyOnWriteArrayList<NamedDoubleTimeSeriesPoint>();
		final long inputTime = System.nanoTime();

		tspl.add(new NamedDoubleTimeSeriesPoint(inputTime, Double.NaN, "Dummy"));

		this.proceedForecast(tspl);
		Assert.assertTrue(Double.isNaN(this.forecastResult.getForecasted()));
	}
}
