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

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.filesystem.async.AbstractZipWriterThread;
import kieker.monitoring.writer.filesystem.async.BinaryZipWriterThread;
import kieker.monitoring.writer.filesystem.map.StringMappingFileWriter;

/**
 * @author Jan Waller
 */
public final class AsyncBinaryZipWriter extends AbstractAsyncZipWriter {
	private static final String PREFIX = AsyncBinaryZipWriter.class.getName() + ".";
	public static final String CONFIG_BUFFER = PREFIX + "bufferSize"; // NOCS (afterPREFIX)

	private final StringMappingFileWriter mappingFileWriter;

	public AsyncBinaryZipWriter(final Configuration configuration) throws IOException {
		super(configuration);
		this.mappingFileWriter = new StringMappingFileWriter();
	}

	/**
	 * Make sure that the required properties always have default values!
	 */
	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration(super.getDefaultConfiguration());
		configuration.setProperty(CONFIG_BUFFER, "8192");
		return configuration;
	}

	@Override
	protected AbstractZipWriterThread initWorker(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final String path, final int maxEntiresInFile) throws Exception {
		return new BinaryZipWriterThread(monitoringController, writeQueue, this.mappingFileWriter, path, maxEntiresInFile,
				this.configuration.getIntProperty(CONFIG_BUFFER));
	}
}
