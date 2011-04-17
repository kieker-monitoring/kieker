package kieker.monitoring.core;

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
public interface IController {

	/**
	 * Permanently terminates monitoring
	 * 
	 * @see #isMonitoringTerminated()
	 * @return true if now terminated; false if already terminated
	 */
	public abstract boolean terminateMonitoring();

	/**
	 * Returns whether monitoring is permanently terminated.
	 * 
	 * @see #terminateMonitoring()
	 * @return true if monitoring is permanently terminated, false if monitoring is enabled or disabled.
	 */
	public abstract boolean isMonitoringTerminated();

	/**
	 * Returns the name of this controller.
	 * 
	 * @return String
	 */
	public abstract String getName();

	/**
	 * The HostName will be part of the monitoring data and allows to distinguish
	 * observations in cases where the software system is deployed on more
	 * than one host.
	 * 
	 * When you want to distinguish multiple Virtual Machines on one host, you
	 * have to set the HostName manually in the Configuration.
	 */
	public abstract String getHostName();

	/**
	 * Increments the experiment ID by 1 and returns the new value.
	 * 
	 * @return experimentID
	 */
	public abstract int incExperimentId();

	/**
	 * Sets the experiment ID to the given value.
	 * 
	 * @param newExperimentID
	 */
	public abstract void setExperimentId(final int newExperimentID);

	/**
	 * Returns the experiment ID.
	 * 
	 * @return experimentID
	 */
	public abstract int getExperimentId();

	/**
	 * a String representation of the current state
	 * 
	 * @param StringBuilder a StringBuilder to write to
	 */
	public abstract void getState(StringBuilder sb);

	/**
	 * Enables/disables debug mode DebugMode
	 */
	public abstract void setDebug(boolean debug);

	/**
	 * @return debugMode
	 */
	public abstract boolean isDebug();

}
