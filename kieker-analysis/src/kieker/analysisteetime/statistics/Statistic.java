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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Sören Henning
 *
 * @since 1.14
 *
 */
public class Statistic {

	private static final int DEFAULT_INITIAL_CAPACITY = 5;

	private final Map<IProperty, Long> properties = new HashMap<>(DEFAULT_INITIAL_CAPACITY); // NOPMD (no concurrent access intended)

	protected Statistic() {
		// Create statistic
	}

	public void setProperty(final IProperty property, final Long value) {
		Objects.requireNonNull(property, "Property must not be null");
		this.properties.put(property, value);
	}

	public Long getProperty(final IProperty property) {
		return this.properties.get(property);
	}

	public boolean hasProperty(final IProperty property) {
		return this.properties.containsKey(property);
	}

	public Set<IProperty> getProperties() {
		return Collections.unmodifiableSet(this.properties.keySet());
	}

}
