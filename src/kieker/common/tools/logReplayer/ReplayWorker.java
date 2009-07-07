package kieker.common.tools.logReplayer;

import kieker.common.logReader.IKiekerRecordConsumer;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;

/**
 * A Runnable to be scheduled via the ReplayDistributor
 *
 * @author Robert von Massow
 *
 */
public class ReplayWorker implements Runnable {

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
//        this.monRec.setLoggingTimestamp(ctrlInst.getTime());
			cons.consumeMonitoringRecord(this.monRec);
			this.rd.decreaseActive();
		}
	}
}
