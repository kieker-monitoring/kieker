package kieker.monitoring.core.controller;

import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

import kieker.monitoring.core.configuration.Configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Andre van Hoorn, Jan Waller
 */
public final class StateController extends AbstractController implements IStateController {
	private final static Log log = LogFactory.getLog(StateController.class);

	private volatile boolean monitoringEnabled = false;
	private final String name;
	private final String hostname;
	private final AtomicInteger experimentId = new AtomicInteger(0);

	private MonitoringController monitoringController = null;

	protected StateController(final Configuration configuration) {
		this.name = configuration.getStringProperty(Configuration.CONTROLLER_NAME);
		this.experimentId.set(configuration.getIntProperty(Configuration.EXPERIMENT_ID));
		this.monitoringEnabled = configuration.getBooleanProperty(Configuration.MONITORING_ENABLED);
		String hostname = configuration.getStringProperty(Configuration.HOST_NAME);
		if (hostname.isEmpty()) {
			hostname = "<UNKNOWN>";
			try {
				hostname = java.net.InetAddress.getLocalHost().getHostName();
			} catch (final UnknownHostException ex) {
				log.warn("Failed to retrieve hostname");
			}
		}
		this.hostname = hostname;
	}

	protected final synchronized void setMonitoringController(final MonitoringController monitoringController) {
		if (monitoringController == null) {
			this.monitoringController = monitoringController;
			// Register Shutdown Hook
			try {
				Runtime.getRuntime().addShutdownHook(new ShutdownHook(this));
			} catch (final Exception e) {
				log.warn("Failed to add shutdownHook");
			}
		}
	}

	protected final MonitoringController getMonitoringController() {
		return monitoringController;
	}

	@Override
	protected final void cleanup() {
		log.info("Controller (" + getName() + ") shutting down");
		if (monitoringController != null) {
			monitoringController.terminate();
		} else {
			log.warn("Shutting down Monitoring before it is correctly initialized");
		}
		log.info("Controller (" + getName() + ") shutdown completed");
	}

	@Override
	protected final void getState(final StringBuilder sb) {
		if (isMonitoringTerminated()) {
			sb.append("terminated");
		} else if (isMonitoringEnabled()) {
			sb.append("enabled");
		} else {
			sb.append("disabled");
		}
		sb.append("\n");
		sb.append("\tName: '");
		sb.append(name);
		sb.append("'; Hostname: '");
		sb.append(hostname);
		sb.append("'; experimentID: '");
		sb.append(getExperimentId());
		sb.append("'\n");
	}

	@Override
	public final boolean terminateMonitoring() {
		return super.terminate();
	}

	@Override
	public final boolean isMonitoringTerminated() {
		return super.isTerminated();
	}

	@Override
	public final boolean enableMonitoring() {
		if (isMonitoringTerminated()) {
			log.error("Refused to enable monitoring because monitoring has been permanently terminated");
			return false;
		}
		log.info("Enabling monitoring");
		monitoringEnabled = true;
		return true;
	}

	@Override
	public final boolean disableMonitoring() {
		log.info("Disabling monitoring");
		monitoringEnabled = false;
		return true;
	}

	@Override
	public final boolean isMonitoringEnabled() {
		return !super.isTerminated() && monitoringEnabled;
	}

	@Override
	public final String getName() {
		return name;
	}

	@Override
	public final String getHostName() {
		return hostname;
	}

	@Override
	public final int incExperimentId() {
		return experimentId.incrementAndGet();
	}

	@Override
	public final void setExperimentId(final int newExperimentID) {
		experimentId.set(newExperimentID);
	}

	@Override
	public final int getExperimentId() {
		return experimentId.get();
	}

	/**
	 * This class ensures that the terminateMonitoring() method is always called
	 * before shutting down the JVM. This method ensures that necessary cleanup
	 * steps are finished and no information is lost due to asynchronous writers.
	 * 
	 * @author Matthias Rohr, Andre van Hoorn, Jan Waller, Robert von Massow
	 */
	private final class ShutdownHook extends Thread {
		private final StateController ctrl;

		public ShutdownHook(final StateController ctrl) {
			this.ctrl = ctrl;
		}

		@Override
		public void run() {
			// is called when VM shutdown (e.g., strg+c) is initiated or when system.exit is called
			if (!ctrl.isMonitoringTerminated()) {
				// TODO: We can't use a logger in shutdown hooks, logger may already be down! (#26)
				log.info("ShutdownHook notifies controller to initiate shutdown");
				ctrl.terminate();
			}
		}
	}
}
