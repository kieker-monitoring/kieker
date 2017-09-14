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

package kieker.monitoring.writer.serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.IRegistryRecordReceiver;

/**
 * Rudimentary string registry for use by the binary format serializer. This registry is meant for
 * per-chunk usage and is <b>not</b> thread-safe. It hands out only sequential IDs, and
 * allows to retrieve the data required for serialization easily. All methods not required for
 * serialization are not supported by this implementation.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
class SerializerStringRegistry implements IRegistry<String> {

	private final Map<String, Integer> valueToIdMap = new HashMap<String, Integer>(); // NOPMD
	private final List<String> values = new ArrayList<String>();

	SerializerStringRegistry() {
		// Nothing to do
	}
	
	@Override
	public long getId() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int get(final String value) {
		final Integer id = this.valueToIdMap.get(value);

		if (id == null) {
			final int newId = this.values.size();

			this.values.add(value);
			this.valueToIdMap.put(value, newId);

			return newId;
		} else {
			return id;
		}
	}

	/**
	 * Returns a list of the stored IDs, where the position of each element corresponds to their
	 * ID.
	 */
	public List<String> getValues() {
		return this.values;
	}

	@Override
	public String get(final int id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String[] getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getSize() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRecordReceiver(final IRegistryRecordReceiver registryRecordReceiver) {
		throw new UnsupportedOperationException();
	}

}
