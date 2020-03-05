/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageNotWriteableException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.tools.bridge.connector.jms.JMSClientConnector;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * Implements a JMS client sending Kieker records to a JMS queue.
 * This class is used by both JMS tests as message source. It mis-uses
 * the EOI property to deliver the record number.
 *
 * @author Reiner Jung
 *
 * @since 1.8
 */
public class JMSMessageGenerator implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractKiekerTest.class);

	private Connection connection;
	private MessageProducer producer;
	private final String jmsUri;
	private Session session;

	private final String jmsContextFactory;

	/**
	 * Empty constructor.
	 *
	 * @param uri
	 *            jms uri of the broker
	 * @param jmsContextFactory
	 *            full qualified class name of the context factory
	 */
	public JMSMessageGenerator(final String uri, final String jmsContextFactory) {
		LOGGER.info("Destination {}", uri);
		this.jmsUri = uri;
		this.jmsContextFactory = jmsContextFactory;
	}

	/**
	 * General run method for the message generator.
	 */
	@Override
	public void run() {
		this.initialize();
		this.sendRecords();
		this.close();
	}

	private void initialize() {
		try {
			LOGGER.info("Initialize message generator");

			// setup connection
			final Hashtable<String, String> properties = new Hashtable<>(); // NOPMD NOCS (IllegalTypeCheck, InitialContext requires Hashtable)
			properties.put(Context.INITIAL_CONTEXT_FACTORY, this.jmsContextFactory);
			properties.put(Context.PROVIDER_URL, this.jmsUri);
			properties.put(Context.SECURITY_PRINCIPAL, ConfigurationParameters.JMS_USERNAME);
			properties.put(Context.SECURITY_CREDENTIALS, ConfigurationParameters.JMS_PASSWORD);

			final Context context = new InitialContext(properties);
			final ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");

			this.connection = factory.createConnection();

			this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			final Destination destination = this.session.createQueue(JMSClientConnector.KIEKER_DATA_BRIDGE_READ_QUEUE);
			this.producer = this.session.createProducer(destination);

			LOGGER.info("Start connection");
			this.connection.start();
		} catch (final JMSException e) {
			Assert.fail(e.getMessage());
			LOGGER.warn(e.getMessage());
		} catch (final NamingException e) {
			Assert.fail(e.getMessage());
			LOGGER.warn("No connection factory found. {}", e.getMessage());
		}
	}

	private void sendRecords() {
		for (int i = 0; i < ConfigurationParameters.SEND_NUMBER_OF_RECORDS; i++) {
			LOGGER.debug("Send record {}", i);
			try {
				final TextMessage message = this.session.createTextMessage(ConfigurationParameters.TEST_RECORD_ID
						+ ";" + ConfigurationParameters.TEST_OPERATION_SIGNATURE
						+ ";" + ConfigurationParameters.TEST_SESSION_ID
						+ ";" + ConfigurationParameters.TEST_TRACE_ID
						+ ";" + ConfigurationParameters.TEST_TIN
						+ ";" + ConfigurationParameters.TEST_TOUT
						+ ";" + ConfigurationParameters.TEST_HOSTNAME
						+ ";" + i
						+ ";" + ConfigurationParameters.TEST_ESS);
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
			LOGGER.info("Stop connection");
			this.connection.stop();
		} catch (final JMSException e) {
			Assert.fail(e.getMessage());
		}
	}

}
