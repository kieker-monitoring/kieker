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

package kieker.common.util.registry;

import java.util.Arrays;

/**
 *
 * @param <E>
 *            the type of the objects
 *
 * @author Jan Waller
 *
 * @since 1.8
 */
public final class Lookup<E> implements ILookup<E> {

	private final long id;

	private transient volatile E[] array;

	/**
	 * Create a new lookup entry.
	 */
	public Lookup() {
		this(RegistryUtil.generateId());
	}

	/**
	 * Creates a new lookup with the given ID.
	 *
	 * @param id
	 *            The ID to use for this lookup
	 */
	@SuppressWarnings("unchecked")
	public Lookup(final long id) {
		this.id = id;
		this.array = (E[]) new Object[0];
	}

	@Override
	public long getId() {
		return this.id;
	}

	@Override
	public int get(final E value) {
		final E[] arr = this.array; // work on this "copy" to prevent concurrent modification
		if (value != null) {
			for (int i = 0; i < arr.length; i++) {
				if (value.equals(arr[i])) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Get a record from the underlying object array.
	 *
	 * @param i
	 *            the index for the array
	 *
	 * @return the object in the array or null if the array bounds are exceeded
	 */
	public E getNonBlocking(final int i) {
		final E[] arr = this.array; // work on this "copy" to prevent concurrent modification
		if (i < arr.length) {
			return arr[i];
		}
		return null;
	}

	@Override
	public E[] getAll() {
		final E[] arr = this.array; // work on this "copy" to prevent concurrent modification
		@SuppressWarnings("unchecked")
		final E[] result = (E[]) new Object[arr.length];
		System.arraycopy(arr, 0, result, 0, arr.length);
		return result;
	}

	@Override
	public int getSize() {
		return this.array.length;
	}

	@Override
	public E get(final int i) {
		synchronized (this) {
			E value = this.getNonBlocking(i);
			while (null == value) {
				try {
					this.wait();
				} catch (final InterruptedException e) {
					return null;
				}
				value = this.get(i);
			}
			return value;
		}
	}

	@Override
	public boolean set(final E value, final int id) { // NOCS Ignore hiding the ID field
		synchronized (this) {
			if (id < this.array.length) {
				if (null == this.array[id]) {
					this.array[id] = value;
					this.notifyAll();
					return true;
				} else {
					return false;
				}
			} // increase capacity
			@SuppressWarnings("unchecked")
			final E[] newArray = (E[]) new Object[id + 1];
			System.arraycopy(this.array, 0, newArray, 0, this.array.length);
			newArray[id] = value;
			this.array = newArray;
			this.notifyAll();
			return true;
		}
	}

	/**
	 * @deprecated (won't be removed) This method is not supported by this implementation.
	 *
	 * @param recordReceiver
	 *            has no special property.
	 */
	@Override
	@Deprecated
	public void setRecordReceiver(final IRegistryRecordReceiver recordReceiver) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return Arrays.toString(this.getAll());
	}
}
