/***************************************************************************
 * Copyright 2011 by
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

package kieker.test.common.junit;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.common.record.OperationExecutionRecord;

/**
 * @author Andre van Hoorn
 */
public class TestOperationExecutionRecord extends TestCase { // NOCS (MissingCtorCheck)

	/**
	 * Tests the toArray(..) and initFromArray(..) methods of
	 * OperationExecutionRecord.
	 * 
	 * Assert that a record instance r1 equals an
	 * instance r2 created by serializing r1 to an
	 * array a1 and using a1 to init r2.
	 * 
	 */
	public void testSerializeDeserializeEquals() {
		final OperationExecutionRecord r1 = new OperationExecutionRecord();
		r1.className = "p1.p2.p3.componentname";
		r1.eoi = 1; // NOCS (MagicNumberCheck)
		r1.ess = 2; // NOCS (MagicNumberCheck)
		r1.experimentId = 55; // NOCS (MagicNumberCheck)
		r1.isEntryPoint = true;
		r1.operationName = "operation(boolean arg1, int arg2)";
		r1.retVal = new Object();
		r1.sessionId = "XXLJHDJHDHF";
		r1.tin = 5577376; // NOCS (MagicNumberCheck)
		r1.tout = 7544522; // NOCS (MagicNumberCheck)
		r1.traceId = 882287444; // NOCS (MagicNumberCheck)
		r1.hostName = "myVM";

		final Object[] r1Array = r1.toArray();

		final OperationExecutionRecord r2 = new OperationExecutionRecord();
		r2.initFromArray(r1Array);

		Assert.assertEquals(r1, r2);
	}

	/**
	 * Tests the equals(..) method of OperationExecutionRecord.
	 * 
	 * Assert that two record instances with equal variables values
	 * are equal.
	 */
	public void testEqualsEqualVariablesValues() {
		final OperationExecutionRecord r1 = new OperationExecutionRecord();
		r1.className = "p1.p2.p3.componentname";
		r1.eoi = 1; // NOCS (MagicNumberCheck)
		r1.ess = 2; // NOCS (MagicNumberCheck)
		r1.experimentId = 55; // NOCS (MagicNumberCheck)
		r1.isEntryPoint = true;
		r1.operationName = "operation(boolean arg1, int arg2)";
		r1.retVal = new Object();
		r1.sessionId = "XXLJHDJHDHF";
		r1.tin = 5577376; // NOCS (MagicNumberCheck)
		r1.tout = 7544522; // NOCS (MagicNumberCheck)
		r1.traceId = 882287444; // NOCS (MagicNumberCheck)
		r1.hostName = "myVM";

		final OperationExecutionRecord r2 = new OperationExecutionRecord();
		r2.className = "p1.p2.p3.componentname";
		r2.eoi = 1; // NOCS (MagicNumberCheck)
		r2.ess = 2; // NOCS (MagicNumberCheck)
		r2.experimentId = 55; // NOCS (MagicNumberCheck)
		r2.isEntryPoint = true;
		r2.operationName = "operation(boolean arg1, int arg2)";
		r2.retVal = new Object();
		r2.sessionId = "XXLJHDJHDHF";
		r2.tin = 5577376; // NOCS (MagicNumberCheck)
		r2.tout = 7544522; // NOCS (MagicNumberCheck)
		r2.traceId = 882287444; // NOCS (MagicNumberCheck)
		r2.hostName = "myVM";

		Assert.assertEquals(r1, r2);
	}

	/**
	 * Tests the equals(..) method of OperationExecutionRecord.
	 * 
	 * Assert that two record instances with null variables values
	 * are not equal.
	 */
	public void testEqualsNullVariableValues() {
		final OperationExecutionRecord r1 = new OperationExecutionRecord();
		r1.className = "p1.p2.p3.componentname";
		r1.eoi = 1; // NOCS (MagicNumberCheck)
		r1.ess = 2; // NOCS (MagicNumberCheck)
		r1.experimentId = 55; // NOCS (MagicNumberCheck)
		r1.isEntryPoint = true;
		r1.operationName = "operation(boolean arg1, int arg2)";
		r1.retVal = new Object();
		r1.sessionId = "XXLJHDJHDHF";
		r1.tin = 5577376; // NOCS (MagicNumberCheck)
		r1.tout = 7544522; // NOCS (MagicNumberCheck)
		r1.traceId = 882287444; // NOCS (MagicNumberCheck)
		r1.hostName = "myVM";

		final OperationExecutionRecord r2 = new OperationExecutionRecord();
		r2.className = "p1.p2.p3.componentname";
		r2.eoi = 1; // NOCS (MagicNumberCheck)
		r2.ess = 2; // NOCS (MagicNumberCheck)
		r2.experimentId = 55; // NOCS (MagicNumberCheck)
		r2.isEntryPoint = true;
		r2.operationName = "operation(boolean arg1, int arg2)";
		r2.retVal = new Object();
		r2.sessionId = "XXLJHDJHDHF";
		r2.tin = 5577376; // NOCS (MagicNumberCheck)
		r2.tout = 7544522; // NOCS (MagicNumberCheck)
		r2.traceId = 882287444; // NOCS (MagicNumberCheck)
		r2.hostName = "myVM";

		Assert.assertEquals(r1, r2);

		/* Modification of className */
		final String oldComponentName1 = r1.className;
		final String oldComponentName2 = r2.className;
		r1.className = null;
		r2.className = null;
		Assert.assertFalse(r1.equals(r2));
		r1.className = oldComponentName1;
		r2.className = oldComponentName2;
		Assert.assertEquals(r1, r2);

		/* Modification of operationName */
		final String oldOpname1 = r1.operationName;
		final String oldOpname2 = r2.operationName;
		r1.operationName = null;
		r2.operationName = null;
		Assert.assertFalse(r1.equals(r2));
		r1.operationName = oldOpname1;
		r2.operationName = oldOpname2;
		Assert.assertEquals(r1, r2);

		/* Modification of sessionId */
		final String oldSessionId1 = r1.sessionId;
		final String oldSessionId2 = r2.sessionId;
		r1.sessionId = null;
		r2.sessionId = null;
		Assert.assertFalse(r1.equals(r2));
		r1.sessionId = oldSessionId1;
		r2.sessionId = oldSessionId2;
		Assert.assertEquals(r1, r2);

		/* Modification of hostName */
		final String oldVmName1 = r1.hostName;
		final String oldVmName2 = r2.hostName;
		r1.hostName = null;
		r2.hostName = null;
		Assert.assertFalse(r1.equals(r2));
		r1.hostName = oldVmName1;
		r2.hostName = oldVmName2;
		Assert.assertEquals(r1, r2);
	}

