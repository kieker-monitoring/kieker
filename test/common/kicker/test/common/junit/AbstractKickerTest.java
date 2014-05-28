/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.test.common.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import kicker.common.logging.Log;
import kicker.common.logging.LogFactory;
import kicker.common.logging.LogImplJUnit;

/**
 * This abstract class is the base for all other JUnit tests within the system.
 * 
 * @author Jan Waller
 * 
 * @since 1.6
 */
public abstract class AbstractKickerTest { // NOPMD (no abstract methods)

	private static final Log LOG; // NOPMD

	static {
		if (System.getProperty("kicker.common.logging.Log") == null) {
			System.setProperty("kicker.common.logging.Log", "JUNIT");
		}
		LOG = LogFactory.getLog(AbstractKickerTest.class);
	}

	/** This rule makes sure that we can dump the name of the currently executed test on the screen. */
	@Rule
	public TestName nameOfCurrentTest = new TestName(); // NOPMD NOCS

	/**
	 * Default constructor.
	 */
	public AbstractKickerTest() {
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
	 * This method resets the logger.
	 */
	@After
	public final void resetStateOfJUnitLogger() {
		LogImplJUnit.reset();
	}
}
