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
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;

import kieker.tools.kdm.manager.KDMModelManager;
import kieker.tools.kdm.manager.exception.InvalidNamespaceException;

/**
 * @author Nils Christian Ehmke, Benjamin Harms
 */
public class NamespaceNameIteratorTest {

	public NamespaceNameIteratorTest() {
		// empty default constructor
	}

	@Test
	public void testGlobalNamespaces() {
		final KDMModelManager modelManager = new KDMModelManager("tmp/NAnt-p1.xmi");
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
		final KDMModelManager modelManager = new KDMModelManager("tmp/NAnt-p1.xmi");
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
		final KDMModelManager modelManager = new KDMModelManager("tmp/NAnt-p1.xmi");
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
		final KDMModelManager modelManager = new KDMModelManager("tmp/NAnt-p1.xmi");
		try {
			final String key = "NAnt.Core";
			final Iterator<String> it = modelManager.iterateNamespaces(key);
			final List<String> l = new LinkedList<String>();
			while (it.hasNext()) {
				final String name = it.next();
				l.add(name);
			}
			final String[] values = new String[] {
				"NAnt.Core.Attributes",
				"NAnt.Core.Configuration",
				"NAnt.Core.Extensibility",
				"NAnt.Core.Filters",
				"NAnt.Core.Functions",
				"NAnt.Core.Tasks",
				"NAnt.Core.Types",
				"NAnt.Core.Util",
			};

			Assert.assertArrayEquals(values, l.toArray());
		} catch (final InvalidNamespaceException e) {
			e.printStackTrace();
		}
	}
}
