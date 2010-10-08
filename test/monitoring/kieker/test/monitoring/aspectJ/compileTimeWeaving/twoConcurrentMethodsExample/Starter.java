package kieker.test.monitoring.aspectJ.compileTimeWeaving.twoConcurrentMethodsExample;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;
import kieker.monitoring.core.MonitoringController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A simple test and demonstration scenario for Kieker's monitoring component
 * tpmon.
 * 
 * @author Matthias Rohr History: 2009/02/20: Reduced text length 2008/10/20:
 *         Initial version
 * 
 */
public class Starter extends Thread {

	private static final Log log = LogFactory.getLog(Starter.class);

	public static void main(final String[] args) throws InterruptedException {
		for (int i = 0; i < 10000; i++) {
			new Starter().start();
			Thread.sleep((int) (Math.max(0, Math.random() * 115d - (i / 142d)) + 1)); // wait
																						// between
																						// requests
			// log.info(i);
		}
		// TODO: remove since looks strange and should not be necessary:
		MonitoringController.getInstance().terminateMonitoring();
		// System.exit(0);
	}

	@Override
	public void run() {
		final double ranVal = Math.random();
		if (ranVal < 0.5) {
			if (ranVal < 0.25) {
				// do nothing
			} else {
				this.waitP(300);
			}
		} else {
			if (ranVal > 0.75) {
				this.work();
				this.waitP(300);
			} else {
				this.work();
			}
		}
	}

	@OperationExecutionMonitoringProbe()
	public void waitP(final long sleeptime) {
		try {
			Thread.sleep(sleeptime);
		} catch (final Exception e) {
		}
	}

	static boolean boolvar = true;

	@OperationExecutionMonitoringProbe()
	private void work() {
		int a = (int) (Math.random() * 5d);
		for (int i = 0; i < 2000000; i++) {
			a += i / 1000;
		}
		if (a % 10000 == 0) {
			Starter.boolvar = false;
		}
	}
}
