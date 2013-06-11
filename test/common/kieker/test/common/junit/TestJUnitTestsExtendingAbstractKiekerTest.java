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

package kieker.test.common.junit;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.cxf.helpers.FileUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * This JUnit test makes sure that all JUnit tests within Kieker extend the {@link AbstractKiekerTest}. The tests in question will be found using two criteria: They
 * have to be in a package containing the subpackage {@code junit} and their classname has to contain {@code Test}.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.7
 */
public class TestJUnitTestsExtendingAbstractKiekerTest extends AbstractKiekerTest {

	private static final String PATTERN_JUNIT_PACKAGE = "\\junit\\";
	private static final String DIR_NAME_TEST = "test";
	private static final String PATTERN_TEST_SOURCE_FILES = ".*Test.*java";

	/**
	 * Default constructor.
	 */
	public TestJUnitTestsExtendingAbstractKiekerTest() {
		// empty default constructor
	}

	@Test
	public void test() throws IOException, ClassNotFoundException {
		final List<File> sourceFiles = TestJUnitTestsExtendingAbstractKiekerTest.listJavaSourceFilesFromTests();
		for (final File sourceFile : sourceFiles) {
			if (TestJUnitTestsExtendingAbstractKiekerTest.isSourceFileInJUnitPackage(sourceFile)) {
				final String className = TestJUnitTestsExtendingAbstractKiekerTest.sourceFileToClassName(sourceFile);
				final Class<?> clazz = this.getClass().getClassLoader().loadClass(className);
				if (!TestJUnitTestsExtendingAbstractKiekerTest.doesClassExtendKiekerAbstractTest(clazz)) {
					Assert.fail("Class '" + className + "' doesn't extend AbstractKiekerTest");
				}
			}
		}
	}

	private static List<File> listJavaSourceFilesFromTests() {
		return FileUtils.getFilesRecurse(new File(DIR_NAME_TEST), PATTERN_TEST_SOURCE_FILES);
	}

	private static boolean isSourceFileInJUnitPackage(final File file) {
		return file.getAbsolutePath().contains(PATTERN_JUNIT_PACKAGE);
	}

	private static boolean doesClassExtendKiekerAbstractTest(final Class<?> clazz) {
		return AbstractKiekerTest.class.isAssignableFrom(clazz);
	}

	private static String sourceFileToClassName(final File file) {
		final String pathName = file.getPath();
		String className = pathName.substring(0, pathName.length() - 5).replace(File.separator, ".");
		final int secondPointPos = className.indexOf('.', 5);
		className = className.substring(secondPointPos + 1);

		return className;
	}
}
