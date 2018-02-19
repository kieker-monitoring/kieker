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

package kieker.analysisteetime.plugin.reader.filesystem.format.text.file;

import java.io.File;

import kieker.analysisteetime.plugin.reader.filesystem.className.ClassNameRegistry;
import kieker.analysisteetime.plugin.reader.filesystem.className.ClassNameRegistryRepository;
import kieker.analysisteetime.plugin.reader.filesystem.util.MappingException;
import kieker.common.exception.IllegalRecordFormatException;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.exception.UnknownRecordTypeException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;

/**
 * @author Christian Wulf
 *
 * @since 1.10
 */
public class RecordFromTextLineCreator {

	private static final String CSV_SEPARATOR_CHARACTER = ";";

	private static final IllegalRecordFormatException ILLEGAL_RECORD_FORMAT_EXCEPTION = new IllegalRecordFormatException();

	private final ClassNameRegistryRepository classNameRegistryRepository;

	public RecordFromTextLineCreator(final ClassNameRegistryRepository classNameRegistryRepository) {
		this.classNameRegistryRepository = classNameRegistryRepository;
	}

	public IMonitoringRecord createRecordFromLine(final File textFile, final String line) throws MonitoringRecordException, IllegalRecordFormatException,
			MappingException,
			UnknownRecordTypeException {
		final String[] recordFields = line.split(CSV_SEPARATOR_CHARACTER, -1);

		if (recordFields.length < 2) {
			throw ILLEGAL_RECORD_FORMAT_EXCEPTION;
		}

		final boolean isModernRecord = recordFields[0].charAt(0) == '$';
		if (isModernRecord) {
			return this.createModernRecordFromRecordFields(textFile, recordFields);
		} else {
			return this.createLegacyRecordFromRecordFiels(recordFields);
		}
	}

	private IMonitoringRecord createModernRecordFromRecordFields(final File textFile, final String[] recordFields) throws MonitoringRecordException,
			MappingException,
			UnknownRecordTypeException {
		final ClassNameRegistry classNameRegistry = this.classNameRegistryRepository.get(textFile.getParentFile());
		final Integer id = Integer.valueOf(recordFields[0].substring(1));
		final String classname = classNameRegistry.get(id);
		if (classname == null) {
			throw new MappingException("Missing classname mapping for record type id " + "'" + id + "'");
		}
		final Class<? extends IMonitoringRecord> clazz = this.getClassByName(classname);
		final long loggingTimestamp = Long.parseLong(recordFields[1]);
		final int skipValues;
		// check for Kieker < 1.6 OperationExecutionRecords
		if ((recordFields.length == 11) && clazz.equals(OperationExecutionRecord.class)) {
			skipValues = 3;
		} else {
			skipValues = 2;
		}
		// Java 1.5 compatibility
		final String[] recordFieldsReduced = new String[recordFields.length - skipValues];
		System.arraycopy(recordFields, skipValues, recordFieldsReduced, 0, recordFields.length - skipValues);
		// in Java 1.6 this could be simplified to
		// recordFieldsReduced = Arrays.copyOfRange(recordFields, skipValues, recordFields.length);

		final IMonitoringRecord record = AbstractMonitoringRecord.createFromStringArray(clazz, recordFieldsReduced);
		record.setLoggingTimestamp(loggingTimestamp);
		return record;
	}

	private Class<? extends IMonitoringRecord> getClassByName(final String classname) throws MonitoringRecordException, UnknownRecordTypeException {
		try {
			return AbstractMonitoringRecord.classForName(classname);
		} catch (final MonitoringRecordException ex) {
			throw new UnknownRecordTypeException("Failed to load record type " + classname, classname, ex);
		}
	}

	private IMonitoringRecord createLegacyRecordFromRecordFiels(final String[] recordFields) throws MonitoringRecordException {
		final String[] recordFieldsReduced = new String[recordFields.length - 1];
		System.arraycopy(recordFields, 1, recordFieldsReduced, 0, recordFields.length - 1);
		return AbstractMonitoringRecord.createFromStringArray(OperationExecutionRecord.class, recordFieldsReduced);
	}
}
