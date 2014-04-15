/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.tslib.forecast;

import kieker.tools.tslib.ITimeSeries;

/**
 * 
 * @author Andre van Hoorn
 * @since 1.9
 * @param <T>
 */
public interface IForecastResult<T> {

	/**
	 * Returns the point forecasts.
	 * 
	 * @since 1.9
	 */
	public ITimeSeries<T> getForecast();

	/**
	 * Returns the confidence level for the forecast interval.
	 * 
	 * @since 1.9
	 */
	public int getConfidenceLevel();

	/**
	 * Returns the upper limits for forecast interval with respect to the confidence level {@link #getConfidenceLevel()}.
	 * 
	 * @since 1.9
	 */
	public ITimeSeries<T> getUpper();

	/**
	 * Returns the lower limits for forecast interval with respect to the confidence level {@link #getConfidenceLevel()}.
	 * 
	 * @since 1.9
	 */
	public ITimeSeries<T> getLower();

	/**
	 * Returns the original time series that was the basis for the forecast.
	 * 
	 * @since 1.9
	 */
	public ITimeSeries<T> getOriginal();
}
