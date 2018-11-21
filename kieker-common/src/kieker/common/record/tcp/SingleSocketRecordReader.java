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
package kieker.common.record.tcp;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.slf4j.Logger;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IRecordReceivedListener;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.BinaryValueDeserializer;
import kieker.common.registry.reader.ReaderRegistry;

/**
 * Represents a TCP reader which reads and reconstructs Kieker records from a single TCP stream.
 *
 * @author Christian Wulf (chw)
 *
 * @since 1.13
 */
public class SingleSocketRecordReader extends AbstractTcpReader {

	private static final int INT_BYTES = AbstractMonitoringRecord.TYPE_SIZE_INT;
	private static final int LONG_BYTES = AbstractMonitoringRecord.TYPE_SIZE_LONG;
	private static final Charset ENCODING = Charset.forName("UTF-8");

	private final ReaderRegistry<String> readerRegistry = new ReaderRegistry<String>();
	private final IRecordReceivedListener listener;
	private final CachedRecordFactoryCatalog recordFactories = new CachedRecordFactoryCatalog();

	/**
	 * Create single socket reader.
	 *
	 * @param port
	 *            port to listen to
	 * @param bufferCapacity
	 *            buffer capacity
	 * @param logger
	 *            logger to be used
	 * @param listener
	 *            listener to trigger on received records
	 */
	public SingleSocketRecordReader(final int port, final int bufferCapacity, final Logger logger, final IRecordReceivedListener listener) {
		super(port, bufferCapacity, logger);
		this.listener = listener;
	}

	@Override
	protected boolean onBufferReceived(final ByteBuffer buffer) {
		// identify record class
		if (buffer.remaining() < INT_BYTES) {
			return false;
		}
		final int clazzId = buffer.getInt();

		if (clazzId == -1) {
			return this.registerRegistryEntry(buffer);
		} else {
			return this.deserializeRecord(clazzId, buffer);
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

	private boolean deserializeRecord(final int clazzId, final ByteBuffer buffer) {
		// identify logging timestamp
		if (buffer.remaining() < LONG_BYTES) {
			return false;
		}
		final long loggingTimestamp = buffer.getLong(); // NOPMD (timestamp must be read before checking the buffer for record size)

		final String recordClassName = this.readerRegistry.get(clazzId);
		// identify record data
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactories.get(recordClassName);
		if (buffer.remaining() < recordFactory.getRecordSizeInBytes()) {
			return false;
		}

		try {
			final IMonitoringRecord record = recordFactory.create(BinaryValueDeserializer.create(buffer, this.readerRegistry));
			record.setLoggingTimestamp(loggingTimestamp);

			this.listener.onRecordReceived(record);
		} catch (final RecordInstantiationException ex) {
			super.logger.error("Failed to create: {}", recordClassName, ex);
		}

		return true;
	}

}
