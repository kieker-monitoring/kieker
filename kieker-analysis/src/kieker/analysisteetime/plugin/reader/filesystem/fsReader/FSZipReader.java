/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import kieker.analysis.plugin.reader.util.IMonitoringRecordReceiver;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.util.filesystem.FSUtil;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Reads the contents of a single zip file and passes the records to the registered receiver of type {@link IMonitoringRecordReceiver}.
 *
 * @author Jan Waller
 *
 * @since 1.7
 */
public final class FSZipReader implements Runnable {
	private static final Log LOG = LogFactory.getLog(FSZipReader.class);

	private final File zipFile;
	private final IMonitoringRecordReceiver recordReceiver;
	private final boolean ignoreUnknownRecordTypes;

	private final Map<Integer, String> stringRegistry = new HashMap<Integer, String>(); // NOPMD (no synchronization needed)

	private boolean terminated;

	// This set of classes is used to filter only records of a specific type. The value null means all record types are read.
	private final Set<String> unknownTypesObserved = new HashSet<String>();

	/**
	 * Creates a new instance of this class.
	 *
	 * @param zipFile
	 *            The File object for the zip file.
	 * @param recordReceiver
	 *            The receiver handling the records.
	 * @param ignoreUnknownRecordTypes
	 *            select only records of this type; null selects all
	 */
	public FSZipReader(final File zipFile, final IMonitoringRecordReceiver recordReceiver, final boolean ignoreUnknownRecordTypes) {
		if ((zipFile == null) || !zipFile.isFile() || !zipFile.getName().endsWith(FSUtil.ZIP_FILE_EXTENSION)) {
			throw new IllegalArgumentException("Invalid zip file");
		}
		this.zipFile = zipFile;
		this.recordReceiver = recordReceiver;
		this.ignoreUnknownRecordTypes = ignoreUnknownRecordTypes;
	}

	/**
	 * Starts reading and returns after each record has been passed to the registered {@link #recordReceiver}.
	 */
	@Override
	public final void run() {
		ZipInputStream zipInputStream = null;
		try {
			zipInputStream = new ZipInputStream(new FileInputStream(this.zipFile));
			ZipEntry zipEntry;
			while ((null != (zipEntry = zipInputStream.getNextEntry())) && !zipEntry.getName().equals(FSUtil.MAP_FILENAME)) { // NOCS NOPMD
				// do nothing, just skip to the map file if present
			}
			if (null == zipEntry) {
				LOG.error("The zip file does not contain a Kieker log: " + this.zipFile.toString());
				this.recordReceiver.newEndOfFileRecord();
				return;
			}
			// read mapping file
			this.readMappingFile(zipInputStream);
		} catch (final IOException ex) {
			LOG.error("Error accessing ZipInputStream", ex);
			this.recordReceiver.newEndOfFileRecord();
			return;
		} finally {
			if (null != zipInputStream) {
				try {
					zipInputStream.close();
				} catch (final IOException ex) {
					LOG.error("Failed to close ZipInputStream", ex);
				}
			}
		}
		// when we arrive here, we have a valid zip file and read the mapping file
		// start again with the zip file
		BufferedReader reader = null;
		DataInputStream input = null;
		try {
			zipInputStream = new ZipInputStream(new FileInputStream(this.zipFile));
			reader = new BufferedReader(new InputStreamReader(zipInputStream, FSUtil.ENCODING));
			input = new DataInputStream(new BufferedInputStream(zipInputStream, 1024 * 1024));
			ZipEntry zipEntry;
			while (null != (zipEntry = zipInputStream.getNextEntry())) { // NOCS NOPMD
				if (this.terminated) {
					LOG.info("Shutting down DirectoryReader.");
					break;
				}
				final String filename = zipEntry.getName();
				if (filename.startsWith(FSUtil.FILE_PREFIX)) {
					if (filename.endsWith(FSUtil.NORMAL_FILE_EXTENSION)) {
						LOG.info("< Loading " + filename);
						this.readAsciiFile(reader);
					} else if (filename.endsWith(".bin")) {
						LOG.info("< Loading " + filename);
						if (this.ignoreUnknownRecordTypes) { // NOPMD (deeply nested if)
							LOG.warn("The property 'ignoreUnknownRecordTypes' is not supported for binary files. But trying to read '" + filename + "'");
						}
						this.readBinaryFile(input);
					}
				}
			}
		} catch (final IOException ex) {
			LOG.error("Error accessing ZipInputStream", ex);
			this.recordReceiver.newEndOfFileRecord();
			return;
		} finally {
			try {
				zipInputStream.close();
			} catch (final IOException ex) {
				LOG.error("Failed to close ZipInputStream", ex);
			}
			try {
				if (null != reader) {
					reader.close();
				}
			} catch (final IOException ex) {
				LOG.error("Failed to close ZipInputStream", ex);
			}
			try {
				if (null != input) {
					input.close();
				}
			} catch (final IOException ex) {
				LOG.error("Failed to close ZipInputStream", ex);
			}
		}
		this.recordReceiver.newEndOfFileRecord();
	}

