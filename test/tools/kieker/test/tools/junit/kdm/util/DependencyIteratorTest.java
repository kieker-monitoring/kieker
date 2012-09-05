package kieker.test.tools.junit.kdm.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.gmt.modisco.omg.kdm.code.ClassUnit;
import org.eclipse.gmt.modisco.omg.kdm.code.InterfaceUnit;
import org.junit.Assert;
import org.junit.Test;

import kieker.tools.kdm.manager.KDMModelManager;
import kieker.tools.kdm.manager.exception.InvalidClassException;
import kieker.tools.kdm.manager.exception.InvalidInterfaceException;
import kieker.tools.kdm.manager.exception.InvalidNamespaceException;
import kieker.tools.kdm.manager.exception.InvalidPackageException;
import kieker.tools.kdm.manager.util.DependencyIterator;
import kieker.tools.kdm.manager.util.descriptions.DependencyDescription;

public class DependencyIteratorTest {
	private final Map<String, ClassUnit> classes = TestPackageStructure.getClasses();
	private final Map<String, InterfaceUnit> interfaces = TestPackageStructure.getInterfaces();

	public DependencyIteratorTest() {
		// Not necessary
	}

	@Test
	public void testLoad() {
		// Just to load the data and correct the runtime for the next method.
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
	public void testSimpleExtends2() {
		final KDMModelManager modelManager = new KDMModelManager("..\\testdata\\InterfaceExtends.xml");

		try {
			final Iterator<DependencyDescription> it = modelManager.iterateDependenciesFromInterface("test.Iterator");
			final List<String> l = new LinkedList<String>();
			while (it.hasNext()) {
				l.add(this.print(it.next()));
			}

			Assert.assertArrayEquals(new String[] { "EXTENDS INTERFACE test.Iterable" }, l.toArray());
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
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

		Assert.assertArrayEquals(new String[] { "IMPORTS CLASS org.emf.abc.Input.Stream", "IMPLEMENTS INTERFACE test.zwei.zehn.ISetable",
			"EXTENDS CLASS test.zwei.neun.InterfaceClass" }, l.toArray());
	}

	@Test
	public void testMultiHasNextWithoutNext() {
		final String key = "org.emf.kdm.Manager";
		final ClassUnit classUnit = this.classes.get(key);
		final Iterator<DependencyDescription> it = new DependencyIterator(classUnit);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			// A simple call of it.hasNext() may be useful sometimes, so test it
			it.hasNext();
			l.add(this.print(it.next()));
		}

		Assert.assertArrayEquals(new String[] { "IMPORTS CLASS org.emf.abc.Input.Stream", "IMPLEMENTS INTERFACE test.zwei.zehn.ISetable",
			"EXTENDS CLASS test.zwei.neun.InterfaceClass" }, l.toArray());
	}

	@Test
	public void testWithJPetStore() {
		final KDMModelManager modelManager = new KDMModelManager("..\\testdata\\outJPetStore.xmi");
		try {
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
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testParentPackageImport() {
		final KDMModelManager modelManager = new KDMModelManager("..\\testdata\\PackageDependecyTest_kdm.xmi");
		final String key = "count.impl";
		try {
			final Iterator<DependencyDescription> it = modelManager.iterateDependenciesFromPackage(key);
			final List<String> l = new LinkedList<String>();
			while (it.hasNext()) {
				final String d = this.print(it.next());
				l.add(d);
			}
			final String[] values = new String[] { "IMPORTS PACKAGE count" };

			Assert.assertArrayEquals(values, l.toArray());
		} catch (final InvalidPackageException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testMultiPackageSingleElementImport() {
		final KDMModelManager modelManager = new KDMModelManager("..\\testdata\\PackageDependecyTest_kdm.xmi");
		final String key = "string.impl";
		try {
			final Iterator<DependencyDescription> it = modelManager.iterateDependenciesFromPackage(key);
			final List<String> l = new LinkedList<String>();
			while (it.hasNext()) {
				final String d = this.print(it.next());
				l.add(d);
			}
			final String[] values = new String[] { "IMPORTS INTERFACE count.Countable", "IMPORTS INTERFACE string.Comparable" };

			Assert.assertArrayEquals(values, l.toArray());
		} catch (final InvalidPackageException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testMultiPackageImportWithSingleElementAndPackage() {
		final KDMModelManager modelManager = new KDMModelManager("..\\testdata\\PackageDependecyTest_kdm.xmi");
		final String key = "object.impl";
		try {
			final Iterator<DependencyDescription> it = modelManager.iterateDependenciesFromPackage(key);
			final List<String> l = new LinkedList<String>();
			while (it.hasNext()) {
				final String d = this.print(it.next());
				l.add(d);
			}
			final String[] values = new String[] { "IMPORTS INTERFACE object.Printable", "IMPORTS CLASS string.impl.StringImpl", "IMPORTS PACKAGE count" };

			Assert.assertArrayEquals(values, l.toArray());
		} catch (final InvalidPackageException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDependeciesFromCSharpClassImplements() {
		final KDMModelManager modelManager = new KDMModelManager("..\\testdata\\SharpDevelop.xmi");
		final String key = "ICSharpCode.CodeQualityAnalysis.Controls.DependencyIconVertexConverter";
		try {
			final Iterator<DependencyDescription> it = modelManager.iterateDependenciesFromClass(key);
			final List<String> l = new LinkedList<String>();
			while (it.hasNext()) {
				final String d = this.print(it.next());
				l.add(d);
			}
			final String[] values = new String[] { "IMPORTS NAMESPACE System", "IMPORTS NAMESPACE System.Windows.Data", "IMPORTS NAMESPACE System.Globalization",
				"IMPORTS NAMESPACE System.Windows.Media.Imaging", "IMPLEMENTS INTERFACE System.Windows.Data.IValueConverter" };

			Assert.assertArrayEquals(values, l.toArray());
		} catch (final InvalidClassException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDependeciesFromCSharpClassExtends() {
		final KDMModelManager modelManager = new KDMModelManager("..\\testdata\\SharpDevelop.xmi");
		final String key = "XmlEditor.Tests.Schema.AbstractElementTestFixture";
		try {
			final Iterator<DependencyDescription> it = modelManager.iterateDependenciesFromClass(key);
			final List<String> l = new LinkedList<String>();
			while (it.hasNext()) {
				final String d = this.print(it.next());
				l.add(d);
			}
			final String[] values = new String[] { "IMPORTS NAMESPACE System", "IMPORTS NAMESPACE ICSharpCode.SharpDevelop.Editor.CodeCompletion",
				"IMPORTS NAMESPACE ICSharpCode.XmlEditor", "IMPORTS NAMESPACE NUnit.Framework", "EXTENDS CLASS XmlEditor.Tests.Schema.SchemaTestFixtureBase" };

			Assert.assertArrayEquals(values, l.toArray());
		} catch (final InvalidClassException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDependeciesFromCSharpInterfaceExtends() {
		final KDMModelManager modelManager = new KDMModelManager("..\\testdata\\SharpDevelop.xmi");
		final String key = "ICSharpCode.SharpDevelop.Editor.CodeCompletion.IFancyCompletionItem";
		try {
			final Iterator<DependencyDescription> it = modelManager.iterateDependenciesFromInterface(key);
			final List<String> l = new LinkedList<String>();
			while (it.hasNext()) {
				final String d = this.print(it.next());
				l.add(d);
			}
			final String[] values = new String[] { "IMPORTS NAMESPACE System", "EXTENDS INTERFACE ICSharpCode.SharpDevelop.Editor.CodeCompletion.ICompletionItem" };

			Assert.assertArrayEquals(values, l.toArray());
		} catch (final InvalidInterfaceException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDependeciesFromCSharpNamespace() {
		final KDMModelManager modelManager = new KDMModelManager("..\\testdata\\SharpDevelop.xmi");
		final String key = "ICSharpCode.AvalonEdit";
		try {
			final Iterator<DependencyDescription> it = modelManager.iterateDependenciesFromNamespace(key);
			final List<String> l = new LinkedList<String>();
			while (it.hasNext()) {
				final String d = this.print(it.next());
				l.add(d);
			}
			final String[] values = new String[] { "IMPORTS NAMESPACE System", "IMPORTS NAMESPACE System.Windows.Input", "IMPORTS NAMESPACE System.ComponentModel",
				"IMPORTS NAMESPACE System.IO", "IMPORTS NAMESPACE System.Linq", "IMPORTS NAMESPACE System.Text", "IMPORTS NAMESPACE System.Windows",
				"IMPORTS NAMESPACE System.Windows.Controls", "IMPORTS NAMESPACE System.Windows.Controls.Primitives", "IMPORTS NAMESPACE System.Windows.Data",
				"IMPORTS NAMESPACE System.Windows.Markup", "IMPORTS NAMESPACE System.Windows.Media", "IMPORTS NAMESPACE System.Windows.Shapes",
				"IMPORTS NAMESPACE System.Windows.Threading", "IMPORTS NAMESPACE ICSharpCode.AvalonEdit.Document",
				"IMPORTS NAMESPACE ICSharpCode.AvalonEdit.Editing", "IMPORTS NAMESPACE ICSharpCode.AvalonEdit.Highlighting",
				"IMPORTS NAMESPACE ICSharpCode.AvalonEdit.Rendering", "IMPORTS NAMESPACE ICSharpCode.AvalonEdit.Utils",
				"IMPLEMENTS INTERFACE ICSharpCode.AvalonEdit.ITextEditorComponent" };

			Assert.assertArrayEquals(values, l.toArray());
		} catch (final InvalidNamespaceException e) {
			e.printStackTrace();
		}
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
