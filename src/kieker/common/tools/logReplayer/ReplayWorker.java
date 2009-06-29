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

    private static final TpmonController c = TpmonController.getInstance();

	public ReplayWorker(final AbstractKiekerMonitoringRecord monRec) {
		this.monRec = monRec;
	}

	@Override
	public void run() {
		if (monRec != null) {
        monRec.setLoggingTimestamp(c.getTime());
			c.logMonitoringRecord(monRec);
		}
	}
}
