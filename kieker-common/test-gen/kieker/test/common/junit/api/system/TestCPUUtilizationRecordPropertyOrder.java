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

package kieker.test.common.junit.api.system;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.system.CPUUtilizationRecord;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * Tests for kieker.common.record.system.CPUUtilizationRecord records.
 * 
 * @author Reiner Jung
 * 
 * @since 1.10
 */
public class TestCPUUtilizationRecordPropertyOrder extends AbstractKiekerTest {

	/**
	 * All numbers and values must be pairwise unequal. As the string registry also uses integers,
	 * we must guarantee this criteria by starting with 1000 instead of 0.
	 */
	private static final long PROPERTY_TIMESTAMP = 2L;
	private static final String PROPERTY_HOSTNAME = "<hostname>";
	private static final String PROPERTY_CPU_I_D = "<cpuID>";
	private static final double PROPERTY_USER = 2.0;
	private static final double PROPERTY_SYSTEM = 3.0;
	private static final double PROPERTY_WAIT = 4.0;
	private static final double PROPERTY_NICE = 5.0;
	private static final double PROPERTY_IRQ = 6.0;
	private static final double PROPERTY_TOTAL_UTILIZATION = 7.0;
	private static final double PROPERTY_IDLE = 8.0;
							
	/**
	 * Empty constructor.
	 */
	public TestCPUUtilizationRecordPropertyOrder() {}

	/**
	 * Test property order processing of CPUUtilizationRecord constructors.
	 */
	@Test
	public void testCPUUtilizationRecordPropertyOrder() { // NOPMD
		final IRegistry<String> stringRegistry = this.makeStringRegistry();
		final ByteBuffer inputBuffer = this.createByteBuffer(CPUUtilizationRecord.SIZE, 
			this.makeStringRegistry(),
			PROPERTY_TIMESTAMP, PROPERTY_HOSTNAME, PROPERTY_CPU_I_D, PROPERTY_USER, PROPERTY_SYSTEM, PROPERTY_WAIT, PROPERTY_NICE, PROPERTY_IRQ, PROPERTY_TOTAL_UTILIZATION, PROPERTY_IDLE);
		final Object[] values = { PROPERTY_TIMESTAMP, PROPERTY_HOSTNAME, PROPERTY_CPU_I_D, PROPERTY_USER, PROPERTY_SYSTEM, PROPERTY_WAIT, PROPERTY_NICE, PROPERTY_IRQ, PROPERTY_TOTAL_UTILIZATION, PROPERTY_IDLE };
					
		final CPUUtilizationRecord recordInitParameter = new CPUUtilizationRecord(PROPERTY_TIMESTAMP, PROPERTY_HOSTNAME, PROPERTY_CPU_I_D, PROPERTY_USER, PROPERTY_SYSTEM, PROPERTY_WAIT, PROPERTY_NICE, PROPERTY_IRQ, PROPERTY_TOTAL_UTILIZATION, PROPERTY_IDLE);
		final CPUUtilizationRecord recordInitBuffer = new CPUUtilizationRecord(inputBuffer, this.makeStringRegistry());
		final CPUUtilizationRecord recordInitArray = new CPUUtilizationRecord(values);
		
		this.assertCPUUtilizationRecord(recordInitParameter);
		this.assertCPUUtilizationRecord(recordInitBuffer);
		this.assertCPUUtilizationRecord(recordInitArray);

		// test to array
		final Object[] valuesParameter = recordInitParameter.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesParameter);
		final Object[] valuesBuffer = recordInitBuffer.toArray();
		Assert.assertArrayEquals("Result array of record initialized by buffer constructor differs from predefined array.", values, valuesBuffer);
		final Object[] valuesArray = recordInitArray.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesArray);

		// test write to buffer
		final ByteBuffer outputBufferParameter = ByteBuffer.allocate(CPUUtilizationRecord.SIZE);
		recordInitParameter.writeBytes(outputBufferParameter, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (parameter).", inputBuffer.array(), outputBufferParameter.array());

		final ByteBuffer outputBufferBuffer = ByteBuffer.allocate(CPUUtilizationRecord.SIZE);
		recordInitParameter.writeBytes(outputBufferBuffer, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (buffer).", inputBuffer.array(), outputBufferBuffer.array());

		final ByteBuffer outputBufferArray = ByteBuffer.allocate(CPUUtilizationRecord.SIZE);
		recordInitParameter.writeBytes(outputBufferArray, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (array).", inputBuffer.array(), outputBufferArray.array());
	}

	/**
	 * Assertions for CPUUtilizationRecord.
	 */
	private void assertCPUUtilizationRecord(final CPUUtilizationRecord record) {
		Assert.assertEquals("'timestamp' value assertion failed.", record.getTimestamp(), PROPERTY_TIMESTAMP);
		Assert.assertEquals("'hostname' value assertion failed.", record.getHostname(), PROPERTY_HOSTNAME);
		Assert.assertEquals("'cpuID' value assertion failed.", record.getCpuID(), PROPERTY_CPU_I_D);
		Assert.assertEquals("'user' value assertion failed.", record.getUser(), PROPERTY_USER, 0.1);
		Assert.assertEquals("'system' value assertion failed.", record.getSystem(), PROPERTY_SYSTEM, 0.1);
		Assert.assertEquals("'wait' value assertion failed.", record.getWait(), PROPERTY_WAIT, 0.1);
		Assert.assertEquals("'nice' value assertion failed.", record.getNice(), PROPERTY_NICE, 0.1);
		Assert.assertEquals("'irq' value assertion failed.", record.getIrq(), PROPERTY_IRQ, 0.1);
		Assert.assertEquals("'totalUtilization' value assertion failed.", record.getTotalUtilization(), PROPERTY_TOTAL_UTILIZATION, 0.1);
		Assert.assertEquals("'idle' value assertion failed.", record.getIdle(), PROPERTY_IDLE, 0.1);
	}
			
	/**
	 * Build a populated string registry for all tests.
	 */
	private IRegistry<String> makeStringRegistry() {
		final IRegistry<String> stringRegistry = new Registry<String>();
		// get registers string and returns their ID
		stringRegistry.get(PROPERTY_HOSTNAME);
		stringRegistry.get(PROPERTY_CPU_I_D);

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
