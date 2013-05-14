/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.tools.tslib.ITimeSeries;
import kieker.tools.tslib.ITimeSeriesPoint;
import kieker.tools.tslib.TimeSeries;
import kieker.tools.tslib.forecast.IForecastResult;
import kieker.tools.tslib.forecast.mean.MeanForecaster;

/**
 * @author Tillmann Carlos Bielefeld
 */
public class MeanForecasterTest {
	private TimeSeries<Double> ts;
	private TimeUnit timeUnit;
	private long startTime;
	private int steps;
	private MeanForecaster forecaster;
	private IForecastResult<Double> forecast;
	private final int confidenceLevel = 95; // 95%
	private ITimeSeries<Double> forecastSeries;
	private ITimeSeries<Double> upperSeries;
	private ITimeSeries<Double> lowerSeries;
	private long deltaTime;
	private Double mean;

	@Before
	public void setUp() throws Exception {
		this.deltaTime = 1000;
		this.timeUnit = TimeUnit.MILLISECONDS;
		this.startTime = System.currentTimeMillis() - (this.deltaTime * 10);

		this.initForecastWithTimeUnit(this.timeUnit);
	}

	/**
	 * @param timeUnit
	 */
	private void initForecastWithTimeUnit(final TimeUnit timeUnit) {
		this.ts = new TimeSeries<Double>(this.startTime, this.deltaTime, timeUnit);

		this.steps = 1;
		this.mean = new Double(2.0);
		this.ts.append(this.mean - 1);
		this.ts.append(this.mean);
		this.ts.append(this.mean + 1);
		this.forecaster = new MeanForecaster(this.ts, this.confidenceLevel);
		this.forecast = this.forecaster.forecast(this.steps);
		this.forecastSeries = this.forecast.getForecast();
		this.upperSeries = this.forecast.getUpper();
		this.lowerSeries = this.forecast.getLower();
	}

	@Test
	public void testForecastStartingIsAccordingToLastAppend() {
		Assert.assertEquals(this.ts, this.forecaster.getTsOriginal());

		// we added three timepoints, so we must be here:
		final long expectedStartTime = this.startTime + (this.deltaTime * 4);
		Assert.assertEquals(new Date(expectedStartTime), this.forecastSeries.getStartTime());
	}

	/**
	 * Compute the starting point with a different time unit
	 */
	@Test
	public void testForecastStartingIsAccordingToLastAppendSecondsTU() {
		this.initForecastWithTimeUnit(TimeUnit.SECONDS);

		final long expectedStartTime = this.startTime + TimeUnit.MILLISECONDS.convert(this.deltaTime * 4, TimeUnit.SECONDS);
		Assert.assertEquals(new Date(expectedStartTime), this.forecastSeries.getStartTime());
	}

	@Test
	public void testMeanCalculationOneStep() {
		Assert.assertEquals(this.steps, this.forecastSeries.size());

		final ITimeSeriesPoint<Double> stepFC = this.forecast.getForecast().getPoints().get(0);
		Assert.assertEquals(this.mean, stepFC.getValue());
	}

	@Test
	public void testLowerCalculationOneStep() {
		Assert.assertEquals(this.steps, this.lowerSeries.size());

		final ITimeSeriesPoint<Double> stepFC = this.forecast.getLower().getPoints().get(0);
		Assert.assertTrue(this.mean > stepFC.getValue());
	}

	@Test
	public void testUpperCalculationOneStep() {
		Assert.assertEquals(this.steps, this.upperSeries.size());

		final ITimeSeriesPoint<Double> stepFC = this.forecast.getUpper().getPoints().get(0);
		Assert.assertTrue(this.mean < stepFC.getValue());
	}
}
