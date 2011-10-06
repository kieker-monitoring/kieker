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
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.text.Collator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.StringTokenizer;

import kieker.analysis.reader.MonitoringReaderException;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;
import kieker.common.record.MonitoringRecordTypeRegistry;
import kieker.common.record.OperationExecutionRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Reads the contents of a single file system log directory and passes the
 * records to the registered receiver of type {@link IMonitoringRecordReceiver}.
 * 
 * @author Matthias Rohr, Andre van Hoorn
 */
class FSDirectoryReader {

	private static final Log LOG = LogFactory.getLog(FSDirectoryReader.class);

	private static final boolean OLD_KIEKER_EXECUTION_RECORD_COMPATIBILITY_MODE = true;

	private final MonitoringRecordTypeRegistry typeRegistry = new MonitoringRecordTypeRegistry(FSDirectoryReader.OLD_KIEKER_EXECUTION_RECORD_COMPATIBILITY_MODE);
	private volatile boolean recordTypeIdMapInitialized = false; // will read it
																	// "on-demand"
	private File inputDir = null;
	/**
	 * Together with the member recordTypeIdIgnoreList, used to filter only
	 * records of a specific type. The value null means all record types are
	 * read.
	 * 
	 * @see #recordTypeIdIgnoreList
	 */
	private final HashSet<String> recordTypeSelector; // Set of classnames
	/** Records whose ID is in this list are simply skipped by the reader */
	private final HashSet<Integer> recordTypeIdIgnoreList = new HashSet<Integer>();

	private final IMonitoringRecordReceiver recordReceiver;

	/**
	 * Must not be used for construction.
	 */
	@SuppressWarnings("unused")
	private FSDirectoryReader() {
		this.recordTypeSelector = null;
		this.recordReceiver = null;
	}

	/**
	 * 
	 * @param inputDirName
	 * @param readOnlyRecordsOfType
	 *            select only records of this type; null selects all
	 */
	public FSDirectoryReader(final String inputDirName, final IMonitoringRecordReceiver recordReceiver,
			final Collection<Class<? extends IMonitoringRecord>> readOnlyRecordsOfType) {
		initInputDir(inputDirName); // throws IllegalArgumentException
		this.recordReceiver = recordReceiver;
		if (readOnlyRecordsOfType != null) {
			this.recordTypeSelector = new HashSet<String>();
			for (final Class<? extends IMonitoringRecord> recordType : readOnlyRecordsOfType) {
				this.recordTypeSelector.add(recordType.getName());
			}
		} else {
			this.recordTypeSelector = null; // ready records of any type
		}
	}

	private void initInputDir(final String inputDirName) throws IllegalArgumentException {
		if ((inputDirName == null) || inputDirName.equals("")) {
			throw new IllegalArgumentException("Invalid or empty inputDir: " + inputDirName);
		}
		this.inputDir = new File(inputDirName);
	}

	private String filePrefix = "kieker";
	private static final String LEGAY_FILE_PREFIX = "tpmon";
	private final String filePostfix = ".dat";

	/**
	 * Starts reading and returns after each record has been passed to the
	 * registered {@link #recordReceiver}.
	 * 
	 * Errors must be indicated by throwing an {@link Exception}.
	 * 
	 */
	public void read() throws Exception {
		readMappingFile();
		final File[] inputFiles = this.inputDir.listFiles(new FileFilter() {

			@Override
			public boolean accept(final File pathname) {
				return pathname.isFile() && pathname.getName().startsWith(FSDirectoryReader.this.filePrefix)
						&& pathname.getName().endsWith(FSDirectoryReader.this.filePostfix);
			}
		});

		if (inputFiles == null) {
			throw new MonitoringReaderException("Directory '" + this.inputDir + "' does not exist or an I/O error occured.");
		}

		if (inputFiles.length == 0) {
			throw new MonitoringReaderException("Directory '" + this.inputDir + "' contains no files starting with '" + this.filePrefix + "' and ending with '"
					+ this.filePostfix + "' could be found.");
		}

		Arrays.sort(inputFiles, new FileComparator()); // sort alphabetically
		for (final File inputFile : inputFiles) {
			processInputFile(inputFile);
		}
	}

