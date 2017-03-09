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
 * Interface for value deserializers for use by monitoring records.
 * @author Holger Knoche
 * @since 1.13
 */
public interface IValueDeserializer {

	/**
	 * Retrieves a {@code byte} value from the given buffer.
	 * 
	 * @param buffer
	 *            The buffer to get the data from
	 * @return The retrieved value
	 * @since 1.13
	 */
	public byte getByte(ByteBuffer buffer);
	
	/**
	 * Retrieves an {@code int} value from the given buffer.
	 * 
	 * @param buffer
	 *            The buffer to get the data from
	 * @return The retrieved value
	 * @since 1.13
	 */
	public int getInt(ByteBuffer buffer);
	
	/**
	 * Retrieves a {@code long} value from the given buffer.
	 * 
	 * @param buffer
	 *            The buffer to get the data from
	 * @return The retrieved value
	 * @since 1.13
	 */
	public long getLong(ByteBuffer buffer);
	
	/**
	 * Retrieves a {@code double} value from the given buffer.
	 * 
	 * @param buffer
	 *            The buffer to get the data from
	 * @return The retrieved value
	 * @since 1.13
	 */
	public double getDouble(ByteBuffer buffer);
	
	/**
	 * Retrieves a {@code String} value from the given buffer.
	 * 
	 * @param buffer
	 *            The buffer to get the data from
	 * @return The retrieved value
	 * @since 1.13
	 */
	public String getString(ByteBuffer buffer, IRegistry<String> stringRegistry);	
	
	/**
	 * Retrieves raw data from the given buffer.
	 * 
	 * @param buffer
	 *            The buffer to get the data from
	 * @param target
	 *            The array to store the data in
	 * @return The retrieved data
	 * @since 1.13
	 */
	public byte[] getBytes(ByteBuffer buffer, byte[] target);
	
}
