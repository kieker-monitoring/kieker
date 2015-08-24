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

package kieker.analysis.plugin.reader.amqp;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.ILookup;
import kieker.common.util.registry.Lookup;

/**
 * Reader plugin that reads monitoring records from an AMQP queue.
 *
 * @author Holger Knoche
 *
 * @since 1.12
 */
@Plugin(description = "A plugin that reads monitoring records from an AMQP queue", outputPorts = {
	@OutputPort(name = AMQPReader.OUTPUT_PORT_NAME_RECORDS, eventTypes = {
		IMonitoringRecord.class }, description = "Output port of the AMQP reader") }, configuration = {
			@Property(name = AMQPReader.CONFIG_PROPERTY_URI, defaultValue = "", description = "Server URI of the AMQP server"),
			@Property(name = AMQPReader.CONFIG_PROPERTY_QUEUENAME, defaultValue = "", description = "AMQP queue name"),
			@Property(name = AMQPReader.CONFIG_PROPERTY_HEARTBEAT, defaultValue = "60", description = "Heartbeat interval")
		})
public final class AMQPReader extends AbstractReaderPlugin {

	/** The name of the output port delivering the received records. */
	public static final String OUTPUT_PORT_NAME_RECORDS = "monitoringRecords";

	/** The name of the configuration property for the server URI. */
	public static final String CONFIG_PROPERTY_URI = "uri";
	/** The name of the configuration property for the AMQP queue name. */
	public static final String CONFIG_PROPERTY_QUEUENAME = "queueName";
	/** The name of the configuration property for the heartbeat timeout. */
	public static final String CONFIG_PROPERTY_HEARTBEAT = "heartbeat";

	/** ID for registry records. */
	private static final byte REGISTRY_RECORD_ID = (byte) 0xFF;

	/** ID for regular records. */
	private static final byte REGULAR_RECORD_ID = (byte) 0x01;

	private final String uri;
	private final String queueName;
	private final int heartbeat;

	private final Connection connection;
	private final Channel channel;
	private final QueueingConsumer consumer;

	private final ILookup<String> stringRegistry = new Lookup<String>();

	private final Thread registryRecordHandlerThread;
	private final RegistryRecordHandler registryRecordHandler;
	private final RegularRecordHandler regularRecordHandler;

	private final Thread regularRecordHandlerThread;

	private boolean terminated;
	private boolean threadsStarted;

	/**
	 * Creates a new AMQP reader with the given configuration in the given context.
	 *
	 * @param configuration
	 *            The configuration for this reader
	 * @param projectContext
	 *            The project context for this component
	 * @throws KeyManagementException
	 *             If an invalid URI is specified
	 * @throws NoSuchAlgorithmException
	 *             If an invalid URI is specified
	 * @throws IOException
	 *             If an IO error occurs during connection or channel creation
	 * @throws TimeoutException
	 *             If a timeout occurs during connection or channel creation
	 * @throws URISyntaxException
	 *             If an invalid URI is specified
	 */
	public AMQPReader(final Configuration configuration, final IProjectContext projectContext)
			throws KeyManagementException, NoSuchAlgorithmException, IOException, TimeoutException, URISyntaxException {
		super(configuration, projectContext);

		this.uri = this.configuration.getStringProperty(CONFIG_PROPERTY_URI);
		this.queueName = this.configuration.getStringProperty(CONFIG_PROPERTY_QUEUENAME);
		this.heartbeat = this.configuration.getIntProperty(CONFIG_PROPERTY_HEARTBEAT);

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
	}

	private Connection createConnection() throws IOException, TimeoutException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException {
		final ConnectionFactory connectionFactory = new ConnectionFactory();

		connectionFactory.setUri(this.uri);
		connectionFactory.setRequestedHeartbeat(this.heartbeat);

		return connectionFactory.newConnection();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_URI, this.uri);
		configuration.setProperty(CONFIG_PROPERTY_QUEUENAME, this.queueName);
		configuration.setProperty(CONFIG_PROPERTY_HEARTBEAT, Integer.toString(this.heartbeat));

		return configuration;
	}

	@Override
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
					this.log.error(String.format("Unknown record type: %02x", recordType));
					break;
				}
			}
		} catch (final IOException e) {
			this.log.error("Error while reading from queue " + this.queueName, e);
			return false;
		} catch (final InterruptedException e) {
			this.log.error("Consumer was interrupted on queue " + this.queueName, e);
			return false;
		}

		return true;
	}

	@Override
	public void terminate(final boolean error) {
		try {
			this.terminated = true;
			this.connection.close();
		} catch (final IOException e) {
			this.log.error("IO error while trying to close the connection.", e);
		}
	}

	protected void deliverRecord(final IMonitoringRecord monitoringRecord) {
		this.deliver(OUTPUT_PORT_NAME_RECORDS, monitoringRecord);
	}

}
