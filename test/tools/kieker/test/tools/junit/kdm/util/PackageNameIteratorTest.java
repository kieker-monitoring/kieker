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
import kieker.tools.kdm.manager.exception.InvalidPackageException;
import kieker.tools.kdm.manager.util.PackageNameIterator;

/**
 * 
 * @author Benjamin Harms
 * 
 */
public class PackageNameIteratorTest {
	private final CodeModel codeModel = TestPackageStructure.getCodeModel();
	private final Map<String, Package> packages = TestPackageStructure.getPackages();

	/**
	 * Default constructor.
	 */
	public PackageNameIteratorTest() {
		// No code necessary.
	}

	@Test
	public void testLoad() {
		// Just to load the data and correct the runtime for the next method.
	}

	@Test
	public void testNoPackage() {
		final String key = "test.eins";
		final Package pack = this.packages.get(key);
		final Iterator<String> it = new PackageNameIterator(pack, key, false);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}

		Assert.assertArrayEquals(new String[] {}, l.toArray());
	}

	@Test
	public void testBasePackages() {
		final Iterator<String> it = new PackageNameIterator(this.codeModel);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}

		Assert.assertArrayEquals(new String[] { "test", "org" }, l.toArray());
	}

	@Test
	public void testNormalPackage() {
		final String key = "test.zwei.drei";
		final Package pack = this.packages.get(key);
		final Iterator<String> it = new PackageNameIterator(pack, key, false);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}

		Assert.assertArrayEquals(new String[] { "test.zwei.drei.vier" }, l.toArray());
	}

	@Test
	public void testFullStructure() {
		// Normal
		final Iterator<String> it = new PackageNameIterator(this.codeModel);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}
		Assert.assertArrayEquals(new String[] { "test", "org" }, l.toArray());
		// Depth-first search
		final Iterator<String> itDepth = new PackageNameIterator(this.codeModel.getCodeElement(), true);
		final List<String> lDepth = new LinkedList<String>();
		while (itDepth.hasNext()) {
			lDepth.add(itDepth.next());
		}
		Assert.assertArrayEquals(new String[] { "test", "test.eins", "test.zwei", "test.zwei.drei", "test.zwei.drei.vier",
			"test.zwei.fuenf", "test.zwei.sechs", "test.zwei.sieben", "test.zwei.acht", "test.zwei.neun",
			"test.zwei.zehn", "org", "org.emf", "org.emf.kdm", "org.emf.abc", }, lDepth.toArray());
	}

	@Test
	public void testClassesOnly() {
		final String key = "test.zwei.fuenf";
		final Package pack = this.packages.get(key);
		final Iterator<String> it = new PackageNameIterator(pack, key, false);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			l.add(it.next());
		}

		Assert.assertArrayEquals(new String[] {}, l.toArray());
	}

	@Test
	public void testWithPetStore() {
		final KDMModelManager modelManager = new KDMModelManager("..\\testdata\\outJPetStore.xmi");
		try {
			// First level
			final Iterator<String> itOrg = modelManager.iteratePackages();
			Assert.assertTrue(itOrg.hasNext());
			final String orgNext = itOrg.next();
			Assert.assertEquals("org", orgNext);
			// Second level
			final Iterator<String> itMybatis = modelManager.iteratePackages(orgNext);
			Assert.assertTrue(itMybatis.hasNext());
			final String mybatisNext = itMybatis.next();
			Assert.assertEquals("org.mybatis", mybatisNext);
			// Third level
			final Iterator<String> itJpetstore = modelManager.iteratePackages(mybatisNext);
			Assert.assertTrue(itJpetstore.hasNext());
			final String jpetstoreNext = itJpetstore.next();
			Assert.assertEquals("org.mybatis.jpetstore", jpetstoreNext);
			// Fourth level
			final Iterator<String> it = modelManager.iteratePackages(jpetstoreNext);
			final List<String> l = new LinkedList<String>();
			while (it.hasNext()) {
				final String p = it.next();
				l.add(p);
			}

			Assert.assertArrayEquals(new String[] { "org.mybatis.jpetstore.domain", "org.mybatis.jpetstore.exclude", "org.mybatis.jpetstore.persistence",
				"org.mybatis.jpetstore.service", "org.mybatis.jpetstore.web" }, l.toArray());
		} catch (final InvalidPackageException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testBasePackagesWithPetStore() {
		final KDMModelManager modelManager = new KDMModelManager("..\\testdata\\outJPetStore.xmi");
		final Iterator<String> it = modelManager.iteratePackages();
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			final String p = it.next();
			l.add(p);
		}

		Assert.assertArrayEquals(new String[] { "java", "net", "javax", "org" }, l.toArray());
	}

	@Test
	public void testMultiHasNextWithoutNext() {
		final String key = "test.zwei";
		final Package pack = this.packages.get(key);
		final Iterator<String> it = new PackageNameIterator(pack, key, false);
		final List<String> l = new LinkedList<String>();
		while (it.hasNext()) {
			// A simple call of it.hasNext() may be useful sometimes, so test it
			it.hasNext();
			final String p = it.next();
			l.add(p);
		}

		Assert.assertArrayEquals(new String[] { "test.zwei.drei", "test.zwei.fuenf", "test.zwei.sechs", "test.zwei.sieben", "test.zwei.acht", "test.zwei.neun",
			"test.zwei.zehn" }, l.toArray());
	}
}
