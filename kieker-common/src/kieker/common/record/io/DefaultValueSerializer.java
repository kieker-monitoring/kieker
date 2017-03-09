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
 * @author Holger Knoche
 * @since 1.13
 *
 */
public class DefaultValueSerializer implements IValueSerializer {

	private static final DefaultValueSerializer INSTANCE = new DefaultValueSerializer();
	
	protected DefaultValueSerializer() {
		// Empty constructor
	}
	
	public static DefaultValueSerializer instance() {
		return INSTANCE;
	}
	
	@Override
	public void putByte(final byte value, final ByteBuffer buffer) {
		buffer.put(value);
	}
	
	@Override
	public void putInt(final int value, final ByteBuffer buffer) {
		buffer.putInt(value);
	}

	@Override
	public void putLong(final long value, final ByteBuffer buffer) {
		buffer.putLong(value);
	}

	@Override
	public void putDouble(final double value, final ByteBuffer buffer) {
		buffer.putDouble(value);
	}
	
	@Override
	public void putBytes(final byte[] value, final ByteBuffer buffer) {
		buffer.put(value);
	}

	@Override
	public void putString(final String value, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		final int stringId = stringRegistry.get(value);
		this.putInt(stringId, buffer);
	}

}
