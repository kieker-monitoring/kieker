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

package kieker.test.common.junit;

import java.io.File;
import java.lang.reflect.Modifier;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;

import kieker.test.common.junit.util.filesystem.FSUtil4Tests;

/**
 * This abstract class is the base for all other dynamic JUnit tests within the system. Those are tests which search for
 * example for specific classes in the source directory.
 *
 * @author Nils Christian Ehmke
 *
 * @since 1.9
 */
public abstract class AbstractDynamicKiekerTest extends AbstractKiekerTest {

	// abc.java, but not package-info.java
	public static final String REGEX_PATTERN_JAVA_SOURCE_FILES = "[^\\-]*java";
	public static final String REGEX_PATTERN_JAVA_TEST_FILES = ".*Test\\.java";
	public static final String REGEX_PATTERN_JUNIT_PACKAGE_NAME = ".*junit.*";

	private static final String DIR_NAME_TESTS = "test";
	private static final String DIR_NAME_SOURCES = "src";

	/**
	 * Collect all available classes from the source directory.
	 *
	 * @return collection of class types
	 * @throws ClassNotFoundException
	 *             in case a class cannot be found
	 */
	protected Collection<Class<?>> deliverAllAvailableClassesFromSourceDirectory() throws ClassNotFoundException {
		final String dirNameSourcesNormalized = super.modulePathToWorkingPath(DIR_NAME_SOURCES);

		final Collection<File> javaFiles = this.listSourceFiles(dirNameSourcesNormalized,
				REGEX_PATTERN_JAVA_SOURCE_FILES);
		return this.transformClassNameToClasses(this.transformFilesToClassNames(javaFiles));
	}

	/**
	 * Collect all available classes from the test source directory in the junit package.
	 *
	 * @return collection of class types
	 * @throws ClassNotFoundException
	 *             in case a class cannot be found
	 */
	protected Collection<Class<?>> deliverAllAvailableClassesFromTestDirectoryInJUnitPackage()
			throws ClassNotFoundException {
		final String dirNameTestsNormalized = super.modulePathToWorkingPath(DIR_NAME_TESTS);

		final Collection<File> javaTestFiles = this.listSourceFiles(dirNameTestsNormalized,
				REGEX_PATTERN_JAVA_TEST_FILES);
		final Collection<File> filteredTestFiles = AbstractDynamicKiekerTest
				.filterOutFilesNotMatchingFullQualifiedPathName(REGEX_PATTERN_JUNIT_PACKAGE_NAME, javaTestFiles);
		return this.transformClassNameToClasses(this.transformFilesToClassNames(filteredTestFiles));
	}

	private static Collection<File> filterOutFilesNotMatchingFullQualifiedPathName(final String regExPattern,
			final Collection<File> files) {
		final Collection<File> results = new LinkedList<File>();

		for (final File file : files) {
			if (file.getAbsolutePath().matches(regExPattern)) {
				results.add(file);
			}
		}

		return results;
	}

	private Collection<File> listSourceFiles(final String directoryName, final String regexFilePattern) {
		return FSUtil4Tests.listFilesRecursively(Paths.get(directoryName), regexFilePattern);
	}

	private Collection<String> transformFilesToClassNames(final Collection<File> files) {
		final Collection<String> results = new LinkedList<String>();

		for (final File file : files) {
			final String pathName = this.workingPathToModulePath(file.getPath());
			String className = pathName.substring(0, pathName.length() - 5).replace(File.separator, ".");
			final int firstPointPos = className.indexOf('.');
			className = className.substring(firstPointPos + 1);

			results.add(className);
		}

		return results;
	}

	private Collection<Class<?>> transformClassNameToClasses(final Collection<String> classNames)
			throws ClassNotFoundException {
		final Collection<Class<?>> results = new LinkedList<Class<?>>();

		final ClassLoader classLoader = AbstractDynamicKiekerTest.class.getClassLoader();
		for (final String className : classNames) {
			results.add(classLoader.loadClass(className));
		}

		return results;
	}

	/**
	 * Filter collection for non abstract classes.
	 *
	 * @param classes
	 *            all classes
	 * @return returns a collection with non abstract classes only
	 */
	protected Collection<Class<?>> filterOutAbstractClasses(final Collection<Class<?>> classes) {
		final Collection<Class<?>> results = new LinkedList<Class<?>>();

		for (final Class<?> clazz : classes) {
			if (!Modifier.isAbstract(clazz.getModifiers())) {
				results.add(clazz);
			}
		}

		return results;
	}

	/**
	 * Filter to scan only for classes inheriting superClass.
	 *
	 * @param superClass
	 *            super type
	 * @param classes
	 *            all classes
	 * @return returns a collection with classes derived from superClassq
	 */
	protected Collection<Class<?>> filterOutClassesNotExtending(final Class<?> superClass,
			final Collection<Class<?>> classes) {
		final Collection<Class<?>> results = new LinkedList<Class<?>>();

		for (final Class<?> clazz : classes) {
			if (superClass.isAssignableFrom(clazz)) {
				results.add(clazz);
			}
		}

		return results;
	}

	/**
	 * Filter to scan only for classes not inheriting superClass.
	 *
	 * @param superClass
	 *            super type
	 * @param classes
	 *            all classes
	 * @return returns a collection with classes not derived from superClassq
	 */
	public Collection<Class<?>> filterOutClassesExtending(final Class<AbstractKiekerTest> superClass,
			final Collection<Class<?>> classes) {
		final Collection<Class<?>> results = new LinkedList<Class<?>>();

		for (final Class<?> clazz : classes) {
			if (!superClass.isAssignableFrom(clazz)) {
				results.add(clazz);
			}
		}

		return results;
	}

	/**
	 * Filter out classes which do not fit the fullly qualified class name pattern.
	 *
	 * @param pattern
	 *            class name pattern
	 * @param classes
	 *            collection of classes
	 * @return filtered collection of classes.
	 */
	protected Collection<Class<?>> filterOutClassesNotMatchingFullQualifiedClassNamePattern(final String pattern,
			final Collection<Class<?>> classes) {
		final Collection<Class<?>> results = new LinkedList<Class<?>>();

		for (final Class<?> clazz : classes) {
			if (clazz.getCanonicalName().matches(pattern)) {
				results.add(clazz);
			}
		}

		return results;
	}

}
