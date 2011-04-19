package kieker.monitoring.core.controller;

/**
 * @author Jan Waller
 */
public interface IMonitoringControllerState {

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
	 * Enables monitoring.
	 * 
	 * @return true if monitoring is enabled, false otherwise
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
	 */
	public abstract boolean disableMonitoring();

	/**
	 * Returns whether monitoring is enabled or disabled/terminated.
	 * 
	 * @see #disableMonitoring()
	 * @see #enableMonitoring()
	 * 
	 * @return true of monitoring is enabled, false if monitoring is disabled or terminated.
	 */
	public abstract boolean isMonitoringEnabled();

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
	
	public abstract String getState();
}
