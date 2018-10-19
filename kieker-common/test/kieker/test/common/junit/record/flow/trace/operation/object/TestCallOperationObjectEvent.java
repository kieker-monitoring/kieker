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

package kieker.test.common.junit.record.flow.trace.operation.object;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.flow.trace.operation.object.CallOperationObjectEvent;
import kieker.common.record.io.BinaryValueDeserializer;
import kieker.common.record.io.BinaryValueSerializer;
import kieker.common.registry.writer.IWriterRegistry;
import kieker.common.registry.writer.WriterRegistry;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.common.junit.WriterListener;
import kieker.test.common.junit.record.UtilityClass;

/**
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.6
 */
public class TestCallOperationObjectEvent extends AbstractKiekerTest {

	private static final long TSTAMP = 987998L;
	private static final long TRACE_ID = 23444L;
	private static final int ORDER_INDEX = 234;
	private static final String FQ_CALLER_CLASSNAME = "p1.p2.p3.callername";
	private static final String FQ_CALLER_OPERATION_SIGNATURE = FQ_CALLER_CLASSNAME + ".callerOp(boolean arg1, int arg2)";
	private static final String FQ_CALLEE_CLASSNAME = "p1.p2.p3.calleename";
	private static final String FQ_CALLEE_OPERATION_SIGNATURE = FQ_CALLEE_CLASSNAME + ".calleeOp(boolean arg1, int arg2)";
	private static final int CALLER_OBJECT_ID = 42;
	private static final int CALLEE_OBJECT_ID = 666;

	/**
	 * Default constructor.
	 */
	public TestCallOperationObjectEvent() {
		// empty default constructor
	}

	/**
	 * Tests the constructor and toArray(..) methods of {@link CallOperationObjectEvent}.
	 *
	 * Assert that a record instance event1 equals an instance event2 created by serializing event1 to an array event1Array
	 * and using event1Array to construct event2. This ignores a set loggingTimestamp!
	 */
	@Test
	public void testSerializeDeserializeEquals() {

		final CallOperationObjectEvent event1 = new CallOperationObjectEvent(TSTAMP, TRACE_ID, ORDER_INDEX, FQ_CALLER_OPERATION_SIGNATURE, FQ_CALLER_CLASSNAME,
				FQ_CALLEE_OPERATION_SIGNATURE, FQ_CALLEE_CLASSNAME, CALLER_OBJECT_ID, CALLEE_OBJECT_ID);

		Assert.assertEquals("Unexpected timestamp", TSTAMP, event1.getTimestamp());
		Assert.assertEquals("Unexpected trace ID", TRACE_ID, event1.getTraceId());
		Assert.assertEquals("Unexpected order index", ORDER_INDEX, event1.getOrderIndex());
		Assert.assertEquals("Unexpected caller operation name", FQ_CALLER_OPERATION_SIGNATURE, event1.getCallerOperationSignature());
		Assert.assertEquals("Unexpected caller class name", FQ_CALLER_CLASSNAME, event1.getCallerClassSignature());
		Assert.assertEquals("Unexpected callee operation name", FQ_CALLEE_OPERATION_SIGNATURE, event1.getCalleeOperationSignature());
		Assert.assertEquals("Unexpected callee class name", FQ_CALLEE_CLASSNAME, event1.getCalleeClassSignature());
		Assert.assertEquals("Unexpected caller object id", CALLER_OBJECT_ID, event1.getCallerObjectId());
		Assert.assertEquals("Unexpected callee object id", CALLEE_OBJECT_ID, event1.getCalleeObjectId());
	}

	/**
	 * Tests the constructor and writeBytes(..) methods of {@link CallOperationObjectEvent}.
	 */
	@Test
	public void testSerializeDeserializeBinaryEquals() {

		final CallOperationObjectEvent event1 = new CallOperationObjectEvent(TSTAMP, TRACE_ID, ORDER_INDEX, FQ_CALLER_OPERATION_SIGNATURE, FQ_CALLER_CLASSNAME,
				FQ_CALLEE_OPERATION_SIGNATURE, FQ_CALLEE_CLASSNAME, CALLER_OBJECT_ID, CALLEE_OBJECT_ID);

		Assert.assertEquals("Unexpected timestamp", TSTAMP, event1.getTimestamp());
		Assert.assertEquals("Unexpected trace ID", TRACE_ID, event1.getTraceId());
		Assert.assertEquals("Unexpected order index", ORDER_INDEX, event1.getOrderIndex());
		Assert.assertEquals("Unexpected caller operation name", FQ_CALLER_OPERATION_SIGNATURE, event1.getCallerOperationSignature());
		Assert.assertEquals("Unexpected caller class name", FQ_CALLER_CLASSNAME, event1.getCallerClassSignature());
		Assert.assertEquals("Unexpected callee operation name", FQ_CALLEE_OPERATION_SIGNATURE, event1.getCalleeOperationSignature());
		Assert.assertEquals("Unexpected callee class name", FQ_CALLEE_CLASSNAME, event1.getCalleeClassSignature());
		Assert.assertEquals("Unexpected caller object id", CALLER_OBJECT_ID, event1.getCallerObjectId());
		Assert.assertEquals("Unexpected callee object id", CALLEE_OBJECT_ID, event1.getCalleeObjectId());

		final WriterListener receiver = new WriterListener();
		final IWriterRegistry<String> stringRegistry = new WriterRegistry(receiver);
		final ByteBuffer buffer = ByteBuffer.allocate(event1.getSize());
		event1.serialize(BinaryValueSerializer.create(buffer, stringRegistry));
		buffer.flip();

		final CallOperationObjectEvent event2 = new CallOperationObjectEvent(BinaryValueDeserializer.create(buffer, receiver.getReaderRegistry()));

		Assert.assertEquals(event1, event2);
		Assert.assertEquals(0, event1.compareTo(event2));
		Assert.assertTrue(UtilityClass.refersToSameOperationAs(event1, event2));
	}

	@Test
	public void testCallsReferencedOperationOf() {
		final CallOperationObjectEvent event1 = new CallOperationObjectEvent(TSTAMP, TRACE_ID, ORDER_INDEX, FQ_CALLER_CLASSNAME, FQ_CALLER_OPERATION_SIGNATURE,
				FQ_CALLEE_CLASSNAME, FQ_CALLEE_OPERATION_SIGNATURE, CALLER_OBJECT_ID, CALLEE_OBJECT_ID);
		final CallOperationObjectEvent event2 = new CallOperationObjectEvent(TSTAMP, TRACE_ID, ORDER_INDEX, FQ_CALLEE_CLASSNAME, FQ_CALLEE_OPERATION_SIGNATURE,
				FQ_CALLEE_CLASSNAME, FQ_CALLEE_OPERATION_SIGNATURE, CALLER_OBJECT_ID, CALLEE_OBJECT_ID);

		Assert.assertTrue(UtilityClass.callsReferencedOperationOf(event1, event2));
	}
}
