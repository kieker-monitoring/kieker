/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.opad.timeseries.forecast.cs;

import kieker.tools.opad.timeseries.ForecastMethod;
import kieker.tools.opad.timeseries.ITimeSeries;
import kieker.tools.opad.timeseries.forecast.AbstractRForecaster;

/**
 * This is one of the forecasters used in the research
 * paper on <i>Self-adaptive workload classification and forecasting for
 * proactive resource provisioning</i>
 * (http://dx.doi.org/10.1002/cpe.3224), authored by Herbst et al.
 * 
 * @since 1.10
 * @author Nikolas Herbst <nikolas.herbst@uni-wuerzburg.de>
 * 
 *         Cubic splines are fitted to the univariate time series data to obtain
 *         a trend estimate and linear forecast function.
 * 
 *         Prediction intervals are constructed by use of a likelihood approach for
 *         estimation of smoothing parameters. The cubic splines method can be mapped to
 *         an ARIMA 022 stochastic process model with a restricted parameter space.
 * 
 *         Overhead below 100ms for less than 30 values (more values do not sig. improve accuracy)
 * 
 */

public class CSForecaster extends AbstractRForecaster {
	private static final String MODEL_FUNC_NAME = null; // no explicit stochastic model
	private static final String FORECAST_FUNC_NAME = "splinef";
	private final String[] emptyString = new String[0];

	// private final static ForecastMethod strategy = ForecastMethod.CS;
	/**
	 * 
	 * @param historyTimeseries
	 *            timeseries used by forecating algo
	 */
	public CSForecaster(final ITimeSeries<Double> historyTimeseries) {
		super(historyTimeseries, CSForecaster.MODEL_FUNC_NAME, CSForecaster.FORECAST_FUNC_NAME, ForecastMethod.CS);
	}

	/**
	 * 
	 * @param historyTimeseries
	 *            timeseries used by forecating algo
	 * @param confidenceLevel
	 *            value of confidence
	 */
	public CSForecaster(final ITimeSeries<Double> historyTimeseries, final int confidenceLevel) {
		super(historyTimeseries, CSForecaster.MODEL_FUNC_NAME, CSForecaster.FORECAST_FUNC_NAME, confidenceLevel, ForecastMethod.CS);
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
