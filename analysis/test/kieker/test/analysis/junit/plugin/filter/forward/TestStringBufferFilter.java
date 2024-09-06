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

package kieker.test.analysis.junit.plugin.filter.forward;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.filter.forward.StringBufferFilter;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.misc.EmptyRecord;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.6
 *
 * @deprecated since 1.15 remove in 1.16 as it test the old deprecated filter
 */
@Deprecated
public class TestStringBufferFilter extends AbstractKiekerTest {

	/**
	 * Default constructor.
	 */
	public TestStringBufferFilter() {
		// default empty constructor
	}

	@Ignore // NOCS
	@Test
	public void testRecordsWithStringEqualButNeverSame() throws IllegalStateException, AnalysisConfigurationException {
		final IAnalysisController analysisController = new AnalysisController();
		final ListReader<IMonitoringRecord> reader = new ListReader<>(new Configuration(), analysisController);
		final StringBufferFilter stringBufferFilter = new StringBufferFilter(new Configuration(), analysisController);
		final ListCollectionFilter<IMonitoringRecord> collectionFilter = new ListCollectionFilter<>(new Configuration(), analysisController);

		analysisController.connect(reader, ListReader.OUTPUT_PORT_NAME, stringBufferFilter, StringBufferFilter.INPUT_PORT_NAME_EVENTS);
		analysisController.connect(stringBufferFilter, StringBufferFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, collectionFilter, ListCollectionFilter.INPUT_PORT_NAME);

		long timestamp = 3268936L;
		final IMonitoringRecord recordIn1 = TestStringBufferFilter.createOperationExecutionRecord(timestamp);
		timestamp++;
		final IMonitoringRecord recordIn2 = TestStringBufferFilter.createOperationExecutionRecord(timestamp);
		reader.addObject(recordIn1);
		reader.addObject(recordIn2);

		analysisController.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, analysisController.getState());

