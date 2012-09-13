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
import org.eclipse.gmt.modisco.omg.kdm.code.Signature;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import kieker.tools.kdm.manager.KDMModelManager;
import kieker.tools.kdm.manager.exception.InvalidClassException;
import kieker.tools.kdm.manager.exception.InvalidMethodException;
import kieker.tools.kdm.manager.util.ParameterIterator;
import kieker.tools.kdm.manager.util.descriptions.MethodDescription;
import kieker.tools.kdm.manager.util.descriptions.ParameterDescription;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.tools.util.kdm.ProvidePackageStructure;

/**
 * @author Benjamin Harms
 */
public class ParameterIteratorTest extends AbstractKiekerTest {
	private final Map<String, MethodUnit> methods = ProvidePackageStructure.getMethods();

	public ParameterIteratorTest() {
		// Not necessary
	}

	@Test
	public void testNoParameterFromMethod() {
		final MethodUnit methodUnit = this.methods.get("test.zwei.sechs.IIterator.iterate()");
		final Iterator<ParameterDescription> it = new ParameterIterator(methodUnit);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(this.print(it.next()));
		}
		Assert.assertArrayEquals(new Object[] {}, l.toArray());
	}

	@Test
	public void testNoParameterFromSignature() {
		final MethodUnit methodUnit = this.methods.get("test.zwei.sechs.IIterator.iterate()");
		final Signature signature = (Signature) methodUnit.getCodeElement().get(0);
		final Iterator<ParameterDescription> it = new ParameterIterator(signature);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(this.print(it.next()));
		}
		Assert.assertArrayEquals(new Object[] {}, l.toArray());
	}

	@Test
	public void testSimpleParameter() {
		final MethodUnit methodUnit = this.methods.get("org.emf.kdm.Demo.showA()");
		final Iterator<ParameterDescription> it = new ParameterIterator(methodUnit);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(this.print(it.next()));
		}
		Assert.assertArrayEquals(new Object[] { "int count" }, l.toArray());
	}

	@Test
	public void testMultipleSimpleParameter() {
		final MethodUnit methodUnit = this.methods.get("org.emf.kdm.Demo.showB()");
		final Iterator<ParameterDescription> it = new ParameterIterator(methodUnit);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(this.print(it.next()));
		}
		Assert.assertArrayEquals(new Object[] { "char letter", "int count" }, l.toArray());
	}

	@Test
	public void testComplexParameter() {
		final MethodUnit methodUnit = this.methods.get("test.zwei.fuenf.Main.main()");
		final Iterator<ParameterDescription> it = new ParameterIterator(methodUnit);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(this.print(it.next()));
		}
		Assert.assertArrayEquals(new Object[] { "char[] args" }, l.toArray());
	}

	@Test
	public void testMultipleParameterWithComplex() {
		final MethodUnit methodUnit = this.methods.get("org.emf.abc.Input.Stream.open()");
		final Iterator<ParameterDescription> it = new ParameterIterator(methodUnit);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(this.print(it.next()));
		}
		Assert.assertArrayEquals(new Object[] { "char[] name", "int count" }, l.toArray());
	}

	@Test
	public void testClassTypeParameter() {
		final MethodUnit methodUnit = this.methods.get("org.emf.kdm.Demo.set()");
		final Iterator<ParameterDescription> it = new ParameterIterator(methodUnit);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(this.print(it.next()));
		}
		Assert.assertArrayEquals(new Object[] { "Item item" }, l.toArray());
	}

	@Test
	public void testGlobalMethodWithInterfaceType() {
		final MethodUnit methodUnit = this.methods.get("globalMethodNext()");
		final Iterator<ParameterDescription> it = new ParameterIterator(methodUnit);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(this.print(it.next()));
		}
		Assert.assertArrayEquals(new Object[] { "test.zwei.acht.IPrintable letter" }, l.toArray());
	}

	@Test
	public void testWithJPetStore() throws InvalidClassException, InvalidMethodException {
		final KDMModelManager modelManager = new KDMModelManager("examples/kdm/JPetStore.xmi");
		// Get some methods
		Iterator<MethodDescription> methodIterator = modelManager.iterateMethodsFromClass("org.mybatis.jpetstore.domain.Signon");
		Assert.assertTrue(methodIterator.hasNext());
		MethodDescription methodDescription = methodIterator.next();
		Assert.assertEquals("getUsername", methodDescription.getName());
		Assert.assertTrue(methodIterator.hasNext());
		methodDescription = methodIterator.next();
		Assert.assertEquals("setUsername", methodDescription.getName());
		// Get the parameter
		Iterator<ParameterDescription> parameterIterator = modelManager.iterateParameter(methodDescription.getMethodQualifier());
		final List<String> l = new LinkedList<String>();
		while (parameterIterator.hasNext()) {
			final ParameterDescription p = parameterIterator.next();
			l.add(this.print(p));
			// System.out.println(p);
		}
		Assert.assertArrayEquals(new String[] { "java.lang.String username" }, l.toArray());
		// Get some ather methods
		methodIterator = modelManager.iterateMethodsFromClass("org.mybatis.jpetstore.domain.Order");
		Assert.assertTrue(methodIterator.hasNext());
		Assert.assertEquals("getOrderId", methodIterator.next().getName());
		Assert.assertTrue(methodIterator.hasNext());
		Assert.assertEquals("setOrderId", methodIterator.next().getName());
		Assert.assertTrue(methodIterator.hasNext());
		Assert.assertEquals("getUsername", methodIterator.next().getName());
		Assert.assertTrue(methodIterator.hasNext());
		Assert.assertEquals("setUsername", methodIterator.next().getName());
		for (int i = 0; i < 50; i++) {
			Assert.assertTrue(methodIterator.hasNext());
			methodIterator.next();
		}
		Assert.assertTrue(methodIterator.hasNext());
		final MethodDescription methodDescriptionOrder = methodIterator.next();
		Assert.assertEquals("initOrder", methodDescriptionOrder.getName());
		// Get parameter
		parameterIterator = modelManager.iterateParameter(methodDescriptionOrder.getMethodQualifier());
		final List<String> lOrder = new LinkedList<String>();
		while (parameterIterator.hasNext()) {
			final ParameterDescription p = parameterIterator.next();
			lOrder.add(this.print(p));
			// System.out.println(p.getName());
		}
		Assert.assertArrayEquals(new String[] { "org.mybatis.jpetstore.domain.Account account", "org.mybatis.jpetstore.domain.Cart cart", }, lOrder.toArray());
	}

	@Test
	public void testMultiHasNextWithoutNext() {
		final MethodUnit methodUnit = this.methods.get("org.emf.abc.Input.Stream.open()");
		final Iterator<ParameterDescription> it = new ParameterIterator(methodUnit);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(this.print(it.next()));
		}
		Assert.assertArrayEquals(new Object[] { "char[] name", "int count", }, l.toArray());
	}

	@Ignore
	@Test
	public void testParameterFromCSharpMethod() throws InvalidClassException, InvalidMethodException {
		final KDMModelManager modelManager = new KDMModelManager("examples/kdm/SharpDevelop.xmi");
		// Get some methods
		final String key = "ICSharpCode.SharpDevelop.ProjectActiveConditionEvaluator";
		final Iterator<MethodDescription> it = modelManager.iterateMethodsFromClass(key);
		final List<String> l = new LinkedList<String>();
		if (it.hasNext()) {
			final String methodKey = it.next().getMethodQualifier();
			final Iterator<ParameterDescription> parameterIterator = modelManager.iterateParameter(methodKey);
			while (parameterIterator.hasNext()) {
				final String p = this.print(parameterIterator.next());
				l.add(p);
			}
		}
		final String[] values = new String[] { "object caller", "ICSharpCode.Cor.Condition condition", };
		Assert.assertArrayEquals(values, l.toArray());
	}

	private String print(final ParameterDescription parameterDescription) {
		final StringBuilder description = new StringBuilder();
		description.append(parameterDescription.getTypeName());
		if (parameterDescription.isArrayType()) {
			description.append("[]");
		}
		description.append(' ');
		description.append(parameterDescription.getName());
		// System.out.println(parameterDescription.getElementType().toString());

		return description.toString();
	}
}
