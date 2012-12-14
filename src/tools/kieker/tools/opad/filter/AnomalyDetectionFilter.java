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

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.tools.opad.record.NamedDoubleTimeSeriesPoint;

/**
 * 
 * This filter separates input values by their reach of a certain threshold (parameter).
 * It takes events of type NamedDoubleTimeSeriesPoint and channels them into two output ports:
 * + anomalyscore_anomaly - yields a NamedDoubleTimeSeriesPoint if the threshold was reached
 * + anomalyscore_else - if the input value was less than the threshold
 * 
 * This filter has one configuration:
 * + threshold - The format is English with a . separator, e.g., 0.5 0.7, ...
 * 
 * @author Tillmann Carlos Bielefeld
 * 
 */
@Plugin(name = "AnomalyScore Detection Filter",
		outputPorts = {
			@OutputPort(eventTypes = { NamedDoubleTimeSeriesPoint.class }, name = AnomalyDetectionFilter.OUTPUT_PORT_ANOMALY_SCORE_IF_ANOMALY),
			@OutputPort(eventTypes = { NamedDoubleTimeSeriesPoint.class }, name = AnomalyDetectionFilter.OUTPUT_PORT_ANOMALY_SCORE_ELSE)
		})
public class AnomalyDetectionFilter extends AbstractFilterPlugin {

	public AnomalyDetectionFilter(final Configuration configuration) {
		super(configuration);
		final String sThreshold = super.configuration.getStringProperty(CONFIG_PROPERTY_THRESHOLD);
		this.threshold = Double.parseDouble(sThreshold);
	}

	public static final String OUTPUT_PORT_ANOMALY_SCORE_IF_ANOMALY = "anomalyscore_anomaly";
	public static final String OUTPUT_PORT_ANOMALY_SCORE_ELSE = "anomalyscore_else";
	public static final String INPUT_PORT_ANOMALY_SCORE = "anomalyscore";
	public static final String CONFIG_PROPERTY_THRESHOLD = "threshold";

	private final double threshold;
	public Double inputAnomalyScore;

	public Configuration getCurrentConfiguration() {
		final Configuration config = new Configuration();
		config.setProperty(CONFIG_PROPERTY_THRESHOLD, Double.toString(this.threshold));
		return new Configuration();
	}

	@InputPort(eventTypes = { NamedDoubleTimeSeriesPoint.class }, name = AnomalyDetectionFilter.INPUT_PORT_ANOMALY_SCORE)
	public void inputForecastAndMeasurement(final NamedDoubleTimeSeriesPoint anomalyScore) {

		// TODO check for null value?!
		if (anomalyScore.getDoubleValue() > this.threshold) {
			super.deliver(OUTPUT_PORT_ANOMALY_SCORE_ELSE, anomalyScore);
		} else {
			super.deliver(OUTPUT_PORT_ANOMALY_SCORE_IF_ANOMALY, anomalyScore);
		}
	}

}
