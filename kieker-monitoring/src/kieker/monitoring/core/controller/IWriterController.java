/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

import kieker.common.record.IMonitoringRecord;
import kieker.common.registry.IMonitoringRecordReceiver;

/**
 * The methods must not throw any exceptions but indicate problems via its
 * respective return value.
 *
 * @author Andre van Hoorn, Jan Waller, Robert von Massow
 *
 * @since 1.3
 */
public interface IWriterController extends IMonitoringRecordReceiver {

	/**
	 * <p>
	 * Called for each new record to write it out to the pre-configured target (e.g., file system, database, or messaging queue). This method could invoke the given
	 * <code>record</code>'s methods declared in {@link IMonitoringRecord} and thus alter its <code>loggingTimestamp</code> property.
	 *
	 * <p>
	 * Notice, that this method should not throw an exception, but indicate an error by the return value false.
	 *
	 * @param record
	 *            the record.
	 * @return true on success; false in case of an error.
	 *
	 * @since 1.3
	 */
	@Override
	boolean newMonitoringRecord(IMonitoringRecord record);

	/**
	 * Waits for the termination of the monitoring controller. The termination must be previously triggered by {@link MonitoringController#terminateMonitoring()}.
	 *
	 * @param timeoutInMs
	 *            timeout in milliseconds to wait (a timeout of 0 means to wait forever)
	 *
	 * @throws InterruptedException
	 *             if the calling thread was interrupted while waiting for the termination
	 *
	 * @author Christian Wulf
	 *
	 * @since 1.13
	 */
	void waitForTermination(long timeoutInMs) throws InterruptedException;

}
