/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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
import kieker.tools.opad.filter.ExtractionFilter;
import kieker.tools.opad.model.NamedDoubleTimeSeriesPoint;
import kieker.tools.opad.record.NamedDoubleRecord;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * This Filter creates some NamedDoubleRecords and let them run trough the
 * ExtractionFilter. We assume that the output corresponds to the time unit
 * converted input timestamp from the incoming record.
 * 
 * @author Tom Frotscher
 * @since 1.10
 * 
 */
public class ExtractionFilterTest extends AbstractKiekerTest {

	private static final String OP_SIGNATURE_A = "a.A.opA";
	private static final String OP_SIGNATURE_B = "b.B.opB";
	private AnalysisController controller;

	private ListCollectionFilter<NamedDoubleTimeSeriesPoint> sinkPlugin;

	/**
	 * Creates an instance of this class.
	 */
	public ExtractionFilterTest() {
		// empty default constructor
	}

	/**
	 * Creates inputs with timestamps.
	 */
	private List<NamedDoubleRecord> createInputEventSetOER() {
		final List<NamedDoubleRecord> retList = new ArrayList<NamedDoubleRecord>();
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_A, 1369127812664L, 10341.94));
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_B, 1369128812669L, 8341.00));
		retList.add(new NamedDoubleRecord(OP_SIGNATURE_A, 1369129812674L, 78.26));
		return retList;
	}

	/**
	 * Sets up the test for the ExtractionFilter.
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
		final ListReader<NamedDoubleRecord> theReaderRecords = new ListReader<NamedDoubleRecord>(readerOERConfiguration, this.controller);
		theReaderRecords.addAllObjects(this.createInputEventSetOER());
		// End - Read OperationExecutionRecords

		// Start - ExtractionFilter Configuration
		final Configuration extractionConfiguration = new Configuration();
		extractionConfiguration.setProperty(ExtractionFilter.CONFIG_PROPERTY_NAME_TIMEUNIT, "MILLISECONDS");
		final ExtractionFilter extraction = new ExtractionFilter(extractionConfiguration, this.controller);
		// End - ResponseTimeExtractionFilter

		// SINK Mock-up
		this.sinkPlugin = new ListCollectionFilter<NamedDoubleTimeSeriesPoint>(new Configuration(), this.controller);

		// CONNECT the filters
		// Mock-up Reader (OperationExecutionRecords) -> ResponseTimeExtractionFIlter
		this.controller
				.connect(theReaderRecords, ListReader.OUTPUT_PORT_NAME, extraction,
						ExtractionFilter.INPUT_PORT_NAME_VALUE);
		// ResponseTimeExtractionFilter -> SinkPlugin Mock-up
		this.controller
				.connect(extraction, ExtractionFilter.OUTPUT_PORT_NAME_VALUE, this.sinkPlugin,
						ListCollectionFilter.INPUT_PORT_NAME);

		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());

	}

	/**
	 * Test the extraction of the data from ResponseTimeDoubleRecords.
	 * 
	 * @throws InterruptedException
	 *             If interrupted
	 */
	@Test
	public void testResponsetimeOnly() throws InterruptedException {

		final AnalysisControllerThread thread = new AnalysisControllerThread(this.controller);
		thread.start();

		Thread.sleep(2000);
		thread.terminate();

		Assert.assertEquals(3, this.sinkPlugin.getList().size());

		// Test on the timestamp conversion (here as configured to milliseconds
		Assert.assertEquals(1369127, this.sinkPlugin.getList().get(0).getTime(), 0);
		Assert.assertEquals(1369128, this.sinkPlugin.getList().get(1).getTime(), 0);
		Assert.assertEquals(1369129, this.sinkPlugin.getList().get(2).getTime(), 0);

		// Test on the extracted values
		Assert.assertEquals(10341.94, this.sinkPlugin.getList().get(0).getDoubleValue(), 0);
		Assert.assertEquals(8341.00, this.sinkPlugin.getList().get(1).getDoubleValue(), 0);
		Assert.assertEquals(78.26, this.sinkPlugin.getList().get(2).getDoubleValue(), 0);

	}
}
