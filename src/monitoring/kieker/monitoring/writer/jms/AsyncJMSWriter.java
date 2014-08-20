/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.writer.jms;

import java.util.Hashtable;
import java.util.concurrent.BlockingQueue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.AbstractAsyncThread;
import kieker.monitoring.writer.AbstractAsyncWriter;

/**
 * 
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller
 * 
 * @since 0.95a
 */
public final class AsyncJMSWriter extends AbstractAsyncWriter {
	private static final String PREFIX = AsyncJMSWriter.class.getName() + ".";
	public static final String CONFIG_PROVIDERURL = PREFIX + "ProviderUrl"; // NOCS (afterPREFIX)
	public static final String CONFIG_TOPIC = PREFIX + "Topic"; // NOCS (afterPREFIX)
	public static final String CONFIG_CONTEXTFACTORYTYPE = PREFIX + "ContextFactoryType"; // NOCS (afterPREFIX)
	public static final String CONFIG_FACTORYLOOKUPNAME = PREFIX + "FactoryLookupName"; // NOCS (afterPREFIX)
	public static final String CONFIG_MESSAGETTL = PREFIX + "MessageTimeToLive"; // NOCS (afterPREFIX)

	private final String configContextFactoryType;
	private final String configProviderUrl;
	private final String configFactoryLookupName;
	private final String configTopic;
	private final long configMessageTimeToLive;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this writer.
	 */
	public AsyncJMSWriter(final Configuration configuration) {
		super(configuration);
		this.configContextFactoryType = configuration.getStringProperty(CONFIG_CONTEXTFACTORYTYPE);
		this.configProviderUrl = configuration.getStringProperty(CONFIG_PROVIDERURL);
		this.configFactoryLookupName = configuration.getStringProperty(CONFIG_FACTORYLOOKUPNAME);
		this.configTopic = configuration.getStringProperty(CONFIG_TOPIC);
		this.configMessageTimeToLive = configuration.getLongProperty(CONFIG_MESSAGETTL);
	}

	@Override
	protected void init() throws Exception {
		this.addWorker(new JMSWriterThread(this.monitoringController, this.blockingQueue, this.configContextFactoryType,
				this.configProviderUrl, this.configFactoryLookupName, this.configTopic, this.configMessageTimeToLive));
		this.addWorker(new JMSWriterThread(this.monitoringController, this.prioritizedBlockingQueue, this.configContextFactoryType,
				this.configProviderUrl, this.configFactoryLookupName, this.configTopic, this.configMessageTimeToLive));
	}
}

/**
 * The writer moves monitoring data via messaging to a JMS server. This uses the publishRecord/subscribe pattern. The JMS server will only keep each monitoring event
 * for messageTimeToLive milliseconds presents before it is deleted.
 * 
 * At the moment, JmsWorkers do not share any connections, sessions or other objects.
 * 
 * History: 2008-09-13: Initial prototype
 * 
 * @author Matthias Rohr, Jan Waller
 * 
 * @since < 0.9
 */
final class JMSWriterThread extends AbstractAsyncThread {
	private static final Log LOG = LogFactory.getLog(JMSWriterThread.class);

	// configuration parameters
	private Session session;
	private Connection connection;
	private MessageProducer sender;

	public JMSWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue, final String contextFactoryType,
			final String providerUrl, final String factoryLookupName, final String topic, final long messageTimeToLive) throws Exception {
		super(monitoringController, writeQueue);
		try {
			final Hashtable<String, String> properties = new Hashtable<String, String>(); // NOPMD NOCS (IllegalTypeCheck, InitialContext requires Hashtable)
			properties.put(Context.INITIAL_CONTEXT_FACTORY, contextFactoryType);
			properties.put(Context.PROVIDER_URL, providerUrl);

			final Context context = new InitialContext(properties);

			// Leaving the following code snippet here in order to emphasize
			// that it did NOT work in the past!
			//
			// final Context context = new InitialContext();
			// context.addToEnvironment(Context.INITIAL_CONTEXT_FACTORY,
			// contextFactoryType);
			// context.addToEnvironment(Context.PROVIDER_URL, providerUrl);

			final ConnectionFactory factory = (ConnectionFactory) context.lookup(factoryLookupName);
			this.connection = factory.createConnection();
			this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			this.connection.start();

			Destination destination;
			try {
				// As a first step, try a JNDI lookup (this seems to fail with ActiveMQ /HornetQ sometimes)
				destination = (Destination) context.lookup(topic);
			} catch (final NameNotFoundException exc) {
				// JNDI lookup failed, try manual creation (this seems to fail with ActiveMQ/HornetQ sometimes)
				destination = this.session.createQueue(topic);
				if (destination == null) { //
					LOG.error("Failed to lookup queue '" + topic + "' via JNDI: " + exc.getMessage() + " AND failed to create queue");
					throw exc; // will be catched below to abort the read method
				}
			}

			this.sender = this.session.createProducer(destination);
			this.sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			this.sender.setDisableMessageID(false);
			this.sender.setTimeToLive(messageTimeToLive);
		} catch (final NamingException ex) {
			throw new Exception("NamingException Exception while initializing JMS Writer", ex);
		} catch (final JMSException ex) {
			throw new Exception("JMSException Exception while initializing JMS Writer", ex);
		}
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder(128);
		sb.append(super.toString());
		sb.append("; Session: '");
		sb.append(this.session.toString());
		sb.append("'; Connection: '");
		sb.append(this.connection.toString());
		sb.append("'; MessageProducer: '");
		sb.append(this.sender.toString());
		sb.append('\'');
		return sb.toString();
	}

	@Override
	protected final void consume(final IMonitoringRecord monitoringRecord) throws Exception {
		try {
			this.sender.send(this.session.createObjectMessage(monitoringRecord));
		} catch (final JMSException ex) {
			throw new Exception("Error sending jms message", ex);
		}
	}

	@Override
	protected final void cleanup() {
		try {
			if (this.sender != null) {
				this.sender.close();
			}
			if (this.session != null) {
				this.session.close();
			}
			if (this.connection != null) {
				this.connection.close();
			}
		} catch (final JMSException ex) {
			JMSWriterThread.LOG.error("Error closing connection", ex);
		}
	}
}
