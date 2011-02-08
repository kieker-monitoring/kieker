package kieker.monitoring.core;

import kieker.common.record.IMonitoringRecord;

public interface IReplayController extends IController {

	/**
	 * Enables the default mode that the logging timestamp of monitoring records
	 * received via {@link #newMonitoringRecord(IMonitoringRecord)} is set to
	 * the current timestamp.
	 * 
	 * @see IMonitoringRecord#getLoggingTimestamp()
	 * @see #enableReplayMode()
	 */
	public abstract void enableRealtimeMode();

	/**
	 * Returns whether controller is in real-time mode state.
	 * .currentTimeNanos()
	 * 
	 * @see #enableRealtimeMode()
	 * @see #enableReplayMode()
	 * 
	 * @return true if the controller is in real-time mode, false otherwise
	 */
	public abstract boolean isRealtimeMode();

	/**
	 * Enables the mode that the logging timestamp of monitoring records
	 * received via {@link #newMonitoringRecord(IMonitoringRecord)} is not set.
	 * This mode is, for example, helpful to replay recorded traces with their
	 * original timestamps.
	 * 
	 * @see IMonitoringRecord#getLoggingTimestamp()
	 * @see #enableRealtimeMode()
	 */
	public abstract void enableReplayMode();

	/**
	 * Returns whether the controller is in replay mode state.
	 * 
	 * @see #enableRealtimeMode()
	 * @see #enableReplayMode()
	 * 
	 * @return true if the controller is in replay mode, false otherwise
	 */
	public abstract boolean isReplayMode();

}