	/**
	 * Reads the mapping file located in the directory and loads the required {@link IMonitoringRecord} types (i.e., classes).
	 * 
	 * @throws IOException
	 */
	private void readMappingFile() throws IOException {
		File mappingFile = new File(this.inputDir.getAbsolutePath() + File.separator + "kieker.map");

		if (!mappingFile.exists()) {
			/*
			 * No mapping file found. Check whether we find a legacy tpmon.map
			 * file
			 */
			mappingFile = new File(this.inputDir.getAbsolutePath() + File.separator + "tpmon.map");
			if (mappingFile.exists()) {
				FSDirectoryReader.LOG.warn("directory '" + this.inputDir + "' contains no file 'kieker.map'");
				FSDirectoryReader.LOG.info("Found 'tpmon.map' ... switching to legacy mode");
				this.filePrefix = FSDirectoryReader.LEGAY_FILE_PREFIX;
			} else {
				// no {kieker|tpmon}.map exists. This is valid for very old
				// monitoring logs. Hence, only dump a log.warn
				FSDirectoryReader.LOG.warn("No mapping file in directory '" + this.inputDir.getAbsolutePath() + "'");
			}
		}

		BufferedReader in = null;
		StringTokenizer st = null;
		try {
			in = new BufferedReader(new FileReader(mappingFile));
			String line;

			while ((line = in.readLine()) != null) {
				try {
					st = new StringTokenizer(line, "=");
					final int numTokens = st.countTokens();
					if (numTokens == 0) {
						continue;
					}
					if (numTokens != 2) {
						throw new IllegalArgumentException("Invalid number of tokens (" + numTokens + ") Expecting 2");
					}
					final String idStr = st.nextToken();
					// the leading $ is optional
					final Integer id = Integer.valueOf(idStr.startsWith("$") ? idStr.substring(1) : idStr);
					final String classname = st.nextToken();

					if ((this.recordTypeSelector == null) || this.recordTypeSelector.contains(classname)) {
						this.typeRegistry.registerRecordTypeIdMapping(id, classname);
					} else if (FSDirectoryReader.OLD_KIEKER_EXECUTION_RECORD_COMPATIBILITY_MODE
							&& classname.equals(MonitoringRecordTypeRegistry.OLD_KIEKEREXECUTIONRECORD_CLASSNAME)) {
						FSDirectoryReader.LOG.info("Using compatibility mode for mapping " + classname);
						this.typeRegistry.registerRecordTypeIdMapping(id, classname);
					} else {
						this.recordTypeIdIgnoreList.add(id);
						FSDirectoryReader.LOG.info("Ignoring record type for mapping " + line);
					}
				} catch (final ClassNotFoundException e) {
					FSDirectoryReader.LOG.error("Failed to parse line: {" + line + "} from file " + mappingFile.getAbsolutePath(), e);
					break;
				} catch (final IllegalArgumentException e) {
					FSDirectoryReader.LOG.error("Failed to parse line: {" + line + "} from file " + mappingFile.getAbsolutePath(), e);
					break;
				}
			}
			this.recordTypeIdMapInitialized = true;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final Exception e) {
					FSDirectoryReader.LOG.error("Exception", e);
				}
			}
		}
	}

	/**
	 * Reads the records contained in the given file and passes them to the
	 * registered {@link #recordReceiver}.
	 * 
	 * @param input
	 * @throws IOException
	 * @throws MonitoringReaderException
	 */
	private void processInputFile(final File input) throws IOException, MonitoringReaderException {
		FSDirectoryReader.LOG.info("< Loading " + input.getAbsolutePath());

		BufferedReader in = null;
		StringTokenizer st = null;

		try {
			in = new BufferedReader(new FileReader(input));
			String line;

			curRecord: while ((line = in.readLine()) != null) {
				IMonitoringRecord rec = null;
				try {
					if (!this.recordTypeIdMapInitialized && line.startsWith("$")) {
						readMappingFile();
						this.recordTypeIdMapInitialized = true;
					}
					st = new StringTokenizer(line, ";");
					final int numTokens = st.countTokens();
					String[] vec = null;
					boolean haveTypeId = false;

					for (int i = 0; st.hasMoreTokens(); i++) {
						String token = st.nextToken();
						if ((i == 0) && token.startsWith("$")) {
							/*
							 * We found a record type ID and need to lookup the
							 * class
							 */
							final Integer id = Integer.valueOf(token.substring(1));

							if (this.recordTypeIdIgnoreList.contains(id)) {
								/*
								 * skip this record since records of this type
								 * are being ignored
								 */
								continue curRecord;
							}

							final Class<? extends IMonitoringRecord> clazz = this.typeRegistry.fetchClassForRecordTypeId(id);
							if (clazz == null) {
								FSDirectoryReader.LOG.fatal("Missing classname mapping for record type id " + "'" + id + "'");
								throw new IllegalStateException("Missing classname mapping for record type id " + "'" + id + "'");
							}
							rec = clazz.newInstance();
							token = st.nextToken();
							rec.setLoggingTimestamp(Long.valueOf(token));
							vec = new String[numTokens - 2];
							haveTypeId = true;
						} else if (i == 0) { // for historic reasons, this is the default type
							rec = new OperationExecutionRecord();
							vec = new String[numTokens];
						}
						if (!haveTypeId || (i > 0)) { // only if current field
														// is not the id
							vec[haveTypeId ? i - 1 : i] = token;
						}
					}
					if (vec == null) {
						vec = new String[0];
					}

					final Object[] typedArray = fromStringToTypedArray(vec, rec.getValueTypes());
					rec.initFromArray(typedArray);

				} catch (final InstantiationException e) {
					FSDirectoryReader.LOG.error("Failed to process line: {" + line + "} from file " + input.getAbsolutePath(), e);
					FSDirectoryReader.LOG.error("Abort reading");
					throw new MonitoringReaderException("LogReaderExecutionException ", e);
				} catch (final IllegalAccessException e) {
					FSDirectoryReader.LOG.error("Failed to process line: {" + line + "} from file " + input.getAbsolutePath(), e);
					FSDirectoryReader.LOG.error("Abort reading");
					throw new MonitoringReaderException("LogReaderExecutionException ", e);
				} catch (final IllegalStateException e) {
					FSDirectoryReader.LOG.error("Failed to process line: {" + line + "} from file " + input.getAbsolutePath(), e);
					FSDirectoryReader.LOG.error("Abort reading");
					throw new MonitoringReaderException("LogReaderExecutionException ", e);
				}

				/* Deliver record */
				if (!this.recordReceiver.newMonitoringRecord(rec)) {
					final String errorMsg = "failed to deliver record. Will terminate";
					FSDirectoryReader.LOG.error(errorMsg);
					throw new MonitoringReaderException(errorMsg);
				}
			}
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final Exception e) {
					FSDirectoryReader.LOG.error("Exception", e);
				}
			}
		}
	}

	private Object[] fromStringToTypedArray(final String[] vec, final Class<?>[] valueTypes) throws IllegalArgumentException {
		final Object[] typedArray = new Object[vec.length];
		int curIdx = -1;
		for (final Class<?> clazz : valueTypes) {
			curIdx++;
			if (clazz == String.class) {
				typedArray[curIdx] = vec[curIdx];
				continue;
			}
			if ((clazz == int.class) || (clazz == Integer.class)) {
				typedArray[curIdx] = Integer.valueOf(vec[curIdx]);
				continue;
			}
			if ((clazz == long.class) || (clazz == Long.class)) {
				typedArray[curIdx] = Long.valueOf(vec[curIdx]);
				continue;
			}
			if ((clazz == float.class) || (clazz == Float.class)) {
				typedArray[curIdx] = Float.valueOf(vec[curIdx]);
				continue;
			}
			if ((clazz == double.class) || (clazz == Double.class)) {
				typedArray[curIdx] = Double.valueOf(vec[curIdx]);
				continue;
			}
			if ((clazz == byte.class) || (clazz == Byte.class)) {
				typedArray[curIdx] = Byte.valueOf(vec[curIdx]);
				continue;
			}
			if ((clazz == short.class) || (clazz == Short.class)) {
				typedArray[curIdx] = Short.valueOf(vec[curIdx]);
				continue;
			}
			if ((clazz == boolean.class) || (clazz == Boolean.class)) {
				typedArray[curIdx] = Boolean.valueOf(vec[curIdx]);
				continue;
			}
			throw new IllegalArgumentException("Unsupported type: " + clazz.getName());
		}
		return typedArray;
	}

	/**
	 * source:
	 * http://weblog.janek.org/Archive/2005/01/16/HowtoSortFilesandDirector.html
	 */
	private static class FileComparator implements Comparator<File> {

		private final Collator c = Collator.getInstance();

		@Override
		public int compare(final File f1, final File f2) {
			if (f1 == f2) {
				return 0;
			}

			if (f1.isDirectory() && f2.isFile()) {
				return -1;
			}
			if (f1.isFile() && f2.isDirectory()) {
				return 1;
			}

			return this.c.compare(f1.getName(), f2.getName());
		}
	}
}
