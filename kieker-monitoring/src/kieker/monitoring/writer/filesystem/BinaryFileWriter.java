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

package kieker.monitoring.writer.filesystem;

import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.filesystem.FileExtensionFilter;
import kieker.monitoring.registry.GetIdAdapter;
import kieker.monitoring.registry.IRegistryListener;
import kieker.monitoring.registry.RegisterAdapter;
import kieker.monitoring.registry.WriterRegistry;
import kieker.monitoring.writer.AbstractMonitoringWriter;

/**
 * @author Jan Waller, Christian Wulf
 *
 * @since 1.9
 */
public class BinaryFileWriter extends AbstractMonitoringWriter implements IRegistryListener<String>, IFileWriter {

	public static final String PREFIX = BinaryFileWriter.class.getName() + ".";
	/** The name of the configuration for the custom storage path if the writer is advised not to store in the temporary directory. */
	public static final String CONFIG_PATH = PREFIX + "customStoragePath";
	/** The name of the configuration determining the maximal number of entries in a file. */
	public static final String CONFIG_MAXENTRIESINFILE = PREFIX + "maxEntriesInFile";
	/** The name of the configuration determining the maximal size of the files in MiB. */
	public static final String CONFIG_MAXLOGSIZE = PREFIX + "maxLogSize"; // in MiB
	/** The name of the configuration determining the maximal number of log files. */
	public static final String CONFIG_MAXLOGFILES = PREFIX + "maxLogFiles";
	/** The name of the configuration key for the charset name of the mapping file */
	public static final String CONFIG_CHARSET_NAME = PREFIX + "charsetName";
	/** The name of the configuration key determining to enable/disable compression of the record log files */
	public static final String CONFIG_SHOULD_COMPRESS = PREFIX + "shouldCompress";
	/** The name of the configuration key determining the buffer size of the output file stream */
	public static final String CONFIG_BUFFERSIZE = PREFIX + "bufferSize";
	/** The name of the configuration key determining to always flush the output file stream after writing each record */
	public static final String CONFIG_FLUSH = PREFIX + "flush";

	private static final Log LOG = LogFactory.getLog(BinaryFileWriter.class);

	private final ByteBuffer buffer;
	private final MappingFileWriter mappingFileWriter;
	private final BinaryFileWriterPool fileWriterPool;
	private final WriterRegistry writerRegistry;
	private final RegisterAdapter<String> registerStringsAdapter;
	private final GetIdAdapter<String> writeBytesAdapter;
	private final boolean flush;
	private final Path logFolder;

	public BinaryFileWriter(final Configuration configuration) {
		super(configuration);
		this.logFolder = KiekerLogFolder.buildKiekerLogFolder(configuration.getStringProperty(CONFIG_PATH), configuration);

		try {
			Files.createDirectories(this.logFolder);
		} catch (final IOException e) {
			throw new IllegalStateException("Error on creating Kieker's log directory.", e);
		}

		int maxEntriesPerFile = configuration.getIntProperty(CONFIG_MAXENTRIESINFILE);
		int maxMegaBytesPerFile = configuration.getIntProperty(CONFIG_MAXLOGSIZE);
		int maxAmountOfFiles = configuration.getIntProperty(CONFIG_MAXLOGFILES);

		maxEntriesPerFile = (maxEntriesPerFile <= 0) ? Integer.MAX_VALUE : maxEntriesPerFile;// NOCS
		maxMegaBytesPerFile = (maxMegaBytesPerFile <= 0) ? Integer.MAX_VALUE : maxMegaBytesPerFile;// NOCS
		maxAmountOfFiles = (maxAmountOfFiles <= 0) ? Integer.MAX_VALUE : maxAmountOfFiles;// NOCS

		final String charsetName = configuration.getStringProperty(CONFIG_CHARSET_NAME, "UTF-8");
		// TODO should we check for buffers too small for a single record?
		final int bufferSize = this.configuration.getIntProperty(CONFIG_BUFFERSIZE);
		final boolean shouldCompress = configuration.getBooleanProperty(CONFIG_SHOULD_COMPRESS);
		this.flush = configuration.getBooleanProperty(CONFIG_FLUSH);

		this.buffer = ByteBuffer.allocateDirect(bufferSize);
		this.mappingFileWriter = new MappingFileWriter(this.logFolder, charsetName);
		this.fileWriterPool = new BinaryFileWriterPool(LOG, this.logFolder, maxEntriesPerFile, shouldCompress, maxAmountOfFiles, maxMegaBytesPerFile);

		this.writerRegistry = new WriterRegistry(this);
		this.registerStringsAdapter = new RegisterAdapter<String>(this.writerRegistry);
		this.writeBytesAdapter = new GetIdAdapter<String>(this.writerRegistry);
	}

	@Override
	public void onStarting() {
		// do nothing
	}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		final PooledFileChannel channel = this.fileWriterPool.getFileWriter(this.buffer);

		monitoringRecord.registerStrings(this.registerStringsAdapter);

		final ByteBuffer recordBuffer = this.buffer;
		if ((4 + 8 + monitoringRecord.getSize()) > recordBuffer.remaining()) {
			channel.flush(recordBuffer, LOG);
		}

		final String recordClassName = monitoringRecord.getClass().getName();
		this.writerRegistry.register(recordClassName);

		recordBuffer.putInt(this.writerRegistry.getId(recordClassName));
		recordBuffer.putLong(monitoringRecord.getLoggingTimestamp());
		monitoringRecord.writeBytes(recordBuffer, this.writeBytesAdapter);

		if (this.flush) {
			channel.flush(recordBuffer, LOG);
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
	}

	@Override
	public void onTerminating() {
		this.fileWriterPool.close(this.buffer);
		this.mappingFileWriter.close();
	}

	@Override
	public Path getLogFolder() {
		return this.logFolder;
	}

	@Override
	public FilenameFilter getFileNameFilter() {
		return FileExtensionFilter.BIN;
	}
}
