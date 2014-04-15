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

package kieker.tools.tslib;

import kieker.tools.tslib.anomalycalculators.IAnomalyCalculator;
import kieker.tools.tslib.anomalycalculators.SimpleAnomalyCalculator;
import kieker.tools.tslib.forecast.IForecaster;
import kieker.tools.tslib.forecast.mean.MeanForecasterJava;
import kieker.tools.tslib.forecast.windowstart.WindowStartForecaster;

/**
 * 
 * @author Andre van Hoorn, Tillmann Carlos Bielefeld
 * @since 1.9
 * 
 */
public enum ForecastMethod {
	MEAN, SES, WINDOWSTART;

	public IForecaster<Double> getForecaster(final ITimeSeries<Double> history) {
		switch (this) {
		case MEAN:
			return new MeanForecasterJava(history);
		case WINDOWSTART:
			return new WindowStartForecaster(history);
		default:
			return new MeanForecasterJava(history);
		}
	}

	public IAnomalyCalculator<Double> getAnomalyCalculator() {
		return new SimpleAnomalyCalculator();
	}

}
