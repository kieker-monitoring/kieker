/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

import kieker.test.common.junit.AbstractDynamicKiekerTest;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.9
 */
public class TestRecordsUsingCorrectBinarySize extends AbstractDynamicKiekerTest {

	private static final Log LOG = LogFactory.getLog(TestRecordsUsingCorrectBinarySize.class);

	public TestRecordsUsingCorrectBinarySize() {
		// empty default constructor
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		final Collection<Class<?>> availableClasses = super.deliverAllAvailableClassesFromSourceDirectory();
		final Collection<Class<?>> notAbstractClasses = super.filterOutAbstractClasses(availableClasses);
		final Collection<Class<?>> filteredClasses = super.filterOutClassesNotExtending(IMonitoringRecord.BinaryFactory.class, notAbstractClasses);

		for (final Class<?> clazz : filteredClasses) {
			LOG.info("Testing '" + clazz.getSimpleName() + "'...");
			Assert.assertTrue(clazz.getSimpleName() + "' uses an incorrect size field.", this.isSizeCorrect(clazz));
		}
	}

	private boolean isSizeCorrect(final Class<?> clazz) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		final Class<?>[] types = (Class<?>[]) clazz.getField("TYPES").get(null);
		int calculatedSize = 0;
		for (final Class<?> type : types) {
			if (type == Double.TYPE) {
				calculatedSize += Double.SIZE;
			} else if (type == Float.TYPE) {
				calculatedSize += Float.SIZE;
			} else if (type == Long.TYPE) {
				calculatedSize += Long.SIZE;
			} else if (type == Integer.TYPE) {
				calculatedSize += Integer.SIZE;
			} else if (type == Short.TYPE) {
				calculatedSize += Short.SIZE;
			} else if (type == Byte.TYPE) {
				calculatedSize += Byte.SIZE;
			} else if (type == Character.TYPE) {
				calculatedSize += Character.SIZE;
			} else if (type == String.class) {
				calculatedSize += Integer.SIZE;
			} else if (type == Boolean.TYPE) {
				calculatedSize += Byte.SIZE;
			} else {
				throw new UnsupportedOperationException("Unsupported data type found: " + type);
			}
		}
		calculatedSize /= 8;

		final int size = (Integer) clazz.getField("SIZE").get(null);
		return (calculatedSize == size);
	}

}
