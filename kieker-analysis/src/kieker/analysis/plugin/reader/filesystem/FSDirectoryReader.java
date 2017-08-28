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

package kieker.analysis.plugin.reader.filesystem;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kieker.analysis.plugin.reader.util.IMonitoringRecordReceiver;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.util.filesystem.BinaryCompressionMethod;
import kieker.common.util.filesystem.FSUtil;

/**
 * Reads the contents of a single file system log directory and passes the records to the registered receiver of type {@link IMonitoringRecordReceiver}.
 *
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller
 *
 * @since 1.2
 */
final class FSDirectoryReader implements Runnable {
	private static final Log LOG = LogFactory.getLog(FSDirectoryReader.class);

	String filePrefix = FSUtil.FILE_PREFIX; // NOPMD NOCS (package visible for inner class)

	private final Map<Integer, String> stringRegistry = new HashMap<Integer, String>(); // NOPMD (no synchronization needed)

	private final IMonitoringRecordReceiver recordReceiver;
	private final File inputDir;
	private boolean terminated;

	private final boolean ignoreUnknownRecordTypes;
	// This set of classes is used to filter only records of a specific type. The value null means all record types are read.
	private final Set<String> unknownTypesObserved = new HashSet<String>();

	/**
	 * Creates a new instance of this class.
	 *
	 * @param inputDir
	 *            The File object for the input directory.
	 * @param recordReceiver
	 *            The receiver handling the records.
	 * @param ignoreUnknownRecordTypes
	 *            select only records of this type; null selects all
	 */
	public FSDirectoryReader(final File inputDir, final IMonitoringRecordReceiver recordReceiver,
			final boolean ignoreUnknownRecordTypes) {
		if ((inputDir == null) || !inputDir.isDirectory()) {
			throw new IllegalArgumentException("Invalid or empty inputDir");
		}
		this.inputDir = inputDir;
		this.recordReceiver = recordReceiver;
		this.ignoreUnknownRecordTypes = ignoreUnknownRecordTypes;
	}

	/**
	 * Starts reading and returns after each record has been passed to the registered {@link #recordReceiver}.
	 */
	@Override
	public final void run() {
		this.readMappingFile(); // must be the first line to set filePrefix!
		final File[] inputFiles = this.inputDir.listFiles(new FileFilter() {

			@Override
			public boolean accept(final File pathname) {
				final String name = pathname.getName();
				return pathname.isFile()
						&& name.startsWith(FSDirectoryReader.this.filePrefix)
						&& (name.endsWith(FSUtil.NORMAL_FILE_EXTENSION) || BinaryCompressionMethod.hasValidFileExtension(name));
			}
		});
		if (inputFiles == null) {
			LOG.error("Directory '" + this.inputDir + "' does not exist or an I/O error occured.");
		} else if (inputFiles.length == 0) {
			// level 'warn' for this case, because this is not unusual for large monitoring logs including a number of directories
			LOG.warn("Directory '" + this.inputDir + "' contains no files starting with '" + this.filePrefix + "' and ending with a valid file extension.");
		} else { // everything ok, we process the files
			Arrays.sort(inputFiles, new Comparator<File>() {

				@Override
				public final int compare(final File f1, final File f2) {
					return f1.compareTo(f2); // simplified (we expect no dirs!)
				}
			});
			boolean ignoreUnknownRecordTypesWarningAlreadyShown = false;
			for (final File inputFile : inputFiles) {
				if (this.terminated) {
					LOG.info("Shutting down DirectoryReader.");
					break;
				}
				LOG.info("< Loading " + inputFile.getAbsolutePath());
				if (inputFile.getName().endsWith(FSUtil.NORMAL_FILE_EXTENSION)) {
					this.processNormalInputFile(inputFile);
				} else {
					if (this.ignoreUnknownRecordTypes && ignoreUnknownRecordTypesWarningAlreadyShown) {
						ignoreUnknownRecordTypesWarningAlreadyShown = true;
						LOG.warn("The property '" + FSReader.CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES
								+ "' is not supported for binary files. But trying to read '" + inputFile + "'");
					}
					try {
						final BinaryCompressionMethod method = BinaryCompressionMethod.getByFileExtension(inputFile.getName());
						this.processBinaryInputFile(inputFile, method);
					} catch (final IllegalArgumentException ex) {
						LOG.warn("Unknown file extension for file " + inputFile);
						continue;
					}
				}
			}
		}
		this.recordReceiver.newEndOfFileRecord();
	}

