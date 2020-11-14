/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysisteetime.plugin.reader.IRecordReceivedListener;
import kieker.analysisteetime.plugin.reader.RecordDeserializer;
import kieker.common.record.IMonitoringRecord;
import kieker.common.registry.reader.ReaderRegistry;

/**
 * Runnable to handle incoming regular records.
 *
 * @author Holger Knoche
 *
 * @since 1.12
 * @deprecated 1.15 has been moved to final location kieker.analysis.source.amqp
 */
@Deprecated
public class RegularRecordHandler implements Runnable, IRecordReceivedListener {

	/** Default queue size for the regular record queue. */
	private static final int DEFAULT_QUEUE_SIZE = 4096;
	/** The logger of this handler. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RegularRecordHandler.class);

	private final AMQPReader readerLogic;
	private final RecordDeserializer recordDeserializer;

	private final BlockingQueue<ByteBuffer> queue = new ArrayBlockingQueue<>(RegularRecordHandler.DEFAULT_QUEUE_SIZE);

	/**
	 * Creates a new regular record handler.
	 *
	 * @param readerLogic
	 *            The reader logic class to send the instantiated records
	 *            to
	 * @param stringRegistry
	 *            The string registry to use
	 */
	public RegularRecordHandler(final AMQPReader readerLogic, final ReaderRegistry<String> stringRegistry) {
		this.readerLogic = readerLogic;
		this.recordDeserializer = new RecordDeserializer(this, stringRegistry);
	}

	@Override
	public void run() {
		while (true) {
			try {
				final ByteBuffer nextRecord = this.queue.take();

				final int classId = nextRecord.getInt();

				this.recordDeserializer.deserializeRecord(classId, nextRecord);
			} catch (final InterruptedException e) {
				RegularRecordHandler.LOGGER.error("Regular record handler was interrupted", e);
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
			RegularRecordHandler.LOGGER.error("Record queue was interrupted", e);
		}
	}

	@Override
	public void onRecordReceived(final IMonitoringRecord record) {
		this.readerLogic.deliverRecord(record);
	}

}
