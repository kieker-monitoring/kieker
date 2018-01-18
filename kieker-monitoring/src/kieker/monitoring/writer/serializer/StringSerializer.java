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

import java.nio.CharBuffer;
import java.util.Collection;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.TextValueSerializer;
import kieker.monitoring.writer.raw.IRawDataWrapper;

/**
 * String serializer for monitoring records based on the record's toString method.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
public class StringSerializer extends AbstractCharacterRecordSerializer {
	
	private static final char FIELD_SEPARATOR = ';';
	
	/**
	 * Creates a new serializer using the given configuration.
	 *
	 * @param configuration
	 *            The configuration to use
	 */
	public StringSerializer(final Configuration configuration) {
		super(configuration);
	}

	@Override
	public void serializeRecordToCharBuffer(final IMonitoringRecord record, final CharBuffer buffer) {
		// Serialize the record
		this.appendSingleRecord(record, buffer);
	}

	private void appendSingleRecord(final IMonitoringRecord record, final CharBuffer buffer) {
		final TextValueSerializer serializer = TextValueSerializer.create(buffer);
		
		buffer.append(record.getClass().getName());
		buffer.append(FIELD_SEPARATOR);
		buffer.append(String.valueOf(record.getLoggingTimestamp()));
		buffer.append(FIELD_SEPARATOR);
		
		record.serialize(serializer);
		
		buffer.append('\n');
	}

	@Override
	public void serializeRecordsToCharBuffer(final Collection<IMonitoringRecord> records, final CharBuffer buffer) {
		// Serialize the records
		for (final IMonitoringRecord record : records) {
			this.appendSingleRecord(record, buffer);
		}
	}

	@Override
	public Class<? extends IRawDataWrapper<?>> getWrapperType() {
		// No additional wrapping required (linebreaks are already added)
		return NopCharacterWrapper.class;
	}
	
	@Override
	public void onInitialization() {
		// Nothing to do
	}

	@Override
	public void onTermination() {
		// Nothing to do
	}

}
