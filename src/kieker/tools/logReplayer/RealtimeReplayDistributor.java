package kieker.tools.logReplayer;

import java.util.concurrent.CountDownLatch;
import kieker.tpan.consumer.IMonitoringRecordConsumer;
import kieker.tpan.consumer.MonitoringRecordConsumerExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import kieker.common.record.IMonitoringRecord;

import kieker.tpmon.core.TpmonController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * IMonitoringRecordConsumer that distributes the log records to the worker
 * thread for "real time" replays.
 *
 * @author Robert von Massow
 *
 */
public class RealtimeReplayDistributor implements IMonitoringRecordConsumer {

    private static final Log log = LogFactory.getLog(RealtimeReplayDistributor.class);
    public final int numWorkers;
    private final IMonitoringRecordConsumer cons;
    private volatile long startTime = -1, offset = -1, firstLoggingTimestamp;
    private final ScheduledThreadPoolExecutor executor;
    private long lTime;
    private static final TpmonController ctrlnst = TpmonController.getInstance();
    private volatile int active;
    private final int maxQueueSize;
    private final CountDownLatch terminationLatch;

    /** Private constructor should not be used */
    private RealtimeReplayDistributor() {
        this.executor = null;
        this.numWorkers = -1;
        this.cons = null;
        this.maxQueueSize = -1;
        this.terminationLatch = null;
    }

    /**
     * Constructs a RealtimeReplayDistributor.
     * 
     * @param numWorkers number of worker threads processing the internal record buffer
     * @param cons the consumer
     * @param terminationLatch will be decremented after the last record was replayed
     */
    public RealtimeReplayDistributor(final int numWorkers, final IMonitoringRecordConsumer cons, final CountDownLatch terminationLatch) {
        this.numWorkers = numWorkers;
        this.cons = cons;
        this.maxQueueSize = numWorkers * 1000;
        this.executor = new ScheduledThreadPoolExecutor(numWorkers);
        this.executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(true);
        this.executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
        this.terminationLatch = terminationLatch;
    }

    //private static final String outputFn = "SchedulingList";
    //private static PrintStream ps;
    public void consumeMonitoringRecord(
            final IMonitoringRecord monitoringRecord) throws MonitoringRecordConsumerExecutionException {
        if (this.startTime == -1) { // init on first record
            //try {
            // init on first record
            //ps = new PrintStream(new FileOutputStream(outputFn, false)); // do not append
            //} catch (FileNotFoundException ex) {
            //    log.info("FileNotFound:", ex);
            //}
            this.firstLoggingTimestamp = monitoringRecord.getLoggingTimestamp() - (1 * 1000 * 1000); // 1 millisecond tolerance
            this.offset = (2 * 1000 * 1000 * 1000) - firstLoggingTimestamp;
            this.startTime = ctrlnst.getTime();
            //log.info("firstLoggingTimeStamp: " + this.firstLoggingTimestamp);
            //log.info("offset: " + this.offset);
            //log.info("startTime" + this.startTime);
        }
        if (monitoringRecord.getLoggingTimestamp() < this.firstLoggingTimestamp) {
            MonitoringRecordConsumerExecutionException e = new MonitoringRecordConsumerExecutionException("Timestamp of current record " + monitoringRecord.getLoggingTimestamp() + " < firstLoggingTimestamp " + this.firstLoggingTimestamp);
            log.error("RecordConsumerExecutionException", e);
            throw e;
        }
        long schedTime = (monitoringRecord.getLoggingTimestamp() + this.offset) // relative to 1st record
                - (ctrlnst.getTime() - this.startTime); // substract elapsed time
        //ps.println("curT.record: " + monitoringRecord.getLoggingTimestamp());ps.flush();
        //ps.println("curT.ctrl: " + ctrlnst.getTime());ps.flush();
        //ps.println("elapsedT (nsec): " + (ctrlnst.getTime() - this.startTime));
        //ps.println("schedTime (nsec): " + schedTime);
        if (schedTime < 0) {
            MonitoringRecordConsumerExecutionException e = new MonitoringRecordConsumerExecutionException("negative scheduling time: " + schedTime);
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
            this.executor.schedule(new RealtimeReplayWorker(monitoringRecord, this, this.cons),
                    schedTime, TimeUnit.NANOSECONDS); // *relative* delay from now

        }
        this.lTime = this.lTime < monitoringRecord.getLoggingTimestamp() ? monitoringRecord.getLoggingTimestamp()
                : this.lTime;
    }

    public boolean execute() {
        return true;
    }

    public Class<? extends IMonitoringRecord>[] getRecordTypeSubscriptionList() {
        return null;
    }

    public final long getOffset() {
        return this.offset;
    }

    public final long getStartTime() {
        return this.startTime;
    }

    public void terminate(final boolean error) {
        long terminationDelay = (this.lTime + this.offset) - (ctrlnst.getTime() - this.startTime) + 100000000;
        log.info("Will terminate in " + terminationDelay + "nsecs from now");
        this.executor.schedule(new Runnable() {

            public void run() {
                //ctrlnst.terminateMonitoring();
                if (terminationLatch != null) {
                    terminationLatch.countDown(); // signal that last record has been scheduled
                } else {
                    log.warn("terminationLatch == null");
                }
                //cons.terminate(error);
                //log.info("Terminating Controller");
            }
        }, terminationDelay, TimeUnit.NANOSECONDS);
        this.executor.shutdown();
    }

    public synchronized void decreaseActive() {
        this.active--;
        this.notify();
    }
}
