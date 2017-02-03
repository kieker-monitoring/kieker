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

package kieker.monitoring.registry;

import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.IRegistryRecordReceiver;

/**
 * @param <E>
 *            the type of the values in the passed registry
 *
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class GetIdAdapter<E> implements IRegistry<E> {

	private final IWriterRegistry<E> writerRegistry;

	public GetIdAdapter(final IWriterRegistry<E> writerRegistry) {
		this.writerRegistry = writerRegistry;
	}

	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int get(final E value) {
		return this.writerRegistry.getId(value);
	}

	@Override
	public E get(final int i) {
		throw new UnsupportedOperationException();
	}

	@Override
	public E[] getAll() {
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
