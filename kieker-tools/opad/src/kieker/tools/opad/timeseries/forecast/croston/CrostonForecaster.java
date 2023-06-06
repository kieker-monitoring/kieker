/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.opad.timeseries.forecast.croston;

import kieker.tools.opad.timeseries.ForecastMethod;
import kieker.tools.opad.timeseries.ITimeSeries;
import kieker.tools.opad.timeseries.forecast.AbstractRForecaster;

/**
 * Intermittent Demand Forecasting.
 *
 * This is one of the forecasters used in the research
 * paper on <i>Self-adaptive workload classification and forecasting for
 * proactive resource provisioning</i>
 * (http://dx.doi.org/10.1002/cpe.3224), authored by Herbst et al.
 *
 * @since 1.10
 * @author Nikolas Herbst
 *         Decomposition of the time series that contains zero values into
 *         two separate sequences: a non-zero valued time series and a second
 *         that contains the time intervals of zero values. Independent
 *         forecast using SES and combination of the two independent forecasts.
 *         No confidence intervals are computed due to no consistent underlying stochastic model.
 *
 */
public class CrostonForecaster extends AbstractRForecaster {
	private static final String MODEL_FUNC_NAME = null; // no explicit stochastic model
	private static final String FORECAST_FUNC_NAME = "croston";
	private final String[] emptyString = new String[0];

	/**
	 *
	 * @param historyTimeseries
	 *            timeseries used by forecating algo
	 */
	public CrostonForecaster(final ITimeSeries<Double> historyTimeseries) {
		super(historyTimeseries, CrostonForecaster.MODEL_FUNC_NAME, CrostonForecaster.FORECAST_FUNC_NAME, ForecastMethod.CROSTON);
	}

	/**
	 *
	 * @param historyTimeseries
	 *            timeseries used by forecating algo
	 * @param confidenceLevel
	 *            value of confidence
	 */
	public CrostonForecaster(final ITimeSeries<Double> historyTimeseries, final int confidenceLevel) {
		super(historyTimeseries, CrostonForecaster.MODEL_FUNC_NAME, CrostonForecaster.FORECAST_FUNC_NAME, confidenceLevel, ForecastMethod.CROSTON);
	}

	@Override
	protected String[] getModelFuncParams() {
		return this.emptyString.clone(); // no additional params required by this predictor
	}

	@Override
	protected String[] getForecastFuncParams() {
		return this.emptyString.clone(); // no additional params required by this predictor
	}

	@Override
	protected boolean supportsConfidence() {
		// Does not support confidence;
		return false;
	}
}
