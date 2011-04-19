package kieker.monitoring.core.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Jan Waller
 */
public final class TerminationController extends AbstractController {
	private final static Log log = LogFactory.getLog(TerminationController.class);

	private MonitoringController monitoringController = null;

	protected final synchronized void setMonitoringController(MonitoringController monitoringController) {
		if (this.monitoringController == null) {
			this.monitoringController = monitoringController;
			// Register Shutdown Hook
			try {
				Runtime.getRuntime().addShutdownHook(new ShutdownHook(monitoringController));
			} catch (final Exception e) {
				log.warn("Failed to add shutdownHook");
			}
		}
	}

	@Override
	protected void cleanup() {
		if (monitoringController != null) {
			monitoringController.terminateMonitoring();
		}
	}

	@Override
	protected void getState(StringBuilder sb) {
		// do nothing
	}

	/**
	 * This class ensures that the terminateMonitoring() method is always called
	 * before shutting down the JVM. This method ensures that necessary cleanup
	 * steps are finished and no information is lost due to asynchronous writers.
	 * 
	 * @author Matthias Rohr, Andre van Hoorn, Jan Waller, Robert von Massow
	 */
	private final class ShutdownHook extends Thread {
		private final MonitoringController ctrl;

		public ShutdownHook(final MonitoringController ctrl) {
			this.ctrl = ctrl;
		}

		@Override
		public void run() {
			// is called when VM shutdown (e.g., strg+c) is initiated or when system.exit is called
			if (!ctrl.isMonitoringTerminated()) {
				// TODO: We can't use a logger in shutdown hooks, logger may already be down! (#26)
				log.info("ShutdownHook notifies controller to initiate shutdown");
				ctrl.terminateMonitoring();
			}
		}
	}
}
