package kieker.monitoring.core.configuration;

import kieker.monitoring.core.MonitoringController;
import kieker.monitoring.writer.IMonitoringLogWriter;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
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
 * ==================================================
 */

/**
 * author Andre van Hoorn
 */
public interface IMonitoringConfiguration {

	/**
	 * Enables or disables the logging of debug messages by the monitoring
	 * framework.
	 * 
	 * @param debugEnabled
	 *            true to enable debug output; false to disable debug output
	 */
	public void setDebugEnabled(boolean debugEnabled);

	/**
	 * Returns whether debug output is enabled or disabled
	 * 
	 * @return true if debug output is enabled, false otherwise
	 */
	public boolean isDebugEnabled();

	/**
	 * Enables or disables monitoring. If monitoring is disabled, this means
	 * that the {@link MonitoringController} drops all records received via
	 * {@link MonitoringController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)}
	 * instead of passing them to the configured monitoring log writer.
	 * 
	 * @param monitoringEnabled
	 */
	public void setMonitoringEnabled(boolean monitoringEnabled);

	/**
	 * Returns whether monitoring is enabled or disabled.
	 * 
	 * @see #setMonitoringEnabled(boolean)
	 * 
	 * @return if monitoring is enable, false otherwise
	 */
	public boolean isMonitoringEnabled();

	/**
	 * Returns the configured monitoring log writer.
	 * 
	 * @return the monitoring log writer
	 */
	public IMonitoringLogWriter getMonitoringLogWriter();

	/**
	 * Sets the configured size of the thread pool used to execute the
	 * periodic sensor jobs registered and removed via
	 * {@link MonitoringController#schedulePeriodicSensor(kieker.monitoring.probe.util.ITriggeredSensor, long, long, java.util.concurrent.TimeUnit)}
	 * and
	 * {@link MonitoringController#removePeriodicSensor(kieker.monitoring.core.ScheduledSensorJob)}
	 * respectively.
	 * 
	 * @return
	 */
	public void setPeriodicSensorsExecutorPoolSize(int poolSize);
	
	/**
	 * Returns the configured size of the thread pool used to execute the
	 * periodic sensor jobs registered and removed via
	 * {@link MonitoringController#schedulePeriodicSensor(kieker.monitoring.probe.util.ITriggeredSensor, long, long, java.util.concurrent.TimeUnit)}
	 * and
	 * {@link MonitoringController#removePeriodicSensor(kieker.monitoring.core.ScheduledSensorJob)}
	 * respectively.
	 * 
	 * @return
	 */
	public int getPeriodicSensorsExecutorPoolSize();

	/**
	 * Sets the host name field to the given value.
	 * 
	 * @param newHostName
	 */
	public void setHostName(String newHostName);

	/**
	 * Returns the value of the host name field.
	 * 
	 * @return
	 */
	public String getHostName();

	/**
	 * Returns the name of the configuration.
	 * 
	 * @return
	 */
	public String getName();
}
