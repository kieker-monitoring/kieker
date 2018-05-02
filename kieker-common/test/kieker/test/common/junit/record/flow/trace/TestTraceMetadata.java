/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.common.junit.record.flow.trace;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.io.BinaryValueDeserializer;
import kieker.common.record.io.BinaryValueSerializer;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Jan Waller
 *
 * @since 1.6
 */
public class TestTraceMetadata extends AbstractKiekerTest {

	private static final long TRACE_ID = 23444L;
	private static final long THREAD_ID = 2389L;
	private static final String SESSION_ID = "mysession";
	private static final String HOSTNAME = "myhostname";
	private static final long PARENT_TRACE_ID = 3487L;
	private static final int PARENT_ORDER_ID = 357835;

	/**
	 * Default constructor.
	 */
	public TestTraceMetadata() {
		// empty default constructor
	}

	/**
	 * Tests the constructor and toArray(..) methods of {@link TraceMetadata}.
	 *
	 * Assert that a record instance trace1 equals an instance event2 created by serializing trace1 to an array trace1Array
	 * and using trace1Array to construct trace2. This ignores a set loggingTimestamp!
	 */
	@Test
	public void testSerializeDeserializeEquals() {

		final TraceMetadata trace1 = new TraceMetadata(TRACE_ID, THREAD_ID, SESSION_ID, HOSTNAME, PARENT_TRACE_ID, PARENT_ORDER_ID);

		Assert.assertEquals("Unexpected trace ID", TRACE_ID, trace1.getTraceId());
		Assert.assertEquals("Unexpected thread ID", THREAD_ID, trace1.getThreadId());
		Assert.assertEquals("Unexpected session ID", SESSION_ID, trace1.getSessionId());
		Assert.assertEquals("Unexpected hostname", HOSTNAME, trace1.getHostname());
		Assert.assertEquals("Unexpected parent trace ID", PARENT_TRACE_ID, trace1.getParentTraceId());
		Assert.assertEquals("Unexpected parent order ID", PARENT_ORDER_ID, trace1.getParentOrderId());

		final Object[] trace1Array = trace1.toArray();

		final TraceMetadata trace2 = new TraceMetadata(trace1Array);

		Assert.assertEquals(trace1, trace2);
		Assert.assertEquals(0, trace1.compareTo(trace2));
	}

	/**
	 * Tests the constructor and writeBytes(..) methods of {@link TraceMetadata}.
	 */
	@Test
	public void testSerializeDeserializeBinaryEquals() {

		final TraceMetadata trace1 = new TraceMetadata(TRACE_ID, THREAD_ID, SESSION_ID, HOSTNAME, PARENT_TRACE_ID, PARENT_ORDER_ID);

		Assert.assertEquals("Unexpected trace ID", TRACE_ID, trace1.getTraceId());
		Assert.assertEquals("Unexpected thread ID", THREAD_ID, trace1.getThreadId());
		Assert.assertEquals("Unexpected session ID", SESSION_ID, trace1.getSessionId());
		Assert.assertEquals("Unexpected hostname", HOSTNAME, trace1.getHostname());
		Assert.assertEquals("Unexpected parent trace ID", PARENT_TRACE_ID, trace1.getParentTraceId());
		Assert.assertEquals("Unexpected parent order ID", PARENT_ORDER_ID, trace1.getParentOrderId());

		final IRegistry<String> stringRegistry = new Registry<String>();
		final ByteBuffer buffer = ByteBuffer.allocate(trace1.getSize());
		trace1.serialize(BinaryValueSerializer.create(buffer, stringRegistry));
		buffer.flip();

		final TraceMetadata trace2 = new TraceMetadata(BinaryValueDeserializer.create(buffer, stringRegistry));

		Assert.assertEquals(trace1, trace2);
		Assert.assertEquals(0, trace1.compareTo(trace2));
	}
}
