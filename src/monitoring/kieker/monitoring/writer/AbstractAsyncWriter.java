/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.Configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Jan Waller
 */
public abstract class AbstractAsyncWriter extends AbstractMonitoringWriter {
	private static final Log LOG = LogFactory.getLog(AbstractAsyncWriter.class);

	private static final String QUEUESIZE = "QueueSize";
	private static final String BEHAVIOR = "QueueFullBehavior";

	// internal variables
	private final String PREFIX;
	private final List<AbstractAsyncThread> workers = new Vector<AbstractAsyncThread>();
	protected final BlockingQueue<IMonitoringRecord> blockingQueue;
	private final int queueFullBehavior;
	private final AtomicInteger missedRecords;

	protected AbstractAsyncWriter(final Configuration configuration) {
		super(configuration);
		this.PREFIX = this.getClass().getName() + ".";

		final int queueFullBehavior = this.configuration.getIntProperty(this.PREFIX + AbstractAsyncWriter.BEHAVIOR);
		if ((queueFullBehavior < 0) || (queueFullBehavior > 2)) {
			AbstractAsyncWriter.LOG.warn("Unknown value '" + queueFullBehavior + "' for " + this.PREFIX + AbstractAsyncWriter.BEHAVIOR + "; using default value 0");
			this.queueFullBehavior = 0;
		} else {
			this.queueFullBehavior = queueFullBehavior;
		}
		this.missedRecords = new AtomicInteger(0);
		this.blockingQueue = new ArrayBlockingQueue<IMonitoringRecord>(this.configuration.getIntProperty(this.PREFIX + AbstractAsyncWriter.QUEUESIZE));
	}

	/**
	 * Make sure that the two required properties always have default values!
	 */
	@Override
	protected Properties getDefaultProperties() {
		final Properties properties = new Properties(super.getDefaultProperties());
		final String PREFIX = this.getClass().getName() + "."; //can't use this.PREFIX, maybe uninitialized
		properties.setProperty(PREFIX + AbstractAsyncWriter.QUEUESIZE, "10000");
		properties.setProperty(PREFIX + AbstractAsyncWriter.BEHAVIOR, "0");
		return properties;
	}

	/**
	 * This method must be called at the end of the child constructor!
	 * 
	 * @param worker
	 */
	protected final void addWorker(final AbstractAsyncThread worker) {
		this.workers.add(worker);
		worker.setDaemon(true); // might lead to inconsistent data due to harsh shutdown
		worker.start();
	}

	@Override
	public final void terminate() {
		// notify all workers
		for (final AbstractAsyncThread worker : this.workers) {
			worker.initShutdown();
		}
		// wait for all worker to finish
		for (final AbstractAsyncThread worker : this.workers) {
			while (!worker.isFinished()) {
				try {
					Thread.sleep(500);
				} catch (final InterruptedException ex) {
					// we should be able to ignore an interrupted wait
				}
				AbstractAsyncWriter.LOG.info("shutdown delayed - Worker is busy ... waiting additional 0.5 seconds");
				// TODO: we should be able to abort this, perhaps a max time of repeats?
				// See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/174
			}
		}
		AbstractAsyncWriter.LOG.info("Writer shutdown complete");
	}

	@Override
	public final boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		try {
			switch (this.queueFullBehavior) {
				case 1: // blocks when queue full
					this.blockingQueue.put(monitoringRecord);
					break;
				case 2: // does nothing if queue is full
					if (!this.blockingQueue.offer(monitoringRecord)) {
						// warn on missed records
						if (missedRecords.getAndIncrement() % 1000 == 0) {
							AbstractAsyncWriter.LOG.warn("Queue is full, dropping records.");
						}
					}
					break;
				default: // tries to add immediately (error if full)
					this.blockingQueue.add(monitoringRecord);
					break;
			}
		} catch (final Exception ex) {
			AbstractAsyncWriter.LOG.error("Failed to retrieve new monitoring record.", ex);
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("\n\tRecords lost (");
		sb.append(this.missedRecords.intValue());
		sb.append("): ");
		sb.append("\n\tWriter Threads (");
		sb.append(this.workers.size());
		sb.append("): ");
		for (final AbstractAsyncThread worker : this.workers) {
			sb.append("\n\t\t");
			sb.append(worker.toString());
		}
		return sb.toString();
	}
}
