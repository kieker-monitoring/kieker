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

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.InterfaceUnit;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import kieker.tools.kdm.manager.KDMModelManager;
import kieker.tools.kdm.manager.exception.InvalidClassException;
import kieker.tools.kdm.manager.exception.InvalidInterfaceException;
import kieker.tools.kdm.manager.exception.InvalidNamespaceException;
import kieker.tools.kdm.manager.exception.InvalidPackageException;
import kieker.tools.kdm.manager.util.DependencyIterator;
import kieker.tools.kdm.manager.util.descriptions.DependencyDescription;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.tools.util.kdm.ProvidePackageStructure;

/**
 * @author Nils Christian Ehmke, Benjamin Harms
 */
public class DependencyIteratorTest extends AbstractKiekerTest {
	private final Map<String, ClassUnit> classes = ProvidePackageStructure.getClasses();
	private final Map<String, InterfaceUnit> interfaces = ProvidePackageStructure.getInterfaces();

	public DependencyIteratorTest() {
		// Not necessary
	}

	@Test
	public void testNoDependencies() {
		final String key = "test.zwei.sieben.Foo.Bar";
		final ClassUnit classUnit = this.classes.get(key);
		final Iterator<DependencyDescription> it = new DependencyIterator(classUnit);
		Assert.assertFalse(it.hasNext());
	}

