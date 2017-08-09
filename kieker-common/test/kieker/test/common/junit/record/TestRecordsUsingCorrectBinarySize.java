/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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
import kieker.common.record.AbstractMonitoringRecord;
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
				calculatedSize += AbstractMonitoringRecord.TYPE_SIZE_DOUBLE;
			} else if (type == Float.TYPE) {
				calculatedSize += AbstractMonitoringRecord.TYPE_SIZE_FLOAT;
			} else if (type == Long.TYPE) {
				calculatedSize += AbstractMonitoringRecord.TYPE_SIZE_LONG;
			} else if (type == Integer.TYPE) {
				calculatedSize += AbstractMonitoringRecord.TYPE_SIZE_INT;
			} else if (type == Short.TYPE) {
				calculatedSize += AbstractMonitoringRecord.TYPE_SIZE_SHORT;
			} else if (type == Byte.TYPE) {
				calculatedSize += AbstractMonitoringRecord.TYPE_SIZE_BYTE;
			} else if (type == Character.TYPE) {
				calculatedSize += AbstractMonitoringRecord.TYPE_SIZE_CHARACTER;
			} else if (type == String.class) {
				calculatedSize += AbstractMonitoringRecord.TYPE_SIZE_STRING;
			} else if (type == Boolean.TYPE) {
				calculatedSize += AbstractMonitoringRecord.TYPE_SIZE_BOOLEAN;
			} else {
				throw new UnsupportedOperationException("Unsupported data type found: " + type);
			}
		}

		final int size = (Integer) clazz.getField("SIZE").get(null);
		return calculatedSize == size;
	}

}
