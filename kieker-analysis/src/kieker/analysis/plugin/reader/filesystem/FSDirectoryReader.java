/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.plugin.reader.depcompression.AbstractDecompressionFilter;
import kieker.analysis.plugin.reader.util.FSReaderUtil;
import kieker.analysis.plugin.reader.util.IMonitoringRecordReceiver;
import kieker.common.configuration.Configuration;
import kieker.common.registry.reader.ReaderRegistry;
import kieker.common.util.classpath.InstantiationFactory;
import kieker.common.util.filesystem.FSUtil;

/**
 * Reads the contents of a single file system log directory and passes the records to the registered receiver of type {@link IMonitoringRecordReceiver}.
 *
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller
 * @author Reiner Jung -- replaced reader code with code compatible with deserializer
 *
 * @since 1.2
 * @deprecated 1.15 replaced by teetime log reading facilities
 */
@Deprecated
final class FSDirectoryReader implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(FSDirectoryReader.class);

	String filePrefix = FSUtil.FILE_PREFIX; // NOPMD NOCS (package visible for inner class)

	private final ReaderRegistry<String> stringRegistry = new ReaderRegistry<>();

	private final IMonitoringRecordReceiver recordReceiver;
	private final File inputDir;
	private boolean terminated;

	private final boolean ignoreUnknownRecordTypes;

	private final TextFileStreamProcessor textFileStreamProcessor;

	private final BinaryFileStreamProcessor binaryFileStreamProcessor;

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
		this.textFileStreamProcessor = new TextFileStreamProcessor(ignoreUnknownRecordTypes, this.stringRegistry, recordReceiver);
		this.binaryFileStreamProcessor = new BinaryFileStreamProcessor(this.stringRegistry, recordReceiver);
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
						&& (FSReaderUtil.hasValidFileExtension(name));
			}
		});
		if (inputFiles == null) {
			LOGGER.error("Directory '{}' does not exist or an I/O error occured.", this.inputDir);
		} else if (inputFiles.length == 0) {
			// level 'warn' for this case, because this is not unusual for large monitoring logs including a number of directories
			LOGGER.warn("Directory '{}' contains no files starting with '{}' and ending with a valid file extension.", this.inputDir, this.filePrefix);
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
					LOGGER.info("Shutting down DirectoryReader.");
					break;
				}
				LOGGER.info("< Loading {}", inputFile.getAbsolutePath());
				if (inputFile.getName().endsWith(FSUtil.DAT_FILE_EXTENSION)) {
					this.processNormalInputFile(inputFile);
				} else {
					if (this.ignoreUnknownRecordTypes && ignoreUnknownRecordTypesWarningAlreadyShown) {
						ignoreUnknownRecordTypesWarningAlreadyShown = true;
						LOGGER.warn("The property '{}' is not supported for binary files. But trying to read '{}'",
								FSReader.CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES, inputFile);
					}
					try {
						final Class<? extends AbstractDecompressionFilter> clazz = FSReaderUtil.findDecompressionFilterByExtension(inputFile.getName());

						final Configuration configuration = new Configuration();
						final AbstractDecompressionFilter decompressionFilter = InstantiationFactory.getInstance(configuration)
								.createAndInitialize(AbstractDecompressionFilter.class, clazz.getCanonicalName(), configuration);
						this.processBinaryInputFile(inputFile, decompressionFilter);
					} catch (final IllegalArgumentException ex) {
						LOGGER.warn("Unknown file extension for file {}", inputFile);
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
				LOGGER.info("Directory '{}' contains no file '{}'. Found '{}' ... switching to legacy mode", this.inputDir, FSUtil.MAP_FILENAME,
						FSUtil.LEGACY_MAP_FILENAME);
				this.filePrefix = FSUtil.LEGACY_FILE_PREFIX;
			} else {
				// no {kieker|tpmon}.map exists. This is valid for very old monitoring logs. Hence, only dump a log.warn
				LOGGER.warn("No mapping file in directory '{}'", this.inputDir.getAbsolutePath());
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

				LOGGER.debug("Read line: {}", line);

				final int split = line.indexOf('=');
				if (split == -1) {
					LOGGER.error("Failed to parse line: {} from file {}. Each line must contain ID=VALUE pairs.", line, mappingFile.getAbsolutePath());
					continue; // continue on errors
				}
				final String key = line.substring(0, split);
				final String value = FSUtil.decodeNewline(line.substring(split + 1));
				// the leading $ is optional
				final Integer id;
				try {
					id = Integer.valueOf((key.charAt(0) == '$') ? key.substring(1) : key); // NOCS
				} catch (final NumberFormatException ex) {
					LOGGER.error("Error reading mapping file, id must be integer", ex);
					continue; // continue on errors
				}
				final String prevVal = this.stringRegistry.register(id, value);
				if (prevVal != null) {
					LOGGER.error("Found addional entry for id='{}', old value was '{}' new value is '{}'", id, prevVal, value);
				}
			}
		} catch (final IOException ex) {
			LOGGER.error("Error reading mapping file", ex);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException ex) {
					LOGGER.error("Exception while closing input stream for mapping file", ex);
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
		try {
			this.textFileStreamProcessor.processInputChannel(new FileInputStream(inputFile));
			this.terminated = true;
		} catch (final Exception ex) { // NOCS NOPMD (gonna catch them all)
			LOGGER.error("Error reading {}", inputFile, ex);
		}
	}

	/**
	 * Reads the records contained in the given binary file and passes them to the registered {@link #recordReceiver}.
	 *
	 * @param inputFile
	 *            The input file which should be processed.
	 * @param decompressionFilter
	 *            Whether the input file is compressed.
	 */
	private final void processBinaryInputFile(final File inputFile, final AbstractDecompressionFilter decompressionFilter) {
		DataInputStream in = null;
		try {
			in = new DataInputStream(decompressionFilter.chainInputStream(new FileInputStream(inputFile)));
			this.binaryFileStreamProcessor.createRecordsFromBinaryFile(in);
		} catch (final Exception ex) { // NOPMD NOCS (catch Exception)
			LOGGER.error("Error reading {}", inputFile, ex);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException ex) {
					LOGGER.error("Exception while closing input stream for processing input file", ex);
				}
			}
		}
	}
}
