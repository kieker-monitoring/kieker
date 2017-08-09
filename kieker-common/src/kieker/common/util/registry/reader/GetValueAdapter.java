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
package kieker.common.util.registry.reader;

import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.IRegistryRecordReceiver;

/**
 * Represents an adaptor from the new ReaderRegistry to the legacy IRegistry interface.
 *
 * @param <E>
 *            the type of the values in the passed registry
 *
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class GetValueAdapter<E> implements IRegistry<E> {

	private final ReaderRegistry<E> readerRegistry;

	public GetValueAdapter(final ReaderRegistry<E> readerRegistry) {
		this.readerRegistry = readerRegistry;
	}

	@Override
	public long getId() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int get(final E value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public E get(final int key) {
		return this.readerRegistry.get(key);
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
