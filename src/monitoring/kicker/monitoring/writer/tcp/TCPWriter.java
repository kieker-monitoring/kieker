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

package kicker.monitoring.writer.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;

import kicker.common.configuration.Configuration;
import kicker.common.logging.Log;
import kicker.common.logging.LogFactory;
import kicker.common.record.IMonitoringRecord;
import kicker.common.record.misc.RegistryRecord;
import kicker.common.util.registry.IRegistry;
import kicker.monitoring.core.controller.IMonitoringController;
import kicker.monitoring.writer.AbstractAsyncThread;
import kicker.monitoring.writer.AbstractAsyncWriter;

/**
 * 
 * @author Jan Waller
 * 
 * @since 1.8
 */
public final class TCPWriter extends AbstractAsyncWriter {
	private static final String PREFIX = TCPWriter.class.getName() + ".";
	public static final String CONFIG_HOSTNAME = PREFIX + "hostname"; // NOCS (afterPREFIX)
	public static final String CONFIG_PORT1 = PREFIX + "port1"; // NOCS (afterPREFIX)
	public static final String CONFIG_PORT2 = PREFIX + "port2"; // NOCS (afterPREFIX)
	public static final String CONFIG_BUFFERSIZE = PREFIX + "bufferSize"; // NOCS (afterPREFIX)
	public static final String CONFIG_FLUSH = PREFIX + "flush"; // NOCS (afterPREFIX)

	private final String hostname;
	private final int port1;
	private final int port2;
	private final int bufferSize;
	private final boolean flush;

	public TCPWriter(final Configuration configuration) {
		super(configuration);
		this.hostname = configuration.getStringProperty(CONFIG_HOSTNAME);
		this.port1 = configuration.getIntProperty(CONFIG_PORT1);
		this.port2 = configuration.getIntProperty(CONFIG_PORT2);
		// should be check for buffers too small for a single record?
		this.bufferSize = configuration.getIntProperty(CONFIG_BUFFERSIZE);
		this.flush = configuration.getBooleanProperty(CONFIG_FLUSH);
	}

	@Override
	protected void init() throws Exception {
		this.addWorker(new TCPWriterThread(this.monitoringController, this.blockingQueue, this.hostname, this.port1, this.port2, this.bufferSize, this.flush));
	}
}

/**
 * 
 * @author Jan Waller
 * 
 * @since 1.8
 */
final class TCPWriterThread extends AbstractAsyncThread {
	private static final Log LOG = LogFactory.getLog(TCPWriterThread.class);

	private final SocketChannel socketChannelRecords;
	private final SocketChannel socketChannelStrings;
	private final ByteBuffer byteBuffer;
	private final IRegistry<String> stringRegistry;
	private final boolean flush;

	public TCPWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue, final String hostname,
			final int port1, final int port2, final int bufferSize, final boolean flush) throws IOException {
		super(monitoringController, writeQueue);
		this.byteBuffer = ByteBuffer.allocateDirect(bufferSize);
		this.socketChannelRecords = SocketChannel.open(new InetSocketAddress(hostname, port1));
		this.socketChannelStrings = SocketChannel.open(new InetSocketAddress(hostname, port2));
		this.stringRegistry = this.monitoringController.getStringRegistry();
		this.flush = flush;
	}

	@Override
	protected void consume(final IMonitoringRecord monitoringRecord) throws Exception {
		if (monitoringRecord instanceof RegistryRecord) {
			final ByteBuffer buffer = ByteBuffer.allocateDirect(monitoringRecord.getSize());
			monitoringRecord.writeBytes(buffer, this.stringRegistry);
			buffer.flip();
			while (buffer.hasRemaining()) {
				this.socketChannelStrings.write(buffer);
			}
		} else {
			final ByteBuffer buffer = this.byteBuffer;
			if ((monitoringRecord.getSize() + 4 + 8) > buffer.remaining()) {
				buffer.flip();
				while (buffer.hasRemaining()) {
					this.socketChannelRecords.write(buffer);
				}
				buffer.clear();
			}
			buffer.putInt(this.monitoringController.getUniqueIdForString(monitoringRecord.getClass().getName()));
			buffer.putLong(monitoringRecord.getLoggingTimestamp());
			monitoringRecord.writeBytes(buffer, this.stringRegistry);
			if (this.flush) {
				buffer.flip();
				while (buffer.hasRemaining()) {
					this.socketChannelRecords.write(buffer);
				}
				buffer.clear();
			}
		}
	}

	@Override
	protected void cleanup() {
		try {
			final ByteBuffer buffer = this.byteBuffer;
			buffer.flip();
			while (buffer.hasRemaining()) {
				this.socketChannelRecords.write(buffer);
			}
			this.socketChannelRecords.close();
		} catch (final IOException ex) {
			LOG.error("Error closing connection", ex);
		}
		try {
			this.socketChannelStrings.close();
		} catch (final IOException ex) {
			LOG.error("Error closing connection", ex);
		}
	}
}
