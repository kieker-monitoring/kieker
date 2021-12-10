/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.io.IValueDeserializer; 

public class ArrayDeserializer implements IValueDeserializer {
	
	private Object[] objects;
	private int position;

	public ArrayDeserializer() {
		
	}
	
	public void receiveData(Object[] objects) {
		this.objects = objects;
		this.position = 0;
	}
	
	/**
	 * Retrieves a {@code boolean} value from the underlying data source.
	 *
	 * @return The retrieved value
	 * @since 1.13
	 */
	public boolean getBoolean() { // NOPMD getBoolean is more consistent with other methods
		return (boolean) this.objects[this.position++];
	}
	
	/**
	 * Retrieves a {@code byte} value from the underlying data source.
	 *
	 * @return The retrieved value
	 * @since 1.13
	 */
	public byte getByte() throws NumberFormatException {
		return (byte) this.objects[this.position++];
	}

	/**
	 * Retrieves a {@code char} value from the underlying data source.
	 *
	 * @return The retrieved value
	 * @since 1.13
	 */
	public char getChar() {
		return (char) this.objects[this.position++];
	}

	/**
	 * Retrieves a {@code short} value from the underlying data source.
	 *
	 * @return The retrieved value
	 * @since 1.13
	 */
	public short getShort() throws NumberFormatException{ // NOPMD
		return (short) this.objects[this.position++];
	}

	/**
	 * Retrieves an {@code int} value from the underlying data source.
	 *
	 * @return The retrieved value
	 * @since 1.13
	 */
	public int getInt() throws NumberFormatException {
		return (int) this.objects[this.position++];
	}

	/**
	 * Retrieves a {@code long} value from the underlying data source.
	 *
	 * @return The retrieved value
	 * @since 1.13
	 */
	public long getLong() throws NumberFormatException {
		return (long) this.objects[this.position++];
	}

	/**
	 * Retrieves a {@code float} value from the underlying data source.
	 *
	 * @return The retrieved value
	 * @since 1.13
	 */
	public float getFloat() throws NumberFormatException {
		return (float) this.objects[this.position++];
	}

	/**
	 * Retrieves a {@code double} value from the underlying data source.
	 *
	 * @return The retrieved value
	 * @since 1.13
	 */
	public double getDouble() throws NumberFormatException {
		return (double) this.objects[this.position++];
	}

	/**
	 * Retrieves a {@code String} value from the underlying data source.
	 *
	 * @return The retrieved value
	 * @since 1.13
	 */
	public String getString() {
		return (String) this.objects[this.position++];
	}

	/**
	 * Retrieves a {@code Enumeration} value from the underlying data source.
	 *
	 * @param clazz
	 *            enumeration type to be used
	 * @return The retrieved value
	 *
	 * @throws RecordInstantiationException
	 *             in case the received ordinal does not exist in the specified enumeration type
	 * @since 1.14
	 */
	public <T extends Enum<T>> T getEnumeration(Class<T> clazz) throws RecordInstantiationException {
		return (T) this.objects[this.position++];
	}

	/**
	 * Retrieves raw data from the underlying data source.
	 *
	 * @param target
	 *            The array to store the data in
	 * @return The retrieved data
	 * @since 1.13
	 */
	public byte[] getBytes(byte[] target) {
		return null;
	}
}
