package kieker.analysisteetime.statistics;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Sören Henning
 *
 * @since 1.13
 *
 */
public class StatisticsModel {

	private final Map<Object, Statistics> model = new HashMap<>(); // NOPMD (no concurrent access intended)

	public StatisticsModel() {
		// Create statistics model
	}

	public Statistics get(final Object key) {
		Objects.requireNonNull(key, "Key must not be null");
		return this.model.computeIfAbsent(key, x -> new Statistics());
	}

	public boolean has(final Object key) {
		Objects.requireNonNull(key, "Unit must not be null");
		return this.model.containsKey(key);
	}

}
