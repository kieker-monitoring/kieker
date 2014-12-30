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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.examples.analysis.opad.experimentModel.WikiGer24_Oct11_21d_InputModel;
import kieker.examples.analysis.opad.experimentModel.WikiGer24_Oct11_21d_OutputModel;
import kieker.tools.opad.filter.ForecastingFilter;
import kieker.tools.opad.model.ForecastMeasurementPair;
import kieker.tools.opad.model.NamedDoubleTimeSeriesPoint;
import kieker.tools.tslib.ForecastMethod;

public final class ExperimentStarter {

	private static final Log LOG = LogFactory.getLog(ExperimentStarter.class);

	private ExperimentStarter() {}

	public static void main(final String[] args) throws IllegalStateException, AnalysisConfigurationException, InterruptedException {
		// READ CSV
		final String fileName = "examples/analysis/opad-anomaly-detection/src/kieker/examples/analysis/opad/experiment-data/wikiGer24_Oct11_21d";

		final List<ForecastMethod> methods = new ArrayList<ForecastMethod>();
		methods.add(ForecastMethod.ARIMA101);
		// methods.add(ForecastMethod.CROSTON);
		// methods.add(ForecastMethod.CS);
		methods.add(ForecastMethod.ETS);
		methods.add(ForecastMethod.SES);
		methods.add(ForecastMethod.ARIMA);
		methods.add(ForecastMethod.MEAN);
		methods.add(ForecastMethod.NAIVE);

		ExperimentStarter.LOG.info("Starting to read data from " + fileName + ".csv");
		final List<NamedDoubleTimeSeriesPoint> readData = ExperimentStarter.readFromCsv(fileName);
		ExperimentStarter.LOG.info("Done reading data from " + fileName + ".csv");

		for (final ForecastMethod fcMethod : methods) {
			ExperimentStarter.LOG.info("Starting experiment for " + fcMethod.name());
			final List<ForecastMeasurementPair> measurements = ExperimentStarter.forecastWithR(readData, fcMethod);
			ExperimentStarter.writeToCsv(measurements, fileName, fcMethod);
			ExperimentStarter.LOG.info("Done with experiment for " + fcMethod.name());
		}
		ExperimentStarter.LOG.info("All experiments completed.");

	}

	private static List<NamedDoubleTimeSeriesPoint> readFromCsv(final String fileName) {
		CSVReader reader;
		final List<NamedDoubleTimeSeriesPoint> outputList = new ArrayList<NamedDoubleTimeSeriesPoint>();
		try {
			final InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName + ".csv"), "UTF-8");
			reader = new CSVReader(isr);

			final ColumnPositionMappingStrategy<WikiGer24_Oct11_21d_InputModel> strategy = new ColumnPositionMappingStrategy<WikiGer24_Oct11_21d_InputModel>();
			strategy.setType(WikiGer24_Oct11_21d_InputModel.class);
			final String[] columns = new String[] { "pageRequests" };
			strategy.setColumnMapping(columns);
			final CsvToBean<WikiGer24_Oct11_21d_InputModel> csv = new CsvToBean<WikiGer24_Oct11_21d_InputModel>();
			final List<WikiGer24_Oct11_21d_InputModel> objectList = csv.parse(strategy, reader);

