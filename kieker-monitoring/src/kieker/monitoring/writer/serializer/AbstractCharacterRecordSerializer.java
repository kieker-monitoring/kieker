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
import java.util.Collection;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

/**
 * Abstract supertype for serializers producing character data.
 *
 * @author Holger Knoche
 * @since 2.0
 */
public abstract class AbstractCharacterRecordSerializer extends AbstractMonitoringRecordSerializer {

	private static final String UNSUPPORTED_FORMAT_MESSAGE = "This serializer does not support binary data.";
	
	public AbstractCharacterRecordSerializer(final Configuration configuration) {
		super(configuration);
	}

	@Override
	public final boolean producesBinaryData() {
		return false;
	}
	
	@Override
	public final boolean producesCharacterData() {
		return true;
	}	
	
	@Override
	public final void serializeRecordToByteBuffer(final IMonitoringRecord record, final ByteBuffer buffer) {
		throw new UnsupportedOperationException(UNSUPPORTED_FORMAT_MESSAGE);
	}
	
	@Override
	public final void serializeRecordsToByteBuffer(final Collection<IMonitoringRecord> records, final ByteBuffer buffer) {
		throw new UnsupportedOperationException(UNSUPPORTED_FORMAT_MESSAGE);
	}
	
}
