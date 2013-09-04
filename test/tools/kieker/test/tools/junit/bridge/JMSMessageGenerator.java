/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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
package kieker.test.tools.junit.bridge;

import java.net.URI;
import java.net.URISyntaxException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageNotWriteableException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Assert;

import kieker.tools.bridge.connector.jms.JMSClientConnector;

/**
 * @author Reiner Jung
 * 
 * @since 1.8
 */
public class JMSMessageGenerator implements Runnable {

	private Connection connection;
	private MessageProducer producer;

	public void run() {
		this.initialize();
		this.sendRecords();
		this.close();
	}

	private void initialize() {
		try {
			// setup connection
			final ConnectionFactory factory = new ActiveMQConnectionFactory(ConfigurationParameters.JMS_USERNAME,
					ConfigurationParameters.JMS_PASSWORD, new URI(ConfigurationParameters.JMS_URI));

			this.connection = factory.createConnection();

			final Session session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			final Destination destination = session.createQueue(JMSClientConnector.KIEKER_DATA_BRIDGE_READ_QUEUE);
			this.producer = session.createProducer(destination);

			this.connection.start();
		} catch (final JMSException e) {
			Assert.fail(e.getMessage());
		} catch (final URISyntaxException e) {
			Assert.fail(e.getMessage());
		}
	}

	private void sendRecords() {
		final String messageText = ConfigurationParameters.TEST_RECORD_ID + ";" +
				ConfigurationParameters.TEST_OPERATION_SIGNATURE + ";" +
				ConfigurationParameters.TEST_SESSION_ID + ";" +
				ConfigurationParameters.TEST_TRACE_ID + ";" +
				ConfigurationParameters.TEST_TIN + ";" +
				ConfigurationParameters.TEST_TOUT + ";" +
				ConfigurationParameters.TEST_HOSTNAME + ";" +
				ConfigurationParameters.TEST_EOI + ";" +
				ConfigurationParameters.TEST_ESS;

		for (int i = 0; i < ConfigurationParameters.SEND_NUMBER_OF_RECORDS; i++) {
			try {
				final ActiveMQTextMessage message = new ActiveMQTextMessage();
				message.setText(messageText);
				this.producer.send(message);
			} catch (final MessageNotWriteableException e) {
				Assert.fail(e.getMessage());
			} catch (final JMSException e) {
				Assert.fail(e.getMessage());
			}

		}
	}

	private void close() {
		try {
			this.connection.stop();
		} catch (final JMSException e) {
			Assert.fail(e.getMessage());
		}
	}

}
