/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.record.misc.RegistryRecord;
import kieker.common.registry.reader.ReaderRegistry;

/**
 * Runnable to handle incoming registry records.
 *
 * @author holger
 *
 * @since 1.12
 */
public class RegistryRecordHandler implements Runnable {

	/** The default queue size for the registry record queue. */
	private static final int DEFAULT_QUEUE_SIZE = 1024;

	private static final Logger LOGGER = LoggerFactory.getLogger(RegistryRecordHandler.class);

	private final StringRegistryCache stringRegistryCache;
	private final BlockingQueue<ByteBuffer> queue = new ArrayBlockingQueue<>(DEFAULT_QUEUE_SIZE);

	/**
	 * Creates a new registry record handler for the given registry.
	 *
	 * @param stringRegistryCache
	 *            The string registry cache to operate on
	 */
	public RegistryRecordHandler(final StringRegistryCache stringRegistryCache) {
		this.stringRegistryCache = stringRegistryCache;
	}

	@Override
	public void run() {
		while (true) {
			try {
				final ByteBuffer nextRecord = this.queue.take();

				this.readRegistryRecord(nextRecord);
			} catch (final InterruptedException e) {
				LOGGER.error("Registry record handler was interrupted", e);
			}
		}
	}

	/**
	 * Enqueues an unparsed registry record for processing.
	 *
	 * @param buffer
	 *            The unparsed data in an appropriately positioned byte buffer
	 */
	public void enqueueRegistryRecord(final ByteBuffer buffer) {
		try {
			this.queue.put(buffer);
		} catch (final InterruptedException e) {
			LOGGER.error("Record queue was interrupted", e);
		}
	}

	private ReaderRegistry<String> getStringRegistry(final long registryId) {
		return this.stringRegistryCache.getOrCreateRegistry(registryId);
	}

	private void readRegistryRecord(final ByteBuffer buffer) {
		try {
			final long registryId = buffer.getLong();
			final ReaderRegistry<String> stringRegistry = this.getStringRegistry(registryId);

			RegistryRecord.registerRecordInRegistry(buffer, stringRegistry);
		} catch (final BufferUnderflowException e) {
			LOGGER.error("Buffer underflow while reading registry record", e);
		}
	}

}
