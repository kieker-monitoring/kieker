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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;

import org.apache.cxf.helpers.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

import kieker.test.analysis.junit.plugin.TestPluginConfigurationRetention;
import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.9
 */
public class TestRecordsUsingCorrectBinarySize extends AbstractKiekerTest {

	private static final Log LOG = LogFactory.getLog(TestPluginConfigurationRetention.class);

	private static final String DIR_NAME_SOURCES = "src";
	private static final String PATTERN_JAVA_SOURCE_FILES = ".*java";

	/**
	 * Default constructor.
	 */
	public TestRecordsUsingCorrectBinarySize() {
		// empty default constructor
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		final List<File> sourceFiles = TestRecordsUsingCorrectBinarySize.listJavaSourceFiles();
		for (final File sourceFile : sourceFiles) {
			final String className = TestRecordsUsingCorrectBinarySize.sourceFileToClassName(sourceFile);
			final Class<?> clazz = this.getClass().getClassLoader().loadClass(className);
			if (TestRecordsUsingCorrectBinarySize.doesClassImplemendBinaryFactory(clazz) && !this.isClassAbstract(clazz)) {
				LOG.info("Testing class '" + className + "'...");
				if (!this.isSizeCorrect(clazz)) {
					Assert.fail("Class '" + className + "' uses an incorrect size field.");
				}
			}
		}
	}

	private boolean isClassAbstract(final Class<?> clazz) {
		return Modifier.isAbstract(clazz.getModifiers());
	}

	private boolean isSizeCorrect(final Class<?> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, NoSuchFieldException {
		final int size = (Integer) clazz.getField("SIZE").get(null);
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
				calculatedSize += 32;
			} else if (type == Boolean.TYPE) {
				calculatedSize += 8;
			} else {
				throw new UnsupportedOperationException("Unsupported data type found: " + type);
			}
		}
		calculatedSize /= 8;

		return (calculatedSize == size);
	}

	private static List<File> listJavaSourceFiles() {
		return FileUtils.getFilesRecurse(new File(DIR_NAME_SOURCES), PATTERN_JAVA_SOURCE_FILES);
	}

	private static boolean doesClassImplemendBinaryFactory(final Class<?> clazz) {
		return IMonitoringRecord.BinaryFactory.class.isAssignableFrom(clazz);
	}

	private static String sourceFileToClassName(final File file) {
		final String pathName = file.getPath();
		String className = pathName.substring(0, pathName.length() - 5).replace(File.separator, ".");
		final int secondPointPos = className.indexOf('.', 5);
		className = className.substring(secondPointPos + 1);

		return className;
	}
}
