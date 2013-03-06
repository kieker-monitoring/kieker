/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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
import kieker.common.util.filesystem.BinaryCompressionMethod;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.filesystem.async.AbstractFsWriterThread;
import kieker.monitoring.writer.filesystem.async.BinaryFsWriterThread;

/**
 * @author Jan Waller
 */
public final class AsyncBinaryFsWriter extends AbstractAsyncFSWriter {

	private static final String PREFIX = AsyncBinaryFsWriter.class.getName() + ".";
	public static final String CONFIG_BUFFER = PREFIX + "bufferSize"; // NOCS (afterPREFIX)
	public static final String CONFIG_COMPRESS = PREFIX + "compress";

	private static final Log LOG = LogFactory.getLog(AsyncBinaryFsWriter.class);

	public AsyncBinaryFsWriter(final Configuration configuration) {
		super(configuration);
	}

	@Override
	protected final AbstractFsWriterThread initWorker(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final MappingFileWriter mappingFileWriter, final String path, final int maxEntiresInFile, final int maxlogSize, final int maxLogFiles) {
		BinaryCompressionMethod method;
		try {
			method = BinaryCompressionMethod.valueOf(this.configuration.getStringProperty(CONFIG_COMPRESS));
		} catch (final IllegalArgumentException ex) {
			LOG.warn("Failed to select compression method. Using NONE instead. " + ex.getMessage());
			method = BinaryCompressionMethod.NONE;
		}
		return new BinaryFsWriterThread(monitoringController, writeQueue, mappingFileWriter, path, maxEntiresInFile, maxlogSize, maxLogFiles,
				this.configuration.getIntProperty(CONFIG_BUFFER), method);
	}
}
