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

package kieker.tools.resourceMonitor;

import java.io.File;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.sampler.ISampler;
import kieker.monitoring.sampler.sigar.ISigarSamplerFactory;
import kieker.monitoring.sampler.sigar.SigarSamplerFactory;
import kieker.tools.AbstractCommandLineTool;

/**
 * This tool can be used to monitor system resources.
 *
 * @author Teerat Pitakrat
 *
 * @since 1.12
 */
public final class ResourceMonitor extends AbstractCommandLineTool {

	private static final Log LOG = LogFactory.getLog(ResourceMonitor.class);

	private static final String CMD_OPT_NAME_MONITORING_CONFIGURATION = "monitoring.configuration";

	private static final String OPTION_EXAMPLE_FILE_MONITORING_PROPERTIES = File.separator + "path" + File.separator + "to" + File.separator
			+ "monitoring.properties";

	private volatile IMonitoringController monitoringController;

	private volatile long interval = 15;
	private volatile TimeUnit intervalUnit = TimeUnit.SECONDS;

	private volatile long initialDelay;
	private volatile TimeUnit initialDelayUnit = TimeUnit.SECONDS;

	private volatile long duration = -1;
	private volatile TimeUnit durationUnit = TimeUnit.MINUTES;

	private volatile String monitoringConfigurationFile;

	private ResourceMonitor() {
		super(true);
	}

	public static void main(final String[] args) {
		new ResourceMonitor().start(args);
	}

	private ISampler[] createSamplers() {
		final ISigarSamplerFactory sigarFactory = SigarSamplerFactory.INSTANCE;
		return new ISampler[] { sigarFactory.createSensorCPUsDetailedPerc(), sigarFactory.createSensorMemSwapUsage(), sigarFactory.createSensorLoadAverage() };
	}

	private void initSensors() {
		final ISampler[] samplers = this.createSamplers();
		for (final ISampler sampler : samplers) {
			this.monitoringController.schedulePeriodicSampler(sampler,
					TimeUnit.SECONDS.convert(this.initialDelay, this.initialDelayUnit), this.interval,
					this.intervalUnit);
		}
	}

	@Override
	protected void addAdditionalOptions(final Options options) {
		final Option intervalOption = new Option(null, "interval", true, "Sampling interval");
		intervalOption.setArgName("interval");
		intervalOption.setRequired(false);
		intervalOption.setArgs(1);
		options.addOption(intervalOption);

		final Option intervalUnitOption = new Option(null, "interval-unit", true, "Sampling interval time unit (default: SECONDS)");
		intervalUnitOption.setArgName("interval-unit");
		intervalUnitOption.setRequired(false);
		intervalUnitOption.setArgs(1);
		options.addOption(intervalUnitOption);

		final Option initialDelayOption = new Option(null, "initial-delay", true, "Initial delay");
		initialDelayOption.setArgName("initial-delay");
		initialDelayOption.setRequired(false);
		initialDelayOption.setArgs(1);
		options.addOption(initialDelayOption);

		final Option durationUnitOption = new Option(null, "initial-delay-unit", true, "Initial delay time unit (default: SECONDS)");
		durationUnitOption.setArgName("initial-delay-unit");
		durationUnitOption.setRequired(false);
		durationUnitOption.setArgs(1);
		options.addOption(durationUnitOption);

		final Option initialDelayUnitOption = new Option(null, "duration", true, "Monitoring duration");
		initialDelayUnitOption.setArgName("duration");
		initialDelayUnitOption.setRequired(false);
		initialDelayUnitOption.setArgs(1);
		options.addOption(initialDelayUnitOption);

		final Option durationOption = new Option(null, "duration-unit", true, "Monitoring duration time unit (default: MINUTES)");
		durationOption.setArgName("duration-unit");
		durationOption.setRequired(false);
		durationOption.setArgs(1);
		options.addOption(durationOption);

		final Option configurationFileOption = new Option("c", CMD_OPT_NAME_MONITORING_CONFIGURATION, true,
				"Configuration to use for the Kieker monitoring instance");
		configurationFileOption.setArgName(OPTION_EXAMPLE_FILE_MONITORING_PROPERTIES);
		configurationFileOption.setRequired(false);
		configurationFileOption.setValueSeparator('=');
		options.addOption(configurationFileOption);
	}