			long timestamp = 0;
			for (final WikiGer24_Oct11_21d_InputModel input : objectList) {
				outputList.add(input.getNDTSP(timestamp, "experiment"));
				timestamp++;
			}
			reader.close();

		} catch (final FileNotFoundException e) {
			LOG.warn("An exception occurred", e);
		} catch (final IOException e) {
			LOG.warn("An exception occurred", e);
		}

		return outputList;
	}

	private static List<ForecastMeasurementPair> forecastWithR(final List<NamedDoubleTimeSeriesPoint> data, final ForecastMethod fcMethod)
			throws IllegalStateException, AnalysisConfigurationException {
		final Configuration controllerConfig = new Configuration();
		controllerConfig.setProperty(IProjectContext.CONFIG_PROPERTY_NAME_RECORDS_TIME_UNIT, "HOURS");
		final IAnalysisController analysisController = new AnalysisController(controllerConfig);

		final ListReader<NamedDoubleTimeSeriesPoint> listReader = new ListReader<NamedDoubleTimeSeriesPoint>(new Configuration(), analysisController);

		// final UniteMeasurementPairFilter uniteFilter = new UniteMeasurementPairFilter(new Configuration(), analysisController);

		final Configuration forecastConfig = new Configuration();
		forecastConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_FC_METHOD, fcMethod.name());
		forecastConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_DELTA_TIME, "1");
		forecastConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_DELTA_UNIT, "HOURS");
		forecastConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_FC_CONFIDENCE, "95");
		final ForecastingFilter forecaster = new ForecastingFilter(forecastConfig, analysisController);

		final ListCollectionFilter<ForecastMeasurementPair> listCollector = new ListCollectionFilter<ForecastMeasurementPair>(new Configuration(),
				analysisController);

		// analysisController.connect(
		// listReader, ListReader.OUTPUT_PORT_NAME,
		// uniteFilter, UniteMeasurementPairFilter.INPUT_PORT_NAME_TSPOINT);

		analysisController.connect(
				listReader, ListReader.OUTPUT_PORT_NAME,
				forecaster, ForecastingFilter.INPUT_PORT_NAME_TSPOINT);

		// analysisController.connect(
		// forecaster, ForecastingFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT,
		// uniteFilter, UniteMeasurementPairFilter.INPUT_PORT_NAME_FORECAST);

		// analysisController.connect(uniteFilter, UniteMeasurementPairFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT,
		// listCollector, ListCollectionFilter.INPUT_PORT_NAME);

		analysisController.connect(
				forecaster, ForecastingFilter.OUTPUT_PORT_NAME_FORECASTED_AND_MEASURED,
				listCollector, ListCollectionFilter.INPUT_PORT_NAME);

		listReader.addAllObjects(data);

		// Start the analysis
		analysisController.run();

		try {
			Thread.sleep(3000);
		} catch (final InterruptedException e) {
			LOG.warn("An exception occurred", e);
		}
		analysisController.terminate();

		return listCollector.getList();
	}

	private static void writeToCsv(final List<ForecastMeasurementPair> measurements, final String fileNameBase, final ForecastMethod forecaster) {
		CSVWriter writer;
		final List<WikiGer24_Oct11_21d_OutputModel> outputList = new ArrayList<WikiGer24_Oct11_21d_OutputModel>();

		WikiGer24_Oct11_21d_OutputModel outputItem;
		for (final ForecastMeasurementPair fmp : measurements) {
			outputItem = new WikiGer24_Oct11_21d_OutputModel(fmp.getValue(), fmp.getForecasted(), fmp.getConfidenceLevel(), fmp.getConfidenceUpper(),
					fmp.getConfidenceLower(), forecaster.name(), fmp.getMASE());
			outputList.add(outputItem);
		}

		try {
			final OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(fileNameBase + "-out-" + forecaster.name() + ".csv"), "UTF-8");
			writer = new CSVWriter(osw, ';');
			final ColumnPositionMappingStrategy<WikiGer24_Oct11_21d_OutputModel> strategy = new ColumnPositionMappingStrategy<WikiGer24_Oct11_21d_OutputModel>();
			strategy.setType(WikiGer24_Oct11_21d_OutputModel.class);
			final String[] columns = new String[] { "pageRequests", "forecast", "forecaster", "confidence", "confidenceUpper", "confidenceLower", "mase" };
			strategy.setColumnMapping(columns);

			final BeanToCsv<WikiGer24_Oct11_21d_OutputModel> bean = new BeanToCsv<WikiGer24_Oct11_21d_OutputModel>();
			bean.write(strategy, writer, outputList);
			writer.close();
		} catch (final IOException e) {
			LOG.warn("An exception occurred", e);
		}
	}

	// private static void csvOpadCsv(final ForecastMethod fcMethod) throws IllegalStateException, AnalysisConfigurationException {
	//
	// final Configuration controllerConfig = new Configuration();
	// controllerConfig.setProperty(IProjectContext.CONFIG_PROPERTY_NAME_RECORDS_TIME_UNIT, "HOURS");
	// final IAnalysisController analysisController = new AnalysisController(controllerConfig);
	//
	// final ListReader<NamedDoubleTimeSeriesPoint> listReader = new ListReader<NamedDoubleTimeSeriesPoint>(new Configuration(), analysisController);
	//
	// final Configuration forecastConfig = new Configuration();
	// forecastConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_FC_METHOD, fcMethod.name());
	// forecastConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_DELTA_TIME, "1");
	// forecastConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_DELTA_UNIT, "HOURS");
	// forecastConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_FC_CONFIDENCE, "95");
	// final ForecastingFilter forecaster = new ForecastingFilter(forecastConfig, analysisController);
	//
	// final ListCollectionFilter<ForecastMeasurementPair> listCollector = new ListCollectionFilter<ForecastMeasurementPair>(new Configuration(),
	// analysisController);
	//
	// analysisController.connect(
	// listReader, ListReader.OUTPUT_PORT_NAME,
	// forecaster, ForecastingFilter.INPUT_PORT_NAME_TSPOINT);
	//
	// analysisController.connect(
	// forecaster, ForecastingFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT,
	// listCollector, ListCollectionFilter.INPUT_PORT_NAME);
	//
	// // READ CSV
	// final String fileNameIn = "examples/analysis/opad-anomaly-detection/src/kieker/examples/analysis/opad/experiment-data/wikiGer24_Oct11_21d.csv";
	//
	// // Start the analysis
	// analysisController.run();
	//
	// try {
	// Thread.sleep(3000);
	// } catch (final InterruptedException e) {
	// LOG.warn("An exception occurred", e);
	// }
	// analysisController.terminate();
	//
	// // WRITE CSV
	// CSVWriter writer;
	// final String fileNameOut = "examples/analysis/opad-anomaly-detection/src/kieker/examples/analysis/opad/experiment-data/wikiGer24_Oct11_21d-out-"
	// + fcMethod.name() + ".csv";
	// final List<WikiGer24_Oct11_21d_OutputModel> outputList = new ArrayList<WikiGer24_Oct11_21d_OutputModel>();
	//
	// WikiGer24_Oct11_21d_OutputModel outputItem;
	// for (final ForecastMeasurementPair fmp : listCollector.getList()) {
	// outputItem = new WikiGer24_Oct11_21d_OutputModel(fmp.getValue(), fmp.getForecasted(), fmp.getConfidenceLevel(), fmp.getConfidenceUpper(),
	// fmp.getConfidenceLower(), fcMethod.name());
	// outputList.add(outputItem);
	// }
	//
	// try {
	// final OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(fileNameOut), "UTF-8");
	// writer = new CSVWriter(osw, ';');
	// final ColumnPositionMappingStrategy<WikiGer24_Oct11_21d_OutputModel> strategy = new ColumnPositionMappingStrategy<WikiGer24_Oct11_21d_OutputModel>();
	// strategy.setType(WikiGer24_Oct11_21d_OutputModel.class);
	// final String[] columns = new String[] { "pageRequests", "forecast", "forecaster", "confidence", "confidenceUpper", "confidenceLower" };
	// strategy.setColumnMapping(columns);
	//
	// final BeanToCsv<WikiGer24_Oct11_21d_OutputModel> bean = new BeanToCsv<WikiGer24_Oct11_21d_OutputModel>();
	// bean.write(strategy, writer, outputList);
	// writer.close();
	// } catch (final IOException e) {
	// LOG.warn("An exception occurred", e);
	// }
	// }
}
// private static void startWikipediaExperiment(final ForecastMethod fcMethod) throws IllegalStateException, AnalysisConfigurationException {
// final Configuration controllerConfig = new Configuration();
// controllerConfig.setProperty(IProjectContext.CONFIG_PROPERTY_NAME_RECORDS_TIME_UNIT, "HOURS");
// final IAnalysisController analysisController = new AnalysisController(controllerConfig);
//
// final Configuration readerConfig = new Configuration();
// readerConfig.setProperty(SimpleTimeSeriesFileReader.CONFIG_PROPERTY_NAME_INPUT_FILE,
// "examples/analysis/opad-anomaly-detection/src/kieker/examples/analysis/opad/experiment-data/wikiGer24_Oct11_21d.csv");
//
// final SimpleTimeSeriesFileReader tsReader = new SimpleTimeSeriesFileReader(readerConfig, analysisController);
//
// final Configuration forecastConfig = new Configuration();
// forecastConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_FC_METHOD, fcMethod.name());
// forecastConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_DELTA_TIME, "1");
// forecastConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_DELTA_UNIT, "HOURS");
// forecastConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_FC_CONFIDENCE, "95");
// final ForecastingFilter forecaster = new ForecastingFilter(forecastConfig, analysisController);
//
// // final UniteMeasurementPairFilter uniteFilter = new UniteMeasurementPairFilter(new Configuration(), analysisController);
//
// // final TeeFilter tee = new TeeFilter(new Configuration(), analysisController);
//
// final ListCollectionFilter<ForecastMeasurementPair> listCollector = new ListCollectionFilter<ForecastMeasurementPair>(new Configuration(),
// analysisController);
//
// analysisController.connect(
// tsReader, SimpleTimeSeriesFileReader.OUTPUT_PORT_NAME_TS_POINTS,
// forecaster, ForecastingFilter.INPUT_PORT_NAME_TSPOINT);
//
// // analysisController.connect(
// // tsReader, SimpleTimeSeriesFileReader.OUTPUT_PORT_NAME_TS_POINTS,
// // uniteFilter, UniteMeasurementPairFilter.INPUT_PORT_NAME_TSPOINT);
//
// analysisController.connect(
// forecaster, ForecastingFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT,
// // uniteFilter, UniteMeasurementPairFilter.INPUT_PORT_NAME_FORECAST);
//
// // analysisController.connect(
// // uniteFilter, UniteMeasurementPairFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT,
// // tee, TeeFilter.INPUT_PORT_NAME_EVENTS);
// //
// // analysisController.connect(
// // tee, TeeFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS,
// listCollector, ListCollectionFilter.INPUT_PORT_NAME);
//
// // Start the analysis
// analysisController.run();
//
// try {
// Thread.sleep(3000);
// } catch (final InterruptedException e) {
// LOG.warn("An exception occurred", e);
// }
// analysisController.terminate();
//
// ExperimentStarter.analyzeData(listCollector.getList(), fcMethod.name());
//
// }

