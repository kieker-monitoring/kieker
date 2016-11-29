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

package kieker.analysisteetime.plugin.reader.amqp;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.misc.RegistryRecord;
import kieker.common.util.registry.ILookup;

/**
 * Runnable to handle incoming registry records.
 *
 * @author Holger Knoche
 *
 * @since 1.12
 */
public class RegistryRecordHandler implements Runnable {

	/** The default queue size for the registry record queue */
	private static final int DEFAULT_QUEUE_SIZE = 1024;

	private static final Log LOG = LogFactory.getLog(RegistryRecordHandler.class);

	private final ILookup<String> stringRegistry;
	private final BlockingQueue<ByteBuffer> queue = new ArrayBlockingQueue<ByteBuffer>(DEFAULT_QUEUE_SIZE);

	/**
	 * Creates a new registry record handler for the given registry.
	 *
	 * @param stringRegistry
	 *            The string registry to operate on
	 */
	public RegistryRecordHandler(final ILookup<String> stringRegistry) {
		this.stringRegistry = stringRegistry;
	}

	@Override
	public void run() {
		while (true) {
			try {
				final ByteBuffer nextRecord = this.queue.take();

				this.readRegistryRecord(nextRecord);
			} catch (final InterruptedException e) {
				LOG.error("Registry record handler was interrupted", e);
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
			LOG.error("Record queue was interrupted", e);
		}
	}

	private void readRegistryRecord(final ByteBuffer buffer) {
		try {
			RegistryRecord.registerRecordInRegistry(buffer, this.stringRegistry);
		} catch (final BufferUnderflowException e) {
			LOG.error("Buffer underflow while reading registry record", e);
		}
	}

}
