/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import kieker.common.configuration.Configuration;
import kieker.common.util.thread.DaemonThreadFactory;
import kieker.monitoring.writer.raw.IRawDataWriter;

/**
 * AMQP writer plugin that supports chunking via the new raw data I/O infrastructure.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
public class ChunkingAmqpWriter implements IRawDataWriter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChunkingAmqpWriter.class);

	private static final String PREFIX = ChunkingAmqpWriter.class.getName() + ".";

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

	private final Connection connection;
	private final Channel channel;

	public ChunkingAmqpWriter(final Configuration configuration)
			throws KeyManagementException, NoSuchAlgorithmException, URISyntaxException, IOException, TimeoutException {
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

		this.connection = this.createConnection();
		this.channel = this.connection.createChannel();
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
	public void onInitialization() {
		// Do nothing
	}

	@Override
	public void onTermination() {
		try {
			this.connection.close();
		} catch (final IOException e) {
			LOGGER.error("Error closing connection", e);
		}
	}

	@Override
	public void writeData(final ByteBuffer buffer, final int offset, final int length) {
		buffer.position(offset);
		final byte[] rawData = new byte[length];
		buffer.get(rawData);

		try {
			this.channel.basicPublish(this.exchangeName, this.queueName, null, rawData);
		} catch (final IOException e) {
			LOGGER.error("An exception occurred publishing the data.", e);
		}
	}

}
