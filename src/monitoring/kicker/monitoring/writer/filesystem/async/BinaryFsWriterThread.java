/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.monitoring.writer.filesystem.async;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;

import kicker.common.logging.Log;
import kicker.common.logging.LogFactory;
import kicker.common.record.IMonitoringRecord;
import kicker.common.util.filesystem.BinaryCompressionMethod;
import kicker.common.util.registry.IRegistry;
import kicker.monitoring.core.controller.IMonitoringController;
import kicker.monitoring.writer.filesystem.map.MappingFileWriter;

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
