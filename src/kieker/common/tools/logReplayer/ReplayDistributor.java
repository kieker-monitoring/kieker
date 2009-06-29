package kieker.common.tools.logReplayer;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import kieker.common.logReader.IMonitoringRecordConsumer;
import kieker.tpmon.core.TpmonController;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;

import org.apache.log4j.Logger;

/**
 * IMonitoringRecordConsumer that distributes the log records to the worker
 * thread for "real time" replays.
 * 
 * @author Robert von Massow
 * 
 */
public class ReplayDistributor implements IMonitoringRecordConsumer, Runnable {

	public final int numWorkers;

	private volatile long startTime = -1, offset = -1;

	private final ScheduledThreadPoolExecutor executor;

    private static final TpmonController c = TpmonController.getInstance();

	public ReplayDistributor(final int numWorkers) {
		this.numWorkers = numWorkers;
		executor = new ScheduledThreadPoolExecutor(numWorkers);
	}

	@Override
	public void consumeMonitoringRecord(
			final AbstractKiekerMonitoringRecord monitoringRecord) {
		if (startTime == -1) { // init on first record
			offset = monitoringRecord.getLoggingTimestamp() - (20*1000*1000);
			startTime = c.getTime();
		}
		long schedTime = (monitoringRecord.getLoggingTimestamp() - offset)
				- (c.getTime() - startTime);
		executor.schedule(new ReplayWorker(monitoringRecord), schedTime,
				TimeUnit.NANOSECONDS);
        System.out.println(monitoringRecord.getLoggingTimestamp() - offset);
	}

	@Override
	public void run() {
		try {
			synchronized (this) {
				this.wait();
			}
		} catch (InterruptedException e) {
			List<Runnable> q = executor.shutdownNow();
			Logger.getLogger(this.getClass()).warn(
					"Interrupted while " + q.size()
							+ " records were scheduled for replay");
			return;
		}
		Logger.getLogger(this.getClass()).warn(
				"Waiting for "
						+ (executor.getTaskCount() - executor
								.getCompletedTaskCount())
						+ " tasks... This can take some time");
		executor.shutdown();
        this.notifyAll();
        c.terminateMonitoring();
	}

	@Override
	public void execute() {
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public String[] getRecordTypeSubscriptionList() {
		return null;
	}

	public final long getOffset() {
		return offset;
	}

	public final long getStartTime() {
		return startTime;
	}

}
