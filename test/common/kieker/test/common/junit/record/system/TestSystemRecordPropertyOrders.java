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

package kieker.test.common.junit.record.system;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.system.CPUUtilizationRecord;
import kieker.common.record.system.MemSwapUsageRecord;
import kieker.common.record.system.ResourceUtilizationRecord;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * Tests for System.irl records.
 * 
 * @author Reiner Jung
 * 
 * @since 1.10
 */
public class TestSystemRecordPropertyOrders extends AbstractKiekerTest {

	/**
	 * All numbers and values must be pairwise unequal. As the string registry also uses integers,
	 * we must guarantee this criteria by starting with 1000 instead of 0.
	 */
	private static final long PROPERTY_TIMESTAMP = 1000;
	private static final String PROPERTY_HOSTNAME = "<hostname>";
	private static final String PROPERTY_CPU_ID = "<cpu id>";
	private static final double PROPERTY_USER = 1.0;
	private static final double PROPERTY_SYSTEM = 2.0;
	private static final double PROPERTY_WAIT = 3.0;
	private static final double PROPERTY_NICE = 4.0;
	private static final double PROPERTY_IRQ = 5.0;
	private static final double PROPERTY_TOTAL_UTILIZATION = 6.0;
	private static final double PROPERTY_IDLE = 7.0;

	private static final long PROPERTY_MEM_TOTAL = 1001;
	private static final long PROPERTY_MEM_USED = 1002;
	private static final long PROPERTY_MEM_FREE = 1003;

	private static final long PROPERTY_SWAP_TOTAL = 1004;
	private static final long PROPERTY_SWAP_USED = 1005;
	private static final long PROPERTY_SWAP_FREE = 1006;

	private static final String PROPERTY_RESOURCE_NAME = "<resource name>";
	private static final double PROPERTY_UTILIZATION = 8.0;

	/**
	 * Empty constructor.
	 */
	public TestSystemRecordPropertyOrders() {}

	/**
	 * Test property order processing of CPUUtilizationRecord constructors.
	 */
	@Test
	public void testCPUUtilizationRecordPropertyOrder() { // NOPMD
		this.assertCPUUtilizationRecord(new CPUUtilizationRecord(PROPERTY_TIMESTAMP, PROPERTY_HOSTNAME, PROPERTY_CPU_ID, PROPERTY_USER,
				PROPERTY_SYSTEM, PROPERTY_WAIT, PROPERTY_NICE, PROPERTY_IRQ, PROPERTY_TOTAL_UTILIZATION, PROPERTY_IDLE));

		final ByteBuffer buffer = this.createByteBuffer(CPUUtilizationRecord.SIZE, this.makeStringRegistry(),
				PROPERTY_TIMESTAMP, PROPERTY_HOSTNAME, PROPERTY_CPU_ID, PROPERTY_USER,
				PROPERTY_SYSTEM, PROPERTY_WAIT, PROPERTY_NICE, PROPERTY_IRQ,
				PROPERTY_TOTAL_UTILIZATION, PROPERTY_IDLE);

		this.assertCPUUtilizationRecord(new CPUUtilizationRecord(buffer, this.makeStringRegistry()));
	}

	/**
	 * Test property order processing of MemSwapUsageRecord constructors.
	 */
	@Test
	public void testMemSwapUsageRecordPropertyOrder() { // NOPMD
		this.assertMemSwapUsageRecord(new MemSwapUsageRecord(PROPERTY_TIMESTAMP, PROPERTY_HOSTNAME,
				PROPERTY_MEM_TOTAL, PROPERTY_MEM_USED, PROPERTY_MEM_FREE,
				PROPERTY_SWAP_TOTAL, PROPERTY_SWAP_USED, PROPERTY_SWAP_FREE));

		final ByteBuffer buffer = this.createByteBuffer(MemSwapUsageRecord.SIZE, this.makeStringRegistry(),
				PROPERTY_TIMESTAMP, PROPERTY_HOSTNAME,
				PROPERTY_MEM_TOTAL, PROPERTY_MEM_USED, PROPERTY_MEM_FREE,
				PROPERTY_SWAP_TOTAL, PROPERTY_SWAP_USED, PROPERTY_SWAP_FREE);

		this.assertMemSwapUsageRecord(new MemSwapUsageRecord(buffer, this.makeStringRegistry()));
	}