	@Test
	public void testSimpleExtendsFromInterface() {
		final String key = "test.zwei.acht.IPrintable.IWritable";
		final InterfaceUnit interfaceUnit = this.interfaces.get(key);
		final Iterator<DependencyDescription> it = new DependencyIterator(interfaceUnit);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(this.print(it.next()));
		}
		Assert.assertArrayEquals(new String[] { "EXTENDS INTERFACE test.zwei.sechs.IIterator" }, l.toArray());
	}

	@Test
	public void testSimpleImport() {
		final String key = "test.zwei.fuenf.Main";
		final ClassUnit classUnit = this.classes.get(key);
		final Iterator<DependencyDescription> it = new DependencyIterator(classUnit);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(this.print(it.next()));
		}
		Assert.assertArrayEquals(new String[] { "IMPORTS INTERFACE test.zwei.acht.IPrintable" }, l.toArray());
	}

	@Test
	public void testSimpleExtends() {
		final String key = "GlobalClass";
		final ClassUnit classUnit = this.classes.get(key);
		final Iterator<DependencyDescription> it = new DependencyIterator(classUnit);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(this.print(it.next()));
		}
		Assert.assertArrayEquals(new String[] { "EXTENDS CLASS test.zwei.sieben.Foo" }, l.toArray());
	}

	@Test
	public void testSimpleExtends2() throws InvalidInterfaceException, IOException {
		final KDMModelManager modelManager = new KDMModelManager("examples/kdm/InterfaceExtends.xml");
		final Iterator<DependencyDescription> it = modelManager.iterateDependenciesFromInterface("test.Iterator");
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(this.print(it.next()));
		}
		Assert.assertArrayEquals(new String[] { "EXTENDS INTERFACE test.Iterable", }, l.toArray());
	}

	@Test
	public void testMultiDependenciesFromClass() {
		final String key = "org.emf.kdm.Manager";
		final ClassUnit classUnit = this.classes.get(key);
		final Iterator<DependencyDescription> it = new DependencyIterator(classUnit);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(this.print(it.next()));
		}
		Assert.assertArrayEquals(
				new String[] {
					"IMPORTS CLASS org.emf.abc.Input.Stream",
					"IMPLEMENTS INTERFACE test.zwei.zehn.ISetable",
					"EXTENDS CLASS test.zwei.neun.InterfaceClass",
				}, l.toArray());
	}

	@Test
	public void testMultiHasNextWithoutNext() {
		final String key = "org.emf.kdm.Manager";
		final ClassUnit classUnit = this.classes.get(key);
		final Iterator<DependencyDescription> it = new DependencyIterator(classUnit);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(this.print(it.next()));
		}
		Assert.assertArrayEquals(
				new String[] {
					"IMPORTS CLASS org.emf.abc.Input.Stream",
					"IMPLEMENTS INTERFACE test.zwei.zehn.ISetable",
					"EXTENDS CLASS test.zwei.neun.InterfaceClass",
				}, l.toArray());
	}

	@Test
	public void testWithJPetStore() throws InvalidClassException, InvalidInterfaceException {
		final KDMModelManager modelManager = new KDMModelManager("examples/kdm/JPetStore.xmi");
		// Class
		final String classKey = "org.mybatis.jpetstore.domain.Signon";
		Iterator<DependencyDescription> dependencyIterator = modelManager.iterateDependenciesFromClass(classKey);
		List<String> l = new LinkedList<String>();
		while (dependencyIterator.hasNext()) {
			l.add(this.print(dependencyIterator.next()));
		}
		Assert.assertArrayEquals(new String[] { "IMPORTS INTERFACE java.io.Serializable", "IMPLEMENTS INTERFACE java.io.Serializable" }, l.toArray());

		// Interface
		final String interfaceKey = "org.mybatis.jpetstore.persistence.CategoryMapper";
		dependencyIterator = modelManager.iterateDependenciesFromInterface(interfaceKey);
		l = new LinkedList<String>();
		while (dependencyIterator.hasNext()) {
			l.add(this.print(dependencyIterator.next()));
		}
		Assert.assertArrayEquals(new String[] { "IMPORTS UNKNOWN java.util.List<E>", "IMPORTS CLASS org.mybatis.jpetstore.domain.Category" }, l.toArray());
	}

	@Ignore
	@Test
	public void testParentPackageImport() throws InvalidPackageException {
		final KDMModelManager modelManager = new KDMModelManager("examples/kdm/PackageDependecyTest_kdm.xmi");
		final String key = "count.impl";

		final Iterator<DependencyDescription> it = modelManager.iterateDependenciesFromPackage(key);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			final String d = this.print(it.next());
			l.add(d);
		}
		final String[] values = new String[] { "IMPORTS PACKAGE count" };
		Assert.assertArrayEquals(values, l.toArray());
	}

	@Ignore
	@Test
	public void testMultiPackageSingleElementImport() throws InvalidPackageException {
		final KDMModelManager modelManager = new KDMModelManager("examples/kdm/PackageDependecyTest_kdm.xmi");
		final String key = "string.impl";

		final Iterator<DependencyDescription> it = modelManager.iterateDependenciesFromPackage(key);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			final String d = this.print(it.next());
			l.add(d);
		}
		final String[] values = new String[] { "IMPORTS INTERFACE count.Countable", "IMPORTS INTERFACE string.Comparable" };
		Assert.assertArrayEquals(values, l.toArray());
	}

	@Ignore
	@Test
	public void testMultiPackageImportWithSingleElementAndPackage() throws InvalidPackageException {
		final KDMModelManager modelManager = new KDMModelManager("examples/kdm/PackageDependecyTest_kdm.xmi");
		final String key = "object.impl";

		final Iterator<DependencyDescription> it = modelManager.iterateDependenciesFromPackage(key);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			final String d = this.print(it.next());
			l.add(d);
		}
		final String[] values = new String[] { "IMPORTS INTERFACE object.Printable", "IMPORTS CLASS string.impl.StringImpl", "IMPORTS PACKAGE count" };
		Assert.assertArrayEquals(values, l.toArray());
	}

	@Ignore
	@Test
	public void testDependeciesFromCSharpClassImplements() throws InvalidClassException {
		final KDMModelManager modelManager = new KDMModelManager("examples/kdm/SharpDevelop.xmi");
		final String key = "ICSharpCode.CodeQualityAnalysis.Controls.DependencyIconVertexConverter";
		final Iterator<DependencyDescription> it = modelManager.iterateDependenciesFromClass(key);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			final String d = this.print(it.next());
			l.add(d);
		}
		final String[] values = new String[] {
			"IMPORTS NAMESPACE System",
			"IMPORTS NAMESPACE System.Windows.Data",
			"IMPORTS NAMESPACE System.Globalization",
			"IMPORTS NAMESPACE System.Windows.Media.Imaging",
			"IMPLEMENTS INTERFACE System.Windows.Data.IValueConverter",
		};
		Assert.assertArrayEquals(values, l.toArray());
	}

	@Ignore
	@Test
	public void testDependeciesFromCSharpClassExtends() throws InvalidClassException {
		final KDMModelManager modelManager = new KDMModelManager("examples/kdm/SharpDevelop.xmi");
		final String key = "XmlEditor.Tests.Schema.AbstractElementTestFixture";
		final Iterator<DependencyDescription> it = modelManager.iterateDependenciesFromClass(key);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			final String d = this.print(it.next());
			l.add(d);
		}
		final String[] values = new String[] {
			"IMPORTS NAMESPACE System",
			"IMPORTS NAMESPACE ICSharpCode.SharpDevelop.Editor.CodeCompletion",
			"IMPORTS NAMESPACE ICSharpCode.XmlEditor",
			"IMPORTS NAMESPACE NUnit.Framework",
			"EXTENDS CLASS XmlEditor.Tests.Schema.SchemaTestFixtureBase",
		};
		Assert.assertArrayEquals(values, l.toArray());
	}

	@Ignore
	@Test
	public void testDependeciesFromCSharpInterfaceExtends() throws InvalidInterfaceException {
		final KDMModelManager modelManager = new KDMModelManager("examples/kdm/SharpDevelop.xmi");
		final String key = "ICSharpCode.SharpDevelop.Editor.CodeCompletion.IFancyCompletionItem";
		final Iterator<DependencyDescription> it = modelManager.iterateDependenciesFromInterface(key);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			final String d = this.print(it.next());
			l.add(d);
		}
		final String[] values = new String[] { "IMPORTS NAMESPACE System", "EXTENDS INTERFACE ICSharpCode.SharpDevelop.Editor.CodeCompletion.ICompletionItem", };
		Assert.assertArrayEquals(values, l.toArray());
	}

	@Ignore
	@Test
	public void testDependeciesFromCSharpNamespace() throws InvalidNamespaceException {
		final KDMModelManager modelManager = new KDMModelManager("examples/kdm/SharpDevelop.xmi");
		final String key = "ICSharpCode.AvalonEdit";
		final Iterator<DependencyDescription> it = modelManager.iterateDependenciesFromNamespace(key);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			final String d = this.print(it.next());
			l.add(d);
		}
		final String[] values = new String[] {
			"IMPORTS NAMESPACE System",
			"IMPORTS NAMESPACE System.Windows.Input",
			"IMPORTS NAMESPACE System.ComponentModel",
			"IMPORTS NAMESPACE System.IO",
			"IMPORTS NAMESPACE System.Linq",
			"IMPORTS NAMESPACE System.Text",
			"IMPORTS NAMESPACE System.Windows",
			"IMPORTS NAMESPACE System.Windows.Controls",
			"IMPORTS NAMESPACE System.Windows.Controls.Primitives",
			"IMPORTS NAMESPACE System.Windows.Data",
			"IMPORTS NAMESPACE System.Windows.Markup",
			"IMPORTS NAMESPACE System.Windows.Media",
			"IMPORTS NAMESPACE System.Windows.Shapes",
			"IMPORTS NAMESPACE System.Windows.Threading",
			"IMPORTS NAMESPACE ICSharpCode.AvalonEdit.Document",
			"IMPORTS NAMESPACE ICSharpCode.AvalonEdit.Editing",
			"IMPORTS NAMESPACE ICSharpCode.AvalonEdit.Highlighting",
			"IMPORTS NAMESPACE ICSharpCode.AvalonEdit.Rendering",
			"IMPORTS NAMESPACE ICSharpCode.AvalonEdit.Utils",
			"IMPLEMENTS INTERFACE ICSharpCode.AvalonEdit.ITextEditorComponent",
		};
		Assert.assertArrayEquals(values, l.toArray());
	}

	private String print(final DependencyDescription description) {
		final StringBuilder line = new StringBuilder();
		if (description != null) {
			line.append(description.getDependencyType()).append(" ");
			line.append(description.getElementType()).append(" ");
			line.append(description.getFullName());
		}
		return line.toString();
	}
}
