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

package kieker.examples.analysis.opad;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.tools.opad.filter.AnomalyDetectionFilter;
import kieker.tools.opad.filter.AnomalyScoreCalculationFilter;
import kieker.tools.opad.filter.ExtractionFilter;
import kieker.tools.opad.filter.ForecastingFilter;
import kieker.tools.opad.filter.TimeSeriesPointAggregatorFilter;
import kieker.tools.opad.filter.UniteMeasurementPairFilter;

/**
 * @author Thomas Duellmann
 *
 * @since 1.11
 */

public final class AnalysisStarter {

	private AnalysisStarter() {}

	public static void main(final String[] args) throws InterruptedException, IllegalStateException, AnalysisConfigurationException {
		final IAnalysisController analysisController = new AnalysisController();

		// Create the filters
		final Configuration fsReaderConfig = new Configuration();
		fsReaderConfig.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, "testdata");
		final FSReader fsReader = new FSReader(fsReaderConfig, analysisController);

		final ExtractionFilter extractionFilter = new ExtractionFilter(new Configuration(), analysisController);

		final Configuration tsPointAggregatorConfig = new Configuration();
		tsPointAggregatorConfig.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_SPAN, "1");
		tsPointAggregatorConfig.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT, "NANOSECONDS");
		tsPointAggregatorConfig.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_METHOD, "MEANJAVA");
		final TimeSeriesPointAggregatorFilter tsPointAggregatorFilter = new TimeSeriesPointAggregatorFilter(tsPointAggregatorConfig, analysisController);

		final Configuration forecastingConfig = new Configuration();
		forecastingConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_FC_METHOD, "MEANJAVA");
		final ForecastingFilter forecastingFilter = new ForecastingFilter(forecastingConfig, analysisController);

		final UniteMeasurementPairFilter uniteMeasurementPairFilter = new UniteMeasurementPairFilter(new Configuration(), analysisController);
		final AnomalyScoreCalculationFilter anomalyScoreCalcFilter = new AnomalyScoreCalculationFilter(new Configuration(), analysisController);
		final AnomalyDetectionFilter anomalyDetectionFilter = new AnomalyDetectionFilter(new Configuration(), analysisController);
		final AnomalyPrinter anomalyPrinter = new AnomalyPrinter(new Configuration(), analysisController);
		final AnomalyPrinter normalPrinter = new AnomalyPrinter(new Configuration(), analysisController);

		// Connect the filters
		analysisController.connect(fsReader, FSReader.OUTPUT_PORT_NAME_RECORDS, extractionFilter, ExtractionFilter.INPUT_PORT_NAME_VALUE);
		analysisController.connect(extractionFilter, ExtractionFilter.OUTPUT_PORT_NAME_VALUE, tsPointAggregatorFilter,
				TimeSeriesPointAggregatorFilter.INPUT_PORT_NAME_TSPOINT);
		analysisController.connect(tsPointAggregatorFilter, TimeSeriesPointAggregatorFilter.OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, forecastingFilter,
				ForecastingFilter.INPUT_PORT_NAME_TSPOINT);
		analysisController.connect(tsPointAggregatorFilter, TimeSeriesPointAggregatorFilter.OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, uniteMeasurementPairFilter,
				UniteMeasurementPairFilter.INPUT_PORT_NAME_TSPOINT);
		analysisController.connect(forecastingFilter, ForecastingFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT,
				uniteMeasurementPairFilter, UniteMeasurementPairFilter.INPUT_PORT_NAME_FORECAST);
		analysisController.connect(uniteMeasurementPairFilter, UniteMeasurementPairFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, anomalyScoreCalcFilter,
				AnomalyScoreCalculationFilter.INPUT_PORT_CURRENT_FORECAST_PAIR);
		analysisController.connect(anomalyScoreCalcFilter, AnomalyScoreCalculationFilter.OUTPUT_PORT_ANOMALY_SCORE,
				anomalyDetectionFilter, AnomalyDetectionFilter.INPUT_PORT_ANOMALY_SCORE);
		analysisController.connect(anomalyDetectionFilter, AnomalyDetectionFilter.OUTPUT_PORT_ANOMALY_SCORE_IF_ANOMALY,
				anomalyPrinter, AnomalyPrinter.INPUT_PORT_NAME_EVENTS);
		analysisController.connect(anomalyDetectionFilter, AnomalyDetectionFilter.OUTPUT_PORT_ANOMALY_SCORE_ELSE, normalPrinter,
				AnomalyPrinter.INPUT_PORT_NAME_EVENTS);

		// Start the analysis
		analysisController.run();
	}
}
