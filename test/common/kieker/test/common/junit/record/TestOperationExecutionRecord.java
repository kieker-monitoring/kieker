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

package kieker.test.common.junit.record;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.common.record.controlflow.OperationExecutionRecord;

import org.junit.Test;

/**
 * @author Andre van Hoorn
 */
public class TestOperationExecutionRecord extends TestCase { // NOCS (MissingCtorCheck)

	/**
	 * Tests the toArray(..) and initFromArray(..) methods of OperationExecutionRecord.
	 * 
	 * Assert that a record instance r1 equals an instance r2 created by serializing r1 to an array a1 and using a1 to init r2.
	 * This ignores a set loggingTimestamp!
	 */
	@Test
	public void testSerializeDeserializeEquals() {
		final OperationExecutionRecord r1 = new OperationExecutionRecord();
		r1.setClassName("p1.p2.p3.componentname"); // NOPMD (string literal)
		r1.setEoi(1); // NOCS (MagicNumberCheck)
		r1.setEss(2); // NOCS (MagicNumberCheck)
		r1.setExperimentId(55); // NOCS (MagicNumberCheck)
		r1.setEntryPoint(true);
		r1.setOperationName("operation(boolean arg1, int arg2)"); // NOPMD (string literal)
		r1.setRetVal(new Object());
		r1.setSessionId("XXLJHDJHDHF"); // NOPMD (string literal)
		r1.setTin(5577376); // NOCS (MagicNumberCheck)
		r1.setTout(7544522); // NOCS (MagicNumberCheck)
		r1.setTraceId(882287444); // NOCS (MagicNumberCheck)
		r1.setHostName("myVM"); // NOPMD (string literal)

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
		final OperationExecutionRecord r1 = new OperationExecutionRecord();
		r1.setClassName("p1.p2.p3.componentname");
		r1.setEoi(1); // NOCS (MagicNumberCheck)
		r1.setEss(2); // NOCS (MagicNumberCheck)
		r1.setExperimentId(55); // NOCS (MagicNumberCheck)
		r1.setEntryPoint(true);
		r1.setOperationName("operation(boolean arg1, int arg2)");
		r1.setRetVal(new Object());
		r1.setSessionId("XXLJHDJHDHF");
		r1.setTin(5577376); // NOCS (MagicNumberCheck)
		r1.setTout(7544522); // NOCS (MagicNumberCheck)
		r1.setTraceId(882287444); // NOCS (MagicNumberCheck)
		r1.setHostName("myVM");

		final OperationExecutionRecord r2 = new OperationExecutionRecord();
		r2.setClassName("p1.p2.p3.componentname");
		r2.setEoi(1); // NOCS (MagicNumberCheck)
		r2.setEss(2); // NOCS (MagicNumberCheck)
		r2.setExperimentId(55); // NOCS (MagicNumberCheck)
		r2.setEntryPoint(true);
		r2.setOperationName("operation(boolean arg1, int arg2)");
		r2.setRetVal(new Object());
		r2.setSessionId("XXLJHDJHDHF");
		r2.setTin(5577376); // NOCS (MagicNumberCheck)
		r2.setTout(7544522); // NOCS (MagicNumberCheck)
		r2.setTraceId(882287444); // NOCS (MagicNumberCheck)
		r2.setHostName("myVM");

		Assert.assertEquals(r1, r2);
	}

