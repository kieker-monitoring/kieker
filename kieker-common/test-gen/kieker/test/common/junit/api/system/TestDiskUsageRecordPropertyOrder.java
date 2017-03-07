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

package kieker.test.common.junit.api.system;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.io.DefaultValueDeserializer;
import kieker.common.record.io.DefaultValueSerializer;
import kieker.common.record.system.DiskUsageRecord;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;
import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.common.junit.util.APIEvaluationFunctions;
			
/**
 * Test API of {@link kieker.common.record.system.DiskUsageRecord}.
 * 
 * @author API Checker
 * 
 * @since 1.12
 */
public class TestDiskUsageRecordPropertyOrder extends AbstractKiekerTest {

	/**
	 * All numbers and values must be pairwise unequal. As the string registry also uses integers,
	 * we must guarantee this criteria by starting with 1000 instead of 0.
	 */
	/** Constant value parameter for timestamp. */
	private static final long PROPERTY_TIMESTAMP = 2L;
	/** Constant value parameter for hostname. */
	private static final String PROPERTY_HOSTNAME = "<hostname>";
	/** Constant value parameter for deviceName. */
	private static final String PROPERTY_DEVICE_NAME = "<deviceName>";
	/** Constant value parameter for queue. */
	private static final double PROPERTY_QUEUE = 2.0;
	/** Constant value parameter for readBytesPerSecond. */
	private static final double PROPERTY_READ_BYTES_PER_SECOND = 3.0;
	/** Constant value parameter for readsPerSecond. */
	private static final double PROPERTY_READS_PER_SECOND = 4.0;
	/** Constant value parameter for serviceTime. */
	private static final double PROPERTY_SERVICE_TIME = 5.0;
	/** Constant value parameter for writeBytesPerSecond. */
	private static final double PROPERTY_WRITE_BYTES_PER_SECOND = 6.0;
	/** Constant value parameter for writesPerSecond. */
	private static final double PROPERTY_WRITES_PER_SECOND = 7.0;
							
	/**
	 * Empty constructor.
	 */
	public TestDiskUsageRecordPropertyOrder() {
		// Empty constructor for test class.
	}

	/**
	 * Test property order processing of {@link kieker.common.record.system.DiskUsageRecord} constructors and
	 * different serialization routines.
	 */
	@Test
	public void testDiskUsageRecordPropertyOrder() { // NOPMD
		final IRegistry<String> stringRegistry = this.makeStringRegistry();
		final Object[] values = {
			PROPERTY_TIMESTAMP,
			PROPERTY_HOSTNAME,
			PROPERTY_DEVICE_NAME,
			PROPERTY_QUEUE,
			PROPERTY_READ_BYTES_PER_SECOND,
			PROPERTY_READS_PER_SECOND,
			PROPERTY_SERVICE_TIME,
			PROPERTY_WRITE_BYTES_PER_SECOND,
			PROPERTY_WRITES_PER_SECOND,
		};
		final ByteBuffer inputBuffer = APIEvaluationFunctions.createByteBuffer(DiskUsageRecord.SIZE, 
			this.makeStringRegistry(), values);
					
		final DiskUsageRecord recordInitParameter = new DiskUsageRecord(
			PROPERTY_TIMESTAMP,
			PROPERTY_HOSTNAME,
			PROPERTY_DEVICE_NAME,
			PROPERTY_QUEUE,
			PROPERTY_READ_BYTES_PER_SECOND,
			PROPERTY_READS_PER_SECOND,
			PROPERTY_SERVICE_TIME,
			PROPERTY_WRITE_BYTES_PER_SECOND,
			PROPERTY_WRITES_PER_SECOND
		);
		final DiskUsageRecord recordInitBuffer = new DiskUsageRecord(DefaultValueDeserializer.instance(), inputBuffer, this.makeStringRegistry());
		final DiskUsageRecord recordInitArray = new DiskUsageRecord(values);
		
		this.assertDiskUsageRecord(recordInitParameter);
		this.assertDiskUsageRecord(recordInitBuffer);
		this.assertDiskUsageRecord(recordInitArray);

		// test to array
		final Object[] valuesParameter = recordInitParameter.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesParameter);
		final Object[] valuesBuffer = recordInitBuffer.toArray();
		Assert.assertArrayEquals("Result array of record initialized by buffer constructor differs from predefined array.", values, valuesBuffer);
		final Object[] valuesArray = recordInitArray.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesArray);

		// test write to buffer
		final ByteBuffer outputBufferParameter = ByteBuffer.allocate(DiskUsageRecord.SIZE);
		recordInitParameter.writeBytes(DefaultValueSerializer.instance(), outputBufferParameter, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (parameter).", inputBuffer.array(), outputBufferParameter.array());

		final ByteBuffer outputBufferBuffer = ByteBuffer.allocate(DiskUsageRecord.SIZE);
		recordInitParameter.writeBytes(DefaultValueSerializer.instance(), outputBufferBuffer, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (buffer).", inputBuffer.array(), outputBufferBuffer.array());

		final ByteBuffer outputBufferArray = ByteBuffer.allocate(DiskUsageRecord.SIZE);
		recordInitParameter.writeBytes(DefaultValueSerializer.instance(), outputBufferArray, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (array).", inputBuffer.array(), outputBufferArray.array());
	}

	/**
	 * Assertions for DiskUsageRecord.
	 */
	private void assertDiskUsageRecord(final DiskUsageRecord record) {
		Assert.assertEquals("'timestamp' value assertion failed.", record.getTimestamp(), PROPERTY_TIMESTAMP);
		Assert.assertEquals("'hostname' value assertion failed.", record.getHostname(), PROPERTY_HOSTNAME);
		Assert.assertEquals("'deviceName' value assertion failed.", record.getDeviceName(), PROPERTY_DEVICE_NAME);
		Assert.assertEquals("'queue' value assertion failed.", record.getQueue(), PROPERTY_QUEUE, 0.1);
		Assert.assertEquals("'readBytesPerSecond' value assertion failed.", record.getReadBytesPerSecond(), PROPERTY_READ_BYTES_PER_SECOND, 0.1);
		Assert.assertEquals("'readsPerSecond' value assertion failed.", record.getReadsPerSecond(), PROPERTY_READS_PER_SECOND, 0.1);
		Assert.assertEquals("'serviceTime' value assertion failed.", record.getServiceTime(), PROPERTY_SERVICE_TIME, 0.1);
		Assert.assertEquals("'writeBytesPerSecond' value assertion failed.", record.getWriteBytesPerSecond(), PROPERTY_WRITE_BYTES_PER_SECOND, 0.1);
		Assert.assertEquals("'writesPerSecond' value assertion failed.", record.getWritesPerSecond(), PROPERTY_WRITES_PER_SECOND, 0.1);
	}
			
	/**
	 * Build a populated string registry for all tests.
	 */
	private IRegistry<String> makeStringRegistry() {
		final IRegistry<String> stringRegistry = new Registry<String>();
		// get registers string and returns their ID
		stringRegistry.get(PROPERTY_HOSTNAME);
		stringRegistry.get(PROPERTY_DEVICE_NAME);

		return stringRegistry;
	}
}
