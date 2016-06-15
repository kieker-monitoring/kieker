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

package kieker.monitoring.writer;

import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IMonitoringRecordReceiver;
import kieker.monitoring.core.controller.IMonitoringController;

/**
 * @author Andre van Hoorn, Jan Waller, Robert von Massow
 *
 * @since < 0.9
 */
public interface IMonitoringWriter extends IMonitoringRecordReceiver<IMonitoringRecord> {

	/**
	 * {@inheritDoc}
	 *
	 * @since 1.3
	 */
	@Override
	public abstract boolean newMonitoringRecord(IMonitoringRecord record);

	/**
	 * Called for each new record.
	 *
	 * Notice, that this method should not throw an exception, but indicate an error by the return value false.
	 * Also, this method MUST NOT block.
	 *
	 * @param record
	 *            the record.
	 * @return true on success; false in case of an error.
	 *
	 * @since 1.9
	 */
	public abstract boolean newMonitoringRecordNonBlocking(IMonitoringRecord record);

	/**
	 * Called by the Monitoring Controller to announce a shutdown of monitoring.
	 * Writers should return as soon as it is safe to terminate Kieker.
	 *
	 * @since 1.3
	 */
	public abstract void terminate();

	/**
	 * Set the <code>IMonitoringController</code> controlling this writer.
	 *
	 * @param monitoringController
	 *            The monitoring controller which will be the new parent of this writer.
	 *
	 * @throws Exception
	 *             If something went wrong.
	 *
	 * @since 1.3
	 */
	public abstract void setController(final IMonitoringController monitoringController) throws Exception;

	/**
	 * Delivers a string representation of this writer.
	 *
	 * @return A string.
	 *
	 * @since 1.3
	 */
	@Override
	public abstract String toString();
}
