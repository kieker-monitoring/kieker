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

package kieker.analysis.plugin.reader.util;

import kieker.common.record.IMonitoringRecord;

/**
 * This is a simple interface showing that the {@link kieker.analysis.plugin.reader.filesystem.FSReader} can receive records. This is mostly a relict from an older
 * version.
 *
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.2
 */
public interface IMonitoringRecordReceiver {

	/**
	 * This method is called for each new record by each ReaderThread.
	 *
	 * @param record
	 *            The record to be processed.
	 * @return true if and only if the record has been handled correctly.
	 *
	 * @since 1.2
	 */
	public abstract boolean newMonitoringRecord(IMonitoringRecord record);

	/**
	 * @since 2.0
	 */
	void newEndOfFileRecord();
}
