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

import kieker.common.registry.writer.IWriterRegistry;

/**
 * Default value serializer implementation.
 *
 * @author Holger Knoche
 * @since 1.13
 *
 */
public class BinaryValueSerializer implements IValueSerializer {

	private static final byte TRUE_VALUE = (byte) 1;
	private static final byte FALSE_VALUE = (byte) 0;

	private final ByteBuffer buffer;
	private final IWriterRegistry<String> stringRegistry;

	/**
	 * Create a binary value serializer.
	 *
	 * @param buffer
	 *            buffer for the serializer
	 * @param stringRegistry
	 *            the string registry used for the serializer
	 */
	protected BinaryValueSerializer(final ByteBuffer buffer, final IWriterRegistry<String> stringRegistry) {
		this.buffer = buffer;
		this.stringRegistry = stringRegistry;
	}

	/**
	 * Factory method to create a binary value serializer.
	 *
	 * @param buffer
	 *            serialization buffer
	 * @param stringRegistry
	 *            the string registry used for the serializer
	 * @return the value serializer
	 */
	public static BinaryValueSerializer create(final ByteBuffer buffer, final IWriterRegistry<String> stringRegistry) {
		return new BinaryValueSerializer(buffer, stringRegistry);
	}

	@Override
	public void putBoolean(final boolean value) {
		final byte data;

		if (value) {
			data = TRUE_VALUE;
		} else {
			data = FALSE_VALUE;
		}

		this.putByte(data);
	}

	@Override
	public void putByte(final byte value) {
		this.buffer.put(value);
	}

	@Override
	public void putInt(final int value) {
		this.buffer.putInt(value);
	}

	@Override
	public void putLong(final long value) {
		this.buffer.putLong(value);
	}

	@Override
	public void putDouble(final double value) {
		this.buffer.putDouble(value);
	}

	@Override
	public void putBytes(final byte[] value) {
		this.buffer.put(value);
	}

	@Override
	public void putString(final String value) {
		final int stringId = this.stringRegistry.getId(value);
		this.putInt(stringId);
	}

	@Override
	public void putChar(final char value) {
		this.buffer.putChar(value);
	}

	@Override
	public void putShort(final short value) { // NOPMD
		this.buffer.putShort(value);
	}

	@Override
	public void putFloat(final float value) {
		this.buffer.putFloat(value);
	}

	@Override
	public <T extends Enum<T>> void putEnumeration(final T value) {
		this.buffer.putInt(value.ordinal());
	}

}
