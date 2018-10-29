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

import kieker.common.exception.RecordInstantiationException;

/**
 * Interface for value deserializers for use by monitoring records.
 *
 * @author Holger Knoche
 * @author Reiner Jung - added enumeration support
 *
 * @since 1.13
 */
public interface IValueDeserializer {

	/**
	 * Retrieves a {@code boolean} value from the underlying data source.
	 *
	 * @return The retrieved value
	 * @since 1.13
	 */
	public boolean getBoolean(); // NOPMD getBoolean is more consistent with other methods

	/**
	 * Retrieves a {@code byte} value from the underlying data source.
	 *
	 * @return The retrieved value
	 * @since 1.13
	 *
	 * @throws NumberFormatException
	 *             on format errors
	 */
	public byte getByte() throws NumberFormatException;

	/**
	 * Retrieves a {@code char} value from the underlying data source.
	 *
	 * @return The retrieved value
	 * @since 1.13
	 */
	public char getChar();

	/**
	 * Retrieves a {@code short} value from the underlying data source.
	 *
	 * @return The retrieved value
	 * @since 1.13
	 *
	 * @throws NumberFormatException
	 *             on format errors
	 */
	public short getShort() throws NumberFormatException; // NOPMD

	/**
	 * Retrieves an {@code int} value from the underlying data source.
	 *
	 * @return The retrieved value
	 * @since 1.13
	 *
	 * @throws NumberFormatException
	 *             on format errors
	 */
	public int getInt() throws NumberFormatException;

	/**
	 * Retrieves a {@code long} value from the underlying data source.
	 *
	 * @return The retrieved value
	 * @since 1.13
	 *
	 * @throws NumberFormatException
	 *             on format errors
	 */
	public long getLong() throws NumberFormatException;

	/**
	 * Retrieves a {@code float} value from the underlying data source.
	 *
	 * @return The retrieved value
	 * @since 1.13
	 *
	 * @throws NumberFormatException
	 *             on format errors
	 */
	public float getFloat() throws NumberFormatException;

	/**
	 * Retrieves a {@code double} value from the underlying data source.
	 *
	 * @return The retrieved value
	 * @since 1.13
	 *
	 * @throws NumberFormatException
	 *             on format errors
	 */
	public double getDouble() throws NumberFormatException;

	/**
	 * Retrieves a {@code String} value from the underlying data source.
	 *
	 * @return The retrieved value
	 * @since 1.13
	 */
	public String getString();

	/**
	 * Retrieves a {@code Enumeration} value from the underlying data source.
	 *
	 * @param <T>
	 *            the corresponding enumeration type
	 *
	 * @param clazz
	 *            enumeration type to be used
	 * @return The retrieved value
	 *
	 * @throws RecordInstantiationException
	 *             in case the received ordinal does not exist in the specified enumeration type
	 * @since 1.14
	 *
	 * @throws RecordInstantiationException
	 *             input errors
	 */
	public <T extends Enum<T>> T getEnumeration(Class<T> clazz) throws RecordInstantiationException;

	/**
	 * Retrieves raw data from the underlying data source.
	 *
	 * @param target
	 *            The array to store the data in
	 * @return The retrieved data
	 * @since 1.13
	 */
	public byte[] getBytes(byte[] target);

}
