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
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.composite.AbstractCompositeFilterPlugin;
import kieker.analysis.plugin.filter.composite.CompositeInputRelay;
import kieker.analysis.plugin.filter.composite.CompositeOutputRelay;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.tools.opad.record.StorableDetectionResult;

/**
 * Filter plugin that contains the whole opad pipes/filter structure.
 *
 * @author Thomas Düllmann, Tobias Rudolph
 * @since 1.10
 *
 */
@Plugin(description = "Cumulated filters that opad consists of",
outputPorts = {
		@OutputPort(name = OpadFilterPlugin.OUTPUT_PORT_NAME_ANOMALY_SCORE, eventTypes = { StorableDetectionResult.class }) },
		configuration = {
		@Property(name = OpadFilterPlugin.PROPERTY_NAME_FC_METHOD, defaultValue = "MEANJAVA", updateable = true),
		@Property(name = OpadFilterPlugin.PROPERTY_NAME_THRESHOLD, defaultValue = "0.23", updateable = true),
		@Property(name = OpadFilterPlugin.PROPERTY_NAME_FC_DELTA_TIME, defaultValue = "10"),
		@Property(name = OpadFilterPlugin.PROPERTY_NAME_FC_DELTA_UNIT, defaultValue = "MILLISECONDS"),
		@Property(name = OpadFilterPlugin.PROPERTY_NAME_TSAGGREGATOR_AGGREGATION_SPAN, defaultValue = "2"),
		@Property(name = OpadFilterPlugin.PROPERTY_NAME_TSAGGREGATOR_AGGREGATION_TIMEUNIT, defaultValue = "NANOSECONDS"),
		@Property(name = OpadFilterPlugin.PROPERTY_NAME_EXTRACTOR_TIMEUNIT, defaultValue = "MILLISECONDS")
})
public class OpadFilterPlugin extends AbstractCompositeFilterPlugin {
	/**
	 * Output port that sends the resulting data including anomaly scores.
	 */
	public static final String OUTPUT_PORT_NAME_ANOMALY_SCORE = "AnomalyScore";

	/**
	 * Input port that receives the monitoring data.
	 */
	public static final String INPUT_PORT_NAME_VALUES = "Values";

	public static final String PROPERTY_NAME_EXTRACTOR_TIMEUNIT = "ExtractionFilter.timeUnit";

	public static final String PROPERTY_NAME_TSAGGREGATOR_AGGREGATION_SPAN = "TimeSeriesPointAggregatorFilter.aggregationSpan";
	public static final String PROPERTY_NAME_TSAGGREGATOR_AGGREGATION_TIMEUNIT = "TimeSeriesPointAggregatorFilter.timeUnit";

	/**
	 * Property that sets the forecasting method.
	 */
	public static final String PROPERTY_NAME_FC_METHOD = "ForecastingFilter.fcmethod";
	public static final String PROPERTY_NAME_FC_DELTA_TIME = "ForecastingFilter.deltatime";
	public static final String PROPERTY_NAME_FC_DELTA_UNIT = "ForecastingFilter.deltaunit";

	/**
	 * Property that sets the threshold that indicates whether an anomaly score is treated as an anomaly.
	 */
	public static final String PROPERTY_NAME_THRESHOLD = "AnomalyDetectionFilter.threshold";

