package kieker.tools.logReplayer;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.MonitoringRecordReceiverException;
import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;

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
    private final IMonitoringRecord monRec;
    private final IMonitoringRecordConsumerPlugin cons;
    private final RealtimeReplayDistributor rd;

    public RealtimeReplayWorker(final IMonitoringRecord monRec, final RealtimeReplayDistributor rd, final IMonitoringRecordConsumerPlugin cons) {
        this.monRec = monRec;
        this.cons = cons;
        this.rd = rd;
    }

    public void run() {
        if (this.monRec != null) {
            try {
                if (!cons.newMonitoringRecord(this.monRec)) {
                    // TODO: check what to do
                    log.error("Consumer returned with error");
                }
            } catch (MonitoringRecordReceiverException ex) {
                log.error("Caught MonitoringRecordReceiverException", ex);
            }
            this.rd.decreaseActive();
        }
    }
}
