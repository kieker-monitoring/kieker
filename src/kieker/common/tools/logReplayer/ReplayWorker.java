package kieker.common.tools.logReplayer;

import kieker.common.logReader.IKiekerRecordConsumer;
import kieker.common.logReader.RecordConsumerExecutionException;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A Runnable to be scheduled via the ReplayDistributor
 *
 * @author Robert von Massow
 *
 */
public class ReplayWorker implements Runnable {

    private static final Log log = LogFactory.getLog(ReplayWorker.class);
    private final AbstractKiekerMonitoringRecord monRec;
    private final IKiekerRecordConsumer cons;
    private final ReplayDistributor rd;

    public ReplayWorker(final AbstractKiekerMonitoringRecord monRec, final ReplayDistributor rd, final IKiekerRecordConsumer cons) {
        this.monRec = monRec;
        this.cons = cons;
        this.rd = rd;
    }

    @Override
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
