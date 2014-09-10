/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
import java.util.concurrent.CountDownLatch;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.core.controller.IMonitoringController;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.3
 */
public abstract class AbstractAsyncThread extends Thread {
	private static final Log LOG = LogFactory.getLog(AbstractAsyncThread.class);
	private static final IMonitoringRecord END_OF_MONITORING_MARKER = new EmptyRecord();
	/** The monitoring controller for the current monitoring session. */
	protected final IMonitoringController monitoringController;
	private final BlockingQueue<IMonitoringRecord> writeQueue;
	private boolean finished; // only accessed in synchronized blocks
	private CountDownLatch shutdownLatch; // only accessed in synchronized blocks

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param monitoringController
	 *            The monitoring controller for the current monitoring session.
	 * @param writeQueue
	 *            The queue containing the records (and the potential {@code end of monitoring} marker).
	 */
	public AbstractAsyncThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue) {
		this.writeQueue = writeQueue;
		this.monitoringController = monitoringController;
	}

	/**
	 * Initializes the shutdown of this thread if necessary. The method returns immediately in every case and counts the latch down eventually (assuming that no
	 * exception occurs).
	 * 
	 * @param cdl
	 *            The latch which will be count down once the thread has been terminated.
	 */
	public final void initShutdown(final CountDownLatch cdl) {
		synchronized (this) {
			this.shutdownLatch = cdl;
			if (this.finished) {
				cdl.countDown();
			}
		}
		try {
			this.writeQueue.put(END_OF_MONITORING_MARKER);
		} catch (final InterruptedException ex) {
			LOG.error("Error while trying to stop writer thread", ex);
		}
	}

	/**
	 * Can be used to determine whether the thread is in the finished state or not.
	 * 
	 * @return true iff the thread has set the finished flag.
	 */
	public final boolean isFinished() {
		synchronized (this) { // may not be necessary, but doesn't really hurt here
			return this.finished;
		}
	}

	@Override
	public final void run() {
		if (LOG.isDebugEnabled()) {
			LOG.debug(this.getClass().getName() + " running");
		}
		try {
			// making it a local variable for faster access
			final BlockingQueue<IMonitoringRecord> writeQueueLocal = this.writeQueue;
			while (true) {
				try {
					IMonitoringRecord monitoringRecord = writeQueueLocal.take();
					if (monitoringRecord == END_OF_MONITORING_MARKER) { // NOPMD (CompareObjectsWithEquals
						if (LOG.isDebugEnabled()) {
							LOG.debug("Terminating writer thread, " + writeQueueLocal.size() + " entries remaining");
						}
						monitoringRecord = writeQueueLocal.poll();
						while (monitoringRecord != null) {
							if (monitoringRecord != END_OF_MONITORING_MARKER) { // NOPMD (CompareObjectsWithEquals
								this.consume(monitoringRecord);
							}
							monitoringRecord = writeQueueLocal.poll();
						}
						this.writeQueue.put(END_OF_MONITORING_MARKER);
						this.cleanup();
						synchronized (this) {
							if (!this.finished && (this.shutdownLatch != null)) {
								this.shutdownLatch.countDown();
							}
							this.finished = true;
						}
						break; // while
					} else {
						this.consume(monitoringRecord);
					}
				} catch (final InterruptedException ex) {
					continue; // while
					// would be another method to finish the execution
					// but normally we should be able to continue
				}
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("Writer thread finished");
			}
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			// e.g. IOException
			LOG.error("Writer thread will halt", ex);
			this.cleanup();
			synchronized (this) {
				if (!this.finished && (this.shutdownLatch != null)) {
					this.shutdownLatch.countDown();
				}
				this.finished = true;
			}
			this.monitoringController.terminateMonitoring();
		}
	}

	/**
	 * Returns a human-readable information string about the writer's configuration and state.
	 * 
	 * @return the information string.
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Finished: '");
		sb.append(this.isFinished());
		sb.append('\'');
		return sb.toString();
	}

	/**
	 * Inheriting classes should implement this method to consume the given record by, for example, sending it to a JMS client or by writing it to the file sysytem.
	 * 
	 * @param monitoringRecord
	 *            The record to consume.
	 * 
	 * @throws Exception
	 *             Indicates that something went wrong during the consumption.
	 */
	protected abstract void consume(final IMonitoringRecord monitoringRecord) throws Exception;

	/**
	 * Inheriting classes should implement this method to do some cleanup work like, for example, closing open connections.
	 */
	protected abstract void cleanup();
}