	/**
	 * Tests the equals(..) method of OperationExecutionRecord.
	 * 
	 * Assert that two record instances with null variables values are not equal.
	 */
	@Test
	public void testEqualsNullVariableValues() {
		final OperationExecutionRecord r1 = new OperationExecutionRecord();
		r1.setClassName("p1.p2.p3.componentname");
		r1.setEoi(1); // NOCS (MagicNumberCheck)
		r1.setEss(2); // NOCS (MagicNumberCheck)
		r1.setExperimentId(55); // NOCS (MagicNumberCheck)
		r1.setEntryPoint(true);
		r1.setOperationName("operation(boolean arg1, int arg2)");
		r1.setRetVal(new Object());
		r1.setSessionId("XXLJHDJHDHF");
		r1.setTin(5577376); // NOCS (MagicNumberCheck)
		r1.setTout(7544522); // NOCS (MagicNumberCheck)
		r1.setTraceId(882287444); // NOCS (MagicNumberCheck)
		r1.setHostName("myVM");

		final OperationExecutionRecord r2 = new OperationExecutionRecord();
		r2.setClassName("p1.p2.p3.componentname");
		r2.setEoi(1); // NOCS (MagicNumberCheck)
		r2.setEss(2); // NOCS (MagicNumberCheck)
		r2.setExperimentId(55); // NOCS (MagicNumberCheck)
		r2.setEntryPoint(true);
		r2.setOperationName("operation(boolean arg1, int arg2)");
		r2.setRetVal(new Object());
		r2.setSessionId("XXLJHDJHDHF");
		r2.setTin(5577376); // NOCS (MagicNumberCheck)
		r2.setTout(7544522); // NOCS (MagicNumberCheck)
		r2.setTraceId(882287444); // NOCS (MagicNumberCheck)
		r2.setHostName("myVM");

		Assert.assertEquals(r1, r2);

		/* Modification in r1 */
		final String oldComponentName1 = r1.getClassName();
		r1.setClassName(null);
		Assert.assertFalse(r1.equals(r2));
		r1.setClassName(oldComponentName1);
		Assert.assertEquals(r1, r2);

		/* Modification in r2 */
		final String oldComponentName2 = r2.getClassName();
		r2.setClassName(null);
		Assert.assertFalse(r1.equals(r2));
		r2.setClassName(oldComponentName2);
		Assert.assertEquals(r1, r2);

		/* Modification in both */
		r1.setClassName(null);
		r2.setClassName(null);
		Assert.assertEquals(r1, r2);
		r1.setClassName(oldComponentName1);
		r2.setClassName(oldComponentName2);
		Assert.assertEquals(r1, r2);
	}

	/**
	 * Tests the equals(..) method of OperationExecutionRecord.
	 * 
	 * Assert that two record instances with differing variables values are not equal.
	 */
	@Test
	public void testEqualsDifferentVariablesValues() {
		final OperationExecutionRecord r1 = new OperationExecutionRecord();
		r1.setClassName("p1.p2.p3.componentname");
		r1.setEoi(1); // NOCS (MagicNumberCheck)
		r1.setEss(2); // NOCS (MagicNumberCheck)
		r1.setExperimentId(55); // NOCS (MagicNumberCheck)
		r1.setEntryPoint(true);
		r1.setOperationName("operation(boolean arg1, int arg2)");
		r1.setRetVal(new Object());
		r1.setSessionId("XXLJHDJHDHF");
		r1.setTin(5577376); // NOCS (MagicNumberCheck)
		r1.setTout(7544522); // NOCS (MagicNumberCheck)
		r1.setTraceId(882287444); // NOCS (MagicNumberCheck)
		r1.setHostName("myVM");

		final OperationExecutionRecord r2 = new OperationExecutionRecord();
		r2.setClassName("p1.p2.p3.componentname");
		r2.setEoi(1); // NOCS (MagicNumberCheck)
		r2.setEss(2); // NOCS (MagicNumberCheck)
		r2.setExperimentId(55); // NOCS (MagicNumberCheck)
		r2.setEntryPoint(true);
		r2.setOperationName("operation(boolean arg1, int arg2)");
		r2.setRetVal(new Object());
		r2.setSessionId("XXLJHDJHDHF");
		r2.setTin(5577376); // NOCS (MagicNumberCheck)
		r2.setTout(7544522); // NOCS (MagicNumberCheck)
		r2.setTraceId(882287444); // NOCS (MagicNumberCheck)
		r2.setHostName("myVM");

		Assert.assertEquals(r1, r2);

		/* Modification of className */
		final String oldComponentName = r2.getClassName();
		r2.setClassName(r2.getClassName() + "_");
		Assert.assertFalse(r1.equals(r2));
		r2.setClassName(oldComponentName);
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

		/* Modification of operationName */
		final String oldOpname = r2.getOperationName();
		r2.setOperationName(r2.getOperationName() + "_");
		Assert.assertFalse(r1.equals(r2));
		r2.setOperationName(oldOpname);
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

		/* Modification of hostName */
		final String oldVmName = r2.getHostName();
		r2.setHostName(r2.getHostName() + "_");
		Assert.assertFalse(r1.equals(r2));
		r2.setHostName(oldVmName);
		Assert.assertEquals(r1, r2);
	}
}
