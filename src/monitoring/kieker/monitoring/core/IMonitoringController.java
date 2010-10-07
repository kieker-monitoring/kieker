package kieker.monitoring.core;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;
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
 * The methods must not throw any exceptions but indicate problems via its
 * respective return value.
 * 
 * @author Andre van Hoorn
 */
public interface IMonitoringController extends IMonitoringRecordReceiver {

	/**
	 * Returns the configured monitoring log writer.
	 * 
	 * @return
	 */
	public IMonitoringLogWriter getMonitoringLogWriter();

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
	 * @return true if debug the debug mode is enabled, false if it is disabled
	 */
	public boolean isDebugEnabled();

	/**
	 * Enables monitoring.
	 * 
	 * @return true if monitoring is enabled, false otherwise
	 */
	public boolean enableMonitoring();

	/**
	 * Disables monitoring. This means that the {@link MonitoringController}
	 * drops all records received via
	 * {@link MonitoringController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)}
	 * instead of passing them to the configured monitoring log writer.
	 * 
	 * @return true if monitoring is disabled, false otherwise
	 */
	public boolean disableMonitoring();

	/**
	 * Permanently terminates monitoring (e.g., due to a failure). Subsequent
	 * tries to enable monitoring via {@link #setMonitoringEnabled(boolean)}
	 * will be refused. Regardless of this method's return value monitoring will
	 * be terminated after this call returns.
	 * 
	 */
	public void terminateMonitoring();

	/**
	 * Returns whether monitoring is enabled or disabled.
	 * 
	 * @see #setMonitoringEnabled(boolean)
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
	 * @see #setMonitoringEnabled(boolean)
	 * 
	 * @return true if monitoring is permanently terminated, false if monitoring
	 *         is enabled or disabled.
	 */
	public boolean isMonitoringTerminated();

	/**
	 * Passes the given monitoring record to the configured writer if the
	 * controller is enabled. If monitoring is disabled or terminate, this
	 * method returns immediately. As a side-effect, the method must terminate
	 * monitoring if an error occurs.
	 * 
	 * @param monitoringRecord
	 *            the record to be logged
	 * @return true if the record has been passed the writer successfully; false
	 *         in case an error occurred or the controller is not enabled.
	 */
	@Override
	public boolean newMonitoringRecord(IMonitoringRecord record);
}
