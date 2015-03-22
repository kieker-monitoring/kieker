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

/**
 * @author Jan Waller
 * 
 * @since 1.3
 */
public interface IStateController {

	/**
	 * Permanently terminates monitoring.
	 * 
	 * @see #isMonitoringTerminated()
	 * @return true if now terminated; false if already terminated
	 * 
	 * @since 1.3
	 */
	public abstract boolean terminateMonitoring();

	/**
	 * Returns whether monitoring is permanently terminated.
	 * 
	 * @see #terminateMonitoring()
	 * @return true if monitoring is permanently terminated, false if monitoring is enabled or disabled.
	 * 
	 * @since 1.3
	 */
	public abstract boolean isMonitoringTerminated();

	/**
	 * Enables monitoring.
	 * 
	 * @return true if monitoring is enabled, false otherwise
	 * 
	 * @since 1.3
	 */
	public abstract boolean enableMonitoring();

	/**
	 * Disables monitoring.
	 * 
	 * If monitoring is disabled, the MonitoringController simply pauses.
	 * Furthermore, probes should stop collecting new data and monitoring
	 * writers stop should stop writing existing data.
	 * 
	 * @return true if monitoring is disabled, false otherwise
	 * 
	 * @since 1.3
	 */
	public abstract boolean disableMonitoring();

	/**
	 * Returns whether monitoring is enabled or disabled/terminated.
	 * 
	 * @see #disableMonitoring()
	 * @see #enableMonitoring()
	 * 
	 * @return true of monitoring is enabled, false if monitoring is disabled or terminated.
	 * 
	 * @since 1.3
	 */
	public abstract boolean isMonitoringEnabled();

	/**
	 * Returns the name of this controller.
	 * 
	 * @return String
	 * 
	 * @since 1.3
	 */
	public abstract String getName();

	/**
	 * The hostname will be part of the monitoring data and allows to distinguish observations in cases where the software system is deployed on more than one host.
	 * 
	 * When you want to distinguish multiple Virtual Machines on one host, you* have to set the hostname manually in the Configuration.
	 * 
	 * @return The hostname.
	 * 
	 * @since 1.3
	 */
	public abstract String getHostname();

	/**
	 * Increments the experiment ID by 1 and returns the new value.
	 * 
	 * @return experimentID
	 * 
	 * @since 1.3
	 */
	public abstract int incExperimentId();

	/**
	 * Sets the experiment ID to the given value.
	 * 
	 * @param newExperimentID
	 *            The new ID.
	 * 
	 * @since 1.3
	 */
	public abstract void setExperimentId(final int newExperimentID);

	/**
	 * @return The experiment ID.
	 * 
	 * @since 1.3
	 */
	public abstract int getExperimentId();

	/**
	 * Is the debug mode enabled?
	 * 
	 * <p>
	 * Debug mode is for internal use only and changes a few internal id generation mechanisms to enable easier debugging. Additionally, it is possible to enable
	 * debug logging in the settings of the used logger.
	 * </p>
	 * 
	 * @return debug mode
	 * 
	 * @since 1.6
	 */
	public abstract boolean isDebug();
}
