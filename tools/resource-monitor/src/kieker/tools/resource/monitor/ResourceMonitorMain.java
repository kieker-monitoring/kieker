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

package kieker.tools.resource.monitor;

import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.sampler.ISampler;
import kieker.monitoring.sampler.oshi.IOshiSamplerFactory;
import kieker.monitoring.sampler.oshi.OshiSamplerFactory;
import kieker.tools.common.AbstractLegacyTool;

/**
 * This tool can be used to monitor system resources.
 *
 * @author Teerat Pitakrat
 *
 * @since 1.12
 */
public final class ResourceMonitorMain extends AbstractLegacyTool<Settings> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceMonitorMain.class);

	private volatile IMonitoringController monitoringController;

	public static void main(final String[] args) {
		final ResourceMonitorMain monitor = new ResourceMonitorMain();
		System.exit(monitor.run("resmon", "Resource Monitoring", args, new Settings()));
	}

	private ISampler[] createSamplers() {
		final IOshiSamplerFactory oshiFactory = OshiSamplerFactory.INSTANCE;
		return new ISampler[] { oshiFactory.createSensorCPUsDetailedPerc(), oshiFactory.createSensorMemSwapUsage(), oshiFactory.createSensorLoadAverage(),
			oshiFactory.createSensorNetworkUtilization(), oshiFactory.createSensorDiskUsage(), };
	}

	private void initSensors() {
		final ISampler[] samplers = this.createSamplers();
		for (final ISampler sampler : samplers) {
			this.monitoringController.schedulePeriodicSampler(sampler,
					TimeUnit.SECONDS.convert(this.settings.getInitialDelay(), this.settings.getInitialDelayUnit()), this.settings.getInterval(),
					this.settings.getIntervalUnit());
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(2048);
		final String lineSeparator = System.getProperty("line.separator");
		sb.append("Resource Monitoring Configuration:").append(lineSeparator).append("\tSampling interval = ").append(this.settings.getInterval())
				.append(lineSeparator)
				.append("\tSampling interval unit = ").append(this.settings.getIntervalUnit()).append(lineSeparator).append("\tInitial delay = ")
				.append(this.settings.getInitialDelay())
				.append(lineSeparator).append("\tInitial delay unit = ").append(this.settings.getInitialDelayUnit()).append(lineSeparator);
		if (this.settings.getDuration() < 0) {
			sb.append("\tDuration = INFINITE").append(lineSeparator);
		} else {
			sb.append("\tDuration = ").append(this.settings.getDuration());
			sb.append(lineSeparator).append("\tDuration unit = ");
			sb.append(this.settings.getDurationUnit()).append(lineSeparator);
		}
		return sb.toString();
	}

	@Override
	protected int execute(final JCommander commander, final String label) throws ConfigurationException {
		final CountDownLatch cdl = new CountDownLatch(1);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				cdl.countDown();
			}
		});

		final Configuration controllerConfiguration;
		if (this.settings.getMonitoringConfiguration() != null) {
			controllerConfiguration = ConfigurationFactory.createConfigurationFromFile(this.settings.getMonitoringConfiguration());
		} else {
			controllerConfiguration = ConfigurationFactory.createSingletonConfiguration();
		}
		this.monitoringController = MonitoringController.createInstance(controllerConfiguration);

		this.initSensors();
		ResourceMonitorMain.LOGGER.info("Monitoring started");

		if (this.settings.getDuration() >= 0) {
			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					cdl.countDown();
					timer.cancel();
				}
			}, TimeUnit.MILLISECONDS.convert(this.settings.getDuration(), this.settings.getDurationUnit()));
			if (ResourceMonitorMain.LOGGER.isInfoEnabled()) {
				ResourceMonitorMain.LOGGER.info("Waiting for {} {} timeout...", this.settings.getDuration(), this.settings.getDurationUnit());
			}
		}

		try {
			ResourceMonitorMain.LOGGER.info("Press Ctrl+c to terminate");
			cdl.await();
		} catch (final InterruptedException ex) {
			if (ResourceMonitorMain.LOGGER.isWarnEnabled()) {
				ResourceMonitorMain.LOGGER.warn("The monitoring has been interrupted", ex);
			}
			return AbstractLegacyTool.RUNTIME_ERROR;
		} finally {
			ResourceMonitorMain.LOGGER.info("Monitoring terminated");
		}

		return AbstractLegacyTool.SUCCESS_EXIT_CODE;
	}

	@Override
	protected Path getConfigurationPath() {
		return null;
	}

	@Override
	protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
		return false;
	}

	@Override
	protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
		return false;
	}

	@Override
	protected void shutdownService() {
		// nothing to be done here
	}
}
