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

package kieker.test.common.junit;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.cxf.helpers.FileUtils;

/**
 * This abstract class is the base for all other dynamic JUnit tests within the system. Those are tests which search for example for specific classes in the soure
 * directory.
 *
 * @author Nils Christian Ehmke
 *
 * @since 1.9
 */
public abstract class AbstractDynamicKiekerTest extends AbstractKiekerTest {

	private static final String DIR_NAME_TESTS = "test";
	private static final String DIR_NAME_SOURCES = "src";
	private static final String PATTERN_JAVA_SOURCE_FILES = ".*java";
	private static final String PATTERN_JAVA_TEST_FILES = ".*Test.*java";
	private static final String PATTERN_JUNIT_PACKAGE_NAME = ".*junit.*";

	protected Collection<Class<?>> deliverAllAvailableClassesFromSourceDirectory() throws ClassNotFoundException {
		return AbstractDynamicKiekerTest.transformClassNameToClasses(AbstractDynamicKiekerTest.transformFilesToClassNames(AbstractDynamicKiekerTest.listSourceFiles(
				DIR_NAME_SOURCES, PATTERN_JAVA_SOURCE_FILES)));
	}

	protected Collection<Class<?>> deliverAllAvailableClassesFromTestDirectoryInJUnitPackage() throws ClassNotFoundException {
		return AbstractDynamicKiekerTest.transformClassNameToClasses(AbstractDynamicKiekerTest.transformFilesToClassNames(
				AbstractDynamicKiekerTest.filterOutFilesNotMatchingFullQualifiedPathName(PATTERN_JUNIT_PACKAGE_NAME,
						AbstractDynamicKiekerTest.listSourceFiles(DIR_NAME_TESTS, PATTERN_JAVA_TEST_FILES))));
	}

	private static Collection<File> filterOutFilesNotMatchingFullQualifiedPathName(final String pattern, final Collection<File> files) {
		final Collection<File> result = new LinkedList<File>();

		for (final File file : files) {
			if (file.getAbsolutePath().matches(pattern)) {
				result.add(file);
			}
		}

		return result;
	}

	private static Collection<File> listSourceFiles(final String directoryName, final String filePattern) {
		return FileUtils.getFilesRecurse(new File(directoryName), filePattern);
	}

	private static Collection<String> transformFilesToClassNames(final Collection<File> files) {
		final Collection<String> result = new LinkedList<String>();

		for (final File file : files) {
			final String pathName = file.getPath();
			String className = pathName.substring(0, pathName.length() - 5).replace(File.separator, ".");
			final int firstPointPos = className.indexOf('.');
			className = className.substring(firstPointPos + 1);

			result.add(className);
		}

		return result;
	}

	private static Collection<Class<?>> transformClassNameToClasses(final Collection<String> classNames) throws ClassNotFoundException {
		final Collection<Class<?>> result = new LinkedList<Class<?>>();

		for (final String className : classNames) {
			result.add(AbstractDynamicKiekerTest.class.getClassLoader().loadClass(className));
		}

		return result;
	}

	protected Collection<Class<?>> filterOutAbstractClasses(final Collection<Class<?>> classes) {
		final Collection<Class<?>> result = new LinkedList<Class<?>>();

		for (final Class<?> clazz : classes) {
			if (!Modifier.isAbstract(clazz.getModifiers())) {
				result.add(clazz);
			}
		}

		return result;
	}

	protected Collection<Class<?>> filterOutClassesNotExtending(final Class<?> superClass, final Collection<Class<?>> classes) {
		final Collection<Class<?>> result = new LinkedList<Class<?>>();

		for (final Class<?> clazz : classes) {
			if (superClass.isAssignableFrom(clazz)) {
				result.add(clazz);
			}
		}

		return result;
	}

	public Collection<Class<?>> filterOutClassesExtending(final Class<AbstractKiekerTest> superClass, final Collection<Class<?>> classes) {
		final Collection<Class<?>> result = new LinkedList<Class<?>>();

		for (final Class<?> clazz : classes) {
			if (!superClass.isAssignableFrom(clazz)) {
				result.add(clazz);
			}
		}

		return result;
	}

	protected Collection<Class<?>> filterOutClassesNotMatchingFullQualifiedClassNamePattern(final String pattern, final Collection<Class<?>> classes) {
		final Collection<Class<?>> result = new LinkedList<Class<?>>();

		for (final Class<?> clazz : classes) {
			if (clazz.getCanonicalName().matches(pattern)) {
				result.add(clazz);
			}
		}

		return result;
	}

}
