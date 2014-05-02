package kieker.tools.opad.filter;

import kieker.analysis.IProjectContext;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.tools.composite.AbstractCompositeFilterPlugin;
import kieker.tools.composite.CompositeInputRelay;
import kieker.tools.composite.CompositeOutputRelay;

/**
 * Filter plugin that contains the whole opad pipes/filter structure.
 * 
 * @author Thomas Düllmann, Tobias Rudolph
 * 
 */
@Plugin(description = "Cumulated filters that opad consists of",
		outputPorts = @OutputPort(name = OpadFilterPlugin.OUTPUT_PORT_NAME_ANOMALY_SCORE, eventTypes = { IMonitoringRecord.class }),
		configuration = {
			@Property(name = OpadFilterPlugin.PROPERTY_NAME_FC_METHOD, defaultValue = "MEAN", updateable = true),
			@Property(name = OpadFilterPlugin.PROPERTY_NAME_THRESHOLD, defaultValue = "0.5", updateable = true)
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

	/**
	 * Property that sets the forecasting method.
	 */
	public static final String PROPERTY_NAME_FC_METHOD = "fcmethod";

	/**
	 * Property that sets the threshold that indicates whether an anomaly score is treated as an anomaly.
	 */
	public static final String PROPERTY_NAME_THRESHOLD = "threshold";

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
			final ExtractionFilter extractionFilter = new ExtractionFilter(extractionConfiguration, controller);

			/**
			 * TimeSeriesPointAggregatorFilter
			 */
			final Configuration aggregationConfiguration = new Configuration();
			this.updateConfiguration(aggregationConfiguration, TimeSeriesPointAggregatorFilter.class);
			final TimeSeriesPointAggregatorFilter tsPointAggregatorFilter = new TimeSeriesPointAggregatorFilter(aggregationConfiguration, controller);

			/**
			 * ExtendedForecastingFilter
			 */

			final Configuration forecastConfiguration = new Configuration();
			this.updateConfiguration(forecastConfiguration, ExtendedForecastingFilter.class);
			final ExtendedForecastingFilter extForecastingFilter = new ExtendedForecastingFilter(forecastConfiguration, controller);
			configRegistry.registerUpdateableFilterPlugin("extForecastingFilter", extForecastingFilter);

			/**
			 * UniteMeasurementPairFilter
			 */
			final Configuration uniteConfiguration = new Configuration();
			this.updateConfiguration(uniteConfiguration, UniteMeasurementPairFilter.class);
			final UniteMeasurementPairFilter uniteFilter = new UniteMeasurementPairFilter(uniteConfiguration, controller);

			/**
			 * AnomalyScoreCalculatorFilter
			 */
			final Configuration scoreConfiguration = new Configuration();
			this.updateConfiguration(scoreConfiguration, AnomalyScoreCalculationFilter.class);
			final AnomalyScoreCalculationFilter scoreCalculationFilter = new AnomalyScoreCalculationFilter(scoreConfiguration, controller);

			/**
			 * AnomalyDetectionFilter
			 */
			final Configuration configAnomaly = new Configuration();
			this.updateConfiguration(configAnomaly, AnomalyDetectionFilter.class);
			final AnomalyDetectionFilter anomalyDetectionFilter = new AnomalyDetectionFilter(configAnomaly, controller);
			configRegistry.registerUpdateableFilterPlugin("anomalyDetectionFilter", anomalyDetectionFilter);

			/**
			 * SendAndStoreDetectionResultsFilter
			 */
			final Configuration configSaS = new Configuration();
			this.updateConfiguration(configSaS, SendAndStoreDetectionResultFilter.class);
			final SendAndStoreDetectionResultFilter sendAndStoreDetectionResultFilter = new SendAndStoreDetectionResultFilter(configSaS, controller);

			// StringBufferFilter -> ExtractionFilter
			controller.connect(inputRelay, CompositeInputRelay.INPUTRELAY_OUTPUTPORT, extractionFilter, ExtractionFilter.INPUT_PORT_NAME_VALUE);

			// ExtractionFilter -> TSPointAggregatorFilter
			controller.connect(extractionFilter, ExtractionFilter.OUTPUT_PORT_NAME_VALUE, tsPointAggregatorFilter,
					TimeSeriesPointAggregatorFilter.INPUT_PORT_NAME_TSPOINT); // NOCS

			// TSPointAggregatorFilter -> ExtendedForecastingFilter
			controller.connect(tsPointAggregatorFilter, TimeSeriesPointAggregatorFilter.OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, extForecastingFilter,
					ExtendedForecastingFilter.INPUT_PORT_NAME_TSPOINT); // NOCS

			// TSPointAggregatorfilter -> UniteMeasurementPairFilter
			controller.connect(tsPointAggregatorFilter, TimeSeriesPointAggregatorFilter.OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, uniteFilter,
					UniteMeasurementPairFilter.INPUT_PORT_NAME_TSPOINT); // NOCS

			// ExtendedForecastingFilter -> UniteMeasurementPairFilter
			controller.connect(extForecastingFilter, ExtendedForecastingFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, uniteFilter,
					UniteMeasurementPairFilter.INPUT_PORT_NAME_FORECAST); // NOCS

			// UniteMeasurementPairFilter -> AnomalyScoreCalculationFilter
			controller.connect(uniteFilter, UniteMeasurementPairFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, scoreCalculationFilter,
					AnomalyScoreCalculationFilter.INPUT_PORT_CURRENT_FORECAST_PAIR); // NOCS

			// AnomalyScoreCalculationFilter -> AnomalyScoreDetectionFilter
			controller.connect(scoreCalculationFilter, AnomalyScoreCalculationFilter.OUTPUT_PORT_ANOMALY_SCORE, anomalyDetectionFilter,
					AnomalyDetectionFilter.INPUT_PORT_ANOMALY_SCORE); // NOCS

			// AnomalyScoreDetectionFilter -> SendAndStoreDetectionResultFilter
			controller.connect(anomalyDetectionFilter, AnomalyDetectionFilter.OUTPUT_PORT_ALL,
					sendAndStoreDetectionResultFilter, SendAndStoreDetectionResultFilter.INPUT_PORT_DETECTION_RESULTS);

			// SendAndStoreDetectionResultFilter -> OpadOutputCompositionFilter
			controller.connect(sendAndStoreDetectionResultFilter, SendAndStoreDetectionResultFilter.OUTPUT_PORT_SENT_DATA,
					outputRelay, CompositeOutputRelay.INPUT_PORT_NAME_EVENTS);

		} catch (final IllegalStateException ise) {
			LOG.error("An error occurred: " + ise.getMessage());
		} catch (final AnalysisConfigurationException ace) {
			LOG.error("An error occurred: " + ace.getMessage());
		}
	}

	@Override
	public Configuration getCurrentConfiguration() {
		if (!configuration.containsKey(OpadFilterPlugin.PROPERTY_NAME_FC_METHOD)) {
			configuration.setProperty(OpadFilterPlugin.PROPERTY_NAME_FC_METHOD, "MEAN");
		}
		if (!configuration.containsKey(OpadFilterPlugin.PROPERTY_NAME_THRESHOLD)) {
			configuration.setProperty(OpadFilterPlugin.PROPERTY_NAME_THRESHOLD, "0.5");
		}
		return configuration;
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
