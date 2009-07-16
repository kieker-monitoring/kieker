package kieker.common.tools.logReplayer;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import kieker.common.logReader.IKiekerRecordConsumer;
import kieker.tpmon.core.TpmonController;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;

/**
 * IKiekerRecordConsumer that distributes the log records to the worker
 * thread for "real time" replays.
 *
 * @author Robert von Massow
 *
 */
public class ReplayDistributor implements IKiekerRecordConsumer {

	public final int numWorkers;
    private final IKiekerRecordConsumer cons;

	private volatile long startTime = -1, offset = -1, firstLoggingTimestamp;

	private final ScheduledThreadPoolExecutor executor;

	private long lTime;

	private static final TpmonController ctrlnst = TpmonController.getInstance();

    private int active;

	private final int maxQueueSize;

    /** Private constructor should not be used */
    private ReplayDistributor() {
        this.executor = null;
        this.numWorkers = -1;
        this.cons = null;
        this.maxQueueSize = -1;
    }

	public ReplayDistributor(final int numWorkers, final IKiekerRecordConsumer cons) {
		this.numWorkers = numWorkers;
        this.cons = cons;
		this.maxQueueSize = numWorkers * 1000;
		this.executor = new ScheduledThreadPoolExecutor(numWorkers);
		this.executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(true);
		this.executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
	}

	public void consumeMonitoringRecord(
			final AbstractKiekerMonitoringRecord monitoringRecord) {
		if (this.startTime == -1) { // init on first record
            this.firstLoggingTimestamp = monitoringRecord.getLoggingTimestamp();
			this.offset = (20 * 1000 * 1000);
			this.startTime = ctrlnst.getTime();
		}
		long delay;
		long schedTime = (delay =(monitoringRecord.getLoggingTimestamp() - this.offset))
				- (ctrlnst.getTime() - this.startTime);
		synchronized (this) {
			if (this.active > this.maxQueueSize) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			this.active++;
			this.executor.schedule(new ReplayWorker(monitoringRecord, this, this.cons),
					schedTime, TimeUnit.NANOSECONDS);

		}
		this.lTime = this.lTime < monitoringRecord.getLoggingTimestamp() ? monitoringRecord
				.getLoggingTimestamp()
				: this.lTime;
	}

	public boolean execute() {
        return true;
	}

	public String[] getRecordTypeSubscriptionList() {
		return null;
	}

	public final long getOffset() {
		return this.offset;
	}

	public final long getStartTime() {
		return this.startTime;
	}

	public void terminate() {
		this.executor.schedule(new Runnable() {

			public void run() {
				ctrlnst.terminateMonitoring();
			}

		}, (this.lTime - this.offset) - (ctrlnst.getTime() - this.startTime)
				+ 100000000, TimeUnit.NANOSECONDS);
		this.executor.shutdown();
	}

	public synchronized void decreaseActive() {
		this.active--;
		this.notify();
	}

}
