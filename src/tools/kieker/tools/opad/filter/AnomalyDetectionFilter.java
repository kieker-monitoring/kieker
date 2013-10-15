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
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.tools.opad.record.StorableDetectionResult;

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
			@OutputPort(eventTypes = { StorableDetectionResult.class }, name = AnomalyDetectionFilter.OUTPUT_PORT_ANOMALY_SCORE_IF_ANOMALY),
			@OutputPort(eventTypes = { StorableDetectionResult.class }, name = AnomalyDetectionFilter.OUTPUT_PORT_ANOMALY_SCORE_ELSE) },
		configuration = {
			@Property(name = AnomalyDetectionFilter.CONFIG_PROPERTY_THRESHOLD, defaultValue = "0.5"),
			@Property(name = AnomalyDetectionFilter.CONFIG_PROPERTY_THRESHOLD_CRITICAL, defaultValue = "0.95")
		})
public class AnomalyDetectionFilter extends AbstractFilterPlugin {

	/**
	 * Name of the input port receiving the anomalyscore.
	 */
	public static final String INPUT_PORT_ANOMALY_SCORE = "anomalyscore";

	/**
	 * Name of the output port delivering the anomalyscore if it exceeds the threshhold.
	 */
	public static final String OUTPUT_PORT_ANOMALY_SCORE_IF_ANOMALY = "anomalyscore_anomaly";

	/**
	 * Name of the output port delivering the anomalyscore if it remains below the threshhold.
	 */
	public static final String OUTPUT_PORT_ANOMALY_SCORE_ELSE = "anomalyscore_else";

	/** Name of the property determining the threshold. */
	public static final String CONFIG_PROPERTY_THRESHOLD = "threshold";
	/** Name of the property determining a critical threshold. */
	public static final String CONFIG_PROPERTY_THRESHOLD_CRITICAL = "thresholdcritical";

	private final double threshold;
	private final double thresholdCritical;

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param configuration
	 *            Configuration of this component
	 * @param projectContext
	 *            ProjectContext of this component
	 */
	public AnomalyDetectionFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
		final String sThreshold = super.configuration.getStringProperty(CONFIG_PROPERTY_THRESHOLD);
		this.threshold = Double.parseDouble(sThreshold);
		final String sThresholdCritical = super.configuration.getStringProperty(CONFIG_PROPERTY_THRESHOLD_CRITICAL);
		this.thresholdCritical = Double.parseDouble(sThresholdCritical);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration config = new Configuration();
		config.setProperty(CONFIG_PROPERTY_THRESHOLD, Double.toString(this.threshold));
		config.setProperty(CONFIG_PROPERTY_THRESHOLD_CRITICAL, Double.toString(this.thresholdCritical));
		return new Configuration();
	}

	/**
	 * This method represents the input port for the incoming anomalyscore.
	 * 
	 * @param anomalyScore
	 *            Incoming anomaly score
	 */
	@InputPort(eventTypes = { StorableDetectionResult.class }, name = AnomalyDetectionFilter.INPUT_PORT_ANOMALY_SCORE)
	public void inputForecastAndMeasurement(final StorableDetectionResult anomalyScore) {
		if (anomalyScore.getScore() >= this.threshold) {
			if (anomalyScore.getScore() >= this.thresholdCritical) {
				super.deliver(OUTPUT_PORT_ANOMALY_SCORE_IF_ANOMALY, anomalyScore);
			} else {
				super.deliver(OUTPUT_PORT_ANOMALY_SCORE_IF_ANOMALY, anomalyScore);
			}
		} else {
			super.deliver(OUTPUT_PORT_ANOMALY_SCORE_ELSE, anomalyScore);
		}
	}

}
