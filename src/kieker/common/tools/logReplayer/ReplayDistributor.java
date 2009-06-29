package kieker.common.tools.logReplayer;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import kieker.common.logReader.IMonitoringRecordConsumer;
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
				TimeUnit.MILLISECONDS);
	}

	@Override
	public void execute() {
		try {
			synchronized (this) {
				this.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executor.shutdownNow();
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