	/**
	 * Constructor.
	 *
	 * @param configuration
	 *            configuration for this plugin
	 * @param projectContext
	 *            context of the project
	 */
	public OpadFilterPlugin(final Configuration configuration,
			final IProjectContext projectContext) {
		super(configuration, projectContext);

		try {
			/**
			 * Extractionfilter
			 */
			final Configuration extractionConfiguration = new Configuration();
			this.updateConfiguration(extractionConfiguration, ExtractionFilter.class);
			final ExtractionFilter extractionFilter = new ExtractionFilter(extractionConfiguration, this.controller);

			/**
			 * TimeSeriesPointAggregatorFilter
			 */
			final Configuration aggregationConfiguration = new Configuration();
			this.updateConfiguration(aggregationConfiguration, TimeSeriesPointAggregatorFilter.class);
			final TimeSeriesPointAggregatorFilter tsPointAggregatorFilter = new TimeSeriesPointAggregatorFilter(aggregationConfiguration, this.controller);

			/**
			 * ExtendedForecastingFilter
			 */

			final Configuration forecastConfiguration = new Configuration();
			this.updateConfiguration(forecastConfiguration, ForecastingFilter.class);
			final ForecastingFilter forecastingFilter = new ForecastingFilter(forecastConfiguration, this.controller);
			this.configRegistry.registerUpdateableFilterPlugin("extForecastingFilter", forecastingFilter);

			/**
			 * UniteMeasurementPairFilter
			 */
			final Configuration uniteConfiguration = new Configuration();
			this.updateConfiguration(uniteConfiguration, UniteMeasurementPairFilter.class);
			final UniteMeasurementPairFilter uniteFilter = new UniteMeasurementPairFilter(uniteConfiguration, this.controller);

			/**
			 * AnomalyScoreCalculatorFilter
			 */
			final Configuration scoreConfiguration = new Configuration();
			this.updateConfiguration(scoreConfiguration, AnomalyScoreCalculationFilter.class);
			final AnomalyScoreCalculationFilter scoreCalculationFilter = new AnomalyScoreCalculationFilter(scoreConfiguration, this.controller);

			/**
			 * AnomalyDetectionFilter
			 */
			final Configuration configAnomaly = new Configuration();
			this.updateConfiguration(configAnomaly, AnomalyDetectionFilter.class);
			final AnomalyDetectionFilter anomalyDetectionFilter = new AnomalyDetectionFilter(configAnomaly, this.controller);
			this.configRegistry.registerUpdateableFilterPlugin("anomalyDetectionFilter", anomalyDetectionFilter);

			// StringBufferFilter -> ExtractionFilter
			this.controller.connect(this.inputRelay, CompositeInputRelay.INPUTRELAY_OUTPUTPORT, extractionFilter, ExtractionFilter.INPUT_PORT_NAME_VALUE);

			// ExtractionFilter -> TSPointAggregatorFilter
			this.controller.connect(extractionFilter, ExtractionFilter.OUTPUT_PORT_NAME_VALUE, tsPointAggregatorFilter,
					TimeSeriesPointAggregatorFilter.INPUT_PORT_NAME_TSPOINT); // NOCS

			// TSPointAggregatorFilter -> ForecastingFilter
			this.controller.connect(tsPointAggregatorFilter, TimeSeriesPointAggregatorFilter.OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, forecastingFilter,
					ForecastingFilter.INPUT_PORT_NAME_TSPOINT); // NOCS

			// TSPointAggregatorfilter -> UniteMeasurementPairFilter
			this.controller.connect(tsPointAggregatorFilter, TimeSeriesPointAggregatorFilter.OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, uniteFilter,
					UniteMeasurementPairFilter.INPUT_PORT_NAME_TSPOINT); // NOCS

			// ExtendedForecastingFilter -> UniteMeasurementPairFilter
			this.controller.connect(forecastingFilter, ForecastingFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, uniteFilter,
					UniteMeasurementPairFilter.INPUT_PORT_NAME_FORECAST); // NOCS

			// UniteMeasurementPairFilter -> AnomalyScoreCalculationFilter
			this.controller.connect(uniteFilter, UniteMeasurementPairFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, scoreCalculationFilter,
					AnomalyScoreCalculationFilter.INPUT_PORT_CURRENT_FORECAST_PAIR); // NOCS

			// AnomalyScoreCalculationFilter -> AnomalyScoreDetectionFilter
			this.controller.connect(scoreCalculationFilter, AnomalyScoreCalculationFilter.OUTPUT_PORT_ANOMALY_SCORE, anomalyDetectionFilter,
					AnomalyDetectionFilter.INPUT_PORT_ANOMALY_SCORE); // NOCS

			// AnomalyScoreDetectionFilter -> OutputRelay
			this.controller.connect(anomalyDetectionFilter, AnomalyDetectionFilter.OUTPUT_PORT_ALL,
					this.outputRelay, CompositeOutputRelay.INPUT_PORT_NAME_EVENTS);

		} catch (final IllegalStateException ise) {
			LOG.error("An error occurred: " + ise.getMessage());
		} catch (final AnalysisConfigurationException ace) {
			LOG.error("An error occurred: " + ace.getMessage());
		}
	}

	@Override
	public Configuration getCurrentConfiguration() {
		if (!this.configuration.containsKey(OpadFilterPlugin.PROPERTY_NAME_FC_METHOD)) {
			this.configuration.setProperty(OpadFilterPlugin.PROPERTY_NAME_FC_METHOD, "MEANJAVA");
		}
		if (!this.configuration.containsKey(OpadFilterPlugin.PROPERTY_NAME_THRESHOLD)) {
			this.configuration.setProperty(OpadFilterPlugin.PROPERTY_NAME_THRESHOLD, "0.5");
		}

		final Configuration config = new Configuration();
		config.setProperty(OpadFilterPlugin.PROPERTY_NAME_FC_METHOD, this.configuration.getStringProperty(OpadFilterPlugin.PROPERTY_NAME_FC_METHOD));
		config.setProperty(OpadFilterPlugin.PROPERTY_NAME_THRESHOLD, this.configuration.getStringProperty(OpadFilterPlugin.PROPERTY_NAME_THRESHOLD));
		return config;
	}

	/**
	 * Relays the incoming monitoring record to the contained filter input ports.
	 *
	 * @param monitoringRecord
	 *            the incoming monitoring record
	 */
	@InputPort(name = OpadFilterPlugin.INPUT_PORT_NAME_VALUES, eventTypes = { IMonitoringRecord.class })
	public void startAnalysis(final IMonitoringRecord monitoringRecord) {
		this.inputRelay.relayMessage(monitoringRecord);
	}
}
