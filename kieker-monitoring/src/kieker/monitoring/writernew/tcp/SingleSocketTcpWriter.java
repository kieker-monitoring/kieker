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
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.RegistryRecord;
import kieker.monitoring.registry.GetIdAdapter;
import kieker.monitoring.registry.IRegistryListener;
import kieker.monitoring.registry.RegisterAdapter;
import kieker.monitoring.registry.WriterRegistry;
import kieker.monitoring.writernew.AbstractMonitoringWriter;
import kieker.monitoring.writernew.WriterUtil;

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

	private final WritableByteChannel socketChannel;
	private final ByteBuffer buffer;
	private final boolean flush;

	private final WriterRegistry writerRegistry;
	private final RegisterAdapter<String> registerStringsAdapter;
	private final GetIdAdapter<String> writeBytesAdapter;

	public SingleSocketTcpWriter(final Configuration configuration) throws IOException {
		super(configuration);
		final String hostname = configuration.getStringProperty(CONFIG_HOSTNAME);
		final int port1 = configuration.getIntProperty(CONFIG_PORT1);
		// buffer size is available by byteBuffer.capacity()
		this.socketChannel = SocketChannel.open(new InetSocketAddress(hostname, port1));
		// TODO should we check for buffers too small for a single record?
		final int bufferSize = this.configuration.getIntProperty(CONFIG_BUFFERSIZE);
		this.buffer = ByteBuffer.allocateDirect(bufferSize);
		this.flush = configuration.getBooleanProperty(CONFIG_FLUSH);

		this.writerRegistry = new WriterRegistry(this);
		this.registerStringsAdapter = new RegisterAdapter<String>(this.writerRegistry);
		this.writeBytesAdapter = new GetIdAdapter<String>(this.writerRegistry);
	}

	@Override
	public void onStarting() {
		// do nothing
	}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		monitoringRecord.registerStrings(this.registerStringsAdapter);

		final ByteBuffer recordBuffer = this.buffer;
		if ((4 + 8 + monitoringRecord.getSize()) > recordBuffer.remaining()) {
			WriterUtil.flushBuffer(recordBuffer, this.socketChannel, LOG);
		}

		final String recordClassName = monitoringRecord.getClass().getName();
		this.writerRegistry.register(recordClassName);

		recordBuffer.putInt(this.writerRegistry.getId(recordClassName));
		recordBuffer.putLong(monitoringRecord.getLoggingTimestamp());
		monitoringRecord.writeBytes(recordBuffer, this.writeBytesAdapter);
		// monitoringRecord.writeToBuffer(buffer, this.writerRegistry);

		if (this.flush) {
			WriterUtil.flushBuffer(recordBuffer, this.socketChannel, LOG);
		}
	}

	@Override
	public void onNewRegistryEntry(final String value, final int id) {
		final ByteBuffer registryBuffer = this.buffer;

		final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
		// logging timestamp + class id + RegistryRecord.SIZE + bytes.length
		final int requiredBufferSize = (2 * AbstractMonitoringRecord.TYPE_SIZE_INT) + RegistryRecord.SIZE + bytes.length;

		if (registryBuffer.remaining() < requiredBufferSize) {
			WriterUtil.flushBuffer(registryBuffer, this.socketChannel, LOG);
		}

		registryBuffer.putInt(RegistryRecord.CLASS_ID);
		registryBuffer.putInt(id);
		registryBuffer.putInt(value.length());
		registryBuffer.put(bytes);
	}

	@Override
	public void onTerminating() {
		WriterUtil.flushBuffer(this.buffer, this.socketChannel, LOG);
		WriterUtil.close(this.socketChannel, LOG);
	}
}
