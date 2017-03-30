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
import kieker.common.record.system.NetworkUtilizationRecord;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.common.junit.util.APIEvaluationFunctions;

/**
 * Test API of {@link kieker.common.record.system.NetworkUtilizationRecord}.
 *
 * @author API Checker
 *
 * @since 1.12
 */
public class TestNetworkUtilizationRecordPropertyOrder extends AbstractKiekerTest {

	/**
	 * All numbers and values must be pairwise unequal. As the string registry also uses integers,
	 * we must guarantee this criteria by starting with 1000 instead of 0.
	 */
	/** Constant value parameter for timestamp. */
	private static final long PROPERTY_TIMESTAMP = 2L;
	/** Constant value parameter for hostname. */
	private static final String PROPERTY_HOSTNAME = "<hostname>";
	/** Constant value parameter for interfaceName. */
	private static final String PROPERTY_INTERFACE_NAME = "<interfaceName>";
	/** Constant value parameter for speed. */
	private static final long PROPERTY_SPEED = 3L;
	/** Constant value parameter for txBytesPerSecond. */
	private static final double PROPERTY_TX_BYTES_PER_SECOND = 2.0;
	/** Constant value parameter for txCarrierPerSecond. */
	private static final double PROPERTY_TX_CARRIER_PER_SECOND = 3.0;
	/** Constant value parameter for txCollisionsPerSecond. */
	private static final double PROPERTY_TX_COLLISIONS_PER_SECOND = 4.0;
	/** Constant value parameter for txDroppedPerSecond. */
	private static final double PROPERTY_TX_DROPPED_PER_SECOND = 5.0;
	/** Constant value parameter for txErrorsPerSecond. */
	private static final double PROPERTY_TX_ERRORS_PER_SECOND = 6.0;
	/** Constant value parameter for txOverrunsPerSecond. */
	private static final double PROPERTY_TX_OVERRUNS_PER_SECOND = 7.0;
	/** Constant value parameter for txPacketsPerSecond. */
	private static final double PROPERTY_TX_PACKETS_PER_SECOND = 8.0;
	/** Constant value parameter for rxBytesPerSecond. */
	private static final double PROPERTY_RX_BYTES_PER_SECOND = 9.0;
	/** Constant value parameter for rxDroppedPerSecond. */
	private static final double PROPERTY_RX_DROPPED_PER_SECOND = 10.0;
	/** Constant value parameter for rxErrorsPerSecond. */
	private static final double PROPERTY_RX_ERRORS_PER_SECOND = 11.0;
	/** Constant value parameter for rxFramePerSecond. */
	private static final double PROPERTY_RX_FRAME_PER_SECOND = 12.0;
	/** Constant value parameter for rxOverrunsPerSecond. */
	private static final double PROPERTY_RX_OVERRUNS_PER_SECOND = 13.0;
	/** Constant value parameter for rxPacketsPerSecond. */
	private static final double PROPERTY_RX_PACKETS_PER_SECOND = 14.0;

	/**
	 * Empty constructor.
	 */
	public TestNetworkUtilizationRecordPropertyOrder() {
		// Empty constructor for test class.
	}

