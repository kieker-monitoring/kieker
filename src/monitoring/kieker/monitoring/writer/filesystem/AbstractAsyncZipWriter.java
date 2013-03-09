/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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
	public static final String CONFIG_PATH = "customStoragePath";
	public static final String CONFIG_TEMP = "storeInJavaIoTmpdir";
	public static final String CONFIG_MAXENTRIESINFILE = "maxEntriesInFile";
	public static final String CONFIG_BUFFER = "bufferSize";
	public static final String CONFIG_COMPRESS_LEVEL = "compressionLevel";

	private static final Log LOG = LogFactory.getLog(AbstractAsyncZipWriter.class);

	private final StringMappingFileWriter mappingFileWriter;

	public AbstractAsyncZipWriter(final Configuration configuration) {
		super(configuration);
		// Mapping file can be create here (no (real) side effects)
		this.mappingFileWriter = new StringMappingFileWriter();
	}

	/**
	 * Make sure that the required properties always have default values!
	 */
	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration(super.getDefaultConfiguration());
		final String prefix = this.getClass().getName() + "."; // can't use this.prefix, maybe uninitialized
		configuration.setProperty(prefix + CONFIG_PATH, ".");
		configuration.setProperty(prefix + CONFIG_TEMP, "true");
		configuration.setProperty(prefix + CONFIG_MAXENTRIESINFILE, "25000");
		configuration.setProperty(prefix + CONFIG_BUFFER, "8192");
		configuration.setProperty(prefix + CONFIG_COMPRESS_LEVEL, Integer.toString(Deflater.DEFAULT_COMPRESSION));
		return configuration;
	}

	@Override
	protected final void init() throws Exception {
		final String prefix = this.getClass().getName() + '.';
		// Determine path
		String path;
		if (this.configuration.getBooleanProperty(prefix + CONFIG_TEMP)) {
			path = System.getProperty("java.io.tmpdir");
		} else {
			path = this.configuration.getStringProperty(prefix + CONFIG_PATH);
		}
		final File f = new File(path);
		if (!f.isDirectory()) {
			throw new IllegalArgumentException("'" + path + "' is not a directory.");
		}
		// get number of entries per file
		final int maxEntriesInFile = this.configuration.getIntProperty(prefix + CONFIG_MAXENTRIESINFILE);
		if (maxEntriesInFile < 1) {
			throw new IllegalArgumentException(prefix + CONFIG_MAXENTRIESINFILE + " must be greater than 0 but is '" + maxEntriesInFile + "'");
		}
		int buffersize = this.configuration.getIntProperty(prefix + CONFIG_BUFFER);
		if (buffersize <= 0) {
			LOG.warn("Buffer size has to be greater than zero. Using 8192 instead.");
			buffersize = 8192;
		}
		// check compression level and method
		int level = this.configuration.getIntProperty(prefix + CONFIG_COMPRESS_LEVEL);
		if ((level != Deflater.DEFAULT_COMPRESSION) && (level != Deflater.NO_COMPRESSION)
				&& !((level >= Deflater.BEST_SPEED) && (level <= Deflater.BEST_COMPRESSION))) {
			LOG.warn("Illegal compression level. Using default compression level instead.");
			level = Deflater.DEFAULT_COMPRESSION;
		}
		// Create writer thread (should be only one to get a single consistent mapping file)
		this.addWorker(this.initWorker(super.monitoringController, this.blockingQueue, this.mappingFileWriter, path, maxEntriesInFile, buffersize, level));
	}

	protected abstract AbstractZipWriterThread initWorker(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final StringMappingFileWriter strMappingFileWriter, final String path, final int maxEntiresInFile, final int bufferSize, final int level)
			throws Exception;
}
