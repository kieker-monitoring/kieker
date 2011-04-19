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
public final class MonitoringController extends MonitoringControllerState implements IMonitoringController, MonitoringControllerMBean {
	private final static Log log = LogFactory.getLog(MonitoringController.class);

	private final TerminationController terminationController;
	private final WriterController writerController;
	private final SamplingController samplingController;

	/** Name of the MBean. */
	private final ObjectName objectname;

	// FACTORY
	public final static IMonitoringController createInstance(final Configuration configuration) {
		final MonitoringController monitoringController = new MonitoringController(configuration);
		// Initialize and handle Termination
		monitoringController.terminationController.setMonitoringController(monitoringController);
		if (monitoringController.terminationController.isTerminated()) {
			monitoringController.terminateMonitoring();
			return monitoringController;
		}
		// Register MBean
		synchronized (monitoringController) {
			if ((monitoringController.objectname != null) && !monitoringController.isMonitoringTerminated()) {
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
		super(configuration);
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
		// TODO
		this.terminationController = new TerminationController();
		this.writerController = null;
		this.samplingController = null;
	}

	@Override
	public final boolean terminateMonitoring() {
		if (!monitoringTerminated.getAndSet(true)) {
			MonitoringController.log.info("Controller (" + getName() + ") shutting down");
			synchronized (this) {
				if (objectname != null) {
					try {
						ManagementFactory.getPlatformMBeanServer().unregisterMBean(objectname);
					} catch (final Exception e) {
						MonitoringController.log.error("Failed to terminate MBean", e);
					}
				}
			}
			super.terminate();
			if (samplingController != null) {
				samplingController.terminate();
			}
			if (writerController != null) {
				writerController.terminate();
			}
			MonitoringController.log.info("Controller (" + getName() + ") shutdown completed");
			return true;
		}
		return false;
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
	public final String getState() {
		final StringBuilder sb = new StringBuilder();
		super.getState(sb);
		if (this.objectname != null) {
			sb.append("\tMBean available: ");
			sb.append(this.objectname.getCanonicalName());
			sb.append("\n");
		}
		if (writerController != null) {
			writerController.getState(sb);
		}
		if (samplingController != null) {
			samplingController.getState(sb);
		}
		return sb.toString();
	}

	// DELEGATE TO OTHER CONTROLLERS
	// #############################

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
