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

package kieker.common.util;

import java.util.Map;

/**
 * Java's SimpleImmutableEntry, as in implementation of {@link java.util.Map.Entry}, is available for Java 1.6 or higher.
 * Hence, we provide our own implementation here.
 * 
 * @author Andre van Hoorn
 * 
 * @param <K>
 * @param <V>
 */
public class ImmutableEntry<K, V> implements Map.Entry<K, V> {

	private final K key;
	private final V value;

	public ImmutableEntry(final K key, final V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return this.key;
	}

	public V getValue() {
		return this.value;
	}

	public V setValue(final V arg0) {
		throw new UnsupportedOperationException("This entry is immutable");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.key == null) ? 0 : this.key.hashCode()); // NOCS
		result = (prime * result) + ((this.value == null) ? 0 : this.value.hashCode()); // NOCS
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Map.Entry<?, ?>)) {
			return false;
		}
		final Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
		if (this.key == null) {
			if (other.getKey() != null) {
				return false;
			}
		} else if (!this.key.equals(other.getKey())) {
			return false;
		}
		if (this.value == null) {
			if (other.getValue() != null) {
				return false;
			}
		} else if (!this.value.equals(other.getValue())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return this.key + "=" + this.value;
	}
}
