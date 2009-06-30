package kieker.common.tools.logReplayer;

import kieker.common.logReader.IMonitoringRecordConsumer;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;

/**
 * A Runnable to be scheduled via the ReplayDistributor
 *
 * @author Robert von Massow
 *
 */
public class ReplayWorker implements Runnable {

	private final AbstractKiekerMonitoringRecord monRec;
    private final IMonitoringRecordConsumer cons;
	private final ReplayDistributor rd;

	public ReplayWorker(final AbstractKiekerMonitoringRecord monRec, final ReplayDistributor rd, final IMonitoringRecordConsumer cons) {
		this.monRec = monRec;
        this.cons = cons;
		this.rd = rd;
	}

	@Override
	public void run() {
		if (this.monRec != null) {
//        this.monRec.setLoggingTimestamp(ctrlInst.getTime());
			cons.consumeMonitoringRecord(this.monRec);
			this.rd.decreaseActive();
		}
	}
}
