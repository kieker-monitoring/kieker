/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.monitoring.probe.bytebuddy.state;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Naive, depth-bounded binary serializer for object state. It walks the object graph via reflection
 * (without calling any method of the serialized objects) and writes it in a simple heap-dump-like format.
 * Each call of {@link #serialize(Object[])} produces a self-contained entry, i.e., nothing is shared
 * between entries (this is intentionally naive, to measure the size of uncompressed state data).
 *
 * <p>
 * Depth semantics: root values (this, arguments, return value) have depth 0. An object at depth d is
 * expanded (i.e., its fields or array elements are written) if d &lt; maxDepth; otherwise, only its class
 * and identity hash code are written. Hence, maxDepth = 0 writes only identities of root objects, maxDepth = 1
 * writes the fields of root objects, and maxDepth = {@link Integer#MAX_VALUE} writes the full reachable graph.
 * Primitive values, boxed primitives, strings, enums and classes are always written inline.
 * </p>
 *
 * <p>
 * Not thread-safe; use one instance per thread.
 * </p>
 *
 * @author David Georg Reichelt
 */
public final class StateSerializer {

	private static final byte TAG_NULL = 0;
	private static final byte TAG_BOOLEAN = 1;
	private static final byte TAG_BYTE = 2;
	private static final byte TAG_CHAR = 3;
	private static final byte TAG_SHORT = 4;
	private static final byte TAG_INT = 5;
	private static final byte TAG_LONG = 6;
	private static final byte TAG_FLOAT = 7;
	private static final byte TAG_DOUBLE = 8;
	private static final byte TAG_STRING = 9;
	private static final byte TAG_ENUM = 10;
	private static final byte TAG_CLASS = 11;
	/** Reference to an object that is (or will be) expanded in this entry. */
	private static final byte TAG_REF = 12;
	/** Object that is not expanded because of the depth limit: class and identity hash code. */
	private static final byte TAG_OPAQUE = 13;
	/** Start of an expanded object. */
	private static final byte TAG_OBJECT = 14;
	/** Start of an expanded array. */
	private static final byte TAG_ARRAY = 15;
	/** Field that could not be read via reflection (e.g., due to module restrictions). */
	private static final byte TAG_INACCESSIBLE = 16;
	/** Definition of a class name that is used for the first time in this entry. */
	private static final byte TAG_CLASSDEF = 17;

	private static final Map<Class<?>, Field[]> FIELD_CACHE = new java.util.concurrent.ConcurrentHashMap<>();
	private static final Field[] NO_FIELDS = new Field[0];

	private final int maxDepth;
	private final GrowableBuffer buffer = new GrowableBuffer();
	private final IdentityHashMap<Object, Integer> objectIds = new IdentityHashMap<>(); // NOPMD (thread-confined)
	private final Map<Class<?>, Integer> classIds = new HashMap<>(); // NOPMD (thread-confined)
	private final ArrayDeque<Object> queue = new ArrayDeque<>();
	private final ArrayDeque<Integer> queueDepths = new ArrayDeque<>();

	private long expandedObjects;

	public StateSerializer(final int maxDepth) {
		this.maxDepth = maxDepth;
	}

	/**
	 * Serializes the given root values into an entry.
	 *
	 * @param roots
	 *            root values, e.g., this, arguments and return value
	 * @return the buffer containing the entry; valid until the next call
	 */
	public GrowableBuffer serialize(final Object[] roots) {
		this.buffer.reset();
		this.objectIds.clear();
		this.classIds.clear();
		this.queue.clear();
		this.queueDepths.clear();

		this.buffer.writeInt(roots.length);
		for (final Object root : roots) {
			this.writeValue(root, 0);
		}
		while (!this.queue.isEmpty()) {
			final Object current = this.queue.poll();
			final int depth = this.queueDepths.poll();
			this.expand(current, depth);
		}
		return this.buffer;
	}

	/**
	 * @return the overall count of expanded objects since creation of this serializer
	 */
	public long getExpandedObjects() {
		return this.expandedObjects;
	}

	private void writeValue(final Object value, final int depth) { // NOPMD (type dispatch)
		if (value == null) {
			this.buffer.writeByte(TAG_NULL);
		} else if (value instanceof String) {
			this.buffer.writeByte(TAG_STRING);
			this.buffer.writeString((String) value);
		} else if (value instanceof Integer) {
			this.buffer.writeByte(TAG_INT);
			this.buffer.writeInt((Integer) value);
		} else if (value instanceof Long) {
			this.buffer.writeByte(TAG_LONG);
			this.buffer.writeLong((Long) value);
		} else if (value instanceof Boolean) {
			this.buffer.writeByte(TAG_BOOLEAN);
			this.buffer.writeByte(toByte((Boolean) value));
		} else if (value instanceof Double) {
			this.buffer.writeByte(TAG_DOUBLE);
			this.buffer.writeLong(Double.doubleToRawLongBits((Double) value));
		} else if (value instanceof Float) {
			this.buffer.writeByte(TAG_FLOAT);
			this.buffer.writeInt(Float.floatToRawIntBits((Float) value));
		} else if (value instanceof Character) {
			this.buffer.writeByte(TAG_CHAR);
			this.buffer.writeShort((Character) value);
		} else if (value instanceof Byte) {
			this.buffer.writeByte(TAG_BYTE);
			this.buffer.writeByte((Byte) value);
		} else if (value instanceof Short) {
			this.buffer.writeByte(TAG_SHORT);
			this.buffer.writeShort((Short) value);
		} else if (value instanceof Enum) {
			this.buffer.writeByte(TAG_ENUM);
			this.writeClass(((Enum<?>) value).getDeclaringClass());
			this.buffer.writeInt(((Enum<?>) value).ordinal());
		} else if (value instanceof Class) {
			this.buffer.writeByte(TAG_CLASS);
			this.writeClass((Class<?>) value);
		} else {
			this.writeReference(value, depth);
		}
	}

	private void writeReference(final Object value, final int depth) {
		final Integer existingId = this.objectIds.get(value);
		if (existingId != null) {
			this.buffer.writeByte(TAG_REF);
			this.buffer.writeInt(existingId);
		} else if (depth >= this.maxDepth) {
			this.buffer.writeByte(TAG_OPAQUE);
			this.writeClass(value.getClass());
			this.buffer.writeInt(System.identityHashCode(value));
		} else {
			final int id = this.objectIds.size();
			this.objectIds.put(value, id);
			this.queue.add(value);
			this.queueDepths.add(depth);
			this.buffer.writeByte(TAG_REF);
			this.buffer.writeInt(id);
		}
	}

	private void expand(final Object value, final int depth) {
		this.expandedObjects++;
		final Class<?> clazz = value.getClass();
		if (clazz.isArray()) {
			this.buffer.writeByte(TAG_ARRAY);
			this.writeClass(clazz);
			this.writeArray(value, clazz.getComponentType(), depth);
		} else {
			this.buffer.writeByte(TAG_OBJECT);
			this.writeClass(clazz);
			for (final Field field : getFields(clazz)) {
				this.writeField(value, field, depth);
			}
		}
	}

	private void writeArray(final Object array, final Class<?> componentType, final int depth) { // NOPMD (type dispatch)
		final int length = Array.getLength(array);
		this.buffer.writeInt(length);
		if (componentType == int.class) {
			for (final int v : (int[]) array) {
				this.buffer.writeInt(v);
			}
		} else if (componentType == long.class) {
			for (final long v : (long[]) array) {
				this.buffer.writeLong(v);
			}
		} else if (componentType == byte.class) {
			this.buffer.writeBytes((byte[]) array);
		} else if (componentType == char.class) {
			for (final char v : (char[]) array) {
				this.buffer.writeShort(v);
			}
		} else if (componentType == boolean.class) {
			for (final boolean v : (boolean[]) array) {
				this.buffer.writeByte(toByte(v));
			}
		} else if (componentType == double.class) {
			for (final double v : (double[]) array) {
				this.buffer.writeLong(Double.doubleToRawLongBits(v));
			}
		} else if (componentType == float.class) {
			for (final float v : (float[]) array) {
				this.buffer.writeInt(Float.floatToRawIntBits(v));
			}
		} else if (componentType == short.class) {
			for (final short v : (short[]) array) { // NOPMD (primitive arrays are written as they are)
				this.buffer.writeShort(v);
			}
		} else {
			for (final Object element : (Object[]) array) {
				this.writeValue(element, depth + 1);
			}
		}
	}

	private void writeField(final Object owner, final Field field, final int depth) { // NOPMD (type dispatch)
		final Class<?> type = field.getType();
		try {
			if (type == int.class) {
				this.buffer.writeInt(field.getInt(owner));
			} else if (type == long.class) {
				this.buffer.writeLong(field.getLong(owner));
			} else if (type == boolean.class) {
				this.buffer.writeByte(toByte(field.getBoolean(owner)));
			} else if (type == double.class) {
				this.buffer.writeLong(Double.doubleToRawLongBits(field.getDouble(owner)));
			} else if (type == float.class) {
				this.buffer.writeInt(Float.floatToRawIntBits(field.getFloat(owner)));
			} else if (type == char.class) {
				this.buffer.writeShort(field.getChar(owner));
			} else if (type == byte.class) {
				this.buffer.writeByte(field.getByte(owner));
			} else if (type == short.class) {
				this.buffer.writeShort(field.getShort(owner));
			} else {
				this.writeValue(field.get(owner), depth + 1);
			}
		} catch (final IllegalAccessException | IllegalArgumentException e) { // NOPMD (naive measurement: skip unreadable fields)
			this.buffer.writeByte(TAG_INACCESSIBLE);
		}
	}

	private static byte toByte(final boolean value) {
		if (value) {
			return 1;
		}
		return 0;
	}

	private void writeClass(final Class<?> clazz) {
		final Integer existingId = this.classIds.get(clazz);
		if (existingId != null) {
			this.buffer.writeInt(existingId);
		} else {
			final int id = this.classIds.size();
			this.classIds.put(clazz, id);
			this.buffer.writeInt(id);
			this.buffer.writeByte(TAG_CLASSDEF);
			this.buffer.writeString(clazz.getName());
		}
	}

	/**
	 * Returns all non-static fields of the class and its superclasses; fields that cannot be made accessible
	 * (e.g., in JDK modules on newer Java versions) are omitted.
	 */
	private static Field[] getFields(final Class<?> clazz) {
		Field[] fields = FIELD_CACHE.get(clazz);
		if (fields == null) {
			final List<Field> result = new ArrayList<>();
			for (Class<?> current = clazz; current != null; current = current.getSuperclass()) {
				for (final Field field : current.getDeclaredFields()) {
					if (!Modifier.isStatic(field.getModifiers())) {
						try {
							field.setAccessible(true);
							result.add(field);
						} catch (final SecurityException | java.lang.reflect.InaccessibleObjectException e) { // NOPMD (Java 9+ modules)
							// skip field
						}
					}
				}
			}
			if (result.isEmpty()) {
				fields = NO_FIELDS;
			} else {
				fields = result.toArray(NO_FIELDS);
			}
			FIELD_CACHE.put(clazz, fields);
		}
		return fields;
	}

	/**
	 * Simple growable big-endian byte buffer; avoids the synchronization of ByteArrayOutputStream.
	 */
	public static final class GrowableBuffer {
		private byte[] data = new byte[4096];
		private int count;

		private void reset() {
			this.count = 0;
		}

		public int size() {
			return this.count;
		}

		public byte[] array() { // NOPMD (intentionally no copy; valid until the next serialization)
			return this.data;
		}

		private void ensure(final int additional) {
			final int required = this.count + additional;
			if (required > this.data.length) {
				int newLength = this.data.length * 2;
				while (newLength < required) {
					newLength *= 2;
				}
				this.data = java.util.Arrays.copyOf(this.data, newLength);
			}
		}

		private void writeByte(final int v) {
			this.ensure(1);
			this.data[this.count++] = (byte) v;
		}

		private void writeShort(final int v) {
			this.ensure(2);
			this.data[this.count++] = (byte) (v >>> 8);
			this.data[this.count++] = (byte) v;
		}

		private void writeInt(final int v) {
			this.ensure(4);
			this.data[this.count++] = (byte) (v >>> 24);
			this.data[this.count++] = (byte) (v >>> 16);
			this.data[this.count++] = (byte) (v >>> 8);
			this.data[this.count++] = (byte) v;
		}

		private void writeLong(final long v) {
			this.writeInt((int) (v >>> 32));
			this.writeInt((int) v);
		}

		private void writeBytes(final byte[] bytes) {
			this.ensure(bytes.length);
			System.arraycopy(bytes, 0, this.data, this.count, bytes.length);
			this.count += bytes.length;
		}

		private void writeString(final String s) {
			final byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
			this.writeInt(bytes.length);
			this.writeBytes(bytes);
		}
	}
}
