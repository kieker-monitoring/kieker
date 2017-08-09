/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.tslib.forecast;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.tools.opad.timeseries.ITimeSeries;
import kieker.tools.opad.timeseries.ITimeSeriesPoint;
import kieker.tools.opad.timeseries.TimeSeries;
import kieker.tools.opad.timeseries.forecast.IForecastResult;
import kieker.tools.opad.timeseries.forecast.mean.MeanForecaster;

import kieker.test.tools.junit.AbstractKiekerRTest;

/**
 * @since 1.10
 * @author Tillmann Carlos Bielefeld
 */
public class MeanForecasterTest extends AbstractKiekerRTest {
	private static final int CONFIDENCE_LEVEL = 95; // 95%
	private TimeSeries<Double> ts;
	private long startTime;
	private int steps;
	private MeanForecaster forecaster;
	private IForecastResult forecast;
	private ITimeSeries<Double> forecastSeries;
	private ITimeSeries<Double> upperSeries;
	private ITimeSeries<Double> lowerSeries;
	private long deltaTime;
	private Double mean;

	/**
	 * Creates a new instance of this class.
	 */
	public MeanForecasterTest() {
		// Default constructor
	}

	/**
	 * Set up of the MeanForecasterTest.
	 *
	 * @throws Exception
	 *             If exception appears
	 */
	@Before
	public void setUp() throws Exception {
		final TimeUnit timeUnit = TimeUnit.MILLISECONDS;

		this.deltaTime = 1000;
		this.startTime = System.currentTimeMillis() - (this.deltaTime * 10);

		this.initForecastWithTimeUnit(timeUnit);
	}

	/**
	 * Initiation of the test, setting up the test time series.
	 *
	 * @param timeUnit
	 *            Used time unit
	 */
	private void initForecastWithTimeUnit(final TimeUnit tu) {
		this.ts = new TimeSeries<Double>(this.startTime, TimeUnit.MILLISECONDS, this.deltaTime, tu);

		this.steps = 1;
		this.mean = Double.valueOf(2.0);
		this.ts.append(this.mean - 2);
		this.ts.append(this.mean - 1);
		this.ts.append(this.mean);
		this.ts.append(this.mean + 1);
		this.ts.append(this.mean + 2);
		this.forecaster = new MeanForecaster(this.ts, MeanForecasterTest.CONFIDENCE_LEVEL);
		this.forecast = this.forecaster.forecast(this.steps);
		this.forecastSeries = this.forecast.getForecast();
		this.upperSeries = this.forecast.getUpper();
		this.lowerSeries = this.forecast.getLower();
	}

	/**
	 * Test of the MeanForecater via Rserve.
	 */
	@Test
	public void testForecastStartingIsAccordingToLastAppend() {
		Assert.assertEquals(this.ts, this.forecaster.getTsOriginal());

		// we added three timepoints, so we must be here:
		final long expectedStartTime = this.startTime;
		Assert.assertEquals(expectedStartTime, this.forecastSeries.getStartTime());
	}

	/**
	 * Compute the starting point with a different time unit.
	 */
	@Test
	public void testForecastStartingIsAccordingToLastAppendSecondsTU() {
		this.initForecastWithTimeUnit(TimeUnit.SECONDS);

		final long expectedStartTime = this.startTime;
		Assert.assertEquals(expectedStartTime, this.forecastSeries.getStartTime());
	}

	/**
	 * One step of a mean calculation.
	 */
	@Test
	public void testMeanCalculationOneStep() {
		Assert.assertEquals(this.steps, this.forecastSeries.size());

		final ITimeSeriesPoint<Double> stepFC = this.forecast.getForecast().getPoints().get(0);
		Assert.assertEquals(this.mean, stepFC.getValue());
	}

	/**
	 * Test if the correct calculation is done. In this case mean is bigger than the lower bounds.
	 */
	@Test
	public void testLowerCalculationOneStep() {
		Assert.assertEquals(this.steps, this.lowerSeries.size());

		final ITimeSeriesPoint<Double> stepFC = this.forecast.getLower().getPoints().get(0);
		Assert.assertTrue(this.mean > stepFC.getValue());
	}

	/**
	 * Test if the correct calculation is done. In this case mean is smaller than the upper bounds.
	 */
	@Test
	public void testUpperCalculationOneStep() {
		Assert.assertEquals(this.steps, this.upperSeries.size());

		final ITimeSeriesPoint<Double> stepFC = this.forecast.getUpper().getPoints().get(0);
		Assert.assertTrue(this.mean < stepFC.getValue());
	}
}
