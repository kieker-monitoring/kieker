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
package kieker.analysis.plugin.reader.tcp.util;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import kieker.analysis.plugin.reader.util.IRecordReceivedListener;
import kieker.common.exception.RecordInstantiationException;
import kieker.common.logging.Log;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.DefaultValueDeserializer;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.reader.GetValueAdapter;
import kieker.common.util.registry.reader.ReaderRegistry;

/**
 * Represents a TCP reader which reads and reconstructs Kieker records from a single TCP stream.
 *
 * @author Christian Wulf (chw)
 *
 * @since 1.13
 */
public class SingleSocketRecordReader extends AbstractTcpReader {

	private static final int INT_BYTES = AbstractMonitoringRecord.TYPE_SIZE_INT;
	private static final Charset ENCODING = Charset.forName("UTF-8");

	/** Entry ID for a registry entry. */
	private static final byte REGISTRY_ENTRY_ID = (byte) 0xFF;

	private final ReaderRegistry<String> readerRegistry = new ReaderRegistry<>();
	private final IRegistry<String> stringRegistryWrapper;
	private final IRecordReceivedListener listener;
	private final CachedRecordFactoryCatalog recordFactories = new CachedRecordFactoryCatalog();

	public SingleSocketRecordReader(final int port, final int bufferCapacity, final Log logger, final IRecordReceivedListener listener) {
		super(port, bufferCapacity, logger);
		this.listener = listener;
		this.stringRegistryWrapper = new GetValueAdapter<>(this.readerRegistry);
	}

	@Override
	protected boolean onBufferReceived(final ByteBuffer buffer) {
		final IValueDeserializer deserializer = DefaultValueDeserializer.create(buffer, this.stringRegistryWrapper);

		final byte entryId = deserializer.getByte();

		if (entryId == REGISTRY_ENTRY_ID) {
			return this.registerRegistryEntry(buffer);
		} else {
			return this.deserializeRecord(deserializer);
		}
	}

	private boolean registerRegistryEntry(final ByteBuffer buffer) {
		// identify string identifier and string length
		if (buffer.remaining() < (INT_BYTES + INT_BYTES)) {
			return false;
		}

		final int id = buffer.getInt(); // NOPMD (id must be read before stringLength)
		final int stringLength = buffer.getInt();

		if (buffer.remaining() < stringLength) {
			return false;
		}

		final byte[] strBytes = new byte[stringLength];
		buffer.get(strBytes);
		final String string = new String(strBytes, ENCODING);

		this.readerRegistry.register(id, string);
		return true;
	}

	private boolean deserializeRecord(final IValueDeserializer deserializer) {
		final String recordClassName = deserializer.getString();
		final long loggingTimestamp = deserializer.getLong(); // NOPMD (timestamp must be read before checking the buffer for record size)

		// identify record data
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactories.get(recordClassName);

		try {
			final IMonitoringRecord record = recordFactory.create(deserializer);
			record.setLoggingTimestamp(loggingTimestamp);

			this.listener.onRecordReceived(record);
		} catch (final RecordInstantiationException ex) {
			super.logger.error("Failed to create: " + recordClassName, ex);
		}

		return true;
	}

}
