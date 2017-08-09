/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.examples.livedemo.analysis.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A simple extension of a {@link LinkedHashMap} with fixed maximal size. Older entries are removed in FiFo order when new entries exceed the maximal size.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 * 
 * @param <K>
 *            The type of the keys.
 * @param <V>
 *            The type of the values.
 */
public class LimitedHashMap<K, V> extends LinkedHashMap<K, V> {

	private static final long serialVersionUID = -8171905963249349942L;

	private final int maximalNumberOfEntries;

	public LimitedHashMap(final int maximalNumberOfEntries) {
		this.maximalNumberOfEntries = maximalNumberOfEntries;
	}

	@Override
	protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
		return (this.size() > this.maximalNumberOfEntries);
	}

}
