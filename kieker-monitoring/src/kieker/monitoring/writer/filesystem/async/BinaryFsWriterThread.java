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

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.filesystem.BinaryCompressionMethod;
import kieker.common.util.registry.IRegistry;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.filesystem.map.MappingFileWriter;

/**
 * @author Jan Waller
 *
 * @since 1.5
 */
public class BinaryFsWriterThread extends AbstractFsWriterThread {
	private static final Log LOG = LogFactory.getLog(BinaryFsWriterThread.class);

	private DataOutputStream out;

	private final int bufferSize;
	private final BinaryCompressionMethod compressionMethod;
	private final IRegistry<String> stringRegistry;

	/**
	 * Create a new BinaryFsWriterThread.
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
	 * @param maxLogSize
	 *            limit of the log file size
	 * @param maxLogFiles
	 *            limit of the number of log files
	 * @param bufferSize
	 *            size of the output buffer
	 * @param compressionMethod
	 *            compressionMethod to be used for output
	 */
	public BinaryFsWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final MappingFileWriter mappingFileWriter, final String path, final int maxEntriesInFile, final int maxLogSize, final int maxLogFiles,
			final int bufferSize, final BinaryCompressionMethod compressionMethod) {
		super(monitoringController, writeQueue, mappingFileWriter, path, maxEntriesInFile, maxLogSize, maxLogFiles);
		this.compressionMethod = compressionMethod;
		this.fileExtension = compressionMethod.getFileExtension();
		this.bufferSize = bufferSize;
		this.stringRegistry = monitoringController.getStringRegistry();
	}

	@Override
	protected void write(final IMonitoringRecord monitoringRecord) throws IOException {
		final int size = monitoringRecord.getSize() + 4 + 8;

		// FIXME performance issue due to too many object instantiations: ByteBuffer
		final ByteBuffer buffer = ByteBuffer.allocateDirect(size);
		buffer.putInt(this.monitoringController.getUniqueIdForString(monitoringRecord.getClass().getName()));
		buffer.putLong(monitoringRecord.getLoggingTimestamp());
		monitoringRecord.writeBytes(buffer, this.stringRegistry);

		// FIXME performance issue due to too many object instantiations: byte[]
		final byte[] bytes = new byte[size];
		buffer.flip();
		buffer.get(bytes, 0, size);
		this.out.write(bytes);
	}

	@Override
	protected void prepareFile(final String filename) throws IOException {
		if (null != this.out) {
			this.out.close();
		}
		this.out = this.compressionMethod.getDataOutputStream(new File(filename), this.bufferSize);
	}

	@Override
	protected void cleanup() {
		if (this.out != null) {
			try {
				this.out.close();
			} catch (final IOException ex) {
				LOG.error("Failed to close channel.", ex);
			}
		}
	}
}
