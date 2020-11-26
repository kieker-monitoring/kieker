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

package kieker.analysis.source.jms;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageFormatException;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.record.IMonitoringRecord;

/**
 * Reads monitoring records from a (remote or local) JMS queue.
 *
 * @author Andre van Hoorn, Matthias Rohr, Lars Bluemke
 *
 * @since 0.95a
 */
public final class JMSReader {

	private static final Logger LOGGER = LoggerFactory.getLogger(JMSReader.class);

	private final String jmsProviderUrl;
	private final String jmsDestination;
	private final String jmsFactoryLookupName;
	private final CountDownLatch cdLatch = new CountDownLatch(1);

	private final Consumer<IMonitoringRecord> elementReceivedCallback;

	/**
	 * Creates a new logic module for the Consumer.
	 *
	 * @param jmsProviderUrl
	 *            The name of the configuration determining the JMS provider URL,
	 *            e.g. {@code tcp://localhost:3035/}
	 * @param jmsDestination
	 *            The name of the configuration determining the JMS destination,
	 *            e.g. {@code queue1}.
	 * @param jmsFactoryLookupName
	 *            The name of the configuration determining the name of the used JMS
	 *            factory, e.g. {@code org.exolab.jms.jndi.InitialContextFactory}.
	 * @param elementReceivedCallback
	 *            The actual teetime stage which uses this class.
	 *
	 * @throws IllegalArgumentException
	 *             If one of the properties is empty.
	 */
	public JMSReader(final String jmsProviderUrl, final String jmsDestination, final String jmsFactoryLookupName,
			final Consumer<IMonitoringRecord> elementReceivedCallback) throws IllegalArgumentException {

		// Initialize the reader bases
		this.jmsProviderUrl = jmsProviderUrl;
		this.jmsDestination = jmsDestination;
		this.jmsFactoryLookupName = jmsFactoryLookupName;
		this.elementReceivedCallback = elementReceivedCallback;

		// simple sanity check
		if ((this.jmsProviderUrl.length() == 0) || (this.jmsDestination.length() == 0)
				|| (this.jmsFactoryLookupName.length() == 0)) {
			final String message = String.format(
					"Invalid or incomplete parameters: jmsProviderUrl ('%s'), jmsDestination ('%s'), or factoryLookupName ('%s') is null",
					this.jmsProviderUrl, this.jmsDestination, this.jmsFactoryLookupName);
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Starts the reader. This method is intended to be a blocking operation, i.e.,
	 * it is assumed that reading has finished before this method returns. The
	 * method should indicate an error by the return value false.
	 *
	 * In asynchronous scenarios, the {@link #terminate()} method can be used
	 * to initiate the termination of this method.
	 *
	 * @return true if reading was successful; false if an error occurred
	 *
	 */
	public boolean read() {
		boolean retVal = true;
		Connection connection = null;
		try {
			final Hashtable<String, String> properties = new Hashtable<>(); // NOPMD NOCS (InitialContext expects
			// Hashtable)
			properties.put(Context.INITIAL_CONTEXT_FACTORY, this.jmsFactoryLookupName);

			// JMS initialization
			properties.put(Context.PROVIDER_URL, this.jmsProviderUrl);
			final Context context = new InitialContext(properties);
			final ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
			connection = factory.createConnection();
			final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Destination destination;
			try {
				// As a first step, try a JNDI lookup (this seems to fail with ActiveMQ
				// sometimes)
				destination = (Destination) context.lookup(this.jmsDestination);
			} catch (final NameNotFoundException exc) {
				// JNDI lookup failed, try manual creation (this seems to fail with
				// ActiveMQ/HornetQ sometimes)
				destination = session.createQueue(this.jmsDestination);
				if (destination == null) { //
					LOGGER.error("Failed to lookup queue '{}' via JNDI: {} AND failed to create queue",
							this.jmsDestination, exc.getMessage());
					throw exc; // will be catched below to abort the read method
				}
			}

			LOGGER.info("Listening to destination: {} at {} !\n***\n\n", destination, this.jmsProviderUrl);
			final MessageConsumer receiver = session.createConsumer(destination);
			receiver.setMessageListener(new JMSMessageListener());

			// start the connection to enable message delivery
			connection.start();

			LOGGER.info("JMSReader started and waits for incoming monitoring events!");
			this.block();
			LOGGER.info("Woke up by shutdown");
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			LOGGER.error("Error in read()", ex);
			retVal = false;
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (final JMSException ex) {
				LOGGER.error("Failed to close JMS", ex);
			}
		}
		return retVal;
	}

	private final void block() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public final void run() {
				JMSReader.this.unblock();
			}
		});

		try {
			this.cdLatch.await();
		} catch (final InterruptedException e) {
			// ignore
		}
	}

	final void unblock() { // NOPMD (package visible for inner class)
		this.cdLatch.countDown();
	}

	final void deliverIndirect(final IMonitoringRecord data) { // NOPMD (package visible for inner class)
		this.elementReceivedCallback.accept(data);
	}

	/**
	 * Terminates the reader logic and returns iff termination was successful.
	 */
	public void terminate() {
		LOGGER.info("Shutdown of JMSReader requested.");
		this.unblock();
	}

	Logger getLogger() { // NOPMD (package visible for inner class)
		return JMSReader.LOGGER;
	}

	/**
	 * The MessageListener will read onMessage each time a message comes in.
	 */
	private final class JMSMessageListener implements MessageListener {

		public JMSMessageListener() {
			// empty default constructor
		}

		@Override
		public void onMessage(final Message jmsMessage) {
			if (jmsMessage == null) {
				JMSReader.this.getLogger().warn("Received null message");
			} else {
				if (jmsMessage instanceof ObjectMessage) {
					try {
						final ObjectMessage om = (ObjectMessage) jmsMessage;
						final Serializable omo = om.getObject();
						if ((omo instanceof IMonitoringRecord)) {
							JMSReader.this.deliverIndirect((IMonitoringRecord) omo);
						}
					} catch (final MessageFormatException ex) {
						JMSReader.this.getLogger().error("Error delivering record", ex);
					} catch (final JMSException ex) {
						JMSReader.this.getLogger().error("Error delivering record", ex);
					} catch (final Exception ex) { // NOPMD NOCS (catch Exception)
						JMSReader.this.getLogger().error("Error delivering record", ex);
					}
				} else {
					JMSReader.this.getLogger().warn("Received message of invalid type: {}",
							jmsMessage.getClass().getName());
				}
			}
		}
	}
}
