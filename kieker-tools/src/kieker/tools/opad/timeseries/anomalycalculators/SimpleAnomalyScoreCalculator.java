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

package kieker.tools.opad.timeseries.anomalycalculators;

import kieker.tools.opad.timeseries.ITimeSeriesPoint;
import kieker.tools.opad.timeseries.forecast.IForecastResult;

/**
 * @author Tillmann Carlos Bielefeld
 * 
 * @since 1.10
 */
public class SimpleAnomalyScoreCalculator implements IAnomalyScoreCalculator<Double> {

	public SimpleAnomalyScoreCalculator() {
		// No code necessary
	}

	@Override
	public AnomalyScore calculateAnomalyScore(final IForecastResult forecast, final ITimeSeriesPoint<Double> current) {
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
