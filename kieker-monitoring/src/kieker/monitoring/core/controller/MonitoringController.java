/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

import java.util.List;
import java.util.concurrent.TimeUnit;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.KiekerMetadataRecord;
import kieker.common.util.Version;
import kieker.common.util.registry.IRegistry;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.sampler.ISampler;
import kieker.monitoring.core.sampler.ScheduledSamplerJob;
import kieker.monitoring.timer.ITimeSource;

/**
 * @author Jan Waller
 * 
 * @since 1.3
 */
public final class MonitoringController extends AbstractController implements IMonitoringController {
	static final Log LOG = LogFactory.getLog(MonitoringController.class); // NOPMD package for inner class

	/**
	 * Number of milliseconds the ShutdownHook waits after being triggered to send the termination signal
	 * to the MonitoringController. The reason for introducing this was that a shutdown might be initiated
	 * before all records (and particularly mapping enries) are written. #1634
	 */
	private static final long SHUTDOWN_DELAY_MILLIS = 1000;

	private final StateController stateController;
	private final SamplingController samplingController;
	private final JMXController jmxController;
	private final WriterController writerController;
	private final TimeSourceController timeSourceController;
	private final RegistryController registryController;
	private final ProbeController probeController;

	// private Constructor
	private MonitoringController(final Configuration configuration) {
		super(configuration);
		this.stateController = new StateController(configuration);
		this.samplingController = new SamplingController(configuration);
		this.jmxController = new JMXController(configuration);
		this.writerController = new WriterController(configuration);
		this.timeSourceController = new TimeSourceController(configuration);
		this.registryController = new RegistryController(configuration);
		this.probeController = new ProbeController(configuration);
	}

	// FACTORY
	/**
	 * This is a factory method creating a new monitoring controller instance using the given configuration.
	 * 
	 * @param configuration
	 *            The configuration for the new controller.
	 * 
	 * @return A new controller.
	 */
	public static final IMonitoringController createInstance(final Configuration configuration) {
		final MonitoringController monitoringController = new MonitoringController(configuration);
		// Initialize and handle early Termination (once for each Controller!)
		monitoringController.stateController.setMonitoringController(monitoringController);
		if (monitoringController.stateController.isTerminated()) {
			monitoringController.terminate();
		}
		monitoringController.samplingController.setMonitoringController(monitoringController);
		if (monitoringController.samplingController.isTerminated()) {
			monitoringController.terminate();
		}
		monitoringController.jmxController.setMonitoringController(monitoringController);
		if (monitoringController.jmxController.isTerminated()) {
			monitoringController.terminate();
		}
		monitoringController.writerController.setMonitoringController(monitoringController);
		if (monitoringController.writerController.isTerminated()) {
			monitoringController.terminate();
		}
		monitoringController.timeSourceController.setMonitoringController(monitoringController);
		if (monitoringController.timeSourceController.isTerminated()) {
			monitoringController.terminate();
		}
		monitoringController.registryController.setMonitoringController(monitoringController);
		if (monitoringController.registryController.isTerminated()) {
			monitoringController.terminate();
		}
		monitoringController.probeController.setMonitoringController(monitoringController);
		if (monitoringController.probeController.isTerminated()) {
			monitoringController.terminate();
		}
		monitoringController.setMonitoringController(monitoringController);
		if (monitoringController.isTerminated()) {
			return monitoringController;
		}

		if (configuration.getBooleanProperty(ConfigurationFactory.USE_SHUTDOWN_HOOK)) {
			// This ensures that the terminateMonitoring() method is always called before shutting down the JVM. This method ensures that necessary cleanup steps are
			// finished and no information is lost due to asynchronous writers.
			try {
				Runtime.getRuntime().addShutdownHook(new Thread() {

					@Override
					public void run() {
						if (!monitoringController.isMonitoringTerminated()) {
							// WONTFIX: We should not use a logger in shutdown hooks, logger may already be down! (#26)
							LOG.info("ShutdownHook notifies controller to initiate shutdown.");
							monitoringController.terminateMonitoring();
						}
					}
				});
			} catch (final Exception e) { // NOPMD NOCS (Exception)
				LOG.warn("Failed to add shutdownHook");
			}
		} else {
			LOG.warn("Shutdown Hook is disabled, loss of monitoring data might occur.");
		}
		LOG.info(monitoringController.toString());
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
		LOG.info("Shutting down Monitoring Controller (" + this.getName() + ")");
		// this.saveMetadataAsRecord();
		this.probeController.terminate();
		this.registryController.terminate();
		this.timeSourceController.terminate();
		this.writerController.terminate();
		this.jmxController.terminate();
		this.samplingController.terminate();
		this.stateController.terminate();
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder(2048);
		sb.append("Current State of kieker.monitoring (");
		sb.append(MonitoringController.getVersion());
		sb.append(") ");
		sb.append(this.stateController.toString());
		sb.append(this.jmxController.toString());
		sb.append(this.registryController.toString());
		sb.append(this.timeSourceController.toString());
		sb.append(this.probeController.toString());
		sb.append(this.writerController.toString());
		sb.append(this.samplingController.toString());
		return sb.toString();
	}

