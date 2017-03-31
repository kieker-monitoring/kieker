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

package kieker.analysisteetime.plugin.reader.jms;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.concurrent.CountDownLatch;

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

import kieker.common.logging.Log;
import kieker.common.record.IMonitoringRecord;

/**
 * Reads monitoring records from a (remote or local) JMS queue.
 *
 * @author Andre van Hoorn, Matthias Rohr, Lars Bluemke
 *
 * @since 0.95a
 */
public final class JMSReaderLogic {

	private final String jmsProviderUrl;
	private final String jmsDestination;
	private final String jmsFactoryLookupName;
	private final CountDownLatch cdLatch = new CountDownLatch(1);

	private final Log log;
	private final JMSReader jmsReaderStage;

	/**
	 * Creates a new logic module for the {@link JMSReader}.
	 *
	 * @param jmsProviderUrl
	 *            The name of the configuration determining the JMS provider URL,
	 *            e.g. {@code tcp://localhost:3035/}
	 * @param jmsDestination
	 *            The name of the configuration determining the JMS destination,
	 *            e.g. {@code queue1}.
	 * @param jmsFactoryLookupName
	 *            The name of the configuration determining the name of the used JMS factory,
	 *            e.g. {@code org.exolab.jms.jndi.InitialContextFactory}.
	 * @param log
	 *            Kieker log.
	 * @param jmsReaderStage
	 *            The actual teetime stage which uses this class.
	 *
	 * @throws IllegalArgumentException
	 *             If one of the properties is empty.
	 */
	public JMSReaderLogic(final String jmsProviderUrl, final String jmsDestination, final String jmsFactoryLookupName, final Log log,
			final JMSReader jmsReaderStage)
			throws IllegalArgumentException {

		// Initialize the reader bases
		this.jmsProviderUrl = jmsProviderUrl;
		this.jmsDestination = jmsDestination;
		this.jmsFactoryLookupName = jmsFactoryLookupName;
		this.log = log;
		this.jmsReaderStage = jmsReaderStage;

		// simple sanity check
		if ((this.jmsProviderUrl.length() == 0) || (this.jmsDestination.length() == 0) || (this.jmsFactoryLookupName.length() == 0)) {
			throw new IllegalArgumentException("JMSReader has not sufficient parameters. jmsProviderUrl ('" + this.jmsProviderUrl + "'), jmsDestination ('"
					+ this.jmsDestination + "'), or factoryLookupName ('" + this.jmsFactoryLookupName + "') is null");
		}
	}

	/**
	 * Starts the reader. This method is intended to be a blocking operation,
	 * i.e., it is assumed that reading has finished before this method returns.
	 * The method should indicate an error by the return value false.
	 *
	 * In asynchronous scenarios, the {@link #terminate(boolean)} method can be used
	 * to initiate the termination of this method.
	 *
	 * @return true if reading was successful; false if an error occurred
	 *
	 */
	public boolean read() {
		boolean retVal = true;
		Connection connection = null;
		try {
			final Hashtable<String, String> properties = new Hashtable<String, String>(); // NOPMD NOCS (InitialContext expects Hashtable)
			properties.put(Context.INITIAL_CONTEXT_FACTORY, this.jmsFactoryLookupName);

			// JMS initialization
			properties.put(Context.PROVIDER_URL, this.jmsProviderUrl);
			final Context context = new InitialContext(properties);
			final ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
			connection = factory.createConnection();
			final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Destination destination;
			try {
				// As a first step, try a JNDI lookup (this seems to fail with ActiveMQ sometimes)
				destination = (Destination) context.lookup(this.jmsDestination);
			} catch (final NameNotFoundException exc) {
				// JNDI lookup failed, try manual creation (this seems to fail with ActiveMQ/HornetQ sometimes)
				destination = session.createQueue(this.jmsDestination);
				if (destination == null) { //
					this.log.error("Failed to lookup queue '" + this.jmsDestination + "' via JNDI: " + exc.getMessage() + " AND failed to create queue");
					throw exc; // will be catched below to abort the read method
				}
			}

			this.log.info("Listening to destination:" + destination + " at " + this.jmsProviderUrl + " !\n***\n\n");
			final MessageConsumer receiver = session.createConsumer(destination);
			receiver.setMessageListener(new JMSMessageListener());

			// start the connection to enable message delivery
			connection.start();

			this.log.info("JMSReader started and waits for incoming monitoring events!");
			this.block();
			this.log.info("Woke up by shutdown");
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			this.log.error("Error in read()", ex);
			retVal = false;
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (final JMSException ex) {
				this.log.error("Failed to close JMS", ex);
			}
		}
		return retVal;
	}

	private final void block() {
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public final void run() {
				JMSReaderLogic.this.unblock();
			}
		});
		try {
			this.cdLatch.await();
		} catch (final InterruptedException e) { // ignore
		}
	}

	final void unblock() { // NOPMD (package visible for inner class)
		this.cdLatch.countDown();
	}

	final void deliverIndirect(final IMonitoringRecord data) { // NOPMD (package visible for inner class)
		this.jmsReaderStage.deliverRecord(data);
	}

	/**
	 * Terminates the reader logic by returning from read method.
	 */
	public void terminate() {
		this.log.info("Shutdown of JMSReader requested.");
		this.unblock();
	}

	protected Log getLog() {
		return this.log;
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
				JMSReaderLogic.this.getLog().warn("Received null message");
			} else {
				if (jmsMessage instanceof ObjectMessage) {
					try {
						final ObjectMessage om = (ObjectMessage) jmsMessage;
						final Serializable omo = om.getObject();
						if ((omo instanceof IMonitoringRecord)) {
							JMSReaderLogic.this.deliverIndirect((IMonitoringRecord) omo);
						}
					} catch (final MessageFormatException ex) {
						JMSReaderLogic.this.getLog().error("Error delivering record", ex);
					} catch (final JMSException ex) {
						JMSReaderLogic.this.getLog().error("Error delivering record", ex);
					} catch (final Exception ex) { // NOPMD NOCS (catch Exception)
						JMSReaderLogic.this.getLog().error("Error delivering record", ex);
					}
				} else {
					JMSReaderLogic.this.getLog().warn("Received message of invalid type: " + jmsMessage.getClass().getName());
				}
			}
		}
	}
}
