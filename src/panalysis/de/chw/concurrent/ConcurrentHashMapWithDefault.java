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
package de.chw.concurrent;

import java.util.concurrent.ConcurrentHashMap;

import kieker.analysis.plugin.filter.flow.reconstruction.ValueFactory;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class ConcurrentHashMapWithDefault<K, V> extends ConcurrentHashMap<K, V> {

	private static final long serialVersionUID = -7958038532219740472L;

	private final ValueFactory<V> valueFactory;

	/**
	 * @since 1.10
	 */
	public ConcurrentHashMapWithDefault(final ValueFactory<V> valueFactory) {
		this.valueFactory = valueFactory;
	}

	/**
	 * @return the corresponding value if the key exists. Otherwise, it creates, inserts, and returns a new default value.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public V get(final Object key) {
		V value = super.get(key);
		if (value == null) {
			synchronized (this) {
				value = super.get(key);
				if (value == null) { // NOCS (DCL)
					value = this.valueFactory.create();
					super.put((K) key, value);
				}
			}
		}
		return value;
	}
}
