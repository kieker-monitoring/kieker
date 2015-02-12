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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;

import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.examples.analysis.opad.experimentModel.WikiGer24_Oct11_21d_InputModel;
import kieker.tools.opad.model.NamedDoubleTimeSeriesPoint;
import kieker.tools.tslib.ForecastMethod;
import kieker.tools.util.InvalidREvaluationResultException;
import kieker.tools.util.RBridgeControl;

/**
 * Starts the forecast on the given data using different forecasting methods,
 * stores it in the output-data folder and plots the corresponding graphs
 * using an R-script.
 *
 * @author Thomas Duellmann
 *
 * @since 1.11
 */
public final class ExperimentStarter {

	/**
	 * Get Logger for this class.
	 */
	private static final Log LOG = LogFactory.getLog(ExperimentStarter.class);

	/**
	 * Basename of the input data file. This will be the prefix of all resulting data.
	 */
	private static final String DATA_BASENAME = "wikiGer24_Oct11_21d";

	/**
	 * Folder where the following subfolders and the R-script are located.
	 */
	private static final String OPAD_EXAMPLE_FOLDER = "./";

	/**
	 * Folder where the input file is located. *
	 */
	private static final String INPUT_DATA_FOLDER = OPAD_EXAMPLE_FOLDER + "input-data/";

	/**
	 * Folder where the output files will be located after the forecasting is finished.
	 */
	private static final String OUTPUT_DATA_FOLDER = OPAD_EXAMPLE_FOLDER + "output-data/";

	/**
	 * Name of the R plotting script.
	 */
	private static final String R_PLOT_SCRIPT = "dataPlot.r";

	private ExperimentStarter() {}

	/**
	 * Executing the main method starts the experiment.
	 */
	public static void main(final String[] args) throws IllegalStateException, AnalysisConfigurationException, InterruptedException {
		final List<ForecastMethod> methods = new ArrayList<ForecastMethod>();
		/*
		methods.add(ForecastMethod.ARIMA101);
		methods.add(ForecastMethod.CS);
		methods.add(ForecastMethod.ETS);
		methods.add(ForecastMethod.SES);
		methods.add(ForecastMethod.ARIMA);
		methods.add(ForecastMethod.MEAN);
		methods.add(ForecastMethod.NAIVE);
		methods.add(ForecastMethod.MEANJAVA);
		*/

		// Forecasting with CROSTON takes a very long time - therefore it is disabled by default
		// methods.add(ForecastMethod.CROSTON);

		final String inputFileName = INPUT_DATA_FOLDER + DATA_BASENAME + ".csv";

		ExperimentStarter.LOG.info("Starting to read data from " + inputFileName);
		final List<NamedDoubleTimeSeriesPoint> readData = ExperimentStarter.readFromCsv(inputFileName);
		ExperimentStarter.LOG.info("Done reading data from " + inputFileName + ".csv");

		final List<ExperimentThread> experimentThreads = new ArrayList<ExperimentThread>();

		for (final ForecastMethod fcMethod : methods) {
			final ExperimentThread et = new ExperimentThread(inputFileName, fcMethod, readData, OUTPUT_DATA_FOLDER + DATA_BASENAME);
			experimentThreads.add(et);
			et.start();
		}

		for (final ExperimentThread et : experimentThreads) {
			et.join();
		}

		ExperimentStarter.LOG.info("All experiments completed.");

		ExperimentStarter.LOG.info("Connecting to R and plot the forecasting results.");
		final RBridgeControl rBridge = RBridgeControl.getInstance();
		final String baseFolder = (new File(OPAD_EXAMPLE_FOLDER)).getAbsolutePath() + "/";
		try {

			// Set the working directory of R to the OPAD_EXAMPLE_FOLDER
			final String setwdCmdString = "setwd('" + baseFolder + "')";
			System.out.println("PWD:" + setwdCmdString);
			ExperimentStarter.LOG.info("Setting working directory ( " + setwdCmdString + " )");
			rBridge.evalWithR(setwdCmdString);

			// Execute the plotting script
			final String sourceCmdString = "source('" + R_PLOT_SCRIPT + "', local=TRUE, echo=FALSE)";
			ExperimentStarter.LOG.info("Executing plotting script ( " + sourceCmdString + " )");
			rBridge.evalWithR(sourceCmdString);
		} catch (final InvalidREvaluationResultException e) {
			LOG.warn("Could not execute the plotting script.", e);
		}
		ExperimentStarter.LOG.info("Done connecting to R and plotting the forecasting results.");
	}

	private static List<NamedDoubleTimeSeriesPoint> readFromCsv(final String fileName) {
		CSVReader reader;
		final List<NamedDoubleTimeSeriesPoint> outputList = new ArrayList<NamedDoubleTimeSeriesPoint>();
		try {
			final InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
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
}
