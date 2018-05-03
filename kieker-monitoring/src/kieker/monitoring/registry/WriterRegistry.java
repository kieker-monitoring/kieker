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

package kieker.monitoring.registry;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Christian Wulf (chw)
 *
 * @since 1.13
 */
public class WriterRegistry implements IWriterRegistry<String> {

	// TODO introduce faster, non-boxing ObjectIntMap
	private final Map<String, Integer> storage = new HashMap<String, Integer>(); // NOPMD (synchronization is not necessary)
	/** id of the next value which will be registered */
	private int nextId;
	/** the listener of this registry which is notified upon a newly registered value */
	private final IRegistryListener<String> registryListener;
	/** ID of this registry to distinguish multiple ones */
	private final long id;

	public WriterRegistry(final IRegistryListener<String> registryListener) {
		this.registryListener = registryListener;
		this.id = WriterRegistryUtil.generateId();
	}

	@Override
	public int getId(final String value) {
		final Integer valueId = this.storage.get(value);
		if (valueId == null) {
			throw new IllegalArgumentException(
					"The given value '" + value + "' is not registered. Thus, there is no identifier for this value.");
		}
		return valueId;
	}

	@Override
	public void register(final String value) {
		if (!this.storage.containsKey(value)) {
			final int valueId = this.nextId++;
			this.storage.put(value, valueId);
			this.registryListener.onNewRegistryEntry(value, valueId);
		}
	}

	@Override
	public long getId() {
		return this.id;
	}

}
