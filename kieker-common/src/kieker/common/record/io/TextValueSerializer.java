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

import java.nio.CharBuffer;

/**
 * Text value serializer implementation.
 *
 * @author Reiner Jung
 * @since 1.13
 */
public class TextValueSerializer implements IValueSerializer {

	private final CharBuffer buffer;

	/**
	 * Create a text value serializer.
	 *
	 * @param buffer
	 *            buffer for the serializer
	 */
	protected TextValueSerializer(final CharBuffer buffer) {
		this.buffer = buffer;
	}

	/**
	 * Factory method to create a text value serializer.
	 *
	 * @param buffer
	 *            serialization buffer
	 * @return the value serializer
	 */
	public static TextValueSerializer create(final CharBuffer buffer) {
		return new TextValueSerializer(buffer);
	}

	@Override
	public void putBoolean(final boolean value) {
		this.buffer.put(";" + value);
	}

	@Override
	public void putByte(final byte value) {
		this.buffer.put(";" + value);
	}

	@Override
	public void putChar(final char value) {
		this.buffer.put(";" + value);
	}

	@Override
	public void putShort(final short value) { // NOPMD
		this.buffer.put(";" + value);
	}

	@Override
	public void putInt(final int value) {
		this.buffer.put(";" + value);
	}

	@Override
	public void putLong(final long value) {
		this.buffer.put(";" + value);
	}

	@Override
	public void putFloat(final float value) {
		this.buffer.put(";" + value);
	}

	@Override
	public void putDouble(final double value) {
		this.buffer.put(";" + value);
	}

	@Override
	public void putBytes(final byte[] value) {
		this.buffer.put(";" + String.valueOf(value));
	}

	@Override
	public void putString(final String value) {
		if (value != null) {
			this.buffer.put(";" + value.replaceAll(";", "\\;"));
		} else {
			this.buffer.put(";");
		}
	}

	@Override
	public <T extends Enum<T>> void putEnumeration(final T value) {
		this.buffer.put(";" + value.ordinal());
	}

}
