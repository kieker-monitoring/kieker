package kieker.common.tools.logReplayer;

import java.util.Timer;
import java.util.TimerTask;

import kieker.tpmon.core.TpmonController;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;

public class ReplayWorker extends TimerTask {

	private AbstractKiekerMonitoringRecord monRec;
	private ReplayDistributor rd;
	private Timer t;

	public ReplayWorker(AbstractKiekerMonitoringRecord monRec,
			ReplayDistributor rd, Timer t) {
		this.monRec = monRec;
		this.rd = rd;
		this.t = t;
	}

	@Override
	public void run() {
		if (monRec != null) {
			TpmonController.getInstance().logMonitoringRecord(this.monRec);
		}
		synchronized (rd) {
			rd.reInitTimer(this.t);
		}
	}
}
