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

package kieker.test.common.junit.api.flow.trace.operation.constructor;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.flow.trace.operation.constructor.BeforeConstructorEvent;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * Tests for kieker.common.record.flow.trace.operation.constructor.BeforeConstructorEvent records.
 * 
 * @author Reiner Jung
 * 
 * @since 1.10
 */
public class TestBeforeConstructorEventPropertyOrder extends AbstractKiekerTest {

	/**
	 * All numbers and values must be pairwise unequal. As the string registry also uses integers,
	 * we must guarantee this criteria by starting with 1000 instead of 0.
	 */
	private static final long PROPERTY_TIMESTAMP = 2L;
	private static final long PROPERTY_TRACE_ID = 3L;
	private static final int PROPERTY_ORDER_INDEX = 1001;
	private static final String PROPERTY_OPERATION_SIGNATURE = "<operationSignature>";
	private static final String PROPERTY_CLASS_SIGNATURE = "<classSignature>";
							
	/**
	 * Empty constructor.
	 */
	public TestBeforeConstructorEventPropertyOrder() {}

	/**
	 * Test property order processing of CPUUtilizationRecord constructors.
	 */
	@Test
	public void testBeforeConstructorEventPropertyOrder() { // NOPMD
		final IRegistry<String> stringRegistry = this.makeStringRegistry();
		final ByteBuffer inputBuffer = this.createByteBuffer(BeforeConstructorEvent.SIZE, 
			this.makeStringRegistry(),
			PROPERTY_TIMESTAMP, PROPERTY_TRACE_ID, PROPERTY_ORDER_INDEX, PROPERTY_OPERATION_SIGNATURE, PROPERTY_CLASS_SIGNATURE);
		final Object[] values = { PROPERTY_TIMESTAMP, PROPERTY_TRACE_ID, PROPERTY_ORDER_INDEX, PROPERTY_OPERATION_SIGNATURE, PROPERTY_CLASS_SIGNATURE };
					
		final BeforeConstructorEvent recordInitParameter = new BeforeConstructorEvent(PROPERTY_TIMESTAMP, PROPERTY_TRACE_ID, PROPERTY_ORDER_INDEX, PROPERTY_OPERATION_SIGNATURE, PROPERTY_CLASS_SIGNATURE);
		final BeforeConstructorEvent recordInitBuffer = new BeforeConstructorEvent(inputBuffer, this.makeStringRegistry());
		final BeforeConstructorEvent recordInitArray = new BeforeConstructorEvent(values);
		
		this.assertBeforeConstructorEvent(recordInitParameter);
		this.assertBeforeConstructorEvent(recordInitBuffer);
		this.assertBeforeConstructorEvent(recordInitArray);

		// test to array
		final Object[] valuesParameter = recordInitParameter.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesParameter);
		final Object[] valuesBuffer = recordInitBuffer.toArray();
		Assert.assertArrayEquals("Result array of record initialized by buffer constructor differs from predefined array.", values, valuesBuffer);
		final Object[] valuesArray = recordInitArray.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesArray);

		// test write to buffer
		final ByteBuffer outputBufferParameter = ByteBuffer.allocate(BeforeConstructorEvent.SIZE);
		recordInitParameter.writeBytes(outputBufferParameter, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (parameter).", inputBuffer.array(), outputBufferParameter.array());

		final ByteBuffer outputBufferBuffer = ByteBuffer.allocate(BeforeConstructorEvent.SIZE);
		recordInitParameter.writeBytes(outputBufferBuffer, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (buffer).", inputBuffer.array(), outputBufferBuffer.array());

		final ByteBuffer outputBufferArray = ByteBuffer.allocate(BeforeConstructorEvent.SIZE);
		recordInitParameter.writeBytes(outputBufferArray, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (array).", inputBuffer.array(), outputBufferArray.array());
	}

	/**
	 * Assertions for BeforeConstructorEvent.
	 */
	private void assertBeforeConstructorEvent(final BeforeConstructorEvent record) {
		Assert.assertEquals("'timestamp' value assertion failed.", record.getTimestamp(), PROPERTY_TIMESTAMP);
		Assert.assertEquals("'traceId' value assertion failed.", record.getTraceId(), PROPERTY_TRACE_ID);
		Assert.assertEquals("'orderIndex' value assertion failed.", record.getOrderIndex(), PROPERTY_ORDER_INDEX);
		Assert.assertEquals("'operationSignature' value assertion failed.", record.getOperationSignature(), PROPERTY_OPERATION_SIGNATURE);
		Assert.assertEquals("'classSignature' value assertion failed.", record.getClassSignature(), PROPERTY_CLASS_SIGNATURE);
	}
			
	/**
	 * Build a populated string registry for all tests.
	 */
	private IRegistry<String> makeStringRegistry() {
		final IRegistry<String> stringRegistry = new Registry<String>();
		// get registers string and returns their ID
		stringRegistry.get(PROPERTY_OPERATION_SIGNATURE);
		stringRegistry.get(PROPERTY_CLASS_SIGNATURE);

		return stringRegistry;
	}

	/**
	 * Compose a byte buffer for record deserialization.
	 * 
	 * @param size
	 *            the size of the serialized record
	 * @param stringRegistry
	 *            the registry for the string lookup
	 * @param a
	 *            list of "objects" containing an arbitrary number of records
	 * 
	 * @return the completely filled buffer
	 */
	private ByteBuffer createByteBuffer(final int size, final IRegistry<String> stringRegistry, final Object... objects) {
		final ByteBuffer buffer = ByteBuffer.allocate(size);
		for (final Object object : objects) {
			if (object instanceof Byte) {
				buffer.put((Byte) object);
			} else if (object instanceof Short) {
				buffer.putShort((Short) object);
			} else if (object instanceof Integer) {
				buffer.putInt((Integer) object);
			} else if (object instanceof Long) {
				buffer.putLong((Long) object);
			} else if (object instanceof Float) {
				buffer.putFloat((Float) object);
			} else if (object instanceof Double) {
				buffer.putDouble((Double) object);
			} else if (object instanceof Boolean) {
				buffer.put((byte) ((Boolean) object ? 1 : 0)); // NOCS
			} else if (object instanceof Character) {
				buffer.putChar((Character) object);
			} else if (object instanceof String) {
				buffer.putInt(stringRegistry.get((String) object));
			} else {
				Assert.fail("Unsupported record value type " + object.getClass().getName());
			}
		}

		Assert.assertEquals("Buffer size and usage differ.", buffer.position(), size);
		buffer.position(0);

		return buffer;
	}
}
