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
import kieker.common.record.system.CPUUtilizationRecord;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.common.junit.util.APIEvaluationFunctions;

/**
 * Test API of {@link kieker.common.record.system.CPUUtilizationRecord}.
 *
 * @author API Checker
 *
 * @since 1.12
 */
public class TestCPUUtilizationRecordPropertyOrder extends AbstractKiekerTest {

	/**
	 * All numbers and values must be pairwise unequal. As the string registry also uses integers,
	 * we must guarantee this criteria by starting with 1000 instead of 0.
	 */
	/** Constant value parameter for timestamp. */
	private static final long PROPERTY_TIMESTAMP = 2L;
	/** Constant value parameter for hostname. */
	private static final String PROPERTY_HOSTNAME = "<hostname>";
	/** Constant value parameter for cpuID. */
	private static final String PROPERTY_CPU_I_D = "<cpuID>";
	/** Constant value parameter for user. */
	private static final double PROPERTY_USER = 2.0;
	/** Constant value parameter for system. */
	private static final double PROPERTY_SYSTEM = 3.0;
	/** Constant value parameter for wait. */
	private static final double PROPERTY_WAIT = 4.0;
	/** Constant value parameter for nice. */
	private static final double PROPERTY_NICE = 5.0;
	/** Constant value parameter for irq. */
	private static final double PROPERTY_IRQ = 6.0;
	/** Constant value parameter for totalUtilization. */
	private static final double PROPERTY_TOTAL_UTILIZATION = 7.0;
	/** Constant value parameter for idle. */
	private static final double PROPERTY_IDLE = 8.0;

	/**
	 * Empty constructor.
	 */
	public TestCPUUtilizationRecordPropertyOrder() {
		// Empty constructor for test class.
	}

	/**
	 * Test property order processing of {@link kieker.common.record.system.CPUUtilizationRecord} constructors and
	 * different serialization routines.
	 */
	@Test
	public void testCPUUtilizationRecordPropertyOrder() { // NOPMD
		final IRegistry<String> stringRegistry = this.makeStringRegistry();
		final Object[] values = {
			PROPERTY_TIMESTAMP,
			PROPERTY_HOSTNAME,
			PROPERTY_CPU_I_D,
			PROPERTY_USER,
			PROPERTY_SYSTEM,
			PROPERTY_WAIT,
			PROPERTY_NICE,
			PROPERTY_IRQ,
			PROPERTY_TOTAL_UTILIZATION,
			PROPERTY_IDLE,
		};
		final ByteBuffer inputBuffer = APIEvaluationFunctions.createByteBuffer(CPUUtilizationRecord.SIZE,
				this.makeStringRegistry(), values);

		final CPUUtilizationRecord recordInitParameter = new CPUUtilizationRecord(
				PROPERTY_TIMESTAMP,
				PROPERTY_HOSTNAME,
				PROPERTY_CPU_I_D,
				PROPERTY_USER,
				PROPERTY_SYSTEM,
				PROPERTY_WAIT,
				PROPERTY_NICE,
				PROPERTY_IRQ,
				PROPERTY_TOTAL_UTILIZATION,
				PROPERTY_IDLE);
		final CPUUtilizationRecord recordInitBuffer = new CPUUtilizationRecord(DefaultValueDeserializer.create(inputBuffer, this.makeStringRegistry()));
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
		recordInitParameter.serialize(DefaultValueSerializer.create(outputBufferParameter, stringRegistry));
		Assert.assertArrayEquals("Byte buffer do not match (parameter).", inputBuffer.array(), outputBufferParameter.array());

		final ByteBuffer outputBufferBuffer = ByteBuffer.allocate(CPUUtilizationRecord.SIZE);
		recordInitParameter.serialize(DefaultValueSerializer.create(outputBufferBuffer, stringRegistry));
		Assert.assertArrayEquals("Byte buffer do not match (buffer).", inputBuffer.array(), outputBufferBuffer.array());

		final ByteBuffer outputBufferArray = ByteBuffer.allocate(CPUUtilizationRecord.SIZE);
		recordInitParameter.serialize(DefaultValueSerializer.create(outputBufferArray, stringRegistry));
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
}
