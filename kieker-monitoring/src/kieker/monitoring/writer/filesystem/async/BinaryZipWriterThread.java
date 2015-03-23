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

package kieker.monitoring.writer.filesystem.async;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;

import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.filesystem.map.StringMappingFileWriter;

/**
 * @author Jan Waller
 * 
 * @since 1.7
 */
public class BinaryZipWriterThread extends AbstractZipWriterThread {
	private final DataOutputStream out;
	private final IRegistry<String> stringRegistry;

	/**
	 * Create a new BinaryZipWriterThread.
	 * 
	 * @param monitoringController
	 *            the monitoring controller accessed by this thread
	 * @param writeQueue
	 *            the queue where the writer fetches its records from
	 * @param mappingFileWriter
	 *            writer for the mapping file (the file where class names are mapped to record ids)
	 * @param path
	 *            location where to files should go to (the path must point to a directory)
	 * @param maxEntriesInFile
	 *            limit for the number of records per log file
	 * @param bufferSize
	 *            size of the output buffer
	 * @param level
	 *            compression level
	 * 
	 * @throws IOException
	 *             when file operation fails
	 */
	public BinaryZipWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final StringMappingFileWriter mappingFileWriter, final String path, final int maxEntriesInFile, final int bufferSize, final int level)
			throws IOException {
		super(monitoringController, writeQueue, mappingFileWriter, path, maxEntriesInFile, level);
		super.fileExtension = ".bin";
		this.out = new DataOutputStream(new BufferedOutputStream(super.zipOutputStream, bufferSize));
		this.stringRegistry = monitoringController.getStringRegistry();
	}

	@Override
	protected void write(final IMonitoringRecord monitoringRecord) throws IOException {
		final int size = monitoringRecord.getSize() + 4 + 8;
		final ByteBuffer buffer = ByteBuffer.allocateDirect(size);
		buffer.putInt(this.monitoringController.getUniqueIdForString(monitoringRecord.getClass().getName()));
		buffer.putLong(monitoringRecord.getLoggingTimestamp());
		monitoringRecord.writeBytes(buffer, this.stringRegistry);
		final byte[] bytes = new byte[size];
		buffer.flip();
		buffer.get(bytes, 0, size);
		this.out.write(bytes);
	}

	@Override
	protected void cleanupForNextEntry() throws IOException {
		this.out.flush();
	}

	@Override
	protected void cleanupFinal() throws IOException {
		this.out.close();
	}
}
