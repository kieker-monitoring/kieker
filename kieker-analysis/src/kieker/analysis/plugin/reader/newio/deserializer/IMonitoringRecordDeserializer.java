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

package kieker.analysis.plugin.reader.newio.deserializer;

import java.nio.ByteBuffer;
import java.util.List;

import kieker.common.record.IMonitoringRecord;

/**
 * Interface for monitoring record deserializers.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
public interface IMonitoringRecordDeserializer {

	/**
	 * Deserializes the records contained in the given buffer.
	 *
	 * @param buffer
	 *            The buffer to decode the data from
	 * @param dataSize
	 *            The size of the record data in bytes
	 * @return The deserialized records
	 * @throws InvalidFormatException
	 *             If an invalid data format is detected
	 */
	public List<IMonitoringRecord> deserializeRecords(ByteBuffer buffer, int dataSize) throws InvalidFormatException;

	/**
	 * Lifecycle event, is called before data is read.
	 */
	public void init() throws Exception;

	/**
	 * Lifecycle event, is called when the analysis terminates.
	 */
	public void terminate();

}
