/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.opad.timeseries.forecast.naive;

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
 *         The naive forecast considers only the
 *         value of the most recent observation assuming that this
 *         value has the highest probability for the next forecast point.
 * 
 *         Horizon: very short term forecast (1-2 points)
 *         Overhead: nearly none O(1)
 * 
 */
public class NaiveForecaster extends AbstractRForecaster {
	private static final String MODEL_FUNC_NAME = null; // no explicit stochastic model
	private static final String FORECAST_FUNC_NAME = "rwf";
	private final String[] emptyString = new String[0];

	/**
	 * 
	 * @param historyTimeseries
	 *            timeseries used by forecating algo
	 */
	public NaiveForecaster(final ITimeSeries<Double> historyTimeseries) {
		super(historyTimeseries, NaiveForecaster.MODEL_FUNC_NAME, NaiveForecaster.FORECAST_FUNC_NAME, ForecastMethod.NAIVE);
	}

	/**
	 * 
	 * @param historyTimeseries
	 *            timeseries used by forecating algo
	 * @param confidenceLevel
	 *            value of confidence
	 */
	public NaiveForecaster(final ITimeSeries<Double> historyTimeseries, final int confidenceLevel) {
		super(historyTimeseries, NaiveForecaster.MODEL_FUNC_NAME, NaiveForecaster.FORECAST_FUNC_NAME, confidenceLevel, ForecastMethod.NAIVE);
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
