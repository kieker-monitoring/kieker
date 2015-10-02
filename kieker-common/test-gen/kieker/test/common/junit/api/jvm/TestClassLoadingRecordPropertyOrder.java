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

package kieker.test.common.junit.api.jvm;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.jvm.ClassLoadingRecord;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.common.junit.util.APIEvaluationFunctions;
			
/**
 * Test API of {@link kieker.common.record.jvm.ClassLoadingRecord}.
 * 
 * @author API Checker
 * 
 * @since 1.12
 */
public class TestClassLoadingRecordPropertyOrder extends AbstractKiekerTest {

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
	/** Constant value parameter for totalLoadedClassCount. */
	private static final long PROPERTY_TOTAL_LOADED_CLASS_COUNT = 3L;
	/** Constant value parameter for loadedClassCount. */
	private static final int PROPERTY_LOADED_CLASS_COUNT = 1001;
	/** Constant value parameter for unloadedClassCount. */
	private static final long PROPERTY_UNLOADED_CLASS_COUNT = 4L;
							
	/**
	 * Empty constructor.
	 */
	public TestClassLoadingRecordPropertyOrder() {
		// Empty constructor for test class.
	}

	/**
	 * Test property order processing of {@link kieker.common.record.jvm.ClassLoadingRecord} constructors and
	 * different serialization routines.
	 */
	@Test
	public void testClassLoadingRecordPropertyOrder() { // NOPMD
		final IRegistry<String> stringRegistry = this.makeStringRegistry();
		final Object[] values = {
			PROPERTY_TIMESTAMP,
			PROPERTY_HOSTNAME,
			PROPERTY_VM_NAME,
			PROPERTY_TOTAL_LOADED_CLASS_COUNT,
			PROPERTY_LOADED_CLASS_COUNT,
			PROPERTY_UNLOADED_CLASS_COUNT,
		};
		final ByteBuffer inputBuffer = APIEvaluationFunctions.createByteBuffer(ClassLoadingRecord.SIZE, 
			this.makeStringRegistry(), values);
					
		final ClassLoadingRecord recordInitParameter = new ClassLoadingRecord(
			PROPERTY_TIMESTAMP,
			PROPERTY_HOSTNAME,
			PROPERTY_VM_NAME,
			PROPERTY_TOTAL_LOADED_CLASS_COUNT,
			PROPERTY_LOADED_CLASS_COUNT,
			PROPERTY_UNLOADED_CLASS_COUNT
		);
		final ClassLoadingRecord recordInitBuffer = new ClassLoadingRecord(inputBuffer, this.makeStringRegistry());
		final ClassLoadingRecord recordInitArray = new ClassLoadingRecord(values);
		
		this.assertClassLoadingRecord(recordInitParameter);
		this.assertClassLoadingRecord(recordInitBuffer);
		this.assertClassLoadingRecord(recordInitArray);

		// test to array
		final Object[] valuesParameter = recordInitParameter.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesParameter);
		final Object[] valuesBuffer = recordInitBuffer.toArray();
		Assert.assertArrayEquals("Result array of record initialized by buffer constructor differs from predefined array.", values, valuesBuffer);
		final Object[] valuesArray = recordInitArray.toArray();
		Assert.assertArrayEquals("Result array of record initialized by parameter constructor differs from predefined array.", values, valuesArray);

		// test write to buffer
		final ByteBuffer outputBufferParameter = ByteBuffer.allocate(ClassLoadingRecord.SIZE);
		recordInitParameter.writeBytes(outputBufferParameter, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (parameter).", inputBuffer.array(), outputBufferParameter.array());

		final ByteBuffer outputBufferBuffer = ByteBuffer.allocate(ClassLoadingRecord.SIZE);
		recordInitParameter.writeBytes(outputBufferBuffer, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (buffer).", inputBuffer.array(), outputBufferBuffer.array());

		final ByteBuffer outputBufferArray = ByteBuffer.allocate(ClassLoadingRecord.SIZE);
		recordInitParameter.writeBytes(outputBufferArray, stringRegistry);
		Assert.assertArrayEquals("Byte buffer do not match (array).", inputBuffer.array(), outputBufferArray.array());
	}

	/**
	 * Assertions for ClassLoadingRecord.
	 */
	private void assertClassLoadingRecord(final ClassLoadingRecord record) {
		Assert.assertEquals("'timestamp' value assertion failed.", record.getTimestamp(), PROPERTY_TIMESTAMP);
		Assert.assertEquals("'hostname' value assertion failed.", record.getHostname(), PROPERTY_HOSTNAME);
		Assert.assertEquals("'vmName' value assertion failed.", record.getVmName(), PROPERTY_VM_NAME);
		Assert.assertEquals("'totalLoadedClassCount' value assertion failed.", record.getTotalLoadedClassCount(), PROPERTY_TOTAL_LOADED_CLASS_COUNT);
		Assert.assertEquals("'loadedClassCount' value assertion failed.", record.getLoadedClassCount(), PROPERTY_LOADED_CLASS_COUNT);
		Assert.assertEquals("'unloadedClassCount' value assertion failed.", record.getUnloadedClassCount(), PROPERTY_UNLOADED_CLASS_COUNT);
	}
			
	/**
	 * Build a populated string registry for all tests.
	 */
	private IRegistry<String> makeStringRegistry() {
		final IRegistry<String> stringRegistry = new Registry<String>();
		// get registers string and returns their ID
		stringRegistry.get(PROPERTY_HOSTNAME);
		stringRegistry.get(PROPERTY_VM_NAME);

		return stringRegistry;
	}
}
