package kieker.analysisteetime.util.graph.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kieker.analysisteetime.util.graph.Element;

abstract class ElementImpl implements Element {

	protected Map<String, Object> properties = new HashMap<String, Object>();

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getProperty(final String key) {
		return (T) this.properties.get(key);
	}

	@Override
	public Set<String> getPropertyKeys() {
		// TODO no copying, use unmodifiable wrapper instead
		return new HashSet<String>(this.properties.keySet());
	}

	@Override
	public void setProperty(final String key, final Object value) {
		this.properties.put(key, value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T removeProperty(final String key) {
		return (T) this.properties.remove(key);
	}

}
