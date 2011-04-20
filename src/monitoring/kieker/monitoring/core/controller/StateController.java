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

	@Override
	protected final void cleanup() {
		log.info("Shutting down State Controller");
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Status: '");
		if (isMonitoringTerminated()) {
			sb.append("terminated");
		} else if (isMonitoringEnabled()) {
			sb.append("enabled");
		} else {
			sb.append("disabled");
		}
		sb.append("'\n");
		sb.append("\tName: '");
		sb.append(name);
		sb.append("'; Hostname: '");
		sb.append(hostname);
		sb.append("'; experimentID: '");
		sb.append(getExperimentId());
		sb.append("'");
		return sb.toString();
	}

	@Override
	public final boolean terminateMonitoring() {
		final MonitoringController monitoringController = super.getMonitoringController();
		if (monitoringController != null) {
			return monitoringController.terminate();
		} else {
			log.warn("Shutting down Monitoring before it is correctly initialized");
			return false;
		}
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
}
