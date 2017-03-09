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

package kieker.test.common.junit.api.jvm;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.io.DefaultValueDeserializer;
import kieker.common.record.io.DefaultValueSerializer;
import kieker.common.record.jvm.MemoryRecord;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.common.junit.util.APIEvaluationFunctions;
			
/**
 * Test API of {@link kieker.common.record.jvm.MemoryRecord}.
 * 
 * @author API Checker
 * 
 * @since 1.12
 */
public class TestMemoryRecordPropertyOrder extends AbstractKiekerTest {

	/**
	 * All numbers and values must be pairwise unequal. As the string registry also uses integers,
	 * we must guarantee this criteria by starting with 1000 instead of 0.
	 */
	/** Constant value parameter for timestamp. */
	private static final long PROPERTY_TIMESTAMP = 2L;
	/** Constant value parameter for hostname. */
	private static final String PROPERTY_HOSTNAME = "<hostname>";
	/** Constant value parameter for vmName. */
	private static final String PROPERTY_VM_NAME = "<vmName>";
	/** Constant value parameter for heapMaxBytes. */
	private static final long PROPERTY_HEAP_MAX_BYTES = 3L;
	/** Constant value parameter for heapUsedBytes. */
	private static final long PROPERTY_HEAP_USED_BYTES = 4L;
	/** Constant value parameter for heapCommittedBytes. */
	private static final long PROPERTY_HEAP_COMMITTED_BYTES = 5L;
	/** Constant value parameter for heapInitBytes. */
	private static final long PROPERTY_HEAP_INIT_BYTES = 6L;
	/** Constant value parameter for nonHeapMaxBytes. */
	private static final long PROPERTY_NON_HEAP_MAX_BYTES = 7L;
	/** Constant value parameter for nonHeapUsedBytes. */
	private static final long PROPERTY_NON_HEAP_USED_BYTES = 8L;
	/** Constant value parameter for nonHeapCommittedBytes. */
	private static final long PROPERTY_NON_HEAP_COMMITTED_BYTES = 9L;
	/** Constant value parameter for nonHeapInitBytes. */
	private static final long PROPERTY_NON_HEAP_INIT_BYTES = 10L;
	/** Constant value parameter for objectPendingFinalizationCount. */
	private static final int PROPERTY_OBJECT_PENDING_FINALIZATION_COUNT = 1001;
							
	/**
	 * Empty constructor.
	 */
	public TestMemoryRecordPropertyOrder() {
		// Empty constructor for test class.
	}