// private static void analyzeData(final List<ForecastMeasurementPair> resultList, final String methodName) {
// double diffSum = 0;
// double difference = 0;
// StringBuilder sb;
// for (final ForecastMeasurementPair fmPair : resultList) {
// difference = Math.abs(fmPair.getForecasted() - fmPair.getValue());
// diffSum += difference;
// sb = new StringBuilder();
// sb.append("[");
// sb.append(fmPair.getTime());
// sb.append("]");
// sb.append(" Forecasted: ");
// sb.append(fmPair.getForecasted());
// sb.append(", Measured: ");
// sb.append(fmPair.getValue());
// sb.append(", ConfidenceLevel: ");
// sb.append(fmPair.getConfidenceLevel());
// sb.append(", ConfidenceUpper: ");
// sb.append(fmPair.getConfidenceUpper());
// sb.append(", ConfidenceLower: ");
// sb.append(fmPair.getConfidenceLower());
// sb.append(", Difference: ");
// sb.append(difference);
// System.out.println(sb.toString());
// }
// System.out.println("[" + methodName + "] The average difference was: " + (diffSum / resultList.size()));
// }

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

// private static void openCSVtest() throws IOException {
// final String fileName =
// "examples/analysis/opad-anomaly-detection/src/kieker/examples/analysis/opad/experiment-data/de_20110101-00_20131223-23_extracted-from-projectcounts-ez-transformed-1.txt";
//
// final FileInputStream fis = new FileInputStream(fileName);
// final InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
// final BufferedReader br = new BufferedReader(isr);
// final char space = ' ';
// final CSVReader csvr = new CSVReader(br, space);
//
// final List<String[]> lines = csvr.readAll();
// for (final String[] line : lines) {
// System.out.println(line[3]);
// }
// csvr.close();
// }

