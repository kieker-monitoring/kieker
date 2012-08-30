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

package kieker.test.common.junit.record.flow.trace.operation;

import junit.framework.Assert;

import org.junit.Test;

import kieker.common.record.flow.trace.operation.CallOperationEvent;

/**
 * @author Andre van Hoorn
 */
public class TestOperationCallEvent { // NOCS (MissingCtorCheck)

	private static final long TSTAMP = 987998L;
	private static final long TRACE_ID = 23444L;
	private static final int ORDER_INDEX = 234;

	private static final String FQ_CALLER_CLASSNAME = "p1.p2.p3.callername";
	private static final String FQ_CALLER_OPERATION_SIGNATURE =
			FQ_CALLER_CLASSNAME + ".callerOp(boolean arg1, int arg2)";
	private static final String FQ_CALLEE_CLASSNAME = "p1.p2.p3.calleename";
	private static final String FQ_CALLEE_OPERATION_SIGNATURE =
			FQ_CALLEE_CLASSNAME + ".calleeOp(boolean arg1, int arg2)";

	/**
	 * Tests the constructor and toArray(..) methods of {@link OperationCallEvent}.
	 * 
	 * Assert that a record instance call1 equals an instance call2 created by serializing call1 to an array call1Array
	 * and using call1Array to construct call2. This ignores a set loggingTimestamp!
	 */
	@Test
	public void testSerializeDeserializeEquals() {

		final CallOperationEvent call1 =
				new CallOperationEvent(TSTAMP, TRACE_ID, ORDER_INDEX, FQ_CALLER_OPERATION_SIGNATURE, FQ_CALLER_CLASSNAME,
						FQ_CALLEE_OPERATION_SIGNATURE, FQ_CALLEE_CLASSNAME);

		Assert.assertEquals("Unexpected timestamp", TSTAMP, call1.getTimestamp());
		Assert.assertEquals("Unexpected trace ID", TRACE_ID, call1.getTraceId());
		Assert.assertEquals("Unexpected order index", ORDER_INDEX, call1.getOrderIndex());
		Assert.assertEquals("Unexpected caller operation name", FQ_CALLER_OPERATION_SIGNATURE, call1.getCallerOperationSignature());
		Assert.assertEquals("Unexpected caller class name", FQ_CALLER_CLASSNAME, call1.getCallerClassSignature());
		Assert.assertEquals("Unexpected callee operation name", FQ_CALLEE_OPERATION_SIGNATURE, call1.getCalleeOperationSignature());
		Assert.assertEquals("Unexpected callee class name", FQ_CALLEE_CLASSNAME, call1.getCalleeClassSignature());

		final Object[] call1Array = call1.toArray();

		final CallOperationEvent call2 = new CallOperationEvent(call1Array);

		Assert.assertEquals(call1, call2);
	}
}
