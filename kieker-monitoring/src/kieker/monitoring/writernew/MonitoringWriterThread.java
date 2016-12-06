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

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class MonitoringWriterThread extends Thread {

	private static final Log LOG = LogFactory.getLog(MonitoringWriterThread.class);

	private volatile boolean shouldTerminate;
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
			while (!this.shouldTerminate) {
				final IMonitoringRecord record = this.writerQueue.take();
				this.writer.writeMonitoringRecord(record);
				// this.numWrittenRecords++;
				// if ((this.numWrittenRecords % 1000) == 0) {
				// System.out.println(this.getClass().getCanonicalName() + ": numWrittenRecords = " + this.numWrittenRecords);
				// }
			}

			// shouldTerminate=true: write out all possibly remaining records from the queue
			IMonitoringRecord record;
			while ((record = this.writerQueue.poll()) != null) {
				this.writer.writeMonitoringRecord(record);
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

	public void terminate() {
		this.shouldTerminate = true;
		this.interrupt();
	}

}
