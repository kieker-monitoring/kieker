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

package kieker.common.record.io;

import java.nio.ByteBuffer;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.registry.reader.ReaderRegistry;
import kieker.common.util.dataformat.VariableLengthEncoding;

/**
 * Default value deserializer implementation.
 *
 * @author Holger Knoche
 * @author Reiner Jung - enumeration support
 * @since 1.13
 *
 */
public class BinaryValueDeserializer extends AbstractValueDeserializer implements IValueDeserializer {

	private static final byte TRUE_VALUE = (byte) 1;

	private final ByteBuffer buffer;
	private final ReaderRegistry<String> stringRegistry;

	/**
	 * Create a binary value deserializer.
	 *
	 * @param buffer
	 *            buffer for the deserializer
	 * @param stringRegistry
	 *            the string registry used for the deserializer
	 */
	protected BinaryValueDeserializer(final ByteBuffer buffer, final ReaderRegistry<String> stringRegistry) {
		this.buffer = buffer;
		this.stringRegistry = stringRegistry;
	}

	/**
	 * Factory method to create a binary value deserializer.
	 *
	 * @param buffer
	 *            serialization buffer
	 * @param stringRegistry
	 *            the string registry used for the deserializer
	 * @return the value deserializer
	 */
	public static BinaryValueDeserializer create(final ByteBuffer buffer, final ReaderRegistry<String> stringRegistry) {
		return new BinaryValueDeserializer(buffer, stringRegistry);
	}

	@Override
	public boolean getBoolean() { // NOPMD
		return (this.getByte() == BinaryValueDeserializer.TRUE_VALUE);
	}

	@Override
	public byte getByte() {
		return this.buffer.get();
	}

	@Override
	public int getInt() {
		return this.buffer.getInt();
	}

	@Override
	public long getLong() {
		return this.buffer.getLong();
	}

	@Override
	public double getDouble() {
		return this.buffer.getDouble();
	}

	private int getTableIndex() {
		// Use variable-length encoding for indexes into the string table
		return VariableLengthEncoding.decodeInt(this.buffer);
	}

	@Override
	public String getString() {
		final int stringId = this.getTableIndex();
		return this.stringRegistry.get(stringId);
	}

	@Override
	public <T extends Enum<T>> T getEnumeration(final Class<T> clazz) throws RecordInstantiationException {
		final int value = this.buffer.getInt();
		return this.enumerationValueOf(clazz, value);
	}

	@Override
	public char getChar() {
		return this.buffer.getChar();
	}

	@Override
	public short getShort() { // NOPMD
		return this.buffer.getShort();
	}

	@Override
	public float getFloat() {
		return this.buffer.getFloat();
	}

}
