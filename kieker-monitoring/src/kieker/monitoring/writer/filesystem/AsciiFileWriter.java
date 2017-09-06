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

package kieker.monitoring.writer.filesystem;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.TextValueSerializer;
import kieker.common.util.filesystem.FileExtensionFilter;
import kieker.monitoring.core.controller.ReceiveUnfilteredConfiguration;
import kieker.monitoring.registry.IRegistryListener;
import kieker.monitoring.registry.IWriterRegistry;
import kieker.monitoring.registry.WriterRegistry;
import kieker.monitoring.writer.AbstractMonitoringWriter;

/**
 * @author Matthias Rohr, Robert von Massow, Andre van Hoorn, Jan Waller, Christian Wulf
 *
 * @since < 0.9
 */
@ReceiveUnfilteredConfiguration // required for using class KiekerLogFolder
public class AsciiFileWriter extends AbstractMonitoringWriter implements IRegistryListener<String>, IFileWriter {

	public static final String PREFIX = AsciiFileWriter.class.getName() + ".";
	/**
	 * The name of the configuration for the custom storage path if the writer is advised not to store in the temporary
	 * directory.
	 */
	public static final String CONFIG_PATH = PREFIX + "customStoragePath";
	/** The name of the configuration for the charset name (e.g. "UTF-8") */
	public static final String CONFIG_CHARSET_NAME = PREFIX + "charsetName";
	/** The name of the configuration determining the maximal number of entries in a file. */
	public static final String CONFIG_MAXENTRIESINFILE = PREFIX + "maxEntriesInFile";
	/** The name of the configuration key determining to enable/disable compression of the record log files */
	public static final String CONFIG_SHOULD_COMPRESS = PREFIX + "shouldCompress";
	/** The name of the configuration determining the maximal size of the files in MiB. */
	public static final String CONFIG_MAXLOGSIZE = PREFIX + "maxLogSize"; // in MiB
	/** The name of the configuration determining the maximal number of log files. */
	public static final String CONFIG_MAXLOGFILES = PREFIX + "maxLogFiles";
	/** The name of the configuration determining whether to flush upon each incoming record. */
	public static final String CONFIG_FLUSH = PREFIX + "flush";
	/** The name of the configuration determining whether to flush upon each incoming registry entry. */
	public static final String CONFIG_FLUSH_MAPFILE = PREFIX + "flushMapfile";

	private static final Log LOG = LogFactory.getLog(AsciiFileWriter.class);

	private final Path logFolder;
	private final AsciiFileWriterPool fileWriterPool;
	private final MappingFileWriter mappingFileWriter;
	private final IWriterRegistry<String> writerRegistry;
	private final boolean flush;
	private final boolean flushMapfile;

	private final CharBuffer buffer = CharBuffer.allocate(65535);
	private final TextValueSerializer serializer;

	public AsciiFileWriter(final Configuration configuration) {
		super(configuration);

		this.serializer = TextValueSerializer.create(this.buffer);

		String configPathName = configuration.getStringProperty(CONFIG_PATH);
		if (configPathName.isEmpty()) { // if the property does not exist or if the path is empty
			configPathName = System.getProperty("java.io.tmpdir");
		}

		if (!(new File(configPathName)).isDirectory()) {
			final String currentWorkingDir = System.getProperty("user.dir");
			throw new IllegalArgumentException("'" + configPathName
					+ "' is not a directory. The current working directory was: " + currentWorkingDir);
		}

		this.logFolder = KiekerLogFolder.buildKiekerLogFolder(configPathName, configuration);

		try {
			Files.createDirectories(this.logFolder);
		} catch (final IOException e) {
			throw new IllegalStateException("Error on creating Kieker's log directory.", e);
		}

		int maxEntriesPerFile = configuration.getIntProperty(CONFIG_MAXENTRIESINFILE);
		int maxMegaBytesPerFile = configuration.getIntProperty(CONFIG_MAXLOGSIZE);
		int maxAmountOfFiles = configuration.getIntProperty(CONFIG_MAXLOGFILES);

		maxEntriesPerFile = (maxEntriesPerFile <= 0) ? Integer.MAX_VALUE : maxEntriesPerFile; // NOCS
		maxMegaBytesPerFile = (maxMegaBytesPerFile <= 0) ? Integer.MAX_VALUE : maxMegaBytesPerFile; // NOCS
		maxAmountOfFiles = (maxAmountOfFiles <= 0) ? Integer.MAX_VALUE : maxAmountOfFiles; // NOCS

		final String charsetName = configuration.getStringProperty(CONFIG_CHARSET_NAME, "UTF-8");
		final boolean shouldCompress = configuration.getBooleanProperty(CONFIG_SHOULD_COMPRESS);

		this.flush = configuration.getBooleanProperty(CONFIG_FLUSH, false);
		this.flushMapfile = configuration.getBooleanProperty(CONFIG_FLUSH_MAPFILE, true);

		this.mappingFileWriter = new MappingFileWriter(this.logFolder, charsetName);
		this.fileWriterPool = new AsciiFileWriterPool(LOG, this.logFolder, charsetName, maxEntriesPerFile,
				shouldCompress, maxAmountOfFiles, maxMegaBytesPerFile);

		this.writerRegistry = new WriterRegistry(this);
	}

	@Override
	public void onStarting() {
		// do nothing
	}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord record) {
		final String recordClassName = record.getClass().getName();
		this.writerRegistry.register(recordClassName);

		final PrintWriter fileWriter = this.fileWriterPool.getFileWriter();

		this.buffer.rewind();

		fileWriter.print('$');
		fileWriter.print(this.writerRegistry.getId(recordClassName));
		fileWriter.print(';');
		fileWriter.print(record.getLoggingTimestamp());

		record.serialize(this.serializer);

		this.buffer.flip();
		fileWriter.print(this.buffer.toString());
		fileWriter.println();

		if (this.flush) {
			fileWriter.flush();
		}
	}

	@Override
	public void onNewRegistryEntry(final String recordClassName, final int id) {
		final PrintWriter mappingPrintWriter = this.mappingFileWriter.getFileWriter();

		mappingPrintWriter.print('$');
		mappingPrintWriter.print(id);
		mappingPrintWriter.print('=');
		mappingPrintWriter.print(recordClassName);
		mappingPrintWriter.println();

		if (this.flushMapfile) {
			mappingPrintWriter.flush();
		}
	}

	@Override
	public void onTerminating() {
		this.fileWriterPool.close();
		this.mappingFileWriter.close();
	}

	@Override
	public Path getLogFolder() {
		return this.logFolder;
	}

	@Override
	public FilenameFilter getFileNameFilter() {
		return FileExtensionFilter.DAT;
	}

}
