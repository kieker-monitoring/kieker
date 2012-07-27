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
