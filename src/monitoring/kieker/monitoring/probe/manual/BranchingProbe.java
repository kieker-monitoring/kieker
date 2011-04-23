package kieker.monitoring.probe.manual;

import kieker.common.record.BranchingRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.probe.IMonitoringProbe;
import kieker.monitoring.timer.ITimeSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Convenience class which provides a static method to log branching.
 * 
 * @author Andre van Hoorn
 */
public class BranchingProbe implements IMonitoringProbe {
	private static final Log log = LogFactory.getLog(BranchingProbe.class);
	private static final IMonitoringController ctrlInst = MonitoringController.getInstance();
	private static final ITimeSource timesource = ctrlInst.getTimeSource();

	public static void monitorBranch(final int branchID, final int branchingOutcome) {
		// try-catch in order to avoid that any exception is propagated to the application code.
		try {
			BranchingProbe.ctrlInst.newMonitoringRecord(new BranchingRecord(timesource.getTime(), branchID, branchingOutcome));
		} catch (final Exception ex) {
			BranchingProbe.log.error("Error monitoring branching", ex);
		}
	}
}
