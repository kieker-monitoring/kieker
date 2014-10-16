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

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.filesystem.async.AbstractZipWriterThread;
import kieker.monitoring.writer.filesystem.async.AsciiZipWriterThread;
import kieker.monitoring.writer.filesystem.map.StringMappingFileWriter;

/**
 * @author Jan Waller
 * 
 * @since 1.7
 */
public final class AsyncAsciiZipWriter extends AbstractAsyncZipWriter {

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this writer.
	 * 
	 * @throws IOException
	 *             If something went wrong during the initialization of the writer.
	 */
	public AsyncAsciiZipWriter(final Configuration configuration) throws IOException {
		super(configuration);
	}

	@Override
	protected AbstractZipWriterThread initWorker(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final StringMappingFileWriter mappingFileWriter, final String path, final int maxEntiresInFile, final int bufferSize, final int level)
			throws Exception {
		return new AsciiZipWriterThread(monitoringController, writeQueue, mappingFileWriter, path, maxEntiresInFile,
				bufferSize, level);
	}
}
