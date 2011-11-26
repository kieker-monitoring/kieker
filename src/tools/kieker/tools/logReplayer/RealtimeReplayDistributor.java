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

package kieker.tools.logReplayer;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import kieker.analysis.configuration.Configuration;
import kieker.analysis.exception.MonitoringRecordConsumerException;
import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.port.AbstractInputPort;
import kieker.analysis.plugin.port.OutputPort;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.timer.DefaultSystemTimer;
import kieker.monitoring.timer.ITimeSource;

/**
 * IMonitoringRecordConsumerPlugin that distributes the log records to the worker
 * thread for "real time" replays.<br>
 * 
 * This class has exactly one input port named "in" and one output ports named
 * "out".<br>
 * 
 * TODO: Currently this class <b>can not</b> be used for the later analysis
 * tool, as the objects for the constructor cannot be configured with a
 * configuration object.
 * 
 * @author Robert von Massow
 * 
 */
public class RealtimeReplayDistributor extends AbstractAnalysisPlugin {
	private static final Log LOG = LogFactory.getLog(RealtimeReplayDistributor.class);

	private static final ITimeSource TIMESOURCE = DefaultSystemTimer.getInstance();
	private static final int QUEUE_SIZE_FACTOR = 1000;
	private static final int MILLISECOND = 1000 * 1000;
	private static final int REPLAY_OFFSET = 2 * 1000 * RealtimeReplayDistributor.MILLISECOND;

	private final int numWorkers;
	private final AbstractAnalysisPlugin cons;
	private volatile long startTime = -1;
	private volatile long offset = -1;
	private volatile long firstLoggingTimestamp;
	private final ScheduledThreadPoolExecutor executor;
	private long lTime;
	private volatile int active;
	private final int maxQueueSize;
	private final CountDownLatch terminationLatch;

	private final OutputPort output = new OutputPort("out", Collections.unmodifiableCollection(new CopyOnWriteArrayList<Class<?>>(
			new Class<?>[] { IMonitoringRecord.class })));
	private final AbstractInputPort input = new AbstractInputPort("in", Collections.unmodifiableCollection(new CopyOnWriteArrayList<Class<?>>(
			new Class<?>[] { IMonitoringRecord.class }))) {
		@Override
		public void newEvent(final Object event) {
			RealtimeReplayDistributor.this.newMonitoringRecord((IMonitoringRecord) event);

			RealtimeReplayDistributor.this.output.deliver(event);
		}
	};

	public RealtimeReplayDistributor(final Configuration configuration) {
		super(configuration);

		// TODO: Load from configuration.
		this.numWorkers = 0;
		this.cons = null;
		this.maxQueueSize = 0;
		this.executor = null;
		this.terminationLatch = null;

		/* Register the ports. */
		this.registerInputPort("in", this.input);
		this.registerOutputPort("out", this.output);
	}

	/**
	 * Constructs a RealtimeReplayDistributor.
	 * 
	 * @param numWorkers
	 *            number of worker threads processing the internal record buffer
	 * @param cons
	 *            the consumer
	 * @param terminationLatch
	 *            will be decremented after the last record was replayed
	 */
	public RealtimeReplayDistributor(final int numWorkers, final AbstractAnalysisPlugin cons, final CountDownLatch terminationLatch) {
		super(new Configuration(null));
		this.numWorkers = numWorkers;
		this.cons = cons;
		this.maxQueueSize = numWorkers * RealtimeReplayDistributor.QUEUE_SIZE_FACTOR;
		this.executor = new ScheduledThreadPoolExecutor(numWorkers);
		this.executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(true);
		this.executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
		this.terminationLatch = terminationLatch;

		this.registerInputPort("in", this.input);
		this.registerOutputPort("out", this.output);
	}

	public boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		if (this.startTime == -1) { // init on first record
			this.firstLoggingTimestamp = monitoringRecord.getLoggingTimestamp() - (1 * RealtimeReplayDistributor.MILLISECOND);
			this.offset = RealtimeReplayDistributor.REPLAY_OFFSET - this.firstLoggingTimestamp;
			this.startTime = RealtimeReplayDistributor.TIMESOURCE.getTime();
		}
		if (monitoringRecord.getLoggingTimestamp() < this.firstLoggingTimestamp) {
			final MonitoringRecordConsumerException e = new MonitoringRecordConsumerException("Timestamp of current record "
					+ monitoringRecord.getLoggingTimestamp() + " < firstLoggingTimestamp " + this.firstLoggingTimestamp);
			RealtimeReplayDistributor.LOG.error("RecordConsumerExecutionException", e);
			return false;
		}
		final long schedTime = (monitoringRecord.getLoggingTimestamp() + this.offset) // relative to 1st record
				- (RealtimeReplayDistributor.TIMESOURCE.getTime() - this.startTime); // substract elapsed time
		if (schedTime < 0) {
			final MonitoringRecordConsumerException e = new MonitoringRecordConsumerException("negative scheduling time: " + schedTime);
			RealtimeReplayDistributor.LOG.error("RecordConsumerExecutionException", e);
			return false;
		}
		synchronized (this) {
			while (this.active > this.maxQueueSize) {
				try {
					this.wait();
				} catch (final InterruptedException e) { // ignore
				}
			}
			this.active++;
			this.executor.schedule(new RealtimeReplayWorker(monitoringRecord, this, this.cons), schedTime, TimeUnit.NANOSECONDS); // *relative* delay from now

		}
		this.lTime = this.lTime < monitoringRecord.getLoggingTimestamp() ? monitoringRecord.getLoggingTimestamp() : this.lTime; // NOCS
		return true;
	}

	@Override
	public boolean execute() {
		return true;
	}

	public final long getOffset() {
		return this.offset;
	}

	public final long getStartTime() {
		return this.startTime;
	}

	@Override
	public void terminate(final boolean error) {
		final long terminationDelay = ((this.lTime + this.offset) - (RealtimeReplayDistributor.TIMESOURCE.getTime() - this.startTime))
				+ (100 * RealtimeReplayDistributor.MILLISECOND); // NOCS (MagicNumber)
		RealtimeReplayDistributor.LOG.info("Will terminate in " + terminationDelay + "nsecs from now");
		this.executor.schedule(new Runnable() {

			@Override
			public void run() {
				if (RealtimeReplayDistributor.this.terminationLatch != null) {
					RealtimeReplayDistributor.this.terminationLatch.countDown(); // signal that last record has been scheduled
				} else {
					RealtimeReplayDistributor.LOG.warn("terminationLatch == null");
				}
			}
		}, terminationDelay, TimeUnit.NANOSECONDS);
		this.executor.shutdown();
	}

	public void decreaseActive() {
		synchronized (this) {
			this.active--;
			this.notifyAll();
		}
	}

	public int getNumWorkers() {
		return this.numWorkers;
	}

	@Override
	protected Properties getDefaultProperties() {
		// TODO: Deliver default properties
		return new Properties();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration(null);

		// TODO: Save the current configuration

		return configuration;
	}
}
