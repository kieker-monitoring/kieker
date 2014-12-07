/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.util.registry.newversion;

import java.util.HashMap;
import java.util.Map;

/**
 * @param <T>
 *            the type of the elements
 *
 * @author Christian Wulf
 *
 * @since 1.11
 */
public final class Lookup<T> implements ILookup<T> {

	// TODO use a HPC implementation with primitive int keys
	// I recommend: http://labs.carrotsearch.com/hppc.html
	private final Map<Integer, T> registeredEntries;

	public Lookup() {
		this.registeredEntries = new HashMap<Integer, T>();
	}

	@Override
	public void add(final int uniqueId, final T element) {
		this.registeredEntries.put(uniqueId, element);
	}

	@Override
	public final T get(final int uniqueId) {
		return this.registeredEntries.get(uniqueId);
	}

}
