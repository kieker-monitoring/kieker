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

package kieker.analysis.plugin.reader.newio.deserializer;

import java.util.List;

import com.google.common.collect.ImmutableList;

import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.IRegistryRecordReceiver;

/**
 * Rudimentary string registry for use by the binary format decoder.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
public class DeserializerStringRegistry implements IRegistry<String> {

	private final List<String> values;

	/**
	 * Creates a new deserializer string registry.
	 * @param values The values to use
	 */
	public DeserializerStringRegistry(final List<String> values) {
		this.values = ImmutableList.copyOf(values);
	}

	@Override
	public long getId() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int get(final String value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String get(final int i) {
		return this.values.get(i);
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
