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
package kieker.panalysis.framework.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.panalysis.framework.util.Cloner;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class ClonerTest {

	private JavaBean original;
	private JavaBean clone;

	@Before
	public void createOriginalnstanceAndCloneIt() throws Exception {
		this.original = new JavaBean();
		this.original.setValue(42);

		this.clone = Cloner.cloneObject(this.original);
	}

	@Test
	public void testInstancesShouldNotBeEqual() {
		Assert.assertTrue("The cloned instance is the same as the original instance.", this.original != this.clone);
	}

	@Test
	public void testInstancesShouldContainSameValues() {
		Assert.assertEquals("The cloned instance does not contain the same values as the original instance.", this.original.getValue(), this.clone.getValue());
	}

	private static class JavaBean {

		private int value;

		public JavaBean() {}

		public int getValue() {
			return this.value;
		}

		public void setValue(final int value) {
			this.value = value;
		}

	}

}
