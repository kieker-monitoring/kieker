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

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.zip.Deflater;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.AbstractAsyncWriter;
import kieker.monitoring.writer.filesystem.async.AbstractZipWriterThread;
import kieker.monitoring.writer.filesystem.map.StringMappingFileWriter;

/**
 * @author Jan Waller
 * 
 * @since 1.7
 */
public abstract class AbstractAsyncZipWriter extends AbstractAsyncWriter {
	/** The name of the configuration for the custom storage path if the writer is advised not to store in the temporary directory. */
	public static final String CONFIG_PATH = "customStoragePath";
	/** The name of the configuration determining the maximal number of entries in a file. */
	public static final String CONFIG_MAXENTRIESINFILE = "maxEntriesInFile";
	/** The name of the configuration determining the size of the buffer. */
	public static final String CONFIG_BUFFER = "bufferSize";
	/** The name of the configuration determining the compression level. */
	public static final String CONFIG_COMPRESS_LEVEL = "compressionLevel";
	/** The name of the configuration determining whether to store the data in the temporary directory or not. */
	private static final String CONFIG_TEMP = "storeInJavaIoTmpdir";

	private static final Log LOG = LogFactory.getLog(AbstractAsyncZipWriter.class);

	private final StringMappingFileWriter mappingFileWriter;
	private final String configPath;
	private final int configMaxEntriesInFile;
	private final int configBuffersize;
	private final int configLevel;

	/**
	 * @param configuration
	 *            The configuration for this writer.
	 */
	public AbstractAsyncZipWriter(final Configuration configuration) {
		super(configuration);
		// Mapping file can be create here (no (real) side effects)
		this.mappingFileWriter = new StringMappingFileWriter();

		final String prefix = this.getClass().getName() + '.';
		// Determine path
		if (configuration.getBooleanProperty(prefix + CONFIG_TEMP)) {
			LogFactory.getLog(this.getClass()).warn(
					"Using deprecated configuration property " + prefix + CONFIG_TEMP + ". Instead use empty value for " + prefix + CONFIG_PATH);
		}
		String tmpPath = configuration.getStringProperty(prefix + CONFIG_PATH);
		if (tmpPath.length() == 0) {
			tmpPath = System.getProperty("java.io.tmpdir");
		}
		if (!(new File(tmpPath)).isDirectory()) {
			throw new IllegalArgumentException("'" + tmpPath + "' is not a directory.");
		}
		this.configPath = tmpPath;
		// get number of entries per file
		this.configMaxEntriesInFile = configuration.getIntProperty(prefix + CONFIG_MAXENTRIESINFILE);
		if (this.configMaxEntriesInFile < 1) {
			throw new IllegalArgumentException(prefix + CONFIG_MAXENTRIESINFILE + " must be greater than 0 but is '" + this.configMaxEntriesInFile + "'");
		}
		int tmpBuffersize = configuration.getIntProperty(prefix + CONFIG_BUFFER);
		if (tmpBuffersize <= 0) {
			LOG.warn("Buffer size has to be greater than zero. Using 8192 instead.");
			tmpBuffersize = 8192;
		}
		this.configBuffersize = tmpBuffersize;
		// check compression level and method
		int tmpLevel = configuration.getIntProperty(prefix + CONFIG_COMPRESS_LEVEL);
		if ((tmpLevel != Deflater.DEFAULT_COMPRESSION) && (tmpLevel != Deflater.NO_COMPRESSION)
				&& !((tmpLevel >= Deflater.BEST_SPEED) && (tmpLevel <= Deflater.BEST_COMPRESSION))) {
			LOG.warn("Illegal compression level. Using default compression level instead.");
			tmpLevel = Deflater.DEFAULT_COMPRESSION;
		}
		this.configLevel = tmpLevel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration(super.getDefaultConfiguration());
		final String prefix = this.getClass().getName() + "."; // can't use this.prefix, maybe uninitialized
		configuration.setProperty(prefix + CONFIG_PATH, "");
		configuration.setProperty(prefix + CONFIG_MAXENTRIESINFILE, "25000");
		configuration.setProperty(prefix + CONFIG_BUFFER, "8192");
		configuration.setProperty(prefix + CONFIG_COMPRESS_LEVEL, Integer.toString(Deflater.DEFAULT_COMPRESSION));
		return configuration;
	}

	@Override
	protected final void init() throws Exception {
		// Create writer thread (should be only one to get a single consistent mapping file)
		this.addWorker(this.initWorker(super.monitoringController, this.blockingQueue, this.mappingFileWriter, this.configPath, this.configMaxEntriesInFile,
				this.configBuffersize, this.configLevel));
		this.addWorker(this.initWorker(super.monitoringController, this.prioritizedBlockingQueue, this.mappingFileWriter, this.configPath,
				this.configMaxEntriesInFile, this.configBuffersize, this.configLevel));
	}

	protected abstract AbstractZipWriterThread initWorker(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final StringMappingFileWriter strMappingFileWriter, final String path, final int maxEntiresInFile, final int bufferSize, final int level)
			throws Exception;
}
