/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.generic.source.amqp;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

import kieker.common.record.IMonitoringRecord;
import kieker.common.registry.reader.ReaderRegistry;

/**
 * Logic module for the reader stage that reads monitoring records from an AMQP queue.
 *
 * @author Holger Knoche, Lars Bluemke, Sören Henning
 *
 * @since 1.12
 */
public final class AMQPReader {

	private static final Logger LOGGER = LoggerFactory.getLogger(AMQPReader.class);

	/** ID for registry records. */
	private static final byte REGISTRY_RECORD_ID = (byte) 0xFF;

	/** ID for regular records. */
	private static final byte REGULAR_RECORD_ID = (byte) 0x01;

	private final String uri;
	private final String queueName;
	private final int heartbeat;

	private volatile Connection connection;
	private volatile Channel channel;
	private volatile QueueingConsumer consumer;

	private final ReaderRegistry<String> stringRegistry = new ReaderRegistry<>();

	private volatile Thread registryRecordHandlerThread;
	private volatile RegistryRecordHandler registryRecordHandler;
	private volatile RegularRecordHandler regularRecordHandler;

	private volatile Thread regularRecordHandlerThread;

	private volatile boolean terminated;
	private volatile boolean threadsStarted;

	private final Consumer<IMonitoringRecord> elementReceivedCallback;

	/**
	 * Creates a new logic module for an AMQP reader.
	 *
	 * @param uri
	 *            The name of the configuration property for the server URI.
	 * @param queueName
	 *            The name of the configuration property for the AMQP queue name.
	 * @param heartbeat
	 *            The name of the configuration property for the heartbeat timeout.
	 * @param elementReceivedCallback
	 *            The actual teetime stage which uses this class.
	 */
	public AMQPReader(final String uri, final String queueName, final int heartbeat, final Consumer<IMonitoringRecord> elementReceivedCallback) {
		this.uri = uri;
		this.queueName = queueName;
		this.heartbeat = heartbeat;
		this.elementReceivedCallback = elementReceivedCallback;
		this.init();
	}

	public void init() {
		try {
			this.connection = this.createConnection();
			this.channel = this.connection.createChannel();
			this.consumer = new QueueingConsumer(this.channel);

			// Set up record handlers
			this.registryRecordHandler = new RegistryRecordHandler(this.stringRegistry);
			this.regularRecordHandler = new RegularRecordHandler(this, this.stringRegistry);

			// Set up threads
			this.registryRecordHandlerThread = new Thread(this.registryRecordHandler);
			this.registryRecordHandlerThread.setDaemon(true);

			this.regularRecordHandlerThread = new Thread(this.regularRecordHandler);
			this.regularRecordHandlerThread.setDaemon(true);
		} catch (final KeyManagementException | NoSuchAlgorithmException | IOException | TimeoutException | URISyntaxException e) {
			this.handleInitializationError(e);
		}
	}

	private void handleInitializationError(final Throwable e) {
		LOGGER.error("An error occurred initializing the AMQP reader: {}", e);
	}

	private Connection createConnection() throws IOException, TimeoutException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException {
		final ConnectionFactory connectionFactory = new ConnectionFactory();

		connectionFactory.setUri(this.uri);
		connectionFactory.setRequestedHeartbeat(this.heartbeat);

		return connectionFactory.newConnection();
	}

	public boolean read() {
		// Start the worker threads, if necessary
		if (!this.threadsStarted) {
			this.registryRecordHandlerThread.start();
			this.regularRecordHandlerThread.start();
			this.threadsStarted = true;
		}

		try {
			this.channel.basicConsume(this.queueName, true, this.consumer);

			while (!this.terminated) {
				final QueueingConsumer.Delivery delivery = this.consumer.nextDelivery();
				final byte[] body = delivery.getBody();

				final ByteBuffer buffer = ByteBuffer.wrap(body);
				final byte recordType = buffer.get();

				// Dispatch to the appropriate handlers
				switch (recordType) {
				case REGISTRY_RECORD_ID:
					this.registryRecordHandler.enqueueRegistryRecord(buffer);
					break;
				case REGULAR_RECORD_ID:
					this.regularRecordHandler.enqueueRegularRecord(buffer);
					break;
				default:
					if (LOGGER.isErrorEnabled()) {
						LOGGER.error(String.format("Unknown record type: %02x", recordType));
					}
					break;
				}
			}
		} catch (final IOException e) {
			LOGGER.error("Error while reading from queue {}", this.queueName, e);
			return false;
		} catch (final InterruptedException e) {
			LOGGER.error("Consumer was interrupted on queue {}", this.queueName, e);
			return false;
		} catch (final ShutdownSignalException e) {
			LOGGER.info("Consumer was shut down while waiting on queue {}", this.queueName, e);
			return true;
		}

		return true;
	}

	/**
	 * Terminates the reader logic by returning from read method.
	 */
	public void terminate() {
		try {
			this.terminated = true;
			this.connection.close();
		} catch (final IOException e) {
			LOGGER.error("IO error while trying to close the connection.", e);
		}
	}

	public void deliverRecord(final IMonitoringRecord monitoringRecord) {
		this.elementReceivedCallback.accept(monitoringRecord);
	}

}
