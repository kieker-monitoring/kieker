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
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.opad.filter.ResponseTimeExtractionFilter;
import kieker.tools.opad.record.NamedDoubleTimeSeriesPoint;

/**
 * This Filter creates some OperationExecutionRecords and let them run trough the
 * ResponseTimeExtractionFilter. We assume that the Output corresponds to tin - tout, here:
 * 1002 - 1001 = 1.0
 * 22243 - 4000 = 18243.0
 * 5057 - 4021 = 1036.0
 * 
 * @author Tom Frotscher
 * 
 */
public class ResponseTimeExtractionFilterTest {

	private AnalysisController controller;
	private static final String OP_SIGNATURE_A = "a.A.opA";
	private static final String SESSION_ID_TEST = "TestId";
	private static final String HOST_ID_TEST = "TestRechner";
	private static final long TRACE_ID_TEST = (long) 0.1;

	// Variables Mockup OperationExecutionReader
	private ListReader<OperationExecutionRecord> theReaderOperationExecutionRecords;

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

	// Variables ResponsetimeExtractionFilter
	private ResponseTimeExtractionFilter responsetimeExtr;
	private ListCollectionFilter<NamedDoubleTimeSeriesPoint> sinkPlugin;

	@Before
	public void setUp() throws IllegalStateException,
			AnalysisConfigurationException {
		this.controller = new AnalysisController();

		// Start - Read OperationExecutionRecords
		final Configuration readerOERConfiguration = new Configuration();
		readerOERConfiguration.setProperty(ListReader.CONFIG_PROPERTY_NAME_AWAIT_TERMINATION, Boolean.TRUE.toString());
		this.theReaderOperationExecutionRecords = new ListReader<OperationExecutionRecord>(readerOERConfiguration);
		this.theReaderOperationExecutionRecords.addAllObjects(this.createInputEventSetOER());
		this.controller.registerReader(this.theReaderOperationExecutionRecords);
		// End - Read OperationExecutionRecords

		// Start - ResponseTimeExtractionFilter Configuration
		final Configuration ResponseTimeExtractionConfiguration = new Configuration();
		this.responsetimeExtr = new ResponseTimeExtractionFilter(ResponseTimeExtractionConfiguration);
		this.controller.registerFilter(this.responsetimeExtr);
		// End - ResponseTimeExtractionFilter

		// SINK Mock-up
		this.sinkPlugin = new ListCollectionFilter<NamedDoubleTimeSeriesPoint>(new Configuration());
		this.controller.registerFilter(this.sinkPlugin);

		// CONNECT the filters
		// Mock-up Reader (OperationExecutionRecords) -> ResponseTimeExtractionFIlter
		this.controller
				.connect(this.theReaderOperationExecutionRecords, ListReader.OUTPUT_PORT_NAME, this.responsetimeExtr,
						ResponseTimeExtractionFilter.INPUT_PORT_NAME_VALUE);
		// ResponseTimeExtractionFilter -> SinkPlugin Mock-up
		this.controller
				.connect(this.responsetimeExtr, ResponseTimeExtractionFilter.OUTPUT_PORT_NAME_VALUE, this.sinkPlugin,
						ListCollectionFilter.INPUT_PORT_NAME);

		Assert.assertTrue(this.sinkPlugin.getList().isEmpty());

	}

	// Test complete Flow
	@Test
	public void testOpadFlow() throws InterruptedException {

		final AnalysisControllerThread thread = new AnalysisControllerThread(this.controller);
		thread.start();

		Thread.sleep(2000);
		thread.terminate();

		Assert.assertEquals(3, this.sinkPlugin.getList().size());
		Assert.assertEquals(1.0, this.sinkPlugin.getList().get(0).getDoubleValue(), 0);
		Assert.assertEquals(18243.0, this.sinkPlugin.getList().get(1).getDoubleValue(), 0);
		Assert.assertEquals(1036.0, this.sinkPlugin.getList().get(2).getDoubleValue(), 0);

	}
}
