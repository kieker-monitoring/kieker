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

package kieker.monitoring.writernew.filesystem;

import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.filesystem.FSUtil;
import kieker.common.util.filesystem.FileExtensionFilter;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.registry.IRegistryListener;
import kieker.monitoring.registry.IWriterRegistry;
import kieker.monitoring.registry.WriterRegistry;
import kieker.monitoring.writernew.AbstractMonitoringWriter;

/**
 * @author Matthias Rohr, Robert von Massow, Andre van Hoorn, Jan Waller, Christian Wulf
 *
 * @since < 0.9
 */
public class AsciiFileWriter extends AbstractMonitoringWriter implements IRegistryListener<String>, IFileWriter {

	private static final Log LOG = LogFactory.getLog(AsciiFileWriter.class);

	private static final String PREFIX = AsciiFileWriter.class.getName() + ".";
	/** The name of the configuration for the custom storage path if the writer is advised not to store in the temporary directory. */
	public static final String CONFIG_PATH = PREFIX + "customStoragePath";
	/** The name of the configuration for the charset name (e.g. "UTF-8") */
	/* default */ static final String CONFIG_CHARSET_NAME = PREFIX + "charsetName";
	/** The name of the configuration determining the maximal number of entries in a file. */
	public static final String CONFIG_MAXENTRIESINFILE = PREFIX + "maxEntriesInFile";
	/** The name of the configuration key determining to enable/disable compression of the record log files */
	/* default */ static final String CONFIG_SHOULD_COMPRESS = PREFIX + "shouldCompress";
	/** The name of the configuration determining the maximal size of the files in MiB. */
	public static final String CONFIG_MAXLOGSIZE = PREFIX + "maxLogSize"; // in MiB
	/** The name of the configuration determining the maximal number of log files. */
	public static final String CONFIG_MAXLOGFILES = PREFIX + "maxLogFiles";
	/** The name of the configuration determining whether to flush upon each incoming record. */
	public static final String CONFIG_FLUSH = PREFIX + "flush";

	private final Path logFolder;
	private final AsciiFileWriterPool fileWriterPool;
	private final MappingFileWriter mappingFileWriter;
	private final IWriterRegistry<String> writerRegistry;
	private final boolean flush;

	public AsciiFileWriter(final Configuration configuration) {
		super(configuration);
		this.logFolder = this.buildKiekerLogFolder(configuration.getStringProperty(CONFIG_PATH));

		try {
			Files.createDirectories(this.logFolder);
		} catch (final IOException e) {
			throw new IllegalStateException("Error on creating Kieker's log directory.", e);
		}

		int maxEntriesPerFile = configuration.getIntProperty(CONFIG_MAXENTRIESINFILE);
		int maxMegaBytesPerFile = configuration.getIntProperty(CONFIG_MAXLOGSIZE);
		int maxAmountOfFiles = configuration.getIntProperty(CONFIG_MAXLOGFILES);

		maxEntriesPerFile = (maxEntriesPerFile <= 0) ? Integer.MAX_VALUE : maxEntriesPerFile;
		maxMegaBytesPerFile = (maxMegaBytesPerFile <= 0) ? Integer.MAX_VALUE : maxMegaBytesPerFile;
		maxAmountOfFiles = (maxAmountOfFiles <= 0) ? Integer.MAX_VALUE : maxAmountOfFiles;

		final String charsetName = configuration.getStringProperty(CONFIG_CHARSET_NAME, "UTF-8");
		final boolean shouldCompress = configuration.getBooleanProperty(CONFIG_SHOULD_COMPRESS);

		this.flush = configuration.getBooleanProperty(CONFIG_FLUSH);

		this.mappingFileWriter = new MappingFileWriter(this.logFolder, charsetName);
		this.fileWriterPool = new AsciiFileWriterPool(LOG, this.logFolder, charsetName, maxEntriesPerFile, shouldCompress, maxAmountOfFiles, maxMegaBytesPerFile);

		this.writerRegistry = new WriterRegistry(this);
	}

	private Path buildKiekerLogFolder(final String customStoragePath) {
		final DateFormat date = new SimpleDateFormat("yyyyMMdd'-'HHmmssSSS", Locale.US);
		date.setTimeZone(TimeZone.getTimeZone("UTC"));
		final String currentDateStr = date.format(new java.util.Date()); // NOPMD (Date)

		final String hostName = this.configuration.getStringProperty(ConfigurationFactory.HOST_NAME);
		final String controllerName = this.configuration.getStringProperty(ConfigurationFactory.CONTROLLER_NAME);

		final String filename = String.format("%s-%s-UTC-%s-%s", FSUtil.FILE_PREFIX, currentDateStr, hostName, controllerName);

		return Paths.get(customStoragePath, filename);
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

		fileWriter.print('$');
		fileWriter.print(this.writerRegistry.getId(recordClassName));
		fileWriter.print(';');
		fileWriter.print(record.getLoggingTimestamp());
		// IMPROVE performance: provide and use a method Record.writeBytes(CharBuffer, ..) instead
		for (final Object recordField : record.toArray()) {
			fileWriter.print(';');
			fileWriter.print(String.valueOf(recordField));
		}
		fileWriter.println();

		if (this.flush) {
			fileWriter.flush();
		}
	}

	@Override
	public void onNewRegistryEntry(final String recordClassName, final int id) {
		final PrintWriter mappingFileWriter = this.mappingFileWriter.getFileWriter();

		mappingFileWriter.print('$');
		mappingFileWriter.print(id);
		mappingFileWriter.print('=');
		mappingFileWriter.print(recordClassName);
		mappingFileWriter.println();

		if (this.flush) {
			mappingFileWriter.flush();
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
