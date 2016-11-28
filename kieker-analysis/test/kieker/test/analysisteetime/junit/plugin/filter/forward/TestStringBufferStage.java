/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.analysisteetime.junit.plugin.filter.forward;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysisteetime.plugin.filter.forward.StringBufferFilter;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.misc.EmptyRecord;

import teetime.framework.test.StageTester;

/**
 * @author Andre van Hoorn, Jan Waller, Lars Bluemke
 *
 * @since 1.6
 */
public class TestStringBufferStage {

	/**
	 * Default constructor.
	 */
	public TestStringBufferStage() {
		// default empty constructor
	}

	@Test
	public void testRecordsWithStringEqualButNeverSame() {
		final StringBufferFilter stringBufferFilter = new StringBufferFilter();
		final List<Object> inputElements = new ArrayList<Object>();
		final List<Object> outputElements = new ArrayList<Object>();

		long timestamp = 3268936L;
		final IMonitoringRecord recordIn1 = TestStringBufferStage.createOperationExecutionRecord(timestamp);
		timestamp++;
		final IMonitoringRecord recordIn2 = TestStringBufferStage.createOperationExecutionRecord(timestamp);
		inputElements.add(recordIn1);
		inputElements.add(recordIn2);

		StageTester.test(stringBufferFilter)
				.and().send(inputElements).to(stringBufferFilter.getInputPort())
				.and().receive(outputElements).from(stringBufferFilter.getOutputPort())
				.start();

		Assert.assertEquals("Unexpected number of records", 2, outputElements.size());
		final IMonitoringRecord recordOut1 = (IMonitoringRecord) outputElements.get(0);
		final IMonitoringRecord recordOut2 = (IMonitoringRecord) outputElements.get(1);
		Assert.assertNotSame("First output record same as first input record", recordIn1, recordOut1); // includes String, hence NOT "as-is"
		Assert.assertEquals("First output record doesn't equal first input record", recordIn1, recordOut1); // ... but must be equal
		Assert.assertNotSame("Second output record same as second input record", recordIn2, recordOut2); // includes String, hence NOT "as-is"
		Assert.assertEquals("Second output record doesn't equal second input record", recordIn2, recordOut2); // ... but must be equal
	}

	@Test
	public void testRecordsWithoutStringSame() {
		final StringBufferFilter stringBufferFilter = new StringBufferFilter();
		final List<Object> inputElements = new ArrayList<Object>();
		final List<Object> outputElements = new ArrayList<Object>();

		final IMonitoringRecord recordIn1 = new EmptyRecord();
		inputElements.add(recordIn1);

		StageTester.test(stringBufferFilter)
				.and().send(inputElements).to(stringBufferFilter.getInputPort())
				.and().receive(outputElements).from(stringBufferFilter.getOutputPort())
				.start();

		Assert.assertEquals("Unexpected number of records", 1, outputElements.size());
		final IMonitoringRecord recordOut1 = (IMonitoringRecord) outputElements.get(0);
		Assert.assertSame("First output record not same as first input record", recordIn1, recordOut1); // includes no String, hence "as-is"
	}

	@Test
	public void testObjects() {
		final StringBufferFilter stringBufferFilter = new StringBufferFilter();
		final List<Object> inputElements = new ArrayList<Object>();
		final List<Object> outputElements = new ArrayList<Object>();

		final Object objectIn = new Object();
		final String stringIn1 = "In-String";
		final String stringIn2 = new String(stringIn1); // NOPMD (new String)
		Assert.assertNotSame(stringIn1, stringIn2);
		Assert.assertEquals(stringIn1, stringIn2);
		inputElements.add(objectIn);
		inputElements.add(stringIn1);
		inputElements.add(stringIn2);

		StageTester.test(stringBufferFilter)
				.and().send(inputElements).to(stringBufferFilter.getInputPort())
				.and().receive(outputElements).from(stringBufferFilter.getOutputPort())
				.start();

		Assert.assertEquals("Unexpected number of records", 3, outputElements.size());
		final Object objectOut = outputElements.get(0);
		Assert.assertSame("Input object not same as output object", objectIn, objectOut);
		final String stringOut1 = (String) outputElements.get(1);
		final String stringOut2 = (String) outputElements.get(2);
		Assert.assertSame("Input string 1 not same as output string 1", stringIn1, stringOut1); // first occurrence of a string should remain same
		Assert.assertEquals("Input string not equal to output string", stringIn1, stringOut1);
		Assert.assertNotSame("Input string 2 same as output string 2", stringIn2, stringOut2); // next occurrence should be cached
		Assert.assertSame("Input string 1 not same as output string 2", stringIn1, stringOut2);
		Assert.assertEquals("Input string not equal to output string", stringIn2, stringOut2);
	}

	@Test
	public void testStringInRecord() {
		final StringBufferFilter stringBufferFilter = new StringBufferFilter();
		final List<Object> inputElements = new ArrayList<Object>();
		final List<Object> outputElements = new ArrayList<Object>();

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
		inputElements.add(recordIn1);
		inputElements.add(recordIn2);

		StageTester.test(stringBufferFilter)
				.and().send(inputElements).to(stringBufferFilter.getInputPort())
				.and().receive(outputElements).from(stringBufferFilter.getOutputPort())
				.start();

		Assert.assertEquals("Unexpected number of records", 2, outputElements.size());
		final OperationExecutionRecord recordOut1 = (OperationExecutionRecord) outputElements.get(0);
		final OperationExecutionRecord recordOut2 = (OperationExecutionRecord) outputElements.get(1);
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
