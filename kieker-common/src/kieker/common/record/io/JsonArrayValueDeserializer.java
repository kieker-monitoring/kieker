/***************************************************************************
 * Copyright 2019 Kieker Project (http://kieker-monitoring.net)
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

import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import kieker.common.exception.RecordInstantiationException;

/**
 * Deserialize an JSON array of values.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class JsonArrayValueDeserializer extends AbstractValueDeserializer implements IValueDeserializer {

	final Iterator<JsonNode> values;

	/**
	 * Factory method to create a json value deserializer.
	 *
	 * @param buffer
	 *            serialization buffer
	 * @return the value deserializer
	 */
	public static JsonArrayValueDeserializer create(final ArrayNode array) {
		return new JsonArrayValueDeserializer(array);
	}

	protected JsonArrayValueDeserializer(final ArrayNode array) {
		this.values = array.iterator();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kieker.common.record.io.IValueDeserializer#getBoolean()
	 */
	@Override
	public boolean getBoolean() {
		return this.values.next().asBoolean();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kieker.common.record.io.IValueDeserializer#getByte()
	 */
	@Override
	public byte getByte() throws NumberFormatException {
		return (byte) this.values.next().asInt();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kieker.common.record.io.IValueDeserializer#getChar()
	 */
	@Override
	public char getChar() {
		return this.values.next().asText().charAt(0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kieker.common.record.io.IValueDeserializer#getShort()
	 */
	@Override
	public short getShort() throws NumberFormatException {
		return (short) this.values.next().asInt();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kieker.common.record.io.IValueDeserializer#getInt()
	 */
	@Override
	public int getInt() throws NumberFormatException {
		return this.values.next().asInt();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kieker.common.record.io.IValueDeserializer#getLong()
	 */
	@Override
	public long getLong() throws NumberFormatException {
		return this.values.next().asLong();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kieker.common.record.io.IValueDeserializer#getFloat()
	 */
	@Override
	public float getFloat() throws NumberFormatException {
		return (float) this.values.next().asDouble();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kieker.common.record.io.IValueDeserializer#getDouble()
	 */
	@Override
	public double getDouble() throws NumberFormatException {
		return this.values.next().asDouble();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kieker.common.record.io.IValueDeserializer#getString()
	 */
	@Override
	public String getString() {
		return this.values.next().asText();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kieker.common.record.io.IValueDeserializer#getEnumeration(java.lang.Class)
	 */
	@Override
	public <T extends Enum<T>> T getEnumeration(final Class<T> clazz) throws RecordInstantiationException {
		final int value = this.values.next().asInt();
		return this.enumerationValueOf(clazz, value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kieker.common.record.io.IValueDeserializer#getBytes(byte[])
	 */
	@Override
	public byte[] getBytes(final byte[] target) {
		// TODO Auto-generated method stub
		return null;
	}

}
