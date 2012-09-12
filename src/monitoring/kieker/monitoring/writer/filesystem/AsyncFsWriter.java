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
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.filesystem.async.AbstractFsWriterThread;
import kieker.monitoring.writer.filesystem.async.FsWriterThread;

/**
 * @author Matthias Rohr, Robert von Massow, Andre van Hoorn, Jan Waller
 */
public final class AsyncFsWriter extends AbstractAsyncFSWriter {
	public static final String CONFIG_FLUSH = AsyncFsWriter.class.getName() + ".flush";

	public AsyncFsWriter(final Configuration configuration) {
		super(configuration);
	}

	@Override
	protected final AbstractFsWriterThread initWorker(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final MappingFileWriter mappingFileWriter, final String path, final int maxEntiresInFile, final int maxlogSize, final int maxLogFiles) {
		return new FsWriterThread(monitoringController, writeQueue, mappingFileWriter, path, maxEntiresInFile, maxlogSize, maxLogFiles,
				this.configuration.getBooleanProperty(CONFIG_FLUSH));
	}
}
