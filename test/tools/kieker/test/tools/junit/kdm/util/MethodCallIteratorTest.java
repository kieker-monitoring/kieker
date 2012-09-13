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

package kieker.test.tools.junit.kdm.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.gmt.modisco.omg.kdm.code.MethodUnit;
import org.junit.Assert;
import org.junit.Test;

import kieker.tools.kdm.manager.util.MethodCallIterator;

/**
 * @author Nils Christian Ehmke, Benjamin Harms
 */
public class MethodCallIteratorTest {
	private final Map<String, MethodUnit> methodCalls = TestPackageStructure.getMethods();

	public MethodCallIteratorTest() {
		// No code necessary
	}

	@Test
	public void testNoMethodCalls() {
		final String key = "org.emf.kdm.Demo.showA()";
		final MethodUnit methodUnit = this.methodCalls.get(key);
		final Iterator<String> it = new MethodCallIterator(methodUnit);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}
		Assert.assertArrayEquals(l.toArray(), new String[] {});
	}

	@Test
	public void testSingleMethodCall() {
		final String key = "org.emf.kdm.Demo.showB()";
		final MethodUnit methodUnit = this.methodCalls.get(key);
		final Iterator<String> it = new MethodCallIterator(methodUnit);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}
		Assert.assertArrayEquals(new String[] { "org.emf.kdm.Demo.showB() org.emf.abc.Input.Stream.open()", }, l.toArray());
	}

	@Test
	public void testMultipleMethodCalls() {
		final String key = "test.zwei.fuenf.Main.main()";
		final MethodUnit methodUnit = this.methodCalls.get(key);
		final Iterator<String> it = new MethodCallIterator(methodUnit);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}
		Assert.assertArrayEquals(
				new String[] {
					"test.zwei.fuenf.Main.main() org.emf.kdm.Demo.showA()",
					"test.zwei.fuenf.Main.main() org.emf.abc.Input.Stream.open()",
					"test.zwei.fuenf.Main.main() globalMethod()",
				}, l.toArray());
	}

	@Test
	public void testMultiHasNextWithoutNext() {
		final String key = "test.zwei.fuenf.Main.main()";
		final MethodUnit methodUnit = this.methodCalls.get(key);
		final Iterator<String> it = new MethodCallIterator(methodUnit);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}
		Assert.assertArrayEquals(
				new String[] {
					"test.zwei.fuenf.Main.main() org.emf.kdm.Demo.showA()",
					"test.zwei.fuenf.Main.main() org.emf.abc.Input.Stream.open()",
					"test.zwei.fuenf.Main.main() globalMethod()",
				}, l.toArray());
	}
}
