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

package kieker.monitoring.writer.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.DefaultValueSerializer;
import kieker.common.record.misc.RegistryRecord;
import kieker.monitoring.registry.GetIdAdapter;
import kieker.monitoring.registry.IRegistryListener;
import kieker.monitoring.registry.IWriterRegistry;
import kieker.monitoring.registry.RegisterAdapter;
import kieker.monitoring.registry.WriterRegistry;
import kieker.monitoring.writer.AbstractMonitoringWriter;
import kieker.monitoring.writer.WriterUtil;

/**
 * @author "Christian Wulf"
 *
 * @since 1.13
 */
public class DualSocketTcpWriter extends AbstractMonitoringWriter implements IRegistryListener<String> {

	/** default size for the monitoring buffer. */
	private static final int DEFAULT_STRING_REGISTRY_BUFFER_SIZE = 1024;
	/** the logger for this class. */
	private static final Log LOG = LogFactory.getLog(DualSocketTcpWriter.class);
	/** prefix for all configuration keys. */
	private static final String PREFIX = DualSocketTcpWriter.class.getName() + ".";

	/** configuration key for the hostname. */
	public static final String CONFIG_HOSTNAME = PREFIX + "hostname"; // NOCS (afterPREFIX)
	/** configuration key for the monitoring port. */
	public static final String CONFIG_PORT1 = PREFIX + "port1"; // NOCS (afterPREFIX)
	/** configuration key for the registry port. */
	public static final String CONFIG_PORT2 = PREFIX + "port2"; // NOCS (afterPREFIX)
	/** configuration key for the size of the {@link #byteBuffer}. */
	public static final String CONFIG_BUFFERSIZE = PREFIX + "bufferSize"; // NOCS (afterPREFIX)
	/** configuration key for {@link #flush}. */
	public static final String CONFIG_FLUSH = PREFIX + "flush"; // NOCS (afterPREFIX)
	/** configuration key for the size of the {@link #stringRegistryBuffer}. */
	private static final String CONFIG_STRING_REGISTRY_BUFFERSIZE = PREFIX + "StringRegistryBufferSize"; // NOCS (afterPREFIX)

	/** <code>true</code> if the {@link #byteBuffer} should be flushed upon each new incoming monitoring record. */
	private final boolean flush;

	/** the channel which writes out monitoring records. */
	private final SocketChannel monitoringRecordChannel;
	/** the channel which writes out registry records. */
	private final SocketChannel registryRecordChannel;
	/** the buffer used for buffering monitoring records. */
	private final ByteBuffer byteBuffer;
	/** the buffer used for buffering registry records. */
	private final ByteBuffer stringRegistryBuffer;

	/** the registry used to compress string fields in monitoring records. */
	private final IWriterRegistry<String> writerRegistry;
	/** this adapter allows to use the new WriterRegistry with the legacy IRegistry in {@link AbstractMonitoringRecord.registerStrings(..)}. */
	private final RegisterAdapter<String> registerStringsAdapter;
	/** this adapter allows to use the new WriterRegistry with the legacy IRegistry in {@link AbstractMonitoringRecord.serialize(..)}. */
	private final GetIdAdapter<String> writeBytesAdapter;

	public DualSocketTcpWriter(final Configuration configuration) throws IOException {
		super(configuration);
		final String hostname = configuration.getStringProperty(CONFIG_HOSTNAME);
		final int monitoringPort = configuration.getIntProperty(CONFIG_PORT1);
		final int registryPort = configuration.getIntProperty(CONFIG_PORT2);
		// TODO should be check for buffers too small for a single record?
		final int bufferSize = configuration.getIntProperty(CONFIG_BUFFERSIZE);
		int stringRegistryBufferSize = configuration.getIntProperty(CONFIG_STRING_REGISTRY_BUFFERSIZE);
		if (stringRegistryBufferSize <= 0) {
			LOG.warn("Invalid buffer size passed for string registry records: " + stringRegistryBufferSize + ". Defaults to " + DEFAULT_STRING_REGISTRY_BUFFER_SIZE);
			stringRegistryBufferSize = DEFAULT_STRING_REGISTRY_BUFFER_SIZE;
		}

		this.flush = configuration.getBooleanProperty(CONFIG_FLUSH);

		this.byteBuffer = ByteBuffer.allocateDirect(bufferSize);
		this.stringRegistryBuffer = ByteBuffer.allocateDirect(stringRegistryBufferSize);
		// buffer size is available by byteBuffer.capacity()
		this.monitoringRecordChannel = SocketChannel.open(new InetSocketAddress(hostname, monitoringPort));
		this.registryRecordChannel = SocketChannel.open(new InetSocketAddress(hostname, registryPort));

		this.writerRegistry = new WriterRegistry(this);
		this.registerStringsAdapter = new RegisterAdapter<String>(this.writerRegistry);
		this.writeBytesAdapter = new GetIdAdapter<String>(this.writerRegistry);

		// this.encoder = StandardCharsets.UTF_8.newEncoder();
	}

	@Override
	public void onStarting() {
		// do nothing
	}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		monitoringRecord.registerStrings(this.registerStringsAdapter);

		final ByteBuffer buffer = this.byteBuffer;
		final int requiredBufferSize = 4 + 8 + monitoringRecord.getSize();
		if (requiredBufferSize > buffer.remaining()) {
			WriterUtil.flushBuffer(buffer, this.monitoringRecordChannel, LOG);
		}

		final String recordClassName = monitoringRecord.getClass().getName();
		this.writerRegistry.register(recordClassName);

		final int recordClassId = this.writerRegistry.getId(recordClassName);
		final long loggingTimestamp = monitoringRecord.getLoggingTimestamp();

		buffer.putInt(recordClassId);
		buffer.putLong(loggingTimestamp);

		monitoringRecord.serialize(DefaultValueSerializer.create(buffer, this.writeBytesAdapter));
		// monitoringRecord.writeToBuffer(buffer, this.writerRegistry);

		if (this.flush) {
			WriterUtil.flushBuffer(buffer, this.monitoringRecordChannel, LOG);
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

		// always flush so that the stringRegistryBuffer is transmitted before the byteBuffer
		WriterUtil.flushBuffer(buffer, this.registryRecordChannel, LOG);
	}

	@Override
	public void onTerminating() {
		WriterUtil.flushBuffer(this.byteBuffer, this.monitoringRecordChannel, LOG);
		WriterUtil.flushBuffer(this.byteBuffer, this.registryRecordChannel, LOG);

		WriterUtil.close(this.monitoringRecordChannel, LOG);
		WriterUtil.close(this.registryRecordChannel, LOG);
	}
}
