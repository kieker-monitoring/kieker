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
 * 
 * @author Tillmann Carlos Bielefeld
 * @since 1.9
 */
public class SimpleAnomalyCalculator implements IAnomalyCalculator<Double> {

	/**
	 * Creates a new Instance of this class.
	 */
	public SimpleAnomalyCalculator() {
		// No code necessary
	}

	@Override
	public AnomalyScore calculateAnomaly(final IForecastResult<Double> forecast, final ITimeSeriesPoint<Double> current) {
		if (forecast.getForecast().getPoints().size() == 0) {
			return null;
		}

		final Double nextpredicted = forecast.getForecast().getPoints().get(0).getValue();
		if (null == nextpredicted) {
			return null;
		}

		double measuredValue = 0.0;

		measuredValue = current.getValue();

		double difference = nextpredicted - measuredValue;
		final double sum = nextpredicted + measuredValue;

		difference = Math.abs(difference / sum);

		return new AnomalyScore(difference);
	}
}
