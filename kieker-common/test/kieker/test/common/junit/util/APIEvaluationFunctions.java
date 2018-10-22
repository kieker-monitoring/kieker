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

package kieker.test.common.junit.util;

import java.nio.ByteBuffer;

import org.junit.Assert;

import kieker.common.registry.IRegistry;

/**
 * The PMD warning suppression is necessary to avoid complexity
 * warnings about the createByteBuffer method.
 * 
 * @author Reiner Jung
 * 
 * @since 1.10
 */
@SuppressWarnings({ "PMD.ModifiedCyclomaticComplexity", "PMD.StdCyclomaticComplexity" })
public final class APIEvaluationFunctions {

	/**
	 * Utility class.
	 */
	private APIEvaluationFunctions() {
		// Not for instantiation
	}

	/**
	 * Allocate a byte buffer and initialize it with values from an object array
	 * representing the values of a record.
	 * 
	 * @param size
	 *            the size of the serialized record
	 * @param stringRegistry
	 *            the registry for the string lookup
	 * @param objects
	 *            objects containing the values for a record
	 * 
	 * @return the completely filled buffer
	 */
	public static ByteBuffer createByteBuffer(final int size, final IRegistry<String> stringRegistry, final Object[] objects) { // NOPMD
		final ByteBuffer buffer = ByteBuffer.allocate(size);
		for (final Object object : objects) {
			if (object instanceof Byte) {
				buffer.put((Byte) object);
			} else if (object instanceof Short) {
				buffer.putShort((Short) object);
			} else if (object instanceof Integer) {
				buffer.putInt((Integer) object);
			} else if (object instanceof Long) {
				buffer.putLong((Long) object);
			} else if (object instanceof Float) {
				buffer.putFloat((Float) object);
			} else if (object instanceof Double) {
				buffer.putDouble((Double) object);
			} else if (object instanceof Boolean) {
				buffer.put((byte) ((Boolean) object ? 1 : 0)); // NOCS
			} else if (object instanceof Character) {
				buffer.putChar((Character) object);
			} else if (object instanceof String) {
				buffer.putInt(stringRegistry.get((String) object));
			} else {
				Assert.fail("Unsupported record value type " + object.getClass().getName());
			}
		}

		Assert.assertEquals("Buffer size and usage differ.", buffer.position(), size);
		buffer.position(0);

		return buffer;
	}
}
