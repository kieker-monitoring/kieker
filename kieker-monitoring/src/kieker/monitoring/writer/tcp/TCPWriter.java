/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.BinaryValueSerializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.record.misc.RegistryRecord;
import kieker.monitoring.registry.GetIdAdapter;
import kieker.monitoring.registry.IRegistryListener;
import kieker.monitoring.registry.WriterRegistry;
import kieker.monitoring.writer.AbstractMonitoringWriter;
import kieker.monitoring.writer.WriterUtil;

/**
 * @author "Christian Wulf"
 *
 * @deprecated since 1.13 (to be removed in 1.14) Use {@link DualSocketTcpWriter} instead.
 *
 * @since unknown
 */
@Deprecated
public class TCPWriter extends AbstractMonitoringWriter implements IRegistryListener<String> {

	private static final int DEFAULT_STRING_REGISTRY_BUFFER_SIZE = 1024;

	private static final Logger LOGGER = LoggerFactory.getLogger(TCPWriter.class);

	private static final String PREFIX = TCPWriter.class.getName() + ".";

	public static final String CONFIG_HOSTNAME = PREFIX + "hostname"; // NOCS (afterPREFIX)
	public static final String CONFIG_PORT1 = PREFIX + "port1"; // NOCS (afterPREFIX)
	public static final String CONFIG_PORT2 = PREFIX + "port2"; // NOCS (afterPREFIX)
	public static final String CONFIG_BUFFERSIZE = PREFIX + "bufferSize"; // NOCS (afterPREFIX)
	public static final String CONFIG_FLUSH = PREFIX + "flush"; // NOCS (afterPREFIX)

	private static final String CONFIG_STRING_REGISTRY_BUFFERSIZE = PREFIX + "stringRegistryBufferSize"; // NOCS (afterPREFIX)

	private final boolean flush;
	private final SocketChannel monitoringRecordChannel;
	private final SocketChannel registryRecordChannel;
	private final ByteBuffer recordBuffer;
	/** the buffer used for buffering registry records. */
	private final ByteBuffer stringRegistryBuffer;
	/** the serializer to use for the incoming records */
	private final IValueSerializer serializer;

	public TCPWriter(final Configuration configuration) throws IOException {
		super(configuration);
		final String hostname = configuration.getStringProperty(CONFIG_HOSTNAME);
		final int port1 = configuration.getIntProperty(CONFIG_PORT1);
		final int port2 = configuration.getIntProperty(CONFIG_PORT2);
		// TODO should be check for buffers too small for a single record?
		final int bufferSize = configuration.getIntProperty(CONFIG_BUFFERSIZE);
		int stringRegistryBufferSize = configuration.getIntProperty(CONFIG_STRING_REGISTRY_BUFFERSIZE);
		if (stringRegistryBufferSize <= 0) {
			LOGGER.warn("Invalid buffer size passed for string registry records: {}. Defaults to {}", stringRegistryBufferSize, DEFAULT_STRING_REGISTRY_BUFFER_SIZE);
			stringRegistryBufferSize = DEFAULT_STRING_REGISTRY_BUFFER_SIZE;
		}

		this.flush = configuration.getBooleanProperty(CONFIG_FLUSH);

		this.recordBuffer = ByteBuffer.allocateDirect(bufferSize);
		this.stringRegistryBuffer = ByteBuffer.allocateDirect(stringRegistryBufferSize);
		// buffer size is available by byteBuffer.capacity()
		this.monitoringRecordChannel = SocketChannel.open(new InetSocketAddress(hostname, port1));
		this.registryRecordChannel = SocketChannel.open(new InetSocketAddress(hostname, port2));

		final WriterRegistry writerRegistry = new WriterRegistry(this);
		this.serializer = BinaryValueSerializer.create(this.recordBuffer, new GetIdAdapter<>(writerRegistry));
	}

	@Override
	public void onStarting() {
		// do nothing
	}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		final ByteBuffer buffer = this.recordBuffer;
		final int requiredBufferSize = 4 + 8 + monitoringRecord.getSize();
		if (requiredBufferSize > buffer.remaining()) {
			WriterUtil.flushBuffer(buffer, this.monitoringRecordChannel, LOGGER);
		}

		final String recordClassName = monitoringRecord.getClass().getName();

		this.serializer.putString(recordClassName);
		this.serializer.putLong(monitoringRecord.getLoggingTimestamp());
		monitoringRecord.serialize(this.serializer);

		if (this.flush) {
			WriterUtil.flushBuffer(buffer, this.monitoringRecordChannel, LOGGER);
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
		WriterUtil.flushBuffer(buffer, this.registryRecordChannel, LOGGER);
	}

	@Override
	public void onTerminating() {
		WriterUtil.flushBuffer(this.stringRegistryBuffer, this.registryRecordChannel, LOGGER);
		WriterUtil.flushBuffer(this.recordBuffer, this.monitoringRecordChannel, LOGGER);

		WriterUtil.close(this.registryRecordChannel, LOGGER);
		WriterUtil.close(this.monitoringRecordChannel, LOGGER);
	}

}
