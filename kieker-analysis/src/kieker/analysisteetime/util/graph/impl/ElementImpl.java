package kieker.analysisteetime.util.graph.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import kieker.analysisteetime.util.graph.Element;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
abstract class ElementImpl implements Element { // NOPMD NOCS (Element is in this context the abstraction of Graph, Vertex, and Edge)

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
