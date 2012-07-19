package kieker.test.tools.junit.kdm.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.gmt.modisco.omg.kdm.code.CodeModel;
import org.eclipse.gmt.modisco.omg.kdm.code.Package;
import org.junit.Assert;
import org.junit.Test;

import kieker.tools.kdm.manager.util.ClassNameIterator;

/**
 * 
 * 
 * @author Benjamin Harms
 * 
 */
public class ClassNameIteratorTest {
	private final CodeModel codeModel = TestPackageStructure.getCodeModel();
	private final Map<String, Package> packages = TestPackageStructure.getPackages();

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
}
