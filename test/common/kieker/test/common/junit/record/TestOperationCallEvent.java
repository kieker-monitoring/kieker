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
import kieker.common.record.flow.trace.operation.CallOperationEvent;

import org.junit.Test;

/**
 * @author Andre van Hoorn
 */
public class TestOperationCallEvent extends TestCase { // NOCS (MissingCtorCheck)

	private static final long TSTAMP = 987998l; // NOCS (MagicNumberCheck)
	private static final long TRACE_ID = 23444l; // NOCS (MagicNumberCheck)
	private static final int ORDER_INDEX = 234; // NOCS (MagicNumberCheck)

	private static final String FQ_CALLER_CLASSNAME = "p1.p2.p3.callername";
	private static final String FQ_CALLER_OPERATION_SIGNATURE =
			TestOperationCallEvent.FQ_CALLER_CLASSNAME + ".callerOp(boolean arg1, int arg2)";
	private static final String FQ_CALLEE_CLASSNAME = "p1.p2.p3.calleename";
	private static final String FQ_CALLEE_OPERATION_SIGNATURE =
			TestOperationCallEvent.FQ_CALLEE_CLASSNAME + ".calleeOp(boolean arg1, int arg2)";

	/**
	 * Tests the constructor and toArray(..) methods of {@link OperationCallEvent}.
	 * 
	 * Assert that a record instance call1 equals an instance call2 created by serializing call1 to an array call1Array
	 * and using call1Array to construct call2. This ignores a set loggingTimestamp!
	 */
	@Test
	public void testSerializeDeserializeEquals() {

		final CallOperationEvent call1 =
				new CallOperationEvent(TestOperationCallEvent.TSTAMP, TestOperationCallEvent.TRACE_ID, TestOperationCallEvent.ORDER_INDEX,
						TestOperationCallEvent.FQ_CALLER_OPERATION_SIGNATURE, TestOperationCallEvent.FQ_CALLEE_OPERATION_SIGNATURE);

		Assert.assertEquals("Unexpected timestamp", TestOperationCallEvent.TSTAMP, call1.getTimestamp());
		Assert.assertEquals("Unexpected trace ID", TestOperationCallEvent.TRACE_ID, call1.getTraceId());
		Assert.assertEquals("Unexpected order index", TestOperationCallEvent.ORDER_INDEX, call1.getOrderIndex());
		Assert.assertEquals("Unexpected caller operation name", TestOperationCallEvent.FQ_CALLER_OPERATION_SIGNATURE, call1.getCallerOperationSignature());
		Assert.assertEquals("Unexpected callee operation name", TestOperationCallEvent.FQ_CALLEE_OPERATION_SIGNATURE, call1.getCalleeOperationSignature());

		final Object[] call1Array = call1.toArray();

		final CallOperationEvent call2 = new CallOperationEvent(call1Array);

		Assert.assertEquals(call1, call2);
	}
}
