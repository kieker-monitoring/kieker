/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.logging.LogImplJUnit;

/**
 * @author Jan Waller
 */
public abstract class AbstractKiekerTest { // NOPMD (no abstract methods)

	private static final Log LOG;

	static {
		if (System.getProperty("kieker.common.logging.Log") == null) {
			System.setProperty("kieker.common.logging.Log", "JUNIT");
		}
		LOG = LogFactory.getLog(AbstractKiekerTest.class);
	}

	@Rule
	public TestName nameOfCurrentTest = new TestName(); // NOCS

	public AbstractKiekerTest() {
		// empty default constructor
	}

	@Before
	public final void printNameOfCurrentTest() {
		LOG.info("Executing test: " + this.getClass().getName() + "." + this.nameOfCurrentTest.getMethodName() + "()\n\n");
	}

	@After
	public final void resetStateOfJUnitLogger() {
		LogImplJUnit.reset();
	}
}
