/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.KiekerMetadataRecord;
import kieker.common.util.Version;
import kieker.monitoring.core.configuration.ConfigurationConstants;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.sampler.ISampler;
import kieker.monitoring.core.sampler.ScheduledSamplerJob;
import kieker.monitoring.timer.ITimeSource;

/**
 * @author Jan Waller
 *
 * @since 1.3
 */
public final class MonitoringController extends AbstractController implements IMonitoringController, IStateListener {
	static final Logger LOGGER = LoggerFactory.getLogger(MonitoringController.class); // NOPMD package for inner class

	/**
	 * Number of milliseconds the ShutdownHook waits after being triggered to send
	 * the termination signal to the MonitoringController. The reason for
	 * introducing this was that a shutdown might be initiated before all records
	 * (and particularly mapping enries) are written. #1634
	 */
	private static final long SHUTDOWN_DELAY_MILLIS = 1000;

	private final StateController stateController;
	private final SamplingController samplingController;
	private final JMXController jmxController;
	private final TCPController tcpController;
	private final WriterController writerController;
	private final TimeSourceController timeSourceController;
	private final ProbeController probeController;
	/**
	 * Whether or not the {@link IMonitoringRecord#setLoggingTimestamp(long)} is
	 * automatically set.
	 */
	private final boolean autoSetLoggingTimestamp;

	// private Constructor
	private MonitoringController(final Configuration configuration) {
		super(configuration);
		this.stateController = new StateController(configuration);
		this.samplingController = new SamplingController(configuration);
		this.jmxController = new JMXController(configuration);
		this.tcpController = new TCPController(configuration, this);
		this.writerController = new WriterController(configuration);
		this.stateController.setStateListener(this);
		this.timeSourceController = new TimeSourceController(configuration);
		this.probeController = new ProbeController(configuration);
		this.autoSetLoggingTimestamp = configuration.getBooleanProperty(ConfigurationConstants.AUTO_SET_LOGGINGTSTAMP);
	}

	// FACTORY
	/**
	 * This is a factory method creating a new monitoring controller instance using
	 * the given configuration.
	 *
	 * @param configuration
	 *            The configuration for the new controller.
	 *
	 * @return A new controller.
	 */
	public static MonitoringController createInstance(final Configuration configuration) {
		final MonitoringController monitoringController;
		// try {
		monitoringController = new MonitoringController(configuration);
		// } catch (final IllegalStateException e) {
		// // WriterControllerHstr throws an IllegalStateException upon an invalid
		// configuration
		// // TODO all controllers should throw an exception upon an invalid
		// configuration
		// return new TerminatedMonitoringController(configuration); // NullObject
		// pattern
		// }

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
		monitoringController.tcpController.setMonitoringController(monitoringController);
		if (monitoringController.tcpController.isTerminated()) {
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
		monitoringController.probeController.setMonitoringController(monitoringController);
		if (monitoringController.probeController.isTerminated()) {
			monitoringController.terminate();
		}
		monitoringController.setMonitoringController(monitoringController);
		if (monitoringController.isTerminated()) {
			return monitoringController;
		}

		// must be called after all sub controllers has been initialized
		if (monitoringController.isMonitoringEnabled()) {
			monitoringController.enableMonitoring(); // notifies the listener
		}

		if (configuration.getBooleanProperty(ConfigurationConstants.USE_SHUTDOWN_HOOK)) {
			// This ensures that the terminateMonitoring() method is always called before
			// shutting down the JVM. This method ensures that necessary cleanup steps are
			// finished and no information is lost due to asynchronous writers.
			try {
				Runtime.getRuntime().addShutdownHook(new Thread() {

					@Override
					public void run() {
						if (!monitoringController.isMonitoringTerminated()) {
							// WONTFIX: We should not use a logger in shutdown hooks, logger may already be
							// down! (#26)
							MonitoringController.LOGGER.info("ShutdownHook notifies controller to initiate shutdown.");
							monitoringController.terminateMonitoring();
							try {
								monitoringController.waitForTermination(MonitoringController.SHUTDOWN_DELAY_MILLIS);
							} catch (final InterruptedException e) {
								// ignore since we cannot do anything at this point
								MonitoringController.LOGGER.warn("Shutdown was interrupted while waiting");
							}
							// LOG.info("ShutdownHook has finished."); // does not work anymore
							// System.out.println("ShutdownHook has finished."); // works!
						}
					}
				});
			} catch (final Exception e) { // NOPMD NOCS (Exception)
				MonitoringController.LOGGER.warn("Failed to add shutdownHook");
			}
		} else {
			MonitoringController.LOGGER.warn("Shutdown Hook is disabled, loss of monitoring data might occur.");
		}
		MonitoringController.LOGGER.info(monitoringController.toString());
		return monitoringController;
	}

	/**
	 * Return the version name of this controller instance.
	 *
	 * @return the version name
	 */
	public static String getVersion() {
		return Version.getVERSION();
	}

	@Override
	public void beforeEnableMonitoring() {
		if (this.writerController.isLogMetadataRecord()) {
			this.sendMetadataAsRecord();
		}
	}

	@Override
	protected void init() {
		// do nothing
	}

	@Override
	protected void cleanup() {
		MonitoringController.LOGGER.info("Shutting down Monitoring Controller ({})", this.getName());
		// this.saveMetadataAsRecord();
		this.probeController.terminate();
		this.timeSourceController.terminate();
		this.writerController.terminate();
		this.jmxController.terminate();
		this.tcpController.terminate();
		this.samplingController.terminate();
		this.stateController.terminate();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(2048).append("Current State of kieker.monitoring (")
				.append(MonitoringController.getVersion()).append(") ").append(this.stateController.toString())
				.append(this.jmxController.toString()).append(this.timeSourceController.toString())
				.append(this.probeController.toString()).append(this.writerController.toString())
				//.append("\n\tAutomatic assignment of logging timestamps: '").append(this.autoSetLoggingTimestamp)
				.append("'\n").append(this.samplingController.toString());
		return sb.toString();
	}

	/**
	 * This method sends the meta data (like the controller and host name, the
	 * experiment ID, etc.) as a record.
	 *
	 * @return true on success; false in case of an error.
	 */
	@Override
	public boolean sendMetadataAsRecord() {
		final ITimeSource timesource = this.getTimeSource();
		return this.newMonitoringRecord(new KiekerMetadataRecord(null, // Kieker version will be filled in
				this.getName(), // controllerName
				this.getHostname(), // hostname
				this.getExperimentId(), // experimentId
				this.isDebug(), // debugMode
				timesource.getOffset(), // timeOffset
				timesource.getTimeUnit().name(), // timeUnit
				0 // number of inserts (0 since not supported anymore)
		));
	}

	protected SamplingController getSamplingController() {
		return this.samplingController;
	}

	// DELEGATE TO OTHER CONTROLLERS
	// #############################

	@Override
	public boolean terminateMonitoring() {
		MonitoringController.LOGGER.info("Terminating monitoring...");
		return this.stateController.terminateMonitoring();
	}

	@Override
	public boolean isMonitoringTerminated() {
		return this.stateController.isMonitoringTerminated();
	}

	@Override
	public boolean enableMonitoring() {
		return this.stateController.enableMonitoring();
	}

	@Override
	public boolean disableMonitoring() {
		return this.stateController.disableMonitoring();
	}

	@Override
	public boolean isMonitoringEnabled() {
		return this.stateController.isMonitoringEnabled();
	}

	@Override
	public boolean isDebug() {
		return this.stateController.isDebug();
	}

	@Override
	public String getName() {
		return this.stateController.getName();
	}

	@Override
	public String getHostname() {
		return this.stateController.getHostname();
	}

	@Override
	public String getApplicationName() {
		return this.stateController.getApplicationName();
	}

	@Override
	public int incExperimentId() {
		return this.stateController.incExperimentId();
	}

	@Override
	public void setExperimentId(final int newExperimentID) {
		this.stateController.setExperimentId(newExperimentID);
	}

	@Override
	public int getExperimentId() {
		return this.stateController.getExperimentId();
	}

	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		if (!this.isMonitoringEnabled()) { // enabled and not terminated
			return true;
		}
		/*if (this.autoSetLoggingTimestamp) {
			record.setLoggingTimestamp(this.getTimeSource().getTime());
		}*/
		return this.writerController.newMonitoringRecord(record);
	}

