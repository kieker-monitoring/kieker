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

package kieker.test.common.junit.api.misc;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.misc.KiekerMetadataRecord;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * Tests for kieker.common.record.misc.KiekerMetadataRecord records.
 * 
 * @author Reiner Jung
 * 
 * @since 1.10
 */
public class TestKiekerMetadataRecordPropertyOrder extends AbstractKiekerTest {

	/**
	 * All numbers and values must be pairwise unequal. As the string registry also uses integers,
	 * we must guarantee this criteria by starting with 1000 instead of 0.
	 */
	private static final String PROPERTY_VERSION = "<version>";
	private static final String PROPERTY_CONTROLLER_NAME = "<controllerName>";
	private static final String PROPERTY_HOSTNAME = "<hostname>";
	private static final int PROPERTY_EXPERIMENT_ID = 1001;
	private static final boolean PROPERTY_DEBUG_MODE = true;
	private static final long PROPERTY_TIME_OFFSET = 2L;
	private static final String PROPERTY_TIME_UNIT = "<timeUnit>";
	private static final long PROPERTY_NUMBER_OF_RECORDS = 3L;
							
	/**
	 * Empty constructor.
	 */
	public TestKiekerMetadataRecordPropertyOrder() {}

	/**
	 * Test property order processing of CPUUtilizationRecord constructors.
	 */
	@Test
	public void testKiekerMetadataRecordPropertyOrder() { // NOPMD
		final IRegistry<String> stringRegistry = this.makeStringRegistry();
		final ByteBuffer inputBuffer = this.createByteBuffer(KiekerMetadataRecord.SIZE, 
			this.makeStringRegistry(),
			PROPERTY_VERSION, PROPERTY_CONTROLLER_NAME, PROPERTY_HOSTNAME, PROPERTY_EXPERIMENT_ID, PROPERTY_DEBUG_MODE, PROPERTY_TIME_OFFSET, PROPERTY_TIME_UNIT, PROPERTY_NUMBER_OF_RECORDS);
		final Object[] values = { PROPERTY_VERSION, PROPERTY_CONTROLLER_NAME, PROPERTY_HOSTNAME, PROPERTY_EXPERIMENT_ID, PROPERTY_DEBUG_MODE, PROPERTY_TIME_OFFSET, PROPERTY_TIME_UNIT, PROPERTY_NUMBER_OF_RECORDS };
					
		final KiekerMetadataRecord recordInitParameter = new KiekerMetadataRecord(PROPERTY_VERSION, PROPERTY_CONTROLLER_NAME, PROPERTY_HOSTNAME, PROPERTY_EXPERIMENT_ID, PROPERTY_DEBUG_MODE, PROPERTY_TIME_OFFSET, PROPERTY_TIME_UNIT, PROPERTY_NUMBER_OF_RECORDS);
		final KiekerMetadataRecord recordInitBuffer = new KiekerMetadataRecord(inputBuffer, this.makeStringRegistry());
		final KiekerMetadataRecord recordInitArray = new KiekerMetadataRecord(values);
		
		this.assertKiekerMetadataRecord(recordInitParameter);
		this.assertKiekerMetadataRecord(recordInitBuffer);
		this.assertKiekerMetadataRecord(recordInitArray);

		// test to array
		final Object[] valuesParameter = recordInitParameter.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesParameter);
		final Object[] valuesBuffer = recordInitBuffer.toArray();
		Assert.assertArrayEquals("Result array of record initialized by buffer constructor differs from predefined array.", values, valuesBuffer);
		final Object[] valuesArray = recordInitArray.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesArray);

		// test write to buffer
		final ByteBuffer outputBufferParameter = ByteBuffer.allocate(KiekerMetadataRecord.SIZE);
		recordInitParameter.writeBytes(outputBufferParameter, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (parameter).", inputBuffer.array(), outputBufferParameter.array());

		final ByteBuffer outputBufferBuffer = ByteBuffer.allocate(KiekerMetadataRecord.SIZE);
		recordInitParameter.writeBytes(outputBufferBuffer, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (buffer).", inputBuffer.array(), outputBufferBuffer.array());

		final ByteBuffer outputBufferArray = ByteBuffer.allocate(KiekerMetadataRecord.SIZE);
		recordInitParameter.writeBytes(outputBufferArray, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (array).", inputBuffer.array(), outputBufferArray.array());
	}

	/**
	 * Assertions for KiekerMetadataRecord.
	 */
	private void assertKiekerMetadataRecord(final KiekerMetadataRecord record) {
		Assert.assertEquals("'version' value assertion failed.", record.getVersion(), PROPERTY_VERSION);
		Assert.assertEquals("'controllerName' value assertion failed.", record.getControllerName(), PROPERTY_CONTROLLER_NAME);
		Assert.assertEquals("'hostname' value assertion failed.", record.getHostname(), PROPERTY_HOSTNAME);
		Assert.assertEquals("'experimentId' value assertion failed.", record.getExperimentId(), PROPERTY_EXPERIMENT_ID);
		Assert.assertEquals("'debugMode' value assertion failed.", record.isDebugMode(), PROPERTY_DEBUG_MODE);
		Assert.assertEquals("'timeOffset' value assertion failed.", record.getTimeOffset(), PROPERTY_TIME_OFFSET);
		Assert.assertEquals("'timeUnit' value assertion failed.", record.getTimeUnit(), PROPERTY_TIME_UNIT);
		Assert.assertEquals("'numberOfRecords' value assertion failed.", record.getNumberOfRecords(), PROPERTY_NUMBER_OF_RECORDS);
	}
			
	/**
	 * Build a populated string registry for all tests.
	 */
	private IRegistry<String> makeStringRegistry() {
		final IRegistry<String> stringRegistry = new Registry<String>();
		// get registers string and returns their ID
		stringRegistry.get(PROPERTY_VERSION);
		stringRegistry.get(PROPERTY_CONTROLLER_NAME);
		stringRegistry.get(PROPERTY_HOSTNAME);
		stringRegistry.get(PROPERTY_TIME_UNIT);

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
