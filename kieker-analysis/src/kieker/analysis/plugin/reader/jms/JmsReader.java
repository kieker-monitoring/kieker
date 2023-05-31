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
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;

import org.slf4j.Logger;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

/**
 * Reads monitoring records from a (remote or local) JMS queue.
 *
 *
 * @author Andre van Hoorn, Matthias Rohr
 *
 * @since 0.95a
 * @deprecated 1.15 ported to teetime
 */
@Plugin(description = "A reader which reads records from a (remove or local) JMS queue",
		dependencies = "This plugin needs the file 'javax.jms-*.jar'.", outputPorts = {
			@OutputPort(name = JmsReader.OUTPUT_PORT_NAME_RECORDS, eventTypes = IMonitoringRecord.class,
					description = "Output Port of the JmsReader")
		},
		configuration = {
			@Property(name = JmsReader.CONFIG_PROPERTY_NAME_PROVIDERURL, defaultValue = "tcp://127.0.0.1:61616/"),
			@Property(name = JmsReader.CONFIG_PROPERTY_NAME_DESTINATION, defaultValue = "queue1"),
			@Property(name = JmsReader.CONFIG_PROPERTY_NAME_FACTORYLOOKUP, defaultValue = "org.apache.activemq.jndi.ActiveMQInitialContextFactory")
		})
@Deprecated
public final class JmsReader extends AbstractReaderPlugin {

	/** The name of the output port delivering the received records. */
	public static final String OUTPUT_PORT_NAME_RECORDS = "monitoringRecords";

	/** The name of the configuration determining the JMS provider URL. */
	public static final String CONFIG_PROPERTY_NAME_PROVIDERURL = "jmsProviderUrl";
	/** The name of the configuration determining the JMS destination (e.g. queue1). */
	public static final String CONFIG_PROPERTY_NAME_DESTINATION = "jmsDestination";
	/** The name of the configuration determining the name of the used JMS factory. */
	public static final String CONFIG_PROPERTY_NAME_FACTORYLOOKUP = "jmsFactoryLookupName";

	private final String jmsProviderUrl;
	private final String jmsDestination;
	private final String jmsFactoryLookupName;
	private final CountDownLatch cdLatch = new CountDownLatch(1);

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration used to initialize the whole reader. Keep in mind that the configuration should contain the following properties:
	 *            <ul>
	 *            <li>The property {@link #CONFIG_PROPERTY_NAME_PROVIDERURL}, e.g. {@code tcp://localhost:3035/}
	 *            <li>The property {@link #CONFIG_PROPERTY_NAME_DESTINATION}, e.g. {@code queue1}
	 *            <li>The property {@link #CONFIG_PROPERTY_NAME_FACTORYLOOKUP}, e.g. {@code org.exolab.jms.jndi.InitialContextFactory}
	 *            </ul>
	 * @param projectContext
	 *            The project context for this component.
	 *
	 * @throws IllegalArgumentException
	 *             If one of the properties is empty.
	 */
	public JmsReader(final Configuration configuration, final IProjectContext projectContext) throws IllegalArgumentException {
		super(configuration, projectContext);

		// Initialize the reader bases on the given configuration.
		this.jmsProviderUrl = configuration.getStringProperty(CONFIG_PROPERTY_NAME_PROVIDERURL);
		this.jmsDestination = configuration.getStringProperty(CONFIG_PROPERTY_NAME_DESTINATION);
		this.jmsFactoryLookupName = configuration.getStringProperty(CONFIG_PROPERTY_NAME_FACTORYLOOKUP);
		// simple sanity check
		if ((this.jmsProviderUrl.length() == 0) || (this.jmsDestination.length() == 0) || (this.jmsFactoryLookupName.length() == 0)) {
			throw new IllegalArgumentException("JmsReader has not sufficient parameters. jmsProviderUrl ('" + this.jmsProviderUrl + "'), jmsDestination ('"
					+ this.jmsDestination + "'), or factoryLookupName ('" + this.jmsFactoryLookupName + "') is null");
		}
	}

	/**
	 * A call to this method is a blocking call.
	 *
	 * @return true if the method succeeds, false otherwise.
	 */
	@Override
	public boolean read() {
		boolean retVal = true;
		Connection connection = null;
		try {
			final Hashtable<String, String> properties = new Hashtable<>(); // NOPMD NOCS (InitialContext expects Hashtable)
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
					this.logger.error("Failed to lookup queue '{}' via JNDI: {} AND failed to create queue", this.jmsDestination, exc.getMessage());
					throw exc; // will be catched below to abort the read method
				}
			}

			this.logger.info("Listening to destination: {} at {} !\n***\n\n", destination, this.jmsProviderUrl);
			final MessageConsumer receiver = session.createConsumer(destination);
			receiver.setMessageListener(new JMSMessageListener());

			// start the connection to enable message delivery
			connection.start();

			this.logger.info("JmsReader started and waits for incoming monitoring events!");
			this.block();
			this.logger.info("Woke up by shutdown");
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			this.logger.error("Error in read()", ex);
			retVal = false;
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (final JMSException ex) {
				this.logger.error("Failed to close JMS", ex);
			}
		}
		return retVal;
	}

	private final void block() {
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public final void run() {
				JmsReader.this.unblock();
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

	final boolean deliverIndirect(final String outputPortName, final Object data) { // NOPMD (package visible for inner class)
		return super.deliver(outputPortName, data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void terminate(final boolean error) {
		this.logger.info("Shutdown of JmsReader requested.");
		this.unblock();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_PROVIDERURL, this.jmsProviderUrl);
		configuration.setProperty(CONFIG_PROPERTY_NAME_DESTINATION, this.jmsDestination);
		configuration.setProperty(CONFIG_PROPERTY_NAME_FACTORYLOOKUP, this.jmsFactoryLookupName);

		return configuration;
	}

	protected Logger getLog() {
		return super.logger;
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
				JmsReader.this.getLog().warn("Received null message");
			} else {
				if (jmsMessage instanceof ObjectMessage) {
					try {
						final ObjectMessage om = (ObjectMessage) jmsMessage;
						final Serializable omo = om.getObject();
						if ((omo instanceof IMonitoringRecord) && (!JmsReader.this.deliverIndirect(OUTPUT_PORT_NAME_RECORDS, omo))) {
							JmsReader.this.getLog().error("deliverRecord returned false");
						}
					} catch (final MessageFormatException ex) {
						JmsReader.this.getLog().error("Error delivering record", ex);
					} catch (final JMSException ex) {
						JmsReader.this.getLog().error("Error delivering record", ex);
					} catch (final Exception ex) { // NOPMD NOCS (catch Exception)
						JmsReader.this.getLog().error("Error delivering record", ex);
					}
				} else {
					JmsReader.this.getLog().warn("Received message of invalid type: {}", jmsMessage.getClass().getName());
				}
			}
		}
	}
}
