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

package kieker.analysis.tt.reader.filesystem.format.binary.file;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.tt.reader.filesystem.className.ClassNameRegistryRepository;
import kieker.common.exception.MonitoringRecordException;
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
 * This is a record from file creator, hacked to replace the broken Kieker version.
 *
 * @author Christian Wulf -- initial contributor
 * @author Reiner Jung
 *
 * @since 1.10
 *
 * @deprecated since 1.15 removed 1.16
 */
@Deprecated
public class RecordFromBinaryFileCreator {

	private static final Logger LOGGER = LoggerFactory.getLogger(RecordFromBinaryFileCreator.class);

	private static final int LONG_BYTES = AbstractMonitoringRecord.TYPE_SIZE_LONG;

	private final ClassNameRegistryRepository classNameRegistryRepository;

	private final CachedRecordFactoryCatalog recordFactories = CachedRecordFactoryCatalog.getInstance();

	private final ByteBuffer buffer;

	/**
	 * Create a new record from binary file creator.
	 *
	 * @param classNameRegistryRepository
	 *            class and string registry
	 */
	public RecordFromBinaryFileCreator(final ClassNameRegistryRepository classNameRegistryRepository) {
		this.classNameRegistryRepository = classNameRegistryRepository;
		this.buffer = ByteBuffer.allocate(1024000);
	}

	/**
	 * Create records from binary files.
	 *
	 * @param binaryFile
	 *            binary file input, only used for logging info
	 * @param inputStream
	 *            data stream
	 * @param outputPort
	 *            filter output port
	 * @throws IOException
	 *             on io errors during reading
	 * @throws MonitoringRecordException
	 *             on deserialization issues
	 */
	public void createRecordsFromBinaryFile(final File binaryFile, final DataInputStream inputStream,
			final OutputPort<IMonitoringRecord> outputPort) throws IOException, MonitoringRecordException {
		RecordFromBinaryFileCreator.LOGGER.info("reading file {}", binaryFile.getAbsolutePath());

		final ReaderRegistry<String> registry = this.classNameRegistryRepository.get(binaryFile.getParentFile());

		final BinaryValueDeserializer deserializer = BinaryValueDeserializer.create(this.buffer, registry);

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

			this.processBuffer(registry, deserializer, outputPort);

			if (endOfStreamReached) {
				inputStream.close();
			}
		}

	}

	private void processBuffer(final ReaderRegistry<String> registry, final IValueDeserializer deserializer,
			final OutputPort<IMonitoringRecord> outputPort) throws IOException {
		this.buffer.flip();

		try {
			/** Needs at least an record id. */
			while ((this.buffer.position() + 4) <= this.buffer.limit()) {
				this.buffer.mark();
				final IMonitoringRecord record = this.deserializeRecord(registry, deserializer);
				if (record == null) {
					return;
				} else {
					outputPort.send(record);
				}
			}
			this.buffer.mark();
			this.buffer.compact();
		} catch (final BufferUnderflowException ex) {
			RecordFromBinaryFileCreator.LOGGER.warn("Unexpected buffer underflow. Resetting and compacting buffer.", ex);
			this.buffer.reset();
			this.buffer.compact();
			throw ex;
		}
	}

	private IMonitoringRecord deserializeRecord(final ReaderRegistry<String> registry, final IValueDeserializer deserializer)
			throws IOException {
		final int clazzId = this.buffer.getInt();
		final String recordClassName = registry.get(clazzId);

		if (recordClassName == null) {
			RecordFromBinaryFileCreator.LOGGER.error("Missing classname mapping for record type id '{}'",
					clazzId);
			return null; // we can't easily recover on errors
		}

		// identify logging timestamp
		if (this.buffer.remaining() < RecordFromBinaryFileCreator.LONG_BYTES) {
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
					RecordFromBinaryFileCreator.LOGGER.warn("Failed to create: {} error {}", recordClassName, ex);
					// incomplete record, move back
					this.buffer.reset();
					this.buffer.compact();
					return null;
				} catch (final BufferUnderflowException ex) {
					RecordFromBinaryFileCreator.LOGGER.warn("Failed to create: {} error {}", recordClassName, ex);
					// incomplete record, move back
					this.buffer.reset();
					this.buffer.compact();
					return null;

				}
			}
		}
	}

}
