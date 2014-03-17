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

package kieker.monitoring.writer.filesystem.async;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
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
 * @since 1.9
 */
public class BinaryNFsWriterThread extends AbstractFsWriterThread {
	private static final Log LOG = LogFactory.getLog(BinaryNFsWriterThread.class);

	private FileOutputStream out;
	private FileChannel channel;

	private final ByteBuffer byteBuffer;
	private final IRegistry<String> stringRegistry;

	public BinaryNFsWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final MappingFileWriter mappingFileWriter, final String path, final int maxEntriesInFile, final int maxLogSize, final int maxLogFiles,
			final int bufferSize) {
		super(monitoringController, writeQueue, mappingFileWriter, path, maxEntriesInFile, maxLogSize, maxLogFiles);
		this.byteBuffer = ByteBuffer.allocateDirect(bufferSize);
		this.stringRegistry = monitoringController.getStringRegistry();
		this.fileExtension = BinaryCompressionMethod.NONE.getFileExtension();
	}

	@Override
	protected void write(final IMonitoringRecord monitoringRecord) throws IOException {
		final ByteBuffer buffer = this.byteBuffer;
		if ((monitoringRecord.getSize() + 4 + 8) > buffer.remaining()) {
			buffer.flip();
			while (buffer.hasRemaining()) {
				this.channel.write(buffer);
			}
			buffer.clear();
		}
		buffer.putInt(this.monitoringController.getUniqueIdForString(monitoringRecord.getClass().getName()));
		buffer.putLong(monitoringRecord.getLoggingTimestamp());
		monitoringRecord.writeBytes(buffer, this.stringRegistry);
	}

	@Override
	protected void prepareFile(final String filename) throws IOException {
		if (null != this.out) {
			final ByteBuffer buffer = this.byteBuffer;
			buffer.flip();
			while (buffer.hasRemaining()) {
				this.channel.write(buffer);
			}
			buffer.clear();
			this.channel.force(false);
			this.out.close();
		}
		this.out = new FileOutputStream(filename);
		this.channel = this.out.getChannel();
	}

	@Override
	protected void cleanup() {
		if (this.out != null) {
			try {
				final ByteBuffer buffer = this.byteBuffer;
				buffer.flip();
				while (buffer.hasRemaining()) {
					this.channel.write(buffer);
				}
				buffer.clear();
				this.channel.force(false);
				this.out.close();
			} catch (final IOException ex) {
				LOG.error("Failed to close channel.", ex);
			}
		}
	}
}
