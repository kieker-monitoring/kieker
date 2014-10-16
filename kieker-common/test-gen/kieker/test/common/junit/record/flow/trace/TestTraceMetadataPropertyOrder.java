/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.common.junit.util.APIEvaluationFunctions;

/**
 * Test API of {@link kieker.common.record.flow.trace.TraceMetadata}.
 * 
 * @author Reiner Jung
 * 
 * @since 1.10
 */
public class TestTraceMetadataPropertyOrder extends AbstractKiekerTest {

	/**
	 * All numbers and values must be pairwise unequal. As the string registry also uses integers,
	 * we must guarantee this criteria by starting with 1000 instead of 0.
	 */
	/** Constant value parameter for traceId. */
	private static final long PROPERTY_TRACE_ID = 2L;
	/** Constant value parameter for threadId. */
	private static final long PROPERTY_THREAD_ID = 3L;
	/** Constant value parameter for sessionId. */
	private static final String PROPERTY_SESSION_ID = "<sessionId>";
	/** Constant value parameter for hostname. */
	private static final String PROPERTY_HOSTNAME = "<hostname>";
	/** Constant value parameter for parentTraceId. */
	private static final long PROPERTY_PARENT_TRACE_ID = 4L;
	/** Constant value parameter for parentOrderId. */
	private static final int PROPERTY_PARENT_ORDER_ID = 1001;

	/**
	 * Empty constructor.
	 */
	public TestTraceMetadataPropertyOrder() {
		// Empty constructor for test class.
	}

	/**
	 * Test property order processing of {@link kieker.common.record.flow.trace.TraceMetadata} constructors and
	 * different serialization routines.
	 */
	@Test
	public void testTraceMetadataPropertyOrder() { // NOPMD
		final IRegistry<String> stringRegistry = this.makeStringRegistry();
		final Object[] values = {
			PROPERTY_TRACE_ID,
			PROPERTY_THREAD_ID,
			PROPERTY_SESSION_ID,
			PROPERTY_HOSTNAME,
			PROPERTY_PARENT_TRACE_ID,
			PROPERTY_PARENT_ORDER_ID,
		};
		final ByteBuffer inputBuffer = APIEvaluationFunctions.createByteBuffer(TraceMetadata.SIZE,
				this.makeStringRegistry(), values);

		final TraceMetadata recordInitParameter = new TraceMetadata(
				PROPERTY_TRACE_ID,
				PROPERTY_THREAD_ID,
				PROPERTY_SESSION_ID,
				PROPERTY_HOSTNAME,
				PROPERTY_PARENT_TRACE_ID,
				PROPERTY_PARENT_ORDER_ID
				);
		final TraceMetadata recordInitBuffer = new TraceMetadata(inputBuffer, this.makeStringRegistry());
		final TraceMetadata recordInitArray = new TraceMetadata(values);

		this.assertTraceMetadata(recordInitParameter);
		this.assertTraceMetadata(recordInitBuffer);
		this.assertTraceMetadata(recordInitArray);

		// test to array
		final Object[] valuesParameter = recordInitParameter.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesParameter);
		final Object[] valuesBuffer = recordInitBuffer.toArray();
		Assert.assertArrayEquals("Result array of record initialized by buffer constructor differs from predefined array.", values, valuesBuffer);
		final Object[] valuesArray = recordInitArray.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesArray);

		// test write to buffer
		final ByteBuffer outputBufferParameter = ByteBuffer.allocate(TraceMetadata.SIZE);
		recordInitParameter.writeBytes(outputBufferParameter, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (parameter).", inputBuffer.array(), outputBufferParameter.array());

		final ByteBuffer outputBufferBuffer = ByteBuffer.allocate(TraceMetadata.SIZE);
		recordInitParameter.writeBytes(outputBufferBuffer, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (buffer).", inputBuffer.array(), outputBufferBuffer.array());

		final ByteBuffer outputBufferArray = ByteBuffer.allocate(TraceMetadata.SIZE);
		recordInitParameter.writeBytes(outputBufferArray, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (array).", inputBuffer.array(), outputBufferArray.array());
	}

	/**
	 * Assertions for TraceMetadata.
	 */
	private void assertTraceMetadata(final TraceMetadata record) {
		Assert.assertEquals("'traceId' value assertion failed.", record.getTraceId(), PROPERTY_TRACE_ID);
		Assert.assertEquals("'threadId' value assertion failed.", record.getThreadId(), PROPERTY_THREAD_ID);
		Assert.assertEquals("'sessionId' value assertion failed.", record.getSessionId(), PROPERTY_SESSION_ID);
		Assert.assertEquals("'hostname' value assertion failed.", record.getHostname(), PROPERTY_HOSTNAME);
		Assert.assertEquals("'parentTraceId' value assertion failed.", record.getParentTraceId(), PROPERTY_PARENT_TRACE_ID);
		Assert.assertEquals("'parentOrderId' value assertion failed.", record.getParentOrderId(), PROPERTY_PARENT_ORDER_ID);
	}

	/**
	 * Build a populated string registry for all tests.
	 */
	private IRegistry<String> makeStringRegistry() {
		final IRegistry<String> stringRegistry = new Registry<String>();
		// get registers string and returns their ID
		stringRegistry.get(PROPERTY_SESSION_ID);
		stringRegistry.get(PROPERTY_HOSTNAME);

		return stringRegistry;
	}
}
