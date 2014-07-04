/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

import java.util.concurrent.BlockingQueue;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.filesystem.async.AbstractFsWriterThread;
import kieker.monitoring.writer.filesystem.async.FsWriterThread;
import kieker.monitoring.writer.filesystem.map.MappingFileWriter;

/**
 * @author Matthias Rohr, Robert von Massow, Andre van Hoorn, Jan Waller
 * 
 * @since < 0.9
 */
public final class AsyncFsWriter extends AbstractAsyncFSWriter {
	private static final String PREFIX = AsyncFsWriter.class.getName() + ".";
	public static final String CONFIG_FLUSH = PREFIX + "flush"; // NOCS (afterPREFIX)
	public static final String CONFIG_BUFFER = PREFIX + "bufferSize"; // NOCS (afterPREFIX)

	private static final Log LOG = LogFactory.getLog(AsyncFsWriter.class);

	private final int buffersize;
	private final boolean flush;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this writer.
	 */
	public AsyncFsWriter(final Configuration configuration) {
		super(configuration);
		int tmpBuffersize = configuration.getIntProperty(CONFIG_BUFFER);
		if (tmpBuffersize <= 0) {
			LOG.warn("Buffer size has to be greater than zero. Using 8192 instead.");
			tmpBuffersize = 8192;
		}
		this.buffersize = tmpBuffersize;
		this.flush = configuration.getBooleanProperty(CONFIG_FLUSH);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration(super.getDefaultConfiguration());
		configuration.setProperty(CONFIG_BUFFER, "8192");
		configuration.setProperty(CONFIG_FLUSH, "true");
		return configuration;
	}

	@Override
	protected final AbstractFsWriterThread initWorker(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final MappingFileWriter mappingFileWriter, final String path, final int maxEntiresInFile, final int maxlogSize, final int maxLogFiles) {

		return new FsWriterThread(monitoringController, writeQueue, mappingFileWriter, path, maxEntiresInFile, maxlogSize, maxLogFiles,
				this.flush, this.buffersize);
	}
}