// private static void readWriteCSVbean() {
// final String fileNameIn = "examples/analysis/opad-anomaly-detection/src/kieker/examples/analysis/opad/experiment-data/wikiGer24_Oct11_21d.csv";
// final String fileNameOut = "examples/analysis/opad-anomaly-detection/src/kieker/examples/analysis/opad/experiment-data/wikiGer24_Oct11_21d-out.csv";
// final List<WikiGer24_Oct11_21d_OutputModel> outputList = new ArrayList<WikiGer24_Oct11_21d_OutputModel>();
// CSVReader reader;
// try {
// reader = new CSVReader(new FileReader(fileNameIn));
//
// final ColumnPositionMappingStrategy<WikiGer24_Oct11_21d_InputModel> strategy = new ColumnPositionMappingStrategy<WikiGer24_Oct11_21d_InputModel>();
// // final HeaderColumnNameMappingStrategy<WikiGer24_Oct11_21d_InputModel> strategy = new
// // HeaderColumnNameMappingStrategy<WikiGer24_Oct11_21d_InputModel>();
// strategy.setType(WikiGer24_Oct11_21d_InputModel.class);
// final String[] columns = new String[] { "pageRequests" };
// strategy.setColumnMapping(columns);
// final CsvToBean<WikiGer24_Oct11_21d_InputModel> csv = new CsvToBean<WikiGer24_Oct11_21d_InputModel>();
// final List<WikiGer24_Oct11_21d_InputModel> objectList = csv.parse(strategy, reader);
//
// WikiGer24_Oct11_21d_OutputModel om;
// for (final WikiGer24_Oct11_21d_InputModel im : objectList) {
// System.out.println(im.getPageRequests());
// om = new WikiGer24_Oct11_21d_OutputModel();
// om.setPageRequests(im.getPageRequests());
// om.setForecast(im.getPageRequests() + 1);
// om.setConfidence(0);
// outputList.add(om);
// }
// reader.close();
// } catch (final FileNotFoundException e) {
// LOG.warn("An exception occurred", e);
// } catch (final IOException e) {
// LOG.warn("An exception occurred", e);
// }
//
// CSVWriter writer;
//
// try {
// writer = new CSVWriter(new FileWriter(fileNameOut), ';');
// final ColumnPositionMappingStrategy<WikiGer24_Oct11_21d_OutputModel> strategy = new ColumnPositionMappingStrategy<WikiGer24_Oct11_21d_OutputModel>();
// strategy.setType(WikiGer24_Oct11_21d_OutputModel.class);
// final String[] columns = new String[] { "pageRequests", "forecast", "confidence" };
// strategy.setColumnMapping(columns);
//
// final BeanToCsv<WikiGer24_Oct11_21d_OutputModel> bean = new BeanToCsv<WikiGer24_Oct11_21d_OutputModel>();
// bean.write(strategy, writer, outputList);
// writer.close();
// } catch (final IOException e) {
// LOG.warn("An exception occurred", e);
// }
// }