	/**
	 * Test property order processing of {@link kieker.common.record.jvm.MemoryRecord} constructors and
	 * different serialization routines.
	 */
	@Test
	public void testMemoryRecordPropertyOrder() { // NOPMD
		final IRegistry<String> stringRegistry = this.makeStringRegistry();
		final Object[] values = {
			PROPERTY_TIMESTAMP,
			PROPERTY_HOSTNAME,
			PROPERTY_VM_NAME,
			PROPERTY_HEAP_MAX_BYTES,
			PROPERTY_HEAP_USED_BYTES,
			PROPERTY_HEAP_COMMITTED_BYTES,
			PROPERTY_HEAP_INIT_BYTES,
			PROPERTY_NON_HEAP_MAX_BYTES,
			PROPERTY_NON_HEAP_USED_BYTES,
			PROPERTY_NON_HEAP_COMMITTED_BYTES,
			PROPERTY_NON_HEAP_INIT_BYTES,
			PROPERTY_OBJECT_PENDING_FINALIZATION_COUNT,
		};
		final ByteBuffer inputBuffer = APIEvaluationFunctions.createByteBuffer(MemoryRecord.SIZE, 
			this.makeStringRegistry(), values);
					
		final MemoryRecord recordInitParameter = new MemoryRecord(
			PROPERTY_TIMESTAMP,
			PROPERTY_HOSTNAME,
			PROPERTY_VM_NAME,
			PROPERTY_HEAP_MAX_BYTES,
			PROPERTY_HEAP_USED_BYTES,
			PROPERTY_HEAP_COMMITTED_BYTES,
			PROPERTY_HEAP_INIT_BYTES,
			PROPERTY_NON_HEAP_MAX_BYTES,
			PROPERTY_NON_HEAP_USED_BYTES,
			PROPERTY_NON_HEAP_COMMITTED_BYTES,
			PROPERTY_NON_HEAP_INIT_BYTES,
			PROPERTY_OBJECT_PENDING_FINALIZATION_COUNT
		);
		final MemoryRecord recordInitBuffer = new MemoryRecord(DefaultValueDeserializer.instance(), inputBuffer, this.makeStringRegistry());
		final MemoryRecord recordInitArray = new MemoryRecord(values);
		
		this.assertMemoryRecord(recordInitParameter);
		this.assertMemoryRecord(recordInitBuffer);
		this.assertMemoryRecord(recordInitArray);

		// test to array
		final Object[] valuesParameter = recordInitParameter.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesParameter);
		final Object[] valuesBuffer = recordInitBuffer.toArray();
		Assert.assertArrayEquals("Result array of record initialized by buffer constructor differs from predefined array.", values, valuesBuffer);
		final Object[] valuesArray = recordInitArray.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesArray);

		// test write to buffer
		final ByteBuffer outputBufferParameter = ByteBuffer.allocate(MemoryRecord.SIZE);
		recordInitParameter.writeBytes(DefaultValueSerializer.instance(), outputBufferParameter, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (parameter).", inputBuffer.array(), outputBufferParameter.array());

		final ByteBuffer outputBufferBuffer = ByteBuffer.allocate(MemoryRecord.SIZE);
		recordInitParameter.writeBytes(DefaultValueSerializer.instance(), outputBufferBuffer, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (buffer).", inputBuffer.array(), outputBufferBuffer.array());

		final ByteBuffer outputBufferArray = ByteBuffer.allocate(MemoryRecord.SIZE);
		recordInitParameter.writeBytes(DefaultValueSerializer.instance(), outputBufferArray, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (array).", inputBuffer.array(), outputBufferArray.array());
	}

	/**
	 * Assertions for MemoryRecord.
	 */
	private void assertMemoryRecord(final MemoryRecord record) {
		Assert.assertEquals("'timestamp' value assertion failed.", record.getTimestamp(), PROPERTY_TIMESTAMP);
		Assert.assertEquals("'hostname' value assertion failed.", record.getHostname(), PROPERTY_HOSTNAME);
		Assert.assertEquals("'vmName' value assertion failed.", record.getVmName(), PROPERTY_VM_NAME);
		Assert.assertEquals("'heapMaxBytes' value assertion failed.", record.getHeapMaxBytes(), PROPERTY_HEAP_MAX_BYTES);
		Assert.assertEquals("'heapUsedBytes' value assertion failed.", record.getHeapUsedBytes(), PROPERTY_HEAP_USED_BYTES);
		Assert.assertEquals("'heapCommittedBytes' value assertion failed.", record.getHeapCommittedBytes(), PROPERTY_HEAP_COMMITTED_BYTES);
		Assert.assertEquals("'heapInitBytes' value assertion failed.", record.getHeapInitBytes(), PROPERTY_HEAP_INIT_BYTES);
		Assert.assertEquals("'nonHeapMaxBytes' value assertion failed.", record.getNonHeapMaxBytes(), PROPERTY_NON_HEAP_MAX_BYTES);
		Assert.assertEquals("'nonHeapUsedBytes' value assertion failed.", record.getNonHeapUsedBytes(), PROPERTY_NON_HEAP_USED_BYTES);
		Assert.assertEquals("'nonHeapCommittedBytes' value assertion failed.", record.getNonHeapCommittedBytes(), PROPERTY_NON_HEAP_COMMITTED_BYTES);
		Assert.assertEquals("'nonHeapInitBytes' value assertion failed.", record.getNonHeapInitBytes(), PROPERTY_NON_HEAP_INIT_BYTES);
		Assert.assertEquals("'objectPendingFinalizationCount' value assertion failed.", record.getObjectPendingFinalizationCount(), PROPERTY_OBJECT_PENDING_FINALIZATION_COUNT);
	}
			
	/**
	 * Build a populated string registry for all tests.
	 */
	private IRegistry<String> makeStringRegistry() {
		final IRegistry<String> stringRegistry = new Registry<String>();
		// get registers string and returns their ID
		stringRegistry.get(PROPERTY_HOSTNAME);
		stringRegistry.get(PROPERTY_VM_NAME);

		return stringRegistry;
	}
}
