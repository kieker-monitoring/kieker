/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.plugin.reader.util;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

import org.slf4j.Logger;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.BinaryValueDeserializer;
import kieker.common.registry.reader.ReaderRegistry;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 * @deprecated since 1.15.1 old plugin api
 */
@Deprecated
public class ByteBufferDeserializer {

	private static final int INT_BYTES = AbstractMonitoringRecord.TYPE_SIZE_INT;
	private static final int LONG_BYTES = AbstractMonitoringRecord.TYPE_SIZE_LONG;

	private final ReaderRegistry<String> stringRegistry;
	private final Logger logger; // NOPMD (logger from the caller)
	private final CachedRecordFactoryCatalog recordFactories;
	private final ByteBuffer buffer;
	private IMonitoringRecordReceiver recordReceiver;

	public ByteBufferDeserializer(final ReaderRegistry<String> readerRegistry, final Logger logger, final int bufferCapacity) {
		super();
		this.stringRegistry = readerRegistry;
		this.logger = logger;
		this.recordFactories = new CachedRecordFactoryCatalog();
		this.buffer = ByteBuffer.allocateDirect(bufferCapacity);
	}

	public void deserialize(final ReadableByteChannel channel) throws IOException {
		final ByteBuffer buffer = this.buffer; // NOCS (hides field)
		while ((channel.read(buffer) != -1)) {
			this.process(buffer);
		}
	}

	private void process(final ByteBuffer buffer) { // NOCS (hides field)
		buffer.flip();
		try {
			while (buffer.hasRemaining()) {
				buffer.mark();
				final boolean success = this.onBufferReceived(buffer);
				if (!success) {
					buffer.reset();
					buffer.compact();
					return;
				}
			}
			buffer.clear();
		} catch (final BufferUnderflowException ex) {
			this.logger.warn("Unexpected buffer underflow. Resetting and compacting buffer.", ex);
			buffer.reset();
			buffer.compact();
		}
	}

	private boolean onBufferReceived(final ByteBuffer buffer) { // NOCS (hides field)
		// identify record class
		if (buffer.remaining() < INT_BYTES) {
			return false;
		}
		final int clazzId = buffer.getInt(); // NOPMD (clazzId must be read before reading timestamp)

		// identify logging timestamp
		if (buffer.remaining() < LONG_BYTES) {
			return false;
		}
		final long loggingTimestamp = buffer.getLong(); // NOPMD (timestamp must be read before checking the buffer for record size)

		final String recordClassName = this.stringRegistry.get(clazzId);
		if (recordClassName != null) {
			// identify record data
			final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactories.get(recordClassName);
			if (recordFactory != null) {
				if (buffer.remaining() < recordFactory.getRecordSizeInBytes()) { // includes the case where size is -1
					return false;
				}
			} else {
				return false;
			}

			try {
				final IMonitoringRecord record = recordFactory.create(BinaryValueDeserializer.create(buffer, this.stringRegistry));
				record.setLoggingTimestamp(loggingTimestamp);

				this.recordReceiver.newMonitoringRecord(record);
			} catch (final RecordInstantiationException ex) {
				this.logger.error("Failed to create: {}", recordClassName, ex);
				throw ex; // we cannot continue reading the buffer because we do not know at which position to continue
			}

			return true;
		} else {
			this.logger.error("Failed to identify a event type {}, no classname registered.", clazzId);
			throw new RecordInstantiationException("Cannot identify record class. Unknown id" + clazzId);
		}
	}

	public void register(final IMonitoringRecordReceiver recordReceiver) { // NOCS (hides field)
		this.recordReceiver = recordReceiver;
	}

}
