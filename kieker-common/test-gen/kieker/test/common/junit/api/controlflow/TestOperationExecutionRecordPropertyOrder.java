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

package kieker.test.common.junit.api.controlflow;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.io.DefaultValueDeserializer;
import kieker.common.record.io.DefaultValueSerializer;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.common.junit.util.APIEvaluationFunctions;

/**
 * Test API of {@link kieker.common.record.controlflow.OperationExecutionRecord}.
 *
 * @author API Checker
 *
 * @since 1.12
 */
public class TestOperationExecutionRecordPropertyOrder extends AbstractKiekerTest {

	/**
	 * All numbers and values must be pairwise unequal. As the string registry also uses integers,
	 * we must guarantee this criteria by starting with 1000 instead of 0.
	 */
	/** Constant value parameter for operationSignature. */
	private static final String PROPERTY_OPERATION_SIGNATURE = "<operationSignature>";
	/** Constant value parameter for sessionId. */
	private static final String PROPERTY_SESSION_ID = "<sessionId>";
	/** Constant value parameter for traceId. */
	private static final long PROPERTY_TRACE_ID = 2L;
	/** Constant value parameter for tin. */
	private static final long PROPERTY_TIN = 3L;
	/** Constant value parameter for tout. */
	private static final long PROPERTY_TOUT = 4L;
	/** Constant value parameter for hostname. */
	private static final String PROPERTY_HOSTNAME = "<hostname>";
	/** Constant value parameter for eoi. */
	private static final int PROPERTY_EOI = 1001;
	/** Constant value parameter for ess. */
	private static final int PROPERTY_ESS = 1002;

	/**
	 * Empty constructor.
	 */
	public TestOperationExecutionRecordPropertyOrder() {
		// Empty constructor for test class.
	}

	/**
	 * Test property order processing of {@link kieker.common.record.controlflow.OperationExecutionRecord} constructors and
	 * different serialization routines.
	 */
	@Test
	public void testOperationExecutionRecordPropertyOrder() { // NOPMD
		final IRegistry<String> stringRegistry = this.makeStringRegistry();
		final Object[] values = {
			PROPERTY_OPERATION_SIGNATURE,
			PROPERTY_SESSION_ID,
			PROPERTY_TRACE_ID,
			PROPERTY_TIN,
			PROPERTY_TOUT,
			PROPERTY_HOSTNAME,
			PROPERTY_EOI,
			PROPERTY_ESS,
		};
		final ByteBuffer inputBuffer = APIEvaluationFunctions.createByteBuffer(OperationExecutionRecord.SIZE,
				this.makeStringRegistry(), values);

		final OperationExecutionRecord recordInitParameter = new OperationExecutionRecord(
				PROPERTY_OPERATION_SIGNATURE,
				PROPERTY_SESSION_ID,
				PROPERTY_TRACE_ID,
				PROPERTY_TIN,
				PROPERTY_TOUT,
				PROPERTY_HOSTNAME,
				PROPERTY_EOI,
				PROPERTY_ESS);
		final OperationExecutionRecord recordInitBuffer = new OperationExecutionRecord(DefaultValueDeserializer.create(inputBuffer, this.makeStringRegistry()));
		final OperationExecutionRecord recordInitArray = new OperationExecutionRecord(values);

		this.assertOperationExecutionRecord(recordInitParameter);
		this.assertOperationExecutionRecord(recordInitBuffer);
		this.assertOperationExecutionRecord(recordInitArray);

		// test to array
		final Object[] valuesParameter = recordInitParameter.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesParameter);
		final Object[] valuesBuffer = recordInitBuffer.toArray();
		Assert.assertArrayEquals("Result array of record initialized by buffer constructor differs from predefined array.", values, valuesBuffer);
		final Object[] valuesArray = recordInitArray.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesArray);

		// test write to buffer
		final ByteBuffer outputBufferParameter = ByteBuffer.allocate(OperationExecutionRecord.SIZE);
		recordInitParameter.serialize(DefaultValueSerializer.create(outputBufferParameter, stringRegistry));
		Assert.assertArrayEquals("Byte buffer do not match (parameter).", inputBuffer.array(), outputBufferParameter.array());

		final ByteBuffer outputBufferBuffer = ByteBuffer.allocate(OperationExecutionRecord.SIZE);
		recordInitParameter.serialize(DefaultValueSerializer.create(outputBufferBuffer, stringRegistry));
		Assert.assertArrayEquals("Byte buffer do not match (buffer).", inputBuffer.array(), outputBufferBuffer.array());

		final ByteBuffer outputBufferArray = ByteBuffer.allocate(OperationExecutionRecord.SIZE);
		recordInitParameter.serialize(DefaultValueSerializer.create(outputBufferArray, stringRegistry));
		Assert.assertArrayEquals("Byte buffer do not match (array).", inputBuffer.array(), outputBufferArray.array());
	}

	/**
	 * Assertions for OperationExecutionRecord.
	 */
	private void assertOperationExecutionRecord(final OperationExecutionRecord record) {
		Assert.assertEquals("'operationSignature' value assertion failed.", record.getOperationSignature(), PROPERTY_OPERATION_SIGNATURE);
		Assert.assertEquals("'sessionId' value assertion failed.", record.getSessionId(), PROPERTY_SESSION_ID);
		Assert.assertEquals("'traceId' value assertion failed.", record.getTraceId(), PROPERTY_TRACE_ID);
		Assert.assertEquals("'tin' value assertion failed.", record.getTin(), PROPERTY_TIN);
		Assert.assertEquals("'tout' value assertion failed.", record.getTout(), PROPERTY_TOUT);
		Assert.assertEquals("'hostname' value assertion failed.", record.getHostname(), PROPERTY_HOSTNAME);
		Assert.assertEquals("'eoi' value assertion failed.", record.getEoi(), PROPERTY_EOI);
		Assert.assertEquals("'ess' value assertion failed.", record.getEss(), PROPERTY_ESS);
	}

	/**
	 * Build a populated string registry for all tests.
	 */
	private IRegistry<String> makeStringRegistry() {
		final IRegistry<String> stringRegistry = new Registry<String>();
		// get registers string and returns their ID
		stringRegistry.get(PROPERTY_OPERATION_SIGNATURE);
		stringRegistry.get(PROPERTY_SESSION_ID);
		stringRegistry.get(PROPERTY_HOSTNAME);

		return stringRegistry;
	}
}
