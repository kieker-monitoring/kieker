/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
package kieker.examples.userguide.ch3and4bookstore;

import java.util.ArrayList;
import java.util.List;
import kieker.common.record.io.IValueSerializer;

/**
 * @author Reiner Jung
 *
 */
public class ArraySerializer implements IValueSerializer {
	
	List<Object> objects = new ArrayList<Object>();
	
	public ArraySerializer() {
		
	}
	
	public void clear() {
		objects.clear();
	}
	
	public Object[] getObjectArray() {
		return objects.toArray();
	}

	/**
	 * Stores a {@code boolean} value in the underlying data store.
	 *
	 * @param value
	 *            The value to store
	 * @since 1.13
	 */
	public void putBoolean(boolean value) {
		this.objects.add(value);
	}

	/**
	 * Stores a {@code byte} value in the underlying data store.
	 *
	 * @param value
	 *            The value to store
	 * @since 1.13
	 */
	public void putByte(byte value) {
		this.objects.add(value);
	}

	/**
	 * Stores a {@code char} value in the underlying data store.
	 *
	 * @param value
	 *            The value to store
	 * @since 1.13
	 */
	public void putChar(char value) {
		this.objects.add(value);
	}

	/**
	 * Stores a {@code short} value in the underlying data store.
	 *
	 * @param value
	 *            The value to store
	 * @since 1.13
	 */
	public void putShort(short value) { // NOPMD
		this.objects.add(value);
	}

	/**
	 * Stores an {@code int} value in the underlying data store.
	 *
	 * @param value
	 *            The value to store
	 * @since 1.13
	 */
	public void putInt(int value) {
		this.objects.add(value);
	}

	/**
	 * Stores a {@code long} value in the underlying data store.
	 *
	 * @param value
	 *            The value to store
	 * @since 1.13
	 */
	public void putLong(long value) {
		this.objects.add(value);
	}

	/**
	 * Stores a {@code float} value in the underlying data store.
	 *
	 * @param value
	 *            The value to store
	 * @since 1.13
	 */
	public void putFloat(float value) {
		this.objects.add(value);
	}

	/**
	 * Stores a {@code double} value in the underlying data store.
	 *
	 * @param value
	 *            The value to store
	 * @since 1.13
	 */
	public void putDouble(double value) {
		this.objects.add(value);
	}

	/**
	 * Stores a {@code Enumeration} value in the underlying data store.
	 *
	 * @param value
	 *            The value to store
	 * @since 1.14
	 */
	public <T extends Enum<T>> void putEnumeration(T value) {
		this.objects.add(value);
	}

	/**
	 * Stores raw data in the underlying data store.
	 *
	 * @param value
	 *            The data to store
	 * @since 1.13
	 */
	public void putBytes(byte[] value) {
		this.objects.add(value);
	}

	/**
	 * Stores a {@code String} value in the underlying data store.
	 *
	 * @param value
	 *            The value to store
	 * @since 1.13
	 */
	public void putString(String value) {
		this.objects.add(value);
	}
}
