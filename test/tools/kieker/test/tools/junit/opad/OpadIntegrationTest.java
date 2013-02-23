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

package kieker.test.tools.junit.opad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.AnalysisControllerThread;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.tools.opad.filter.AnomalyDetectionFilter;
import kieker.tools.opad.filter.AnomalyScoreCalculationFilter;
import kieker.tools.opad.filter.ForecastingFilter;
import kieker.tools.opad.record.NamedDoubleTimeSeriesPoint;

/**
 * 
 * @author Tom Frotscher
 */
public class OpadIntegrationTest {

	private AnalysisController controller;

	// Variables ForecastingFilter
	private ListReader<NamedDoubleTimeSeriesPoint> theReaderForecast;
	private ForecastingFilter forecasting;

	// HelperMethods ForecastingFilter
	private List<NamedDoubleTimeSeriesPoint> createInputEventSetForecast() {
		final List<NamedDoubleTimeSeriesPoint> retList = new ArrayList<NamedDoubleTimeSeriesPoint>();
		retList.add(this.createNDTSP(OP_SIGNATURE_A, 0.3));
		retList.add(this.createNDTSP(OP_SIGNATURE_A, 6.0));
		retList.add(this.createNDTSP(OP_SIGNATURE_A, 0.5));
		return retList;
	}

	private NamedDoubleTimeSeriesPoint createNDTSP(final String signature, final double value) {
		final NamedDoubleTimeSeriesPoint r = new NamedDoubleTimeSeriesPoint(new Date(), value, signature);
		return r;
	}

	// Variables AnomalyScoreCalculationFilter
	private AnomalyScoreCalculationFilter scoreCalc;

	// Variables AnomalyDetectionFilter
	private AnomalyDetectionFilter anomalyDetectionFilter;
	private ListCollectionFilter<NamedDoubleTimeSeriesPoint> sinkPluginIfAnomaly;
	private ListCollectionFilter<NamedDoubleTimeSeriesPoint> sinkPluginElse;
	private static final String OP_SIGNATURE_A = "a.A.opA";

	@Before
	public void setUp() throws IllegalStateException,
			AnalysisConfigurationException {
		this.controller = new AnalysisController();

		// Start - ForecastingFilter
		// READER with Mock-up Data
		final Configuration readerForecastConfiguration = new Configuration();
		readerForecastConfiguration.setProperty(ListReader.CONFIG_PROPERTY_NAME_AWAIT_TERMINATION, Boolean.TRUE.toString());
		this.theReaderForecast = new ListReader<NamedDoubleTimeSeriesPoint>(readerForecastConfiguration);
		this.theReaderForecast.addAllObjects(this.createInputEventSetForecast());
		this.controller.registerReader(this.theReaderForecast);

		// ForecastingFilter Configuration
		final Configuration forecastConfiguration = new Configuration();
		forecastConfiguration.setProperty(ForecastingFilter.CONFIG_PROPERTY_DELTA_TIME, "1000");
		forecastConfiguration.setProperty(ForecastingFilter.CONFIG_PROPERTY_DELTA_UNIT,
				"MILLISECONDS");
		forecastConfiguration.setProperty(ForecastingFilter.CONFIG_PROPERTY_FC_METHOD, "MEAN");
		this.forecasting = new ForecastingFilter(forecastConfiguration);
		this.controller.registerFilter(this.forecasting);
		// End - ForecastingFilter

		// Start - AnomalyScoreCalculatorFilter
		final Configuration scoreConfiguration = new Configuration();
		this.scoreCalc = new AnomalyScoreCalculationFilter(scoreConfiguration);
		this.controller.registerFilter(this.scoreCalc);
		// End - AnomalyScoreCalculatorFilter

		// Start - AnomalyDetectionFilter
		// AnomalyDetectionFilter Configuration
		final Configuration configAnomaly = new Configuration();
		configAnomaly.setProperty(AnomalyDetectionFilter.CONFIG_PROPERTY_THRESHOLD, "0.6");
		this.anomalyDetectionFilter = new AnomalyDetectionFilter(configAnomaly);
		this.controller.registerFilter(this.anomalyDetectionFilter);

		// SINK 1 Mock-up
		this.sinkPluginIfAnomaly = new ListCollectionFilter<NamedDoubleTimeSeriesPoint>(new Configuration());
		this.controller.registerFilter(this.sinkPluginIfAnomaly);

		// SINK 2 Mock-up
		this.sinkPluginElse = new ListCollectionFilter<NamedDoubleTimeSeriesPoint>(new Configuration());
		this.controller.registerFilter(this.sinkPluginElse);
		// End - AnomalyDetectionFilter

		// CONNECT the filters
		// Mock-up Reader -> Forecast Input
		this.controller
				.connect(this.theReaderForecast, ListReader.OUTPUT_PORT_NAME, this.forecasting, ForecastingFilter.INPUT_PORT_NAME_TSPOINT);
		// Forecast Output -> AnomalyScoreCalculation Input
		this.controller
				.connect(this.forecasting, ForecastingFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, this.scoreCalc,
						AnomalyScoreCalculationFilter.INPUT_PORT_CURRENT_FORECAST_PAIR);
		// ScoreCalculation Output -> AnomalyDetection Input
		this.controller
				.connect(this.scoreCalc, AnomalyScoreCalculationFilter.OUTPUT_PORT_ANOMALY_SCORE, this.anomalyDetectionFilter,
						AnomalyDetectionFilter.INPUT_PORT_ANOMALY_SCORE);

		// AnomalyDetection Output -> Mock-up Sinks
		this.controller
				.connect(this.anomalyDetectionFilter, AnomalyDetectionFilter.OUTPUT_PORT_ANOMALY_SCORE_IF_ANOMALY, this.sinkPluginIfAnomaly,
						ListCollectionFilter.INPUT_PORT_NAME);
		this.controller
				.connect(this.anomalyDetectionFilter, AnomalyDetectionFilter.OUTPUT_PORT_ANOMALY_SCORE_ELSE, this.sinkPluginElse,
						ListCollectionFilter.INPUT_PORT_NAME);

		Assert.assertTrue(this.sinkPluginIfAnomaly.getList().isEmpty());

	}

	// Test complete Flow
	@Test
	public void testOpadFlow() throws InterruptedException {

		final AnalysisControllerThread thread = new AnalysisControllerThread(this.controller);
		thread.start();

		Thread.sleep(2000);
		thread.terminate();

		Assert.assertEquals(2, this.sinkPluginIfAnomaly.getList().size());
		Assert.assertEquals(1, this.sinkPluginElse.getList().size());

	}

}
