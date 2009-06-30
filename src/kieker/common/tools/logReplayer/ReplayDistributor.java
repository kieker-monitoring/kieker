package kieker.common.tools.logReplayer;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import kieker.common.logReader.IMonitoringRecordConsumer;
import kieker.tpmon.core.TpmonController;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;

/**
 * IMonitoringRecordConsumer that distributes the log records to the worker
 * thread for "real time" replays.
 *
 * @author Robert von Massow
 *
 */
public class ReplayDistributor implements IMonitoringRecordConsumer {

	public final int numWorkers;

	private volatile long startTime = -1, offset = -1;

	private final ScheduledThreadPoolExecutor executor;

	private long lTime;

	private int active;

	private final int maxQueueSize;

	private static final TpmonController c = TpmonController.getInstance();

	public ReplayDistributor(final int numWorkers) {
		this.numWorkers = numWorkers;
		this.maxQueueSize = numWorkers * 1000;
		this.executor = new ScheduledThreadPoolExecutor(numWorkers);
		this.executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(true);
		this.executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
	}

	@Override
	public void consumeMonitoringRecord(
			final AbstractKiekerMonitoringRecord monitoringRecord) {
		if (this.startTime == -1) { // init on first record
			this.offset = monitoringRecord.getLoggingTimestamp()
					- (20 * 1000 * 1000);
			this.startTime = c.getTime();
		}
		long schedTime = (monitoringRecord.getLoggingTimestamp() - this.offset)
				- (c.getTime() - this.startTime);
		synchronized (this) {
			if (this.active > this.maxQueueSize) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			this.active++;
			this.executor.schedule(new ReplayWorker(monitoringRecord, this),
					schedTime, TimeUnit.NANOSECONDS);

		}
		this.lTime = this.lTime < monitoringRecord.getLoggingTimestamp() ? monitoringRecord
				.getLoggingTimestamp()
				: this.lTime;
	}

	@Override
	public boolean execute() {
        return true;
	}

	@Override
	public String[] getRecordTypeSubscriptionList() {
		return null;
	}

	public final long getOffset() {
		return this.offset;
	}

	public final long getStartTime() {
		return this.startTime;
	}

	@Override
	public void terminate() {
		this.executor.schedule(new Runnable() {

			@Override
			public void run() {
				c.terminateMonitoring();
			}

		}, (this.lTime - this.offset) - (c.getTime() - this.startTime)
				+ 100000000, TimeUnit.NANOSECONDS);
		this.executor.shutdown();
	}

	public synchronized void decreaseActive() {
		this.active--;
		this.notify();
	}

}
