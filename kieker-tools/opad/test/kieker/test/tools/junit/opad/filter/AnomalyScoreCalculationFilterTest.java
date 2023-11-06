/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.opad.filter;

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
import kieker.tools.opad.filter.AnomalyScoreCalculationFilter;
import kieker.tools.opad.model.ForecastMeasurementPair;
import kieker.tools.opad.record.StorableDetectionResult;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * Test for the AnomalyScoreCalculationFilter. Therefore comparing the result of the Filter with some
 * previous manually calculated Results.
 *
 * @since 1.10
 * @author Tom Frotscher
 */
public class AnomalyScoreCalculationFilterTest extends AbstractKiekerTest {

	private static final String OP_SIGNATURE_A = "a.A.opA";

	private AnalysisController controller;
	private ListCollectionFilter<StorableDetectionResult> sinkAnomalyScore;

	/**
	 * Creates a new instance of this class.
	 */
	public AnomalyScoreCalculationFilterTest() {
		// empty default constructor
	}

	private ForecastMeasurementPair createFMP(final String name, final Double forecast, final Double measurement) {
		return new ForecastMeasurementPair(name, forecast, measurement, System.currentTimeMillis());
	}

	private List<ForecastMeasurementPair> createInputEventSetScoreCalc() {
		final List<ForecastMeasurementPair> retList = new ArrayList<>();

		retList.add(this.createFMP(OP_SIGNATURE_A, 0.6, 0.4));
		retList.add(this.createFMP(OP_SIGNATURE_A, 0.3, 0.4));
		retList.add(this.createFMP(OP_SIGNATURE_A, 0.5, 0.5));

		return retList;
	}

	/**
	 * Set up for the AnomalyScoreCalculationFilterTest.
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

		// READER
		final Configuration readerScoreCalcConfiguration = new Configuration();
		readerScoreCalcConfiguration.setProperty(ListReader.CONFIG_PROPERTY_NAME_AWAIT_TERMINATION, Boolean.TRUE.toString());
		final ListReader<ForecastMeasurementPair> theReaderScoreCalc = new ListReader<>(readerScoreCalcConfiguration, this.controller);
		theReaderScoreCalc.addAllObjects(this.createInputEventSetScoreCalc());

		final Configuration scoreConfiguration = new Configuration();
		final AnomalyScoreCalculationFilter scoreCalc = new AnomalyScoreCalculationFilter(scoreConfiguration, this.controller);

		// SINK 1
		this.sinkAnomalyScore = new ListCollectionFilter<>(new Configuration(), this.controller);

		// CONNECTION
		this.controller
				.connect(theReaderScoreCalc, ListReader.OUTPUT_PORT_NAME, scoreCalc, AnomalyScoreCalculationFilter.INPUT_PORT_CURRENT_FORECAST_PAIR);
		this.controller
				.connect(scoreCalc, AnomalyScoreCalculationFilter.OUTPUT_PORT_ANOMALY_SCORE, this.sinkAnomalyScore, ListCollectionFilter.INPUT_PORT_NAME);
	}

	/**
	 * Test of the AnomalyScoreCalculationFilter.
	 *
	 * @throws InterruptedException
	 *             If interrupted
	 * @throws IllegalStateException
	 *             If illegal state
	 * @throws AnalysisConfigurationException
	 *             If wrong configuration
	 */
	@Test
	public void testAnomalyScoreCalculationOnly() throws InterruptedException, IllegalStateException, AnalysisConfigurationException {
		final AnalysisControllerThread thread = new AnalysisControllerThread(this.controller);
		thread.start();

		Thread.sleep(1000);
		thread.terminate();
		Assert.assertEquals(3, this.sinkAnomalyScore.getList().size());
		Assert.assertEquals(this.sinkAnomalyScore.getList().get(0).getScore(), 0.19999999999999996, 0);
		Assert.assertEquals(this.sinkAnomalyScore.getList().get(1).getScore(), 0.1428571428571429, 0);
		Assert.assertEquals(this.sinkAnomalyScore.getList().get(2).getScore(), 0.0, 0);
	}

}
