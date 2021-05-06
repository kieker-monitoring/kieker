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

package kieker.monitoring.writer.serializer;

import java.nio.ByteBuffer;
import java.util.Collection;

import kieker.common.record.IMonitoringRecord;

/**
 * Interface for monitoring record serializers.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
public interface IMonitoringRecordSerializer {

	/**
	 * Serializes a single record into the given byte buffer.
	 *
	 * @param record
	 *            The record to serialize
	 * @param buffer
	 *            The buffer to write to
	 * @return The number of bytes written to the buffer
	 * @since 1.13
	 */
	int serializeRecord(IMonitoringRecord record, ByteBuffer buffer);

	/**
	 * Serializes multiple monitoring records into the given byte buffer.
	 *
	 * @param records
	 *            The records to serialize
	 * @param buffer
	 *            The buffer to write to
	 * @return The number of bytes written to the buffer
	 * @since 1.13
	 */
	int serializeRecords(Collection<IMonitoringRecord> records, ByteBuffer buffer);

	/**
	 * Called by the collector during initialization (before any records are written).
	 *
	 * @since 1.13
	 */
	void onInitialization();

	/**
	 * Called by the collector upon termination (after remaining records have been flushed).
	 *
	 * @since 1.13
	 */
	void onTermination();

}