	/**
	 * Test property order processing of {@link kieker.common.record.system.NetworkUtilizationRecord} constructors and
	 * different serialization routines.
	 */
	@Test
	public void testNetworkUtilizationRecordPropertyOrder() { // NOPMD
		final IRegistry<String> stringRegistry = this.makeStringRegistry();
		final Object[] values = {
			PROPERTY_TIMESTAMP,
			PROPERTY_HOSTNAME,
			PROPERTY_INTERFACE_NAME,
			PROPERTY_SPEED,
			PROPERTY_TX_BYTES_PER_SECOND,
			PROPERTY_TX_CARRIER_PER_SECOND,
			PROPERTY_TX_COLLISIONS_PER_SECOND,
			PROPERTY_TX_DROPPED_PER_SECOND,
			PROPERTY_TX_ERRORS_PER_SECOND,
			PROPERTY_TX_OVERRUNS_PER_SECOND,
			PROPERTY_TX_PACKETS_PER_SECOND,
			PROPERTY_RX_BYTES_PER_SECOND,
			PROPERTY_RX_DROPPED_PER_SECOND,
			PROPERTY_RX_ERRORS_PER_SECOND,
			PROPERTY_RX_FRAME_PER_SECOND,
			PROPERTY_RX_OVERRUNS_PER_SECOND,
			PROPERTY_RX_PACKETS_PER_SECOND,
		};
		final ByteBuffer inputBuffer = APIEvaluationFunctions.createByteBuffer(NetworkUtilizationRecord.SIZE,
				this.makeStringRegistry(), values);

		final NetworkUtilizationRecord recordInitParameter = new NetworkUtilizationRecord(
				PROPERTY_TIMESTAMP,
				PROPERTY_HOSTNAME,
				PROPERTY_INTERFACE_NAME,
				PROPERTY_SPEED,
				PROPERTY_TX_BYTES_PER_SECOND,
				PROPERTY_TX_CARRIER_PER_SECOND,
				PROPERTY_TX_COLLISIONS_PER_SECOND,
				PROPERTY_TX_DROPPED_PER_SECOND,
				PROPERTY_TX_ERRORS_PER_SECOND,
				PROPERTY_TX_OVERRUNS_PER_SECOND,
				PROPERTY_TX_PACKETS_PER_SECOND,
				PROPERTY_RX_BYTES_PER_SECOND,
				PROPERTY_RX_DROPPED_PER_SECOND,
				PROPERTY_RX_ERRORS_PER_SECOND,
				PROPERTY_RX_FRAME_PER_SECOND,
				PROPERTY_RX_OVERRUNS_PER_SECOND,
				PROPERTY_RX_PACKETS_PER_SECOND);
		final NetworkUtilizationRecord recordInitBuffer = new NetworkUtilizationRecord(DefaultValueDeserializer.create(inputBuffer, this.makeStringRegistry()));
		final NetworkUtilizationRecord recordInitArray = new NetworkUtilizationRecord(values);

		this.assertNetworkUtilizationRecord(recordInitParameter);
		this.assertNetworkUtilizationRecord(recordInitBuffer);
		this.assertNetworkUtilizationRecord(recordInitArray);

		// test to array
		final Object[] valuesParameter = recordInitParameter.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesParameter);
		final Object[] valuesBuffer = recordInitBuffer.toArray();
		Assert.assertArrayEquals("Result array of record initialized by buffer constructor differs from predefined array.", values, valuesBuffer);
		final Object[] valuesArray = recordInitArray.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesArray);

		// test write to buffer
		final ByteBuffer outputBufferParameter = ByteBuffer.allocate(NetworkUtilizationRecord.SIZE);
		recordInitParameter.serialize(DefaultValueSerializer.create(outputBufferParameter, stringRegistry));
		Assert.assertArrayEquals("Byte buffer do not match (parameter).", inputBuffer.array(), outputBufferParameter.array());

		final ByteBuffer outputBufferBuffer = ByteBuffer.allocate(NetworkUtilizationRecord.SIZE);
		recordInitParameter.serialize(DefaultValueSerializer.create(outputBufferBuffer, stringRegistry));
		Assert.assertArrayEquals("Byte buffer do not match (buffer).", inputBuffer.array(), outputBufferBuffer.array());

		final ByteBuffer outputBufferArray = ByteBuffer.allocate(NetworkUtilizationRecord.SIZE);
		recordInitParameter.serialize(DefaultValueSerializer.create(outputBufferArray, stringRegistry));
		Assert.assertArrayEquals("Byte buffer do not match (array).", inputBuffer.array(), outputBufferArray.array());
	}

	/**
	 * Assertions for NetworkUtilizationRecord.
	 */
	private void assertNetworkUtilizationRecord(final NetworkUtilizationRecord record) {
		Assert.assertEquals("'timestamp' value assertion failed.", record.getTimestamp(), PROPERTY_TIMESTAMP);
		Assert.assertEquals("'hostname' value assertion failed.", record.getHostname(), PROPERTY_HOSTNAME);
		Assert.assertEquals("'interfaceName' value assertion failed.", record.getInterfaceName(), PROPERTY_INTERFACE_NAME);
		Assert.assertEquals("'speed' value assertion failed.", record.getSpeed(), PROPERTY_SPEED);
		Assert.assertEquals("'txBytesPerSecond' value assertion failed.", record.getTxBytesPerSecond(), PROPERTY_TX_BYTES_PER_SECOND, 0.1);
		Assert.assertEquals("'txCarrierPerSecond' value assertion failed.", record.getTxCarrierPerSecond(), PROPERTY_TX_CARRIER_PER_SECOND, 0.1);
		Assert.assertEquals("'txCollisionsPerSecond' value assertion failed.", record.getTxCollisionsPerSecond(), PROPERTY_TX_COLLISIONS_PER_SECOND, 0.1);
		Assert.assertEquals("'txDroppedPerSecond' value assertion failed.", record.getTxDroppedPerSecond(), PROPERTY_TX_DROPPED_PER_SECOND, 0.1);
		Assert.assertEquals("'txErrorsPerSecond' value assertion failed.", record.getTxErrorsPerSecond(), PROPERTY_TX_ERRORS_PER_SECOND, 0.1);
		Assert.assertEquals("'txOverrunsPerSecond' value assertion failed.", record.getTxOverrunsPerSecond(), PROPERTY_TX_OVERRUNS_PER_SECOND, 0.1);
		Assert.assertEquals("'txPacketsPerSecond' value assertion failed.", record.getTxPacketsPerSecond(), PROPERTY_TX_PACKETS_PER_SECOND, 0.1);
		Assert.assertEquals("'rxBytesPerSecond' value assertion failed.", record.getRxBytesPerSecond(), PROPERTY_RX_BYTES_PER_SECOND, 0.1);
		Assert.assertEquals("'rxDroppedPerSecond' value assertion failed.", record.getRxDroppedPerSecond(), PROPERTY_RX_DROPPED_PER_SECOND, 0.1);
		Assert.assertEquals("'rxErrorsPerSecond' value assertion failed.", record.getRxErrorsPerSecond(), PROPERTY_RX_ERRORS_PER_SECOND, 0.1);
		Assert.assertEquals("'rxFramePerSecond' value assertion failed.", record.getRxFramePerSecond(), PROPERTY_RX_FRAME_PER_SECOND, 0.1);
		Assert.assertEquals("'rxOverrunsPerSecond' value assertion failed.", record.getRxOverrunsPerSecond(), PROPERTY_RX_OVERRUNS_PER_SECOND, 0.1);
		Assert.assertEquals("'rxPacketsPerSecond' value assertion failed.", record.getRxPacketsPerSecond(), PROPERTY_RX_PACKETS_PER_SECOND, 0.1);
	}

	/**
	 * Build a populated string registry for all tests.
	 */
	private IRegistry<String> makeStringRegistry() {
		final IRegistry<String> stringRegistry = new Registry<String>();
		// get registers string and returns their ID
		stringRegistry.get(PROPERTY_HOSTNAME);
		stringRegistry.get(PROPERTY_INTERFACE_NAME);

		return stringRegistry;
	}
}
