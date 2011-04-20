package kieker.monitoring.core.controller;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

import javax.management.ObjectName;

import kieker.common.record.IMonitoringRecord;
import kieker.common.util.Version;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.core.sampler.ISampler;
import kieker.monitoring.core.sampler.ScheduledSamplerJob;
import kieker.monitoring.timer.ITimeSource;
import kieker.monitoring.writer.IMonitoringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Jan Waller
 */
public final class MonitoringController extends AbstractController implements IMonitoringController, MonitoringControllerMBean {
	private final static Log log = LogFactory.getLog(MonitoringController.class);

	private final StateController stateController;
	private final WriterController writerController;
	private final SamplingController samplingController;

	/** Name of the MBean. */
	private final ObjectName objectname;

	// FACTORY
	public final static IMonitoringController createInstance(final Configuration configuration) {
		final MonitoringController monitoringController = new MonitoringController(configuration);
		// Initialize and handle Termination
		monitoringController.stateController.setMonitoringController(monitoringController);
		if (monitoringController.stateController.isTerminated()) {
			monitoringController.terminate();
			return monitoringController;
		}
		// Register MBean
		synchronized (monitoringController) {
			if ((monitoringController.objectname != null) && !monitoringController.isTerminated()) {
				try {
					ManagementFactory.getPlatformMBeanServer().registerMBean(monitoringController, monitoringController.objectname);
				} catch (final Exception e) {
					MonitoringController.log.warn("Unable to register MBean Server", e);
				}
			}
		}
		return monitoringController;
	}

	// private Constructor
	private MonitoringController(final Configuration configuration) {
		// MBean
		ObjectName objectname = null;
		if (configuration.getBooleanProperty(Configuration.ACTIVATE_MBEAN)) {
			try {
				objectname = new ObjectName(configuration.getStringProperty(Configuration.ACTIVATE_MBEAN_DOMAIN), "type",
						configuration.getStringProperty(Configuration.ACTIVATE_MBEAN_TYPE));
			} catch (final Exception e) {
				MonitoringController.log.warn("Failed to initialize ObjectName", e);
			}
		}
		this.objectname = objectname;
		this.stateController = new StateController(configuration);
		this.writerController = new WriterController(configuration, stateController);
		this.samplingController = new SamplingController(configuration, writerController);
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
		synchronized (this) {
			if (objectname != null) {
				try {
					ManagementFactory.getPlatformMBeanServer().unregisterMBean(objectname);
				} catch (final Exception e) {
					MonitoringController.log.error("Failed to terminate MBean", e);
				}
			}
		}
		samplingController.terminate();
		writerController.terminate();
	}

	@Override
	protected final void getState(final StringBuilder sb) {
		sb.append("Current State of kieker.monitoring (");
		sb.append(MonitoringController.getVersion());
		sb.append("): ");
		stateController.getState(sb);
		if (this.objectname != null) {
			sb.append("\tMBean available: ");
			sb.append(this.objectname.getCanonicalName());
			sb.append("\n");
		}
		writerController.getState(sb);
		samplingController.getState(sb);
	}

	@Override
	public final String getState() {
		final StringBuilder sb = new StringBuilder();
		getState(sb);
		return sb.toString();
	}

	// DELEGATE TO OTHER CONTROLLERS
	// #############################

	@Override
	public final boolean terminateMonitoring() {
		return stateController.terminateMonitoring();
	}

	@Override
	public final boolean isMonitoringTerminated() {
		return stateController.isMonitoringTerminated();
	}

	@Override
	public final boolean enableMonitoring() {
		return stateController.enableMonitoring();
	}

	@Override
	public final boolean disableMonitoring() {
		return stateController.disableMonitoring();
	}

	@Override
	public final boolean isMonitoringEnabled() {
		return stateController.isMonitoringEnabled();
	}

	@Override
	public final String getName() {
		return stateController.getName();
	}

	@Override
	public final String getHostName() {
		return stateController.getHostName();
	}

	@Override
	public final int incExperimentId() {
		return stateController.incExperimentId();
	}

	@Override
	public final void setExperimentId(final int newExperimentID) {
		stateController.setExperimentId(newExperimentID);
	}

	@Override
	public final int getExperimentId() {
		return stateController.getExperimentId();
	}

	@Override
	public final boolean newMonitoringRecord(final IMonitoringRecord record) {
		return writerController.newMonitoringRecord(record);
	}

	@Override
	public final IMonitoringWriter getMonitoringWriter() {
		return writerController.getMonitoringWriter();
	}

	@Override
	public final long getNumberOfInserts() {
		return writerController.getNumberOfInserts();
	}

	@Override
	public final ITimeSource getTimeSource() {
		return writerController.getTimeSource();
	}

	@Override
	public final ScheduledSamplerJob schedulePeriodicSampler(final ISampler sampler, final long initialDelay, final long period, final TimeUnit timeUnit) {
		return samplingController.schedulePeriodicSampler(sampler, initialDelay, period, timeUnit);
	}

	@Override
	public final boolean removeScheduledSampler(final ScheduledSamplerJob sampler) {
		return samplingController.removeScheduledSampler(sampler);
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
