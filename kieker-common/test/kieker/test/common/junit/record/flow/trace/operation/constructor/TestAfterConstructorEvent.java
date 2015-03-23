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

package kieker.test.common.junit.record.flow.trace.operation.constructor;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.flow.trace.operation.constructor.AfterConstructorEvent;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.common.junit.record.UtilityClass;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public class TestAfterConstructorEvent extends AbstractKiekerTest {

	private static final long TSTAMP = 987998L;
	private static final long TRACE_ID = 23444L;
	private static final int ORDER_INDEX = 234;
	private static final String FQ_CLASSNAME = "p1.p2.p3.classname";
	private static final String FQ_OPERATION_SIGNATURE = FQ_CLASSNAME + ".callerOp(boolean arg1, int arg2)";

	/**
	 * Default constructor.
	 */
	public TestAfterConstructorEvent() {
		// empty default constructor
	}

	/**
	 * Tests the constructor and toArray(..) methods of {@link AfterConstructorEvent}.
	 * 
	 * Assert that a record instance event1 equals an instance event2 created by serializing event1 to an array event1Array
	 * and using event1Array to construct event2. This ignores a set loggingTimestamp!
	 */
	@Test
	public void testSerializeDeserializeEquals() {

		final AfterConstructorEvent event1 =
				new AfterConstructorEvent(TSTAMP, TRACE_ID, ORDER_INDEX, FQ_OPERATION_SIGNATURE, FQ_CLASSNAME);

		Assert.assertEquals("Unexpected timestamp", TSTAMP, event1.getTimestamp());
		Assert.assertEquals("Unexpected trace ID", TRACE_ID, event1.getTraceId());
		Assert.assertEquals("Unexpected order index", ORDER_INDEX, event1.getOrderIndex());
		Assert.assertEquals("Unexpected class name", FQ_CLASSNAME, event1.getClassSignature());
		Assert.assertEquals("Unexpected operation signature", FQ_OPERATION_SIGNATURE, event1.getOperationSignature());

		final Object[] event1Array = event1.toArray();

		final AfterConstructorEvent event2 = new AfterConstructorEvent(event1Array);

		Assert.assertEquals(event1, event2);
		Assert.assertEquals(0, event1.compareTo(event2));
		Assert.assertTrue(UtilityClass.refersToSameOperationAs(event1, event2));
	}

	/**
	 * Tests the constructor and writeBytes(..) methods of {@link AfterConstructorEvent}.
	 */
	@Test
	public void testSerializeDeserializeBinaryEquals() {

		final AfterConstructorEvent event1 =
				new AfterConstructorEvent(TSTAMP, TRACE_ID, ORDER_INDEX, FQ_OPERATION_SIGNATURE, FQ_CLASSNAME);

		Assert.assertEquals("Unexpected timestamp", TSTAMP, event1.getTimestamp());
		Assert.assertEquals("Unexpected trace ID", TRACE_ID, event1.getTraceId());
		Assert.assertEquals("Unexpected order index", ORDER_INDEX, event1.getOrderIndex());
		Assert.assertEquals("Unexpected class name", FQ_CLASSNAME, event1.getClassSignature());
		Assert.assertEquals("Unexpected operation signature", FQ_OPERATION_SIGNATURE, event1.getOperationSignature());

		final IRegistry<String> stringRegistry = new Registry<String>();
		final ByteBuffer buffer = ByteBuffer.allocate(event1.getSize());
		event1.writeBytes(buffer, stringRegistry);
		buffer.flip();

		final AfterConstructorEvent event2 = new AfterConstructorEvent(buffer, stringRegistry);

		Assert.assertEquals(event1, event2);
		Assert.assertEquals(0, event1.compareTo(event2));
		Assert.assertTrue(UtilityClass.refersToSameOperationAs(event1, event2));
	}
}
