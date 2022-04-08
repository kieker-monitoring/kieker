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

package kieker.test.tools.junit.opad.filter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.opad.filter.RecordConverter;
import kieker.tools.opad.record.NamedDoubleRecord;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * Tests the RecordConverter.
 * 
 * @author Thomas Duellmann
 * @since 1.10
 * 
 */
public final class RecordConverterTest extends AbstractKiekerTest {

	/**
	 * Timestamp when data was logged.
	 */
	private static long loggingTimestamp;

	/**
	 * Signature of the operation.
	 */
	private static String operationSignature;

	/**
	 * Timestamp when method was started.
	 */
	private static long timeIn;

	/**
	 * Timestamp when method ended.
	 */
	private static long timeOut;

	/**
	 * Hostname of the instrumented system.
	 */
	private static String hostname;

	/**
	 * Name of the instrumentation.
	 */
	private static String appname;

	/**
	 * Combination of host and app name (hostname+appname).
	 */
	private static String hostAppInput;

	private IAnalysisController analysisController;
	private ListReader<OperationExecutionRecord> simpleListReader;
	private ListCollectionFilter<NamedDoubleRecord> listCollectionfilter;
	private NamedDoubleRecord conversionResult;

	/**
	 * Constructor.
	 */
	public RecordConverterTest() {
		// Default constructor
	}

	/**
	 * Set the values that are used in the test object that will be converted.
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		RecordConverterTest.loggingTimestamp = 1336L;
		RecordConverterTest.operationSignature = "public void foo.bar.method(String)";
		RecordConverterTest.timeIn = 1337L;
		RecordConverterTest.timeOut = 2000L;
		RecordConverterTest.hostname = "host";
		RecordConverterTest.appname = "app";
		RecordConverterTest.hostAppInput = RecordConverterTest.hostname + "+" + RecordConverterTest.appname;

	}

	/**
	 * Sets up a simple test filter structure (SimpleListReader -> RecordConverter -> ListCollectionFilter)
	 * and creates an OperationExecutionRecord that will be converterd for testing reasons.
	 * 
	 * @throws Exception
	 *             throws exceptions that are thrown while setting up the filter structure
	 */
	@Before
	public void setUp() throws Exception {
		final OperationExecutionRecord oer;
		final RecordConverter recordConverter;

		this.analysisController = new AnalysisController();
		recordConverter = new RecordConverter(new Configuration(), this.analysisController);
		this.simpleListReader = new ListReader<OperationExecutionRecord>(new Configuration(), this.analysisController);
		this.listCollectionfilter = new ListCollectionFilter<NamedDoubleRecord>(new Configuration(), this.analysisController);

		this.analysisController.connect(this.simpleListReader, ListReader.OUTPUT_PORT_NAME, recordConverter, RecordConverter.INPUT_PORT_NAME_OER);
		this.analysisController.connect(recordConverter, RecordConverter.OUTPUT_PORT_NAME_NDR, this.listCollectionfilter, ListCollectionFilter.INPUT_PORT_NAME);

		oer = new OperationExecutionRecord(RecordConverterTest.operationSignature,
				OperationExecutionRecord.NO_SESSION_ID,
				OperationExecutionRecord.NO_TRACE_ID,
				RecordConverterTest.timeIn,
				RecordConverterTest.timeOut,
				RecordConverterTest.hostAppInput,
				OperationExecutionRecord.NO_EOI_ESS,
				OperationExecutionRecord.NO_EOI_ESS);

		oer.setLoggingTimestamp(RecordConverterTest.loggingTimestamp);
		this.convertRecord(oer);
	}

	/**
	 * Converts the OperationExecutionRecord using the previously set up filter structure.
	 * 
	 * @param oer
	 * @throws IllegalStateException
	 *             exception that can possibly be thrown by AnalysisController
	 * @throws AnalysisConfigurationException
	 *             exception that can possibly be thrown by AnalysisController
	 */
	private void convertRecord(final OperationExecutionRecord oer) throws IllegalStateException, AnalysisConfigurationException {
		this.simpleListReader.addObject(oer);
		this.analysisController.run();
		this.conversionResult = this.listCollectionfilter.getList().get(0);
	}

	/**
	 * Tests the timestamp of the previously converted OperationExecutionRecord.
	 */
	@Test
	public void testConversionTimestamp() {
		Assert.assertEquals("Check whether timestamp is still intact", RecordConverterTest.loggingTimestamp, this.conversionResult.getTimestamp());
	}

	/**
	 * Tests the latency of the previously converted OperationExecutionRecord.
	 */
	@Test
	public void testConversionLatency() {
		Assert.assertEquals("Check whether latency has been computed correctly",
				RecordConverterTest.timeOut - RecordConverterTest.timeIn, this.conversionResult.getResponseTime(), 0d);
	}

	/**
	 * Tests the identifiers of the previously converted OperationExecutionRecord.
	 */
	@Test
	public void testConversionIdentifier() {
		Assert.assertEquals("Check whether host and appname have been extracted correctly",
				RecordConverterTest.hostAppInput + ":" + RecordConverterTest.operationSignature, this.conversionResult.getApplicationName());
	}

}
