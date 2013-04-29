/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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
import kieker.tools.opad.record.IForecastMeasurementPair;
import kieker.tools.opad.record.NamedDoubleTimeSeriesPoint;

/**
 * This filter calculates the anomaly score from the distance of the forecast and the current value.
 * 
 * 
 * @author Tillmann Carlos Bielefeld
 * 
 */
@Plugin(name = "AnomalyScore Calculation Filter",
		outputPorts = { @OutputPort(eventTypes = { NamedDoubleTimeSeriesPoint.class }, name = AnomalyScoreCalculationFilter.OUTPUT_PORT_ANOMALY_SCORE)
		})
public class AnomalyScoreCalculationFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_CURRENT_FORECAST_PAIR = "currentforecast";
	public static final String OUTPUT_PORT_ANOMALY_SCORE = "anomalyscore";

	public AnomalyScoreCalculationFilter(final Configuration configAnomaly, final IProjectContext projectContext) {
		super(configAnomaly, projectContext);
	}

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

		super.deliver(OUTPUT_PORT_ANOMALY_SCORE, new NamedDoubleTimeSeriesPoint(fmp.getTime(), score, fmp.getName()));
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}
