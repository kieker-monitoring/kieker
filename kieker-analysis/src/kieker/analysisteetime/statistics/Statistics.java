package kieker.analysisteetime.statistics;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Statistics {

	private static final int DEFAULT_INITIAL_CAPACITY = 4; 
	
	private Map<Unit, Statistic> statistics = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
	
	public Statistics() {}
	
	public void addStatistic(final Unit unit) {
		this.addStatistic(unit, new Statistic());
	}
	
	public Statistic getStatistic(final Unit unit) {
		// TODO implement null object pattern
		//return this.statistics.getOrDefault(unit, ...);
		return this.statistics.get(unit);
	}
	
	public boolean hasStatistic(final Unit unit) {
		return this.statistics.containsKey(unit);
	}
	
	public Set<Unit> getUnits() {
		return Collections.unmodifiableSet(this.statistics.keySet());
	}
	
	private void addStatistic(final Unit unit, final Statistic statistic) {
		Objects.requireNonNull(unit, "Unit must not be null");
		if (this.statistics.putIfAbsent(unit, statistic) != null) {
			throw new IllegalArgumentException("Statistic for unit is already present");
		}
	}
}