	/**
	 * Tests the equals(..) method of OperationExecutionRecord.
	 * 
	 * Assert that two record instances with differing variables values
	 * are not equal.
	 */
	public void testEqualsDifferentVariablesValues() {
		final OperationExecutionRecord r1 = new OperationExecutionRecord();
		r1.className = "p1.p2.p3.componentname";
		r1.eoi = 1; // NOCS (MagicNumberCheck)
		r1.ess = 2; // NOCS (MagicNumberCheck)
		r1.experimentId = 55; // NOCS (MagicNumberCheck)
		r1.isEntryPoint = true;
		r1.operationName = "operation(boolean arg1, int arg2)";
		r1.retVal = new Object();
		r1.sessionId = "XXLJHDJHDHF";
		r1.tin = 5577376; // NOCS (MagicNumberCheck)
		r1.tout = 7544522; // NOCS (MagicNumberCheck)
		r1.traceId = 882287444; // NOCS (MagicNumberCheck)
		r1.hostName = "myVM";

		final OperationExecutionRecord r2 = new OperationExecutionRecord();
		r2.className = "p1.p2.p3.componentname";
		r2.eoi = 1; // NOCS (MagicNumberCheck)
		r2.ess = 2; // NOCS (MagicNumberCheck)
		r2.experimentId = 55; // NOCS (MagicNumberCheck)
		r2.isEntryPoint = true;
		r2.operationName = "operation(boolean arg1, int arg2)";
		r2.retVal = new Object();
		r2.sessionId = "XXLJHDJHDHF";
		r2.tin = 5577376; // NOCS (MagicNumberCheck)
		r2.tout = 7544522; // NOCS (MagicNumberCheck)
		r2.traceId = 882287444; // NOCS (MagicNumberCheck)
		r2.hostName = "myVM";

		Assert.assertEquals(r1, r2);

		/* Modification of className */
		final String oldComponentName = r2.className;
		r2.className = r2.className + "_";
		Assert.assertFalse(r1.equals(r2));
		r2.className = oldComponentName;
		Assert.assertEquals(r1, r2);

		/* Modification of eoi */
		final int oldEoi = r2.eoi;
		r2.eoi++;
		Assert.assertFalse(r1.equals(r2));
		r2.eoi = oldEoi;
		Assert.assertEquals(r1, r2);

		/* Modification of ess */
		final int oldEss = r2.ess;
		r2.ess++;
		Assert.assertFalse(r1.equals(r2));
		r2.ess = oldEss;
		Assert.assertEquals(r1, r2);

		/* Modification of operationName */
		final String oldOpname = r2.operationName;
		r2.operationName = r2.operationName + "_";
		Assert.assertFalse(r1.equals(r2));
		r2.operationName = oldOpname;
		Assert.assertEquals(r1, r2);

		/* Modification of sessionId */
		final String oldsessionId = r2.sessionId;
		r2.sessionId = r2.sessionId + "_";
		Assert.assertFalse(r1.equals(r2));
		r2.sessionId = oldsessionId;
		Assert.assertEquals(r1, r2);

		/* Modification of tin */
		final long oldTin = r2.tin;
		r2.tin++;
		Assert.assertFalse(r1.equals(r2));
		r2.tin = oldTin;
		Assert.assertEquals(r1, r2);

		/* Modification of tout */
		final long oldTout = r2.tout;
		r2.tout++;
		Assert.assertFalse(r1.equals(r2));
		r2.tout = oldTout;
		Assert.assertEquals(r1, r2);

		/* Modification of traceId */
		final long oldTraceId = r2.traceId;
		r2.traceId++;
		Assert.assertFalse(r1.equals(r2));
		r2.traceId = oldTraceId;
		Assert.assertEquals(r1, r2);

		/* Modification of hostName */
		final String oldVmName = r2.hostName;
		r2.hostName = r2.hostName + "_";
		Assert.assertFalse(r1.equals(r2));
		r2.hostName = oldVmName;
		Assert.assertEquals(r1, r2);
	}
}
