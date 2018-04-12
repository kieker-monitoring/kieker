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
package kieker.monitoring.writer.filesystem;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.registry.IRegistryListener;
import kieker.common.registry.writer.WriterRegistry;
import kieker.common.util.filesystem.FSUtil;
import kieker.monitoring.core.controller.ControllerFactory;
import kieker.monitoring.writer.AbstractMonitoringWriter;
import kieker.monitoring.writer.compression.ICompressionFilter;
import kieker.monitoring.writer.compression.NoneCompressionFilter;

/**
 * Generic file writer which can be used to write any type of serialization.
 * Presently, two serialization methods exist.
 *
 * @author Reiner Jung
 *
 * @since 1.14
 *
 */
public class FileWriter extends AbstractMonitoringWriter implements IRegistryListener<String>, IFileWriter {

	public static final String PREFIX = FileWriter.class.getName() + ".";
	/** The name of the configuration for the custom storage path if the writer is advised not to store in the temporary directory. */
	public static final String CONFIG_PATH = PREFIX + "customStoragePath";
	/** The name of the configuration for the charset name used to store strings (e.g. "UTF-8"). */
	public static final String CONFIG_CHARSET_NAME = PREFIX + "charsetName";
	/** The name of the configuration determining the maximal number of entries in a file. */
	public static final String CONFIG_MAXENTRIESINFILE = PREFIX + "maxEntriesInFile";
	/** The name of the configuration determining the maximal size of the files in MiB. */
	public static final String CONFIG_MAXLOGSIZE = PREFIX + "maxLogSize"; // in MiB
	/** The name of the configuration determining the maximal number of log files. */
	public static final String CONFIG_MAXLOGFILES = PREFIX + "maxLogFiles";
	/** The name of the configuration to switch for the map file handler; default TextMapFileHandler. */
	public static final String CONFIG_MAP_FILE_HANDLER = PREFIX + "mapFileHandler";
	/** The name of the configuration to define the handler of the file pool. */
	public static final String CONFIG_LOG_POOL_FILE_HANDLER = PREFIX + "logFilePoolHandler";
	/** The name of the configuration to define the handler of the log stream. */
	public static final String CONFIG_LOG_STREAM_HANDLER = PREFIX + "logStreamHandler";
	/** The name of the configuration key determining to always flush the output file stream after writing each record. */
	public static final String CONFIG_FLUSH = PREFIX + "flush";
	/** The name of the configuration key to select a compression for the record log files. */
	public static final String CONFIG_COMPRESSION_FILTER = PREFIX + "compression";
	/** The name of the configuration key for the buffer size. */
	public static final String CONFIG_BUFFERSIZE = PREFIX + "bufferSize";

	private static final Logger LOGGER = LoggerFactory.getLogger(FileWriter.class);

	private final int maxEntriesInFile;

	private final IMapFileHandler mapFileHandler;
	private final ILogFilePoolHandler logFilePoolHandler;
	private final AbstractLogStreamHandler logStreamHandler;
	private final long maxBytesInFile;
	private final WriterRegistry writerRegistry;

	private final Path folder;

