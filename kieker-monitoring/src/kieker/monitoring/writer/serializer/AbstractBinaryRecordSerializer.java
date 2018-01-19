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
import kieker.monitoring.writer.raw.IRawDataWrapper;

/**
 * Abstract supertype for serializers producing binary data.
 *
 * @author Holger Knoche
 * @since 2.0
 */
public abstract class AbstractBinaryRecordSerializer extends AbstractMonitoringRecordSerializer {

	private static final String UNSUPPORTED_FORMAT_MESSAGE = "This serializer does not support character data.";
	
	public AbstractBinaryRecordSerializer(final Configuration configuration) {
		super(configuration);
	}
	
	@Override
	public final boolean producesBinaryData() {
		return true;
	}
	
	@Override
	public final boolean producesCharacterData() {
		return false;
	}	
	
	@Override
	public final void serializeRecordToCharBuffer(final IMonitoringRecord record, final CharBuffer buffer) {
		throw new UnsupportedOperationException(UNSUPPORTED_FORMAT_MESSAGE);
	}
	
	@Override
	public final void serializeRecordsToCharBuffer(final Collection<IMonitoringRecord> records, final CharBuffer buffer) {
		throw new UnsupportedOperationException(UNSUPPORTED_FORMAT_MESSAGE);
	}
	
	@Override
	public Class<? extends IRawDataWrapper<?>> getWrapperType() {
		return BinaryChunkLengthWrapper.class;
	}
	
}
