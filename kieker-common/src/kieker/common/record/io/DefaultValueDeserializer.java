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
 * Default value deserializer implementation.
 * @author Holger Knoche
 * @since 1.13
 *
 */
public class DefaultValueDeserializer implements IValueDeserializer {

	private static final DefaultValueDeserializer INSTANCE = new DefaultValueDeserializer();
	
	protected DefaultValueDeserializer() {
		// Empty constructor
	}
	
	public static DefaultValueDeserializer instance() {
		return INSTANCE;
	}
	
	@Override
	public byte getByte(final ByteBuffer buffer) {
		return buffer.get();
	}
	
	@Override
	public int getInt(final ByteBuffer buffer) {
		return buffer.getInt();
	}

	@Override
	public long getLong(final ByteBuffer buffer) {
		return buffer.getLong();
	}
	
	@Override
	public double getDouble(final ByteBuffer buffer) {
		return buffer.getDouble();
	}

	@Override
	public String getString(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		final int stringId = this.getInt(buffer);
		return stringRegistry.get(stringId);
	}

	@Override
	public byte[] getBytes(final ByteBuffer buffer, final byte[] target) {
		buffer.get(target);
		return target;
	}

}
