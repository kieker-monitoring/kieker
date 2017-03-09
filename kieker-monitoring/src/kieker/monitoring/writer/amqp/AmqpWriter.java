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

package kieker.monitoring.writer.amqp;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.DefaultValueSerializer;
import kieker.common.record.misc.RegistryRecord;
import kieker.monitoring.registry.GetIdAdapter;
import kieker.monitoring.registry.IRegistryListener;
import kieker.monitoring.registry.RegisterAdapter;
import kieker.monitoring.registry.WriterRegistry;
import kieker.monitoring.writer.AbstractMonitoringWriter;

/**
 * Monitoring record writer which sends records using the AMQP protocol to a message queue.
 *
 * @author Holger Knoche, Christian Wulf
 *
 * @since 1.12
 */
public class AmqpWriter extends AbstractMonitoringWriter implements IRegistryListener<String> {

	/** ID for registry records. */
	public static final byte REGISTRY_RECORD_ID = (byte) 0xFF;
	/** ID for regular records. */
	public static final byte REGULAR_RECORD_ID = (byte) 0x01;

	private static final Log LOG = LogFactory.getLog(AmqpWriter.class);
	/** The default size for the buffer used to serialize records */
	private static final int DEFAULT_BUFFER_SIZE = 16384;
	/** Size of the "envelope" data which is prepended before the actual record. */
	private static final int SIZE_OF_ENVELOPE = 1 + 8;

	private static final String PREFIX = AmqpWriter.class.getName() + ".";

	/** The name of the configuration property for the server URI. */
	public static final String CONFIG_URI = PREFIX + "uri"; // NOCS (afterPREFIX)
	/** The name of the configuration property for the AMQP exchange name. */
	public static final String CONFIG_EXCHANGENAME = PREFIX + "exchangename"; // NOCS (afterPREFIX)
	/** The name of the configuration property for the AMQP queue name. */
	public static final String CONFIG_QUEUENAME = PREFIX + "queuename"; // NOCS (afterPREFIX)
	/** The name of the configuration property for the heartbeat timeout. */
	public static final String CONFIG_HEARTBEAT = PREFIX + "heartbeat"; // NOCS (afterPREFIX)

	/**
	 * Default heartbeat timeout interval in seconds.
	 */
	private static final int DEFAULT_HEARTBEAT = 60;

	private final String uri;
	private final String exchangeName;
	private final String queueName;
	private final int heartbeat;

	private final ByteBuffer buffer;
	private final Connection connection;
	private final Channel channel;

	private final WriterRegistry writerRegistry;
	/**
	 * Adapter for the current, generated record structure.
	 * The record generator should generate records with the new interface.
	 */
	private final RegisterAdapter<String> registerStringsAdapter;
	/**
	 * Adapter for the current, generated record structure.
	 * The record generator should generate records with the new interface.
	 */
	private final GetIdAdapter<String> writeBytesAdapter;

	public AmqpWriter(final Configuration configuration) throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, TimeoutException {
		super(configuration);

		// Read configuration parameters from configuration
		this.uri = configuration.getStringProperty(CONFIG_URI);
		this.exchangeName = configuration.getStringProperty(CONFIG_EXCHANGENAME);
		this.queueName = configuration.getStringProperty(CONFIG_QUEUENAME);

		final int configuredHeartbeat = configuration.getIntProperty(CONFIG_HEARTBEAT);
		if (configuredHeartbeat == 0) {
			this.heartbeat = DEFAULT_HEARTBEAT;
		} else {
			this.heartbeat = configuredHeartbeat;
		}

		final int bufferSize = DEFAULT_BUFFER_SIZE;
		this.buffer = ByteBuffer.allocate(bufferSize);

		this.connection = this.createConnection();
		this.channel = this.connection.createChannel();

		this.writerRegistry = new WriterRegistry(this);
		this.registerStringsAdapter = new RegisterAdapter<String>(this.writerRegistry);
		this.writeBytesAdapter = new GetIdAdapter<String>(this.writerRegistry);
	}

	private Connection createConnection() throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, TimeoutException {
		final ConnectionFactory connectionFactory = new ConnectionFactory();

		connectionFactory.setUri(this.uri);
		connectionFactory.setRequestedHeartbeat(this.heartbeat);
		// Use only daemon threads for connections. Otherwise, all connections would have to be explicitly
		// closed for the JVM to terminate.
		connectionFactory.setThreadFactory(new DaemonThreadFactory());

		return connectionFactory.newConnection();
	}

	@Override
	public void onStarting() {
		// do nothing
	}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		monitoringRecord.registerStrings(this.registerStringsAdapter);

		final ByteBuffer recordBuffer = this.buffer;
		final int requiredBufferSize = SIZE_OF_ENVELOPE + 4 + 8 + monitoringRecord.getSize();
		if (recordBuffer.capacity() < requiredBufferSize) {
			throw new IllegalStateException("Insufficient buffer capacity for string registry data");
		}

		// register monitoringRecord class name
		final String recordClassName = monitoringRecord.getClass().getName();
		this.writerRegistry.register(recordClassName);

		// Prepend envelope data
		recordBuffer.put(REGULAR_RECORD_ID);
		recordBuffer.putLong(this.writerRegistry.getId());

		// serialized monitoringRecord
		recordBuffer.putInt(this.writerRegistry.getId(recordClassName));
		recordBuffer.putLong(monitoringRecord.getLoggingTimestamp());
		monitoringRecord.writeBytes(DefaultValueSerializer.instance(), recordBuffer, this.writeBytesAdapter);

		this.publishBuffer(recordBuffer);
	}

	@Override
	public void onNewRegistryEntry(final String value, final int id) {
		final ByteBuffer registryBuffer = this.buffer;

		final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

		final int requiredBufferSize = SIZE_OF_ENVELOPE + RegistryRecord.SIZE + bytes.length;
		if (registryBuffer.capacity() < requiredBufferSize) {
			throw new IllegalStateException("Insufficient buffer capacity for string registry data");
		}

		// Prepend envelope data.
		registryBuffer.put(REGISTRY_RECORD_ID);
		registryBuffer.putLong(this.writerRegistry.getId());
		// id-string pair
		registryBuffer.putInt(id);
		registryBuffer.putInt(bytes.length);
		registryBuffer.put(bytes);

		this.publishBuffer(registryBuffer);
	}

	private void publishBuffer(final ByteBuffer localBuffer) {
		localBuffer.flip();
		final int dataSize = localBuffer.limit();
		final byte[] data = new byte[dataSize];
		System.arraycopy(localBuffer.array(), localBuffer.arrayOffset(), data, 0, dataSize);

		try {
			this.channel.basicPublish(this.exchangeName, this.queueName, null, data);
		} catch (final IOException e) {
			LOG.error("An exception occurred", e);
		}
	}

	@Override
	public void onTerminating() {
		try {
			this.connection.close();
		} catch (final IOException e) {
			LOG.error("Error closing connection", e);
		}
	}

	/**
	 * A thread factory that creates daemon threads. The default thread parameters are based on
	 * the default thread factory.
	 *
	 * @author Holger Knoche
	 *
	 * @since 1.12
	 */
	private static class DaemonThreadFactory implements ThreadFactory {

		DaemonThreadFactory() {
			// Do nothing
		}

		@Override
		public Thread newThread(final Runnable runnable) {
			final Thread thread = new Thread(runnable);

			thread.setDaemon(true);
			thread.setPriority(Thread.NORM_PRIORITY);

			return thread;
		}

	}
}
