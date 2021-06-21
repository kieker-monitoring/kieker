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

package kieker.tools.opad.timeseries.forecast.ets;

import kieker.tools.opad.timeseries.ForecastMethod;
import kieker.tools.opad.timeseries.ITimeSeries;
import kieker.tools.opad.timeseries.forecast.AbstractRForecaster;

/**
 * An R-based time series forecaster which computes a forecast based on exponential smoothing.
 * 
 * @since 1.10
 * @author Andre van Hoorn
 * 
 */
public class ETSForecaster extends AbstractRForecaster {
	private static final String MODEL_FUNC_NAME = "ets"; // no explicit stochastic model
	private static final String FORECAST_FUNC_NAME = "forecast.ets";
	private final String[] emptyString = new String[0];

	/**
	 * 
	 * @param historyTimeseries
	 *            Time Series
	 */
	public ETSForecaster(final ITimeSeries<Double> historyTimeseries) {
		super(historyTimeseries, ETSForecaster.MODEL_FUNC_NAME, ETSForecaster.FORECAST_FUNC_NAME, ForecastMethod.ETS);
	}

	/**
	 * 
	 * @param historyTimeseries
	 *            Time Series
	 * @param confidenceLevel
	 *            value of confidence level (0-100)
	 */
	public ETSForecaster(final ITimeSeries<Double> historyTimeseries, final int confidenceLevel) {
		super(historyTimeseries, ETSForecaster.MODEL_FUNC_NAME, ETSForecaster.FORECAST_FUNC_NAME, confidenceLevel, ForecastMethod.ETS);
	}

	@Override
	protected String[] getModelFuncParams() {
		return this.emptyString.clone(); // no additional params required by this predictor
	}

	@Override
	protected String[] getForecastFuncParams() {
		return this.emptyString.clone(); // no additional params required by this predictor
	}
}
