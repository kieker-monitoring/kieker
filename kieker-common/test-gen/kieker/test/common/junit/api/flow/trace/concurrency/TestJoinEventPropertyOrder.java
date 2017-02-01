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

package kieker.test.common.junit.api.flow.trace.concurrency;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.flow.trace.concurrency.JoinEvent;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.common.junit.util.APIEvaluationFunctions;
			
/**
 * Test API of {@link kieker.common.record.flow.trace.concurrency.JoinEvent}.
 * 
 * @author API Checker
 * 
 * @since 1.12
 */
public class TestJoinEventPropertyOrder extends AbstractKiekerTest {

	/**
	 * All numbers and values must be pairwise unequal. As the string registry also uses integers,
	 * we must guarantee this criteria by starting with 1000 instead of 0.
	 */
	/** Constant value parameter for timestamp. */
	private static final long PROPERTY_TIMESTAMP = 2L;
	/** Constant value parameter for traceId. */
	private static final long PROPERTY_TRACE_ID = 3L;
	/** Constant value parameter for orderIndex. */
	private static final int PROPERTY_ORDER_INDEX = 1001;
	/** Constant value parameter for joinedTraceId. */
	private static final long PROPERTY_JOINED_TRACE_ID = 4L;
							
	/**
	 * Empty constructor.
	 */
	public TestJoinEventPropertyOrder() {
		// Empty constructor for test class.
	}

	/**
	 * Test property order processing of {@link kieker.common.record.flow.trace.concurrency.JoinEvent} constructors and
	 * different serialization routines.
	 */
	@Test
	public void testJoinEventPropertyOrder() { // NOPMD
		final IRegistry<String> stringRegistry = this.makeStringRegistry();
		final Object[] values = {
			PROPERTY_TIMESTAMP,
			PROPERTY_TRACE_ID,
			PROPERTY_ORDER_INDEX,
			PROPERTY_JOINED_TRACE_ID,
		};
		final ByteBuffer inputBuffer = APIEvaluationFunctions.createByteBuffer(JoinEvent.SIZE, 
			this.makeStringRegistry(), values);
					
		final JoinEvent recordInitParameter = new JoinEvent(
			PROPERTY_TIMESTAMP,
			PROPERTY_TRACE_ID,
			PROPERTY_ORDER_INDEX,
			PROPERTY_JOINED_TRACE_ID
		);
		final JoinEvent recordInitBuffer = new JoinEvent(inputBuffer, this.makeStringRegistry());
		final JoinEvent recordInitArray = new JoinEvent(values);
		
		this.assertJoinEvent(recordInitParameter);
		this.assertJoinEvent(recordInitBuffer);
		this.assertJoinEvent(recordInitArray);

		// test to array
		final Object[] valuesParameter = recordInitParameter.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesParameter);
		final Object[] valuesBuffer = recordInitBuffer.toArray();
		Assert.assertArrayEquals("Result array of record initialized by buffer constructor differs from predefined array.", values, valuesBuffer);
		final Object[] valuesArray = recordInitArray.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesArray);

		// test write to buffer
		final ByteBuffer outputBufferParameter = ByteBuffer.allocate(JoinEvent.SIZE);
		recordInitParameter.writeBytes(outputBufferParameter, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (parameter).", inputBuffer.array(), outputBufferParameter.array());

		final ByteBuffer outputBufferBuffer = ByteBuffer.allocate(JoinEvent.SIZE);
		recordInitParameter.writeBytes(outputBufferBuffer, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (buffer).", inputBuffer.array(), outputBufferBuffer.array());

		final ByteBuffer outputBufferArray = ByteBuffer.allocate(JoinEvent.SIZE);
		recordInitParameter.writeBytes(outputBufferArray, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (array).", inputBuffer.array(), outputBufferArray.array());
	}

	/**
	 * Assertions for JoinEvent.
	 */
	private void assertJoinEvent(final JoinEvent record) {
		Assert.assertEquals("'timestamp' value assertion failed.", record.getTimestamp(), PROPERTY_TIMESTAMP);
		Assert.assertEquals("'traceId' value assertion failed.", record.getTraceId(), PROPERTY_TRACE_ID);
		Assert.assertEquals("'orderIndex' value assertion failed.", record.getOrderIndex(), PROPERTY_ORDER_INDEX);
		Assert.assertEquals("'joinedTraceId' value assertion failed.", record.getJoinedTraceId(), PROPERTY_JOINED_TRACE_ID);
	}
			
	/**
	 * Build a populated string registry for all tests.
	 */
	private IRegistry<String> makeStringRegistry() {
		final IRegistry<String> stringRegistry = new Registry<String>();
		// get registers string and returns their ID

		return stringRegistry;
	}
}
