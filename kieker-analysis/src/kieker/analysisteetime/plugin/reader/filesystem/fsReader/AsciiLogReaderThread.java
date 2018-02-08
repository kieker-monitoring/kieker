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

package kieker.analysisteetime.plugin.reader.filesystem.fsReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipInputStream;

import kieker.analysis.plugin.reader.util.IMonitoringRecordReceiver;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.util.filesystem.FSUtil;
import kieker.common.util.filesystem.FileExtensionFilter;

/**
 * Reads the contents of a single file system log directory and passes the records to the registered receiver of type
 * {@link IMonitoringRecordReceiver}.
 *
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller
 *
 * @since 1.2
 */
class AsciiLogReaderThread extends AbstractLogReaderThread {

	private static final Log LOG = LogFactory.getLog(AsciiLogReaderThread.class);

	private final Map<Integer, String> stringRegistry = new HashMap<>(); // NOPMD (no synchronization
																			// needed)
	private final IMonitoringRecordReceiver recordReceiver;
	private final File inputDir;
	private final boolean ignoreUnknownRecordTypes;
	private final boolean shouldDecompress;
	// This set of classes is used to filter only records of a specific type. The value null means all record types are
	// read.
	private final Set<String> unknownTypesObserved = new HashSet<>();

	/**
	 * Creates a new instance of this class.
	 *
	 * @param inputDir
	 *            The File object for the input directory.
	 * @param recordReceiver
	 *            The receiver handling the records.
	 * @param ignoreUnknownRecordTypes
	 *            select only records of this type; null selects all
	 * @param shouldDecompress
	 *            <code>true</code> if each log file is compressed, otherwise <code>false</code>
	 */
	public AsciiLogReaderThread(final File inputDir, final IMonitoringRecordReceiver recordReceiver,
			final boolean ignoreUnknownRecordTypes, final boolean shouldDecompress) {
		super(LOG, inputDir);
		if ((inputDir == null) || !inputDir.isDirectory()) {
			throw new IllegalArgumentException("Invalid or empty inputDir");
		}
		this.inputDir = inputDir;
		this.recordReceiver = recordReceiver;
		this.ignoreUnknownRecordTypes = ignoreUnknownRecordTypes;
		this.shouldDecompress = shouldDecompress;
	}

