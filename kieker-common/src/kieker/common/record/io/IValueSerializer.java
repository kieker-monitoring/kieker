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

package kieker.common.record.io;

/**
 * Interface for value serializers for use by monitoring records.
 *
 * @param <T>
 *            for the expected enumeration type
 *
 * @author Holger Knoche
 * @author Reiner Jung - added enumeration support
 *
 * @since 1.13
 */
public interface IValueSerializer {

	/**
	 * Stores a {@code boolean} value in the underlying data store.
	 *
	 * @param value
	 *            The value to store
	 * @since 1.13
	 */
	public void putBoolean(boolean value);

	/**
	 * Stores a {@code byte} value in the underlying data store.
	 *
	 * @param value
	 *            The value to store
	 * @since 1.13
	 */
	public void putByte(byte value);

	/**
	 * Stores a {@code char} value in the underlying data store.
	 *
	 * @param value
	 *            The value to store
	 * @since 1.13
	 */
	public void putChar(char value);

	/**
	 * Stores a {@code short} value in the underlying data store.
	 *
	 * @param value
	 *            The value to store
	 * @since 1.13
	 */
	public void putShort(short value); // NOPMD

	/**
	 * Stores an {@code int} value in the underlying data store.
	 *
	 * @param value
	 *            The value to store
	 * @since 1.13
	 */
	public void putInt(int value);

	/**
	 * Stores a {@code long} value in the underlying data store.
	 *
	 * @param value
	 *            The value to store
	 * @since 1.13
	 */
	public void putLong(long value);

	/**
	 * Stores a {@code float} value in the underlying data store.
	 *
	 * @param value
	 *            The value to store
	 * @since 1.13
	 */
	public void putFloat(float value);

	/**
	 * Stores a {@code double} value in the underlying data store.
	 *
	 * @param value
	 *            The value to store
	 * @since 1.13
	 */
	public void putDouble(double value);

	/**
	 * Stores a {@code Enumeration} value in the underlying data store.
	 *
	 * @param value
	 *            The value to store
	 * @since 1.14
	 */
	public <T extends Enum<T>> void putEnumeration(T value);

	/**
	 * Stores raw data in the underlying data store.
	 *
	 * @param value
	 *            The data to store
	 * @since 1.13
	 */
	public void putBytes(byte[] value);

	/**
	 * Stores a {@code String} value in the underlying data store.
	 *
	 * @param value
	 *            The value to store
	 * @since 1.13
	 */
	public void putString(String value);

}
