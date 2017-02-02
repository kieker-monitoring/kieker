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

package kieker.test.common.junit.api.flow.trace.operation.constructor;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.flow.trace.operation.constructor.AfterConstructorFailedEvent;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.common.junit.util.APIEvaluationFunctions;
			
/**
 * Test API of {@link kieker.common.record.flow.trace.operation.constructor.AfterConstructorFailedEvent}.
 * 
 * @author API Checker
 * 
 * @since 1.12
 */
public class TestAfterConstructorFailedEventPropertyOrder extends AbstractKiekerTest {

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
	/** Constant value parameter for operationSignature. */
	private static final String PROPERTY_OPERATION_SIGNATURE = "<operationSignature>";
	/** Constant value parameter for classSignature. */
	private static final String PROPERTY_CLASS_SIGNATURE = "<classSignature>";
	/** Constant value parameter for cause. */
	private static final String PROPERTY_CAUSE = "<cause>";
							
	/**
	 * Empty constructor.
	 */
	public TestAfterConstructorFailedEventPropertyOrder() {
		// Empty constructor for test class.
	}

	/**
	 * Test property order processing of {@link kieker.common.record.flow.trace.operation.constructor.AfterConstructorFailedEvent} constructors and
	 * different serialization routines.
	 */
	@Test
	public void testAfterConstructorFailedEventPropertyOrder() { // NOPMD
		final IRegistry<String> stringRegistry = this.makeStringRegistry();
		final Object[] values = {
			PROPERTY_TIMESTAMP,
			PROPERTY_TRACE_ID,
			PROPERTY_ORDER_INDEX,
			PROPERTY_OPERATION_SIGNATURE,
			PROPERTY_CLASS_SIGNATURE,
			PROPERTY_CAUSE,
		};
		final ByteBuffer inputBuffer = APIEvaluationFunctions.createByteBuffer(AfterConstructorFailedEvent.SIZE, 
			this.makeStringRegistry(), values);
					
		final AfterConstructorFailedEvent recordInitParameter = new AfterConstructorFailedEvent(
			PROPERTY_TIMESTAMP,
			PROPERTY_TRACE_ID,
			PROPERTY_ORDER_INDEX,
			PROPERTY_OPERATION_SIGNATURE,
			PROPERTY_CLASS_SIGNATURE,
			PROPERTY_CAUSE
		);
		final AfterConstructorFailedEvent recordInitBuffer = new AfterConstructorFailedEvent(inputBuffer, this.makeStringRegistry());
		final AfterConstructorFailedEvent recordInitArray = new AfterConstructorFailedEvent(values);
		
		this.assertAfterConstructorFailedEvent(recordInitParameter);
		this.assertAfterConstructorFailedEvent(recordInitBuffer);
		this.assertAfterConstructorFailedEvent(recordInitArray);

		// test to array
		final Object[] valuesParameter = recordInitParameter.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesParameter);
		final Object[] valuesBuffer = recordInitBuffer.toArray();
		Assert.assertArrayEquals("Result array of record initialized by buffer constructor differs from predefined array.", values, valuesBuffer);
		final Object[] valuesArray = recordInitArray.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesArray);

		// test write to buffer
		final ByteBuffer outputBufferParameter = ByteBuffer.allocate(AfterConstructorFailedEvent.SIZE);
		recordInitParameter.writeBytes(outputBufferParameter, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (parameter).", inputBuffer.array(), outputBufferParameter.array());

		final ByteBuffer outputBufferBuffer = ByteBuffer.allocate(AfterConstructorFailedEvent.SIZE);
		recordInitParameter.writeBytes(outputBufferBuffer, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (buffer).", inputBuffer.array(), outputBufferBuffer.array());

		final ByteBuffer outputBufferArray = ByteBuffer.allocate(AfterConstructorFailedEvent.SIZE);
		recordInitParameter.writeBytes(outputBufferArray, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (array).", inputBuffer.array(), outputBufferArray.array());
	}

	/**
	 * Assertions for AfterConstructorFailedEvent.
	 */
	private void assertAfterConstructorFailedEvent(final AfterConstructorFailedEvent record) {
		Assert.assertEquals("'timestamp' value assertion failed.", record.getTimestamp(), PROPERTY_TIMESTAMP);
		Assert.assertEquals("'traceId' value assertion failed.", record.getTraceId(), PROPERTY_TRACE_ID);
		Assert.assertEquals("'orderIndex' value assertion failed.", record.getOrderIndex(), PROPERTY_ORDER_INDEX);
		Assert.assertEquals("'operationSignature' value assertion failed.", record.getOperationSignature(), PROPERTY_OPERATION_SIGNATURE);
		Assert.assertEquals("'classSignature' value assertion failed.", record.getClassSignature(), PROPERTY_CLASS_SIGNATURE);
		Assert.assertEquals("'cause' value assertion failed.", record.getCause(), PROPERTY_CAUSE);
	}
			
	/**
	 * Build a populated string registry for all tests.
	 */
	private IRegistry<String> makeStringRegistry() {
		final IRegistry<String> stringRegistry = new Registry<String>();
		// get registers string and returns their ID
		stringRegistry.get(PROPERTY_OPERATION_SIGNATURE);
		stringRegistry.get(PROPERTY_CLASS_SIGNATURE);
		stringRegistry.get(PROPERTY_CAUSE);

		return stringRegistry;
	}
}
