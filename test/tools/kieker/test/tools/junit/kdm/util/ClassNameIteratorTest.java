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
import kieker.tools.kdm.manager.util.ClassNameIterator;

/**
 * @author Benjamin Harms
 */
public class ClassNameIteratorTest {
	private final CodeModel codeModel = TestPackageStructure.getCodeModel();
	private final Map<String, Package> packages = TestPackageStructure.getPackages();
	private final KDMModelManager modelManager = new KDMModelManager("tmp/NAnt-p1.xmi");

	/**
	 * Default constructor.
	 */
	public ClassNameIteratorTest() {
		// No code necessary.
	}

	@Test
	public void testLoad() {
		// Just to load the data and correct the runtime for the next method.
	}

	@Test
	public void testNoClasses() {
		final String key = "test.zwei.drei";
		final Package pack = this.packages.get(key);
		final Iterator<String> it = new ClassNameIterator(pack, key);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}

		Assert.assertArrayEquals(new Object[] {}, l.toArray());
	}

	@Test
	public void testNoClassesButInterfaces() {
		final String key = "test.zwei.sechs";
		final Package pack = this.packages.get(key);
		final Iterator<String> it = new ClassNameIterator(pack, key);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}

		Assert.assertArrayEquals(new Object[] {}, l.toArray());
	}

	@Test
	public void testNormalClass() {
		final String key = "test.zwei.fuenf";
		final Package pack = this.packages.get(key);
		final Iterator<String> it = new ClassNameIterator(pack, key);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}

		Assert.assertArrayEquals(new Object[] { "test.zwei.fuenf.Main" }, l.toArray());
	}

	@Test
	public void testNestedClasses() {
		final String key = "test.zwei.sieben";
		final Package pack = this.packages.get(key);
		final Iterator<String> it = new ClassNameIterator(pack, key);
		final Iterator<String> itDepthSearch = new ClassNameIterator(pack, key, true);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}

		final List<String> lDepthSearch = new LinkedList<String>();
		while (itDepthSearch.hasNext()) {
			lDepthSearch.add(itDepthSearch.next());
		}

		Assert.assertArrayEquals(new Object[] { "test.zwei.sieben.Foo" }, l.toArray());
		Assert.assertArrayEquals(new Object[] { "test.zwei.sieben.Foo", "test.zwei.sieben.Foo.Bar" }, lDepthSearch.toArray());
	}

	@Test
	public void testClassInInterface() {
		final String key = "test.zwei.zehn";
		final Package pack = this.packages.get(key);
		final Iterator<String> it = new ClassNameIterator(pack, key);
		final Iterator<String> itDepthSearch = new ClassNameIterator(pack, key, true);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}

		final List<String> lDepthSearch = new LinkedList<String>();
		while (itDepthSearch.hasNext()) {
			lDepthSearch.add(itDepthSearch.next());
		}

		Assert.assertArrayEquals(new Object[] {}, l.toArray());
		Assert.assertArrayEquals(new Object[] { "test.zwei.zehn.ISetable.ClassInterface" }, lDepthSearch.toArray());
	}

	@Test
	public void testGlobalClass() {
		final Iterator<String> it = new ClassNameIterator(this.codeModel.getCodeElement());
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}

		Assert.assertArrayEquals(new Object[] { "GlobalClass" }, l.toArray());
	}

	@Test
	public void testMultiHasNextWithoutNext() {
		final String key = "test.zwei.sieben";
		final Package pack = this.packages.get(key);
		final Iterator<String> it = new ClassNameIterator(pack, key, true);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			// A simple call of it.hasNext() may be useful sometimes, so test it
			it.hasNext();
			l.add(it.next());
		}

		Assert.assertArrayEquals(new Object[] { "test.zwei.sieben.Foo", "test.zwei.sieben.Foo.Bar" }, l.toArray());
	}

	@Test
	public void testSimpleClassFromNamespace() {
		try {
			final String key = "NAnt.Win32.Functions";
			final Iterator<String> it = this.modelManager.iterateClassesFromNamespace(key);
			final List<String> l = new LinkedList<String>();
			while (it.hasNext()) {
				final String name = it.next();
				l.add(name);
			}
			final String[] values = new String[] { "NAnt.Win32.Functions.CygpathFunctions" };

			Assert.assertArrayEquals(values, l.toArray());
		} catch (final InvalidNamespaceException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testMultipleClassesFromNamespace() {
		try {
			final String key = "NAnt.SourceControl.Tasks";
			final Iterator<String> it = this.modelManager.iterateClassesFromNamespace(key);
			final List<String> l = new LinkedList<String>();
			while (it.hasNext()) {
				final String name = it.next();
				l.add(name);
			}
			final String[] values = new String[] {
				"NAnt.SourceControl.Tasks.AbstractCvsTask",
				"NAnt.SourceControl.Tasks.AbstractSourceControlTask",
				"NAnt.SourceControl.Tasks.ChangeLogTask",
				"NAnt.SourceControl.Tasks.CheckoutTask",
				"NAnt.SourceControl.Tasks.CvsPass",
				"NAnt.SourceControl.Tasks.CvsTask",
				"NAnt.SourceControl.Tasks.ExportTask",
				"NAnt.SourceControl.Tasks.RTagTask",
				"NAnt.SourceControl.Tasks.TagTask",
				"NAnt.SourceControl.Tasks.UpdateTask",
			};

			Assert.assertArrayEquals(values, l.toArray());
		} catch (final InvalidNamespaceException e) {
			e.printStackTrace();
		}
	}
}
