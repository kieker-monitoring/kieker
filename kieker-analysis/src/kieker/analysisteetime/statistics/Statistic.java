package kieker.analysisteetime.statistics;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Statistic {

	private static final int DEFAULT_INITIAL_CAPACITY = 5;
	
	private Map<Property, Long> properties = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
	
	protected Statistic() {}
	
	public void setProperty(final Property property, final Long value) {
		Objects.requireNonNull(property, "Property must not be null");
		this.properties.put(property, value);	
	}
	
	public Long getProperty(final Property property) {
		// TODO implement null object pattern
		//return this.statistics.getOrDefault(unit, ...);
		return this.properties.get(property);
	}
	
	public boolean hasProperty(final Property property) {
		return this.properties.containsKey(property);
	}
	
	public Set<Property> getProperties() {
		return Collections.unmodifiableSet(this.properties.keySet());
	}
	
}
