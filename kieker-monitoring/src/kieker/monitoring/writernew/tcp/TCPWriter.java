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
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.RegistryRecord;
import kieker.monitoring.registry.GetIdAdapter;
import kieker.monitoring.registry.IRegistryListener;
import kieker.monitoring.registry.IWriterRegistry;
import kieker.monitoring.registry.RegisterAdapter;
import kieker.monitoring.registry.WriterRegistry;
import kieker.monitoring.writernew.AbstractMonitoringWriter;

/**
 * @author "Christian Wulf"
 *
 * @deprecated 1.13. Use {@link DualSocketTcpWriter} instead.
 */
@Deprecated
public class TCPWriter extends AbstractMonitoringWriter implements IRegistryListener<String> {

	private static final int DEFAULT_STRING_REGISTRY_BUFFER_SIZE = 1024;

	private static final Log LOG = LogFactory.getLog(TCPWriter.class);

	private static final String PREFIX = TCPWriter.class.getName() + ".";

	public static final String CONFIG_HOSTNAME = PREFIX + "hostname"; // NOCS (afterPREFIX)
	public static final String CONFIG_PORT1 = PREFIX + "port1"; // NOCS (afterPREFIX)
	public static final String CONFIG_PORT2 = PREFIX + "port2"; // NOCS (afterPREFIX)
	public static final String CONFIG_BUFFERSIZE = PREFIX + "bufferSize"; // NOCS (afterPREFIX)
	public static final String CONFIG_FLUSH = PREFIX + "flush"; // NOCS (afterPREFIX)

	private static final String CONFIG_STRING_REGISTRY_BUFFERSIZE = PREFIX + "StringRegistryBufferSize"; // NOCS
	// (afterPREFIX)

	private final boolean flush;

	private final SocketChannel monitoringRecordChannel;
	private final SocketChannel registryRecordChannel;
	private final ByteBuffer byteBuffer;

	private final IWriterRegistry<String> writerRegistry;
	private final RegisterAdapter<String> registerAdapter;
	private final GetIdAdapter<String> readAdapter;

	private final ByteBuffer stringRegistryBuffer;

	// private final CharsetEncoder encoder;

	public TCPWriter(final Configuration configuration) throws IOException {
		super(configuration);
		final String hostname = configuration.getStringProperty(CONFIG_HOSTNAME);
		final int port1 = configuration.getIntProperty(CONFIG_PORT1);
		final int port2 = configuration.getIntProperty(CONFIG_PORT2);
		// TODO should be check for buffers too small for a single record?
		final int bufferSize = configuration.getIntProperty(CONFIG_BUFFERSIZE);
		int stringRegistryBufferSize = configuration.getIntProperty(CONFIG_STRING_REGISTRY_BUFFERSIZE);
		if (stringRegistryBufferSize <= 0) {
			LOG.warn("Invalid buffer size passed for string registry records: " + stringRegistryBufferSize
					+ ". Defaults to " + DEFAULT_STRING_REGISTRY_BUFFER_SIZE);
			stringRegistryBufferSize = DEFAULT_STRING_REGISTRY_BUFFER_SIZE;
		}

		this.flush = configuration.getBooleanProperty(CONFIG_FLUSH);

		this.byteBuffer = ByteBuffer.allocateDirect(bufferSize);
		this.stringRegistryBuffer = ByteBuffer.allocateDirect(stringRegistryBufferSize);
		// buffer size is available by byteBuffer.capacity()
		this.monitoringRecordChannel = SocketChannel.open(new InetSocketAddress(hostname, port1));
		this.registryRecordChannel = SocketChannel.open(new InetSocketAddress(hostname, port2));

		this.writerRegistry = new WriterRegistry(this);
		this.registerAdapter = new RegisterAdapter<String>(this.writerRegistry);
		this.readAdapter = new GetIdAdapter<String>(this.writerRegistry);

		// this.encoder = StandardCharsets.UTF_8.newEncoder();
	}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		final ByteBuffer buffer = this.byteBuffer;
		final int requiredBufferSize = 4 + 8 + monitoringRecord.getSize();
		if (requiredBufferSize > buffer.remaining()) {
			this.flushBuffer(buffer, this.monitoringRecordChannel);
		}

		monitoringRecord.registerStrings(this.registerAdapter);

		final String recordClassName = monitoringRecord.getClass().getName();
		this.writerRegistry.register(recordClassName);

		final int recordClassId = this.writerRegistry.getId(recordClassName);
		final long loggingTimestamp = monitoringRecord.getLoggingTimestamp();

		buffer.putInt(recordClassId);
		buffer.putLong(loggingTimestamp);

		monitoringRecord.writeBytes(buffer, this.readAdapter);
		// monitoringRecord.writeToBuffer(buffer, this.writerRegistry);

		if (this.flush) {
			this.flushBuffer(buffer, this.monitoringRecordChannel);
		}
	}

	@Override
	public void onNewRegistryEntry(final String value, final int id) {
		final ByteBuffer buffer = this.stringRegistryBuffer;

		final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
		// final ByteBuffer valueInByteBuffer = StandardCharsets.UTF_8.encode(value);

		final int requiredBufferSize = RegistryRecord.SIZE + bytes.length;
		if (buffer.capacity() < requiredBufferSize) {
			// stringRegistryBuffer = ByteBuffer.allocateDirect(RegistryRecord.SIZE + bytes.length);
			throw new IllegalStateException("Insufficient capacity for string registry buffer");
		}

		// loggingTimestamp not transmitted by dual socket communication
		// class id not used by dual socket communication
		buffer.putInt(id);
		buffer.putInt(value.length());
		buffer.put(bytes);

		// always flush so that on the reader side the records can be reconstructed
		this.flushBuffer(buffer, this.registryRecordChannel);
	}

	@Override
	public void onTerminating() {
		this.flushBuffer(this.byteBuffer, this.monitoringRecordChannel);
		this.flushBuffer(this.byteBuffer, this.registryRecordChannel);

		this.closeChannel(this.monitoringRecordChannel);
		this.closeChannel(this.registryRecordChannel);
	}

	private void flushBuffer(final ByteBuffer buffer, final SocketChannel socketChannel) {
		buffer.flip();
		try {
			while (buffer.hasRemaining()) {
				socketChannel.write(buffer);
			}
			buffer.clear();
		} catch (final IOException e) {
			LOG.error("Error in writing the record", e);
			this.closeChannel(socketChannel);
		}
	}

	private void closeChannel(final SocketChannel socketChannel) {
		try {
			socketChannel.close();
		} catch (final IOException e) {
			LOG.warn("Error in closing the connection.", e);
		}
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
