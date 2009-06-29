package kieker.loganalysis.recordConsumer;

import java.util.Timer;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

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

	private final Timer[] timers;

	private volatile long startTime = -1, offset = -1;

	private final BlockingQueue<AbstractKiekerMonitoringRecord> q;

	private int timerInitIndex = 0;

	public ReplayDistributor(int numWorkers) {
		this.numWorkers = numWorkers;
		timers = new Timer[this.numWorkers];
		for (int i = 0; i < numWorkers; i++) {
			timers[i] = new Timer();
		}
		q = new LinkedBlockingQueue<AbstractKiekerMonitoringRecord>(
				numWorkers * 10);
	}

	@Override
	public void consumeMonitoringRecord(
			AbstractKiekerMonitoringRecord monitoringRecord) {
		if (startTime == -1) {
			offset = monitoringRecord.getLoggingTimestamp() - 200;
			startTime = System.currentTimeMillis();
		}
		if (timerInitIndex < numWorkers) {
			timers[timerInitIndex].schedule(new ReplayWorker(monitoringRecord,
					this, timers[timerInitIndex++]), monitoringRecord
					.getLoggingTimestamp()
					- offset);
		} else {
			try {
				q.put(monitoringRecord);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Logger.getLogger(this.getClass()).warn("Interrupted replay before event queue was emptied");
				synchronized (this) {
					this.notify();
				}
			}
		}
	}

	@Override
	public void run() {
		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (Timer t : timers) {
			t.cancel();
		}
	}

	@Override
	public String[] getRecordTypeSubscriptionList() {
		return new String[] {};
	}

	protected void reInitTimer(Timer t) {
		AbstractKiekerMonitoringRecord r;
		try {
			r = q.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			this.notify();
			return;
		}
		t.schedule(new ReplayWorker(r, this, t), r.getLoggingTimestamp()
				- offset);
	}

	public AbstractKiekerMonitoringRecord getEvent()
			throws InterruptedException {
		return q.take();
	}

	public final long getOffset() {
		return offset;
	}

	public final long getStartTime() {
		return startTime;
	}

}
