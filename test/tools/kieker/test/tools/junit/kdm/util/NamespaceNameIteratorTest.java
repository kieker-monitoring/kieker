package kieker.test.tools.junit.kdm.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;

import kieker.tools.kdm.manager.KDMModelManager;
import kieker.tools.kdm.manager.exception.InvalidNamespaceException;

public class NamespaceNameIteratorTest {
	@Test
	public void testLoad() {
		// Just to load everything
	}

	@Test
	public void testGlobalNamespaces() {
		final KDMModelManager modelManager = new KDMModelManager("..\\testdata\\NAnt-p1.xmi");
		try {
			final Iterator<String> it = modelManager.iterateNamespaces();
			final List<String> l = new LinkedList<String>();
			while (it.hasNext()) {
				final String n = it.next();
				l.add(n);
				System.out.println(n);
			}

			final String[] values = new String[] { "NAnt", "NDoc" };
			Assert.assertArrayEquals(values, l.toArray());
		} catch (final NoSuchElementException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testNoNamespaces() {
		final KDMModelManager modelManager = new KDMModelManager("..\\testdata\\NAnt-p1.xmi");
		try {
			final String key = "NDoc.Documenter.NAnt";
			final Iterator<String> it = modelManager.iterateNamespaces(key);
			final List<String> l = new LinkedList<String>();
			while (it.hasNext()) {
				final String name = it.next();
				l.add(name);
			}
			final String[] values = new String[] {};

			Assert.assertArrayEquals(values, l.toArray());
		} catch (final InvalidNamespaceException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSimpleNamespace() {
		final KDMModelManager modelManager = new KDMModelManager("..\\testdata\\NAnt-p1.xmi");
		try {
			final String key = "NAnt.MSNet";
			final Iterator<String> it = modelManager.iterateNamespaces(key);
			final List<String> l = new LinkedList<String>();
			while (it.hasNext()) {
				final String name = it.next();
				l.add(name);
			}
			final String[] values = new String[] { "NAnt.MSNet.Tasks" };

			Assert.assertArrayEquals(values, l.toArray());
		} catch (final InvalidNamespaceException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testMultipleNamespaces() {
		final KDMModelManager modelManager = new KDMModelManager("..\\testdata\\NAnt-p1.xmi");
		try {
			final String key = "NAnt.Core";
			final Iterator<String> it = modelManager.iterateNamespaces(key);
			final List<String> l = new LinkedList<String>();
			while (it.hasNext()) {
				final String name = it.next();
				l.add(name);
			}
			final String[] values = new String[] { "NAnt.Core.Attributes", "NAnt.Core.Configuration", "NAnt.Core.Extensibility", "NAnt.Core.Filters",
				"NAnt.Core.Functions", "NAnt.Core.Tasks", "NAnt.Core.Types", "NAnt.Core.Util" };

			Assert.assertArrayEquals(values, l.toArray());
		} catch (final InvalidNamespaceException e) {
			e.printStackTrace();
		}
	}
}
