/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

	private volatile transient E[] array;

	@SuppressWarnings("unchecked")
	public Lookup() {
		this.array = (E[]) new Object[0];
	}

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

	public E getNonBlocking(final int i) {
		final E[] arr = this.array; // work on this "copy" to prevent concurrent modification
		if (i < arr.length) {
			return arr[i];
		}
		return null;
	}

	public E[] getAll() {
		final E[] arr = this.array; // work on this "copy" to prevent concurrent modification
		@SuppressWarnings("unchecked")
		final E[] result = (E[]) new Object[arr.length];
		System.arraycopy(arr, 0, result, 0, arr.length);
		return result;
	}

	public int getSize() {
		return this.array.length;
	}

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

	public boolean set(final E value, final int id) {
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

	@Deprecated
	public void setRecordReceiver(final IMonitoringRecordReceiver recordReceiver) {
		throw new UnsupportedOperationException();
	}
}
