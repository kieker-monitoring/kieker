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

package kieker.tools.bridge.connector.jms;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IRecord;
import kieker.common.record.LookupEntity;
import kieker.common.record.MonitoringRecordFactory;
import kieker.common.record.control.StringMapRecord;
import kieker.tools.bridge.connector.AbstractConnector;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.ConnectorEndOfDataException;
import kieker.tools.bridge.connector.ConnectorProperty;

/**
 * Implements a connector for JMS which supports text and binary messages.
 * 
 * @author Reiner Jung
 * 
 * @since 1.8
 */
@ConnectorProperty(cmdName = "jms-client", name = "JMS Client Connector", description = "JMS Client to receive records from a JMS queue.")
public class JMSClientConnector extends AbstractConnector {

	/** Property name for the configuration user name property. */
	public static final String USERNAME = JMSClientConnector.class.getCanonicalName() + ".username";
	/** Property name for the configuration password property. */
	public static final String PASSWORD = JMSClientConnector.class.getCanonicalName() + ".password";
	/** Property name for the configuration service URI property. */
	public static final String URI = JMSClientConnector.class.getCanonicalName() + ".uri";
	/** Default KDB queue name. */
	public static final String KIEKER_DATA_BRIDGE_READ_QUEUE = "kieker.tools.bridge";

	private final String username;
	private final String password;
	private final String uri;

	private MessageConsumer consumer;

	private Map<Integer, LookupEntity> lookupEntityMap;
	/** normal hashmap is sufficient, as TCPClientConnector is not multi-threaded */
	private final Map<Integer, String> stringMap = new HashMap<Integer, String>();
	private final byte[] buffer = new byte[MonitoringRecordFactory.MAX_BUFFER_SIZE];

	private Connection connection;

	/**
	 * Create a JMSClientConnector.
	 * 
	 * @param configuration
	 *            Kieker configuration including setup for connectors
	 * 
	 * @param lookupEntityMap
	 *            IMonitoringRecord constructor and TYPES-array to id map
	 * @throws ConnectorDataTransmissionException
	 */
	public JMSClientConnector(final Configuration configuration, final ConcurrentMap<Integer, LookupEntity> lookupEntityMap) {
		super(configuration, lookupEntityMap);
		this.username = this.configuration.getStringProperty(JMSClientConnector.USERNAME);
		this.password = this.configuration.getStringProperty(JMSClientConnector.PASSWORD);
		this.uri = this.configuration.getStringProperty(JMSClientConnector.URI);
	}

	/**
	 * Initialize the JMS connection to read from a JMS queue.
	 * 
	 * @throws ConnectorDataTransmissionException
	 *             if any JMSException occurs
	 */
	public void initialize() throws ConnectorDataTransmissionException {
		try {
			// setup connection
			ConnectionFactory factory;
			if ((this.username != null) && (this.password != null)) {
				factory = new ActiveMQConnectionFactory(this.username, this.password, new URI(this.uri));
			} else {
				factory = new ActiveMQConnectionFactory(new URI(this.uri));
			}
			this.connection = factory.createConnection();

			final Session session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			final Destination destination = session.createQueue(KIEKER_DATA_BRIDGE_READ_QUEUE);
			this.consumer = session.createConsumer(destination);

			this.connection.start();
		} catch (final JMSException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		} catch (final URISyntaxException e) {
			throw new ConnectorDataTransmissionException("URI for queue.", e);
		}
	}

	/**
	 * Close the JMS connection.
	 * 
	 * @throws ConnectorDataTransmissionException
	 *             if any JMSException occurs
	 */
	public void close() throws ConnectorDataTransmissionException {
		try {
			this.connection.stop();
		} catch (final JMSException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		}
	}

	/**
	 * Fetch a text or binary message from the JMS queue and use the correct deserializer for the received message.
	 * 
	 * @return One new IMonitoringRecord
	 * 
	 * @throws ConnectorDataTransmissionException
	 *             if the message type is neither binary nor text, or if a JMSException occurs
	 * @throws ConnectorEndOfDataException
	 *             if the received message is null indicating that the consumer is closed
	 */
	public IMonitoringRecord deserializeNextRecord() throws ConnectorDataTransmissionException, ConnectorEndOfDataException {
		final IRecord record = MonitoringRecordFactory.derserializeRecordFromJMS(this.consumer, this.buffer, this.lookupEntityMap, this.stringMap);
		if (record instanceof IMonitoringRecord) {
			return (IMonitoringRecord) record;
		} else {
			if (record instanceof StringMapRecord) {
				this.stringMap.put(((StringMapRecord) record).getKey(), ((StringMapRecord) record).getString());
			}
			return this.deserializeNextRecord();
		}
	}

}
