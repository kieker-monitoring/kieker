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

import java.nio.CharBuffer;
import java.util.List;

import com.google.common.collect.ImmutableList;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.reader.newio.IRawDataUnwrapper;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.TextValueDeserializer;

/**
 * Deserializer for Kieker's default string-based format.
 *
 * @author Holger Knoche
 * @since 2.0
 */
public class StringDeserializer extends AbstractCharacterFormatDeserializer {

	private final CachedRecordFactoryCatalog cachedRecordFactoryCatalog = CachedRecordFactoryCatalog.getInstance();

	public StringDeserializer(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	public Class<? extends IRawDataUnwrapper> getUnwrapperType() {
		return NewlineUnwrapper.class;
	}

	@Override
	public List<IMonitoringRecord> deserializeRecordsFromCharBuffer(final CharBuffer buffer, final int dataSize) throws InvalidFormatException {
		final TextValueDeserializer deserializer = TextValueDeserializer.create(buffer);

		final String recordTypeName = deserializer.getString();
		final long loggingTimestamp = deserializer.getLong();

		final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.cachedRecordFactoryCatalog.get(recordTypeName);
		final IMonitoringRecord record = recordFactory.create(deserializer);
		record.setLoggingTimestamp(loggingTimestamp);

		return ImmutableList.of(record);
	}

	@Override
	public void init() throws Exception {
		// Nothing to do
	}

	@Override
	public void terminate() {
		// Nothing to do
	}

}
