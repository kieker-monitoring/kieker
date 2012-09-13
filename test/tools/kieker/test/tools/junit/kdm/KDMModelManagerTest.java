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

package kieker.test.tools.junit.kdm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Assert;
import org.junit.Test;

import kieker.tools.kdm.manager.KDMModelManager;
import kieker.tools.kdm.manager.exception.InvalidClassException;
import kieker.tools.kdm.manager.exception.InvalidPackageException;
import kieker.tools.kdm.manager.util.descriptions.MethodDescription;

/**
 * 
 * @author Nils Christian Ehmke, Benjamin Harms
 * 
 */
public class KDMModelManagerTest {

	/**
	 * Default constructor.
	 */
	public KDMModelManagerTest() {
		// No code necessary
	}

	@Test
	public void testThreads() throws InterruptedException {
		final int quantityElements = 10000;
		final int quantityThreads = 10;
		final List<String> compList = new ArrayList<String>();
		final List<String> resList = new ArrayList<String>();
		final Queue<String> queue = new ConcurrentLinkedQueue<String>();
		final Thread[] threads = new Thread[quantityThreads];
		final KDMModelManager manager = new KDMModelManager();

		for (int i = 0; i < quantityElements; i++) {
			queue.add("pack" + i);
			compList.add("pack" + i);
		}
		for (int i = 0; i < quantityThreads; i++) {
			threads[i] = new Thread() {
				@Override
				public void run() {
					while (true) {
						final String elem = queue.poll();
						if (elem == null) {
							return;
						} else {
							manager.addPackage(new String[] { elem });
						}
					}
				}
			};
		}

		for (int i = 0; i < quantityThreads; i++) {
			threads[i].start();
		}
		for (int i = 0; i < quantityThreads; i++) {
			threads[i].join();
		}

		final Iterator<String> packIter = manager.iteratePackages();
		while (packIter.hasNext()) {
			resList.add(packIter.next());
		}

		// Check for no lost updates and no duplication
		Assert.assertTrue(resList.containsAll(compList));
		Assert.assertTrue(compList.containsAll(resList));
	}

	@Test
	public void testAddNestedElement() throws InvalidPackageException, InvalidClassException {
		final KDMModelManager manager = new KDMModelManager();
		manager.addMethod(false, false, "", "", new String[0], "myMethod", new String[] { "class1", "class2" }, new String[] { "pack1", "pack2", "pack3" });

		final Iterator<String> packIter = manager.iteratePackages("pack1", true);
		final Iterator<String> classIter = manager.iterateClasses("pack1.pack2.pack3", true);
		final Iterator<MethodDescription> methodIter = manager.iterateMethodsFromClass("pack1.pack2.pack3.class1.class2");

		Assert.assertTrue(packIter.hasNext());
		Assert.assertEquals("pack1.pack2", packIter.next());
		Assert.assertTrue(packIter.hasNext());
		Assert.assertEquals("pack1.pack2.pack3", packIter.next());
		Assert.assertFalse(packIter.hasNext());

		Assert.assertTrue(classIter.hasNext());
		Assert.assertEquals("pack1.pack2.pack3.class1", classIter.next());
		Assert.assertTrue(classIter.hasNext());
		Assert.assertEquals("pack1.pack2.pack3.class1.class2", classIter.next());
		Assert.assertFalse(classIter.hasNext());

		Assert.assertTrue(methodIter.hasNext());
		Assert.assertEquals("myMethod", methodIter.next().getName());
		Assert.assertFalse(methodIter.hasNext());
	}

	@Test
	public void testAddEmptyPackage() {
		final KDMModelManager manager = new KDMModelManager();
		manager.addPackage(new String[0]);
		manager.addPackage(new String[] {});
		manager.addPackage(new String[] { "" });
		final Iterator<String> packIter = manager.iteratePackages();

		Assert.assertFalse(packIter.hasNext());
	}

	@Test
	public void testAddClassWithEmptyPackage() throws InvalidPackageException {
		final KDMModelManager manager = new KDMModelManager();
		manager.addClass(new String[] { "class" }, new String[] {});

		final Iterator<String> packIter = manager.iteratePackages();
		final Iterator<String> classIter = manager.iterateClasses("");

		Assert.assertFalse(packIter.hasNext());
		Assert.assertTrue(classIter.hasNext());
		Assert.assertEquals("class", classIter.next());
	}

	@Test
	public void testAddClassToInterface() throws InvalidPackageException {
		final KDMModelManager modelManager = new KDMModelManager();
		modelManager.addInterface(new String[] { "ISetable" }, new String[] { "test", "zwei", "zehn" });
		modelManager.addClass(new String[] { "ISetable", "ClassInterface" }, new String[] { "test", "zwei", "zehn" });
		final Iterator<String> iterator = modelManager.iterateClasses("test.zwei.zehn", true);
		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals(iterator.next(), "test.zwei.zehn.ISetable.ClassInterface");
		Assert.assertFalse(iterator.hasNext());
	}

	@Test
	public void testAddClassToNonexistingInterface() throws InvalidPackageException {
		final KDMModelManager modelManager = new KDMModelManager();
		modelManager.addClass(new String[] { "ISetable", "ClassInterface" }, new String[] { "test", "zwei", "zehn" });
		final Iterator<String> iterator = modelManager.iterateClasses("test.zwei.zehn", true);
		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals(iterator.next(), "test.zwei.zehn.ISetable");
		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals(iterator.next(), "test.zwei.zehn.ISetable.ClassInterface");
		Assert.assertFalse(iterator.hasNext());
	}

