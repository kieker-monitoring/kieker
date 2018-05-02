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

package kieker.analysis.display;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This class is currently under development, mostly for test purposes, and not designed for productive deployment.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.8
 */
public class XYPlot extends AbstractDisplay {

	private final ConcurrentMap<String, CacheMap> entries;
	private final int maxEntriesPerSeries;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param maxEntriesPerSeries
	 *            The maximal number of allowed entries per series in this plot.
	 */
	public XYPlot(final int maxEntriesPerSeries) {
		this.entries = new ConcurrentHashMap<String, CacheMap>();
		this.maxEntriesPerSeries = maxEntriesPerSeries;
	}

	/**
	 * Delivers the entries for the given series.
	 * 
	 * @param key
	 *            The name of the series.
	 * 
	 * @return A map with the series entries.
	 */
	public Map<Object, Number> getEntries(final String key) {
		synchronized (this.entries.get(key)) {
			return Collections.unmodifiableMap(this.entries.get(key));
		}
	}

	public Set<String> getKeys() {
		return this.entries.keySet();
	}

	/**
	 * Sets a value for the given series.
	 * 
	 * @param key
	 *            The name of the series to modify.
	 * @param x
	 *            The x value.
	 * @param y
	 *            The y value.
	 */
	public void setEntry(final String key, final Object x, final Number y) {
		final CacheMap newCacheMap = new CacheMap(this.maxEntriesPerSeries);
		final CacheMap oldCacheMap = this.entries.putIfAbsent(key, newCacheMap);

		final CacheMap syncObj;
		if (oldCacheMap != null) {
			syncObj = oldCacheMap;
		} else {
			syncObj = newCacheMap;
		}

		synchronized (syncObj) {
			syncObj.put(x, y);
		}
	}

	/**
	 * A helper class representing a simple LRU cache with fixed size.
	 * 
	 * @author Nils Christian Ehmke
	 * @since 1.8
	 */
	private static class CacheMap extends LinkedHashMap<Object, Number> {

		private static final long serialVersionUID = 1L;
		private final int maxEntriesPerSeries;

		public CacheMap(final int maxEntriesPerSeries) {
			this.maxEntriesPerSeries = maxEntriesPerSeries;
		}

		@Override
		protected boolean removeEldestEntry(final Map.Entry<Object, Number> entry) {
			return this.size() > this.maxEntriesPerSeries;
		}

	}

}
