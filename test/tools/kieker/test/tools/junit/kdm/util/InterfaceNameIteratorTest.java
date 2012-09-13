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

import org.eclipse.gmt.modisco.omg.kdm.code.CodeModel;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import kieker.tools.kdm.manager.KDMModelManager;
import kieker.tools.kdm.manager.exception.InvalidNamespaceException;
import kieker.tools.kdm.manager.util.InterfaceNameIterator;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.tools.util.kdm.ProvidePackageStructure;

/**
 * @author Benjamin Harms
 */
public class InterfaceNameIteratorTest extends AbstractKiekerTest {
	private final CodeModel codeModel = ProvidePackageStructure.getCodeModel();
	private final Map<String, Package> packages = ProvidePackageStructure.getPackages();

	public InterfaceNameIteratorTest() {
		// No code necessary.
	}

	@Test
	public void testNoInterface() {
		final String key = "test.zwei.drei";
		final Package clazz = this.packages.get(key);
		final Iterator<String> it = new InterfaceNameIterator(clazz, key);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}
		Assert.assertArrayEquals(new Object[] {}, l.toArray());
	}

	@Test
	public void testNoInterfacesButClasses() {
		final String key = "test.zwei.fuenf";
		final Package clazz = this.packages.get(key);
		final Iterator<String> it = new InterfaceNameIterator(clazz, key);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}
		Assert.assertArrayEquals(new Object[] {}, l.toArray());
	}

	@Test
	public void testNormalInterface() {
		final String key = "test.zwei.sechs";
		final Package clazz = this.packages.get(key);
		final Iterator<String> it = new InterfaceNameIterator(clazz, key);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}
		Assert.assertArrayEquals(new Object[] { "test.zwei.sechs.IIterator" }, l.toArray());
	}

	@Test
	public void testNestedInterfaces() {
		final String key = "test.zwei.acht";
		final Package clazz = this.packages.get(key);
		final Iterator<String> it = new InterfaceNameIterator(clazz, key);
		final Iterator<String> itDepthSearch = new InterfaceNameIterator(clazz, key, true);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}
		final List<String> lDepthSearch = new LinkedList<String>();
		while (itDepthSearch.hasNext()) {
			lDepthSearch.add(itDepthSearch.next());
		}
		Assert.assertArrayEquals(new Object[] { "test.zwei.acht.IPrintable" }, l.toArray());
		Assert.assertArrayEquals(new Object[] { "test.zwei.acht.IPrintable", "test.zwei.acht.IPrintable.IWritable" }, lDepthSearch.toArray());
	}

	@Test
	public void testInterfaceInClass() {
		final String key = "test.zwei.neun";
		final Package clazz = this.packages.get(key);
		final Iterator<String> it = new InterfaceNameIterator(clazz, key);
		final Iterator<String> itDepthSearch = new InterfaceNameIterator(clazz, key, true);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}
		final List<String> lDepthSearch = new LinkedList<String>();
		while (itDepthSearch.hasNext()) {
			lDepthSearch.add(itDepthSearch.next());
		}
		Assert.assertArrayEquals(new Object[] {}, l.toArray());
		Assert.assertArrayEquals(new Object[] { "test.zwei.neun.InterfaceClass.IGetable" }, lDepthSearch.toArray());
	}

	@Test
	public void testGlobalInterface() {
		final Iterator<String> it = new InterfaceNameIterator(this.codeModel.getCodeElement());
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}
		Assert.assertArrayEquals(new Object[] { "IGlobalInterface" }, l.toArray());
	}

	@Test
	public void testMultiHasNextWithoutNext() {
		final String key = "test.zwei.acht";
		final Package clazz = this.packages.get(key);
		final Iterator<String> it = new InterfaceNameIterator(clazz, key, true);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}
		Assert.assertArrayEquals(new Object[] { "test.zwei.acht.IPrintable", "test.zwei.acht.IPrintable.IWritable" }, l.toArray());
	}

	@Ignore
	@Test
	public void testSimpleInterfaceFromNamespace() throws InvalidNamespaceException {
		final KDMModelManager modelManager = new KDMModelManager("examples/kdm/NAnt-p1.xmi");
		final String key = "NAnt.NUnit1.Types";
		final Iterator<String> it = modelManager.iterateInterfacesFromNamespace(key);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			final String name = it.next();
			l.add(name);
		}
		final String[] values = new String[] { "NAnt.NUnit1.Types.IResultFormatter" };
		Assert.assertArrayEquals(values, l.toArray());
	}

	@Ignore
	@Test
	public void testMultipleInterfacesFromNamespace() throws InvalidNamespaceException {
		final KDMModelManager modelManager = new KDMModelManager("examples/kdm/NAnt-p1.xmi");
		final String key = "NAnt.Core";
		final Iterator<String> it = modelManager.iterateInterfacesFromNamespace(key);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			final String name = it.next();
			l.add(name);
		}
		final String[] values = new String[] { "NAnt.Core.IAttributeSetter", "NAnt.Core.IBuildListener", "NAnt.Core.IBuildLogger" };
		Assert.assertArrayEquals(values, l.toArray());
	}
}
