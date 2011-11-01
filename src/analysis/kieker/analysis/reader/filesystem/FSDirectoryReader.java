/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.analysis.reader.filesystem;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;
import kieker.common.record.OperationExecutionRecord;

/**
 * Reads the contents of a single file system log directory and passes the records to the registered receiver of type {@link IMonitoringRecordReceiver}.
 * 
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller
 */
final class FSDirectoryReader implements Runnable {
	private static final Log LOG = LogFactory.getLog(FSDirectoryReader.class);

	private static final String OLD_KIEKEREXECUTIONRECORD_CLASSNAME = "kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord";
	private static final String LEGACY_FILE_PREFIX = "tpmon";
	private static final String NORMAL_FILE_PREFIX = "kieker";
	private static final String NORMAL_FILE_POSTFIX = ".dat";
	private static final String BINARY_FILE_POSTFIX = ".bin";

	private final Map<Integer, String> stringRegistry = new HashMap<Integer, String>();
	// This set of classnames is used to filter only records of a specific type. The value null means all record types are read.
	private final Set<String> recordTypeSelector;
	private final IMonitoringRecordReceiver recordReceiver;
	private final File inputDir;
	private String filePrefix = FSDirectoryReader.NORMAL_FILE_PREFIX;
	private boolean terminated = false;

	/**
	 * 
	 * @param inputDirName
	 * @param readOnlyRecordsOfType
	 *            select only records of this type; null selects all
	 */
	public FSDirectoryReader(final String inputDirName, final IMonitoringRecordReceiver recordReceiver,
			final Collection<Class<? extends IMonitoringRecord>> readOnlyRecordsOfType) {
		if ((inputDirName == null) || inputDirName.isEmpty()) {
			throw new IllegalArgumentException("Invalid or empty inputDir: " + inputDirName);
		}
		this.inputDir = new File(inputDirName);
		this.recordReceiver = recordReceiver;
		if (readOnlyRecordsOfType != null) {
			this.recordTypeSelector = new HashSet<String>();
			for (final Class<? extends IMonitoringRecord> recordType : readOnlyRecordsOfType) {
				this.recordTypeSelector.add(recordType.getName());
			}
		} else {
			this.recordTypeSelector = null; // NOPMD (read records of any type)
		}
	}

	/**
	 * Starts reading and returns after each record has been passed to the registered {@link #recordReceiver}.
	 * 
	 * Errors must be indicated by throwing an {@link RuntimeException}.
	 */
	@Override
	public final void run() {
		this.readMappingFile(); // must be the first line to set filePrefix!
		final File[] inputFiles = this.inputDir.listFiles(new FileFilter() {
			@Override
			public boolean accept(final File pathname) {
				return pathname.isFile()
						&& pathname.getName().startsWith(FSDirectoryReader.this.filePrefix)
						&& (pathname.getName().endsWith(FSDirectoryReader.NORMAL_FILE_POSTFIX) || pathname.getName().endsWith(FSDirectoryReader.BINARY_FILE_POSTFIX));
			}
		});
		if (inputFiles == null) {
			FSDirectoryReader.LOG.error("Directory '" + this.inputDir + "' does not exist or an I/O error occured.");
		} else if (inputFiles.length == 0) {
			FSDirectoryReader.LOG.error("Directory '" + this.inputDir + "' contains no files starting with '" + this.filePrefix + "' and ending with '"
					+ FSDirectoryReader.NORMAL_FILE_POSTFIX + "' or '" + FSDirectoryReader.BINARY_FILE_POSTFIX + "'.");
		} else { // everything ok, we process the files
			Arrays.sort(inputFiles, new Comparator<File>() {
				@Override
				public final int compare(final File f1, final File f2) {
					return f1.compareTo(f2); // simplified (we expect no dirs!)
				}
			});
			for (final File inputFile : inputFiles) {
				if (this.terminated) {
					FSDirectoryReader.LOG.info("Shutting down DirectoryReader.");
					break;
				}
				FSDirectoryReader.LOG.info("< Loading " + inputFile.getAbsolutePath());
				if (inputFile.getName().endsWith(FSDirectoryReader.NORMAL_FILE_POSTFIX)) {
					this.processNormalInputFile(inputFile);
				} else if (inputFile.getName().endsWith(FSDirectoryReader.BINARY_FILE_POSTFIX)) {
					this.processBinaryInputFile(inputFile);
				}
			}
		}
		this.recordReceiver.newMonitoringRecord(FSReader.EOF);
	}

