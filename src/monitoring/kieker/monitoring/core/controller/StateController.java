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
				StateController.log.warn("Failed to retrieve hostname");
			}
		}
		this.hostname = hostname;
	}

	@Override
	protected final void cleanup() {
		StateController.log.debug("Shutting down State Controller");
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Status: '");
		if (this.isMonitoringTerminated()) {
			sb.append("terminated");
		} else if (this.isMonitoringEnabled()) {
			sb.append("enabled");
		} else {
			sb.append("disabled");
		}
		sb.append("'\n");
		sb.append("\tName: '");
		sb.append(this.name);
		sb.append("'; Hostname: '");
		sb.append(this.hostname);
		sb.append("'; experimentID: '");
		sb.append(this.getExperimentId());
		sb.append("'");
		return sb.toString();
	}

	@Override
	public final boolean terminateMonitoring() {
		final MonitoringController monitoringController = super.monitoringController;
		if (monitoringController != null) {
			return monitoringController.terminate();
		} else {
			StateController.log.warn("Shutting down Monitoring before it is correctly initialized");
			return false;
		}
	}

	@Override
	public final boolean isMonitoringTerminated() {
		return super.isTerminated();
	}

	@Override
	public final boolean enableMonitoring() {
		if (this.isMonitoringTerminated()) {
			StateController.log.error("Refused to enable monitoring because monitoring has been permanently terminated");
			return false;
		}
		StateController.log.info("Enabling monitoring");
		this.monitoringEnabled = true;
		return true;
	}

	@Override
	public final boolean disableMonitoring() {
		StateController.log.info("Disabling monitoring");
		this.monitoringEnabled = false;
		return true;
	}

	@Override
	public final boolean isMonitoringEnabled() {
		return !super.isTerminated() && this.monitoringEnabled;
	}

	@Override
	public final String getName() {
		return this.name;
	}

	@Override
	public final String getHostName() {
		return this.hostname;
	}

	@Override
	public final int incExperimentId() {
		return this.experimentId.incrementAndGet();
	}

	@Override
	public final void setExperimentId(final int newExperimentID) {
		this.experimentId.set(newExperimentID);
	}

	@Override
	public final int getExperimentId() {
		return this.experimentId.get();
	}
}
