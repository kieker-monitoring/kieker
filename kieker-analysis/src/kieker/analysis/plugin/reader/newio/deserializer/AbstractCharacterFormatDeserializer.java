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
package kieker.analysis.plugin.reader.newio.deserializer;

import java.nio.ByteBuffer;
import java.util.List;

import kieker.analysis.IProjectContext;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

/**
 * Abstract superclass for deserializers using character-based formats.
 *
 * @author Holger Knoche
 * @since 2.0
 */
public abstract class AbstractCharacterFormatDeserializer extends AbstractMonitoringRecordDeserializer {

	public AbstractCharacterFormatDeserializer(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	public List<IMonitoringRecord> deserializeRecordsFromByteBuffer(final ByteBuffer buffer, final int dataSize) throws InvalidFormatException {
		throw new UnsupportedOperationException("Reading binary data is unsupported by this reader.");
	}

}
