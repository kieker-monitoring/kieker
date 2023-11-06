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
package kieker.analysis.source.file;

import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.BinaryValueDeserializer;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.registry.reader.ReaderRegistry;

import teetime.framework.OutputPort;

/**
 * This deserializer reads a binary input stream and deserializes them into IMonitoringRecords.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class BinaryEventDeserializer extends AbstractEventDeserializer {

	public static final String BUFFER_SIZE = BinaryEventDeserializer.class.getCanonicalName() + ".bufferSize";

	public static final int DEFAULT_BUFFER_SIZE = 102400;

	private static final Logger LOGGER = LoggerFactory.getLogger(BinaryEventDeserializer.class);

	private static final int LONG_BYTES = AbstractMonitoringRecord.TYPE_SIZE_LONG;

	private final CachedRecordFactoryCatalog recordFactories = CachedRecordFactoryCatalog.getInstance();

	private final ByteBuffer buffer;

	public BinaryEventDeserializer(final Integer bufferSize, final ReaderRegistry<String> registry) {
		super(registry);
		this.buffer = ByteBuffer.allocate(bufferSize == null ? DEFAULT_BUFFER_SIZE : bufferSize); // NOCS
	}

	@Override
	public void processDataStream(final InputStream chainInputStream, final OutputPort<IMonitoringRecord> outputPort) throws IOException {
		final BinaryValueDeserializer deserializer = BinaryValueDeserializer.create(this.buffer, this.registry);

		boolean endOfStreamReached = false;
		while (!endOfStreamReached) {
			byte[] bytes = this.buffer.array();
			int bytesRead = chainInputStream.read(bytes, this.buffer.position(), this.buffer.remaining());
			this.buffer.position(this.buffer.position() + bytesRead);
			while (bytesRead > 0) {
				bytes = this.buffer.array();
				bytesRead = chainInputStream.read(bytes, this.buffer.position(), this.buffer.remaining());
				if (bytesRead >= 0) {
					this.buffer.position(this.buffer.position() + bytesRead);
				}
			}
			if (bytesRead == -1) {
				endOfStreamReached = true;
			}

			this.processBuffer(deserializer, outputPort);

			if (endOfStreamReached) {
				chainInputStream.close();
			}
		}
	}

	private void processBuffer(final IValueDeserializer deserializer,
			final OutputPort<IMonitoringRecord> outputPort) throws IOException {
		this.buffer.flip();

		try {
			/** Needs at least an record id. */
			while ((this.buffer.position() + 4) <= this.buffer.limit()) {
				this.buffer.mark();
				final IMonitoringRecord record = this.deserializeRecord(deserializer);
				if (record == null) {
					return;
				} else {
					outputPort.send(record);
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
		final String eventTypeName = this.registry.get(clazzId);

		if (eventTypeName == null) {
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
			final IRecordFactory<? extends IMonitoringRecord> eventTypeFactory = this.recordFactories.get(eventTypeName);
			if (eventTypeFactory == null) {
				LOGGER.error("Class type {} was not found. Cannot instantiate event type.", eventTypeName);
				this.buffer.reset();
				this.buffer.compact();
				return null;
			} else if (this.buffer.remaining() < eventTypeFactory.getRecordSizeInBytes()) {
				// incomplete record, move back
				this.buffer.reset();
				this.buffer.compact();
				return null;
			} else {
				try {
					final IMonitoringRecord record = eventTypeFactory.create(deserializer);
					record.setLoggingTimestamp(loggingTimestamp);
					return record;
				} catch (final RecordInstantiationException ex) { // This happens when dynamic
					// arrays are used and the buffer
					// does not hold the complete
					// record.
					LOGGER.warn("Buffer too small to hold complete event: {} error {}", eventTypeName, ex);
					// incomplete record, move back
					this.buffer.reset();
					this.buffer.compact();
					return null;
				} catch (final BufferUnderflowException ex) {
					LOGGER.warn("Received event is incomplete: {} error {}", eventTypeName, ex);
					// incomplete record, move back
					this.buffer.reset();
					this.buffer.compact();
					return null;
				}
			}
		}
	}

}
