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

import java.nio.ByteBuffer;

import kieker.common.util.registry.IRegistry;

/**
 * Interface for value serializers for use by monitoring records.
 * 
 * @author Holger Knoche
 * @since 1.13
 */
public interface IValueSerializer {

	/**
	 * Stores a {@code byte} value in the given buffer.
	 * 
	 * @param value
	 *            The value to store
	 * @param buffer
	 *            The buffer to store the data in
	 * @since 1.13
	 */
	public void putByte(byte value, ByteBuffer buffer);

	/**
	 * Stores an {@code int} value in the given buffer.
	 * 
	 * @param value
	 *            The value to store
	 * @param buffer
	 *            The buffer to store the data in
	 * @since 1.13
	 */
	public void putInt(int value, ByteBuffer buffer);

	/**
	 * Stores a {@code long} value in the given buffer.
	 * 
	 * @param value
	 *            The value to store
	 * @param buffer
	 *            The buffer to store the data in
	 * @since 1.13
	 */
	public void putLong(long value, ByteBuffer buffer);

	/**
	 * Stores a {@code double} value in the given buffer.
	 * 
	 * @param value
	 *            The value to store
	 * @param buffer
	 *            The buffer to store the data in
	 * @since 1.13
	 */
	public void putDouble(double value, ByteBuffer buffer);

	/**
	 * Stores raw data in the given buffer.
	 * 
	 * @param value
	 *            The data to store
	 * @param buffer
	 *            The buffer to store the data in
	 * @since 1.13
	 */
	public void putBytes(byte[] value, ByteBuffer buffer);

	/**
	 * Stores a {@code String} value in the given buffer.
	 * 
	 * @param value
	 *            The value to store
	 * @param buffer
	 *            The buffer to store the data in
	 * @since 1.13
	 */
	public void putString(String value, ByteBuffer buffer, IRegistry<String> stringRegistry);

}
