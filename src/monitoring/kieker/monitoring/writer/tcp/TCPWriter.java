/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.writer.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.AbstractAsyncThread;
import kieker.monitoring.writer.AbstractAsyncWriter;

/**
 * 
 * @author Jan Waller
 * 
 * @since 1.8
 */
public class TCPWriter extends AbstractAsyncWriter {
	private static final String PREFIX = TCPWriter.class.getName() + ".";
	public static final String CONFIG_HOSTNAME = PREFIX + "hostname"; // NOCS (afterPREFIX)
	public static final String CONFIG_PORT = PREFIX + "port"; // NOCS (afterPREFIX)

	private final String hostname;
	private final int port;

	protected TCPWriter(final Configuration configuration) {
		super(configuration);
		this.hostname = configuration.getStringProperty(CONFIG_HOSTNAME);
		this.port = configuration.getIntProperty(CONFIG_PORT);
	}

	@Override
	protected void init() throws Exception {
		this.addWorker(new TCPWriterThread(this.monitoringController, this.blockingQueue, this.hostname, this.port));

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

	private static final int MESSAGE_BUFFER_SIZE = 65536;
	private final SocketChannel socketChannel;
	private final ByteBuffer buffer;

	public TCPWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue, final String hostname, final int port)
			throws IOException {
		super(monitoringController, writeQueue);
		this.buffer = ByteBuffer.allocateDirect(MESSAGE_BUFFER_SIZE);
		this.socketChannel = SocketChannel.open(new InetSocketAddress(hostname, port));
	}

	@Override
	protected void consume(final IMonitoringRecord monitoringRecord) throws Exception {
		this.buffer.putInt(this.monitoringController.getIdForString(monitoringRecord.getClass().getName()));
		this.buffer.putLong(monitoringRecord.getLoggingTimestamp());
		this.buffer.put(monitoringRecord.toByteArray());
		// FIXME: actually write something
	}

	@Override
	protected void cleanup() {
		try {
			this.socketChannel.close();
		} catch (final IOException ex) {
			LOG.error("Error closing connection", ex);
		}
	}
}
