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
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.opad.filter.AnomalyDetectionFilter;
import kieker.tools.opad.filter.AnomalyScoreCalculationFilter;
import kieker.tools.opad.filter.ForecastingFilter;
import kieker.tools.opad.filter.ResponseTimeExtractionFilter;
import kieker.tools.opad.record.NamedDoubleTimeSeriesPoint;

/**
 * This test creates some OperationExecutionRecords and let them run through all currently
 * available OPAD Filter.
 * 
 * @author Tom Frotscher
 */
public class OpadIntegrationTest {

	private static final String OP_SIGNATURE_A = "a.A.opA";
	private static final String SESSION_ID_TEST = "TestId";
	private static final String HOST_ID_TEST = "TestPC";
	private static final long TRACE_ID_TEST = (long) 0.1;

	private AnalysisController controller;

	// Variables ResponsetimeExtractionFilter
	private ResponseTimeExtractionFilter responsetimeExtr;

	// Variables ForecastingFilter
	private ForecastingFilter forecasting;

	// Variables AnomalyScoreCalculationFilter
	private AnomalyScoreCalculationFilter scoreCalc;

	// Variables AnomalyDetectionFilter
	private AnomalyDetectionFilter anomalyDetectionFilter;
	private ListCollectionFilter<NamedDoubleTimeSeriesPoint> sinkPluginIfAnomaly;
	private ListCollectionFilter<NamedDoubleTimeSeriesPoint> sinkPluginElse;

	// Variables Mockup OperationExecutionReader
	private ListReader<OperationExecutionRecord> theReaderOperationExecutionRecords;

	public OpadIntegrationTest() {
		// empty default constructor
	}

	// HelperMethods Mockup OperationExecutionReader
	private List<OperationExecutionRecord> createInputEventSetOER() {
		final List<OperationExecutionRecord> retList = new ArrayList<OperationExecutionRecord>();
		retList.add(this.createOER(OP_SIGNATURE_A, SESSION_ID_TEST, TRACE_ID_TEST, 1001, 1002));
		retList.add(this.createOER(OP_SIGNATURE_A, SESSION_ID_TEST, TRACE_ID_TEST, 4000, 22243));
		retList.add(this.createOER(OP_SIGNATURE_A, SESSION_ID_TEST, TRACE_ID_TEST, 4021, 5057));
		return retList;
	}

	private OperationExecutionRecord createOER(final String signature, final String sessionid, final long traceid, final long tin, final long tout) {
		final OperationExecutionRecord oer = new OperationExecutionRecord(signature, sessionid, traceid, tin, tout, HOST_ID_TEST, -1, -1);
		return oer;
	}

	@Before
	public void setUp() throws IllegalStateException,
			AnalysisConfigurationException {
		this.controller = new AnalysisController();

		// Start - Read OperationExecutionRecords
		final Configuration readerOERConfiguration = new Configuration();
		readerOERConfiguration.setProperty(ListReader.CONFIG_PROPERTY_NAME_AWAIT_TERMINATION, Boolean.TRUE.toString());
		this.theReaderOperationExecutionRecords = new ListReader<OperationExecutionRecord>(readerOERConfiguration, this.controller);
		this.theReaderOperationExecutionRecords.addAllObjects(this.createInputEventSetOER());
		// End - Read OperationExecutionRecords

		// Start - ResponseTimeExtractionFilter Configuration
		// ResponseTimeExtractionFilter Configuration
		final Configuration responseTimeExtractionConfiguration = new Configuration();
		this.responsetimeExtr = new ResponseTimeExtractionFilter(responseTimeExtractionConfiguration, this.controller);
		// End - ResponseTimeExtractionFilter

		// Start - ForecastingFilter
		// ForecastingFilter Configuration
		final Configuration forecastConfiguration = new Configuration();
		forecastConfiguration.setProperty(ForecastingFilter.CONFIG_PROPERTY_DELTA_TIME, "1000");
		forecastConfiguration.setProperty(ForecastingFilter.CONFIG_PROPERTY_DELTA_UNIT,
				"MILLISECONDS");
		forecastConfiguration.setProperty(ForecastingFilter.CONFIG_PROPERTY_FC_METHOD, "MEAN");
		this.forecasting = new ForecastingFilter(forecastConfiguration, this.controller);
		// End - ForecastingFilter

		// Start - AnomalyScoreCalculatorFilter
		final Configuration scoreConfiguration = new Configuration();
		this.scoreCalc = new AnomalyScoreCalculationFilter(scoreConfiguration, this.controller);
		// End - AnomalyScoreCalculatorFilter

		// Start - AnomalyDetectionFilter
		// AnomalyDetectionFilter Configuration
		final Configuration configAnomaly = new Configuration();
		configAnomaly.setProperty(AnomalyDetectionFilter.CONFIG_PROPERTY_THRESHOLD, "0.6");
		this.anomalyDetectionFilter = new AnomalyDetectionFilter(configAnomaly, this.controller);

		// SINK 1 Mock-up
		this.sinkPluginIfAnomaly = new ListCollectionFilter<NamedDoubleTimeSeriesPoint>(new Configuration(), this.controller);

		// SINK 2 Mock-up
		this.sinkPluginElse = new ListCollectionFilter<NamedDoubleTimeSeriesPoint>(new Configuration(), this.controller);
		// End - AnomalyDetectionFilter

		// CONNECT the filters
		// Mock-up Reader (OperationExecutionRecords) -> ResponseTimeExtractionFIlter
		this.controller
				.connect(this.theReaderOperationExecutionRecords, ListReader.OUTPUT_PORT_NAME, this.responsetimeExtr,
						ResponseTimeExtractionFilter.INPUT_PORT_NAME_VALUE);
		// ResponseTimeExtractionFilter -> Forecast Input
		this.controller
				.connect(this.responsetimeExtr, ResponseTimeExtractionFilter.OUTPUT_PORT_NAME_VALUE, this.forecasting,
						ForecastingFilter.INPUT_PORT_NAME_TSPOINT);
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

		Assert.assertEquals(1, this.sinkPluginIfAnomaly.getList().size());
		Assert.assertEquals(2, this.sinkPluginElse.getList().size());

	}

}
