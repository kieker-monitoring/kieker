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

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.logging.LogImplJUnit;

/**
 * This abstract class is the base for all other JUnit tests within the system.
 *
 * @author Jan Waller
 *
 * @since 1.6
 */
public abstract class AbstractKiekerTest { // NOPMD (no abstract methods)

	private static final Log LOG; // NOPMD (instantiation takes place in the static constructor)

	/** This rule makes sure that we can dump the name of the currently executed test on the screen. */
	@Rule
	public TestName nameOfCurrentTest = new TestName(); // NOPMD NOCS

	/**
	 * Workaround to let JUnit tests with relative references to files within the respective
	 * module execute in settings where they are execute as modules but also in settings
	 * where Kieker as executed as a whole. Background: In Gradle the working directory
	 * when executing tests is the subdirectory of the respective module, e.g., <i>kieker-common</i>;
	 * when executing within Eclipse, the working directory is Kieker's project root directory.
	 * This causes problems when relative paths within the modules are used by the tests.
	 *
	 * Note that the path ends with '/'.
	 */
	private volatile String testModulePrefix = "./";

	/**
	 * True if test executed inside the respective module (e.g., <i>kieker-common</i>),
	 * which is true for Gradle-based executions; false if the working directory is
	 * Kieker's root directory (e.g., in Eclipse).
	 *
	 */
	private volatile boolean workingDirectoryIsModuleDirectory;

	static {
		if (System.getProperty("kieker.common.logging.Log") == null) {
			System.setProperty("kieker.common.logging.Log", "JUNIT");
		}
		LOG = LogFactory.getLog(AbstractKiekerTest.class);
	}

	/**
	 * Default constructor.
	 */
	public AbstractKiekerTest() {
		// empty default constructor
	}

	/**
	 * This method writes in fact just the name of the test which is currently executed.
	 */
	@Before
	public final void printNameOfCurrentTest() {
		LOG.info("Executing test: " + this.getClass().getName() + "." + this.nameOfCurrentTest.getMethodName() + "()\n\n");
	}

	/**
	 * Initialize the infrastructure for the method ...
	 */
	@Before
	public final void initTestModulePrefix() {
		final String currentDir = System.getProperty("user.dir");
		this.workingDirectoryIsModuleDirectory = new File(currentDir + "/src").isDirectory();
		if (this.workingDirectoryIsModuleDirectory) { // in this case, we are fine and can use the paths right away
			this.testModulePrefix = "./";
		} else { // we need to find out whether in kieker-common, kieker-monitoring, kieker-tools, or kieker-analysis
			final String testClassName = this.getClass().getName();
			if (testClassName.startsWith("kieker.test.common")) {
				this.testModulePrefix = "kieker-common/";
			} else if (testClassName.startsWith("kieker.test.analysis")) {
				this.testModulePrefix = "kieker-analysis/";
			} else if (testClassName.startsWith("kieker.test.monitoring")) {
				this.testModulePrefix = "kieker-monitoring/";
			} else if (testClassName.startsWith("kieker.test.tools")) {
				this.testModulePrefix = "kieker-tools/";
			} else {
				LOG.error("Failed to detect test module for test: " + testClassName);
			}

		}
		LOG.info("Prefix for test resources is " + this.testModulePrefix);
	}

	protected String getTestModulePrefix() {
		return this.testModulePrefix;
	}

	/**
	 * Turns a path relative to the module directory (e.g., kieker-common) into
	 * a path relative to the working directory.
	 *
	 * @param path
	 *            the path relative to the module directory
	 * @return the converted path
	 */
	protected String modulePathToWorkingPath(final String path) {
		if (this.workingDirectoryIsModuleDirectory) {
			return path;
		}
		return this.testModulePrefix + path;
	}

	/**
	 * Turns a path relative to the working directory into a path relative to the module directory (e.g., kieker-common).
	 *
	 * @param path
	 *            the path relative to the working directory
	 * @return the converted path
	 */
	protected String workingPathToModulePath(final String path) {
		if (this.workingDirectoryIsModuleDirectory) {
			return path;
		}
		return path.substring(this.testModulePrefix.length()); // remove module prefix
	}

	/**
	 * This method resets the logger.
	 */
	@After
	public final void resetStateOfJUnitLogger() {
		LogImplJUnit.reset();
	}
}
