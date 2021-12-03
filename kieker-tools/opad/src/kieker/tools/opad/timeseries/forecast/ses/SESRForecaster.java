/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.opad.timeseries.forecast.ses;

import kieker.tools.opad.timeseries.ForecastMethod;
import kieker.tools.opad.timeseries.ITimeSeries;
import kieker.tools.opad.timeseries.forecast.AbstractRForecaster;

/**
 * This is one of the forecasters used in the research
 * paper on <i>Self-adaptive workload classification and forecasting for
 * proactive resource provisioning</i>
 * (http://dx.doi.org/10.1002/cpe.3224), authored by Herbst et al.
 * 
 * @author Nikolas Herbst <nikolas.herbst@uni-wuerzburg.de>
 *         Generalization of MA by using weights according to the exponential function
 *         to give higher weight to more recent values.
 *         1st step: estimation of parameters for weights/exp. function
 *         2nd step: calculation of weighted averages as point forecast
 * 
 * @since 1.10
 */
public class SESRForecaster extends AbstractRForecaster {
	private static final String MODEL_FUNC_NAME = "ets"; // no explicit stochastic model
	private static final String FORECAST_FUNC_NAME = "forecast";
	private final String[] emptyString = new String[0];

	/**
	 * 
	 * @param historyTimeseries
	 *            timeseries used by forecating algo
	 */
	public SESRForecaster(final ITimeSeries<Double> historyTimeseries) {
		super(historyTimeseries, SESRForecaster.MODEL_FUNC_NAME, SESRForecaster.FORECAST_FUNC_NAME, ForecastMethod.SES);
	}

	/**
	 * 
	 * @param historyTimeseries
	 *            timeseries used by forecating algo
	 * 
	 * @param confidenceLevel
	 *            confidenceLevel
	 */
	public SESRForecaster(final ITimeSeries<Double> historyTimeseries, final int confidenceLevel) {
		super(historyTimeseries, SESRForecaster.MODEL_FUNC_NAME, SESRForecaster.FORECAST_FUNC_NAME, confidenceLevel, ForecastMethod.SES);
	}

	@Override
	protected String[] getModelFuncParams() {
		return new String[] { "model=\"ANN\"" };
	}

	@Override
	/**
	 * From R Forecast documentation:
	 * Usually a three-character string identifying method
	 * using the framework terminology of Hyndman et al. (2002) and Hyndman et al. (2008).
	 * The first letter denotes the error type ("A", "M" or "Z");
	 * the second letter denotes the trend type ("N","A","M" or "Z");
	 * and the third letter denotes the season type ("N","A","M" or "Z").
	 * In all cases, "N"=none, "A"=additive, "M"=multiplicative and "Z"=automatically selected.
	 * So, for example, "ANN" is simple exponential smoothing with additive errors,
	 * "MAM" is multiplicative Holt-Winters' method with multiplicative errors, and so on.
	 * It is also possible for the model to be equal to the output from a previous call to ets.
	 * In this case, the same model is fitted to y without re-estimating any parameters.
	 * 
	 * no additional params required by this predictor
	 * 
	 * @return emptyString array
	 */
	protected String[] getForecastFuncParams() {
		return this.emptyString.clone(); // no additional params required by this predictor
	}
}
