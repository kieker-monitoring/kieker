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

package kieker.analysisteetime.statistics;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Sören Henning
 *
 * @since 1.14
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