	@Test
	public void testAddInterfaceToClass() throws InvalidPackageException {
		final KDMModelManager modelManager = new KDMModelManager();
		modelManager.addClass(new String[] { "InterfaceClass" }, new String[] { "test", "zwei", "neun" });
		modelManager.addInterface(new String[] { "InterfaceClass", "IGetable" }, new String[] { "test", "zwei", "neun" });
		final Iterator<String> iterator = modelManager.iterateInterfaces("test.zwei.neun", true);
		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals(iterator.next(), "test.zwei.neun.InterfaceClass.IGetable");
		Assert.assertFalse(iterator.hasNext());
	}

	@Test
	public void testAddInterfaceToNonexistingClass() throws InvalidPackageException {
		final KDMModelManager modelManager = new KDMModelManager();
		modelManager.addInterface(new String[] { "InterfaceClass", "IGetable" }, new String[] { "test", "zwei", "neun" });
		final Iterator<String> iterator = modelManager.iterateInterfaces("test.zwei.neun", true);
		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals(iterator.next(), "test.zwei.neun.InterfaceClass");
		Assert.assertTrue(iterator.hasNext());
		Assert.assertEquals(iterator.next(), "test.zwei.neun.InterfaceClass.IGetable");
		Assert.assertFalse(iterator.hasNext());
	}

	@Test
	public void testInitializeHashMap() throws IOException, InvalidPackageException {
		final File tempFile = File.createTempFile("java", ".tmp");
		final KDMModelManager managerBefore = new KDMModelManager();

		// Put in some structure
		managerBefore.addPackage(new String[] { "test", "eins", });
		managerBefore.addPackage(new String[] { "test", "zwei", "drei", "vier", });
		managerBefore.addClass(new String[] { "Main", }, new String[] { "test", "zwei", "fuenf", });
		managerBefore.addClass(new String[] { "Foo", "Bar", }, new String[] { "test", "zwei", "sieben", });
		managerBefore.addClass(new String[] { "InterfaceClass", }, new String[] { "test", "zwei", "neun", });
		managerBefore.addClass(new String[] { "Demo", }, new String[] { "org", "emf", "kdm", });
		managerBefore.addClass(new String[] { "Input", "Stream", }, new String[] { "org", "emf", "abc", });
		managerBefore.addClass(new String[] { "GlobalClass", }, new String[] { "", });

		managerBefore.addInterface(new String[] { "IITerator", }, new String[] { "test", "zwei", "sechs", });
		managerBefore.addInterface(new String[] { "IPrintable", "IWritable", }, new String[] { "test", "zwei", "acht", });
		managerBefore.addInterface(new String[] { "ISetable", }, new String[] { "test", "zwei", "zehn", });
		managerBefore.addInterface(new String[] { "IGlobalInterface", }, new String[] { "", });

		managerBefore.addMethod(true, false, "void", "public", new String[] { "char[] args", }, "main", new String[] { "Main", }, new String[] { "test", "zwei",
			"fuenf", });
		managerBefore.addMethod(false, false, "void", "public", new String[] { "int count", }, "showA", new String[] { "Demo", },
				new String[] { "org", "emf", "kdm" });
		managerBefore.addMethod(false, false, "void", "public", new String[] { "char letter", "int count", }, "showB", new String[] { "Demo", }, new String[] {
			"org", "emf", "kdm", });
		managerBefore.addMethod(false, false, "int", "protected", new String[0], "showC", new String[] { "Demo", }, new String[] { "org", "emf", "kdm", });
		// Constructor
		managerBefore.addMethod(false, true, "", "public", new String[0], "Demo", new String[] { "Demo", }, new String[] { "org", "emf", "kdm", });
		managerBefore.addMethod(false, false, "int", "public", new String[] { "char[] name", "int count", }, "open", new String[] { "Input", "Stream", },
				new String[] { "org", "emf", "abc", });
		managerBefore.addMethod(false, false, "void", "public", new String[] { "Item item", }, "set", new String[] { "Demo", },
				new String[] { "org", "emf", "kdm", });
		managerBefore.addMethod(false, false, "void", "private", new String[0], "globalMethod", new String[] { "" }, new String[0]);
		// Method call
		managerBefore.addMethodCall(false, false, "void", "public", new String[] { "char[] args", }, "main", new String[] { "Main", }, new String[] { "test",
			"zwei", "fuenf", }, false, false, "void", "public", new String[] { "char letter", "int count", }, "showB", new String[] { "Demo", }, new String[] {
			"org", "emf", "kdm", });

		// Get all keys
		final List<String> insertedKeys = managerBefore.logHashMapKeys();
		// Save to file
		managerBefore.saveToFile(tempFile);

		// Reload it
		final KDMModelManager managerAfter = new KDMModelManager(tempFile);
		// Get keys again
		final List<String> loadedKeys = managerAfter.logHashMapKeys();
		final Object[] inKeyArray = insertedKeys.toArray();
		final Object[] loadedKeyArray = loadedKeys.toArray();
		// Debug
		// for (final String k : insertedKeys) {
		// System.out.println(k);
		// }
		// System.out.println();
		// for (final String k : loadedKeys) {
		// System.out.println(k);
		// }
		// Compare keys
		Assert.assertEquals(inKeyArray.length, loadedKeyArray.length);
		Assert.assertArrayEquals(inKeyArray, loadedKeyArray);
	}
}
