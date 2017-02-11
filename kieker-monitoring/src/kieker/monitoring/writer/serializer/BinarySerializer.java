/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.writer.serializer;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.dataformat.FormatIdentifier;
import kieker.common.util.dataformat.VariableLengthEncoding;
import kieker.common.util.registry.IRegistry;

/**
 * Serializer for the default Kieker binary record format.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
public class BinarySerializer extends AbstractContainerFormatSerializer {

	/** Format identifier. */
	public static final int FORMAT_IDENTIFIER = FormatIdentifier.DEFAULT_BINARY_FORMAT.getIdentifierValue();

	/** Encoding to use for Strings. */
	private static final String ENCODING_NAME = "UTF-8";
	
	/** Charset to use for Strings. */
	private static final Charset CHARSET = Charset.forName(ENCODING_NAME);
	
	/**
	 * Creates a new serializer using the given configuration.
	 *
	 * @param configuration
	 *            The configuration to use
	 */
	public BinarySerializer(final Configuration configuration) {
		super(configuration);
	}

	@Override
	protected int getFormatIdentifier() {
		return FORMAT_IDENTIFIER;
	}

	@Override
	protected int writeRecords(final Collection<IMonitoringRecord> records, final ByteBuffer buffer) {
		final SerializerStringRegistry stringRegistry = new SerializerStringRegistry();

		// Write the record data into the buffer and collect the strings in the
		// registry
		final int recordDataSize = this.encodeRecords(records, buffer, stringRegistry);

		// Encode the string registry
		final int stringDataSize = this.encodeStringRegistry(stringRegistry, buffer);

		// Append both lengths to the chunk
		buffer.putInt(recordDataSize);

		// Return the total data size
		return (recordDataSize + stringDataSize) + 4;
	}

	private int encodeRecords(final Collection<IMonitoringRecord> records, final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		final int offsetBefore = buffer.position();

		for (final IMonitoringRecord record : records) {
			// Since writeBytes does not contain the type name and the logging timestamp,
			// these two fields must be serialized separately
			final String typeName = record.getClass().getName();
			final int typeNameId = stringRegistry.get(typeName);

			buffer.putInt(typeNameId);
			buffer.putLong(record.getLoggingTimestamp());

			record.writeBytes(buffer, stringRegistry);
		}

		final int offsetAfter = buffer.position();
		return (offsetAfter - offsetBefore);
	}

	private int encodeStringRegistry(final SerializerStringRegistry registry, final ByteBuffer buffer) {
		final List<String> allStrings = registry.getValues();

		final int offsetBefore = buffer.position();

		// Write the number of strings before the actual data
		VariableLengthEncoding.encodeInt(allStrings.size(), buffer);

		for (final String string : allStrings) {
			final byte[] stringBytes = string.getBytes(CHARSET);
			final int length = stringBytes.length;

			// We assume that the strings are usually short, and therefore use
			// a variable-length encoding to save space
			VariableLengthEncoding.encodeInt(length, buffer);
			buffer.put(stringBytes);
		}
		final int offsetAfter = buffer.position();

		return (offsetAfter - offsetBefore);
	}

	@Override
	public void init() {
		// Nothing to do
	}

	@Override
	public void terminate() {
		// Nothing to do
	}

}
