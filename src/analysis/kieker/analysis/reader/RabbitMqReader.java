/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.analysis.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import kieker.common.record.OperationExecutionRecord;
import kieker.common.util.PropertyMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConnectionParameters;
import com.rabbitmq.client.QueueingConsumer;

/**
 * 
 * This reader is an alternative to the JMS reader. RabbitMQ showed RabbitMQ
 * high performance in experiments.
 * 
 * @author matthias
 */
public class RabbitMqReader extends AbstractMonitoringLogReader {

	private String rmqHostName = "localhost";
	private String rmqVirtualHost = "kiekertest";
	private String rmqExchangeName = "myExchange";
	private String rmqQueueName = "myQueue";
	private String rmqUserName = "matthias";
	private String rmqRoutingKey = "routingKey";
	private String rmqPassword = "testmatthias";
	private int rmqPortNumber = 5672; // default 5672

	private static final Log log = LogFactory.getLog(JMSReader.class);

	/**
	 * 
	 * @param rmqHostName
	 * @param rmqVirtualHost
	 * @param rmqExchangeName
	 * @param rmqQueueName
	 * @param rmqUserName
	 * @param rmqRoutingKey
	 * @param rmqPassword
	 * @param rmqPortNumber
	 */
	public RabbitMqReader(final String rmqHostName,
			final String rmqVirtualHost, final String rmqExchangeName,
			final String rmqQueueName, final String rmqUserName,
			final String rmqRoutingKey, final String rmqPassword,
			final int rmqPortNumber) {
		this.initInstanceFromArgs(rmqHostName, rmqVirtualHost, rmqExchangeName,
				rmqQueueName, rmqUserName, rmqRoutingKey, rmqPassword,
				rmqPortNumber); // throws IllegalArgumentException
	}

	/**
	 * Constructor for RabbitMqReader. Requires a subsequent call to the init
	 * method in order to specify the input directory using the parameters
	 * 
	 * @a jmsServerLocation and @a jmsDestination.
	 */
	public RabbitMqReader() {
	}

	/**
	 * Valid key/value pair: jmsProviderUrl=tcp://localhost:3035/ |
	 * jmsDestination=queue1
	 */

	@Override
	public boolean init(final String initString) {
		try {
			final PropertyMap propertyMap = new PropertyMap(initString, "|",
					"="); // throws IllegalArgumentException

			this.rmqHostName = propertyMap.getProperty("rmqHostName", null);
			this.rmqVirtualHost = propertyMap.getProperty("rmqVirtualHost",
					null);
			this.rmqExchangeName = propertyMap.getProperty("rmqExchangeName",
					null);
			this.rmqQueueName = propertyMap.getProperty("rmqQueueName", null);
			this.rmqUserName = propertyMap.getProperty("rmqUserName", null);
			this.rmqRoutingKey = propertyMap.getProperty("rmqRoutingKey", null);
			this.rmqPassword = propertyMap.getProperty("rmqPassword", null);
			this.rmqPortNumber = Integer.parseInt(propertyMap.getProperty(
					"rmqPortNumber", null));
			this.initInstanceFromArgs(this.rmqHostName, this.rmqVirtualHost,
					this.rmqExchangeName, this.rmqQueueName, this.rmqUserName,
					this.rmqRoutingKey, this.rmqPassword, this.rmqPortNumber);
		} catch (final Exception exc) {
			RabbitMqReader.log.error("Failed to parse initString '"
					+ initString + "': " + exc.getMessage());
			return false;
		}
		return true;
	}

	private void initInstanceFromArgs(final String rmqHostName,
			final String rmqVirtualHost, final String rmqExchangeName,
			final String rmqQueueName, final String rmqUserName,
			final String rmqRoutingKey, final String rmqPassword,
			final int rmqPortNumber) {
		this.initInstanceFromArgs(rmqHostName, rmqVirtualHost, rmqExchangeName,
				rmqQueueName, rmqUserName, rmqRoutingKey, rmqPassword,
				rmqPortNumber); // throws IllegalArgumentException
	}

	@Override
	/**
	 * A call to this method is a blocking call.
	 */
	public boolean read() {
		final boolean retVal = false;

		System.out
				.printf("Starting connection to the Rabbit MQ message broker at %s : %d. (User %s, Pass %s)\n",
						this.rmqHostName, this.rmqPortNumber, this.rmqUserName,
						this.rmqPassword);

		Channel channel = null;
		try {
			final ConnectionParameters params = new ConnectionParameters();
			params.setUsername(this.rmqUserName);
			params.setPassword(this.rmqPassword);
			params.setVirtualHost(this.rmqVirtualHost);
			params.setRequestedHeartbeat(0);
			final ConnectionFactory factory = new ConnectionFactory(params);
			final Connection conn = factory.newConnection(this.rmqHostName,
					this.rmqPortNumber);
			System.out
					.printf("Successfully connect to the rabbit MQ message broker at %s : %d. \n",
							this.rmqHostName, this.rmqPortNumber);
			channel = conn.createChannel();
		} catch (final IOException ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null,
					ex);
			System.out
					.println("Error: Failed to connect to the rabbit MQ message broker.");
			System.out.println(ex.getMessage());
		}

		int numberOfMessagesReceived = 0;
		// Retrieving messages by subscription
		final boolean noAck = false;
		final QueueingConsumer consumer = new QueueingConsumer(channel);
		try {
			channel.basicConsume(this.rmqQueueName, noAck, consumer);

			System.out.printf("Listening for messages at queue %s\n",
					this.rmqQueueName);
			while (true) {
				QueueingConsumer.Delivery delivery;
				numberOfMessagesReceived++;
				try {
					delivery = consumer.nextDelivery();
				} catch (final InterruptedException ie) {
					continue;
				}
				// (process the message components ...)
				final String execRecordString = new String(delivery.getBody());

				try {
					// AbstractMonitoringRecord abs =
					// AbstractMonitoringRecord.getInstance();
					// abs.initFromStringArray(toStringArray(execRecordString));
					final int indexOfFirstTokenEnd = execRecordString
							.indexOf(";");
					final String execRecStringWithoutFirstToken = execRecordString
							.substring(indexOfFirstTokenEnd + 1,
									execRecordString.length());

					final OperationExecutionRecord ker = new OperationExecutionRecord();
					if (true) {
						throw new RuntimeException("tokenize string");
					}
					// OLD: ker.initFromString(execRecStringWithoutFirstToken);
					// System.out.println(execRecStringWithoutFirstToken);
					// System.out.printf("Recieved (%d) and decoded KiekerExecutionRecord: %s \n",
					// numberOfMessagesReceived, ker);
					this.deliverRecord(ker);
				} catch (final Exception e) {
					System.out
							.printf("Recieved (%d) but could not Decode as KiekerExecutionRecord: %s \n %s",
									numberOfMessagesReceived, execRecordString,
									e.getMessage());
					// System.out.printf("Skipping Message (%d) (Not proper KiekerExecutionRecord) \n");
				}
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			}

		} catch (final IOException ex) {
			Logger.getLogger(RabbitMqReader.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return retVal;
	}

	private final String[] toStringArray(final String stringSerialization) {
		final StringTokenizer stk = new StringTokenizer(stringSerialization,
				";");
		final ArrayList<String> als = new ArrayList<String>();
		while (stk.hasMoreElements()) {
			als.add(stk.nextToken());
		}
		return als.toArray(new String[als.size()]);

	}

}
