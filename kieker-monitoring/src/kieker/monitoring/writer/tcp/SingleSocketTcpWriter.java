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
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.AbstractMonitoringRecord;
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
 * Represents a monitoring writer which serializes records via TCP to a given
 * host:port.
 *
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class SingleSocketTcpWriter extends AbstractMonitoringWriter implements IRegistryListener<String> {

	/**
	 * This writer can be configured by the configuration file "kieker.properties".
	 * For this purpose, it uses this prefix for all configuration keys.
	 */
	public static final String PREFIX = SingleSocketTcpWriter.class.getName() + ".";

	/** configuration key for the hostname. */
	public static final String CONFIG_HOSTNAME = PREFIX + "hostname"; // NOCS
																		// (afterPREFIX)
	/** configuration key for the port. */
	public static final String CONFIG_PORT = PREFIX + "port"; // NOCS
																// (afterPREFIX)
	/** configuration key for the size of the {@link #buffer}. */
	public static final String CONFIG_BUFFERSIZE = PREFIX + "bufferSize"; // NOCS
																			// (afterPREFIX)
	/** configuration key for {@link #flush}. */
	public static final String CONFIG_FLUSH = PREFIX + "flush"; // NOCS

	/** configuration key for {@link #connectionTimeoutInMs}. */
	public static final String CONFIG_CONN_TIMEOUT_IN_MS = PREFIX + "connectionTimeoutInMs";

	/** the logger for this class. */
	private static final Log LOG = LogFactory.getLog(SingleSocketTcpWriter.class);

	// (afterPREFIX)
	/** the host name and the port of the record reader. */
	private final InetSocketAddress socketAddress;
	/** the connection timeout for the initial connect. */
	private final int connectionTimeoutInMs;
	/** the channel which writes out monitoring and registry records. */
	private SocketChannel socketChannel;
	/** the buffer used for buffering monitoring records. */
	private final ByteBuffer buffer;
	/** the buffer used for buffering registry records. */
	private final ByteBuffer registryBuffer;
	/**
	 * <code>true</code> if the {@link #buffer} should be flushed upon each new
	 * incoming monitoring record.
	 */
	private final boolean flush;
	/** the serializer to use for the incoming records. */
	private final IValueSerializer serializer;

	// remove RegisterAdapter

	public SingleSocketTcpWriter(final Configuration configuration) throws IOException {
		super(configuration);
		final String hostname = configuration.getStringProperty(CONFIG_HOSTNAME);
		final int port = configuration.getIntProperty(CONFIG_PORT);
		this.socketAddress = new InetSocketAddress(hostname, port);

		final int configConnectionTimeoutInMs = configuration.getIntProperty(CONFIG_CONN_TIMEOUT_IN_MS, 1);
		if (configConnectionTimeoutInMs > 0) {
			this.connectionTimeoutInMs = configConnectionTimeoutInMs;
		} else {
			this.connectionTimeoutInMs = 1;
		}

		// buffer size is available by byteBuffer.capacity()
		// TODO should we check for buffers too small for a single record?
		final int bufferSize = this.configuration.getIntProperty(CONFIG_BUFFERSIZE);
		this.buffer = ByteBuffer.allocateDirect(bufferSize);
		this.registryBuffer = ByteBuffer.allocateDirect(bufferSize);
		this.flush = configuration.getBooleanProperty(CONFIG_FLUSH);

		final WriterRegistry writerRegistry = new WriterRegistry(this);
		this.serializer = BinaryValueSerializer.create(this.buffer, new GetIdAdapter<>(writerRegistry));
	}

	@Override
	public void onStarting() {
		final TimeoutCountdown timeoutCountdown = new TimeoutCountdown(this.connectionTimeoutInMs);

		do {
			try {
				this.socketChannel = SocketChannel.open();
			} catch (final IOException e) {
				throw new IllegalStateException(e);
			}

			this.tryConnect(timeoutCountdown);
		} while (!this.socketChannel.isConnected());

		final String message = String.format("Successfully connected to %s.", this.socketAddress);
		LOG.info(message);
	}

	private void tryConnect(final TimeoutCountdown timeoutCountdown) throws ConnectionTimeoutException {
		final Socket socket = this.socketChannel.socket();

		final long startTimestampInNs = System.nanoTime(); // NOPMD (PrematureDeclaration)

		if (this.connectOrTimeout(socket, timeoutCountdown.getCurrentTimeoutinMs())) {
			return;
		}

		final long currentTimestampInNs = System.nanoTime();

		final long elapsedTimeInNs = currentTimestampInNs - startTimestampInNs;
		final long elapsedTimeInMs = TimeUnit.NANOSECONDS.toMillis(elapsedTimeInNs);
		timeoutCountdown.countdown(elapsedTimeInMs);

		if (timeoutCountdown.getCurrentTimeoutinMs() <= 0) {
			final String message = String.format("Connection timeout of %d ms exceeded.", this.connectionTimeoutInMs);
			throw new ConnectionTimeoutException(message);
		}
	}

	/**
	 * @return <code>true</code> if connected, <code>false</code> if not connected
	 *         due to a timeout.
	 */
	private boolean connectOrTimeout(final Socket socket, final int timeoutInMs) {
		try {
			socket.connect(this.socketAddress, timeoutInMs);
			return true;
		} catch (SocketTimeoutException | ConnectException e) {
			// both of the exceptions indicate a connection timeout
			// => ignore to reconnect
			return false;
		} catch (final IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		final ByteBuffer recordBuffer = this.buffer;
		if ((4 + 8 + monitoringRecord.getSize()) > recordBuffer.remaining()) {
			// Always flush the registryBuffer before flushing the recordBuffer.
			// Otherwise the monitoring records could arrive before their string
			// records
			WriterUtil.flushBuffer(this.registryBuffer, this.socketChannel, LOG);
			WriterUtil.flushBuffer(recordBuffer, this.socketChannel, LOG);
		}

		final String recordClassName = monitoringRecord.getClass().getName();

		this.serializer.putString(recordClassName);
		this.serializer.putLong(monitoringRecord.getLoggingTimestamp());
		monitoringRecord.serialize(this.serializer);

		if (this.flush) {
			// Always flush the registryBuffer before flushing the recordBuffer.
			// Otherwise the monitoring records could arrive before their string
			// records
			WriterUtil.flushBuffer(this.registryBuffer, this.socketChannel, LOG);
			WriterUtil.flushBuffer(recordBuffer, this.socketChannel, LOG);
		}
	}

	@Override
	public void onNewRegistryEntry(final String value, final int id) {
		final ByteBuffer localRegistryBuffer = this.registryBuffer;

		final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
		// logging timestamp + class id + RegistryRecord.SIZE + bytes.length
		final int requiredBufferSize = (2 * AbstractMonitoringRecord.TYPE_SIZE_INT) + RegistryRecord.SIZE
				+ bytes.length;

		if (localRegistryBuffer.remaining() < requiredBufferSize) {
			WriterUtil.flushBuffer(localRegistryBuffer, this.socketChannel, LOG);
		}

		localRegistryBuffer.putInt(RegistryRecord.CLASS_ID);
		localRegistryBuffer.putInt(id);
		localRegistryBuffer.putInt(value.length());
		localRegistryBuffer.put(bytes);
	}

	@Override
	public void onTerminating() {
		// Always flush the registryBuffer before flushing the recordBuffer.
		// Otherwise the monitoring records could arrive before their string
		// records
		WriterUtil.flushBuffer(this.registryBuffer, this.socketChannel, LOG);
		WriterUtil.flushBuffer(this.buffer, this.socketChannel, LOG);
		WriterUtil.close(this.socketChannel, LOG);
	}
}
