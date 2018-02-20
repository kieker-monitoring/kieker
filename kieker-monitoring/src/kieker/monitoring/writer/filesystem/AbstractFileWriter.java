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
import java.io.PrintWriter;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueSerializer;
import kieker.monitoring.core.controller.ControllerFactory;
import kieker.monitoring.registry.IRegistryListener;
import kieker.monitoring.registry.IWriterRegistry;
import kieker.monitoring.registry.WriterRegistry;
import kieker.monitoring.writer.AbstractMonitoringWriter;
import kieker.monitoring.writer.filesystem.compression.ICompressionFilter;
import kieker.monitoring.writer.filesystem.compression.NoneCompressionFilter;
import kieker.monitoring.writer.filesystem.compression.ZipCompressionFilter;

/**
 * Abstract file writer.
 *
 * @param <T>
 *            buffer type
 * @param <S>
 *            serializer type
 *
 * @author Reiner Jung
 *
 * @since 1.14
 *
 * @deprecated 1.14 should be removed in 1.15 replaced by new FileWriter API
 */
@Deprecated
public abstract class AbstractFileWriter<T extends Buffer, S extends IValueSerializer> extends AbstractMonitoringWriter
		implements IRegistryListener<String>, IFileWriter {

	public static final String PREFIX = AbstractFileWriter.class.getName() + ".";
	/** The name of the configuration for the custom storage path if the writer is advised not to store in the temporary directory. */
	public static final String CONFIG_PATH = PREFIX + "customStoragePath";
	/** The name of the configuration key for the charset name of the mapping file */
	public static final String CONFIG_CHARSET_NAME = PREFIX + "charsetName";
	/** The name of the configuration determining the maximal number of entries in a file. */
	public static final String CONFIG_MAXENTRIESINFILE = PREFIX + "maxEntriesInFile";
	/** The name of the configuration key to select a compression for the record log files */
	public static final String CONFIG_COMPRESSION_FILTER = PREFIX + "compression";
	@Deprecated
	public static final String CONFIG_SHOULD_COMPRESS = PREFIX + "shouldCompress";
	/** The name of the configuration determining the maximal size of the files in MiB. */
	public static final String CONFIG_MAXLOGSIZE = PREFIX + "maxLogSize"; // in MiB
	/** The name of the configuration determining the maximal number of log files. */
	public static final String CONFIG_MAXLOGFILES = PREFIX + "maxLogFiles";
	/** The name of the configuration key determining the buffer size of the output file stream */
	public static final String CONFIG_BUFFERSIZE = PREFIX + "bufferSize";
	/** The name of the configuration key determining to always flush the output file stream after writing each record */
	public static final String CONFIG_FLUSH = PREFIX + "flush";
	/** The name of the configuration determining whether to flush upon each incoming registry entry. */
	public static final String CONFIG_FLUSH_MAPFILE = PREFIX + "flushMapfile";

	private final Path logFolder;
	private final MappingFileWriter mappingFileWriter;
	private final boolean flushLogFile;
	private final boolean flushMapFile;

	private final int bufferSize;
	private final AbstractFileWriterPool<T> fileWriterPool;
	private final IValueSerializer serializer;
	private final IWriterRegistry<String> writerRegistry;
	private T buffer;

	public AbstractFileWriter(final Configuration configuration) {
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

		this.logFolder = KiekerLogFolder.buildKiekerLogFolder(configPathName, configuration);

		try {
			Files.createDirectories(this.logFolder);
		} catch (final IOException e) {
			throw new IllegalStateException("Error on creating Kieker's log directory.", e);
		}

		int maxEntriesInFile = configuration.getIntProperty(CONFIG_MAXENTRIESINFILE);
		int maxMegaBytesInFile = configuration.getIntProperty(CONFIG_MAXLOGSIZE);
		int maxAmountOfFiles = configuration.getIntProperty(CONFIG_MAXLOGFILES);

		maxEntriesInFile = (maxEntriesInFile <= 0) ? Integer.MAX_VALUE : maxEntriesInFile; // NOCS
		maxMegaBytesInFile = (maxMegaBytesInFile <= 0) ? Integer.MAX_VALUE : maxMegaBytesInFile; // NOCS
		maxAmountOfFiles = (maxAmountOfFiles <= 0) ? Integer.MAX_VALUE : maxAmountOfFiles; // NOCS

		final String charsetName = configuration.getStringProperty(CONFIG_CHARSET_NAME, "UTF-8");
		this.bufferSize = this.configuration.getIntProperty(CONFIG_BUFFERSIZE, 65536);

		String compressionFilterClassName = configuration.getStringProperty(CONFIG_COMPRESSION_FILTER, NoneCompressionFilter.class.getName());
		if (compressionFilterClassName.equals(NoneCompressionFilter.class.getName())) {
			final boolean compress = configuration.getBooleanProperty(CONFIG_SHOULD_COMPRESS, false);
			if (compress) {
				compressionFilterClassName = ZipCompressionFilter.class.getName();
			}
		}
		final ICompressionFilter compressionFilter = ControllerFactory.getInstance(configuration).createAndInitialize(ICompressionFilter.class,
				compressionFilterClassName, configuration);

		this.flushLogFile = configuration.getBooleanProperty(CONFIG_FLUSH, false);
		this.flushMapFile = configuration.getBooleanProperty(CONFIG_FLUSH_MAPFILE, true);

		this.writerRegistry = new WriterRegistry(this);

		this.mappingFileWriter = new MappingFileWriter(this.logFolder, charsetName);

		this.fileWriterPool = this.createFilePoolHandler(charsetName, maxEntriesInFile, compressionFilter, maxAmountOfFiles, maxMegaBytesInFile);
		this.serializer = this.createSerializer();
	}

	protected abstract IValueSerializer createSerializer();

	protected abstract AbstractFileWriterPool<T> createFilePoolHandler(String charsetName, int maxEntriesInFile, ICompressionFilter compressionFilter,
			int maxAmountOfFiles, int maxMegaBytesInFile);

	@Override
	public void onStarting() { // NOPMD we do not need on starting for file serializer.
		// do nothing
	}

	protected int registerMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		final String recordClassName = monitoringRecord.getClass().getName();
		this.getWriterRegistry().register(recordClassName);

		return this.getWriterRegistry().getId(recordClassName);
	}

	@Override
	public void onNewRegistryEntry(final String recordClassName, final int id) {
		final PrintWriter mappingPrintWriter = this.getMappingFileWriter().getFileWriter();

		mappingPrintWriter.print('$');
		mappingPrintWriter.print(id);
		mappingPrintWriter.print('=');
		mappingPrintWriter.print(recordClassName);
		mappingPrintWriter.println();

		if (this.isFlushMapFile()) {
			mappingPrintWriter.flush();
		}
	}

	@Override
	public void onTerminating() {
		this.getFileWriterPool().close();
		this.getMappingFileWriter().close();
	}

	@Override
	public String toString() {
		final String configInfo = super.toString();
		final StringBuilder builder = new StringBuilder()
				.append(configInfo)
				.append("\n\t")
				.append("Internal properties:")
				.append("\n\t\t")
				.append("Log location: ")
				.append(this.getLogFolder());
		return builder.toString();
	}

	@Override
	public Path getLogFolder() {
		return this.logFolder;
	}

	protected int getBufferSize() {
		return this.bufferSize;
	}

	protected MappingFileWriter getMappingFileWriter() {
		return this.mappingFileWriter;
	}

	protected boolean isFlushLogFile() {
		return this.flushLogFile;
	}

	protected boolean isFlushMapFile() {
		return this.flushMapFile;
	}

	protected AbstractFileWriterPool<T> getFileWriterPool() {
		return this.fileWriterPool;
	}

	protected IValueSerializer getSerializer() {
		return this.serializer;
	}

	protected IWriterRegistry<String> getWriterRegistry() {
		return this.writerRegistry;
	}

	protected T getBuffer() {
		return this.buffer;
	}

	protected void setBuffer(final T buffer) {
		this.buffer = buffer;
	}

}
