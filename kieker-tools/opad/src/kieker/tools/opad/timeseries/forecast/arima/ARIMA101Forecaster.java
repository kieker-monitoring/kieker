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

package kieker.tools.opad.timeseries.forecast.arima;

import kieker.tools.opad.timeseries.ForecastMethod;
import kieker.tools.opad.timeseries.ITimeSeries;
import kieker.tools.opad.timeseries.forecast.AbstractRForecaster;

/**
 * An R-based time series forecaster which computes a forecast based on exponential smoothing.
 *
 * @since 1.10
 * @author Andre van Hoorn, Tillmann Carlos Bielefeld
 *
 */
public class ARIMA101Forecaster extends AbstractRForecaster {

	private static final String FORECAST_FUNC_NAME = "predict";
	private static final String MODEL_FUNC_NAME = "arima"; // no explicit stochastic model
	private final String[] emptyString = new String[0];

	/**
	 *
	 * @param historyTimeseries
	 *            timeseries used by forecating algo
	 */
	public ARIMA101Forecaster(final ITimeSeries<Double> historyTimeseries) {
		super(historyTimeseries, ARIMA101Forecaster.MODEL_FUNC_NAME, ARIMA101Forecaster.FORECAST_FUNC_NAME, ForecastMethod.ARIMA101);
	}

	/**
	 *
	 * @param historyTimeseries
	 *            timeseries used by forecating algo
	 * @param confidenceLevel
	 *            value of confidence
	 */
	public ARIMA101Forecaster(final ITimeSeries<Double> historyTimeseries, final int confidenceLevel) {
		super(historyTimeseries, ARIMA101Forecaster.MODEL_FUNC_NAME, ARIMA101Forecaster.FORECAST_FUNC_NAME, confidenceLevel, ForecastMethod.ARIMA101);
	}

	@Override
	protected String[] getModelFuncParams() {
		return new String[] { "c(1,0,1)", "method=\"ML\"" };
		// return new String[] { "c(1,0,1)", "method=\"CSS-ML\"" };
	}

	@Override
	protected String[] getForecastFuncParams() {
		return this.emptyString.clone(); // no additional params required by this predictor
	}

	@Override
	protected String forecastOperationOnResult(final String varNameForecast) {
		return String.format("%s$pred", varNameForecast);
	}

	/**
	 * @param varNameForecast
	 *            Forecastname
	 * @return lowOperation
	 */
	@Override
	protected String lowerOperationOnResult(final String varNameForecast) {
		return String.format("(%s$pred - %s$se)", varNameForecast, varNameForecast);
	}

	/**
	 * @param varNameForecast
	 *            Forecastname
	 * @return upperOperation
	 */
	@Override
	protected String upperOperationOnResult(final String varNameForecast) {
		return String.format("(%s$pred + %s$se)", varNameForecast, varNameForecast);
	}

}
