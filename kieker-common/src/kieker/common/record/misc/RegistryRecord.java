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

package kieker.common.record.misc;

import java.io.UnsupportedEncodingException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.registry.reader.ReaderRegistry;

/**
 * Record used to associate Objects (typically Strings) with unique ids.
 *
 * @author Jan Waller
 *
 * @since 1.5
 */
public final class RegistryRecord extends AbstractMonitoringRecord {
	public static final int SIZE = TYPE_SIZE_INT + TYPE_SIZE_STRING;
	public static final Class<?>[] TYPES = new Class<?>[] {
		int.class, // id
		String.class, // object
	};

	public static final String ENCODING = "UTF-8";
	public static final int CLASS_ID = -1;

	private static final long serialVersionUID = -8264706549927546468L;

	private final int id;
	private final String string;
	private final byte[] strBytes;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param id
	 *            The ID.
	 * @param string
	 *            The string.
	 */
	public RegistryRecord(final int id, final String string) {
		this.id = id;
		this.string = string;
		this.strBytes = RegistryRecord.stringToBytes(this.string);
	}

	/**
	 * This constructor converts the given array into a record. It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 *
	 * @param values
	 *            The values for the record.
	 */
	public RegistryRecord(final Object[] values) { // NOPMD (direct store of E (usually String))
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.id = (Integer) values[0];
		this.string = (String) (values[1]);
		this.strBytes = RegistryRecord.stringToBytes(this.string);
	}

	/**
	 * This constructor converts the given array into a record.
	 *
	 * @param deserializer
	 *            The deserializer to decode the record with.
	 *
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public RegistryRecord(final IValueDeserializer deserializer) throws BufferUnderflowException { // NOPMD
		this.id = deserializer.getInt();
		this.string = deserializer.getString();
		this.strBytes = this.string.getBytes();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putInt(this.getId());
		serializer.putString(this.getString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?>[] getValueTypes() {
		return TYPES; // NOPMD
	}

	@Override
	public String[] getValueNames() {
		return new String[] { "id", "string", "strBytes" };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSize() {
		return 4 + 4 + this.strBytes.length;
	}

	/**
	 * @return the String as byte[]
	 */
	public final byte[] getStrBytes() {
		return this.strBytes; // NOPMD (We know what we are doing here...)
	}

	/**
	 * @return the id
	 */
	public final int getId() {
		return this.id;
	}

	/**
	 * @return the string
	 */
	public final String getString() {
		return this.string;
	}

	/**
	 * Static function used to register strings stored in a byte buffer as string in the string registry with the proper id.
	 *
	 * @param buffer
	 *            the byte buffer containing the string
	 * @param stringRegistry
	 *            the registry where the string is stored
	 *
	 * @throws BufferOverflowException
	 *             if the length encoded in the buffer exceeds the buffers boundary
	 */
	public static final void registerRecordInRegistry(final ByteBuffer buffer, final ReaderRegistry<String> stringRegistry) throws BufferOverflowException {
		final int id = buffer.getInt();
		final byte[] strBytes = new byte[buffer.getInt()];
		buffer.get(strBytes);
		final String string = RegistryRecord.bytesToString(strBytes);
		stringRegistry.register(id, string);
	}

	private static String bytesToString(final byte[] strBytes) {
		String str;
		try {
			str = new String(strBytes, ENCODING);
		} catch (final UnsupportedEncodingException e) {
			str = new String(strBytes, Charset.defaultCharset());
		}
		return str;
	}

	private static byte[] stringToBytes(final String string) {
		byte[] tmpBytes;
		try {
			tmpBytes = string.getBytes(ENCODING);
		} catch (final UnsupportedEncodingException ex) {
			tmpBytes = string.getBytes(Charset.defaultCharset());
		}
		return tmpBytes;
	}

}
