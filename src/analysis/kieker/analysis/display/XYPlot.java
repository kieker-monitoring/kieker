/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

/**
 * This class is currently under development, mostly for test purposes, and not designed for productive deployment.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.8
 */
public class XYPlot extends AbstractDisplay {

	private final CacheMap entries = new CacheMap();

	public XYPlot() {
		// No code necessary
	}

	public Map<Object, Number> getEntries() {
		return Collections.unmodifiableMap(this.entries);
	}

	public void setEntry(final Object x, final Number y) {
		synchronized (this) {
			this.entries.put(x, y);
		}
	}

	private static class CacheMap extends LinkedHashMap<Object, Number> {

		private static final long serialVersionUID = 1L;
		private static final int MAX_ENTRIES = 50;

		public CacheMap() {
			// No code necessary
		}

		@Override
		protected boolean removeEldestEntry(final Map.Entry<Object, Number> arg0) {
			return this.size() > MAX_ENTRIES;
		}

	}

}
