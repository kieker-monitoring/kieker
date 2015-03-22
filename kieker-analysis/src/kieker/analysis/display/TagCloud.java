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

package kieker.analysis.display;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class is currently under development, mostly for test purposes, and not designed for productive deployment.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.8
 */
public class TagCloud {

	private final ConcurrentMap<String, AtomicLong> counters = new ConcurrentHashMap<String, AtomicLong>();

	public TagCloud() {
		// No code necessary
	}

	public void incrementCounter(final String tag) {
		final AtomicLong newCounter = new AtomicLong();
		final AtomicLong oldCounter = this.counters.putIfAbsent(tag, newCounter);

		if (oldCounter == null) {
			newCounter.incrementAndGet();
		} else {
			oldCounter.incrementAndGet();
		}
	}

	public Map<String, AtomicLong> getCounters() {
		return this.counters;
	}

}
