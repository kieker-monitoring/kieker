/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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
package kieker.java;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

/**
 * Tests which checks Java's enum behavior.
 * 
 * @author Christian Wulf
 * @since 1.14
 */
public class EnumTest {

	/**
	 * @author Christian Wulf
	 * @since 1.14
	 */
	static enum ExampleEnum {
		MONDAY, FRIDAY
	}

	public EnumTest() {
		// empty constructor
	}

	@Test
	public void twoValuesReturnDifferentInstances() {
		final ExampleEnum[] firstCallToValues = ExampleEnum.values();
		final ExampleEnum[] secondCallToValues = ExampleEnum.values();

		assertThat(firstCallToValues, is(not(sameInstance(secondCallToValues))));
		// => values() returns a new array on each call which can be a performance problem.
	}

	@Test
	public void twoGetEnumConstantsReturnDifferentInstances() {
		final Class<ExampleEnum> enumClass = ExampleEnum.class;
		final ExampleEnum[] firstCallToValues = enumClass.getEnumConstants();
		final ExampleEnum[] secondCallToValues = enumClass.getEnumConstants();

		assertThat(firstCallToValues, is(not(sameInstance(secondCallToValues))));
		// => getEnumConstants() returns a new array on each call which can be a performance problem.
	}
}
