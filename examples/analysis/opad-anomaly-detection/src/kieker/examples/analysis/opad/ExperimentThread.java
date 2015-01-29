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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;
import au.com.bytecode.opencsv.bean.BeanToCsv;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.IProjectContext;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.examples.analysis.opad.experimentModel.WikiGer24_Oct11_21d_OutputModel;
import kieker.tools.opad.filter.ForecastingFilter;
import kieker.tools.opad.model.ForecastMeasurementPair;
import kieker.tools.opad.model.NamedDoubleTimeSeriesPoint;
import kieker.tools.tslib.ForecastMethod;

/**
 * @author Thomas Duellmann
 *
 * @since 1.11
 */
public class ExperimentThread extends Thread {

	private static final Log LOG = LogFactory.getLog(ExperimentThread.class);
	private final List<NamedDoubleTimeSeriesPoint> readData;
	private final String fileName;
	private final ForecastMethod fcMethod;
	private final String outputFileBase;

	public ExperimentThread(final String fileName, final ForecastMethod fcMethod, final List<NamedDoubleTimeSeriesPoint> readData, final String outputFileBase) {
		this.readData = readData;
		this.fileName = fileName;
		this.fcMethod = fcMethod;
		this.outputFileBase = outputFileBase;
	}

	@Override
	public void run() {
		ExperimentThread.LOG.info("Starting experiment for " + this.fcMethod.name());
		List<ForecastMeasurementPair> measurements;
		try {
			measurements = this.forecastWithR(this.readData, this.fcMethod);
			this.writeToCsv(measurements, this.fileName, this.fcMethod);
		} catch (final IllegalStateException e) {
			LOG.warn("An exception occurred", e);
		} catch (final AnalysisConfigurationException e) {
			LOG.warn("An exception occurred", e);
		}
		ExperimentThread.LOG.info("Done with experiment for " + this.fcMethod.name());
	}

	private List<ForecastMeasurementPair> forecastWithR(final List<NamedDoubleTimeSeriesPoint> data, final ForecastMethod fcMethod)
			throws IllegalStateException, AnalysisConfigurationException {
		final Configuration controllerConfig = new Configuration();
		controllerConfig.setProperty(IProjectContext.CONFIG_PROPERTY_NAME_RECORDS_TIME_UNIT, "HOURS");
		final IAnalysisController analysisController = new AnalysisController(controllerConfig);

		final ListReader<NamedDoubleTimeSeriesPoint> listReader = new ListReader<NamedDoubleTimeSeriesPoint>(new Configuration(), analysisController);

		final Configuration forecastConfig = new Configuration();
		forecastConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_FC_METHOD, fcMethod.name());
		forecastConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_DELTA_TIME, "1");
		forecastConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_DELTA_UNIT, "HOURS");
		forecastConfig.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_FC_CONFIDENCE, "95");
		final ForecastingFilter forecaster = new ForecastingFilter(forecastConfig, analysisController);

		final ListCollectionFilter<ForecastMeasurementPair> listCollector = new ListCollectionFilter<ForecastMeasurementPair>(new Configuration(),
				analysisController);

		analysisController.connect(
				listReader, ListReader.OUTPUT_PORT_NAME,
				forecaster, ForecastingFilter.INPUT_PORT_NAME_TSPOINT);

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

	private void writeToCsv(final List<ForecastMeasurementPair> measurements, final String fileNameBase, final ForecastMethod forecaster) {
		CSVWriter writer;
		final List<WikiGer24_Oct11_21d_OutputModel> outputList = new ArrayList<WikiGer24_Oct11_21d_OutputModel>();

		WikiGer24_Oct11_21d_OutputModel outputItem;
		for (final ForecastMeasurementPair fmp : measurements) {
			outputItem = new WikiGer24_Oct11_21d_OutputModel(fmp.getValue(), fmp.getForecasted(), fmp.getConfidenceLevel(), fmp.getConfidenceUpper(),
					fmp.getConfidenceLower(), forecaster.name(), fmp.getMASE());
			outputList.add(outputItem);
		}

		try {
			final OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(this.outputFileBase + "-" + forecaster.name() + ".csv"),
					"UTF-8");
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
}
