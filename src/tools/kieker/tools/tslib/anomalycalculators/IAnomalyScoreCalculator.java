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

package kieker.tools.tslib.anomalycalculators;

import kieker.tools.tslib.ITimeSeriesPoint;
import kieker.tools.tslib.forecast.IForecastResult;

/**
 * Classes implementing this interface calculate anomaly scores based on a forecasted and an actual value.
 * 
 * @author Tillmann Carlos Bielefeld
 * 
 * @since 1.10
 * 
 * @param <T>
 *            The type of the calculator.
 */
public interface IAnomalyScoreCalculator<T> {

	/**
	 * Calculates an anomaly score based on the given values.
	 * 
	 * @param forecast
	 *            The forecasted value.
	 * @param current
	 *            The actual value.
	 * 
	 * @return An anomaly score for the given values.
	 * 
	 * @since 1.10
	 */
	public AnomalyScore calculateAnomalyScore(IForecastResult forecast, ITimeSeriesPoint<T> current);

}
