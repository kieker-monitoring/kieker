/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.AbstractMonitoringWriter;

/**
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller, Christian Wulf
 *
 * @since 0.95a
 */
public class JmsWriter extends AbstractMonitoringWriter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JmsWriter.class);

	private static final String PREFIX = JmsWriter.class.getName() + ".";

	public static final String CONFIG_PROVIDERURL = PREFIX + "ProviderUrl"; // NOCS (afterPREFIX)
	public static final String CONFIG_TOPIC = PREFIX + "Topic"; // NOCS (afterPREFIX)
	public static final String CONFIG_CONTEXTFACTORYTYPE = PREFIX + "ContextFactoryType"; // NOCS (afterPREFIX)
	public static final String CONFIG_FACTORYLOOKUPNAME = PREFIX + "FactoryLookupName"; // NOCS (afterPREFIX)
	public static final String CONFIG_MESSAGETTL = PREFIX + "MessageTimeToLive"; // NOCS (afterPREFIX)
    public static final String CONFIG_USERNAME = PREFIX + "Username"; // NOCS (afterPREFIX)
    public static final String CONFIG_PASSWORD = PREFIX + "Password"; // NOCS (afterPREFIX)

	private final String configContextFactoryType;
	private final String configProviderUrl;
	private final String configFactoryLookupName;
	private final String configTopic;
	private final long configMessageTimeToLive;
	private final String configUsername;
	private final String configPassword;

	private Session session;
	private Connection connection;
	private MessageProducer sender;

	public JmsWriter(final Configuration configuration) {
		super(configuration);
		this.configContextFactoryType = configuration.getStringProperty(CONFIG_CONTEXTFACTORYTYPE);
		this.configProviderUrl = configuration.getStringProperty(CONFIG_PROVIDERURL);
		this.configFactoryLookupName = configuration.getStringProperty(CONFIG_FACTORYLOOKUPNAME);
		this.configTopic = configuration.getStringProperty(CONFIG_TOPIC);
		this.configMessageTimeToLive = configuration.getLongProperty(CONFIG_MESSAGETTL);
		this.configUsername = configuration.getStringProperty(CONFIG_USERNAME);
		this.configPassword = configuration.getStringProperty(CONFIG_PASSWORD);

		this.init();
	}

	private void init() {
		try {
			final Hashtable<String, String> properties = new Hashtable<>(); // NOPMD NOCS (IllegalTypeCheck, InitialContext requires Hashtable)
			properties.put(Context.INITIAL_CONTEXT_FACTORY, this.configContextFactoryType);
			properties.put(Context.PROVIDER_URL, this.configProviderUrl);

			final Context context = new InitialContext(properties);

			// Leaving the following code snippet here in order to emphasize
			// that it did NOT work in the past!
			//
			// final Context context = new InitialContext();
			// context.addToEnvironment(Context.INITIAL_CONTEXT_FACTORY,
			// contextFactoryType);
			// context.addToEnvironment(Context.PROVIDER_URL, providerUrl);

			final ConnectionFactory factory = (ConnectionFactory) context.lookup(this.configFactoryLookupName);


			if(this.configUsername.isEmpty() && this.configPassword.isEmpty()) {
                this.connection = factory.createConnection();
            } else {
                this.connection = factory.createConnection(this.configUsername, this.configPassword);
            }

			this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			this.connection.start();

			Destination destination;
			try {
				// As a first step, try a JNDI lookup (this seems to fail with ActiveMQ /HornetQ sometimes)
				destination = (Destination) context.lookup(this.configTopic);
			} catch (final NameNotFoundException exc) {
				// JNDI lookup failed, try manual creation (this seems to fail with ActiveMQ/HornetQ sometimes)
				destination = this.session.createQueue(this.configTopic);
				if (destination == null) { //
					LOGGER.error("Failed to lookup queue '{}' via JNDI: {} AND failed to create queue", this.configTopic, exc.getMessage());
					throw exc; // will be catched below to abort the read method
				}
			}

			this.sender = this.session.createProducer(destination);
			this.sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			this.sender.setDisableMessageID(false);
			this.sender.setTimeToLive(this.configMessageTimeToLive);
		} catch (final NamingException ex) {
			throw new IllegalStateException("NamingException Exception while initializing JMS Writer", ex);
		} catch (final JMSException ex) {
			throw new IllegalStateException("JMSException Exception while initializing JMS Writer", ex);
		}
	}

	@Override
	public void onStarting() {
		// do nothing
	}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord record) {
		try {
			final Message message = this.session.createObjectMessage(record);
			this.sender.send(message);
		} catch (final JMSException ex) {
			throw new IllegalStateException("Error sending jms message", ex);
		}
	}

	@Override
	public void onTerminating() {
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
			LOGGER.error("Error closing connection", ex);
		}
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder(128);
		sb.append(super.toString()).append("; Session: '").append(this.session.toString()).append("'; Connection: '").append(this.connection.toString())
				.append("'; MessageProducer: '").append(this.sender.toString()).append('\'');
		return sb.toString();
	}

}
