/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.analysis.plugin.reader.jms;

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
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;

import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

/**
 * Reads monitoring records from a (remote or local) JMS queue.
 * 
 * 
 * @author Andre van Hoorn, Matthias Rohr
 */
@Plugin(outputPorts = @OutputPort(name = JMSReader.OUTPUT_PORT_NAME_RECORDS, eventTypes = { IMonitoringRecord.class }, description = "Output Port of the JMSReader"))
public final class JMSReader extends AbstractReaderPlugin {

	public static final String OUTPUT_PORT_NAME_RECORDS = "monitoring-records";

	public static final String CONFIG_PROPERTY_NAME_PROVIDERURL = "jmsProviderUrl";
	public static final String CONFIG_PROPERTY_NAME_DESTINATION = "jmsDestination";
	public static final String CONFIG_PROPERTY_NAME_FACTORYLOOKUP = "jmsFactoryLookupName";

	private static final Log LOG = LogFactory.getLog(JMSReader.class);

	private final String jmsProviderUrl;
	private final String jmsDestination;
	private final String jmsFactoryLookupName;
	private final CountDownLatch cdLatch = new CountDownLatch(1);

	/**
	 * Creates a new instance of this class using the given parameters to
	 * configure the reader.
	 * 
	 * @param configuration
	 *            The configuration used to initialize the whole reader. Keep in mind that the configuration should contain the following properties:
	 *            <ul>
	 *            <li>The property {@link CONFIG_PROVIDERURL}, e.g. {@code tcp://localhost:3035/}
	 *            <li>The property {@link CONFIG_DESTINATION}, e.g. {@code queue1}
	 *            <li>The property {@link CONFIG_FACTORYLOOKUP}, e.g. {@code org.exolab.jms.jndi.InitialContextFactory}
	 *            </ul>
	 * 
	 * @throws IllegalArgumentException
	 *             If one of the properties is empty.
	 */
	public JMSReader(final Configuration configuration) throws IllegalArgumentException {
		/* Call the inherited constructor. */
		super(configuration);
		/* Initialize the reader bases on the given configuration. */
		this.jmsProviderUrl = configuration.getStringProperty(JMSReader.CONFIG_PROPERTY_NAME_PROVIDERURL);
		this.jmsDestination = configuration.getStringProperty(JMSReader.CONFIG_PROPERTY_NAME_DESTINATION);
		this.jmsFactoryLookupName = configuration.getStringProperty(JMSReader.CONFIG_PROPERTY_NAME_FACTORYLOOKUP);
		// simple sanity check
		if ((this.jmsProviderUrl.length() == 0) || (this.jmsDestination.length() == 0) || (this.jmsFactoryLookupName.length() == 0)) {
			throw new IllegalArgumentException("JMSReader has not sufficient parameters. jmsProviderUrl ('" + this.jmsProviderUrl + "'), jmsDestination ('"
					+ this.jmsDestination + "'), or factoryLookupName ('" + this.jmsFactoryLookupName + "') is null");
		}
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration defaultConfiguration = new Configuration();
		// TODO: provide default values!
		defaultConfiguration.setProperty(JMSReader.CONFIG_PROPERTY_NAME_PROVIDERURL, "");
		defaultConfiguration.setProperty(JMSReader.CONFIG_PROPERTY_NAME_DESTINATION, "");
		defaultConfiguration.setProperty(JMSReader.CONFIG_PROPERTY_NAME_FACTORYLOOKUP, "");
		return defaultConfiguration;
	}

	/**
	 * A call to this method is a blocking call.
	 */

	public boolean read() {
		boolean retVal = false;
		Connection connection = null;
		try {
			final Hashtable<String, String> properties = new Hashtable<String, String>(); // NOPMD // NOCS (InitialContext expects Hashtable)
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
				// JNDI lookup failed, try manual creation (this seems to fail with ActiveMQ sometimes)
				JMSReader.LOG.warn("Failed to lookup queue '" + this.jmsDestination + "' via JNDI", exc);
				JMSReader.LOG.info("Attempting to create queue ...");
				destination = session.createQueue(this.jmsDestination);
			}

			JMSReader.LOG.info("Listening to destination:" + destination + " at " + this.jmsProviderUrl + " !\n***\n\n");
			final MessageConsumer receiver = session.createConsumer(destination);
			receiver.setMessageListener(new MessageListener() {
				// the MessageListener will read onMessage each time a message comes in

				public void onMessage(final Message jmsMessage) {
					if (jmsMessage instanceof TextMessage) {
						final TextMessage text = (TextMessage) jmsMessage;
						JMSReader.LOG.info("Received text message: " + text);

					} else {
						try {
							final ObjectMessage om = (ObjectMessage) jmsMessage;
							final Serializable omo = om.getObject();
							if ((omo instanceof IMonitoringRecord) && (!JMSReader.super.deliver(JMSReader.OUTPUT_PORT_NAME_RECORDS, omo))) {
								JMSReader.LOG.error("deliverRecord returned false");
							}
						} catch (final MessageFormatException ex) {
							JMSReader.LOG.error("Error delivering record", ex);
						} catch (final JMSException ex) {
							JMSReader.LOG.error("Error delivering record", ex);
						} catch (final Exception ex) { // NOCS // NOPMD
							JMSReader.LOG.error("Error delivering record", ex);
						}
					}
				}
			});

			// start the connection to enable message delivery
			connection.start();

			JMSReader.LOG.info("JMSReader started and waits for incomming monitoring events!");
			this.block();
			JMSReader.LOG.info("Woke up by shutdown");
		} catch (final Exception ex) { // FindBugs complains but wontfix // NOCS (IllegalCatchCheck) // NOPMD
			JMSReader.LOG.error("Error in read()", ex);
			retVal = false;
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (final JMSException ex) {
				JMSReader.LOG.error("Failed to close JMS", ex);
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
		} catch (final InterruptedException e) { // ignore
		}
	}

	private final void unblock() {
		this.cdLatch.countDown();
	}

	public void terminate(final boolean error) {
		JMSReader.LOG.info("Shutdown of JMSReader requested.");
		this.unblock();
	}

	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(JMSReader.CONFIG_PROPERTY_NAME_PROVIDERURL, this.jmsProviderUrl);
		configuration.setProperty(JMSReader.CONFIG_PROPERTY_NAME_DESTINATION, this.jmsDestination);
		configuration.setProperty(JMSReader.CONFIG_PROPERTY_NAME_FACTORYLOOKUP, this.jmsFactoryLookupName);

		return configuration;
	}
}
