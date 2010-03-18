package kieker.common.logReader;

import kieker.common.monitoringRecord.AbstractKiekerMonitoringRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A Runnable to be scheduled via the RealtimeReplayDistributor
 *
 * @author Robert von Massow
 *
 */
public class RealtimeReplayWorker implements Runnable {

    private static final Log log = LogFactory.getLog(RealtimeReplayWorker.class);
    private final AbstractKiekerMonitoringRecord monRec;
    private final IKiekerRecordConsumer cons;
    private final RealtimeReplayDistributor rd;

    public RealtimeReplayWorker(final AbstractKiekerMonitoringRecord monRec, final RealtimeReplayDistributor rd, final IKiekerRecordConsumer cons) {
        this.monRec = monRec;
        this.cons = cons;
        this.rd = rd;
    }

    public void run() {
        if (this.monRec != null) {
            try {
                cons.consumeMonitoringRecord(this.monRec);
            } catch (RecordConsumerExecutionException ex) {
                // TODO: check what to do
                log.error("Caught RecordConsumerExecutionException", ex);
            }
            this.rd.decreaseActive();
        }
    }
}
