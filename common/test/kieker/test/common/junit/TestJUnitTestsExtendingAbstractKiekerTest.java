/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

import java.util.Collection;

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
public class TestJUnitTestsExtendingAbstractKiekerTest extends AbstractDynamicKiekerTest {

	/**
	 * Default constructor.
	 */
	public TestJUnitTestsExtendingAbstractKiekerTest() {
		// empty default constructor
	}

	/**
	 * Check whether all test inherit AbstractDynamicKiekerTest.
	 *
	 * @throws ClassNotFoundException
	 *             in case a class is not found
	 */
	@Test
	public void test() throws ClassNotFoundException {
		final Collection<Class<?>> availableClasses = super.deliverAllAvailableClassesFromTestDirectoryInJUnitPackage();
		final Collection<Class<?>> filteredClasses = super.filterOutClassesExtending(AbstractKiekerTest.class, availableClasses);

		Assert.assertTrue("Following classes should extend AbstractKiekerTest: " + filteredClasses.toString(), filteredClasses.isEmpty());
	}
}
