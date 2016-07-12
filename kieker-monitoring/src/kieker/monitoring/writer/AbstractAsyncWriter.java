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

package kieker.monitoring.writer;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

/**
 * @author Jan Waller
 *
 * @since 1.3
 */
public abstract class AbstractAsyncWriter extends AbstractMonitoringWriter {

	/** The name of the configuration determining the size of the queue of this writer. */
	public static final String CONFIG_QUEUESIZE = "QueueSize";
	/** The name of the configuration determining the size of the prioritized queue of this writer. */
	public static final String CONFIG_PRIORITIZED_QUEUESIZE = "PrioritizedQueueSize";
	/** The name of the configuration determining the behavior of this writer in case of a full queue. */
	public static final String CONFIG_BEHAVIOR = "QueueFullBehavior";
	/** The name of the configuration determining the maximal shutdown delay of this writer (in milliseconds). */
	public static final String CONFIG_SHUTDOWNDELAY = "MaxShutdownDelay";

	private static final Log LOG = LogFactory.getLog(AbstractAsyncWriter.class);

	// internal variables
	/** The queue containing the records to be written. */
	protected final BlockingQueue<IMonitoringRecord> blockingQueue;
	/** The queue containing prioritized records (mostly {@link kieker.common.record.misc.RegistryRecord}) to be written. */
	protected final BlockingQueue<IMonitoringRecord> prioritizedBlockingQueue;
	private final List<AbstractAsyncThread> workers = new CopyOnWriteArrayList<AbstractAsyncThread>();
	private final int queueFullBehavior;
	private final int maxShutdownDelay;
	private final AtomicLong missedRecords;

	/**
	 * This constructor initializes the writer based on the given configuration.
	 *
	 * @param configuration
	 *            The configuration for this writer.
	 */
	protected AbstractAsyncWriter(final Configuration configuration) {
		super(configuration);
		final String prefix = this.getClass().getName() + ".";

		final int queueFullBehaviorTmp = configuration.getIntProperty(prefix + CONFIG_BEHAVIOR);
		if ((queueFullBehaviorTmp < 0) || (queueFullBehaviorTmp > 2)) {
			LOG.warn("Unknown value '" + queueFullBehaviorTmp + "' for " + prefix + CONFIG_BEHAVIOR + "; using default value 0");
			this.queueFullBehavior = 0;
		} else {
			this.queueFullBehavior = queueFullBehaviorTmp;
		}
		this.missedRecords = new AtomicLong(0);
		this.blockingQueue = new ArrayBlockingQueue<IMonitoringRecord>(configuration.getIntProperty(prefix + CONFIG_QUEUESIZE));
		this.prioritizedBlockingQueue = new ArrayBlockingQueue<IMonitoringRecord>(configuration.getIntProperty(prefix + CONFIG_PRIORITIZED_QUEUESIZE));
		this.maxShutdownDelay = configuration.getIntProperty(prefix + CONFIG_SHUTDOWNDELAY);
	}

	/**
	 * {@inheritDoc} Make sure that the three required properties always have default values!
	 */
	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration(super.getDefaultConfiguration());
		final String prefix = this.getClass().getName() + "."; // can't use this.prefix, maybe uninitialized
		configuration.setProperty(prefix + CONFIG_QUEUESIZE, "10000");
		configuration.setProperty(prefix + CONFIG_PRIORITIZED_QUEUESIZE, "100");
		configuration.setProperty(prefix + CONFIG_BEHAVIOR, "0");
		configuration.setProperty(prefix + CONFIG_SHUTDOWNDELAY, "-1");
		return configuration;
	}

	/**
	 * This method must be called at the end of the child constructor!
	 *
	 * @param worker
	 *            The new worker.
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		try {
			switch (this.queueFullBehavior) {
			case 1: // blocks when queue full
				for (int i = 0; i < 10; i++) { // drop out if more than 10 times interrupted
					try {
						this.blockingQueue.put(monitoringRecord);
						return true;
					} catch (final InterruptedException ignore) {
						// The interrupt status has been reset by the put method when throwing the exception.
						// We will not propagate the interrupt because the error is reported by returning false.
						LOG.warn("Interrupted when adding new monitoring record to queue. Try: " + i);
					}
				}
				LOG.error("Failed to add new monitoring record to queue (maximum number of attempts reached).");
				return false;
			case 2: // does nothing if queue is full
				if (!this.blockingQueue.offer(monitoringRecord)) {
					final long tmpMissedRecords = this.missedRecords.incrementAndGet();
					if (LOG.isWarnEnabled() && ((tmpMissedRecords % 1024) == 1)) {
						// warn upon the first failed element and upon all 1024th one
						LOG.warn("Queue is full, dropping records. Number of already dropped records: " + tmpMissedRecords);
					}
				}
				return true;
			default: // tries to add immediately (error if full)
				try {
					this.blockingQueue.add(monitoringRecord);
				} catch (final IllegalStateException ex) {
					LOG.error(
							"Failed to add new monitoring record to queue. Queue is full. Either increase 'QueueSize' or change 'QueueFullBehavior' for the configured writer."); // NOCS
					return false;
				}
				return true;
			}
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			LOG.error("Failed to add new monitoring record to queue.", ex);
			return false;
		}
	}

	@Override
	public boolean newMonitoringRecordNonBlocking(final IMonitoringRecord monitoringRecord) {
		try {
			if (!this.prioritizedBlockingQueue.offer(monitoringRecord)) {
				new Thread() {
					@Override
					public void run() {
						try {
							AbstractAsyncWriter.this.prioritizedBlockingQueue.put(monitoringRecord);
							return;
						} catch (final InterruptedException ignore) {
							Thread.currentThread().interrupt(); // propagate interrupt
						}
					}
				}.start();
			}
			return true;
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			LOG.error("Failed to add new monitoring record to queue.", ex);
			return false;
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(64);
		sb.append(super.toString());
		sb.append("\n\tRecords lost: ");
		sb.append(this.missedRecords.get());
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
