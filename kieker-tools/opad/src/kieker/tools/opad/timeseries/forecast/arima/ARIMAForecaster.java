/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
 * An R-based time series forecaster, auto-arima model selection.
 * 
 * This is one of the forecasters used in the research
 * paper on <i>Self-adaptive workload classification and forecasting for
 * proactive resource provisioning</i>
 * (http://dx.doi.org/10.1002/cpe.3224), authored by Herbst et al.
 * 
 * @since 1.10
 * @author Andre van Hoorn, Nikolas Herbst <nikolas.herbst@uni-wuerzburg.de>
 * 
 *         The automated ARIMA model selection process of the R forecasting package starts
 *         with a complex estimation of an appropriate ARIMA(p, d, q)(P, D, Q)m model by using
 *         unit-root tests and an information criterions (like the AIC) in combination with
 *         a step-wise procedure for traversing a relevant model space.
 *         The selected ARIMA model is then fitted to the data to provide point forecasts
 *         and confidence intervals.
 */
public class ARIMAForecaster extends AbstractRForecaster {
	private static final String MODEL_FUNC_NAME = "auto.arima"; // no explicit stochastic model
	private static final String FORECAST_FUNC_NAME = "forecast";
	private final String[] emptyString = new String[0];

	/**
	 * 
	 * @param historyTimeseries
	 *            timeseries used by forecating algo
	 */
	public ARIMAForecaster(final ITimeSeries<Double> historyTimeseries) {
		super(historyTimeseries, ARIMAForecaster.MODEL_FUNC_NAME, ARIMAForecaster.FORECAST_FUNC_NAME, ForecastMethod.ARIMA);
	}

	/**
	 * 
	 * @param historyTimeseries
	 *            timeseries used by forecating algo
	 * @param confidenceLevel
	 *            value of confidence
	 */
	public ARIMAForecaster(final ITimeSeries<Double> historyTimeseries, final int confidenceLevel) {
		super(historyTimeseries, ARIMAForecaster.MODEL_FUNC_NAME, ARIMAForecaster.FORECAST_FUNC_NAME, confidenceLevel, ForecastMethod.ARIMA);
	}

	@Override
	protected String[] getModelFuncParams() {
		return new String[] { "parallel=\"true\"" };
	}

	@Override
	protected String[] getForecastFuncParams() {
		return this.emptyString.clone(); // no additional params required by this predictor
	}
}
