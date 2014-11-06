/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.common.junit.util.map;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.util.map.BoundedConcurrentHashMap;
import kieker.common.util.map.BoundedConcurrentHashMap.BoundedCacheBehaviour;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Nils Christian Ehmke
 *
 * @since 1.10
 */
public class BoundedConcurrentHashMapTest extends AbstractKiekerTest {

	/**
	 * Test class constructor.
	 */
	public BoundedConcurrentHashMapTest() {
		// No code necessary
	}

	/**
	 * Test if inserted values make it to the hash map.
	 */
	@Test
	public void testMaxEntryBehaviour() {
		final BoundedConcurrentHashMap<Integer, Integer> map = new BoundedConcurrentHashMap<Integer, Integer>(BoundedCacheBehaviour.IGNORE_NEW_ENTRIES, 5);

		for (int i = 0; i < 10; i++) {
			map.put(i, i);
		}

		for (int i = 0; i < 5; i++) {
			Assert.assertTrue("Bounded map does not contain expected entry (" + i + ")", map.contains(i));
		}
		Assert.assertEquals("Bounded map contains wrong number of entries", 5, map.size());
	}

	@Test
	public void testRandomRemoveBehaviour() {
		final BoundedConcurrentHashMap<Integer, Integer> map = new BoundedConcurrentHashMap<Integer, Integer>(BoundedCacheBehaviour.REMOVE_RANDOM_ENTRY, 5);

		for (int i = 0; i < 10; i++) {
			map.put(i, i);
		}

		Assert.assertEquals("Bounded map contains wrong number of entries", 5, map.size());
	}

	@Test
	public void testClearBehaviour() {
		final BoundedConcurrentHashMap<Integer, Integer> map = new BoundedConcurrentHashMap<Integer, Integer>(BoundedCacheBehaviour.CLEAR_CACHE, 5);

		for (int i = 0; i < 6; i++) {
			map.put(i, i);
		}

		Assert.assertEquals("Bounded map contains wrong number of entries", 1, map.size());
		Assert.assertTrue("Bounded map does not contain expected entry (5)", map.contains(5));
	}

}
