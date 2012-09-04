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

package kieker.tools.tslib.forecast.windowstart;

import kieker.tools.tslib.ITimeSeries;
import kieker.tools.tslib.ITimeSeriesPoint;
import kieker.tools.tslib.forecast.AbstractForecaster;
import kieker.tools.tslib.forecast.ForecastResult;
import kieker.tools.tslib.forecast.IForecastResult;

/**
 * This forecaster uses the start of its timeseries window.
 * When defining a window length of e.g. a day, it gives the value of yesterday at the same time.
 * It can also be used for weeks, months, ... and ever other periodicity.
 * 
 * @author Tillmann Carlos Bielefeld
 * 
 */
public class WindowStartForecaster extends AbstractForecaster<Double> {

	public WindowStartForecaster(final ITimeSeries<Double> historyTimeseries) {
		super(historyTimeseries);
	}

	public IForecastResult<Double> forecast(final int numForecastSteps) {
		final ITimeSeries<Double> history = this.getTsOriginal();
		final ITimeSeries<Double> tsFC = this.prepareForecastTS();

		for (int i = 0; i < numForecastSteps; i++) {
			final ITimeSeriesPoint<Double> iTimeSeriesPoint = history.getPoints().get(i);
			if (null == iTimeSeriesPoint) {
				tsFC.append(Double.NaN);
			} else {
				tsFC.append(iTimeSeriesPoint.getValue());
			}
		}

		return new ForecastResult<Double>(tsFC, this.getTsOriginal());
	}

}
