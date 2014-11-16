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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.BeanToCsv;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.IProjectContext;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.examples.analysis.opad.experimentModel.WikiGer24_Oct11_21d_InputModel;
import kieker.examples.analysis.opad.experimentModel.WikiGer24_Oct11_21d_OutputModel;
import kieker.tools.opad.filter.ForecastingFilter;
import kieker.tools.opad.filter.UniteMeasurementPairFilter;
import kieker.tools.opad.model.ForecastMeasurementPair;
import kieker.tools.tslib.ForecastMethod;

public final class ExperimentStarter {

	private static final Log LOG = LogFactory.getLog(ExperimentStarter.class);

	private ExperimentStarter() {}

	public static void main(final String[] args) throws IllegalStateException, AnalysisConfigurationException, InterruptedException {
		// for (final ForecastMethod fm : ForecastMethod.values()) {
		// ExperimentStarter.startWikipediaExperiment(ForecastMethod.ARIMA);
		// }

		// try {
		// ExperimentStarter.openCSVtest();
		// } catch (final IOException e) {
		// LOG.warn("An exception occurred", e);
		// }
		// ExperimentStarter.startExperiment();
		ExperimentStarter.readWriteCSVbean();
	}

	private static void startWikipediaExperiment(final ForecastMethod fcMethod) throws IllegalStateException, AnalysisConfigurationException {
		final Configuration controllerConfig = new Configuration();
		controllerConfig.setProperty(IProjectContext.CONFIG_PROPERTY_NAME_RECORDS_TIME_UNIT, "HOURS");
		final IAnalysisController analysisController = new AnalysisController(controllerConfig);

		final Configuration readerConfig = new Configuration();
		readerConfig.setProperty(SimpleTimeSeriesFileReader.CONFIG_PROPERTY_NAME_INPUT_FILE,
				"examples/analysis/opad-anomaly-detection/src/kieker/examples/analysis/opad/experiment-data/wikiGer24_Oct11_21d-2.csv");

		final SimpleTimeSeriesFileReader tsReader = new SimpleTimeSeriesFileReader(readerConfig, analysisController);

		final Configuration forecastConfig = new Configuration();
		forecastConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_FC_METHOD, fcMethod.name());
		forecastConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_DELTA_TIME, "1");
		forecastConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_DELTA_UNIT, "HOURS");
		final ForecastingFilter forecaster = new ForecastingFilter(forecastConfig, analysisController);

		final UniteMeasurementPairFilter uniteFilter = new UniteMeasurementPairFilter(new Configuration(), analysisController);

		// final TeeFilter tee = new TeeFilter(new Configuration(), analysisController);

		final ListCollectionFilter<ForecastMeasurementPair> listCollector = new ListCollectionFilter<ForecastMeasurementPair>(new Configuration(),
				analysisController);

		analysisController.connect(
				tsReader, SimpleTimeSeriesFileReader.OUTPUT_PORT_NAME_TS_POINTS,
				forecaster, ForecastingFilter.INPUT_PORT_NAME_TSPOINT);

		analysisController.connect(
				tsReader, SimpleTimeSeriesFileReader.OUTPUT_PORT_NAME_TS_POINTS,
				uniteFilter, UniteMeasurementPairFilter.INPUT_PORT_NAME_TSPOINT);

		analysisController.connect(
				forecaster, ForecastingFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT,
				uniteFilter, UniteMeasurementPairFilter.INPUT_PORT_NAME_FORECAST);

		analysisController.connect(
				uniteFilter, UniteMeasurementPairFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT,
				// tee, TeeFilter.INPUT_PORT_NAME_EVENTS);
				//
				// analysisController.connect(
				// tee, TeeFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS,
				listCollector, ListCollectionFilter.INPUT_PORT_NAME);

		// Start the analysis
		analysisController.run();

		try {
			Thread.sleep(3000);
		} catch (final InterruptedException e) {
			LOG.warn("An exception occurred", e);
		}
		analysisController.terminate();

		ExperimentStarter.analyzeData(listCollector.getList(), fcMethod.name());

	}

	private static void analyzeData(final List<ForecastMeasurementPair> resultList, final String methodName) {
		double diffSum = 0;
		double difference = 0;
		// StringBuilder sb;
		for (final ForecastMeasurementPair fmPair : resultList) {
			difference = Math.abs(fmPair.getForecasted() - fmPair.getValue());
			diffSum += difference;
			// sb = new StringBuilder();
			// sb.append("[");
			// sb.append(fmPair.getTime());
			// sb.append("]");
			// sb.append(" Forecasted: ");
			// sb.append(fmPair.getForecasted());
			// sb.append(", Measured: ");
			// sb.append(fmPair.getValue());
			// sb.append(", Difference: ");
			// sb.append(difference);
			// System.out.println(sb.toString());
		}
		System.out.println("[" + methodName + "] The average difference was: " + (diffSum / resultList.size()));
	}

	// private static void startExperiment() throws IllegalStateException, AnalysisConfigurationException {
	// final Configuration controllerConfig = new Configuration();
	// controllerConfig.setProperty(IProjectContext.CONFIG_PROPERTY_NAME_RECORDS_TIME_UNIT, "MINUTES");
	// final IAnalysisController analysisController = new AnalysisController(controllerConfig);
	//
	// final Configuration readerConfig = new Configuration();
	// readerConfig.setProperty(SimpleTimeSeriesFileReader.CONFIG_PROPERTY_NAME_INPUT_FILE,
	// "examples/analysis/opad-anomaly-detection/src/kieker/examples/analysis/opad/experiment-data/data.csv");
	// readerConfig.setProperty(SimpleTimeSeriesFileReader.CONFIG_PROPERTY_NAME_TS_INTERVAL, "15");
	// final SimpleTimeSeriesFileReader tsReader = new SimpleTimeSeriesFileReader(readerConfig, analysisController);
	//
	// final Configuration aggregatorConfig = new Configuration();
	// aggregatorConfig.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_METHOD, "MEANJAVA");
	// aggregatorConfig.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_SPAN, "60");
	// aggregatorConfig.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT, "MINUTES");
	// final TimeSeriesPointAggregatorFilter tsPointAggregator = new TimeSeriesPointAggregatorFilter(aggregatorConfig, analysisController);
	//
	// final Configuration forecastConfig = new Configuration();
	// forecastConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_FC_METHOD, "MEANJAVA");
	// forecastConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_DELTA_TIME, "1");
	// forecastConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_DELTA_UNIT, "HOURS");
	// final ForecastingFilter forecaster = new ForecastingFilter(forecastConfig, analysisController);
	//
	// final UniteMeasurementPairFilter uniteFilter = new UniteMeasurementPairFilter(new Configuration(), analysisController);
	//
	// final TeeFilter tee = new TeeFilter(new Configuration(), analysisController);
	//
	// analysisController.connect(
	// tsReader, SimpleTimeSeriesFileReader.OUTPUT_PORT_NAME_TS_POINTS,
	// tsPointAggregator, TimeSeriesPointAggregatorFilter.INPUT_PORT_NAME_TSPOINT);
	//
	// analysisController.connect(
	// tsPointAggregator, TimeSeriesPointAggregatorFilter.OUTPUT_PORT_NAME_AGGREGATED_TSPOINT,
	// forecaster, ForecastingFilter.INPUT_PORT_NAME_TSPOINT);
	//
	// analysisController.connect(
	// tsPointAggregator, TimeSeriesPointAggregatorFilter.OUTPUT_PORT_NAME_AGGREGATED_TSPOINT,
	// uniteFilter, UniteMeasurementPairFilter.INPUT_PORT_NAME_TSPOINT);
	//
	// analysisController.connect(
	// forecaster, ForecastingFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT,
	// uniteFilter, UniteMeasurementPairFilter.INPUT_PORT_NAME_FORECAST);
	//
	// analysisController.connect(
	// uniteFilter, UniteMeasurementPairFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT,
	// tee, TeeFilter.INPUT_PORT_NAME_EVENTS);
	//
	// // Start the analysis
	// analysisController.run();
	//
	// }

	private static void openCSVtest() throws IOException {
		final String fileName = "examples/analysis/opad-anomaly-detection/src/kieker/examples/analysis/opad/experiment-data/de_20110101-00_20131223-23_extracted-from-projectcounts-ez-transformed-1.txt";

		final FileInputStream fis = new FileInputStream(fileName);
		final InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		final BufferedReader br = new BufferedReader(isr);
		final char space = ' ';
		final CSVReader csvr = new CSVReader(br, space);

		final List<String[]> lines = csvr.readAll();
		for (final String[] line : lines) {
			System.out.println(line[3]);
		}
	}

	private static void readWriteCSVbean() {
		final String fileNameIn = "examples/analysis/opad-anomaly-detection/src/kieker/examples/analysis/opad/experiment-data/wikiGer24_Oct11_21d.csv";
		final String fileNameOut = "examples/analysis/opad-anomaly-detection/src/kieker/examples/analysis/opad/experiment-data/wikiGer24_Oct11_21d-out.csv";
		final List<WikiGer24_Oct11_21d_OutputModel> outputList = new ArrayList<WikiGer24_Oct11_21d_OutputModel>();
		CSVReader reader;
		try {
			reader = new CSVReader(new FileReader(fileNameIn));

			final ColumnPositionMappingStrategy<WikiGer24_Oct11_21d_InputModel> strategy = new ColumnPositionMappingStrategy<WikiGer24_Oct11_21d_InputModel>();
			// final HeaderColumnNameMappingStrategy<WikiGer24_Oct11_21d_InputModel> strategy = new
			// HeaderColumnNameMappingStrategy<WikiGer24_Oct11_21d_InputModel>();
			strategy.setType(WikiGer24_Oct11_21d_InputModel.class);
			final String[] columns = new String[] { "pageRequests" };
			strategy.setColumnMapping(columns);
			final CsvToBean<WikiGer24_Oct11_21d_InputModel> csv = new CsvToBean<WikiGer24_Oct11_21d_InputModel>();
			final List<WikiGer24_Oct11_21d_InputModel> objectList = csv.parse(strategy, reader);

			WikiGer24_Oct11_21d_OutputModel om;
			for (final WikiGer24_Oct11_21d_InputModel im : objectList) {
				System.out.println(im.getPageRequests());
				om = new WikiGer24_Oct11_21d_OutputModel();
				om.setPageRequests(im.getPageRequests());
				om.setForecast(im.getPageRequests() + 1);
				om.setConfidence(Double.NaN);
				outputList.add(om);
			}
			reader.close();
		} catch (final FileNotFoundException e) {
			LOG.warn("An exception occurred", e);
		} catch (final IOException e) {
			LOG.warn("An exception occurred", e);
		}

		CSVWriter writer;

		try {
			writer = new CSVWriter(new FileWriter(fileNameOut), ';');
			final ColumnPositionMappingStrategy<WikiGer24_Oct11_21d_OutputModel> strategy = new ColumnPositionMappingStrategy<WikiGer24_Oct11_21d_OutputModel>();
			strategy.setType(WikiGer24_Oct11_21d_OutputModel.class);
			final String[] columns = new String[] { "pageRequests", "forecast", "confidence" };
			strategy.setColumnMapping(columns);

			final BeanToCsv<WikiGer24_Oct11_21d_OutputModel> bean = new BeanToCsv<WikiGer24_Oct11_21d_OutputModel>();
			bean.write(strategy, writer, outputList);
			writer.close();
		} catch (final IOException e) {
			LOG.warn("An exception occurred", e);
		}
	}
}
