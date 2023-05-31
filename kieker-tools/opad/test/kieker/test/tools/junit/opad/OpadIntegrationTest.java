/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
import kieker.tools.opad.filter.AnomalyDetectionFilter;
import kieker.tools.opad.filter.AnomalyScoreCalculationFilter;
import kieker.tools.opad.filter.ExtractionFilter;
import kieker.tools.opad.filter.ForecastingFilter;
import kieker.tools.opad.filter.TimeSeriesPointAggregatorFilter;
import kieker.tools.opad.record.NamedDoubleRecord;
import kieker.tools.opad.record.StorableDetectionResult;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * This test creates some ResponseTimeDoubleRecords and let them run through all currently
 * available OPAD Filter. The ResponseTimeDoubleRecords are create as records from different
 * applications. (Requires MongoDB Connection and also writes to the database because of the
 * SendAndStoreDetectionResultsFilter)
 *
 * @author Tom Frotscher
 * @since 1.10
 */
public class OpadIntegrationTest extends AbstractKiekerTest {

	private static final String OP_SIGNATURE_A = "a.A.opA";
	private static final String OP_SIGNATURE_B = "b.B.opB";

	private AnalysisController controller;

	// Mockup sink
	private ListCollectionFilter<StorableDetectionResult> sinkPluginIfAnomaly;
	private ListCollectionFilter<StorableDetectionResult> sinkPluginElse;

	/**
	 * Creates a new instance of this class.
	 */
	public OpadIntegrationTest() {
		// empty default constructor
	}

