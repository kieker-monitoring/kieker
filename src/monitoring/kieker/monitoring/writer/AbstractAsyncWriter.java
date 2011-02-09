package kieker.monitoring.writer;

import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.IMonitoringController;
import kieker.monitoring.core.configuration.Configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */
/**
 * @author Jan Waller
 */
public abstract class AbstractAsyncWriter extends AbstractMonitoringWriter {
	private static final Log log = LogFactory.getLog(AbstractAsyncWriter.class);

	private static final String QUEUESIZE = "QueueSize";
	private static final String BEHAVIOR = "QueueFullBehavior";
	
	// internal variables
	private final String PREFIX;
	protected final Vector<AbstractAsyncThread> workers = new Vector<AbstractAsyncThread>();
	protected final BlockingQueue<IMonitoringRecord> blockingQueue;
	private final int queueFullBehavior;
	
	protected AbstractAsyncWriter(final IMonitoringController ctrl, final Configuration configuration) {
		super(ctrl, configuration);
		this.PREFIX = this.getClass().getName() + ".";
		
		final int queueFullBehavior = this.configuration.getIntProperty(PREFIX + BEHAVIOR);
		if ((queueFullBehavior < 0) || (queueFullBehavior > 2)) {
			AbstractAsyncWriter.log.warn("Unknown value '" + queueFullBehavior + "' for " + PREFIX + BEHAVIOR + "; using default value 0");
			this.queueFullBehavior = 0;
		} else {
			this.queueFullBehavior = queueFullBehavior;
		}
		this.blockingQueue = new ArrayBlockingQueue<IMonitoringRecord>(this.configuration.getIntProperty(PREFIX + QUEUESIZE));
	}
	
	/**
	 * Make sure that the two required properties always have default values!
	 */
	@Override
	protected Properties getDefaultProperties() {
		final Properties properties = new Properties(super.getDefaultProperties());
		final String PREFIX = this.getClass().getName() + ".";
		properties.setProperty(PREFIX + QUEUESIZE, "10000");
		properties.setProperty(PREFIX + BEHAVIOR, "0");
		return properties;
	}
	
	/**
	 * This method must be called at the end of the child constructor!
	 * 
	 * @param worker
	 */
	protected final void addWorker(AbstractAsyncThread worker) {
		this.workers.add(worker);
		worker.setDaemon(true); // might lead to inconsistent data due to harsh shutdown
		worker.start();
	}

	@Override
	public final void terminate() {
		// notify all workers
		for (AbstractAsyncThread worker : this.workers) {
			worker.initShutdown();
		}
		// wait for all worker to finish
		for (AbstractAsyncThread worker : this.workers) {
			while (!worker.isFinished()) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException ex) {
					// we should be able to ignore an interrupted wait
				}
				AbstractAsyncWriter.log.info("shutdown delayed - Worker is busy ... waiting additional 0.5 seconds");
				//TODO: we should be able to abort this, perhaps a max time of repeats?
			}
		}
		AbstractAsyncWriter.log.info("Writer shutdown complete");
	}

	@Override
	public final boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		try {
			switch (this.queueFullBehavior) {
			case 1: // blocks when queue full
				this.blockingQueue.put(monitoringRecord);
				break;
			case 2: // does nothing if queue is full
				this.blockingQueue.offer(monitoringRecord);
				break;
			default: // tries to add immediately (error if full)
				this.blockingQueue.add(monitoringRecord);
				break;
			}
		} catch (final Exception ex) {
			AbstractAsyncWriter.log.error("Failed to retrieve new monitoring record." + ex);
			return false;
		}
		return true;
	}
	
	@Override
	public String getInfoString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.getInfoString());
		sb.append("\n\tWriter Threads (");
		sb.append(this.workers.size());
		sb.append("): ");
		for (AbstractAsyncThread worker : this.workers) {
			sb.append("\n\t\t");
			sb.append(worker.getInfoString());
		}
		return sb.toString();
	}
}
