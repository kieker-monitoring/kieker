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
import kieker.common.record.jvm.CompilationRecord;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.common.junit.util.APIEvaluationFunctions;
			
/**
 * Test API of {@link kieker.common.record.jvm.CompilationRecord}.
 * 
 * @author API Checker
 * 
 * @since 1.12
 */
public class TestCompilationRecordPropertyOrder extends AbstractKiekerTest {

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
	/** Constant value parameter for jitCompilerName. */
	private static final String PROPERTY_JIT_COMPILER_NAME = "<jitCompilerName>";
	/** Constant value parameter for totalCompilationTimeMS. */
	private static final long PROPERTY_TOTAL_COMPILATION_TIME_M_S = 3L;
							
	/**
	 * Empty constructor.
	 */
	public TestCompilationRecordPropertyOrder() {
		// Empty constructor for test class.
	}

	/**
	 * Test property order processing of {@link kieker.common.record.jvm.CompilationRecord} constructors and
	 * different serialization routines.
	 */
	@Test
	public void testCompilationRecordPropertyOrder() { // NOPMD
		final IRegistry<String> stringRegistry = this.makeStringRegistry();
		final Object[] values = {
			PROPERTY_TIMESTAMP,
			PROPERTY_HOSTNAME,
			PROPERTY_VM_NAME,
			PROPERTY_JIT_COMPILER_NAME,
			PROPERTY_TOTAL_COMPILATION_TIME_M_S,
		};
		final ByteBuffer inputBuffer = APIEvaluationFunctions.createByteBuffer(CompilationRecord.SIZE, 
			this.makeStringRegistry(), values);
					
		final CompilationRecord recordInitParameter = new CompilationRecord(
			PROPERTY_TIMESTAMP,
			PROPERTY_HOSTNAME,
			PROPERTY_VM_NAME,
			PROPERTY_JIT_COMPILER_NAME,
			PROPERTY_TOTAL_COMPILATION_TIME_M_S
		);
		final CompilationRecord recordInitBuffer = new CompilationRecord(DefaultValueDeserializer.instance(), inputBuffer, this.makeStringRegistry());
		final CompilationRecord recordInitArray = new CompilationRecord(values);
		
		this.assertCompilationRecord(recordInitParameter);
		this.assertCompilationRecord(recordInitBuffer);
		this.assertCompilationRecord(recordInitArray);

		// test to array
		final Object[] valuesParameter = recordInitParameter.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesParameter);
		final Object[] valuesBuffer = recordInitBuffer.toArray();
		Assert.assertArrayEquals("Result array of record initialized by buffer constructor differs from predefined array.", values, valuesBuffer);
		final Object[] valuesArray = recordInitArray.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesArray);

		// test write to buffer
		final ByteBuffer outputBufferParameter = ByteBuffer.allocate(CompilationRecord.SIZE);
		recordInitParameter.writeBytes(DefaultValueSerializer.instance(), outputBufferParameter, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (parameter).", inputBuffer.array(), outputBufferParameter.array());

		final ByteBuffer outputBufferBuffer = ByteBuffer.allocate(CompilationRecord.SIZE);
		recordInitParameter.writeBytes(DefaultValueSerializer.instance(), outputBufferBuffer, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (buffer).", inputBuffer.array(), outputBufferBuffer.array());

		final ByteBuffer outputBufferArray = ByteBuffer.allocate(CompilationRecord.SIZE);
		recordInitParameter.writeBytes(DefaultValueSerializer.instance(), outputBufferArray, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (array).", inputBuffer.array(), outputBufferArray.array());
	}

	/**
	 * Assertions for CompilationRecord.
	 */
	private void assertCompilationRecord(final CompilationRecord record) {
		Assert.assertEquals("'timestamp' value assertion failed.", record.getTimestamp(), PROPERTY_TIMESTAMP);
		Assert.assertEquals("'hostname' value assertion failed.", record.getHostname(), PROPERTY_HOSTNAME);
		Assert.assertEquals("'vmName' value assertion failed.", record.getVmName(), PROPERTY_VM_NAME);
		Assert.assertEquals("'jitCompilerName' value assertion failed.", record.getJitCompilerName(), PROPERTY_JIT_COMPILER_NAME);
		Assert.assertEquals("'totalCompilationTimeMS' value assertion failed.", record.getTotalCompilationTimeMS(), PROPERTY_TOTAL_COMPILATION_TIME_M_S);
	}
			
	/**
	 * Build a populated string registry for all tests.
	 */
	private IRegistry<String> makeStringRegistry() {
		final IRegistry<String> stringRegistry = new Registry<String>();
		// get registers string and returns their ID
		stringRegistry.get(PROPERTY_HOSTNAME);
		stringRegistry.get(PROPERTY_VM_NAME);
		stringRegistry.get(PROPERTY_JIT_COMPILER_NAME);

		return stringRegistry;
	}
}
