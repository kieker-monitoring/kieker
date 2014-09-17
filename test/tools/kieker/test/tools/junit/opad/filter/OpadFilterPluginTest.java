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
import kieker.tools.opad.filter.OpadFilterPlugin;
import kieker.tools.opad.record.NamedDoubleRecord;
import kieker.tools.opad.record.StorableDetectionResult;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * This test creates some ResponseTimeDoubleRecords and let them run through all currently
 * available OPADFilter. The ResponseTimeDoubleRecords are create as records from different
 * applications. (Requires MongoDB Connection and also writes to the database because of the
 * SendAndStoreDetectionResultsFilter)
 *
 * @author Tom Frotscher
 * @since 1.10
 */
public class OpadFilterPluginTest extends AbstractKiekerTest {

	private static final String OP_SIGNATURE_A = "a.A.opA";
	private static final String OP_SIGNATURE_B = "b.B.opB";

	private AnalysisController controller;

	// Mockup sink
	private ListCollectionFilter<StorableDetectionResult> sinkPlugin;

	/**
	 * Creates a new instance of this class.
	 */
	public OpadFilterPluginTest() {
		// empty default constructor
	}

	private List<NamedDoubleRecord> createInputEventSetOER() {
		final List<NamedDoubleRecord> retList = new ArrayList<NamedDoubleRecord>();
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
		this.sinkPlugin = new ListCollectionFilter<StorableDetectionResult>(new Configuration(), this.controller);

		// Start - Read OperationExecutionRecords
		final Configuration readerOERConfiguration = new Configuration();
		readerOERConfiguration.setProperty(ListReader.CONFIG_PROPERTY_NAME_AWAIT_TERMINATION, Boolean.TRUE.toString());
		final ListReader<NamedDoubleRecord> theReaderNamedDoubleRecord = new ListReader<NamedDoubleRecord>(readerOERConfiguration, this.controller);
		theReaderNamedDoubleRecord.addAllObjects(this.createInputEventSetOER());
		// End - Read OperationExecutionRecords

		final OpadFilterPlugin opadFilterPlugin = new OpadFilterPlugin(new Configuration(), this.controller);

		// Reader -> Composite
		this.controller.connect(theReaderNamedDoubleRecord, ListReader.OUTPUT_PORT_NAME, opadFilterPlugin, OpadFilterPlugin.INPUT_PORT_NAME_VALUES);

		// Composite -> Sinks
		this.controller.connect(opadFilterPlugin, OpadFilterPlugin.OUTPUT_PORT_NAME_ANOMALY_SCORE, this.sinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);

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

		Assert.assertEquals(13221, this.sinkPlugin.getList().size());
	}
}
