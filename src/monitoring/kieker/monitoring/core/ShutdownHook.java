package kieker.monitoring.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class ensures that virtual machine shutdown (e.g., cause by a
 * System.exit(int)) is delayed until all monitoring data is written. This is
 * important for the asynchronous writers for the files system and database,
 * since these store data with a small delay and data would be lost when
 * System.exit is not delayed.
 *
 * When the system shutdown is initiated, the termination of the Virtual Machine
 * is delayed until all registered worker queues are empty.
 *
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller, Robert von Massow
 */
final class ShutdownHook extends Thread {
	private static final Log log = LogFactory.getLog(ShutdownHook.class);

	private final IMonitoringControllerState ctrl;

	public ShutdownHook(final IMonitoringControllerState ctrl) {
		this.ctrl = ctrl;
	}

	@Override
	public void run() {
		// is called when VM shutdown (e.g., strg+c) is initiated or when system.exit is called
		if (!this.ctrl.isMonitoringTerminated()) {
			//TODO: We can't use a logger in shutdown hooks, logger may already be down!
			// i think: make sure that they are either down and don't log or they are terminated
			// by the shutdown hook. it would be ok to tear down logging at last
			ShutdownHook.log.info("ShutdownHook notifies controller to initiate shutdown");
			this.ctrl.terminateMonitoring();
		}
	}
}

