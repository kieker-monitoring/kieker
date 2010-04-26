package kieker.tools.logReplayer;

import java.util.logging.Level;
import java.util.logging.Logger;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.MonitoringRecordReceiverException;
import kieker.tpan.consumer.IMonitoringRecordConsumer;
import kieker.tpan.consumer.MonitoringRecordConsumerException;

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
    private final IMonitoringRecordConsumer cons;
    private final RealtimeReplayDistributor rd;

    public RealtimeReplayWorker(final IMonitoringRecord monRec, final RealtimeReplayDistributor rd, final IMonitoringRecordConsumer cons) {
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
