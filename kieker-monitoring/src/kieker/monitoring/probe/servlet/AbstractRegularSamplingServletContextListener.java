/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.probe.servlet;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.monitoring.core.controller.ISamplingController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.sampler.ISampler;
import kieker.monitoring.core.sampler.ScheduledSamplerJob;

/**
 * @author Nils Christian Ehmke, Andre van Hoorn
 * 
 * @since 1.10
 */
public abstract class AbstractRegularSamplingServletContextListener implements ServletContextListener {

	public static final long DEFAULT_SENSOR_INTERVAL_SECONDS = 15;
	public static final long DEFAULT_SENSOR_INITIAL_DELAY_SECONDS = 0;

	private static final Log LOG = LogFactory.getLog(AbstractRegularSamplingServletContextListener.class);

	private final String contextParameterNameSamplingIntervalSeconds = this.getContextParameterNameSamplingIntervalSeconds();
	private final String contextParameterNameSamplingDelaySeconds = this.getContextParameterNameSamplingDelaySeconds();

	private final ISamplingController samplingController = MonitoringController.getInstance();
	private final Collection<ScheduledSamplerJob> startedSamplerJobs = new CopyOnWriteArrayList<ScheduledSamplerJob>();

	private volatile long sensorIntervalSeconds = DEFAULT_SENSOR_INTERVAL_SECONDS;
	private volatile long initialDelaySeconds = DEFAULT_SENSOR_INITIAL_DELAY_SECONDS;

	/**
	 * Empty default constructor.
	 */
	public AbstractRegularSamplingServletContextListener() {
		// nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void contextDestroyed(final ServletContextEvent event) {
		for (final ScheduledSamplerJob s : this.startedSamplerJobs) {
			this.samplingController.removeScheduledSampler(s);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void contextInitialized(final ServletContextEvent event) {
		this.initParameters(event.getServletContext());
		this.initSensors();
	}

	private void initParameters(final ServletContext c) {
		if (c == null) {
			LOG.warn("ServletContext == null");
			// we are using the default values assigned during variable declaration.
			return;
		}

		// allowed values: Int>=0
		this.initialDelaySeconds = this.readLongInitParameter(c, this.contextParameterNameSamplingDelaySeconds, DEFAULT_SENSOR_INITIAL_DELAY_SECONDS);

		// allows values: Int>0
		this.sensorIntervalSeconds = this.readLongInitParameter(c, this.contextParameterNameSamplingIntervalSeconds, DEFAULT_SENSOR_INTERVAL_SECONDS);
		if (this.sensorIntervalSeconds == 0) {
			LOG.warn("values for the init-param '" + this.contextParameterNameSamplingIntervalSeconds + "' must be >0; found: " + this.sensorIntervalSeconds
					+ ". Using default value: " + DEFAULT_SENSOR_INTERVAL_SECONDS);
			this.sensorIntervalSeconds = DEFAULT_SENSOR_INTERVAL_SECONDS;
		}

	}

	private long readLongInitParameter(final ServletContext c, final String paramName, final long defaultValue) {
		long val = -1;
		final String valStr = c.getInitParameter(paramName);

		if (valStr != null) {
			try {
				val = Long.parseLong(valStr);
			} catch (final NumberFormatException exc) {
				val = -1;
				// will use default value below.
			}
		}

		if (val < 0) {
			LOG.warn("Invalid or missing value for context-param '" + paramName + "': " + valStr + ". Using default value: " + defaultValue);
			val = defaultValue;
		}

		return val;
	}

	/**
	 * Creates and schedules the {@link ScheduledSamplerJob}s and stores them for later removal in the {@link Collection} {@link #samplerJobs}.
	 */
	private void initSensors() {
		final ISampler[] samplers = this.createSamplers();
		for (final ISampler sampler : samplers) {
			final ScheduledSamplerJob job = this.samplingController.schedulePeriodicSampler(sampler, this.initialDelaySeconds, this.sensorIntervalSeconds,
					TimeUnit.SECONDS);
			this.startedSamplerJobs.add(job);
		}
	}

	/**
	 * Parameter name for the sampling interval to be used in the web.xml file.
	 * 
	 * @return the parameter name
	 */
	protected abstract String getContextParameterNameSamplingIntervalSeconds();

	/**
	 * Parameter name for the sampling delay to be used in the web.xml file.
	 * 
	 * @return the parameter name
	 */
	protected abstract String getContextParameterNameSamplingDelaySeconds();

	/**
	 * Create samplers for the specific sampling class.
	 * 
	 * @return array of samplers
	 */
	protected abstract ISampler[] createSamplers();

}