	/**
	 * Create a generic file writer.
	 *
	 * @param configuration
	 *            Kieker configuration object.
	 * @throws IOException
	 */
	public FileWriter(final Configuration configuration) throws IOException {
		super(configuration);

		String configPathName = configuration.getStringProperty(CONFIG_PATH);
		if (configPathName.isEmpty()) { // if the property does not exist or if the path is empty
			configPathName = System.getProperty("java.io.tmpdir");
		}

		if (!(new File(configPathName)).isDirectory()) {
			final String currentWorkingDir = System.getProperty("user.dir");
			throw new IllegalArgumentException("'" + configPathName
					+ "' is not a directory. The current working directory was: " + currentWorkingDir);
		}

		final Path logFolder = KiekerLogFolder.buildKiekerLogFolder(configPathName, configuration);
		try {
			Files.createDirectories(logFolder);
			this.folder = logFolder;
		} catch (final IOException e) {
			throw new IllegalStateException("Error on creating Kieker's log directory.", e);
		}

		this.writerRegistry = new WriterRegistry(this);

		final boolean flushLogFile = configuration.getBooleanProperty(CONFIG_FLUSH, false);
		final int bufferSize = configuration.getIntProperty(CONFIG_BUFFERSIZE, 65536);

		final Charset charset = Charset.forName(configuration.getStringProperty(FileWriter.CONFIG_CHARSET_NAME, "UTF-8"));

		this.maxEntriesInFile = (configuration.getIntProperty(CONFIG_MAXENTRIESINFILE) <= 0) ? Integer.MAX_VALUE // NOCS
				: configuration.getIntProperty(CONFIG_MAXENTRIESINFILE); // NOCS

		final long maxMegaBytesInFile = configuration.getIntProperty(CONFIG_MAXLOGSIZE);
		this.maxBytesInFile = (maxMegaBytesInFile <= 0) ? Integer.MAX_VALUE : maxMegaBytesInFile * 1024 * 1024; // NOCS

		int maxAmountOfFiles = configuration.getIntProperty(FileWriter.CONFIG_MAXLOGFILES);
		maxAmountOfFiles = (maxAmountOfFiles <= 0) ? Integer.MAX_VALUE : maxAmountOfFiles; // NOCS

		final String charsetName = configuration.getStringProperty(CONFIG_CHARSET_NAME, "UTF-8");

		/** get compression filter main data. */
		final String compressionFilterClassName = configuration.getStringProperty(CONFIG_COMPRESSION_FILTER, NoneCompressionFilter.class.getName());
		final ICompressionFilter compressionFilter = ControllerFactory.getInstance(configuration).createAndInitialize(ICompressionFilter.class,
				compressionFilterClassName, configuration);

		/** get map file handler. */
		final String mapFileHandlerClassName = configuration.getStringProperty(CONFIG_MAP_FILE_HANDLER, TextMapFileHandler.class.getName());
		this.mapFileHandler = ControllerFactory.getInstance(configuration).createAndInitialize(IMapFileHandler.class,
				mapFileHandlerClassName, configuration);
		this.mapFileHandler.create(logFolder.resolve(FSUtil.MAP_FILENAME), Charset.forName(charsetName));

		/** get log stream handler. */
		final String logHandlerClassName = configuration.getStringProperty(CONFIG_LOG_STREAM_HANDLER, TextLogStreamHandler.class.getName());
		final Class<?>[] logHandlerSignature = { Boolean.class, Integer.class, Charset.class, ICompressionFilter.class, WriterRegistry.class };
		this.logStreamHandler = ControllerFactory.getInstance(configuration).create(AbstractLogStreamHandler.class, logHandlerClassName, logHandlerSignature,
				flushLogFile,
				bufferSize, charset, compressionFilter, this.writerRegistry);

		/** get log file handler. */
		final String logFilePoolHandlerClassName = configuration.getStringProperty(CONFIG_LOG_POOL_FILE_HANDLER, RotatingLogFilePoolHandler.class.getName());
		final Class<?>[] logFilePoolHandlerSignature = { Path.class, String.class, Integer.class, };
		this.logFilePoolHandler = ControllerFactory.getInstance(configuration).create(ILogFilePoolHandler.class,
				logFilePoolHandlerClassName, logFilePoolHandlerSignature, logFolder, this.logStreamHandler.getFileExtension(), maxAmountOfFiles);

		final Path outputFile = this.logFilePoolHandler.requestFile();

		this.logStreamHandler.initialize(Files.newOutputStream(outputFile, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE), outputFile.getFileName());
	}

	@Override
	public void onNewRegistryEntry(final String value, final int id) {
		this.mapFileHandler.add(id, value);
	}

	@Override
	public void onStarting() {
		// empty
	}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord record) {
		if (this.logStreamHandler.getNumOfEntries() >= this.maxEntriesInFile) {
			this.createNewLogFile();
		} else if (this.logStreamHandler.getNumOfBytes() >= this.maxBytesInFile) {
			this.createNewLogFile();
		}

		final String recordClassName = record.getClass().getName();
		this.writerRegistry.register(recordClassName);

		try {
			this.logStreamHandler.serialize(record, this.writerRegistry.getId(recordClassName));
		} catch (final IOException e) {
			LOGGER.error("Serializing of a record failed.", e);
		}
	}

	private void createNewLogFile() {
		try {
			// request new file.
			this.logStreamHandler.close();
			final Path outputFile = this.logFilePoolHandler.requestFile();

			this.logStreamHandler.initialize(Files.newOutputStream(outputFile, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE),
					outputFile.getFileName());
		} catch (final IOException ex) {
			LOGGER.error("Switching files in logger failed.", ex);
		}
	}

	@Override
	public void onTerminating() {
		try {
			this.logStreamHandler.close();
			this.mapFileHandler.close();
		} catch (final IOException ex) {
			LOGGER.error("Closing logger failed.", ex);
		}
	}

	@Override
	public Path getLogFolder() {
		return this.folder;
	}

}
