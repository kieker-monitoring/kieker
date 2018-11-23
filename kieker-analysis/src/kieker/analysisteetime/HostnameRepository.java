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

package kieker.analysisteetime;

import java.util.HashMap;
import java.util.Map;

import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

/**
 * A class that manages host names for trace IDs. The intent to register host names via the {@code addEntry()} method
 * and retrieve them via the {@code get()} method.
 *
 * Furthermore, this repository contains an internal counter, which can be used to automatically remove an entry, when
 * its counter reaches 0. This can be used to process a stack of {@link BeforeOperationEvent}s and
 * {@link AfterOperationEvent}s by calling the {@code inc()} method when a {@link BeforeOperationEvent} is processed
 * and calling the {@code dec()} method when an {@link AfterOperationEvent} is processed. Thus the entry in this
 * repository will be removed after the last {@link AfterOperationEvent}.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class HostnameRepository {

	private final Map<Long, Entry> repository = new HashMap<>(); // NOPMD (Class not designed for concurrent use)

	public HostnameRepository() {
		// Create an empty repository
	}

	public void addEntry(final long traceId, final String hostname) {
		this.repository.put(traceId, new Entry(hostname));
	}

	public void inc(final long traceId) {
		this.repository.get(traceId).size++;
	}

	public void dec(final long traceId) {
		final Entry entry = this.repository.get(traceId);
		entry.size--;
		if (entry.size <= 0) {
			this.repository.remove(traceId);
		}
	}

	public String getHostname(final long traceId) {
		final Entry entry = this.repository.get(traceId);
		if (entry == null) {
			return null;
		} else {
			return entry.hostname;
		}
	}

	/**
	 * Repository entries.
	 */
	private static class Entry {
		protected int size; // size = 0
		protected String hostname;

		protected Entry(final String hostname) {
			this.hostname = hostname;
		}
	}

}
