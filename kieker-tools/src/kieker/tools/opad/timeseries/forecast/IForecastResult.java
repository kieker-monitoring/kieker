/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.opad.timeseries.forecast;

import kieker.tools.opad.timeseries.ForecastMethod;
import kieker.tools.opad.timeseries.ITimeSeries;

/**
 * @author Andre van Hoorn
 * @since 1.10
 */
public interface IForecastResult {

	/**
	 * Returns the point forecasts.
	 * 
	 * @since 1.10
	 */
	public ITimeSeries<Double> getForecast();

	/**
	 * Returns the confidence level for the forecast interval.
	 * 
	 * @since 1.10
	 */
	public int getConfidenceLevel();

	/**
	 * Returns the upper limits for forecast interval with respect to the confidence level {@link #getConfidenceLevel()}.
	 * 
	 * @since 1.10
	 */
	public ITimeSeries<Double> getUpper();

	/**
	 * Returns the lower limits for forecast interval with respect to the confidence level {@link #getConfidenceLevel()}.
	 * 
	 * @since 1.10
	 */
	public ITimeSeries<Double> getLower();

	/**
	 * Returns the original time series that was the basis for the forecast.
	 * 
	 * @since 1.10
	 * @return orginal Timeseries
	 */
	public ITimeSeries<Double> getOriginal();

	/**
	 * Returns the MeanAbsoluteScaledError.
	 * 
	 * @since 1.10
	 * @return MASE
	 */
	public double getMeanAbsoluteScaledError();

	/**
	 * Returns the forecasting strategy that has been used for this forecast.
	 * 
	 * @since 1.10
	 * @return ForecastMethod
	 */
	public ForecastMethod getFcStrategy();

	/**
	 * Returns whether the result is plausible - mean forecast bigger than 0 and smaller than 1.5*maximum.
	 * 
	 * @since 1.10
	 * @return if result is plausible
	 */
	public boolean isPlausible();

}