	@Override
	public void waitForTermination(final long timeoutInMs) throws InterruptedException {
		this.writerController.waitForTermination(timeoutInMs);
	}

	@Override
	public ScheduledSamplerJob schedulePeriodicSampler(final ISampler sampler, final long initialDelay,
			final long period, final TimeUnit timeUnit) {
		return this.samplingController.schedulePeriodicSampler(sampler, initialDelay, period, timeUnit);
	}

	@Override
	public boolean removeScheduledSampler(final ScheduledSamplerJob sampler) {
		return this.samplingController.removeScheduledSampler(sampler);
	}

	@Override
	public ITimeSource getTimeSource() {
		return this.timeSourceController.getTimeSource();
	}

	@Override
	public String getControllerDomain() {
		return this.jmxController.getControllerDomain();
	}

	@Override
	public boolean activateProbe(final String pattern) {
		return this.probeController.activateProbe(pattern);
	}

	@Override
	public boolean deactivateProbe(final String pattern) {
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

	@Override
	public Map<String, List<String>> getAllPatternParameters(final String pattern) {
		return this.probeController.getAllPatternParameters(pattern);
	}

	@Override
	public void deletePatternParameter(final String pattern, final String parameterName) {
		this.probeController.deletePatternParameter(pattern, parameterName);
	}

	@Override
	public void clearPatternParameters(final String pattern) {
		this.probeController.clearPatternParameters(pattern);
	}

	@Override
	public void addPatternParameter(final String pattern, final String parameterName, final List<String> parameters) {
		this.probeController.addPatternParameter(pattern, parameterName, parameters);
	}

	@Override
	public void addPatternParameterValue(final String pattern, final String name, final String value) {
		this.probeController.addPatternParameterValue(pattern, name, value);
	}

	@Override
	public void removePatternParameterValue(final String pattern, final String name, final String value) {
		this.probeController.removePatternParameterValue(pattern, name, value);
	}

	// GET SINGLETON INSTANCE
	// #############################
	public static IMonitoringController getInstance() {
		return SingletonHelper.INSTANCE;
	}

	/**
	 * SINGLETON.
	 */
	private static final class SingletonHelper { // NOCS
		static final IMonitoringController INSTANCE = MonitoringController // NOPMD default modifier
				.createInstance(ConfigurationFactory.createSingletonConfiguration()); // NOPMD package
	}

}
