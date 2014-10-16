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

package kieker.test.common.junit.record.misc;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.misc.TimestampRecord;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.common.junit.util.APIEvaluationFunctions;

/**
 * Test API of {@link kieker.common.record.misc.TimestampRecord}.
 * 
 * @author Reiner Jung
 * 
 * @since 1.10
 */
public class TestTimestampRecordPropertyOrder extends AbstractKiekerTest {

	/**
	 * All numbers and values must be pairwise unequal. As the string registry also uses integers,
	 * we must guarantee this criteria by starting with 1000 instead of 0.
	 */
	/** Constant value parameter for timestamp. */
	private static final long PROPERTY_TIMESTAMP = 2L;

	/**
	 * Empty constructor.
	 */
	public TestTimestampRecordPropertyOrder() {
		// Empty constructor for test class.
	}

	/**
	 * Test property order processing of {@link kieker.common.record.misc.TimestampRecord} constructors and
	 * different serialization routines.
	 */
	@Test
	public void testTimestampRecordPropertyOrder() { // NOPMD
		final IRegistry<String> stringRegistry = this.makeStringRegistry();
		final Object[] values = {
			PROPERTY_TIMESTAMP,
		};
		final ByteBuffer inputBuffer = APIEvaluationFunctions.createByteBuffer(TimestampRecord.SIZE,
				this.makeStringRegistry(), values);

		final TimestampRecord recordInitParameter = new TimestampRecord(
				PROPERTY_TIMESTAMP
				);
		final TimestampRecord recordInitBuffer = new TimestampRecord(inputBuffer, this.makeStringRegistry());
		final TimestampRecord recordInitArray = new TimestampRecord(values);

		this.assertTimestampRecord(recordInitParameter);
		this.assertTimestampRecord(recordInitBuffer);
		this.assertTimestampRecord(recordInitArray);

		// test to array
		final Object[] valuesParameter = recordInitParameter.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesParameter);
		final Object[] valuesBuffer = recordInitBuffer.toArray();
		Assert.assertArrayEquals("Result array of record initialized by buffer constructor differs from predefined array.", values, valuesBuffer);
		final Object[] valuesArray = recordInitArray.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesArray);

		// test write to buffer
		final ByteBuffer outputBufferParameter = ByteBuffer.allocate(TimestampRecord.SIZE);
		recordInitParameter.writeBytes(outputBufferParameter, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (parameter).", inputBuffer.array(), outputBufferParameter.array());

		final ByteBuffer outputBufferBuffer = ByteBuffer.allocate(TimestampRecord.SIZE);
		recordInitParameter.writeBytes(outputBufferBuffer, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (buffer).", inputBuffer.array(), outputBufferBuffer.array());

		final ByteBuffer outputBufferArray = ByteBuffer.allocate(TimestampRecord.SIZE);
		recordInitParameter.writeBytes(outputBufferArray, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (array).", inputBuffer.array(), outputBufferArray.array());
	}

	/**
	 * Assertions for TimestampRecord.
	 */
	private void assertTimestampRecord(final TimestampRecord record) {
		Assert.assertEquals("'timestamp' value assertion failed.", record.getTimestamp(), PROPERTY_TIMESTAMP);
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