	/**
	 * This method sends the meta data (like the controller and host name, the experiment ID, etc.) as a record.
	 * 
	 * @return true on success; false in case of an error.
	 */
	@Override
	public final boolean sendMetadataAsRecord() {
		final ITimeSource timesource = this.getTimeSource();
		return this.newMonitoringRecord(new KiekerMetadataRecord(
				null, // Kieker version will be filled in
				this.getName(), // controllerName
				this.getHostname(), // hostname
				this.getExperimentId(), // experimentId
				this.isDebug(), // debugMode
				timesource.getOffset(), // timeOffset
				timesource.getTimeUnit().name(), // timeUnit
				this.getNumberOfInserts() // numberOfRecords
				));
	}

	protected SamplingController getSamplingController() {
		return this.samplingController;
	}

	// DELEGATE TO OTHER CONTROLLERS
	// #############################

	@Override
	public final boolean terminateMonitoring() {
		LOG.info("Controller shutting down in " + SHUTDOWN_DELAY_MILLIS + " milliseconds");
		// System.err.println(monitoringController.toString());
		try {
			Thread.sleep(SHUTDOWN_DELAY_MILLIS);
		} catch (final InterruptedException e) {
			LOG.warn("Shutdown was interrupted while waiting");
		}
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
	public final boolean isDebug() {
		return this.stateController.isDebug();
	}

	@Override
	public final String getName() {
		return this.stateController.getName();
	}

	@Override
	public final String getHostname() {
		return this.stateController.getHostname();
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

	@Override
	public final String getJMXDomain() {
		return this.jmxController.getJMXDomain();
	}

	@Override
	public final int getUniqueIdForString(final String string) {
		return this.registryController.getUniqueIdForString(string);
	}

	@Override
	public String getStringForUniqueId(final int id) {
		return this.registryController.getStringForUniqueId(id);
	}

	@Override
	public IRegistry<String> getStringRegistry() {
		return this.registryController.getStringRegistry();
	}

	@Override
	public final boolean activateProbe(final String pattern) {
		return this.probeController.activateProbe(pattern);
	}

	@Override
	public final boolean deactivateProbe(final String pattern) {
		return this.probeController.deactivateProbe(pattern);
	}

	@Override
	public boolean isProbeActivated(final String signature) {
		return this.probeController.isProbeActivated(signature);
	}

	@Override
	public void setProbePatternList(final List<String> patternList) {
		this.probeController.setProbePatternList(patternList);
	}

	@Override
	public List<String> getProbePatternList() {
		return this.probeController.getProbePatternList();
	}

	// GET SINGLETON INSTANCE
	// #############################
	public static final IMonitoringController getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * SINGLETON.
	 */
	private static final class LazyHolder { // NOCS
		static final IMonitoringController INSTANCE = MonitoringController.createInstance(ConfigurationFactory.createSingletonConfiguration()); // NOPMD package
	}
}
