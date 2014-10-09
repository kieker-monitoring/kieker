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

package kieker.test.common.junit.record.flow.trace.operation.constructor;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.flow.trace.operation.constructor.CallConstructorEvent;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.common.junit.util.APIEvaluationFunctions;

/**
 * Test API of {@link kieker.common.record.flow.trace.operation.constructor.CallConstructorEvent}.
 * 
 * @author Reiner Jung
 * 
 * @since 1.10
 */
public class TestCallConstructorEventPropertyOrder extends AbstractKiekerTest {

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
	/** Constant value parameter for callerOperationSignature. */
	private static final String PROPERTY_CALLER_OPERATION_SIGNATURE = "<callerOperationSignature>";
	/** Constant value parameter for callerClassSignature. */
	private static final String PROPERTY_CALLER_CLASS_SIGNATURE = "<callerClassSignature>";
	/** Constant value parameter for calleeOperationSignature. */
	private static final String PROPERTY_CALLEE_OPERATION_SIGNATURE = "<calleeOperationSignature>";
	/** Constant value parameter for calleeClassSignature. */
	private static final String PROPERTY_CALLEE_CLASS_SIGNATURE = "<calleeClassSignature>";

	/**
	 * Empty constructor.
	 */
	public TestCallConstructorEventPropertyOrder() {
		// Empty constructor for test class.
	}

	/**
	 * Test property order processing of {@link kieker.common.record.flow.trace.operation.constructor.CallConstructorEvent} constructors and
	 * different serialization routines.
	 */
	@Test
	public void testCallConstructorEventPropertyOrder() { // NOPMD
		final IRegistry<String> stringRegistry = this.makeStringRegistry();
		final Object[] values = {
			PROPERTY_TIMESTAMP,
			PROPERTY_TRACE_ID,
			PROPERTY_ORDER_INDEX,
			PROPERTY_CALLER_OPERATION_SIGNATURE,
			PROPERTY_CALLER_CLASS_SIGNATURE,
			PROPERTY_CALLEE_OPERATION_SIGNATURE,
			PROPERTY_CALLEE_CLASS_SIGNATURE,
		};
		final ByteBuffer inputBuffer = APIEvaluationFunctions.createByteBuffer(CallConstructorEvent.SIZE,
				this.makeStringRegistry(), values);

		final CallConstructorEvent recordInitParameter = new CallConstructorEvent(
				PROPERTY_TIMESTAMP,
				PROPERTY_TRACE_ID,
				PROPERTY_ORDER_INDEX,
				PROPERTY_CALLER_OPERATION_SIGNATURE,
				PROPERTY_CALLER_CLASS_SIGNATURE,
				PROPERTY_CALLEE_OPERATION_SIGNATURE,
				PROPERTY_CALLEE_CLASS_SIGNATURE
				);
		final CallConstructorEvent recordInitBuffer = new CallConstructorEvent(inputBuffer, this.makeStringRegistry());
		final CallConstructorEvent recordInitArray = new CallConstructorEvent(values);

		this.assertCallConstructorEvent(recordInitParameter);
		this.assertCallConstructorEvent(recordInitBuffer);
		this.assertCallConstructorEvent(recordInitArray);

		// test to array
		final Object[] valuesParameter = recordInitParameter.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesParameter);
		final Object[] valuesBuffer = recordInitBuffer.toArray();
		Assert.assertArrayEquals("Result array of record initialized by buffer constructor differs from predefined array.", values, valuesBuffer);
		final Object[] valuesArray = recordInitArray.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesArray);

		// test write to buffer
		final ByteBuffer outputBufferParameter = ByteBuffer.allocate(CallConstructorEvent.SIZE);
		recordInitParameter.writeBytes(outputBufferParameter, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (parameter).", inputBuffer.array(), outputBufferParameter.array());

		final ByteBuffer outputBufferBuffer = ByteBuffer.allocate(CallConstructorEvent.SIZE);
		recordInitParameter.writeBytes(outputBufferBuffer, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (buffer).", inputBuffer.array(), outputBufferBuffer.array());

		final ByteBuffer outputBufferArray = ByteBuffer.allocate(CallConstructorEvent.SIZE);
		recordInitParameter.writeBytes(outputBufferArray, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (array).", inputBuffer.array(), outputBufferArray.array());
	}

	/**
	 * Assertions for CallConstructorEvent.
	 */
	private void assertCallConstructorEvent(final CallConstructorEvent record) {
		Assert.assertEquals("'timestamp' value assertion failed.", record.getTimestamp(), PROPERTY_TIMESTAMP);
		Assert.assertEquals("'traceId' value assertion failed.", record.getTraceId(), PROPERTY_TRACE_ID);
		Assert.assertEquals("'orderIndex' value assertion failed.", record.getOrderIndex(), PROPERTY_ORDER_INDEX);
		Assert.assertEquals("'callerOperationSignature' value assertion failed.", record.getCallerOperationSignature(), PROPERTY_CALLER_OPERATION_SIGNATURE);
		Assert.assertEquals("'callerClassSignature' value assertion failed.", record.getCallerClassSignature(), PROPERTY_CALLER_CLASS_SIGNATURE);
		Assert.assertEquals("'calleeOperationSignature' value assertion failed.", record.getCalleeOperationSignature(), PROPERTY_CALLEE_OPERATION_SIGNATURE);
		Assert.assertEquals("'calleeClassSignature' value assertion failed.", record.getCalleeClassSignature(), PROPERTY_CALLEE_CLASS_SIGNATURE);
	}

	/**
	 * Build a populated string registry for all tests.
	 */
	private IRegistry<String> makeStringRegistry() {
		final IRegistry<String> stringRegistry = new Registry<String>();
		// get registers string and returns their ID
		stringRegistry.get(PROPERTY_CALLER_OPERATION_SIGNATURE);
		stringRegistry.get(PROPERTY_CALLER_CLASS_SIGNATURE);
		stringRegistry.get(PROPERTY_CALLEE_OPERATION_SIGNATURE);
		stringRegistry.get(PROPERTY_CALLEE_CLASS_SIGNATURE);

		return stringRegistry;
	}
}
