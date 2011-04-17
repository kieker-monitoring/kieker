package kieker.monitoring.core;

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

/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */
/**
 * @author Jan Waller
 */
public final class MonitoringController implements IMonitoringController, MonitoringControllerMBean {
	private static final Log log = LogFactory.getLog(MonitoringController.class);

	private final ISamplingController samplingController;
	private final IWriterController writerController;
	private final IMonitoringControllerState controller;

	/**
	 * Name of the MBean.
	 */
	private final ObjectName objectname;

	/**
	 * @return the singleton instance of Kieker
	 */
	public final static IMonitoringController getInstance() {
		return LazyHolder.INSTANCE;
	}

	protected MonitoringController(final IMonitoringControllerState controller, final IWriterController writerController,
			final ISamplingController samplingController, final ObjectName name) {
		this.controller = controller;
		this.writerController = writerController;
		this.samplingController = samplingController;
		this.objectname = name;

	}

	//	protected MonitoringController(final Configuration configuration) {
	//		if (isMonitoringTerminated()) {
	//			this.objectname = null;
	//			MonitoringController.log.error("Controller (" + this.getName() +") initializsation failed\n" + getState());
	//			final StringBuilder sb = new StringBuilder("Configuration:");
	//			final Set<String> keys = configuration.stringPropertyNames();
	//			if (keys.isEmpty()) {
	//				sb.append("\n\tNo Configuration found");
	//			} else {
	//				for (String property : keys) {
	//					sb.append("\n\t");
	//					sb.append(property);
	//					sb.append("='");
	//					sb.append(configuration.getProperty(property));
	//					sb.append("'");
	//				}
	//			}
	//			MonitoringController.log.error(sb.toString());
	//			return;
	//		}
	//		// register MBean
	//		ObjectName objectname = null;
	//		if (configuration.getBooleanProperty(Configuration.ACTIVATE_MBEAN)) {
	//			try {
	//				objectname = new ObjectName(
	//						configuration.getStringProperty(Configuration.ACTIVATE_MBEAN_DOMAIN),
	//						"type",
	//						configuration.getStringProperty(Configuration.ACTIVATE_MBEAN_TYPE));
	//				ManagementFactory.getPlatformMBeanServer().registerMBean(this, objectname);
	//			} catch (final MalformedObjectNameException ex) {
	//				log.warn("Unable to register MBean (constructed ObjectName malformated)! Check the following configuration values '" +
	//						Configuration.ACTIVATE_MBEAN_DOMAIN + "=" + configuration.getStringProperty(Configuration.ACTIVATE_MBEAN_DOMAIN) + "' and '" +
	//						Configuration.ACTIVATE_MBEAN_TYPE + "=" + configuration.getStringProperty(Configuration.ACTIVATE_MBEAN_TYPE) + "'");
	//				objectname = null;
	//			} catch (final Exception ex) {
	//				log.warn("Unable to register MBean", ex);
	//				objectname = null;
	//			}
	//		}
	//		this.objectname = objectname;
	//		// finished initialization
	//		MonitoringController.log.info("Controller (" + this.getName() +") initializsation finished\n" + getState());
	//	}

	@Override
	public final boolean newMonitoringRecord(final IMonitoringRecord record) {
		return this.writerController.newMonitoringRecord(record);
	}

	@Override
	public final IMonitoringWriter getMonitoringWriter() {
		return this.writerController.getMonitoringWriter();
	}

	@Override
	public boolean isWritingEnabled() {
		return this.writerController.isWritingEnabled();
	}

	@Override
	public void setWritingEnabled(final boolean enableWriting) {
		this.writerController.setWritingEnabled(enableWriting);
	}
	
	@Override
	public final long getNumberOfInserts() {
		return this.writerController.getNumberOfInserts();
	}

	@Override
	public final IMonitoringControllerState getControllerConfig() {
		return this.writerController.getControllerConfig();
	}

	@Override
	public final boolean isMonitoringTerminated() {
		return this.controller.isMonitoringTerminated();
	}

	@Override
	public final String getName() {
		return this.controller.getName();
	}

	@Override
	public final String getHostName() {
		return this.controller.getHostName();
	}

	@Override
	public final int incExperimentId() {
		return this.controller.incExperimentId();
	}

	@Override
	public final void setExperimentId(final int newExperimentID) {
		this.controller.setExperimentId(newExperimentID);
	}

	@Override
	public final int getExperimentId() {
		return this.controller.getExperimentId();
	}

	@Override
	public final void setDebug(final boolean debug) {
		this.controller.setDebug(debug);
	}

	@Override
	public final boolean isDebug() {
		return this.controller.isDebug();
	}

	@Override
	public final boolean terminateMonitoring() {
		boolean res = this.controller.terminateMonitoring();
		if(res) {
			res &= this.samplingController.terminateMonitoring();
		}
		if(res) {
			res &= this.writerController.terminateMonitoring();
		}
		if(res) {
			if (this.objectname != null) {
				try {
					ManagementFactory.getPlatformMBeanServer().unregisterMBean(this.objectname);
				} catch (final Exception ex) {
					MonitoringController.log.error("Failed to terminate MBean", ex);
				}
			}
			MonitoringController.log.info("Controller (" + this.getName() +") shutdown completed");
			return true;
		}
		return false;
	}

	@Override
	public final String getState() {
		final StringBuilder sb = new StringBuilder();
		this.getState(sb);
		return sb.toString();
	}

	@Override
	public final void getState(final StringBuilder sb) {
		sb.append("Current State of kieker.monitoring (");
		sb.append(MonitoringController.getVersion());
		sb.append(")");
		if (this.objectname != null) {
			sb.append("(MBean available: ");
			sb.append(this.objectname.getCanonicalName());
			sb.append(")");
		}
		sb.append(":\n");
		this.samplingController.getState(sb);
		this.writerController.getState(sb);
		this.controller.getState(sb);
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
	public final ITimeSource getTimeSource() {
		return this.writerController.getTimeSource();
	}


	@Override
	public final void enableRealtimeMode() {
		this.writerController.enableRealtimeMode();
	}

	@Override
	public final boolean isRealtimeMode() {
		return this.writerController.isRealtimeMode();
	}

	@Override
	public final void enableReplayMode() {
		this.writerController.enableReplayMode();
	}

	@Override
	public final boolean isReplayMode() {
		return this.writerController.isReplayMode();
	}

	@Override
	public final ScheduledSamplerJob schedulePeriodicSampler(final ISampler sampler,
			final long initialDelay, final long period, final TimeUnit timeUnit) {
		return this.samplingController.schedulePeriodicSampler(sampler,
				initialDelay, period, timeUnit);
	}

	@Override
	public final boolean removeScheduledSampler(final ScheduledSamplerJob sampler) {
		return this.samplingController.removeScheduledSampler(sampler);
	}

	/**
	 * SINGLETON
	 */
	private final static class LazyHolder {
		static {
			MonitoringController.log.info("Initialization started");
			IMonitoringController tmp = null;
			try {
				tmp = MonitoringControllerFactory.createInstance(Configuration.createSingletonConfiguration());
			} catch (final Exception e) {
				MonitoringController.log.error("Something went wrong initializing the Controller", e);
			} finally {
				INSTANCE = tmp;
			}
		}
		private static final IMonitoringController INSTANCE;
	}
}
