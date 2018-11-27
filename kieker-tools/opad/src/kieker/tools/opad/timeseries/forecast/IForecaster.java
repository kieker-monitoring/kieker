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

package kieker.tools.opad.timeseries.forecast;

import kieker.tools.opad.timeseries.ITimeSeries;

/**
 * @author Andre van Hoorn
 * @since 1.10
 * 
 * @param <T>
 *            The type of the forecaster.
 */
public interface IForecaster<T> {

	/**
	 * Performs a time series forecast for the given number of steps in the future.
	 * 
	 * @param numForecastSteps
	 *            number of steps which will be forecated
	 * @return ForecastResult
	 * 
	 * @since 1.10
	 */
	public IForecastResult forecast(final int numForecastSteps);

	/**
	 * Returns the original time series used for the forecast.
	 * 
	 * @return returns get ts original
	 *
	 * @since 1.10
	 */
	public ITimeSeries<T> getTsOriginal();

	/**
	 * Returns the confidence level to be computed for the forecast.
	 * 
	 * @return returns the get confidence level
	 *
	 * @since 1.10
	 */
	public int getConfidenceLevel();
}
