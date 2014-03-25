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

package kieker.tools.opad.filter;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.opad.IForecastMeasurementPair;
import kieker.common.record.opad.StorableDetectionResult;

/**
 * This filter calculates the anomaly score from the distance of the forecast and the current value.
 * 
 * 
 * @author Tillmann Carlos Bielefeld
 * @since 1.9
 * 
 */
@Plugin(name = "AnomalyScore Calculation Filter",
		outputPorts = { @OutputPort(eventTypes = { StorableDetectionResult.class }, name = AnomalyScoreCalculationFilter.OUTPUT_PORT_ANOMALY_SCORE)
		})
public class AnomalyScoreCalculationFilter extends AbstractFilterPlugin {

	/**
	 * Name of the input port receiving the pair consisting of measurement and forecast.
	 */
	public static final String INPUT_PORT_CURRENT_FORECAST_PAIR = "currentforecast";

	/**
	 * Name of the output port delivering the anomaly score.
	 */
	public static final String OUTPUT_PORT_ANOMALY_SCORE = "anomalyscore";

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param configAnomaly
	 *            Configuration of this filter
	 * @param projectContext
	 *            ProjectContext of this filter
	 */
	public AnomalyScoreCalculationFilter(final Configuration configAnomaly, final IProjectContext projectContext) {
		super(configAnomaly, projectContext);
	}

	/**
	 * Representing the input port for pairs of measurements and forecasts.
	 * 
	 * @param fmp
	 *            Pair consisting of measurement and forecast
	 */
	@InputPort(eventTypes = { IForecastMeasurementPair.class }, name = AnomalyScoreCalculationFilter.INPUT_PORT_CURRENT_FORECAST_PAIR)
	public void inputForecastAndMeasurement(final IForecastMeasurementPair fmp) {
		Double score = null;

		if (null != fmp.getForecasted()) {
			final double nextpredicted = fmp.getForecasted();

			final double measuredValue = fmp.getValue();

			final double difference = nextpredicted - measuredValue;
			final double sum = nextpredicted + measuredValue;

			score = Math.abs(difference / sum);
		}

		final StorableDetectionResult dr = new StorableDetectionResult(fmp.getName(), fmp.getValue(), fmp.getTime(), fmp.getForecasted(), score);
		super.deliver(OUTPUT_PORT_ANOMALY_SCORE, dr);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}
