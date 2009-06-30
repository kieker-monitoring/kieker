package kieker.common.tools.logReplayer;

import kieker.tpmon.core.TpmonController;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;

/**
 * A Runnable to be scheduled via the ReplayDistributor
 *
 * @author Robert von Massow
 *
 */
public class ReplayWorker implements Runnable {

	private final AbstractKiekerMonitoringRecord monRec;

	private final ReplayDistributor rd;

    private static final TpmonController c = TpmonController.getInstance();

	public ReplayWorker(final AbstractKiekerMonitoringRecord monRec, final ReplayDistributor rd) {
		this.monRec = monRec;
		this.rd = rd;
	}

	@Override
	public void run() {
		if (this.monRec != null) {
//        this.monRec.setLoggingTimestamp(c.getTime());
			c.logMonitoringRecord(this.monRec);
			this.rd.decreaseActive();
		}
	}
}
