/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.generic.graph.impl;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import kieker.analysis.generic.graph.IElement;

/**
 *
 * @author Reiner Jung
 * @since 2.0.0
 *
 */
public class ElementImpl implements IElement {

	private final String id;
	private final ConcurrentHashMap<String, Object> properties = new ConcurrentHashMap<>();

	public ElementImpl(final String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getProperty(final String key) {
		return (T) this.properties.get(key);
	}

	@Override
	public Set<String> getPropertyKeys() {
		return this.properties.keySet();
	}

	@Override
	public void setProperty(final String key, final Object value) {
		this.properties.put(key, value);
	}

	@Override
	public void setPropertyIfAbsent(final String key, final Object value) {
		if (!this.properties.containsKey(key)) {
			this.setProperty(key, value);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T removeProperty(final String key) {
		return (T) this.properties.remove(key);
	}

}
