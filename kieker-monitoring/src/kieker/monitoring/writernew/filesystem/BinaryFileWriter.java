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
import java.io.PrintWriter;
import java.nio.ByteBuffer;
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
import kieker.monitoring.registry.GetIdAdapter;
import kieker.monitoring.registry.IRegistryListener;
import kieker.monitoring.registry.RegisterAdapter;
import kieker.monitoring.registry.WriterRegistry;
import kieker.monitoring.writernew.AbstractMonitoringWriter;

/**
 * @author Jan Waller, Christian Wulf
 *
 * @since 1.9
 */
public class BinaryFileWriter extends AbstractMonitoringWriter implements IRegistryListener<String>, IFileWriter {

	private static final Log LOG = LogFactory.getLog(BinaryFileWriter.class);

	private static final String PREFIX = BinaryFileWriter.class.getName() + ".";
	/** The name of the configuration for the custom storage path if the writer is advised not to store in the temporary directory. */
	static final String CONFIG_PATH = PREFIX + "customStoragePath";
	/** The name of the configuration determining the maximal number of entries in a file. */
	static final String CONFIG_MAXENTRIESINFILE = PREFIX + "maxEntriesInFile";
	/** The name of the configuration determining the maximal size of the files in MiB. */
	static final String CONFIG_MAXLOGSIZE = PREFIX + "maxLogSize"; // in MiB
	/** The name of the configuration determining the maximal number of log files. */
	static final String CONFIG_MAXLOGFILES = PREFIX + "maxLogFiles";
	/** The name of the configuration key for the charset name of the mapping file */
	static final String CONFIG_CHARSET_NAME = PREFIX + "charsetName";
	/** The name of the configuration key determining to enable/disable compression of the record log files */
	static final String CONFIG_SHOULD_COMPRESS = PREFIX + "shouldCompress";
	/** The name of the configuration key determining the buffer size of the output file stream */
	static final String CONFIG_BUFFERSIZE = PREFIX + "bufferSize";
	/** The name of the configuration key determining to always flush the output file stream after writing each record */
	static final String CONFIG_FLUSH = PREFIX + "flush";

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
		this.logFolder = this.buildKiekerLogFolder(configuration.getStringProperty(CONFIG_PATH));
		int maxEntriesPerFile = configuration.getIntProperty(CONFIG_MAXENTRIESINFILE);
		int maxMegaBytesPerFile = configuration.getIntProperty(CONFIG_MAXLOGSIZE);
		int maxAmountOfFiles = configuration.getIntProperty(CONFIG_MAXLOGFILES);

		maxEntriesPerFile = (maxEntriesPerFile <= 0) ? Integer.MAX_VALUE : maxEntriesPerFile;
		maxMegaBytesPerFile = (maxMegaBytesPerFile <= 0) ? Integer.MAX_VALUE : maxMegaBytesPerFile;
		maxAmountOfFiles = (maxAmountOfFiles <= 0) ? Integer.MAX_VALUE : maxAmountOfFiles;

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
		final PrintWriter mappingFileWriter = this.mappingFileWriter.getFileWriter();

		mappingFileWriter.print('$');
		mappingFileWriter.print(id);
		mappingFileWriter.print('=');
		mappingFileWriter.print(recordClassName);
		mappingFileWriter.println();
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
