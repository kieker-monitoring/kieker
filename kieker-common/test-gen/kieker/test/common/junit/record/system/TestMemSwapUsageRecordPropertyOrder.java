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

import kieker.common.record.system.MemSwapUsageRecord;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.common.junit.util.APIEvaluationFunctions;

/**
 * Test API of {@link kieker.common.record.system.MemSwapUsageRecord}.
 * 
 * @author Reiner Jung
 * 
 * @since 1.10
 */
public class TestMemSwapUsageRecordPropertyOrder extends AbstractKiekerTest {

	/**
	 * All numbers and values must be pairwise unequal. As the string registry also uses integers,
	 * we must guarantee this criteria by starting with 1000 instead of 0.
	 */
	/** Constant value parameter for timestamp. */
	private static final long PROPERTY_TIMESTAMP = 2L;
	/** Constant value parameter for hostname. */
	private static final String PROPERTY_HOSTNAME = "<hostname>";
	/** Constant value parameter for memTotal. */
	private static final long PROPERTY_MEM_TOTAL = 3L;
	/** Constant value parameter for memUsed. */
	private static final long PROPERTY_MEM_USED = 4L;
	/** Constant value parameter for memFree. */
	private static final long PROPERTY_MEM_FREE = 5L;
	/** Constant value parameter for swapTotal. */
	private static final long PROPERTY_SWAP_TOTAL = 6L;
	/** Constant value parameter for swapUsed. */
	private static final long PROPERTY_SWAP_USED = 7L;
	/** Constant value parameter for swapFree. */
	private static final long PROPERTY_SWAP_FREE = 8L;

	/**
	 * Empty constructor.
	 */
	public TestMemSwapUsageRecordPropertyOrder() {
		// Empty constructor for test class.
	}

	/**
	 * Test property order processing of {@link kieker.common.record.system.MemSwapUsageRecord} constructors and
	 * different serialization routines.
	 */
	@Test
	public void testMemSwapUsageRecordPropertyOrder() { // NOPMD
		final IRegistry<String> stringRegistry = this.makeStringRegistry();
		final Object[] values = {
			PROPERTY_TIMESTAMP,
			PROPERTY_HOSTNAME,
			PROPERTY_MEM_TOTAL,
			PROPERTY_MEM_USED,
			PROPERTY_MEM_FREE,
			PROPERTY_SWAP_TOTAL,
			PROPERTY_SWAP_USED,
			PROPERTY_SWAP_FREE,
		};
		final ByteBuffer inputBuffer = APIEvaluationFunctions.createByteBuffer(MemSwapUsageRecord.SIZE,
				this.makeStringRegistry(), values);

		final MemSwapUsageRecord recordInitParameter = new MemSwapUsageRecord(
				PROPERTY_TIMESTAMP,
				PROPERTY_HOSTNAME,
				PROPERTY_MEM_TOTAL,
				PROPERTY_MEM_USED,
				PROPERTY_MEM_FREE,
				PROPERTY_SWAP_TOTAL,
				PROPERTY_SWAP_USED,
				PROPERTY_SWAP_FREE
				);
		final MemSwapUsageRecord recordInitBuffer = new MemSwapUsageRecord(inputBuffer, this.makeStringRegistry());
		final MemSwapUsageRecord recordInitArray = new MemSwapUsageRecord(values);

		this.assertMemSwapUsageRecord(recordInitParameter);
		this.assertMemSwapUsageRecord(recordInitBuffer);
		this.assertMemSwapUsageRecord(recordInitArray);

		// test to array
		final Object[] valuesParameter = recordInitParameter.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesParameter);
		final Object[] valuesBuffer = recordInitBuffer.toArray();
		Assert.assertArrayEquals("Result array of record initialized by buffer constructor differs from predefined array.", values, valuesBuffer);
		final Object[] valuesArray = recordInitArray.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesArray);

		// test write to buffer
		final ByteBuffer outputBufferParameter = ByteBuffer.allocate(MemSwapUsageRecord.SIZE);
		recordInitParameter.writeBytes(outputBufferParameter, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (parameter).", inputBuffer.array(), outputBufferParameter.array());

		final ByteBuffer outputBufferBuffer = ByteBuffer.allocate(MemSwapUsageRecord.SIZE);
		recordInitParameter.writeBytes(outputBufferBuffer, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (buffer).", inputBuffer.array(), outputBufferBuffer.array());

		final ByteBuffer outputBufferArray = ByteBuffer.allocate(MemSwapUsageRecord.SIZE);
		recordInitParameter.writeBytes(outputBufferArray, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (array).", inputBuffer.array(), outputBufferArray.array());
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
	 * Build a populated string registry for all tests.
	 */
	private IRegistry<String> makeStringRegistry() {
		final IRegistry<String> stringRegistry = new Registry<String>();
		// get registers string and returns their ID
		stringRegistry.get(PROPERTY_HOSTNAME);

		return stringRegistry;
	}
}
