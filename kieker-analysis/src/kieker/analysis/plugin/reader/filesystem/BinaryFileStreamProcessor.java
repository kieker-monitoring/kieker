/***************************************************************************
 * Copyright 2018 Kieker Project (https://kieker-monitoring.net)
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
package kieker.analysis.plugin.reader.filesystem;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.plugin.reader.util.IMonitoringRecordReceiver;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.BinaryValueDeserializer;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.registry.reader.ReaderRegistry;

/**
 * Process an binary stream and produce IMonitoringRecords.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class BinaryFileStreamProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(BinaryFileStreamProcessor.class);

	private static final int LONG_BYTES = AbstractMonitoringRecord.TYPE_SIZE_LONG;

	private final CachedRecordFactoryCatalog recordFactories = CachedRecordFactoryCatalog.getInstance();

	private final ByteBuffer buffer;

	private final ReaderRegistry<String> stringRegistry;

	private final IMonitoringRecordReceiver recordReceiver;

	public BinaryFileStreamProcessor(final ReaderRegistry<String> stringRegistry, final IMonitoringRecordReceiver recordReceiver) {
		this.stringRegistry = stringRegistry;
		this.buffer = ByteBuffer.allocate(1024000);
		this.recordReceiver = recordReceiver;
	}

	/**
	 * Create records from binary files.
	 *
	 * @param inputStream
	 *            data stream
	 * @param outputPort
	 *            filter output port
	 * @throws IOException
	 *             on io errors during reading
	 * @throws MonitoringRecordException
	 *             on deserialization issues
	 */
	public void createRecordsFromBinaryFile(final DataInputStream inputStream) throws IOException, MonitoringRecordException {
		final BinaryValueDeserializer deserializer = BinaryValueDeserializer.create(this.buffer, this.stringRegistry);

		boolean endOfStreamReached = false;
		while (!endOfStreamReached) {
			byte[] bytes = this.buffer.array();
			int bytesRead = inputStream.read(bytes, this.buffer.position(), this.buffer.remaining());
			this.buffer.position(this.buffer.position() + bytesRead);
			while (bytesRead > 0) {
				bytes = this.buffer.array();
				bytesRead = inputStream.read(bytes, this.buffer.position(), this.buffer.remaining());
				if (bytesRead >= 0) {
					this.buffer.position(this.buffer.position() + bytesRead);
				}
			}
			if (bytesRead == -1) {
				endOfStreamReached = true;
			}

			this.processBuffer(deserializer);

			if (endOfStreamReached) {
				inputStream.close();
			}
		}

	}

	private void processBuffer(final IValueDeserializer deserializer) throws IOException {
		this.buffer.flip();

		try {
			/** Needs at least an record id. */
			while ((this.buffer.position() + 4) <= this.buffer.limit()) {
				this.buffer.mark();
				final IMonitoringRecord record = this.deserializeRecord(deserializer);
				if (record == null) {
					return;
				} else {
					this.recordReceiver.newMonitoringRecord(record);
				}
			}
			this.buffer.mark();
			this.buffer.compact();
		} catch (final BufferUnderflowException ex) {
			LOGGER.warn("Unexpected buffer underflow. Resetting and compacting buffer.", ex);
			this.buffer.reset();
			this.buffer.compact();
			throw ex;
		}
	}

	private IMonitoringRecord deserializeRecord(final IValueDeserializer deserializer)
			throws IOException {
		final int clazzId = this.buffer.getInt();
		final String recordClassName = this.stringRegistry.get(clazzId);

		if (recordClassName == null) {
			LOGGER.error("Missing classname mapping for record type id '{}'",
					clazzId);
			return null; // we can't easily recover on errors
		}

		// identify logging timestamp
		if (this.buffer.remaining() < LONG_BYTES) {
			// incomplete record, move back
			this.buffer.reset();
			this.buffer.compact();
			return null;
		} else {
			final long loggingTimestamp = this.buffer.getLong();

			// identify record data
			final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactories.get(recordClassName);
			if (this.buffer.remaining() < recordFactory.getRecordSizeInBytes()) {
				// incomplete record, move back
				this.buffer.reset();
				this.buffer.compact();
				return null;
			} else {
				try {
					final IMonitoringRecord record = recordFactory.create(deserializer);
					record.setLoggingTimestamp(loggingTimestamp);
					return record;
				} catch (final RecordInstantiationException ex) { // This happens when dynamic
					// arrays are used and the buffer
					// does not hold the complete
					// record.
					LOGGER.warn("Failed to create: {} error {}", recordClassName, ex);
					// incomplete record, move back
					this.buffer.reset();
					this.buffer.compact();
					return null;
				} catch (final BufferUnderflowException ex) {
					LOGGER.warn("Failed to create: {} error {}", recordClassName, ex);
					// incomplete record, move back
					this.buffer.reset();
					this.buffer.compact();
					return null;

				}
			}
		}
	}
}
