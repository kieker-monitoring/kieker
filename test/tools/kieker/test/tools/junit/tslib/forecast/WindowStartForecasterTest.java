/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
import org.junit.Ignore;
import org.junit.Test;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.tslib.ITimeSeries;
import kieker.tools.tslib.TimeSeries;
import kieker.tools.tslib.forecast.IForecastResult;
import kieker.tools.tslib.forecast.windowstart.WindowStartForecaster;

import kieker.test.tools.junit.AbstractKiekerRTest;

/**
 * @author Tillmann Carlos Bielefeld
 * @since 1.10
 */
public class WindowStartForecasterTest extends AbstractKiekerRTest {

	private static final Log LOG = LogFactory.getLog(WindowStartForecasterTest.class);

	/**
	 * Creates a new instance of this class.
	 */
	public WindowStartForecasterTest() {
		// Default constructor
	}

	/**
	 * Test of the WindowStartForecaster for the seasonal pattern of one day.
	 */
	@Test
	@Ignore("Deactivated RServe Script (#1420)")
	public void testWindowOfADay() {

		// Time: 2011-12-19 10:05:00 Unix: 1324285500000
		// final long startTime = 1324285500000L;
		final int windowLength = 24;
		final double valueStep = 1.01;
		final long delta = 1000 * 60 * 60;
		final long startTime = 0;
		final long endTime = startTime + (windowLength * (delta - 1));

		final TimeSeries<Double> ts = new TimeSeries<Double>(startTime, TimeUnit.MILLISECONDS, 60 * 60, TimeUnit.SECONDS);
		LOG.info("TS so far: " + ts);
		Assert.assertEquals(startTime, ts.getStartTime());

		Double nextValue = 0.0;
		for (long insertTime = startTime; insertTime < endTime; insertTime += delta) {
			ts.append(nextValue);
			nextValue += valueStep;
			LOG.info("Inserting point: " + new Date(insertTime));
		}
		LOG.info("TS so far: " + ts);

		final WindowStartForecaster forecaster = new WindowStartForecaster(ts);
		final IForecastResult forecast = forecaster.forecast(3);

		final ITimeSeries<Double> forecastSeries = forecast.getForecast();
		Assert.assertEquals(3, forecastSeries.getPoints().size());
		Assert.assertEquals(endTime, ts.getEndTime());
		Assert.assertEquals(endTime, forecastSeries.getStartTime());
		Assert.assertEquals(endTime + delta, forecastSeries.getPoints().get(0).getTime());
		Assert.assertEquals(Double.valueOf(0 * 1.01), forecastSeries.getPoints().get(0).getValue());
		Assert.assertEquals(Double.valueOf(1 * 1.01), forecastSeries.getPoints().get(1).getValue());
		Assert.assertEquals(Double.valueOf(2 * 1.01), forecastSeries.getPoints().get(2).getValue());

	}

}