	@SuppressFBWarnings("REC_CATCH_EXCEPTION")
	private final void readBinaryFile(final DataInputStream input) {
		try {
			while (true) {
				final Integer id;
				try {
					id = input.readInt();
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
				final long loggingTimestamp = input.readLong(); // NOPMD (must be read here!)
				final Object[] objectArray = new Object[typeArray.length];
				int idx = -1;
				for (final Class<?> type : typeArray) {
					idx++;
					if (type == String.class) {
						final Integer strId = input.readInt();
						final String str = this.stringRegistry.get(strId);
						if (str == null) {
							LOG.error("No String mapping found for id " + strId.toString());
							objectArray[idx] = "";
						} else {
							objectArray[idx] = str;
						}
					} else if ((type == int.class) || (type == Integer.class)) {
						objectArray[idx] = input.readInt();
					} else if ((type == long.class) || (type == Long.class)) {
						objectArray[idx] = input.readLong();
					} else if ((type == float.class) || (type == Float.class)) {
						objectArray[idx] = input.readFloat();
					} else if ((type == double.class) || (type == Double.class)) {
						objectArray[idx] = input.readDouble();
					} else if ((type == byte.class) || (type == Byte.class)) {
						objectArray[idx] = input.readByte();
					} else if ((type == short.class) || (type == Short.class)) { // NOPMD (short)
						objectArray[idx] = input.readShort();
					} else if ((type == boolean.class) || (type == Boolean.class)) {
						objectArray[idx] = input.readBoolean();
					} else {
						if (input.readByte() != 0) {
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
			LOG.error("Error reading " + this.zipFile.toString(), ex);
		}
	}

	@SuppressFBWarnings("REC_CATCH_EXCEPTION")
	private final void readAsciiFile(final BufferedReader reader) {
		boolean abortDueToUnknownRecordType = false;
		try {
			String line;
			while ((line = reader.readLine()) != null) { // NOPMD (assign)
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
			LOG.error("Error reading " + this.zipFile.toString(), ex);
		}
	}

	/**
	 * Reads the mapping file located in the zip file.
	 *
	 * @throws IOException
	 */
	private final void readMappingFile(final ZipInputStream zipInputStream) throws IOException {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(zipInputStream, FSUtil.ENCODING));
			String line;
			while ((line = in.readLine()) != null) { // NOPMD (assign)
				if (line.length() == 0) {
					continue; // ignore empty lines
				}
				final int split = line.indexOf('=');
				if (split == -1) {
					LOG.error("Failed to parse line: {" + line + "} from mapping file in zip file " + this.zipFile.toString()
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
		} finally {
			if (in != null) {
				in.close(); // this also closes the zipInputStream
			}
		}
	}
}
