/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
import kieker.tools.opad.filter.AnomalyDetectionFilter;
import kieker.tools.opad.record.ExtendedStorableDetectionResult;
import kieker.tools.opad.record.StorableDetectionResult;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 *
 * Testing the AnomalyDetectionFilter. What is basically testing is following:
 * + Set Threshold of the Filter to 0.6
 * + Input 0.5, 0.6, 0.7
 * + Awaits
 * - 0.5 to be normal (1 in the sink)
 * - 0.6 and 0.7 to be an anomaly
 *
 * @author Tillmann Carlos Bielefeld
 * @since 1.10
 */
public class AnomalyDetectionFilterTest extends AbstractKiekerTest {

	private static final String OP_SIGNATURE_A = "a.A.opA";

	private ListCollectionFilter<StorableDetectionResult> sinkPluginIfAnomaly;
	private ListCollectionFilter<StorableDetectionResult> sinkPluginElse;
	private ListCollectionFilter<ExtendedStorableDetectionResult> sinkPluginAll;
	private AnalysisController controller;

	/**
	 * Creates a new instance of this class.
	 */
	public AnomalyDetectionFilterTest() {
		// empty default constructor
	}

	private StorableDetectionResult createNDTSP(final String signature, final double value) {
		return new StorableDetectionResult(signature, value, System.currentTimeMillis(), value, value);
	}

	private List<StorableDetectionResult> createInputEventSet() {
		final List<StorableDetectionResult> retList = new ArrayList<>();
		retList.add(this.createNDTSP(OP_SIGNATURE_A, 0.5));
		retList.add(this.createNDTSP(OP_SIGNATURE_A, 0.6));
		retList.add(this.createNDTSP(OP_SIGNATURE_A, 0.7));
		return retList;
	}

	/**
	 * Set up for the AnomalyDetectionFilterTest.
	 *
	 * @throws IllegalStateException
	 *             If illegal state
	 * @throws AnalysisConfigurationException
	 *             If wrong configuration
	 */
	@Before
	public void setUp() throws IllegalStateException, AnalysisConfigurationException {
		this.controller = new AnalysisController();

		// READER
		final Configuration readerConfiguration = new Configuration();
		readerConfiguration.setProperty(ListReader.CONFIG_PROPERTY_NAME_AWAIT_TERMINATION, Boolean.TRUE.toString());
		final ListReader<StorableDetectionResult> theReader = new ListReader<>(readerConfiguration, this.controller);
		theReader.addAllObjects(this.createInputEventSet());

		// ANOMALY DETECTION FILTER
		final Configuration configAnomaly = new Configuration();
		configAnomaly.setProperty(AnomalyDetectionFilter.CONFIG_PROPERTY_NAME_THRESHOLD, "0.6");
		final AnomalyDetectionFilter anomalyDetectionFilter = new AnomalyDetectionFilter(configAnomaly, this.controller);

		// SINK 1
		this.sinkPluginIfAnomaly = new ListCollectionFilter<>(new Configuration(), this.controller);

		// SINK 2
		this.sinkPluginElse = new ListCollectionFilter<>(new Configuration(), this.controller);

		// SINK 3
		this.sinkPluginAll = new ListCollectionFilter<>(new Configuration(), this.controller);

		// CONNECT the filters
		this.controller.connect(theReader, ListReader.OUTPUT_PORT_NAME,
				anomalyDetectionFilter, AnomalyDetectionFilter.INPUT_PORT_ANOMALY_SCORE);
		this.controller.connect(anomalyDetectionFilter, AnomalyDetectionFilter.OUTPUT_PORT_ANOMALY_SCORE_IF_ANOMALY,
				this.sinkPluginIfAnomaly, ListCollectionFilter.INPUT_PORT_NAME);
		this.controller.connect(anomalyDetectionFilter, AnomalyDetectionFilter.OUTPUT_PORT_ANOMALY_SCORE_ELSE,
				this.sinkPluginElse, ListCollectionFilter.INPUT_PORT_NAME);
		this.controller.connect(anomalyDetectionFilter, AnomalyDetectionFilter.OUTPUT_PORT_ALL,
				this.sinkPluginAll, ListCollectionFilter.INPUT_PORT_NAME);

		Assert.assertTrue(this.sinkPluginIfAnomaly.getList().isEmpty());
		Assert.assertTrue(this.sinkPluginElse.getList().isEmpty());
		Assert.assertTrue(this.sinkPluginAll.getList().isEmpty());
	}

	/**
	 * Test of the AnomalyDetectionFilter.
	 *
	 * @throws InterruptedException
	 *             If interrupted
	 * @throws IllegalStateException
	 *             If illegal state
	 * @throws AnalysisConfigurationException
	 *             If wrong configuration
	 */
	@Test
	public void testDetectionOnly() throws InterruptedException, IllegalStateException, AnalysisConfigurationException {
		final AnalysisControllerThread thread = new AnalysisControllerThread(this.controller);
		thread.start();

		Thread.sleep(1000);
		thread.terminate();

		Assert.assertEquals(2, this.sinkPluginIfAnomaly.getList().size());
		Assert.assertEquals(1, this.sinkPluginElse.getList().size());
		Assert.assertEquals(3, this.sinkPluginAll.getList().size());
	}

}