	/**
	 * Reads the mapping file located in the directory.
	 */
	@Override
	protected void readMappingFile() {
		File mappingFile = new File(this.inputDir.getAbsolutePath() + File.separator + FSUtil.MAP_FILENAME);
		if (!mappingFile.exists()) {
			// No mapping file found. Check whether we find a legacy tpmon.map file!
			mappingFile = new File(this.inputDir.getAbsolutePath() + File.separator + FSUtil.LEGACY_MAP_FILENAME);
			if (mappingFile.exists()) {
				LOG.info("Directory '" + this.inputDir + "' contains no file '" + FSUtil.MAP_FILENAME + "'. Found '"
						+ FSUtil.LEGACY_MAP_FILENAME + "' ... switching to legacy mode");
			} else {
				// no {kieker|tpmon}.map exists. This is valid for very old monitoring logs. Hence, only dump a log.warn
				if (LOG.isWarnEnabled()) {
					LOG.warn("No mapping file in directory '" + this.inputDir.getAbsolutePath() + "'");
				}
				return;
			}
		}
		// found any kind of mapping file
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(mappingFile), FSUtil.ENCODING));
			String line;
			while ((line = in.readLine()) != null) { // NOPMD (assign)
				if (line.length() == 0) {
					continue; // ignore empty lines
				}
				if (LOG.isDebugEnabled()) {
					LOG.debug("Read line: " + line);
				}
				final int split = line.indexOf('=');
				if (split == -1) {
					final String message = String.format(
							"Failed to parse line: {%s} from file %s. Each line must contain ID=VALUE pairs.", line,
							mappingFile.getAbsolutePath());
					LOG.error(message);
					continue; // continue on errors
				}
				final String key = line.substring(0, split);
				final String value = FSUtil.decodeNewline(line.substring(split + 1));
				// the leading $ is optional
				final Integer id;
				try {
					id = Integer.valueOf((key.charAt(0) == '$') ? key.substring(1) : key); // NOCS
				} catch (final NumberFormatException ex) {
					LOG.error("Error reading mapping file, id must be integer", ex);
					continue; // continue on errors
				}
				final String prevVal = this.stringRegistry.put(id, value);
				if (prevVal != null) {
					final String message = String.format(
							"Found addional entry for id='%s', old value was '%s' new value is '%s'", id, prevVal,
							value);
					LOG.error(message); // NOPMD (guard not necessary for error level)
				}
			}
		} catch (final IOException ex) {
			LOG.error("Error reading mapping file", ex);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException ex) {
					LOG.error("Exception while closing input stream for mapping file", ex);
				}
			}
		}
	}

	/**
	 * Reads the records contained in the given normal file and passes them to the registered {@link #recordReceiver}.
	 *
	 * @param inputFile
	 *            The input file which should be processed.
	 */
	@Override
	protected void processNormalInputFile(final File inputFile) {
		boolean abortDueToUnknownRecordType = false;
		BufferedReader in = null;
		try {
			InputStream fileInputStream = new FileInputStream(inputFile);
			if (this.shouldDecompress) {
				@SuppressWarnings("resource")
				final ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
				zipInputStream.getNextEntry();
				fileInputStream = zipInputStream;
			}
			in = new BufferedReader(new InputStreamReader(fileInputStream, FSUtil.ENCODING));
			String line;
			while ((line = in.readLine()) != null) { // NOPMD (assign)
				line = line.trim();
				if (line.length() == 0) {
					continue; // ignore empty lines
				}
				IMonitoringRecord record = null;
				final String[] recordFields = line.split(";");
				try {
					if (recordFields[0].charAt(0) == '$') { // modern record
						if (recordFields.length < 2) {
							LOG.error("Illegal record format: " + line); // NOPMD (guard not necessary for error level)
							continue; // skip this record
						}
						final Integer id = Integer.valueOf(recordFields[0].substring(1));
						final String classname = this.stringRegistry.get(id);
						if (classname == null) {
							final String message = String.format("Missing classname mapping for record type id '%s'",
									id);
							LOG.error(message); // NOPMD (guard not necessary for error level)
							continue; // skip this record
						}
						Class<? extends IMonitoringRecord> clazz = null;
						try { // NOCS (nested try)
							clazz = AbstractMonitoringRecord.classForName(classname);
						} catch (final MonitoringRecordException ex) { // NOPMD (ExceptionAsFlowControl); need this to
																		// distinguish error by
																		// abortDueToUnknownRecordType
							if (!this.ignoreUnknownRecordTypes) {
								// log message will be dumped in the Exception handler below
								abortDueToUnknownRecordType = true;
								throw new MonitoringRecordException("Failed to load record type " + classname, ex);
							} else if (!this.unknownTypesObserved.contains(classname)) {
								LOG.error("Failed to load record type " + classname, ex); // log once for this type
								this.unknownTypesObserved.add(classname);
							}
							continue; // skip this ignored record
						}
						final long loggingTimestamp = Long.parseLong(recordFields[1]);
						final int skipValues;
						// check for Kieker < 1.6 OperationExecutionRecords
						if ((recordFields.length == 11) && clazz.equals(OperationExecutionRecord.class)) {
							skipValues = 3;
						} else {
							skipValues = 2;
						}

						record = AbstractMonitoringRecord.createFromStringArray(clazz,
								Arrays.copyOfRange(recordFields, skipValues, recordFields.length));
						record.setLoggingTimestamp(loggingTimestamp);
					} else { // legacy record
						final String[] recordFieldsReduced = new String[recordFields.length - 1];
						System.arraycopy(recordFields, 1, recordFieldsReduced, 0, recordFields.length - 1);
						record = AbstractMonitoringRecord.createFromStringArray(OperationExecutionRecord.class,
								recordFieldsReduced);
					}
				} catch (final MonitoringRecordException ex) { // NOPMD (exception as flow control)
					if (abortDueToUnknownRecordType) {
						this.terminate(); // at least it doesn't hurt to set it
						final IOException newEx = new IOException("Error processing line: " + line);
						newEx.initCause(ex);
						throw newEx; // NOPMD (cause is set above)
					} else {
						if (LOG.isWarnEnabled()) {
							LOG.warn("Error processing line: " + line, ex);
						}
						continue; // skip this record
					}
				}
				if (!this.recordReceiver.newMonitoringRecord(record)) {
					this.terminate();
					break; // we got the signal to stop processing
				}
			}
		} catch (final IOException e) {
			LOG.error("Error reading " + inputFile, e); // NOPMD (guard not necessary for error level)
		} catch (final RuntimeException e) { // NOCS NOPMD (gonna catch them all)
			LOG.error("Error reading " + inputFile, e); // NOPMD (guard not necessary for error level)
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException ex) {
					LOG.error("Exception while closing input stream for processing input file", ex);
				}
			}
		}
	}

	@Override
	protected FileExtensionFilter getFileExtensionFilter() {
		return (this.shouldDecompress) ? FileExtensionFilter.ZIP : FileExtensionFilter.DAT; // NOCS
	}

	@Override
	protected void onEndOfRun() {
		this.recordReceiver.newEndOfFileRecord();
	}

}
