/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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
import java.nio.CharBuffer;
import java.util.Collection;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.raw.IRawDataWrapper;

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
	 * 
	 * @deprecated Replaced by {@linkplain #serializeRecordToByteBuffer(IMonitoringRecord, ByteBuffer)}.
	 */
	@Deprecated
	public int serializeRecord(IMonitoringRecord record, ByteBuffer buffer);

	/**
	 * Serializes multiple monitoring records into the given byte buffer.
	 *
	 * @param records
	 *            The records to serialize
	 * @param buffer
	 *            The buffer to write to
	 * @return The number of bytes written to the buffer
	 * @since 1.13
	 * 
	 * @deprecated Replaced by {@linkplain #serializeRecordsToByteBuffer(Collection, ByteBuffer)}.
	 */
	@Deprecated
	public int serializeRecords(Collection<IMonitoringRecord> records, ByteBuffer buffer);

	/**
	 * Denotes whether this serializer produces binary data.
	 * @return see above
	 * 
	 * @since 2.0
	 */
	public boolean producesBinaryData();
	
	/**
	 * Serializes a single record into the given byte buffer.
	 *
	 * @param record
	 *            The record to serialize
	 * @param buffer
	 *            The buffer to write to
	 * @return The number of bytes written to the buffer
	 * @since 2.0
	 */
	public void serializeRecordToByteBuffer(IMonitoringRecord record, ByteBuffer buffer);

	/**
	 * Serializes multiple monitoring records into the given byte buffer.
	 *
	 * @param records
	 *            The records to serialize
	 * @param buffer
	 *            The buffer to write to. This buffer is expected to be rewound.
	 * @return The number of bytes written to the buffer
	 * @since 2.0
	 */
	public void serializeRecordsToByteBuffer(Collection<IMonitoringRecord> records, ByteBuffer buffer);

	/**
	 * Denotes whether this serializer produces character data.
	 * @return see above
	 * 
	 * @since 2.0
	 */
	public boolean producesCharacterData();
	
	/**
	 * Serializes a single record into the given character buffer.
	 *
	 * @param record
	 *            The record to serialize
	 * @param buffer
	 *            The buffer to write to
	 * 
	 * @since 2.0
	 */
	public void serializeRecordToCharBuffer(IMonitoringRecord record, CharBuffer buffer);
	
	/**
	 * Serializes multiple monitoring records into the given character buffer.
	 *
	 * @param records
	 *            The records to serialize
	 * @param buffer
	 *            The buffer to write to
	 * 
	 * @since 2.0
	 */
	public void serializeRecordsToCharBuffer(Collection<IMonitoringRecord> records, CharBuffer buffer);
	
	/**
	 * Returns the wrapper type required by this serializer, if any. This method returns {@code null} if wrapping is
	 * unsupported by this serializer.
	 * 
	 * @return A wrapper type, or {@code null} if wrapping is not supported
	 *
	 * @since 2.0
	 */
	public Class<? extends IRawDataWrapper<?>> getWrapperType();
	
	/**
	 * Called by the collector during initialization (before any records are written).
	 * 
	 * @since 1.13
	 */
	public void onInitialization();

	/**
	 * Called by the collector upon termination (after remaining records have been flushed).
	 * 
	 * @since 1.13
	 */
	public void onTermination();

}
