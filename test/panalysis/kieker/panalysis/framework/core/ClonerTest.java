/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
package kieker.panalysis.framework.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class ClonerTest {

	@Test
	public void test() throws ReflectiveOperationException {
		final JavaBean original = new JavaBean(42);
		final JavaBean clone = Cloner.cloneObject(original);

		Assert.assertEquals(original.getValue(), clone.getValue());
	}

	private static class JavaBean {

		private int value;

		@SuppressWarnings("unused")
		public JavaBean() {}

		public JavaBean(final int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}

		@SuppressWarnings("unused")
		public void setValue(final int value) {
			this.value = value;
		}

	}

}
