/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.util.map;

import java.util.concurrent.ConcurrentHashMap;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @author Nils Christian Ehmke
 *
 * @since 1.10
 *
 * @param <K>
 *            The type of the keys.
 * @param <V>
 *            The type of the values.
 */
@SuppressFBWarnings(value = "EQ_DOESNT_OVERRIDE_EQUALS") // inherit equals to follow Map's definition of equality 
public class BoundedConcurrentHashMap<K, V> extends ConcurrentHashMap<K, V> {

	private static final long serialVersionUID = 1L;

	private final BoundedCacheBehaviour boundedCacheBehaviour;
	private final int maxCacheSize;

	/**
	 * Initialize a bounded concurrent hash map.
	 *
	 * @param boundedCacheBehaviour
	 *            set the cache behavior
	 * @param maxCacheSize
	 *            define limit of the cache
	 */
	public BoundedConcurrentHashMap(final BoundedCacheBehaviour boundedCacheBehaviour, final int maxCacheSize) {
		super();

		this.boundedCacheBehaviour = boundedCacheBehaviour;
		this.maxCacheSize = maxCacheSize;
	}

	/**
	 * Works like the overriden method, except that {@code null} is returned, if the given element could not be added
	 * due to map limitations.
	 *
	 * @param key
	 *            key-value
	 * @param value
	 *            the associated value
	 *
	 * @return the previous value associated with key, or null if there was no mapping for key, or null if the bounds
	 *         limit was reached
	 */
	@Override
	public V put(final K key, final V value) {
		if (this.checkBounds()) {
			return super.put(key, value);
		} else {
			return null;
		}
	}

	/**
	 * Works like the overriden method, except that {@code null} is returned, if the given element could not be added
	 * due to map limitations.
	 *
	 * @param key
	 *            key-value
	 * @param value
	 *            the associated value
	 *
	 * @return the previous value or null
	 */
	@Override
	public V putIfAbsent(final K key, final V value) {
		if (this.checkBounds()) {
			return super.putIfAbsent(key, value);
		} else {
			return null;
		}
	}

	private boolean checkBounds() {
		final boolean elementCanBeAdded;

		switch (this.boundedCacheBehaviour) {
		case IGNORE_NEW_ENTRIES:
			elementCanBeAdded = (this.size() < this.maxCacheSize);
			break;
		case REMOVE_RANDOM_ENTRY:
			if (this.size() >= this.maxCacheSize) {
				this.remove(this.keys().nextElement());
			}
			elementCanBeAdded = true;
			break;
		case CLEAR_CACHE:
			if (this.size() >= this.maxCacheSize) {
				this.clear();
			}
			elementCanBeAdded = true;
			break;
		default:
			// Ignored
			elementCanBeAdded = true;
			break;
		}

		return elementCanBeAdded;
	}

	/**
	 * @author Nils Christian Ehmke
	 *
	 * @since 1.10
	 */
	public enum BoundedCacheBehaviour {
		IGNORE_NEW_ENTRIES, REMOVE_RANDOM_ENTRY, CLEAR_CACHE,
	}

}
