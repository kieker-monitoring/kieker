package kieker.monitoring.core.controller;

import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import kieker.monitoring.core.configuration.Configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Andre van Hoorn, Jan Waller
 */
public abstract class MonitoringControllerState extends AbstractController implements IMonitoringControllerState {
	private final static Log log = LogFactory.getLog(MonitoringControllerState.class);

	protected final AtomicBoolean monitoringTerminated = new AtomicBoolean(false);
	private volatile boolean monitoringEnabled = false;
	private final String name;
	private final String hostname;
	private final AtomicInteger experimentId = new AtomicInteger(0);

	protected MonitoringControllerState(final Configuration configuration) {
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
	}
	
	@Override
	public final boolean isMonitoringTerminated() {
		return monitoringTerminated.get();
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
		return !isMonitoringTerminated() && monitoringEnabled;
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

	@Override
	protected void getState(final StringBuilder sb) {
		sb.append("Current State of kieker.monitoring (");
		sb.append(MonitoringController.getVersion());
		sb.append("): ");
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
		sb.append("'");
	}
}
