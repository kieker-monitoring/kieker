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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
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
	private static final String SHUTDOWNDELAY = "MaxShutdownDelay";

	// internal variables
	protected final BlockingQueue<IMonitoringRecord> blockingQueue;
	private final List<AbstractAsyncThread> workers = new CopyOnWriteArrayList<AbstractAsyncThread>();
	private final int queueFullBehavior;
	private final int maxShutdownDelay;
	private final AtomicInteger missedRecords;

	protected AbstractAsyncWriter(final Configuration configuration) {
		super(configuration);
		final String prefix = this.getClass().getName() + "."; // NOCS (MultipleStringLiteralsCheck)

		final int queueFullBehaviorTmp = this.configuration.getIntProperty(prefix + AbstractAsyncWriter.BEHAVIOR);
		if ((queueFullBehaviorTmp < 0) || (queueFullBehaviorTmp > 2)) {
			AbstractAsyncWriter.LOG.warn("Unknown value '" + queueFullBehaviorTmp + "' for " + prefix + AbstractAsyncWriter.BEHAVIOR
					+ "; using default value 0");
			this.queueFullBehavior = 0;
		} else {
			this.queueFullBehavior = queueFullBehaviorTmp;
		}
		this.missedRecords = new AtomicInteger(0);
		this.blockingQueue = new ArrayBlockingQueue<IMonitoringRecord>(this.configuration.getIntProperty(prefix + AbstractAsyncWriter.QUEUESIZE));
		this.maxShutdownDelay = this.configuration.getIntProperty(prefix + AbstractAsyncWriter.SHUTDOWNDELAY);
	}

	/**
	 * Make sure that the three required properties always have default values!
	 */
	@Override
	protected Properties getDefaultProperties() {
		final Properties properties = new Properties(super.getDefaultProperties());
		final String prefix = this.getClass().getName() + "."; // can't use this.prefix, maybe uninitialized
		properties.setProperty(prefix + AbstractAsyncWriter.QUEUESIZE, "10000");
		properties.setProperty(prefix + AbstractAsyncWriter.BEHAVIOR, "0");
		properties.setProperty(prefix + AbstractAsyncWriter.SHUTDOWNDELAY, "0");
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

	/**
	 * The framework ensures, that this method is called only once!
	 */
	@Override
	public final void terminate() {
		final CountDownLatch cdl = new CountDownLatch(this.workers.size());
		// notify all workers
		for (final AbstractAsyncThread worker : this.workers) {
			worker.initShutdown(cdl);
		}
		boolean finished = true;
		for (final AbstractAsyncThread worker : this.workers) {
			finished &= worker.isFinished();
		}
		int shutdowntries = 0;
		while (!finished) {
			shutdowntries++;
			try {
				finished = cdl.await(500, TimeUnit.MILLISECONDS);
			} catch (final InterruptedException ex) {
				// we should be able to ignore an interrupted wait
			}
			if (!finished) {
				finished = true;
				for (final AbstractAsyncThread worker : this.workers) {
					finished &= worker.isFinished();
				}
			}
			if ((!finished) && ((this.maxShutdownDelay == 0) || (shutdowntries < this.maxShutdownDelay))) {
				AbstractAsyncWriter.LOG.info("shutdown delayed - Worker is busy ... waiting 0.5 seconds");
			} else {
				finished = true;
				break;
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
				if (!this.blockingQueue.offer(monitoringRecord) && ((this.missedRecords.getAndIncrement() % 1000) == 0)) { // NOCS
					AbstractAsyncWriter.LOG.warn("Queue is full, dropping records.");
				}
				break;
			default: // tries to add immediately (error if full)
				this.blockingQueue.add(monitoringRecord);
				break;
			}
		} catch (final Exception ex) { // NOCS (IllegalCatchCheck) // NOPMD
			AbstractAsyncWriter.LOG.error("Failed to retrieve new monitoring record.", ex);
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("\n\tRecords lost: ");
		sb.append(this.missedRecords.intValue());
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
