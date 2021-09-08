/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.tt.reader.filesystem.format.text.file;

import java.io.File;
import java.nio.CharBuffer;

import kieker.analysis.tt.reader.filesystem.className.ClassNameRegistryRepository;
import kieker.analysis.tt.reader.filesystem.util.MappingException;
import kieker.common.exception.IllegalRecordFormatException;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.exception.UnknownRecordTypeException;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.TextValueDeserializer;
import kieker.common.registry.reader.ReaderRegistry;

/**
 * @author Christian Wulf
 *
 * @since 1.10
 *
 * @deprecated since 1.15 removed 1.16
 */
@Deprecated
public class RecordFromTextLineCreator {

	private static final IllegalRecordFormatException ILLEGAL_RECORD_FORMAT_EXCEPTION = new IllegalRecordFormatException();

	private final CachedRecordFactoryCatalog recordFactories = CachedRecordFactoryCatalog.getInstance();

	private final ClassNameRegistryRepository classNameRegistryRepository;

	private final CharBuffer charBuffer;

	public RecordFromTextLineCreator(final ClassNameRegistryRepository classNameRegistryRepository) {
		this.classNameRegistryRepository = classNameRegistryRepository;
		this.charBuffer = CharBuffer.allocate(100000);
	}

	public IMonitoringRecord createRecordFromLine(final File textFile, final String line) throws MonitoringRecordException, IllegalRecordFormatException,
			MappingException,
			UnknownRecordTypeException {
		this.charBuffer.put(line);
		return this.createRecord(this.classNameRegistryRepository.get(textFile.getParentFile()));

	}

	private IMonitoringRecord createRecord(final ReaderRegistry<String> readerRegistry)
			throws MappingException, MonitoringRecordException, UnknownRecordTypeException, IllegalRecordFormatException {
		this.charBuffer.flip();
		final char lead = this.charBuffer.get();
		if (lead == '$') {
			final TextValueDeserializer deserializer = TextValueDeserializer.create(this.charBuffer);
			final int id = deserializer.getInt();
			final String classname = readerRegistry.get(id);
			if (classname == null) {
				throw new MappingException("Missing classname mapping for record type id " + "'" + id + "'");
			}

			final long loggingTimestamp = deserializer.getLong();
			final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactories.get(classname);
			final IMonitoringRecord event = recordFactory.create(deserializer);
			event.setLoggingTimestamp(loggingTimestamp);
			this.charBuffer.clear();

			return event;
		} else {
			throw ILLEGAL_RECORD_FORMAT_EXCEPTION;
		}
	}
}
