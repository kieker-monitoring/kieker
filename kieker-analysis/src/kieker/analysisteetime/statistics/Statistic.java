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
 * @author S�ren Henning
 *
 */
public class Statistic {

	private static final int DEFAULT_INITIAL_CAPACITY = 5;

	private final Map<Property, Long> properties = new HashMap<>(DEFAULT_INITIAL_CAPACITY);

	protected Statistic() {}

	public void setProperty(final Property property, final Long value) {
		Objects.requireNonNull(property, "Property must not be null");
		this.properties.put(property, value);
	}

	public Long getProperty(final Property property) {
		return this.properties.get(property);
	}

	public boolean hasProperty(final Property property) {
		return this.properties.containsKey(property);
	}

	public Set<Property> getProperties() {
		return Collections.unmodifiableSet(this.properties.keySet());
	}

}
