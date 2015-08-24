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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.RegistryRecord;
import kieker.common.util.registry.IRegistry;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.AbstractAsyncThread;
import kieker.monitoring.writer.AbstractAsyncWriter;

/**
 * Monitoring record writer which sends records using the AMQP protocol to a message queue.
 *
 * @author Holger Knoche
 *
 * @since 1.12
 */
public final class AMQPWriter extends AbstractAsyncWriter {

	/** ID for registry records. */
	public static final byte REGISTRY_RECORD_ID = (byte) 0xFF;

	/** ID for regular records. */
	public static final byte REGULAR_RECORD_ID = (byte) 0x01;

	private static final String PREFIX = AMQPWriter.class.getName() + ".";

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

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this writer.
	 */
	public AMQPWriter(final Configuration configuration) {
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
	}

	@Override
	protected void init() throws Exception {
		this.addWorker(new AMQPWriterThread(this.monitoringController, this.blockingQueue, this.uri, this.heartbeat, this.exchangeName, this.queueName));
		this.addWorker(new AMQPWriterThread(this.monitoringController, this.prioritizedBlockingQueue, this.uri, this.heartbeat, this.exchangeName, this.queueName));
	}

}

/**
 * Writer thread for AMQP messages.
 *
 * @author Holger Knoche
 *
 * @since 1.12
 */
final class AMQPWriterThread extends AbstractAsyncThread {

	private static final Log LOG = LogFactory.getLog(AMQPWriterThread.class);

	private static final int DEFAULT_BUFFER_SIZE = 16384;

	private final String uri;
	private final int heartbeat;
	private final String exchangeName;
	private final String queueName;

	private final Connection connection;
	private final Channel channel;

	private final ByteBuffer buffer;
	private final IRegistry<String> stringRegistry;

	public AMQPWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final String uri, final int heartbeat, final String exchangeName, final String queueName)
					throws TimeoutException, IOException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException {
		super(monitoringController, writeQueue);

		this.uri = uri;
		this.heartbeat = heartbeat;
		this.exchangeName = exchangeName;
		this.queueName = queueName;

		this.connection = this.createConnection();
		this.channel = this.connection.createChannel();

		this.buffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
		this.stringRegistry = this.monitoringController.getStringRegistry();
	}

	private Connection createConnection() throws TimeoutException, KeyManagementException, NoSuchAlgorithmException, IOException, URISyntaxException {
		final ConnectionFactory connectionFactory = new ConnectionFactory();

		connectionFactory.setUri(this.uri);
		connectionFactory.setRequestedHeartbeat(this.heartbeat);

		// Use only daemon threads for connections. Otherwise, all connections would have to be explicitly
		// closed for the JVM to terminate.
		connectionFactory.setThreadFactory(new DaemonThreadFactory());

		return connectionFactory.newConnection();
	}

	@Override
	protected void consume(final IMonitoringRecord monitoringRecord) throws Exception {
		if (monitoringRecord instanceof RegistryRecord) {
			final ByteBuffer localBuffer = ByteBuffer.allocate(monitoringRecord.getSize() + 1);
			localBuffer.put(AMQPWriter.REGISTRY_RECORD_ID);
			monitoringRecord.writeBytes(localBuffer, this.stringRegistry);
			localBuffer.flip();

			final byte[] data = localBuffer.array();
			this.channel.basicPublish(this.exchangeName, this.queueName, null, data);
		} else {
			final ByteBuffer localBuffer = this.buffer;
			localBuffer.clear();

			localBuffer.put(AMQPWriter.REGULAR_RECORD_ID);
			localBuffer.putInt(this.monitoringController.getUniqueIdForString(monitoringRecord.getClass().getName()));
			localBuffer.putLong(monitoringRecord.getLoggingTimestamp());
			monitoringRecord.writeBytes(localBuffer, this.stringRegistry);

			localBuffer.flip();
			final int dataSize = localBuffer.limit();
			final byte[] data = new byte[dataSize];
			System.arraycopy(localBuffer.array(), localBuffer.arrayOffset(), data, 0, dataSize);

			this.channel.basicPublish(this.exchangeName, this.queueName, null, data);
		}
	}

	@Override
	protected void cleanup() {
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
