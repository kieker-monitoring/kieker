package kieker.analysis.plugin.reader.amqp;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import kieker.analysis.plugin.reader.newio.IRawDataProcessor;
import kieker.analysis.plugin.reader.newio.IRawDataReader;
import kieker.analysis.plugin.reader.newio.Outcome;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * AMQP reader plugin that supports chunking using the new raw data I/O infrastructure.
 * 
 * @author Holger Knoche
 *
 * @since 1.13
 */
public class ChunkingAMQPReader implements IRawDataReader {
	
	/** The name of the configuration property for the server URI. */
	public static final String CONFIG_PROPERTY_URI = "uri";
	/** The name of the configuration property for the AMQP queue name. */
	public static final String CONFIG_PROPERTY_QUEUENAME = "queueName";
	/** The name of the configuration property for the heartbeat timeout. */
	public static final String CONFIG_PROPERTY_HEARTBEAT = "heartbeat";
	
	private static final Log LOG = LogFactory.getLog(ChunkingAMQPReader.class);
	
	private final String uri;
	private final String queueName;
	private final int heartbeat;

	private final IRawDataProcessor processor;
	
	private volatile Connection connection;
	private volatile Channel channel;
	private volatile QueueingConsumer consumer;
	
	private volatile boolean terminated;
	
	public ChunkingAMQPReader(final Configuration configuration, final IRawDataProcessor processor) {
		this.uri = configuration.getStringProperty(CONFIG_PROPERTY_URI);
		this.queueName = configuration.getStringProperty(CONFIG_PROPERTY_QUEUENAME);
		this.heartbeat = configuration.getIntProperty(CONFIG_PROPERTY_HEARTBEAT);
		
		this.processor = processor;
	}

	@Override
	public Outcome onInitialization() {
		try {
			// Prepare the connection, channel and consumer
			this.connection = this.createConnection();
			this.channel = this.connection.createChannel();
			this.consumer = new QueueingConsumer(this.channel);
			
			return Outcome.SUCCESS;
		} catch(final KeyManagementException | NoSuchAlgorithmException | TimeoutException | URISyntaxException | IOException e) {
			this.handleInitializationError(e);
			
			return Outcome.FAILURE;
		}
	}

	private void handleInitializationError(final Throwable e) {
		LOG.error("An error occurred initializing the AMQP reader.", e);
	}
	
	private Connection createConnection() throws IOException, TimeoutException, KeyManagementException, NoSuchAlgorithmException, URISyntaxException {
		final ConnectionFactory connectionFactory = new ConnectionFactory();

		connectionFactory.setUri(this.uri);
		connectionFactory.setRequestedHeartbeat(this.heartbeat);

		return connectionFactory.newConnection();
	}
	
	@Override
	public Outcome read() {
		final IRawDataProcessor rawDataProcessor = this.processor;		
		
		try {
			this.channel.basicConsume(this.queueName, true, this.consumer);

			while (!this.terminated) {
				// Read the next message and dispatch the received data to the processor
				final QueueingConsumer.Delivery delivery = this.consumer.nextDelivery();
				final byte[] body = delivery.getBody();

				rawDataProcessor.decodeAndDeliverRecords(body);
			}
		} catch (final IOException e) {
			LOG.error("Error while reading from queue " + this.queueName, e);
			
			return Outcome.FAILURE;
		} catch (final InterruptedException e) {
			LOG.error("Consumer was interrupted on queue " + this.queueName, e);
			
			return Outcome.FAILURE;
		}
		
		return Outcome.SUCCESS;
	}

	@Override
	public Outcome onTermination() {
		try {
			this.terminated = true;
			this.connection.close();
			
			return Outcome.SUCCESS;
		} catch (final IOException e) {
			LOG.error("IO error while trying to close the connection.", e);
			
			return Outcome.FAILURE;
		}
		
	}
	
}