	/**
	 * Reads the mapping file located in the directory.
	 * 
	 * @throws IOException
	 */
	private final void readMappingFile() {
		File mappingFile = new File(this.inputDir.getAbsolutePath() + File.separator + "kieker.map");
		if (!mappingFile.exists()) {
			// No mapping file found. Check whether we find a legacy tpmon.map file!
			mappingFile = new File(this.inputDir.getAbsolutePath() + File.separator + "tpmon.map");
			if (mappingFile.exists()) {
				FSDirectoryReader.LOG.info("Directory '" + this.inputDir + "' contains no file 'kieker.map'. Found 'tpmon.map' ... switching to legacy mode");
				this.filePrefix = FSDirectoryReader.LEGACY_FILE_PREFIX;
			} else {
				// no {kieker|tpmon}.map exists. This is valid for very old monitoring logs. Hence, only dump a log.warn
				FSDirectoryReader.LOG.warn("No mapping file in directory '" + this.inputDir.getAbsolutePath() + "'"); // NOCS (MultipleStringLiteralsCheck)
				return;
			}
		}
		// found any kind of mapping file
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(mappingFile)));
			String line;
			while ((line = in.readLine()) != null) {
				if (line.trim().length() == 0) {
					continue; // ignore empty lines
				}
				final String[] assignment = line.split("=");
				if (assignment.length != 2) {
					FSDirectoryReader.LOG.error("Failed to parse line: {" + line + "} from file " + mappingFile.getAbsolutePath()
							+ ". Each line must contain ID=VALUE pairs.");
					continue; // continue on errors
				}
				// the leading $ is optional
				final Integer id;
				try {
					id = Integer.valueOf((assignment[0].charAt(0) == '$') ? assignment[0].substring(1) : assignment[0]); // NOCS
				} catch (final NumberFormatException ex) {
					FSDirectoryReader.LOG.error("Error reading mapping file, id must be integer", ex);
					continue;
				}
				final String prevVal = this.stringRegistry.put(id, assignment[1]);
				if (prevVal != null) {
					FSDirectoryReader.LOG.error("Found addional entry for id='" + id + "', old value was '" + prevVal + "' new value is '" + assignment[1] + "'");
				}
			}
		} catch (final IOException ex) {
			FSDirectoryReader.LOG.error("Error reading mapping file", ex);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException ex) {
					FSDirectoryReader.LOG.error("Exception while closing input stream for mapping file", ex);
				}
			}
		}
	}

	/**
	 * Reads the records contained in the given normal file and passes them to the registered {@link #recordReceiver}.
	 * 
	 * @param inputFile
	 */
	private final void processNormalInputFile(final File inputFile) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
			String line;
			while ((line = in.readLine()) != null) {
				if (line.trim().length() == 0) {
					continue; // ignore empty lines
				}
				IMonitoringRecord record = null;
				String[] recordFields = line.split(";");
				try {
					if (recordFields[0].charAt(0) == '$') { // modern record
						if (recordFields.length < 3) {
							FSDirectoryReader.LOG.error("Illegal record format: " + line);
							continue; // skip this record
						}
						final Integer id = Integer.valueOf(recordFields[0].substring(1));
						String classname = this.stringRegistry.get(id);
						if (classname == null) {
							FSDirectoryReader.LOG.error("Missing classname mapping for record type id " + "'" + id + "'");
							continue; // skip this record
						}
						if (classname.equals(FSDirectoryReader.OLD_KIEKEREXECUTIONRECORD_CLASSNAME)) {
							classname = OperationExecutionRecord.class.getName();
						}
						if ((this.recordTypeSelector != null) && !this.recordTypeSelector.contains(classname)) {
							continue; // skip this ignored record
						}
						Class<? extends IMonitoringRecord> clazz = null;
						clazz = Class.forName(classname).asSubclass(IMonitoringRecord.class);
						record = clazz.newInstance();
						record.setLoggingTimestamp(Long.valueOf(recordFields[1]));
						recordFields = Arrays.copyOfRange(recordFields, 2, recordFields.length);
					} else { // legacy record
						record = new OperationExecutionRecord();
					}
					final Object[] typedArray = AbstractMonitoringRecord.fromStringArrayToTypedArray(recordFields, record.getValueTypes());
					record.initFromArray(typedArray);
				} catch (final Exception ex) {
					// ClassNotFoundException ClassCastException InstantiationException IllegalAccessException NumberFormatException IllegalArgumentException
					FSDirectoryReader.LOG.error("Error loading record type", ex);
					continue; // skip this record
				}
				if (!this.recordReceiver.newMonitoringRecord(record)) {
					this.terminated = true;
					break; // we got the signal to stop processing
				}
			}
		} catch (final IOException ex) {
			FSDirectoryReader.LOG.error("Error reading " + inputFile, ex);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException ex) {
					FSDirectoryReader.LOG.error("Exception while closing input stream for processing input file", ex);
				}
			}
		}
	}

	/**
	 * Reads the records contained in the given binary file and passes them to the registered {@link #recordReceiver}.
	 * 
	 * @param inputFile
	 */
	private final void processBinaryInputFile(final File inputFile) {
		DataInputStream in = null;
		try {
			in = new DataInputStream(new FileInputStream(inputFile));
			while (true) {
				final Integer id;
				try {
					id = in.readInt();
				} catch (final EOFException eof) {
					break; // we are finished
				}
				final String classname = this.stringRegistry.get(id);
				if (classname == null) {
					FSDirectoryReader.LOG.error("Missing classname mapping for record type id " + "'" + id + "'");
					break; // we can't easily recover on errors
				}
				Class<? extends IMonitoringRecord> clazz = null;
				clazz = Class.forName(classname).asSubclass(IMonitoringRecord.class);
				final IMonitoringRecord record = clazz.newInstance();
				record.setLoggingTimestamp(in.readLong());
				// read record
				final Class<?>[] typeArray = record.getValueTypes();
				final Object[] objectArray = new Object[typeArray.length];
				int idx = -1;
				for (final Class<?> type : typeArray) {
					idx++;
					if (type == String.class) {
						final Integer strId = in.readInt();
						final String str = this.stringRegistry.get(strId);
						if (str == null) {
							FSDirectoryReader.LOG.error("No String mapping found for id " + strId.toString());
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
					} else if ((type == short.class) || (type == Short.class)) {
						objectArray[idx] = in.readShort();
					} else if ((type == boolean.class) || (type == Boolean.class)) {
						objectArray[idx] = in.readBoolean();
					} else {
						if (in.readByte() != 0) {
							FSDirectoryReader.LOG.error("Unexpected value for unsupported type: " + clazz.getName());
							return; // breaking error (break would not terminate the correct loop)
						}
						FSDirectoryReader.LOG.warn("Unsupported type: " + clazz.getName());
						objectArray[idx] = null;
					}
				}
				record.initFromArray(objectArray);
				if ((this.recordTypeSelector == null) || this.recordTypeSelector.contains(classname)) {
					if (!this.recordReceiver.newMonitoringRecord(record)) {
						this.terminated = true;
						break; // we got the signal to stop processing
					}
				}
			}
		} catch (final Exception ex) {
			FSDirectoryReader.LOG.error("Error reading " + inputFile, ex);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException ex) {
					FSDirectoryReader.LOG.error("Exception while closing input stream for processing input file", ex);
				}
			}
		}
	}
}
