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

package kieker.analysis.plugin.reader.newio.deserializer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import kieker.analysis.IProjectContext;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Holger Knoche
 *
 * @since 1.13
 */
public class BinaryDeserializer extends AbstractContainerFormatDeserializer {

	/** Format identifier. Reads "DFLT" in ASCII encoding. */
	public static final int FORMAT_IDENTIFIER = 0x44464C54;

	private final CachedRecordFactoryCatalog cachedRecordFactoryCatalog = CachedRecordFactoryCatalog.getInstance();

	/**
	 * Creates a new record deserializer.
	 *
	 * @param configuration
	 *            The configuration to use
	 * @param projectContext
	 *            The project context to use
	 */
	public BinaryDeserializer(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	protected int getFormatIdentifier() {
		return FORMAT_IDENTIFIER;
	}

	@Override
	protected List<IMonitoringRecord> decodeRecords(final ByteBuffer buffer, final int dataSize) throws InvalidFormatException {
		final int baseOffset = buffer.position();

		// Retrieve the offset of the string table (last four bytes of the data)
		buffer.position((baseOffset + dataSize) - 4);
		final int stringTableOffset = buffer.getInt();

		// Position the buffer and decode the string table
		final int absoluteStringTableOffset = baseOffset + stringTableOffset;
		buffer.position(absoluteStringTableOffset);
		final IRegistry<String> stringRegistry = this.decodeStringRegistry(buffer);

		// Position the buffer and decode the records
		buffer.position(baseOffset);
		final List<IMonitoringRecord> records = this.decodeMonitoringRecords(buffer, stringRegistry, absoluteStringTableOffset);

		return records;
	}

	private IRegistry<String> decodeStringRegistry(final ByteBuffer buffer) {
		final int numberOfEntries = BinaryDeserializer.decodeInt(buffer);
		final List<String> values = new ArrayList<String>(numberOfEntries);

		for (int entryIndex = 0; entryIndex < numberOfEntries; entryIndex++) {
			final int entryLength = BinaryDeserializer.decodeInt(buffer);
			final byte[] entryDataBytes = new byte[entryLength];
			buffer.get(entryDataBytes);

			final String entryData = new String(entryDataBytes);
			values.add(entryData);
		}

		return new DeserializerStringRegistry(values);
	}

	private List<IMonitoringRecord> decodeMonitoringRecords(final ByteBuffer buffer, final IRegistry<String> stringRegistry, final int endOffset) {
		final List<IMonitoringRecord> records = new ArrayList<IMonitoringRecord>();
		int currentOffset = buffer.position();

		final CachedRecordFactoryCatalog recordFactoryCatalog = this.cachedRecordFactoryCatalog;

		while (currentOffset < endOffset) {
			final int recordTypeId = buffer.getInt();
			final String recordTypeName = stringRegistry.get(recordTypeId);
			final long loggingTimestamp = buffer.getLong();

			final IRecordFactory<? extends IMonitoringRecord> recordFactory = recordFactoryCatalog.get(recordTypeName);
			final IMonitoringRecord record = recordFactory.create(buffer, stringRegistry);
			record.setLoggingTimestamp(loggingTimestamp);

			records.add(record);

			currentOffset = buffer.position();
		}

		// The record data must end exactly at the given end offset
		if (currentOffset != endOffset) {
			throw new InvalidFormatException("Invalid record data found, should have ended at offset " + endOffset + ", but ended at offset " + currentOffset + ".");
		}

		return records;
	}

	/**
	 * Decodes a variable-length integer value stored at the current position
	 * in the byte buffer.
	 *
	 * @param buffer
	 *            The buffer to decode the data from
	 * @return The decoded integer value
	 */
	private static int decodeInt(final ByteBuffer buffer) {
		// FIXME Move VariableLengthEncoding to common so that this redundant implementation
		// can be removed
		final int startPosition = buffer.position();
		int value = 0;
		int shiftAmount = 0;

		while (true) {
			final byte currentByte = buffer.get();
			// Non-terminal bytes have their msb set and are thus negative
			final boolean terminalByte = (currentByte >= 0);

			if (terminalByte) {
				value |= (currentByte << shiftAmount);
				break;
			} else {
				value |= ((currentByte & 0x7F) << shiftAmount);
				shiftAmount += 7;
			}
		}

		// Check whether the highest allowed shift amount was exceeded
		if (shiftAmount > 28) {
			throw new IllegalArgumentException("Unterminated variable-length int found at position " + startPosition);
		}

		return value;
	}

	@Override
	public void init() throws Exception {
		// Nothing to do
	}

	@Override
	public void terminate() {
		// Nothing to do
	}

}
