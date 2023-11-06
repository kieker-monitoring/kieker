/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.namedRecordPipe;

import kieker.common.record.IMonitoringRecord;

/**
 * This is a simple interface for a reader that works on a pipe.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.3
 */
public interface IPipeReader {

	/**
	 * Called to notify the reader that the pipe is closed.
	 * 
	 * @since 1.3
	 */
	public void notifyPipeClosed();

	/**
	 * Called for each new record.
	 * 
	 * @param record
	 *            the record.
	 * @return true on success; false in case of an error.
	 * 
	 * @since 1.5
	 */
	public abstract boolean newMonitoringRecord(IMonitoringRecord record);
}
