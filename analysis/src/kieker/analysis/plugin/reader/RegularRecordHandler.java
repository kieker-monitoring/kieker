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

package kieker.analysis.plugin.reader;

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.BinaryValueDeserializer;
import kieker.common.registry.reader.ReaderRegistry;

/**
 * Runnable to handle incoming regular records.
 *
 * @author Holger Knoche
 *
 * @since 1.12
 * @deprecated since 1.15.1 old plugin api
 */
@Deprecated
public class RegularRecordHandler implements Runnable {

	/** Default queue size for the regular record queue. */
	private static final int DEFAULT_QUEUE_SIZE = 4096;

	private static final Logger LOGGER = LoggerFactory.getLogger(RegularRecordHandler.class);

	private final StringRegistryCache stringRegistryCache;
	private final CachedRecordFactoryCatalog cachedRecordFactoryCatalog = CachedRecordFactoryCatalog.getInstance();
	private final AbstractStringRegistryReaderPlugin reader;

	private final BlockingQueue<ByteBuffer> queue = new ArrayBlockingQueue<>(DEFAULT_QUEUE_SIZE);

	/**
	 * Creates a new regular record handler.
	 *
	 * @param reader
	 *            The reader to send the instantiated records to
	 * @param stringRegistryCache
	 *            The string registry cache to use
	 */
	public RegularRecordHandler(final AbstractStringRegistryReaderPlugin reader, final StringRegistryCache stringRegistryCache) {
		this.reader = reader;
		this.stringRegistryCache = stringRegistryCache;
	}

	@Override
	public void run() {
		while (true) {
			try {
				final ByteBuffer nextRecord = this.queue.take();

				this.readRegularRecord(nextRecord);
			} catch (final InterruptedException e) {
				LOGGER.error("Regular record handler was interrupted", e);
			}
		}
	}

	/**
	 * Enqueues an unparsed regular record for processing.
	 *
	 * @param buffer
	 *            The unparsed data in an appropriately positioned byte buffer
	 */
	public void enqueueRegularRecord(final ByteBuffer buffer) {
		try {
			this.queue.put(buffer);
		} catch (final InterruptedException e) {
			LOGGER.error("Record queue was interrupted", e);
		}
	}

	private ReaderRegistry<String> getStringRegistry(final long registryId) {
		return this.stringRegistryCache.getOrCreateRegistry(registryId);
	}

	private void readRegularRecord(final ByteBuffer buffer) {
		final long registryId = buffer.getLong();
		final int classId = buffer.getInt();
		final long loggingTimestamp = buffer.getLong();

		final ReaderRegistry<String> stringRegistry = this.getStringRegistry(registryId);

		try {
			final String recordClassName = stringRegistry.get(classId);
			final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.cachedRecordFactoryCatalog.get(recordClassName);
			final IMonitoringRecord record = recordFactory.create(BinaryValueDeserializer.create(buffer, stringRegistry));
			record.setLoggingTimestamp(loggingTimestamp);

			this.reader.deliverRecord(record);
		} catch (final RecordInstantiationException e) {
			LOGGER.error("Error instantiating record", e);
		}
	}

}