	private List<NamedDoubleRecord> createInputEventSetOER() {
		final List<NamedDoubleRecord> retList = new ArrayList<>();
		// First Span:
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_A, 1500000, 500000));
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_B, 2200000, 125000));
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_A, 2400000, 100000));
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_A, 2600000, 100000));
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_B, 2500000, 162200));
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_A, 2900000, 200000));
		// Second Span:
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_B, 4360000, 620000));
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_A, 4300000, 200000));
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_A, 4400000, 50000));
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_B, 4100000, 70000));
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_A, 4640000, 40000));
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_A, 4900000, 100000));
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_A, 4900000, 800000));
		// Third Span:
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_A, 5300000, 200000));
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_B, 5350000, 789000));
		// Fourth Span:
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_A, 7200000, 100000));
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_B, 7300000, 678000));
		// Fifth Span:
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_B, 9170000, 70866000));
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_A, 9150000, 50000));
		// Sixth Span exceeded: (Anomaly A)
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_B, 11200000, 706000));
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_A, 11000000, 10200000));
		// One Span exceeded:
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_B, 15400000, 13900000));
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_A, 15400000, 13900000));
		return retList;
	}

	/**
	 * Setup for the VariateOPADIntegrationTest.
	 *
	 * @throws IllegalStateException
	 *             If illegal state
	 * @throws AnalysisConfigurationException
	 *             If wrong configuration
	 */
	@Before
	public void setUp() throws IllegalStateException,
			AnalysisConfigurationException {
		this.controller = new AnalysisController();

		// Start - Read OperationExecutionRecords
		final Configuration readerOERConfiguration = new Configuration();
		readerOERConfiguration.setProperty(ListReader.CONFIG_PROPERTY_NAME_AWAIT_TERMINATION, Boolean.TRUE.toString());
		final ListReader<NamedDoubleRecord> theReaderNamedDoubleRecord = new ListReader<>(readerOERConfiguration, this.controller);
		theReaderNamedDoubleRecord.addAllObjects(this.createInputEventSetOER());
		// End - Read OperationExecutionRecords

		// Start - ExtractionFilter Configuration
		// ExtractionFilter Configuration
		final Configuration extractionConfiguration = new Configuration();
		extractionConfiguration.setProperty(ExtractionFilter.CONFIG_PROPERTY_NAME_TIMEUNIT, "MILLISECONDS");
		final ExtractionFilter extractionFilter = new ExtractionFilter(extractionConfiguration, this.controller);
		// End - ResponseTimeExtractionFilter

		// Start - TimeSeriesPointAggregatorFilter
		// TimeSeriesPointAggregator Configuration
		final Configuration aggregationConfiguration = new Configuration();
		aggregationConfiguration.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_SPAN, "2");
		aggregationConfiguration.setProperty(TimeSeriesPointAggregatorFilter.CONFIG_PROPERTY_NAME_AGGREGATION_TIMEUNIT, "NANOSECONDS");
		final TimeSeriesPointAggregatorFilter aggregationFilter = new TimeSeriesPointAggregatorFilter(aggregationConfiguration, this.controller);
		// End - TimeSeriesPointAggregatorFilter

		// Start - ForecastingFilter
		// ForecastingFilter Configuration
		final Configuration forecastConfiguration = new Configuration();
		forecastConfiguration.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_DELTA_TIME, "10");
		forecastConfiguration.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_DELTA_UNIT, "MILLISECONDS");
		forecastConfiguration.setProperty(ForecastingFilter.CONFIG_PROPERTY_NAME_FC_METHOD, "MEANJAVA");
		final ForecastingFilter forecasting = new ForecastingFilter(forecastConfiguration, this.controller);
		// End - ForecastingFilter

		// Start - AnomalyScoreCalculatorFilter
		final Configuration scoreConfiguration = new Configuration();
		final AnomalyScoreCalculationFilter scoreCalc = new AnomalyScoreCalculationFilter(scoreConfiguration, this.controller);
		// End - AnomalyScoreCalculatorFilter

		// Start - AnomalyDetectionFilter
		// AnomalyDetectionFilter Configuration
		final Configuration configAnomalyPre = new Configuration();
		configAnomalyPre.setProperty(AnomalyDetectionFilter.CONFIG_PROPERTY_NAME_THRESHOLD, "0.23");
		final AnomalyDetectionFilter anomalyDetectionFilter = new AnomalyDetectionFilter(configAnomalyPre, this.controller);
		// End - AnomalyDetectionFilter

		// SINK 1
		this.sinkPluginIfAnomaly = new ListCollectionFilter<>(new Configuration(), this.controller);
		// SINK 2
		this.sinkPluginElse = new ListCollectionFilter<>(new Configuration(), this.controller);

		// CONNECT the filters
		// Mock-up Reader (NamedDoubleRecords) -> Extraction Input
		this.controller
				.connect(theReaderNamedDoubleRecord, ListReader.OUTPUT_PORT_NAME, extractionFilter,
						ExtractionFilter.INPUT_PORT_NAME_VALUE);
		// ExtractionFilter -> Aggregator Input
		this.controller
				.connect(extractionFilter, ExtractionFilter.OUTPUT_PORT_NAME_VALUE, aggregationFilter,
						TimeSeriesPointAggregatorFilter.INPUT_PORT_NAME_TSPOINT);
		// AggregatorFilter -> Forecast Input
		this.controller
				.connect(aggregationFilter, TimeSeriesPointAggregatorFilter.OUTPUT_PORT_NAME_AGGREGATED_TSPOINT, forecasting,
						ForecastingFilter.INPUT_PORT_NAME_TSPOINT);

		// Forecast Output -> AnomalyScoreCalculation Input
		this.controller
				.connect(forecasting, ForecastingFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, scoreCalc,
						AnomalyScoreCalculationFilter.INPUT_PORT_CURRENT_FORECAST_PAIR);
		// ScoreCalculation Output -> AnomalyDetection Input
		this.controller
				.connect(scoreCalc, AnomalyScoreCalculationFilter.OUTPUT_PORT_ANOMALY_SCORE, anomalyDetectionFilter,
						AnomalyDetectionFilter.INPUT_PORT_ANOMALY_SCORE);
		// AnomalyDetection -> Sinks
		this.controller.connect(anomalyDetectionFilter, AnomalyDetectionFilter.OUTPUT_PORT_ANOMALY_SCORE_IF_ANOMALY,
				this.sinkPluginIfAnomaly, ListCollectionFilter.INPUT_PORT_NAME);
		this.controller.connect(anomalyDetectionFilter, AnomalyDetectionFilter.OUTPUT_PORT_ANOMALY_SCORE_ELSE,
				this.sinkPluginElse, ListCollectionFilter.INPUT_PORT_NAME);
	}

	/**
	 * Starts a complete test flow through all currently available filters. In this case, the NamedDoubleRecords can
	 * be from different applications and will still be treated correctly.
	 *
	 * @throws InterruptedException
	 *             If interrupted
	 */
	@Test
	public void testOpadFlow() throws InterruptedException {
		final AnalysisControllerThread thread = new AnalysisControllerThread(this.controller);
		thread.start();
		Thread.sleep(2000);
		thread.terminate();

		Assert.assertEquals(7, this.sinkPluginIfAnomaly.getList().size());
		Assert.assertEquals(6, this.sinkPluginElse.getList().size());

	}

}
