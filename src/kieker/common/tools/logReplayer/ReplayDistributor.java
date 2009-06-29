package kieker.common.tools.logReplayer;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import kieker.common.logReader.IMonitoringRecordConsumer;
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

	public ReplayDistributor(final int numWorkers) {
		this.numWorkers = numWorkers;
		executor = new ScheduledThreadPoolExecutor(numWorkers);
	}

	@Override
	public void consumeMonitoringRecord(
			final AbstractKiekerMonitoringRecord monitoringRecord) {
		if (startTime == -1) { // init on first record
			offset = monitoringRecord.getLoggingTimestamp() - 20000;
			startTime = System.nanoTime();
		}
		long schedTime = (monitoringRecord.getLoggingTimestamp() - offset)
				- (System.nanoTime() - startTime);
		executor.schedule(new ReplayWorker(monitoringRecord), schedTime,
				TimeUnit.NANOSECONDS);
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