	/**
	 * Reads the mapping file located in the directory.
	 */
	private final void readMappingFile() {
		File mappingFile = new File(this.inputDir.getAbsolutePath() + File.separator + FSUtil.MAP_FILENAME);
		if (!mappingFile.exists()) {
			// No mapping file found. Check whether we find a legacy tpmon.map file!
			mappingFile = new File(this.inputDir.getAbsolutePath() + File.separator + FSUtil.LEGACY_MAP_FILENAME);
			if (mappingFile.exists()) {
				LOG.info("Directory '" + this.inputDir + "' contains no file '" + FSUtil.MAP_FILENAME + "'. Found '" + FSUtil.LEGACY_MAP_FILENAME
						+ "' ... switching to legacy mode");
				this.filePrefix = FSUtil.LEGACY_FILE_PREFIX;
			} else {
				// no {kieker|tpmon}.map exists. This is valid for very old monitoring logs. Hence, only dump a log.warn
				LOG.warn("No mapping file in directory '" + this.inputDir.getAbsolutePath() + "'");
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
					LOG.error("Failed to parse line: {" + line + "} from file " + mappingFile.getAbsolutePath()
							+ ". Each line must contain ID=VALUE pairs.");
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
					LOG.error("Found addional entry for id='" + id + "', old value was '" + prevVal + "' new value is '" + value + "'");
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
	private final void processNormalInputFile(final File inputFile) {
		boolean abortDueToUnknownRecordType = false;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), FSUtil.ENCODING));
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
							LOG.error("Illegal record format: " + line);
							continue; // skip this record
						}
						final Integer id = Integer.valueOf(recordFields[0].substring(1));
						final String classname = this.stringRegistry.get(id);
						if (classname == null) {
							LOG.error("Missing classname mapping for record type id " + "'" + id + "'");
							continue; // skip this record
						}
						Class<? extends IMonitoringRecord> clazz = null;
						try { // NOCS (nested try)
							clazz = AbstractMonitoringRecord.classForName(classname);
						} catch (final MonitoringRecordException ex) { // NOPMD (ExceptionAsFlowControl); need this to distinguish error by
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

						record = AbstractMonitoringRecord.createFromStringArray(clazz, Arrays.copyOfRange(recordFields, skipValues, recordFields.length));
						record.setLoggingTimestamp(loggingTimestamp);
					} else { // legacy record
						final String[] recordFieldsReduced = new String[recordFields.length - 1];
						System.arraycopy(recordFields, 1, recordFieldsReduced, 0, recordFields.length - 1);
						record = AbstractMonitoringRecord.createFromStringArray(OperationExecutionRecord.class, recordFieldsReduced);
					}
				} catch (final MonitoringRecordException ex) { // NOPMD (exception as flow control)
					if (abortDueToUnknownRecordType) {
						this.terminated = true; // at least it doesn't hurt to set it
						final IOException newEx = new IOException("Error processing line: " + line);
						newEx.initCause(ex);
						throw newEx; // NOPMD (cause is set above)
					} else {
						LOG.warn("Error processing line: " + line, ex);
						continue; // skip this record
					}
				}
				if (!this.recordReceiver.newMonitoringRecord(record)) {
					this.terminated = true;
					break; // we got the signal to stop processing
				}
			}
		} catch (final Exception ex) { // NOCS NOPMD (gonna catch them all)
			LOG.error("Error reading " + inputFile, ex);
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

	/**
	 * Reads the records contained in the given binary file and passes them to the registered {@link #recordReceiver}.
	 *
	 * @param inputFile
	 *            The input file which should be processed.
	 * @param compressionMethod
	 *            Whether the input file is compressed.
	 */
	private final void processBinaryInputFile(final File inputFile, final BinaryCompressionMethod method) {
		DataInputStream in = null;
		try {
			in = method.getDataInputStream(inputFile, 1024 * 1024); // 1 MiB buffer
			while (true) {
				final Integer id;
				try {
					id = in.readInt();
				} catch (final EOFException eof) {
					break; // we are finished
				}
				final String classname = this.stringRegistry.get(id);
				if (classname == null) {
					LOG.error("Missing classname mapping for record type id " + "'" + id + "'");
					break; // we can't easily recover on errors
				}

				final Class<? extends IMonitoringRecord> clazz = AbstractMonitoringRecord.classForName(classname);
				final Class<?>[] typeArray = AbstractMonitoringRecord.typesForClass(clazz);

				// read record
				final long loggingTimestamp = in.readLong(); // NOPMD (must be read here!)
				final Object[] objectArray = new Object[typeArray.length];
				int idx = -1;
				for (final Class<?> type : typeArray) {
					idx++;
					if (type == String.class) {
						final Integer strId = in.readInt();
						final String str = this.stringRegistry.get(strId);
						if (str == null) {
							LOG.error("No String mapping found for id " + strId.toString());
							objectArray[idx] = "";
						} else {
							objectArray[idx] = str;
						}
					} else if ((type == int.class) || (type == Integer.class)) {
						objectArray[idx] = in.readInt();
					} else if ((type == long.class) || (type == Long.class)) {
						objectArray[idx] = in.readLong();
					} else if ((type == float.class) || (type == Float.class)) {
						objectArray[idx] = in.readFloat();
					} else if ((type == double.class) || (type == Double.class)) {
						objectArray[idx] = in.readDouble();
					} else if ((type == byte.class) || (type == Byte.class)) {
						objectArray[idx] = in.readByte();
					} else if ((type == short.class) || (type == Short.class)) { // NOPMD (short)
						objectArray[idx] = in.readShort();
					} else if ((type == boolean.class) || (type == Boolean.class)) {
						objectArray[idx] = in.readBoolean();
					} else {
						if (in.readByte() != 0) {
							LOG.error("Unexpected value for unsupported type: " + clazz.getName());
							return; // breaking error (break would not terminate the correct loop)
						}
						LOG.warn("Unsupported type: " + clazz.getName());
						objectArray[idx] = null;
					}
				}
				final IMonitoringRecord record = AbstractMonitoringRecord.createFromArray(clazz, objectArray);
				record.setLoggingTimestamp(loggingTimestamp);
				if (!this.recordReceiver.newMonitoringRecord(record)) {
					this.terminated = true;
					break; // we got the signal to stop processing
				}
			}
		} catch (final Exception ex) { // NOPMD NOCS (catch Exception)
			LOG.error("Error reading " + inputFile, ex);
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
}
