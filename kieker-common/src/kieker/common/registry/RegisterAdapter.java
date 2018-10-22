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

package kieker.common.registry;

import kieker.common.registry.writer.IWriterRegistry;

/**
 * @param <E>
 *            the type of the values in the passed registry
 *
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class RegisterAdapter<E> implements IRegistry<E> {

	/** the registry which is used register new values. */
	private final IWriterRegistry<E> writerRegistry;

	public RegisterAdapter(final IWriterRegistry<E> writerRegistry) {
		this.writerRegistry = writerRegistry;
	}

	@Override
	public int get(final E value) {
		this.writerRegistry.register(value);
		return 0;
	}

	@Override
	public long getId() {
		return this.writerRegistry.getId();
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
