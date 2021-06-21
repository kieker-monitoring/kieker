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
package kieker.common.record.io;

import java.nio.CharBuffer;

import kieker.common.exception.RecordInstantiationException;

/**
 * Text value deserializer implementation.
 *
 * @author Reiner Jung
 * @since 1.13
 *
 */
public class TextValueDeserializer extends AbstractValueDeserializer implements IValueDeserializer {

	private final CharBuffer buffer;

	/**
	 * Create a text value deserializer.
	 *
	 * @param buffer
	 *            buffer for the deserializer
	 */
	protected TextValueDeserializer(final CharBuffer buffer) {
		this.buffer = buffer;
	}

	/**
	 * Factory method to create a text value deserializer.
	 *
	 * @param buffer
	 *            serialization buffer
	 * @return the value deserializer
	 */
	public static TextValueDeserializer create(final CharBuffer buffer) {
		return new TextValueDeserializer(buffer);
	}

	@Override
	public boolean getBoolean() { // NOPMD
		return Boolean.parseBoolean(this.readValue());
	}

	@Override
	public byte getByte() throws NumberFormatException {
		return Byte.parseByte(this.readValue());
	}

	@Override
	public char getChar() {
		final char ch = this.buffer.get();
		this.buffer.get(); /** reading semicolon. */
		return ch;
	}

	@Override
	public short getShort() throws NumberFormatException { // NOPMD
		return Short.parseShort(this.readValue());
	}

	@Override
	public int getInt() throws NumberFormatException {
		return Integer.parseInt(this.readValue());
	}

	@Override
	public long getLong() throws NumberFormatException {
		return Long.parseLong(this.readValue());
	}

	@Override
	public float getFloat() throws NumberFormatException {
		return Float.parseFloat(this.readValue());
	}

	@Override
	public double getDouble() throws NumberFormatException {
		return Double.parseDouble(this.readValue());
	}

	@Override
	public String getString() {
		return this.readValue();
	}

	@Override
	public <T extends Enum<T>> T getEnumeration(final Class<T> clazz) throws RecordInstantiationException {
		final int value = Integer.parseInt(this.readValue());
		return this.enumerationValueOf(clazz, value);
	}

	private String readValue() {
		final char[] charArray = new char[this.buffer.limit()]; // NOPMD plural name not relevant
		final int remaining = this.buffer.limit() - this.buffer.position();
		char ch;
		int i = 0;
		if (remaining > 0) {
			do {
				ch = this.buffer.get();
				if (ch == '\\') {
					charArray[i++] = ch;
					charArray[i++] = this.buffer.get();
				} else if (ch != ';') {
					charArray[i++] = ch;
				}
			} while ((ch != ';') && (i < remaining));
			return new String(charArray, 0, i);
		} else {
			return "";
		}
	}

}
