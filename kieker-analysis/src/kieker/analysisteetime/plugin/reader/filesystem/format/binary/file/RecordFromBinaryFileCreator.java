/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.plugin.reader.filesystem.format.binary.file;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysisteetime.plugin.reader.filesystem.className.ClassNameRegistry;
import kieker.analysisteetime.plugin.reader.filesystem.className.ClassNameRegistryRepository;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;

/**
 * @author Christian Wulf
 *
 * @since 1.10
 */
public class RecordFromBinaryFileCreator {

	private static final Logger LOGGER = LoggerFactory.getLogger(RecordFromBinaryFileCreator.class);

	private final ClassNameRegistryRepository classNameRegistryRepository;

	public RecordFromBinaryFileCreator(final ClassNameRegistryRepository classNameRegistryRepository) {
		this.classNameRegistryRepository = classNameRegistryRepository;
	}

	public IMonitoringRecord createRecordFromBinaryFile(final File binaryFile, final DataInputStream inputStream)
			throws IOException, MonitoringRecordException {
		final Integer id;
		try {
			id = inputStream.readInt();
		} catch (final EOFException eof) {
			return null; // we are finished
		}

		final ClassNameRegistry classNameRegistry = this.classNameRegistryRepository.get(binaryFile.getParentFile());
		final String classname = classNameRegistry.get(id);
		if (classname == null) {
			LOGGER.error("Missing classname mapping for record type id '{}'", id);
			return null; // we can't easily recover on errors
		}

		final Class<? extends IMonitoringRecord> clazz = AbstractMonitoringRecord.classForName(classname);
		final Class<?>[] typeArray = AbstractMonitoringRecord.typesForClass(clazz);

		// read record
		final long loggingTimestamp = inputStream.readLong(); // NOPMD (must be read here!)
		final Object[] objectArray = new Object[typeArray.length];
		int idx = -1;
		for (final Class<?> type : typeArray) {
			idx++;
			final boolean successful = this.writeToObjectArray(inputStream, classNameRegistry, clazz, objectArray, idx, type);
			if (!successful) {
				return null;
			}
		}
		final IMonitoringRecord record = AbstractMonitoringRecord.createFromArray(clazz, objectArray);
		record.setLoggingTimestamp(loggingTimestamp);

		return record;
	}

	private boolean writeToObjectArray(final DataInputStream inputStream, final ClassNameRegistry classNameRegistry,
			final Class<? extends IMonitoringRecord> clazz, final Object[] objectArray, final int idx,
			final Class<?> type) throws IOException {
		if (type == String.class) {
			final Integer strId = inputStream.readInt();
			final String str = classNameRegistry.get(strId);
			if (str == null) {
				LOGGER.error("No String mapping found for id {}", strId.toString());
				objectArray[idx] = "";
			} else {
				objectArray[idx] = str;
			}
		} else if ((type == int.class) || (type == Integer.class)) {
			objectArray[idx] = inputStream.readInt();
		} else if ((type == long.class) || (type == Long.class)) {
			objectArray[idx] = inputStream.readLong();
		} else if ((type == float.class) || (type == Float.class)) {
			objectArray[idx] = inputStream.readFloat();
		} else if ((type == double.class) || (type == Double.class)) {
			objectArray[idx] = inputStream.readDouble();
		} else if ((type == byte.class) || (type == Byte.class)) {
			objectArray[idx] = inputStream.readByte();
		} else if ((type == short.class) || (type == Short.class)) { // NOPMD (short)
			objectArray[idx] = inputStream.readShort();
		} else if ((type == boolean.class) || (type == Boolean.class)) {
			objectArray[idx] = inputStream.readBoolean();
		} else {
			if (inputStream.readByte() != 0) {
				LOGGER.error("Unexpected value for unsupported type: {}", clazz.getName());
				return false; // breaking error (break would not terminate the correct loop)
			}
			LOGGER.warn("Unsupported type: {}", clazz.getName());
			objectArray[idx] = null;
		}

		return true;
	}
}
