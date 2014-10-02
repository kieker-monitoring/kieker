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

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import kieker.tools.tslib.ITimeSeries;
import kieker.tools.tslib.TimeSeries;
import kieker.tools.tslib.forecast.IForecastResult;
import kieker.tools.tslib.forecast.ses.SESRForecaster;

import kieker.test.tools.junit.AbstractKiekerRTest;

/**
 *
 * @author Tillmann Carlos Bielefeld
 * @since 1.10
 *
 */
public class SESRForecasterTest extends AbstractKiekerRTest {

	/**
	 * Creates a new instance of this class.
	 */
	public SESRForecasterTest() {
		// Default constructor
	}

	/**
	 * Test of the SESRForecaster via Rserve.
	 */
	@Test
	public void test() {
		final int deltaTime = 1000;
		final TimeUnit timeUnit = TimeUnit.MILLISECONDS;
		final long startTime = System.currentTimeMillis() - (deltaTime * 10);

		final TimeSeries<Double> ts = new TimeSeries<Double>(startTime, TimeUnit.NANOSECONDS, deltaTime, timeUnit);
		ts.append(1.0);
		ts.append(2.0);
		ts.append(3.0);
		ts.append(1.0);
		ts.append(2.0);
		ts.append(3.0);

		final SESRForecaster forecaster = new SESRForecaster(ts);
		final IForecastResult forecast = forecaster.forecast(1);
		final ITimeSeries<Double> forecastSeries = forecast.getForecast();

		// final ITimeSeriesPoint<Double> stepFC = forecastSeries.getPoints().get(0);
		Assert.assertEquals(2.000054d, AbstractKiekerRTest.getTsPoint(forecastSeries), 0.001d);
	}
}
