/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.statistics;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Sören Henning
 *
 */
public class Statistics {

	private static final int DEFAULT_INITIAL_CAPACITY = 4;

	private final Map<Unit, Statistic> statistics = new HashMap<>(DEFAULT_INITIAL_CAPACITY); // NOPMD (no concurrent access intended)

	public Statistics() {}

	public Statistic getStatistic(final Unit unit) {
		Objects.requireNonNull(unit, "Unit must not be null");
		return this.statistics.computeIfAbsent(unit, x -> new Statistic());
	}

	public boolean hasStatistic(final Unit unit) {
		Objects.requireNonNull(unit, "Unit must not be null");
		return this.statistics.containsKey(unit);
	}

	public Set<Unit> getUnits() {
		return Collections.unmodifiableSet(this.statistics.keySet());
	}

}