		final List<IMonitoringRecord> records = collectionFilter.getList();
		Assert.assertEquals("Unexpected number of records", 2, records.size());
		final IMonitoringRecord recordOut1 = records.get(0);
		final IMonitoringRecord recordOut2 = records.get(1);
		Assert.assertNotSame("First output record same as first input record", recordIn1, recordOut1); // includes String, hence NOT "as-is"
		Assert.assertEquals("First output record doesn't equal first input record", recordIn1, recordOut1); // ... but must be equal
		Assert.assertNotSame("Second output record same as second input record", recordIn2, recordOut2); // includes String, hence NOT "as-is"
		Assert.assertEquals("Second output record doesn't equal second input record", recordIn2, recordOut2); // ... but must be equal
	}

	@Ignore // NOCS
	@Test
	public void testRecordsWithoutStringSame() throws IllegalStateException, AnalysisConfigurationException {
		final AnalysisController analysisController = new AnalysisController();
		final ListReader<IMonitoringRecord> reader = new ListReader<>(new Configuration(), analysisController);
		final StringBufferFilter stringBufferFilter = new StringBufferFilter(new Configuration(), analysisController);
		final ListCollectionFilter<IMonitoringRecord> collectionFilter = new ListCollectionFilter<>(new Configuration(), analysisController);

		analysisController.connect(reader, ListReader.OUTPUT_PORT_NAME, stringBufferFilter, StringBufferFilter.INPUT_PORT_NAME_EVENTS);
		analysisController.connect(stringBufferFilter, StringBufferFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, collectionFilter, ListCollectionFilter.INPUT_PORT_NAME);

		final IMonitoringRecord recordIn1 = new EmptyRecord();
		reader.addObject(recordIn1);

		analysisController.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, analysisController.getState());

		final List<IMonitoringRecord> records = collectionFilter.getList();
		Assert.assertEquals("Unexpected number of records", 1, records.size());
		final IMonitoringRecord recordOut1 = records.get(0);
		Assert.assertSame("First output record not same as first input record", recordIn1, recordOut1); // includes no String, hence "as-is"
	}

	@Ignore // NOCS
	@Test
	public void testObjects() throws IllegalStateException, AnalysisConfigurationException {
		final AnalysisController analysisController = new AnalysisController();
		final ListReader<Object> reader = new ListReader<>(new Configuration(), analysisController);
		final StringBufferFilter stringBufferFilter = new StringBufferFilter(new Configuration(), analysisController);
		final ListCollectionFilter<Object> collectionFilter = new ListCollectionFilter<>(new Configuration(), analysisController);

		analysisController.connect(reader, ListReader.OUTPUT_PORT_NAME, stringBufferFilter, StringBufferFilter.INPUT_PORT_NAME_EVENTS);
		analysisController.connect(stringBufferFilter, StringBufferFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, collectionFilter, ListCollectionFilter.INPUT_PORT_NAME);

		final Object objectIn = new Object();
		final String stringIn1 = "In-String";
		final String stringIn2 = new String(stringIn1); // NOPMD (new String)
		Assert.assertNotSame(stringIn1, stringIn2);
		Assert.assertEquals(stringIn1, stringIn2);
		reader.addObject(objectIn);
		reader.addObject(stringIn1);
		reader.addObject(stringIn2);

		analysisController.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, analysisController.getState());

		final List<Object> records = collectionFilter.getList();
		Assert.assertEquals("Unexpected number of records", 3, records.size());
		final Object objectOut = records.get(0);
		Assert.assertSame("Input object not same as output object", objectIn, objectOut);
		final String stringOut1 = (String) records.get(1);
		final String stringOut2 = (String) records.get(2);
		Assert.assertSame("Input string 1 not same as output string 1", stringIn1, stringOut1); // first occurrence of a string should remain same
		Assert.assertEquals("Input string not equal to output string", stringIn1, stringOut1);
		Assert.assertNotSame("Input string 2 same as output string 2", stringIn2, stringOut2); // next occurrence should be cached
		Assert.assertSame("Input string 1 not same as output string 2", stringIn1, stringOut2);
		Assert.assertEquals("Input string not equal to output string", stringIn2, stringOut2);
	}

	@Ignore // NOCS
	@Test
	public void testStringInRecord() throws IllegalStateException, AnalysisConfigurationException {
		final AnalysisController analysisController = new AnalysisController();
		final ListReader<OperationExecutionRecord> reader = new ListReader<>(new Configuration(), analysisController);
		final StringBufferFilter stringBufferFilter = new StringBufferFilter(new Configuration(), analysisController);
		final ListCollectionFilter<OperationExecutionRecord> collectionFilter = new ListCollectionFilter<>(new Configuration(),
				analysisController);

		analysisController.connect(reader, ListReader.OUTPUT_PORT_NAME, stringBufferFilter, StringBufferFilter.INPUT_PORT_NAME_EVENTS);
		analysisController.connect(stringBufferFilter, StringBufferFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, collectionFilter, ListCollectionFilter.INPUT_PORT_NAME);

		final String inString1a = "inString1a";
		final String inString1b = "inString1b";
		final String inString1c = "inString1c";
		final String inString2a = new String(inString1a); // NOPMD (new String)
		final String inString2b = new String(inString1b); // NOPMD (new String)
		final String inString2c = new String(inString1c); // NOPMD (new String)
		Assert.assertNotSame(inString1a, inString2a);
		Assert.assertEquals(inString1a, inString2a);
		Assert.assertNotSame(inString1b, inString2b);
		Assert.assertEquals(inString1b, inString2b);
		Assert.assertNotSame(inString1c, inString2c);
		Assert.assertEquals(inString1c, inString2c);
		final OperationExecutionRecord recordIn1 = new OperationExecutionRecord(inString1a, inString1b, 753L, 258L, 456L, inString1c, 159, 9877);
		final OperationExecutionRecord recordIn2 = new OperationExecutionRecord(inString2a, inString2b, 753L, 258L, 456L, inString2c, 159, 9877);
		reader.addObject(recordIn1);
		reader.addObject(recordIn2);

		analysisController.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, analysisController.getState());

		final List<OperationExecutionRecord> records = collectionFilter.getList();
		Assert.assertEquals("Unexpected number of records", 2, records.size());
		final OperationExecutionRecord recordOut1 = records.get(0);
		final OperationExecutionRecord recordOut2 = records.get(1);
		Assert.assertNotSame("First output record same as first input record", recordIn1, recordOut1); // includes String, hence NOT "as-is"
		Assert.assertEquals("First output record doesn't equal first input record", recordIn1, recordOut1); // ... but must be equal
		Assert.assertNotSame("First output record same as first input record", recordIn2, recordOut2); // includes String, hence NOT "as-is"
		Assert.assertEquals("First output record doesn't equal first input record", recordIn2, recordOut2); // ... but must be equal
		// test Strings in records
		Assert.assertSame(inString1a, recordOut1.getOperationSignature());
		Assert.assertSame(inString1b, recordOut1.getSessionId());
		Assert.assertSame(inString1c, recordOut1.getHostname());
		Assert.assertSame(inString1a, recordOut2.getOperationSignature());
		Assert.assertSame(inString1b, recordOut2.getSessionId());
		Assert.assertSame(inString1c, recordOut2.getHostname());
		Assert.assertNotSame(inString2a, recordOut2.getOperationSignature());
		Assert.assertNotSame(inString2b, recordOut2.getSessionId());
		Assert.assertNotSame(inString2c, recordOut2.getHostname());
	}

	private static OperationExecutionRecord createOperationExecutionRecord(final long loggingTimestamp) {
		final OperationExecutionRecord opRec = new OperationExecutionRecord("mySignatureStringWhichShouldBeCached", OperationExecutionRecord.NO_SESSION_ID,
				OperationExecutionRecord.NO_TRACE_ID, OperationExecutionRecord.NO_TIMESTAMP, OperationExecutionRecord.NO_TIMESTAMP,
				OperationExecutionRecord.NO_HOSTNAME, OperationExecutionRecord.NO_EOI_ESS, OperationExecutionRecord.NO_EOI_ESS);
		opRec.setLoggingTimestamp(loggingTimestamp);
		return opRec;
	}
}
