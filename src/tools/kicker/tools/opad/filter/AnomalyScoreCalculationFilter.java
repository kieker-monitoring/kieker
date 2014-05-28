/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.tools.opad.filter;

import kicker.analysis.IProjectContext;
import kicker.analysis.plugin.annotation.InputPort;
import kicker.analysis.plugin.annotation.OutputPort;
import kicker.analysis.plugin.annotation.Plugin;
import kicker.analysis.plugin.filter.AbstractFilterPlugin;
import kicker.common.configuration.Configuration;
import kicker.tools.opad.record.IForecastMeasurementPair;
import kicker.tools.opad.record.StorableDetectionResult;

/**
 * This filter calculates the anomaly score based on the distance between the forecasted and the actual value.
 * 
 * @author Tillmann Carlos Bielefeld
 * 
 * @since 1.9
 */
@Plugin(name = "AnomalyScore Calculation Filter",
		outputPorts = { @OutputPort(eventTypes = { StorableDetectionResult.class }, name = AnomalyScoreCalculationFilter.OUTPUT_PORT_ANOMALY_SCORE) })
public class AnomalyScoreCalculationFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_CURRENT_FORECAST_PAIR = "currentforecast";
	public static final String OUTPUT_PORT_ANOMALY_SCORE = "anomalyscore";

	public AnomalyScoreCalculationFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	@InputPort(eventTypes = { IForecastMeasurementPair.class }, name = AnomalyScoreCalculationFilter.INPUT_PORT_CURRENT_FORECAST_PAIR)
	public void inputForecastAndMeasurement(final IForecastMeasurementPair fmp) {
		if (null != fmp.getForecasted()) {
			final double forecastedValue = fmp.getForecasted();
			final double actualValue = fmp.getValue();

			final double difference = forecastedValue - actualValue;
			final double sum = forecastedValue + actualValue;
			final double anomalyScore = Math.abs(difference / sum);

			final StorableDetectionResult result = new StorableDetectionResult(fmp.getName(), fmp.getValue(), fmp.getTime(), fmp.getForecasted(), anomalyScore);
			super.deliver(OUTPUT_PORT_ANOMALY_SCORE, result);
		}
	}

}
