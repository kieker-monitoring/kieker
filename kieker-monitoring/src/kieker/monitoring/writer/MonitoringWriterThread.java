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

package kieker.monitoring.writer;

import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class MonitoringWriterThread extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringWriterThread.class);

	private static final IMonitoringRecord END_OF_MONITORING_RECORD = new EmptyRecord();

	private final BlockingQueue<IMonitoringRecord> writerQueue;
	private final AbstractMonitoringWriter writer;

	// private int numWrittenRecords;

	public MonitoringWriterThread(final AbstractMonitoringWriter writer, final BlockingQueue<IMonitoringRecord> writerQueue) {
		if (writer == null) {
			throw new NullPointerException("The given writer may not be null.");
		}
		if (writerQueue == null) {
			throw new NullPointerException("The given writerQueue may not be null.");
		}
		this.writer = writer;
		this.writerQueue = writerQueue;
		// All Kieker threads must be daemon threads. Otherwise the monitored application can never terminate.
		this.setDaemon(true);
	}

	@Override
	public void run() {
		LOGGER.debug("{} is running.", this.getClass().getName());

		this.writer.onStarting();

		try {
			IMonitoringRecord record = this.writerQueue.take();
			while (record != END_OF_MONITORING_RECORD) { // NOPMD (compare references by == not by equals())
				this.writer.writeMonitoringRecord(record);
				record = this.writerQueue.take();
			}
		} catch (final InterruptedException e) {
			LOGGER.debug("{} was interrupted.", this.getClass().getName(), e);
			// do nothing; the thread terminates itself
		}

		this.writer.onTerminating();

		LOGGER.debug("{} has finished.", this.getClass().getName());
	}

	/**
	 * Initiates the termination of this thread.
	 */
	public void terminate() {
		// Do not call interrupt() to indicate thread termination.
		// It interrupts writes to (socket) channels.
		// Instead, we use a unique end-of-monitoring token to indicate that the thread should terminate itself.
		try {
			// apply "blocking wait" since we must ensure that this EOF record is inserted to terminate the thread
			this.writerQueue.put(END_OF_MONITORING_RECORD);
		} catch (final InterruptedException e) {
			LOGGER.warn("An exception occurred", e);
		}
	}

}
