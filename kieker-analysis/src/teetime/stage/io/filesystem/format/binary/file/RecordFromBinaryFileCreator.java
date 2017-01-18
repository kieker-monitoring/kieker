/**
 * Copyright (C) 2015 Christian Wulf, Nelson Tavares de Sousa (http://teetime-framework.github.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package teetime.stage.io.filesystem.format.binary.file;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;

import teetime.stage.className.ClassNameRegistry;
import teetime.stage.className.ClassNameRegistryRepository;

import kieker.common.exception.MonitoringRecordException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;

/**
 * @author Christian Wulf
 *
 * @since 1.10
 */
public class RecordFromBinaryFileCreator {

	private final Logger logger;
	private final ClassNameRegistryRepository classNameRegistryRepository;

	public RecordFromBinaryFileCreator(final Logger logger, final ClassNameRegistryRepository classNameRegistryRepository) {
		this.logger = logger;
		this.classNameRegistryRepository = classNameRegistryRepository;
	}

	public IMonitoringRecord createRecordFromBinaryFile(final File binaryFile, final DataInputStream inputStream) throws IOException, MonitoringRecordException {
		final ClassNameRegistry classNameRegistry = this.classNameRegistryRepository.get(binaryFile.getParentFile());

		final Integer id;
		try {
			id = inputStream.readInt();
		} catch (final EOFException eof) {
			return null; // we are finished
		}
		final String classname = classNameRegistry.get(id);
		if (classname == null) {
			this.logger.error("Missing classname mapping for record type id " + "'" + id + "'");
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
			boolean successful = this.writeToObjectArray(inputStream, classNameRegistry, clazz, objectArray, idx, type);
			if (!successful) {
				return null;
			}
		}
		final IMonitoringRecord record = AbstractMonitoringRecord.createFromArray(clazz, objectArray);
		record.setLoggingTimestamp(loggingTimestamp);

		return record;
	}

	private boolean writeToObjectArray(final DataInputStream inputStream, final ClassNameRegistry classNameRegistry, final Class<? extends IMonitoringRecord> clazz,
			final Object[] objectArray, final int idx, final Class<?> type) throws IOException {
		if (type == String.class) {
			final Integer strId = inputStream.readInt();
			final String str = classNameRegistry.get(strId);
			if (str == null) {
				this.logger.error("No String mapping found for id " + strId.toString());
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
				this.logger.error("Unexpected value for unsupported type: " + clazz.getName());
				return false; // breaking error (break would not terminate the correct loop)
			}
			this.logger.warn("Unsupported type: " + clazz.getName());
			objectArray[idx] = null;
		}

		return true;
	}
}
