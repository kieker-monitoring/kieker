package kieker.monitoring.core;

import kieker.common.record.IMonitoringRecordReceiver;
import kieker.monitoring.writer.IMonitoringWriter;

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
 * The methods must not throw any exceptions but indicate problems via its
 * respective return value.
 * 
 * @author Andre van Hoorn, Jan Waller
 */
public interface IWriterController extends IMonitoringRecordReceiver, IController, ITimerController {

	/**
	 * Returns the configured monitoring writer.
	 * 
	 * @return
	 */
	public abstract IMonitoringWriter getMonitoringWriter();

	/**
	 * Enables monitoring.
	 * 
	 * @return true if monitoring is enabled, false otherwise
	 */
	public abstract boolean enableMonitoring();

	/**
	 * Disables monitoring. This means that the {@link WriterController} drops all records received via
	 * {@link WriterController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)}
	 * instead of passing them to the configured monitoring log writer.
	 * 
	 * @return true if monitoring is disabled, false otherwise
	 */
	public abstract boolean disableMonitoring();

	/**
	 * Returns whether monitoring is enabled or disabled.
	 * 
	 * @see #disableMonitoring()
	 * @see #enableMonitoring()
	 * 
	 * @return true of monitoring is enabled, false if monitoring is disabled or terminated.
	 */
	public abstract boolean isMonitoringEnabled();

	/**
	 * Returns whether monitoring is disabled.
	 * 
	 * @see #disableMonitoring()
	 * @see #enableMonitoring()
	 * 
	 * @return true if monitoring is disabled; false it monitoring is enabled or terminated.
	 */
	public abstract boolean isMonitoringDisabled();
	
	/**
	 * Shows how many inserts have been performed since last restart of the execution environment.
	 * 
	 * @return long
	 */
	public abstract long getNumberOfInserts();
}
