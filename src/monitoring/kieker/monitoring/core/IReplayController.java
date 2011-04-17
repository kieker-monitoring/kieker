package kieker.monitoring.core;

import kieker.common.record.IMonitoringRecord;

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
public interface IReplayController extends IMonitoringControllerState {

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