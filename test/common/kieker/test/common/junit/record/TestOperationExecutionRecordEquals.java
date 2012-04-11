/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.test.common.junit.record;

import junit.framework.Assert;

import org.junit.Test;

import kieker.common.record.controlflow.OperationExecutionRecord;

/**
 * @author Andre van Hoorn
 */
public class TestOperationExecutionRecordEquals { // NOCS (MissingCtorCheck)

	/**
	 * Tests the toArray(..) and initFromArray(..) methods of OperationExecutionRecord.
	 * 
	 * Assert that a record instance r1 equals an instance r2 created by serializing r1 to an array a1 and using a1 to init r2.
	 * This ignores a set loggingTimestamp!
	 */
	@Test
	public void testSerializeDeserializeEquals() {
		final OperationExecutionRecord r1 = new OperationExecutionRecord(
				"public void p1.p2.p3.componentname.operation(boolean, int)", // operationSignature
				"XXLJHDJHDHF", // sessionId
				882287444, // traceId
				5577376, // tin
				7544522, // tout
				"myVM", // hostname
				1, // eoi
				2 // ess
		);

		final Object[] r1Array = r1.toArray();

		final OperationExecutionRecord r2 = new OperationExecutionRecord(r1Array);

		Assert.assertEquals(r1, r2);
	}

	/**
	 * Tests the equals(..) method of OperationExecutionRecord.
	 * 
	 * Assert that two record instances with equal variables values are equal.
	 */
	@Test
	public void testEqualsEqualVariablesValues() {
		final OperationExecutionRecord r1 = new OperationExecutionRecord(
				"public void p1.p2.p3.componentname.operation(boolean, int)", // operationSignature
				"XXLJHDJHDHF", // sessionId
				882287444, // traceId
				5577376, // tin
				7544522, // tout
				"myVM", // hostname
				1, // eoi
				2 // ess
		);

		final OperationExecutionRecord r2 = new OperationExecutionRecord(
				"public void p1.p2.p3.componentname.operation(boolean, int)", // operationSignature
				"XXLJHDJHDHF", // sessionId
				882287444, // traceId
				5577376, // tin
				7544522, // tout
				"myVM", // hostname
				1, // eoi
				2 // ess
		);

		Assert.assertEquals(r1, r2);
	}

	/**
	 * Tests the equals(..) method of OperationExecutionRecord.
	 * 
	 * Assert that two record instances with null variables values are not equal.
	 */
	// TODO: in 1.6 check with another method!
	@SuppressWarnings("deprecation")
	@Test
	public void testEqualsNullVariableValues() {
		final OperationExecutionRecord r1 = new OperationExecutionRecord(
				"public void p1.p2.p3.componentname.operation(boolean, int)", // operationSignature
				"XXLJHDJHDHF", // sessionId
				882287444, // traceId
				5577376, // tin
				7544522, // tout
				"myVM", // hostname
				1, // eoi
				2 // ess
		);

		final OperationExecutionRecord r2 = new OperationExecutionRecord(
				"public void p1.p2.p3.componentname.operation(boolean, int)", // operationSignature
				"XXLJHDJHDHF", // sessionId
				882287444, // traceId
				5577376, // tin
				7544522, // tout
				"myVM", // hostname
				1, // eoi
				2 // ess
		);

		Assert.assertEquals(r1, r2);

		/* Modification in r1 */
		final String oldOperationSignature1 = r1.getOperationSignature();
		r1.setOperationSignature(null);
		Assert.assertFalse(r1.equals(r2));
		r1.setOperationSignature(oldOperationSignature1);
		Assert.assertEquals(r1, r2);

		/* Modification in r2 */
		final String oldOperationSignature2 = r2.getOperationSignature();
		r2.setOperationSignature(null);
		Assert.assertFalse(r1.equals(r2));
		r2.setOperationSignature(oldOperationSignature2);
		Assert.assertEquals(r1, r2);

		/* Modification in both */
		r1.setOperationSignature(null);
		r2.setOperationSignature(null);
		Assert.assertEquals(r1, r2);
		r1.setOperationSignature(oldOperationSignature1);
		r2.setOperationSignature(oldOperationSignature2);
		Assert.assertEquals(r1, r2);

		// TODO: test incomplete (hostname, sessionId, retVal)
	}

	/**
	 * Tests the equals(..) method of OperationExecutionRecord.
	 * 
	 * Assert that two record instances with differing variables values are not equal.
	 */
	// TODO: in 1.6 check with another method!
	@SuppressWarnings("deprecation")
	@Test
	public void testEqualsDifferentVariablesValues() {
		final OperationExecutionRecord r1 = new OperationExecutionRecord(
				"public void p1.p2.p3.componentname.operation(boolean, int)", // operationSignature
				"XXLJHDJHDHF", // sessionId
				882287444, // traceId
				5577376, // tin
				7544522, // tout
				"myVM", // hostname
				1, // eoi
				2 // ess
		);

		final OperationExecutionRecord r2 = new OperationExecutionRecord(
				"public void p1.p2.p3.componentname.operation(boolean, int)", // operationSignature
				"XXLJHDJHDHF", // sessionId
				882287444, // traceId
				5577376, // tin
				7544522, // tout
				"myVM", // hostname
				1, // eoi
				2 // ess
		);

		Assert.assertEquals(r1, r2);

		/* Modification of operationSignature */
		final String oldOperationSignature = r2.getOperationSignature();
		r2.setOperationSignature(r2.getOperationSignature() + "_");
		Assert.assertFalse(r1.equals(r2));
		r2.setOperationSignature(oldOperationSignature);
		Assert.assertEquals(r1, r2);

		/* Modification of eoi */
		final int oldEoi = r2.getEoi();
		r2.setEoi(r2.getEoi() + 1);
		Assert.assertFalse(r1.equals(r2));
		r2.setEoi(oldEoi);
		Assert.assertEquals(r1, r2);

		/* Modification of ess */
		final int oldEss = r2.getEss();
		r2.setEss(r2.getEss() + 1);
		Assert.assertFalse(r1.equals(r2));
		r2.setEss(oldEss);
		Assert.assertEquals(r1, r2);

		/* Modification of sessionId */
		final String oldsessionId = r2.getSessionId();
		r2.setSessionId(r2.getSessionId() + "_");
		Assert.assertFalse(r1.equals(r2));
		r2.setSessionId(oldsessionId);
		Assert.assertEquals(r1, r2);

		/* Modification of tin */
		final long oldTin = r2.getTin();
		r2.setTin(r2.getTin() + 1);
		Assert.assertFalse(r1.equals(r2));
		r2.setTin(oldTin);
		Assert.assertEquals(r1, r2);

		/* Modification of tout */
		final long oldTout = r2.getTout();
		r2.setTout(r2.getTout() + 1);
		Assert.assertFalse(r1.equals(r2));
		r2.setTout(oldTout);
		Assert.assertEquals(r1, r2);

		/* Modification of traceId */
		final long oldTraceId = r2.getTraceId();
		r2.setTraceId(r2.getTraceId() + 1);
		Assert.assertFalse(r1.equals(r2));
		r2.setTraceId(oldTraceId);
		Assert.assertEquals(r1, r2);

		/* Modification of hostname */
		final String oldVmName = r2.getHostname();
		r2.setHostname(r2.getHostname() + "_");
		Assert.assertFalse(r1.equals(r2));
		r2.setHostname(oldVmName);
		Assert.assertEquals(r1, r2);

		// TODO: test incomplete (entryPoint, retVal)
	}
}
