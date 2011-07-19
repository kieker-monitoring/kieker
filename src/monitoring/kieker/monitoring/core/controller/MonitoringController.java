package kieker.monitoring.core.controller;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import kieker.common.record.IMonitoringRecord;
import kieker.common.util.Version;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.core.sampler.ISampler;
import kieker.monitoring.core.sampler.ScheduledSamplerJob;
import kieker.monitoring.timer.ITimeSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Jan Waller
 */
public final class MonitoringController extends AbstractController implements IMonitoringController {
	private final static Log log = LogFactory.getLog(MonitoringController.class);

	private final StateController stateController;
	private final WriterController writerController;
	private final SamplingController samplingController;
	private final TimeSourceController timeSourceController;

	/** Name of the MBean. */
	private final ObjectName objectname;
	private final JMXConnectorServer jmxserver;

	// FACTORY
	public final static IMonitoringController createInstance(final Configuration configuration) {
		final MonitoringController monitoringController = new MonitoringController(configuration);
		// Initialize and handle early Termination (once for each Controller!)
		monitoringController.stateController.setMonitoringController(monitoringController);
		if (monitoringController.stateController.isTerminated()) {
			monitoringController.terminate();
			return monitoringController;
		}
		monitoringController.samplingController.setMonitoringController(monitoringController);
		if (monitoringController.samplingController.isTerminated()) {
			monitoringController.terminate();
			return monitoringController;
		}
		monitoringController.writerController.setMonitoringController(monitoringController);
		if (monitoringController.writerController.isTerminated()) {
			monitoringController.terminate();
			return monitoringController;
		}
		monitoringController.timeSourceController.setMonitoringController(monitoringController);
		if (monitoringController.timeSourceController.isTerminated()) {
			monitoringController.terminate();
			return monitoringController;
		}
		// Register JMX Interfaces
		synchronized (monitoringController) { // TODO: why synchronized?
			if (configuration.getBooleanProperty(Configuration.ACTIVATE_JMX)) {
				final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
				if ((monitoringController.objectname != null) && !monitoringController.isTerminated()) {
					try {
						// MXBeans is currently not possible (getClasses in IRecord)
						final StandardMBean mbean = new StandardMBean(monitoringController, IMonitoringController.class, false);
						mbs.registerMBean(mbean, monitoringController.objectname);

					} catch (final Exception e) {
						MonitoringController.log.warn("Unable to register Monitoring Controller MBean", e);
					}
				}
				if ((monitoringController.jmxserver != null) && !monitoringController.isTerminated()) {
					try {
						final ObjectName serverName = new ObjectName(configuration.getStringProperty(Configuration.ACTIVATE_JMX_DOMAIN), "type",
								configuration.getStringProperty(Configuration.ACTIVATE_JMX_REMOTE_NAME));
						mbs.registerMBean(monitoringController.jmxserver, serverName);
						monitoringController.jmxserver.start();
					} catch (final Exception e) {
						MonitoringController.log.warn("Unable to register JMXServer", e);
					}
				}
			}
		}
		/*
		 * This ensures that the terminateMonitoring() method is always called
		 * before shutting down the JVM. This method ensures that necessary cleanup
		 * steps are finished and no information is lost due to asynchronous writers.
		 */
		try {
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					if (!monitoringController.isMonitoringTerminated()) {
						// TODO: We should not use a logger in shutdown hooks, logger may already be down! (#26)
						MonitoringController.log.info("ShutdownHook notifies controller to initiate shutdown");
						monitoringController.terminateMonitoring();
					}
				}
			});
		} catch (final Exception e) {
			MonitoringController.log.warn("Failed to add shutdownHook");
		}
		MonitoringController.log.info(monitoringController.toString());
		return monitoringController;
	}

	// private Constructor
	private MonitoringController(final Configuration configuration) {
		// MBean
		ObjectName objectname = null;
		JMXConnectorServer jmxserver = null;
		if (configuration.getBooleanProperty(Configuration.ACTIVATE_JMX)) {
			if (configuration.getBooleanProperty(Configuration.ACTIVATE_JMX_CONTROLLER)) {
				try {
					objectname = new ObjectName(configuration.getStringProperty(Configuration.ACTIVATE_JMX_DOMAIN), "type",
							configuration.getStringProperty(Configuration.ACTIVATE_JMX_CONTROLLER_NAME));
				} catch (final Exception e) {
					MonitoringController.log.warn("Failed to initialize MonitoringController MBean", e);
				}
			}
			if (configuration.getBooleanProperty(Configuration.ACTIVATE_JMX_REMOTE)) {
				try {
					final JMXServiceURL serviceURL = new JMXServiceURL(configuration.getStringProperty(Configuration.ACTIVATE_JMX_REMOTE_URL));
					jmxserver = JMXConnectorServerFactory.newJMXConnectorServer(serviceURL, null, null);
				} catch (final Exception e) {
					MonitoringController.log.warn("Failed to initialize JMXServer", e);
				}
			}
		}
		this.objectname = objectname;
		this.jmxserver = jmxserver;
		this.stateController = new StateController(configuration);
		this.writerController = new WriterController(configuration);
		this.samplingController = new SamplingController(configuration);
		this.timeSourceController = new TimeSourceController(configuration);
	}

	/**
	 * Return the version name of this controller instance.
	 * 
	 * @return the version name
	 */
	public final static String getVersion() {
		return Version.getVERSION();
	}

	@Override
	protected final void cleanup() {
		MonitoringController.log.info("Shutting down Monitoring Controller (" + this.getName() + ")");
		this.stateController.terminate();
		synchronized (this) {
			if (this.objectname != null) {
				try {
					ManagementFactory.getPlatformMBeanServer().unregisterMBean(this.objectname);
				} catch (final Exception e) {
					MonitoringController.log.error("Failed to terminate MBean", e);
				}
			}
		}
		this.timeSourceController.terminate();
		this.samplingController.terminate();
		this.writerController.terminate();
		synchronized (this) {
			// TODO: correct placement? When do we want to shutdown all connections?
			if (this.jmxserver != null) {
				try {
					this.jmxserver.stop();
				} catch (final IOException e) {
					MonitoringController.log.error("Failed to terminate JMXServer", e);
				}
			}
		}
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Current State of kieker.monitoring (");
		sb.append(MonitoringController.getVersion());
		sb.append(") ");
		sb.append(this.stateController.toString());
		sb.append("\n");
		if ((this.jmxserver != null) && (this.jmxserver.isActive())) {
			sb.append("\tJMX remote access available\n");
			sb.append("\t\tService URL: '");
			sb.append(this.jmxserver.getAddress().toString());
			sb.append("'\n");
			final String[] connections = this.jmxserver.getConnectionIds();
			if (connections.length == 0) {
				sb.append("\t\tNo current remote connections\n");
			} else {
				for (String connection : connections) {
					sb.append("\t\tRemote connection: '");
					sb.append(connection);
					sb.append("'\n");
				}
			}
		}
		if (this.objectname != null) {
			sb.append("\tMonitoringController MBean available: '");
			sb.append(this.objectname.getCanonicalName());
			sb.append("'\n");
		}
		sb.append(this.writerController.toString());
		sb.append("\n");
		sb.append(this.samplingController.toString());
		return sb.toString();
	}

	// DELEGATE TO OTHER CONTROLLERS
	// #############################

	@Override
	public final boolean terminateMonitoring() {
		return this.stateController.terminateMonitoring();
	}

	@Override
	public final boolean isMonitoringTerminated() {
		return this.stateController.isMonitoringTerminated();
	}

	@Override
	public final boolean enableMonitoring() {
		return this.stateController.enableMonitoring();
	}

	@Override
	public final boolean disableMonitoring() {
		return this.stateController.disableMonitoring();
	}

	@Override
	public final boolean isMonitoringEnabled() {
		return this.stateController.isMonitoringEnabled();
	}

	@Override
	public final String getName() {
		return this.stateController.getName();
	}

	@Override
	public final String getHostName() {
		return this.stateController.getHostName();
	}

	@Override
	public final int incExperimentId() {
		return this.stateController.incExperimentId();
	}

	@Override
	public final void setExperimentId(final int newExperimentID) {
		this.stateController.setExperimentId(newExperimentID);
	}

	@Override
	public final int getExperimentId() {
		return this.stateController.getExperimentId();
	}

	@Override
	public final boolean newMonitoringRecord(final IMonitoringRecord record) {
		return this.writerController.newMonitoringRecord(record);
	}

	@Override
	public final long getNumberOfInserts() {
		return this.writerController.getNumberOfInserts();
	}

	@Override
	public final ScheduledSamplerJob schedulePeriodicSampler(final ISampler sampler, final long initialDelay, final long period, final TimeUnit timeUnit) {
		return this.samplingController.schedulePeriodicSampler(sampler, initialDelay, period, timeUnit);
	}

	@Override
	public final boolean removeScheduledSampler(final ScheduledSamplerJob sampler) {
		return this.samplingController.removeScheduledSampler(sampler);
	}

	@Override
	public final ITimeSource getTimeSource() {
		return this.timeSourceController.getTimeSource();
	}

	// GET SINGLETON INSTANCE
	// #############################

	public final static IMonitoringController getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * SINGLETON
	 */
	private final static class LazyHolder {
		static {
			INSTANCE = MonitoringController.createInstance(Configuration.createSingletonConfiguration());
		}
		private final static IMonitoringController INSTANCE;
	}
}
