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

import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.filesystem.FSUtil;
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
public class AsciiFileWriter extends AbstractMonitoringWriter implements IRegistryListener<String> {

	private static final String PREFIX = AsciiFileWriter.class.getName() + ".";
	/** The name of the configuration for the custom storage path if the writer is advised not to store in the temporary directory. */
	static final String CONFIG_PATH = PREFIX + "customStoragePath";
	/** The name of the configuration for the charset name (e.g. "UTF-8") */
	static final String CONFIG_CHARSETNAME = PREFIX + "charsetName";
	/** The name of the configuration determining the maximal number of entries in a file. */
	static final String CONFIG_MAXENTRIESINFILE = PREFIX + "maxEntriesInFile";
	/** The name of the configuration key determining to enable/disable compression of the record log files */
	static final String CONFIG_SHOULD_COMPRESS = PREFIX + "shouldCompress";
	// /** The name of the configuration determining the maximal size of the files in MiB. */
	// static final String CONFIG_MAXLOGSIZE = PREFIX + "maxLogSize"; // in MiB
	// /** The name of the configuration determining the maximal number of log files. */
	// static final String CONFIG_MAXLOGFILES = PREFIX + "maxLogFiles";

	private final Path logFolder;
	private final FileWriterPool fileWriterPool;
	private final MappingFileWriter mappingFileWriter;
	private final IWriterRegistry<String> writerRegistry;

	public AsciiFileWriter(final Configuration configuration) {
		super(configuration);
		this.logFolder = this.buildKiekerLogFolder(configuration.getStringProperty(CONFIG_PATH));
		final String charsetName = configuration.getStringProperty(CONFIG_CHARSETNAME, "UTF-8");
		final int maxEntriesInFile = configuration.getIntProperty(CONFIG_MAXENTRIESINFILE);
		final boolean shouldCompress = configuration.getBooleanProperty(CONFIG_SHOULD_COMPRESS);
		// final int maxLogFiles = configuration.getIntProperty(CONFIG_MAXLOGFILES);
		this.fileWriterPool = new FileWriterPool(this.logFolder, charsetName, maxEntriesInFile, shouldCompress);
		this.mappingFileWriter = new MappingFileWriter(this.logFolder, charsetName);

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
		for (final Object recordField : record.toArray()) {
			fileWriter.print(';');
			fileWriter.print(String.valueOf(recordField));
		}
		fileWriter.println();
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
		this.fileWriterPool.close();
		this.mappingFileWriter.close();
	}

	public Path getLogFolder() {
		return this.logFolder;
	}

}
