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

package kieker.monitoring.writernew;

import java.util.concurrent.BlockingQueue;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class MonitoringWriterThread extends Thread {

	private static final Log LOG = LogFactory.getLog(MonitoringWriterThread.class);

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
		if (LOG.isDebugEnabled()) {
			LOG.debug(this.getClass().getName() + " is running.");
		}

		this.writer.onStarting();

		try {
			IMonitoringRecord record = this.writerQueue.take();
			while (record != END_OF_MONITORING_RECORD) { // NOPMD (compare references by == not by equals())
				this.writer.writeMonitoringRecord(record);
				record = this.writerQueue.take();
			}
		} catch (final InterruptedException e) {
			if (LOG.isDebugEnabled()) {
				LOG.debug(this.getClass().getName() + " was interrupted.", e);
			}
			// do nothing; the thread terminates itself
		}

		this.writer.onTerminating();

		if (LOG.isDebugEnabled()) {
			LOG.debug(this.getClass().getName() + " has finished.");
		}
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
			LOG.warn("An exception occurred", e);
		}
	}

}
