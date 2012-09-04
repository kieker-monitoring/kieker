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

import junit.framework.Assert;
import junit.framework.TestCase;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.tslib.ITimeSeries;
import kieker.tools.tslib.TimeSeries;
import kieker.tools.tslib.forecast.IForecastResult;
import kieker.tools.tslib.forecast.windowstart.WindowStartForecaster;

/**
 * 
 * @author Tillmann Carlos Bielefeld
 * 
 */
public class WindowStartForecasterTest extends TestCase {

	private static final Log LOG = LogFactory.getLog(WindowStartForecasterTest.class);

	public void testWindowOfADay() {

		// Time: 2011-12-19 10:05:00 Unix: 1324285500000
		// final long startTime = 1324285500000L;
		final int windowLength = 24;
		final double valueStep = 1.01;
		final long delta = 1000 * 60 * 60;
		final long startTime = 0;
		final long endTime = startTime + (windowLength * delta);

		final TimeSeries<Double> ts =
				new TimeSeries<Double>(new Date(startTime), 1, TimeUnit.HOURS);
		LOG.info("TS so far: " + ts);
		Assert.assertEquals(new Date(startTime), ts.getStartTime());

		Double nextValue = 0.0;
		for (long insertTime = startTime; insertTime < endTime; insertTime += delta) {
			ts.append(nextValue);
			nextValue += valueStep;
			LOG.info("Inserting point: " + new Date(insertTime));
		}
		LOG.info("TS so far: " + ts);

		final WindowStartForecaster forecaster = new WindowStartForecaster(ts);
		final IForecastResult<Double> forecast = forecaster.forecast(3);

		final ITimeSeries<Double> forecastSeries = forecast.getForecast();
		Assert.assertEquals(3, forecastSeries.getPoints().size());
		Assert.assertEquals(new Date(endTime), ts.getEndTime());
		Assert.assertEquals(new Date(endTime), forecastSeries.getStartTime());
		Assert.assertEquals(new Date(endTime + delta), forecastSeries.getPoints().get(0).getTime());
		Assert.assertEquals(0 * 1.01, forecastSeries.getPoints().get(0).getValue());
		Assert.assertEquals(1 * 1.01, forecastSeries.getPoints().get(1).getValue());
		Assert.assertEquals(2 * 1.01, forecastSeries.getPoints().get(2).getValue());

	}

}
