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

package kieker.test.common.junit.record.controlflow;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.controlflow.OperationExecutionRecord;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.2
 */
public class TestOperationExecutionRecordEquals extends AbstractKiekerTest { // NOCS (MissingCtorCheck)

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
	 * Assert that two record instances with differing variables values are not equal.
	 */
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
				3 // ess
		);

		Assert.assertNotEquals(r1, r2);
	}
}
