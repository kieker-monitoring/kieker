/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

/**
 * @author Jan Waller
 */
public abstract class AbstractAsyncWriter extends AbstractMonitoringWriter {

	public static final String CONFIG_QUEUESIZE = "QueueSize";
	public static final String CONFIG_BEHAVIOR = "QueueFullBehavior";
	public static final String CONFIG_SHUTDOWNDELAY = "MaxShutdownDelay";

	private static final Log LOG = LogFactory.getLog(AbstractAsyncWriter.class);

	// internal variables
	protected final BlockingQueue<IMonitoringRecord> blockingQueue;
	private final List<AbstractAsyncThread> workers = new CopyOnWriteArrayList<AbstractAsyncThread>();
	private final int queueFullBehavior;
	private final int maxShutdownDelay;
	private final AtomicInteger missedRecords;

	protected AbstractAsyncWriter(final Configuration configuration) {
		super(configuration);
		final String prefix = this.getClass().getName() + ".";

		final int queueFullBehaviorTmp = this.configuration.getIntProperty(prefix + CONFIG_BEHAVIOR);
		if ((queueFullBehaviorTmp < 0) || (queueFullBehaviorTmp > 2)) {
			LOG.warn("Unknown value '" + queueFullBehaviorTmp + "' for " + prefix + CONFIG_BEHAVIOR + "; using default value 0");
			this.queueFullBehavior = 0;
		} else {
			this.queueFullBehavior = queueFullBehaviorTmp;
		}
		this.missedRecords = new AtomicInteger(0);
		this.blockingQueue = new ArrayBlockingQueue<IMonitoringRecord>(this.configuration.getIntProperty(prefix + CONFIG_QUEUESIZE));
		this.maxShutdownDelay = this.configuration.getIntProperty(prefix + CONFIG_SHUTDOWNDELAY);
	}

	/**
	 * Make sure that the three required properties always have default values!
	 */

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration(super.getDefaultConfiguration());
		final String prefix = this.getClass().getName() + "."; // can't use this.prefix, maybe uninitialized
		configuration.setProperty(prefix + CONFIG_QUEUESIZE, "10000");
		configuration.setProperty(prefix + CONFIG_BEHAVIOR, "0");
		configuration.setProperty(prefix + CONFIG_SHUTDOWNDELAY, "-1");
		return configuration;
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

	public final void terminate() {
		final CountDownLatch cdl = new CountDownLatch(this.workers.size());
		for (final AbstractAsyncThread worker : this.workers) {
			worker.initShutdown(cdl); // notify all workers
		}
		boolean finished = false;
		try {
			if (this.maxShutdownDelay > -1) {
				LOG.info("Shutting down writers, waiting at most " + this.maxShutdownDelay + " milliseconds.");
				finished = cdl.await(this.maxShutdownDelay, TimeUnit.MILLISECONDS);
			} else {
				LOG.info("Shutting down writers.");
				cdl.await();
				finished = true;
			}
		} catch (final InterruptedException ex) {
			// we should be able to ignore an interrupted wait (finished is still false)
		}
		if (finished) {
			LOG.info("Writer shutdown complete.");
		} else {
			LOG.info("Writer shutdown incomplete, " + cdl.getCount() + " worker(s) halted.");
		}
	}

	public final boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		try {
			switch (this.queueFullBehavior) {
			case 1: // blocks when queue full
				this.blockingQueue.put(monitoringRecord);
				break;
			case 2: // does nothing if queue is full
				if (!this.blockingQueue.offer(monitoringRecord) && ((this.missedRecords.getAndIncrement() % 1000) == 0)) { // NOCS
					LOG.warn("Queue is full, dropping records.");
				}
				break;
			default: // tries to add immediately (error if full)
				this.blockingQueue.add(monitoringRecord);
				break;
			}
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			LOG.error("Failed to retrieve new monitoring record.", ex);
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(64);
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
