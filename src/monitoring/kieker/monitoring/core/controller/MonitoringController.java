/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.monitoring.core.controller;

import java.util.concurrent.TimeUnit;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.Version;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.sampler.ISampler;
import kieker.monitoring.core.sampler.ScheduledSamplerJob;
import kieker.monitoring.timer.ITimeSource;

/**
 * @author Jan Waller
 */
public final class MonitoringController extends AbstractController implements IMonitoringController {
	private static final Log LOG = LogFactory.getLog(MonitoringController.class);

	private final StateController stateController;
	private final JMXController jmxController;
	private final WriterController writerController;
	private final SamplingController samplingController;
	private final TimeSourceController timeSourceController;
	private final RegistryController registryController;

	// private Constructor
	private MonitoringController(final Configuration configuration) {
		super(configuration);
		this.stateController = new StateController(configuration);
		this.jmxController = new JMXController(configuration);
		this.writerController = new WriterController(configuration);
		this.samplingController = new SamplingController(configuration);
		this.timeSourceController = new TimeSourceController(configuration);
		this.registryController = new RegistryController(configuration);
	}

	// FACTORY
	public static final IMonitoringController createInstance(final Configuration configuration) {
		final MonitoringController monitoringController = new MonitoringController(configuration);
		// Initialize and handle early Termination (once for each Controller!)
		monitoringController.stateController.setMonitoringController(monitoringController);
		if (monitoringController.stateController.isTerminated()) {
			monitoringController.terminate();
			return monitoringController;
		}
		monitoringController.jmxController.setMonitoringController(monitoringController);
		if (monitoringController.jmxController.isTerminated()) {
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
		monitoringController.registryController.setMonitoringController(monitoringController);
		if (monitoringController.registryController.isTerminated()) {
			monitoringController.terminate();
			return monitoringController;
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
						// WONTFIX: We should not use a logger in shutdown hooks, logger may already be down! (#26)
						MonitoringController.LOG.info("ShutdownHook notifies controller to initiate shutdown");
						// System.err.println(monitoringController.toString());
						monitoringController.terminateMonitoring();
					}
				}
			});
		} catch (final Exception e) { // NOPMD NOCS (Exception)
			MonitoringController.LOG.warn("Failed to add shutdownHook");
		}
		MonitoringController.LOG.info(monitoringController.toString());
		return monitoringController;
	}

	/**
	 * Return the version name of this controller instance.
	 * 
	 * @return the version name
	 */
	public static final String getVersion() {
		return Version.getVERSION();
	}

	@Override
	protected final void init() {
		// do nothing
	}

	@Override
	protected final void cleanup() {
		MonitoringController.LOG.info("Shutting down Monitoring Controller (" + this.getName() + ")");
		this.stateController.terminate();
		this.jmxController.terminate();
		this.timeSourceController.terminate();
		this.samplingController.terminate();
		this.writerController.terminate();
		this.registryController.terminate();
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Current State of kieker.monitoring (");
		sb.append(MonitoringController.getVersion());
		sb.append(") ");
		sb.append(this.stateController.toString());
		sb.append(this.jmxController.toString());
		sb.append(this.registryController.toString());
		sb.append(this.timeSourceController.toString());
		sb.append(this.writerController.toString());
		sb.append(this.samplingController.toString());
		return sb.toString();
	}

	// DELEGATE TO OTHER CONTROLLERS
	// #############################

	public final boolean terminateMonitoring() {
		return this.stateController.terminateMonitoring();
	}

	public final boolean isMonitoringTerminated() {
		return this.stateController.isMonitoringTerminated();
	}

	public final boolean enableMonitoring() {
		return this.stateController.enableMonitoring();
	}

	public final boolean disableMonitoring() {
		return this.stateController.disableMonitoring();
	}

	public final boolean isMonitoringEnabled() {
		return this.stateController.isMonitoringEnabled();
	}

	public final String getName() {
		return this.stateController.getName();
	}

	public final String getHostname() {
		return this.stateController.getHostname();
	}

	public final int incExperimentId() {
		return this.stateController.incExperimentId();
	}

	public final void setExperimentId(final int newExperimentID) {
		this.stateController.setExperimentId(newExperimentID);
	}

	public final int getExperimentId() {
		return this.stateController.getExperimentId();
	}

	public final boolean newMonitoringRecord(final IMonitoringRecord record) {
		return this.writerController.newMonitoringRecord(record);
	}

	public final long getNumberOfInserts() {
		return this.writerController.getNumberOfInserts();
	}

	public final ScheduledSamplerJob schedulePeriodicSampler(final ISampler sampler, final long initialDelay, final long period, final TimeUnit timeUnit) {
		return this.samplingController.schedulePeriodicSampler(sampler, initialDelay, period, timeUnit);
	}

	public final boolean removeScheduledSampler(final ScheduledSamplerJob sampler) {
		return this.samplingController.removeScheduledSampler(sampler);
	}

	public final ITimeSource getTimeSource() {
		return this.timeSourceController.getTimeSource();
	}

	public final String getJMXDomain() {
		return this.jmxController.getJMXDomain();
	}

	public int getIdForString(final String string) {
		return this.registryController.getIdForString(string);
	}

	// GET SINGLETON INSTANCE
	// #############################
	public static final IMonitoringController getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * SINGLETON
	 */
	private static final class LazyHolder { // NOCS
		private static final IMonitoringController INSTANCE = MonitoringController.createInstance(ConfigurationFactory.createSingletonConfiguration());
	}
}
