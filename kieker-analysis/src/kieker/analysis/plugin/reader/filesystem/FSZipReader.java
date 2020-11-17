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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.plugin.reader.util.IMonitoringRecordReceiver;
import kieker.analysis.tt.reader.filesystem.util.MappingException;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.exception.UnknownRecordTypeException;
import kieker.common.registry.reader.ReaderRegistry;
import kieker.common.util.filesystem.FSUtil;

/**
 * Reads the contents of a single zip file and passes the records to the registered receiver of type {@link IMonitoringRecordReceiver}.
 *
 * @author Jan Waller
 *
 * @since 1.7
 * @deprecated 1.15 replaced by teetime log reading facilities
 */
@Deprecated
public final class FSZipReader implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(FSZipReader.class);

	private final File zipFile;
	private final IMonitoringRecordReceiver recordReceiver;
	private final boolean ignoreUnknownRecordTypes;

	private final ReaderRegistry<String> stringRegistry = new ReaderRegistry<>();

	private boolean terminated;

	private final BinaryFileStreamProcessor binaryFileStreamProcessor;
	private final TextFileStreamProcessor textFileStreamProcessor;

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
		this.binaryFileStreamProcessor = new BinaryFileStreamProcessor(this.stringRegistry, recordReceiver);
		this.textFileStreamProcessor = new TextFileStreamProcessor(ignoreUnknownRecordTypes, this.stringRegistry, recordReceiver);
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
				LOGGER.error("The zip file does not contain a Kieker log: {}", this.zipFile.toString());
				this.recordReceiver.newEndOfFileRecord();
				return;
			}
			// read mapping file
			this.readMappingFile(zipInputStream);
		} catch (final IOException ex) {
			LOGGER.error("Error accessing ZipInputStream", ex);
			this.recordReceiver.newEndOfFileRecord();
			return;
		} finally {
			if (null != zipInputStream) {
				try {
					zipInputStream.close();
				} catch (final IOException ex) {
					LOGGER.error("Failed to close ZipInputStream", ex);
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
					LOGGER.info("Shutting down DirectoryReader.");
					break;
				}
				final String filename = zipEntry.getName();
				if (filename.startsWith(FSUtil.FILE_PREFIX)) {
					if (filename.endsWith(FSUtil.DAT_FILE_EXTENSION)) {
						LOGGER.info("< Loading {}", filename);
						try {
							this.textFileStreamProcessor.processInputChannel(input);
						} catch (MappingException | MonitoringRecordException | UnknownRecordTypeException e) {
							LOGGER.error("Cannot deserialize text record {}", e);
						}
					} else if (filename.endsWith(".bin")) {
						LOGGER.info("< Loading {}", filename);
						if (this.ignoreUnknownRecordTypes) { // NOPMD (deeply nested if)
							LOGGER.warn("The property '{}' is not supported for binary files. But trying to read '{}'",
									FSReader.CONFIG_PROPERTY_NAME_IGNORE_UNKNOWN_RECORD_TYPES, filename);
						}
						try {
							this.binaryFileStreamProcessor.createRecordsFromBinaryFile(input);
						} catch (IOException | MonitoringRecordException e) {
							LOGGER.error("Cannot deserialize binary record {}", e);
						}
						this.terminated = true;
					}
				}
			}
		} catch (final IOException ex) {
			LOGGER.error("Error accessing ZipInputStream", ex);
			this.recordReceiver.newEndOfFileRecord();
			return;
		} finally {
			try {
				zipInputStream.close();
			} catch (final IOException ex) {
				LOGGER.error("Failed to close ZipInputStream", ex);
			}
			try {
				if (null != reader) {
					reader.close();
				}
			} catch (final IOException ex) {
				LOGGER.error("Failed to close ZipInputStream", ex);
			}
			try {
				if (null != input) {
					input.close();
				}
			} catch (final IOException ex) {
				LOGGER.error("Failed to close ZipInputStream", ex);
			}
		}
		this.recordReceiver.newEndOfFileRecord();
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
					LOGGER.error("Failed to parse line: {} from mapping file in zip file {}. Each line must contain ID=VALUE pairs.", line, this.zipFile.toString());
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
		} finally {
			if (in != null) {
				in.close(); // this also closes the zipInputStream
			}
		}
	}
}
