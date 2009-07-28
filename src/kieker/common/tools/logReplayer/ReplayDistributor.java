package kieker.common.tools.logReplayer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import kieker.common.logReader.IKiekerRecordConsumer;
import kieker.common.logReader.RecordConsumerExecutionException;
import kieker.tpmon.core.TpmonController;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * IKiekerRecordConsumer that distributes the log records to the worker
 * thread for "real time" replays.
 *
 * @author Robert von Massow
 *
 */
public class ReplayDistributor implements IKiekerRecordConsumer {
    private static final Log log = LogFactory.getLog(ReplayDistributor.class);
    private static TpmonController ctrlInst = null;
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

    private static final String outputFn = "SchedulingList";
    private static PrintStream ps;

	public void consumeMonitoringRecord(
			final AbstractKiekerMonitoringRecord monitoringRecord) throws RecordConsumerExecutionException {
		if (this.startTime == -1) { // init on first record
            try {
                // init on first record
                ps = new PrintStream(new FileOutputStream(outputFn, false)); // do not append
            } catch (FileNotFoundException ex) {
                log.info("FileNotFound:", ex);
            }
            this.firstLoggingTimestamp = monitoringRecord.getLoggingTimestamp() - (1*1000*1000) ; // 1 millisecond tolerance
			this.offset = (2 * 1000 * 1000 * 1000) - firstLoggingTimestamp;
			this.startTime = ctrlnst.getTime();
            log.info("firstLoggingTimeStamp: " + this.firstLoggingTimestamp);
            log.info("offset: " + this.offset);
            log.info("startTime" + this.startTime);
		}
        if (monitoringRecord.getLoggingTimestamp() < this.firstLoggingTimestamp){
            RecordConsumerExecutionException e = new RecordConsumerExecutionException("Timestamp of current record "+monitoringRecord.getLoggingTimestamp()+" < firstLoggingTimestamp "+ this.firstLoggingTimestamp);
            log.error("RecordConsumerExecutionException", e);
            throw e;
        }
		long schedTime = (monitoringRecord.getLoggingTimestamp() + this.offset) // relative to 1st record
				- (ctrlnst.getTime() - this.startTime); // substract elapsed time
        ps.println("curT.record: " + monitoringRecord.getLoggingTimestamp());ps.flush();
        ps.println("curT.ctrl: " + ctrlnst.getTime());ps.flush();
        ps.println("elapsedT (nsec): " + (ctrlnst.getTime() - this.startTime));
        ps.println("schedTime (nsec): " + schedTime);
        if (schedTime < 0){
            RecordConsumerExecutionException e = new RecordConsumerExecutionException("negative scheduling time: "+schedTime);
            log.error("RecordConsumerExecutionException", e);
            throw e;
        }
		synchronized (this) {
			if (this.active > this.maxQueueSize) {
                //log.info("this.active > this.maxQueueSize ("+this.active+"<"+this.maxQueueSize+")");
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			this.active++;
			this.executor.schedule(new ReplayWorker(monitoringRecord, this, this.cons),
					schedTime, TimeUnit.NANOSECONDS); // *relative* delay from now

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
        long terminationDelay = (this.lTime + this.offset) - (ctrlnst.getTime() - this.startTime)
				+ 100000000;
        log.info("Will terminate in " + terminationDelay + "nsecs from now");
		this.executor.schedule(new Runnable() {

			public void run() {
				ctrlnst.terminateMonitoring();
			}

		}, terminationDelay, TimeUnit.NANOSECONDS);
		this.executor.shutdown();
	}

	public synchronized void decreaseActive() {
		this.active--;
		this.notify();
	}

}
