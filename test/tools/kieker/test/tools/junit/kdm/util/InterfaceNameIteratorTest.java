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
import org.junit.Test;

import kieker.tools.kdm.manager.KDMModelManager;
import kieker.tools.kdm.manager.exception.InvalidNamespaceException;
import kieker.tools.kdm.manager.util.InterfaceNameIterator;

/**
 * 
 * @author Benjamin Harms
 * 
 */
public class InterfaceNameIteratorTest {
	private final CodeModel codeModel = TestPackageStructure.getCodeModel();
	private final Map<String, Package> packages = TestPackageStructure.getPackages();
	private final KDMModelManager modelManager = new KDMModelManager("tmp/NAnt-p1.xmi");

	/**
	 * Default constructor.
	 */
	public InterfaceNameIteratorTest() {
		// No code necessary.
	}

	@Test
	public void testLoad() {
		// Just to load the data and correct the runtime for the next method.
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
			// A simple call of it.hasNext() may be useful sometimes, so test it
			it.hasNext();
			l.add(it.next());
		}

		Assert.assertArrayEquals(new Object[] { "test.zwei.acht.IPrintable", "test.zwei.acht.IPrintable.IWritable" }, l.toArray());
	}

	@Test
	public void testSimpleInterfaceFromNamespace() {
		try {
			final String key = "NAnt.NUnit1.Types";
			final Iterator<String> it = this.modelManager.iterateInterfacesFromNamespace(key);
			final List<String> l = new LinkedList<String>();
			while (it.hasNext()) {
				final String name = it.next();
				l.add(name);
			}
			final String[] values = new String[] { "NAnt.NUnit1.Types.IResultFormatter" };

			Assert.assertArrayEquals(values, l.toArray());
		} catch (final InvalidNamespaceException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testMultipleInterfacesFromNamespace() {
		try {
			final String key = "NAnt.Core";
			final Iterator<String> it = this.modelManager.iterateInterfacesFromNamespace(key);
			final List<String> l = new LinkedList<String>();
			while (it.hasNext()) {
				final String name = it.next();
				l.add(name);
			}
			final String[] values = new String[] { "NAnt.Core.IAttributeSetter", "NAnt.Core.IBuildListener", "NAnt.Core.IBuildLogger" };

			Assert.assertArrayEquals(values, l.toArray());
		} catch (final InvalidNamespaceException e) {
			e.printStackTrace();
		}
	}
}