	@Override
	protected boolean readPropertiesFromCommandLine(final CommandLine commandLine) {
		final String intervalStr = commandLine.getOptionValue("interval");
		if (intervalStr != null) {
			try {
				this.interval = Long.parseLong(intervalStr);
			} catch (final NumberFormatException ex) {
				LOG.error("Failed to parse interval: " + intervalStr, ex);
				return false;
			}
		}

		final String intervalUnitStr = commandLine.getOptionValue("interval-unit");
		if (intervalUnitStr != null) {
			try {
				this.intervalUnit = TimeUnit.valueOf(intervalUnitStr.toUpperCase(Locale.US));
			} catch (final IllegalArgumentException ex) {
				LOG.error("Failed to parse interval unit: " + intervalUnitStr, ex);
				return false;
			} catch (final NullPointerException ex) { // NOPMD (AvoidCatchingGenericException)
				LOG.error("No interval unit passed as argument", ex);
				return false;
			}
		}

		final String initialDelayStr = commandLine.getOptionValue("initial-delay");
		if (initialDelayStr != null) {
			try {
				this.initialDelay = Long.parseLong(initialDelayStr);
			} catch (final NumberFormatException ex) {
				LOG.error("Failed to parse initial delay: " + initialDelayStr, ex);
				return false;
			}
		}

		final String initialDelayUnitStr = commandLine.getOptionValue("initial-delay-unit");
		if (initialDelayUnitStr != null) {
			try {
				this.initialDelayUnit = TimeUnit.valueOf(initialDelayUnitStr.toUpperCase(Locale.US));
			} catch (final IllegalArgumentException ex) {
				LOG.error("Failed to parse initial delay unit: " + initialDelayUnitStr, ex);
				return false;
			} catch (final NullPointerException ex) { // NOPMD (AvoidCatchingGenericException)
				LOG.error("No initial delay unit passed as argument", ex);
				return false;
			}
		}

		final String durationStr = commandLine.getOptionValue("duration");
		if (durationStr != null) {
			try {
				this.duration = Long.parseLong(durationStr);
			} catch (final NumberFormatException ex) {
				LOG.error("Failed to parse duration: " + durationStr, ex);
				return false;
			}
		}

		final String durationUnitStr = commandLine.getOptionValue("duration-unit");
		if (durationUnitStr != null) {
			try {
				this.durationUnit = TimeUnit.valueOf(durationUnitStr.toUpperCase(Locale.US));
			} catch (final IllegalArgumentException ex) {
				LOG.error("Failed to parse duration unit: " + durationUnitStr, ex);
				return false;
			} catch (final NullPointerException ex) { // NOPMD (AvoidCatchingGenericException)
				LOG.error("No duration unit passed as argument", ex);
				return false;
			}
		}

		this.monitoringConfigurationFile = commandLine.getOptionValue(CMD_OPT_NAME_MONITORING_CONFIGURATION);

		return true;
	}

	@Override
	protected boolean performTask() {
		LOG.info(this.toString());

		final CountDownLatch cdl = new CountDownLatch(1);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				cdl.countDown();
			}
		});

		final Configuration controllerConfiguration;
		if (this.monitoringConfigurationFile != null) {
			controllerConfiguration = ConfigurationFactory.createConfigurationFromFile(this.monitoringConfigurationFile);
		} else {
			controllerConfiguration = ConfigurationFactory.createSingletonConfiguration();
		}
		this.monitoringController = MonitoringController.createInstance(controllerConfiguration);

		this.initSensors();
		LOG.info("Monitoring started");

		if (this.duration >= 0) {
			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					cdl.countDown();
					timer.cancel();
				}
			}, TimeUnit.MILLISECONDS.convert(this.duration, this.durationUnit));
			LOG.info("Waiting for " + this.duration + " " + this.durationUnit + " timeout...");
		}

		try {
			LOG.info("Press Ctrl+c to terminate");
			cdl.await();
		} catch (final InterruptedException ex) {
			LOG.warn("The monitoring has been interrupted", ex);
			return false;
		} finally {
			LOG.info("Monitoring terminated");
		}

		return true;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(2048);
		final String lineSeparator = System.getProperty("line.separator");
		sb.append("Resource Monitoring Configuration:");
		sb.append(lineSeparator);
		sb.append("\tSampling interval = ");
		sb.append(this.interval);
		sb.append(lineSeparator);
		sb.append("\tSampling interval unit = ");
		sb.append(this.intervalUnit);
		sb.append(lineSeparator);
		sb.append("\tInitial delay = ");
		sb.append(this.initialDelay);
		sb.append(lineSeparator);
		sb.append("\tInitial delay unit = ");
		sb.append(this.initialDelayUnit);
		sb.append(lineSeparator);
		if (this.duration < 0) {
			sb.append("\tDuration = INFINITE");
			sb.append(lineSeparator);
		} else {
			sb.append("\tDuration = ");
			sb.append(this.duration);
			sb.append(lineSeparator);
			sb.append("\tDuration unit = ");
			sb.append(this.durationUnit);
			sb.append(lineSeparator);
		}
		return sb.toString();
	}
}
