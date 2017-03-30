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

package kieker.test.common.junit.record;

import java.nio.ByteBuffer;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.exception.MonitoringRecordException;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.DefaultValueDeserializer;
import kieker.common.record.io.DefaultValueSerializer;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractDynamicKiekerTest;

/**
 * @author Nils Christian Ehmke
 *
 * @since 1.9
 */
public class TestRecordSerialization extends AbstractDynamicKiekerTest {

	private static final Log LOG = LogFactory.getLog(TestRecordSerialization.class);

	private final IRegistry<String> registry = new Registry<String>();

	public TestRecordSerialization() {
		// empty default constructor
	}

	@Test
	public void test() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException,
			MonitoringRecordException {
		final Collection<Class<?>> filteredClasses = this.getFilteredClasses();

		for (final Class<?> clazz : filteredClasses) {
			LOG.info("Testing '" + clazz.getSimpleName() + "'...");
			final Object[] unserializedTestArray = TestRecordSerialization.prepareAndFillTestArray(clazz);

			Assert.assertArrayEquals(clazz.getSimpleName() + "' uses an incorrect object serialization/dezerialization mechanism.", unserializedTestArray,
					this.serializeAndDeserializeTestArrayWithObject(clazz, unserializedTestArray));
			Assert.assertArrayEquals(clazz.getSimpleName() + "' uses an incorrect binary serialization/dezerialization mechanism.", unserializedTestArray,
					this.serializeAndDeserializeTestArrayWithBinary(clazz, unserializedTestArray));
			Assert.assertArrayEquals(clazz.getSimpleName() + "' uses an incorrect string serialization/dezerialization mechanism.", unserializedTestArray,
					this.serializeAndDeserializeTestArrayWithString(clazz, unserializedTestArray));
		}
	}

	private Collection<Class<?>> getFilteredClasses() throws ClassNotFoundException {
		final Collection<Class<?>> availableClasses = super.deliverAllAvailableClassesFromSourceDirectory();
		final Collection<Class<?>> notAbstractClasses = super.filterOutAbstractClasses(availableClasses);
		final Collection<Class<?>> filteredClasses = super.filterOutClassesNotExtending(IMonitoringRecord.class, notAbstractClasses);

		return filteredClasses;
	}

	private static Object[] prepareAndFillTestArray(final Class<?> clazz) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException,
			SecurityException {
		final Class<?>[] types = (Class<?>[]) clazz.getField("TYPES").get(null);
		final int arrayLength = types.length;
		final Object[] testArray = new Object[arrayLength];

		for (int i = 0; i < arrayLength; i++) {
			final Class<?> type = types[i];
			final Object objectForTestArray;

			if (type == Double.TYPE) {
				objectForTestArray = 1.0;
			} else if (type == Float.TYPE) {
				objectForTestArray = 2.0f;
			} else if (type == Long.TYPE) {
				objectForTestArray = 3L;
			} else if (type == Integer.TYPE) {
				objectForTestArray = 4;
			} else if (type == Short.TYPE) {
				objectForTestArray = (short) 5; // NOPMD (the short type is just used for test purposes here)
			} else if (type == Byte.TYPE) {
				objectForTestArray = (byte) 6;
			} else if (type == Character.TYPE) {
				objectForTestArray = 'A';
			} else if (type == String.class) {
				objectForTestArray = "Kieker";
			} else if (type == Boolean.TYPE) {
				objectForTestArray = true;
			} else {
				throw new UnsupportedOperationException("Unsupported data type found: " + type);
			}

			testArray[i] = objectForTestArray;
		}

		return testArray;
	}

	@SuppressWarnings("unchecked")
	private Object[] serializeAndDeserializeTestArrayWithObject(final Class<?> clazz, final Object[] unserializedTestArray) throws MonitoringRecordException {
		final IMonitoringRecord testRecord = AbstractMonitoringRecord.createFromArray((Class<? extends IMonitoringRecord>) clazz, unserializedTestArray);
		return testRecord.toArray();
	}

	@SuppressWarnings("unchecked")
	private Object[] serializeAndDeserializeTestArrayWithBinary(final Class<?> clazz, final Object[] unserializedTestArray) throws MonitoringRecordException {
		final IMonitoringRecord inRecord = AbstractMonitoringRecord.createFromArray((Class<? extends IMonitoringRecord>) clazz, unserializedTestArray);
		final ByteBuffer byteBuffer = ByteBuffer.allocate(inRecord.getSize());

		final String clazzID = clazz.getCanonicalName();
		inRecord.serialize(DefaultValueSerializer.create(byteBuffer, this.registry));
		byteBuffer.flip();

		final IMonitoringRecord outRecord = AbstractMonitoringRecord.createFromDeserializer(clazzID, DefaultValueDeserializer.create(byteBuffer, this.registry));
		return outRecord.toArray();
	}

	@SuppressWarnings("unchecked")
	private Object[] serializeAndDeserializeTestArrayWithString(final Class<?> clazz, final Object[] unserializedTestArray) throws MonitoringRecordException {
		final String[] unserializedStringTestArray = TestRecordSerialization.testArrayToStringArray(unserializedTestArray);
		final IMonitoringRecord testRecord = AbstractMonitoringRecord.createFromStringArray((Class<? extends IMonitoringRecord>) clazz, unserializedStringTestArray);
		return testRecord.toArray();
	}

	private static String[] testArrayToStringArray(final Object[] unserializedTestArray) { // NOPMD (this is not a test method)
		final int arrayLength = unserializedTestArray.length;
		final String[] stringArray = new String[arrayLength];

		for (int i = 0; i < arrayLength; i++) {
			stringArray[i] = unserializedTestArray[i].toString();
		}

		return stringArray;
	}

}
