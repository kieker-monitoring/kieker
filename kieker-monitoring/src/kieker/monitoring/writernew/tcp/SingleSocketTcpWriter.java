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

package kieker.monitoring.writernew.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.RegistryRecord;
import kieker.monitoring.registry.GetIdAdapter;
import kieker.monitoring.registry.IRegistryListener;
import kieker.monitoring.registry.IWriterRegistry;
import kieker.monitoring.registry.RegisterAdapter;
import kieker.monitoring.registry.WriterRegistry;
import kieker.monitoring.writernew.AbstractMonitoringWriter;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class SingleSocketTcpWriter extends AbstractMonitoringWriter implements IRegistryListener<String> {

	private static final Log LOG = LogFactory.getLog(SingleSocketTcpWriter.class);

	private static final String PREFIX = SingleSocketTcpWriter.class.getName() + ".";

	public static final String CONFIG_HOSTNAME = PREFIX + "hostname"; // NOCS (afterPREFIX)
	public static final String CONFIG_PORT1 = PREFIX + "port1"; // NOCS (afterPREFIX)
	public static final String CONFIG_BUFFERSIZE = PREFIX + "bufferSize"; // NOCS (afterPREFIX)
	public static final String CONFIG_FLUSH = PREFIX + "flush"; // NOCS (afterPREFIX)

	private final boolean flush;

	private final SocketChannel socketChannel;
	private final ByteBuffer byteBuffer;

	private final IWriterRegistry<String> writerRegistry;
	private final RegisterAdapter<String> registerAdapter;
	private final GetIdAdapter<String> readAdapter;

	public SingleSocketTcpWriter(final Configuration configuration) throws IOException {
		super(configuration);
		final String hostname = configuration.getStringProperty(CONFIG_HOSTNAME);
		final int port1 = configuration.getIntProperty(CONFIG_PORT1);
		// TODO should be check for buffers too small for a single record?
		final int bufferSize = configuration.getIntProperty(CONFIG_BUFFERSIZE);
		this.flush = configuration.getBooleanProperty(CONFIG_FLUSH);

		this.byteBuffer = ByteBuffer.allocateDirect(bufferSize);
		// buffer size is available by byteBuffer.capacity()
		this.socketChannel = SocketChannel.open(new InetSocketAddress(hostname, port1));
		this.writerRegistry = new WriterRegistry(this);
		this.registerAdapter = new RegisterAdapter<String>(this.writerRegistry);
		this.readAdapter = new GetIdAdapter<String>(this.writerRegistry);
	}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		final ByteBuffer buffer = this.byteBuffer;

		monitoringRecord.registerStrings(this.registerAdapter);

		if ((4 + 8 + monitoringRecord.getSize()) > buffer.remaining()) {
			this.flushBuffer(buffer);
		}

		final String recordClassName = monitoringRecord.getClass().getName();
		this.writerRegistry.register(recordClassName);

		final int recordClassId = this.writerRegistry.getId(recordClassName);
		final long loggingTimestamp = monitoringRecord.getLoggingTimestamp();

		buffer.putInt(recordClassId);
		buffer.putLong(loggingTimestamp);
		monitoringRecord.writeBytes(buffer, this.readAdapter);
		// monitoringRecord.writeToBuffer(buffer, this.writerRegistry);

		if (this.flush) {
			this.flushBuffer(buffer);
		}
	}

	@Override
	public void onNewRegistryEntry(final String value, final int id) {
		final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
		// logging timestamp + class id + RegistryRecord.SIZE + bytes.length
		final int requiredBufferSize = (2 * AbstractMonitoringRecord.TYPE_SIZE_INT) + RegistryRecord.SIZE + bytes.length;

		final ByteBuffer buffer = this.byteBuffer;
		if (buffer.remaining() < requiredBufferSize) {
			this.flushBuffer(buffer);
		}

		buffer.putInt(RegistryRecord.CLASS_ID);
		buffer.putInt(id);
		buffer.putInt(value.length());
		buffer.put(bytes);
	}

	@Override
	public void onTerminating() {
		this.flushBuffer(this.byteBuffer);

		try {
			this.socketChannel.close();
		} catch (final IOException e) {
			LOG.warn("Error closing the connection", e);
		}
	}

	private void flushBuffer(final ByteBuffer buffer) {
		buffer.flip();
		try {
			while (buffer.hasRemaining()) {
				this.socketChannel.write(buffer);
			}
			buffer.clear();
		} catch (final IOException e) {
			LOG.error("Error in writing the record", e);
			try {
				this.socketChannel.close();
			} catch (final IOException e1) {
				LOG.warn("Error closing the connection", e1);
			}
		}
	}
}
