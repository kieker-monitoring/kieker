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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.plugin.reader.util.ByteBufferDeserializer;
import kieker.analysis.plugin.reader.util.IMonitoringRecordReceiver;
import kieker.common.registry.reader.ReaderRegistry;
import kieker.common.util.filesystem.FSUtil;
import kieker.common.util.filesystem.FileExtensionFilter;

/**
 * Reads the contents of a single file system log directory and passes the records to the registered receiver of type
 * {@link IMonitoringRecordReceiver}.
 *
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller
 *
 * @since 1.2
 *
 * @deprecated 1.15
 */
@Deprecated
class BinaryLogReaderThread extends AbstractLogReaderThread {

	private static final Logger LOGGER = LoggerFactory.getLogger(BinaryLogReaderThread.class);

	private final ReaderRegistry<String> readerRegistry = new ReaderRegistry<>();
	private final IMonitoringRecordReceiver recordReceiver;
	private final File inputDir;
	private final boolean shouldDecompress;
	private final ByteBufferDeserializer deserializer;

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
	public BinaryLogReaderThread(final File inputDir, final IMonitoringRecordReceiver recordReceiver,
			final boolean shouldDecompress) {
		super(LOGGER, inputDir);
		if ((inputDir == null) || !inputDir.isDirectory()) {
			throw new IllegalArgumentException("Invalid or empty inputDir");
		}
		this.inputDir = inputDir;
		this.recordReceiver = recordReceiver;
		this.shouldDecompress = shouldDecompress;
		this.deserializer = new ByteBufferDeserializer(this.readerRegistry, LOGGER, 8192);
		this.deserializer.register(recordReceiver);
	}

	@Override
	protected FileExtensionFilter getFileExtensionFilter() {
		return (this.shouldDecompress) ? FileExtensionFilter.ZIP : FileExtensionFilter.BIN; // NOCS // NOPMD
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
				LOGGER.info("Directory '{}' contains no file '{}'. Found '{}' ... switching to legacy mode", this.inputDir, FSUtil.MAP_FILENAME,
						FSUtil.LEGACY_MAP_FILENAME);
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
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Read line: " + line);
				}
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
				final String prevVal = this.readerRegistry.register(id, value);
				if (prevVal != null) {
					LOGGER.error("Found addional entry for id='{', old value was '{}' new value is '{}'", id, prevVal, value);
				}
			}
		} catch (final IOException e) {
			LOGGER.error("Error reading {}", mappingFile, e);
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
	@Override
	protected void processNormalInputFile(final File inputFile) {
		ReadableByteChannel channel = null;
		try {
			InputStream fileInputStream = new FileInputStream(inputFile);
			if (this.shouldDecompress) {
				@SuppressWarnings("resource")
				final ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
				zipInputStream.getNextEntry();
				fileInputStream = zipInputStream;
			}

			channel = Channels.newChannel(fileInputStream);

			this.deserializer.deserialize(channel);

		} catch (final FileNotFoundException e) {
			LOGGER.error("The binary input file could not be found.", e);
		} catch (final IOException e) {
			LOGGER.error("An exception occurred", e);
		} finally {
			if (channel != null) {
				try {
					channel.close();
				} catch (final IOException e) {
					LOGGER.warn("An exception occurred", e);
				}
			}
		}
	}

	@Override
	protected void onEndOfRun() {
		this.recordReceiver.newEndOfFileRecord();
	}

}
