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

package kieker.analysisteetime.util.graph.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import kieker.analysisteetime.util.graph.IElement;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
abstract class ElementImpl implements IElement { // NOPMD NOCS (Element is in this context the abstraction of Graph, Vertex, and Edge)

	protected Map<String, Object> properties = new HashMap<>(); // NOPMD (no concurrent access intended)

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getProperty(final String key) {
		return (T) this.properties.get(key);
	}

	@Override
	public Set<String> getPropertyKeys() {
		return Collections.unmodifiableSet(this.properties.keySet());
	}

	@Override
	public void setProperty(final String key, final Object value) {
		this.properties.put(key, value);
	}

	@Override
	public void setPropertyIfAbsent(final String key, final Object value) {
		this.properties.putIfAbsent(key, value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T removeProperty(final String key) {
		return (T) this.properties.remove(key);
	}

}
