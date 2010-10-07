package kieker.monitoring.core.state;

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
 * @author Andre van Hoorn
 */
public interface IMonitoringControllerState {

	/**
	 * Sets the monitoring state to enabled.
	 * 
	 * @see #terminateMonitoring()
	 * @see #disableMonitoring()
	 * 
	 * @return true if monitoring is enabled, false otherwise
	 */
	public boolean enableMonitoring();

	/**
	 * Sets the monitoring state to disabled.
	 * 
	 * @see #terminateMonitoring()
	 * @see #enableMonitoring()
	 * 
	 * @return true if monitoring is disabled, false otherwise
	 */
	public boolean disableMonitoring();

	/**
	 * Sets the monitoring state to terminated. This is a final state, meaning
	 * that it not possible to change the monitoring state afterwards.
	 * 
	 * @see #enableMonitoring()
	 * @see #disableMonitoring()
	 * 
	 */
	public void terminateMonitoring();

	/**
	 * Returns whether monitoring is enabled or disabled.
	 * 
	 * @see #terminateMonitoring()
	 * @see #enableMonitoring()
	 * @see #disableMonitoring()
	 * 
	 * @return true of monitoring is enabled, false if monitoring is disabled or
	 *         terminated.
	 */
	public boolean isMonitoringEnabled();

	/**
	 * Returns whether monitoring is disabled.
	 * 
	 * @see #disableMonitoring()
	 * @see #enableMonitoring()
	 * @see #terminateMonitoring()
	 * 
	 * @return true if monitoring is disabled; false it monitoring is enabled or
	 *         terminated.
	 */
	public boolean isMonitoringDisabled();

	/**
	 * Returns whether monitoring is permanently terminated.
	 * 
	 * @see #terminateMonitoring()
	 * @see #enableMonitoring()
	 * @see #disableMonitoring()
	 * 
	 * @return true if monitoring is permanently terminated, false if monitoring
	 *         is enabled or disabled.
	 */
	public boolean isMonitoringTerminated();

	/**
	 * Returns the experiment ID.
	 * 
	 * @return
	 */
	public int getExperimentId();

	/**
	 * Increments the experiment ID by 1 and returns the new value.
	 * 
	 * @return
	 */
	public int incExperimentId();

	/**
	 * Sets the experiment ID to the given value.
	 * 
	 * @param newExperimentID
	 *            the new experiment ID
	 */
	public void setExperimentId(final int newExperimentID);

	/**
	 * Enables the replay mode.
	 */
	public void enableReplayMode();

	/**
	 * Enables the real-time mode.
	 */
	public void enableRealtimeMode();

	/**
	 * Returns whether the controller is in replay mode state.
	 * 
	 * @return
	 */
	public boolean isReplayMode();

	/**
	 * Returns whether controller is in real-time mode state.
	 * 
	 * @return
	 */
	public boolean isRealtimeMode();

	/**
	 * Sets the debug state to the given value.
	 * 
	 * @param debugEnabled
	 *            true to enable debug output; false to disable debug output
	 */
	public void setDebugEnabled(boolean debugEnabled);

	/**
	 * Returns whether debug output is enabled or disabled
	 * 
	 * @return true if debug the debug mode is enabled, false if it is disabled
	 */
	public boolean isDebugEnabled();

	/**
	 * Returns a human-readable information string about the controller
	 * configuration.
	 * 
	 * @return the information string
	 */
	public String stateInfo();

	/**
	 * Returns the current value of the host name field.
	 * 
	 * @return
	 */
	public String getHostName();

	/**
	 * Sets the host name variable to the given value.
	 * 
	 * @param newHostName
	 */
	public void setHostName(final String newHostName);

	/**
	 * Returns the configured monitoring log writer.
	 * 
	 * @return
	 */
	public IMonitoringLogWriter getMonitoringLogWriter();
}