	/**
	 * Test property order processing of ResourceUtilizationRecord constructors.
	 */
	@Test
	public void testResourceUtilizationRecordPropertyOrder() { // NOPMD
		this.assertResourceUtilizationRecord(new ResourceUtilizationRecord(PROPERTY_TIMESTAMP, PROPERTY_HOSTNAME,
				PROPERTY_RESOURCE_NAME, PROPERTY_UTILIZATION));

		final ByteBuffer buffer = this.createByteBuffer(ResourceUtilizationRecord.SIZE, this.makeStringRegistry(),
				PROPERTY_TIMESTAMP, PROPERTY_HOSTNAME,
				PROPERTY_RESOURCE_NAME, PROPERTY_UTILIZATION);

		this.assertResourceUtilizationRecord(new ResourceUtilizationRecord(buffer, this.makeStringRegistry()));
	}

	/**
	 * Assertions for record types.
	 */

	/**
	 * Assertions for CPUUtilizationRecord.
	 */
	private void assertCPUUtilizationRecord(final CPUUtilizationRecord record) {
		Assert.assertEquals("'timestamp' value assertion failed.", record.getTimestamp(), PROPERTY_TIMESTAMP);
		Assert.assertEquals("'hostname' value assertion failed.", record.getHostname(), PROPERTY_HOSTNAME);

		Assert.assertEquals("'cupID' value assertion failed.", record.getCpuID(), PROPERTY_CPU_ID);
		Assert.assertEquals("'user' value assertion failed.", record.getUser(), PROPERTY_USER, 0.1);

		Assert.assertEquals("'system' value assertion failed.", record.getSystem(), PROPERTY_SYSTEM, 0.1);
		Assert.assertEquals("'wait' value assertion failed.", record.getWait(), PROPERTY_WAIT, 0.1);
		Assert.assertEquals("'nice' value assertion failed.", record.getNice(), PROPERTY_NICE, 0.1);
		Assert.assertEquals("'icq' value assertion failed.", record.getIrq(), PROPERTY_IRQ, 0.1);

		Assert.assertEquals("'totalUtilization' value assertion failed.", record.getTotalUtilization(), PROPERTY_TOTAL_UTILIZATION, 0.1);
		Assert.assertEquals("'idle' value assertion failed.", record.getIdle(), PROPERTY_IDLE, 0.1);
	}

	/**
	 * Assertions for MemSwapUsageRecord.
	 */
	private void assertMemSwapUsageRecord(final MemSwapUsageRecord record) {
		Assert.assertEquals("'timestamp' value assertion failed.", record.getTimestamp(), PROPERTY_TIMESTAMP);
		Assert.assertEquals("'hostname' value assertion failed.", record.getHostname(), PROPERTY_HOSTNAME);

		Assert.assertEquals("'memTotal' value assertion failed.", record.getMemTotal(), PROPERTY_MEM_TOTAL);
		Assert.assertEquals("'memUsed' value assertion failed.", record.getMemUsed(), PROPERTY_MEM_USED);
		Assert.assertEquals("'memFree' value assertion failed.", record.getMemFree(), PROPERTY_MEM_FREE);

		Assert.assertEquals("'swapTotal' value assertion failed.", record.getSwapTotal(), PROPERTY_SWAP_TOTAL);
		Assert.assertEquals("'swapUsed' value assertion failed.", record.getSwapUsed(), PROPERTY_SWAP_USED);
		Assert.assertEquals("'swapFree' value assertion failed.", record.getSwapFree(), PROPERTY_SWAP_FREE);
	}

	/**
	 * Assertions for ResourceUtilizationRecord.
	 */
	private void assertResourceUtilizationRecord(final ResourceUtilizationRecord record) {
		Assert.assertEquals("'timestamp' value assertion failed.", record.getTimestamp(), PROPERTY_TIMESTAMP);
		Assert.assertEquals("'hostname' value assertion failed.", record.getHostname(), PROPERTY_HOSTNAME);

		Assert.assertEquals("'resourceName' value assertion failed.", record.getResourceName(), PROPERTY_RESOURCE_NAME);
		Assert.assertEquals("'Utilization' value assertion failed.", record.getUtilization(), PROPERTY_UTILIZATION, 0.1);
	}

	/**
	 * Build a populated string registry for all tests.
	 */
	private IRegistry<String> makeStringRegistry() {
		final IRegistry<String> stringRegistry = new Registry<String>();
		// get registers string and returns their ID
		stringRegistry.get(PROPERTY_HOSTNAME);
		stringRegistry.get(PROPERTY_CPU_ID);
		stringRegistry.get(PROPERTY_RESOURCE_NAME);

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
