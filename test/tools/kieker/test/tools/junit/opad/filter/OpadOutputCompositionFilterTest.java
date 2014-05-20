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

package kieker.test.tools.junit.opad.filter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.tools.opad.filter.OpadOutputCompositionFilter;
import kieker.tools.opad.record.ExtendedStorableDetectionResult;
import kieker.tools.opad.record.OpadOutputData;

import kieker.test.common.junit.AbstractKiekerTest;

//NOCS

/**
 * Tests the composition of the OpadOutputCompositionFilter.
 * 
 * @author Thomas Düllmann
 * @since 1.10
 * 
 */
public class OpadOutputCompositionFilterTest extends AbstractKiekerTest {

	private static final String HOSTNAME = "hostname";
	private static final String APPNAME = "appname";
	private static final String OPERATION = "public void doSomething(java.lang.String)";

	// ESDR: Variables that are transferred via the ExtendedStorableDetectionResult object which is initialized with those values.
	private static final String ESDR_HOST_APP_OPERATION_NAME = HOSTNAME + "+" + APPNAME + ":" + OPERATION;
	private static final long ESDR_LATENCY = 50L;
	private static final long ESDR_TIMESTAMP = 1337L;
	private static final double ESDR_FORECAST = 42d;
	private static final double ESDR_SCORE = 0.2d;
	private static final double ESDR_ANOMALY_THRESHOLD = 0.23d;

	private IAnalysisController controller;
	private ListReader<ExtendedStorableDetectionResult> listReader;
	private ListCollectionFilter<OpadOutputData> listCollectionFilter;
	private OpadOutputCompositionFilter ooCompFilter;
	private ExtendedStorableDetectionResult detectionResult;
	private OpadOutputData compositionResult;

	/**
	 * Constructor.
	 */
	public OpadOutputCompositionFilterTest() {}

	/**
	 * Set up the testing environment.
	 * Here we create the filters required for testing and connect them with each other. Finally the controller is run.
	 * 
	 * @throws Exception
	 *             exception that are thrown during the setup.
	 */
	@Before
	public void setUp() throws Exception {
		this.controller = new AnalysisController();
		this.listReader = new ListReader<ExtendedStorableDetectionResult>(new Configuration(), this.controller);
		this.ooCompFilter = new OpadOutputCompositionFilter(new Configuration(), this.controller);
		this.listCollectionFilter = new ListCollectionFilter<OpadOutputData>(new Configuration(), this.controller);

		this.controller.connect(this.listReader, ListReader.OUTPUT_PORT_NAME, this.ooCompFilter, OpadOutputCompositionFilter.INPUT_PORT_NAME_DETECTION_RESULTS);
		this.controller.connect(this.ooCompFilter, OpadOutputCompositionFilter.OUTPUT_PORT_OPAD_DATA,
				this.listCollectionFilter, ListCollectionFilter.INPUT_PORT_NAME);

		this.detectionResult = new ExtendedStorableDetectionResult(ESDR_HOST_APP_OPERATION_NAME, ESDR_LATENCY, ESDR_TIMESTAMP, ESDR_FORECAST, ESDR_SCORE,
				ESDR_ANOMALY_THRESHOLD);

		this.composeOutput();
	}

	/**
	 * All instances are reset to null to have a fresh environment for every test.
	 * 
	 * @throws Exception
	 *             exceptions that are thrown during teardown.
	 */
	@After
	public void tearDown() throws Exception {
		this.controller.terminate();
		this.controller = null;
		this.listReader = null;
		this.ooCompFilter = null;
		this.listCollectionFilter = null;
		this.detectionResult = null;
		this.compositionResult = null;
	}

	private void composeOutput() throws IllegalStateException, AnalysisConfigurationException {
		this.listReader.addObject(this.detectionResult);
		this.controller.run();
		this.compositionResult = this.listCollectionFilter.getList().get(0);
	}

	/**
	 * Test whether the hostname is extracted correctly.
	 */
	@Test
	public void testOpadOutputCompositionHostname() {
		Assert.assertEquals(this.compositionResult.getOperationSignature().getHostName(), HOSTNAME);
	}

	/**
	 * Test whether the appname is extracted correctly.
	 */
	@Test
	public void testOpadOutputCompositionAppname() {
		Assert.assertEquals(this.compositionResult.getOperationSignature().getApplicationName(), APPNAME);
	}

	/**
	 * Test whether the operation string is extracted correctly.
	 */
	@Test
	public void testOpadOutputCompositionOperation() {
		Assert.assertEquals(this.compositionResult.getOperationSignature().getStringSignature(), OPERATION);
	}

	/**
	 * Test whether the latency is correctly transferred to the new object.
	 */
	@Test
	public void testOpadOutputCompositionLatency() {
		Assert.assertEquals(this.compositionResult.getResponseTime(), ESDR_LATENCY);
	}

	/**
	 * Test whether the timestamp is correctly transferred to the new object.
	 */
	@Test
	public void testOpadOutputCompositionTimestamp() {
		Assert.assertEquals(this.compositionResult.getTimestamp(), ESDR_TIMESTAMP);
	}

	// @Test
	// public void testOpadOutputCompositionForecast() {
	// assertEquals(this.compositionResult.getForecast(), ESDR_FORECAST);
	// }

	/**
	 * Test whether the anomaly score is correctly transferred to the new object.
	 */
	@Test
	public void testOpadOutputCompositionScore() {
		Assert.assertEquals(this.compositionResult.getAnomalyScore(), ESDR_SCORE, 0.0d);
	}

	/**
	 * Test whether the anomaly threshold is correctly transferred to the new object.
	 */
	@Test
	public void testOpadOutputCompositionAnomalyThreshold() {
		Assert.assertEquals(this.compositionResult.getAnomalyThreshold(), ESDR_ANOMALY_THRESHOLD, 0.0d);
	}
}
