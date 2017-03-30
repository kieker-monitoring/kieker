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
 * Default value serializer implementation.
 *
 * @author Holger Knoche
 * @since 1.13
 *
 */
public class DefaultValueSerializer implements IValueSerializer {

	private static final byte TRUE_VALUE = (byte) 1;
	private static final byte FALSE_VALUE = (byte) 0;

	private final ByteBuffer buffer;
	private final IRegistry<String> stringRegistry;

	protected DefaultValueSerializer(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		this.buffer = buffer;
		this.stringRegistry = stringRegistry;
	}

	public static DefaultValueSerializer create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		return new DefaultValueSerializer(buffer, stringRegistry);
	}

	@Override
	public void putBoolean(final boolean value) {
		final byte data = (value) ? TRUE_VALUE : FALSE_VALUE;
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
		final int stringId = this.stringRegistry.get(value);
		this.putInt(stringId);
	}

}
