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

package kieker.test.analysis.junit.plugin.reader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import org.apache.cxf.helpers.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * This JUnit test makes sure that there are no readers with input ports in Kieker.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.9
 */
public class TestNoInputPortsForReader extends AbstractKiekerTest {

	private static final Log LOG = LogFactory.getLog(TestNoInputPortsForReader.class);

	private static final String DIR_NAME_SOURCES = "src";
	private static final String PATTERN_JAVA_SOURCE_FILES = ".*java";

	/**
	 * Default constructor.
	 */
	public TestNoInputPortsForReader() {
		// empty default constructor
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		final List<File> sourceFiles = TestNoInputPortsForReader.listJavaSourceFiles();
		for (final File sourceFile : sourceFiles) {
			final String className = TestNoInputPortsForReader.sourceFileToClassName(sourceFile);
			final Class<?> clazz = this.getClass().getClassLoader().loadClass(className);
			if (TestNoInputPortsForReader.doesClassExtendAbstractReaderPlugin(clazz) && !(this.isClassAbstract(clazz))) {
				LOG.info("Testing class '" + className + "'...");
				if (TestNoInputPortsForReader.containsInputPorts((Class<? extends AbstractReaderPlugin>) clazz)) {
					Assert.fail("Class '" + className + "' is a reader with input ports.");
				}
			}
		}
	}

	private boolean isClassAbstract(final Class<?> clazz) {
		return Modifier.isAbstract(clazz.getModifiers());
	}

	private static boolean containsInputPorts(final Class<? extends AbstractReaderPlugin> clazz) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		for (final Method method : clazz.getMethods()) {
			if (method.isAnnotationPresent(InputPort.class)) {
				return true;
			}
		}
		return false;
	}

	private static List<File> listJavaSourceFiles() {
		return FileUtils.getFilesRecurse(new File(DIR_NAME_SOURCES), PATTERN_JAVA_SOURCE_FILES);
	}

	private static boolean doesClassExtendAbstractReaderPlugin(final Class<?> clazz) {
		return AbstractReaderPlugin.class.isAssignableFrom(clazz);
	}

	private static String sourceFileToClassName(final File file) {
		final String pathName = file.getPath();
		String className = pathName.substring(0, pathName.length() - 5).replace(File.separator, ".");
		final int secondPointPos = className.indexOf('.', 5);
		className = className.substring(secondPointPos + 1);

		return className;
	}
}
