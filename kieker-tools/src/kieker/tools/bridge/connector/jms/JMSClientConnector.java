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

package kieker.tools.bridge.connector.jms;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.nio.CharBuffer;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentMap;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.TextValueDeserializer;
import kieker.tools.bridge.LookupEntity;
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
	/** Property name for the configuration of the JMS connector. */
	public static final String FACTORY_LOOKUP_NAME = JMSClientConnector.class.getCanonicalName()
			+ ".jmsFactoryLookupName";
	/** Default KDB queue name. */
	public static final String KIEKER_DATA_BRIDGE_READ_QUEUE = "kieker.tools.bridge";

	private static final int BUF_LEN = 65536;

	/** username used to connect to the JMS service. */
	protected final String username;
	/** password used to connect to the JMS service. */
	protected final String password;
	private final String uri;

	private MessageConsumer consumer;
	private final byte[] buffer = new byte[JMSClientConnector.BUF_LEN];
	private Connection connection;
	private final String jmsFactoryLookupName;

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
	public JMSClientConnector(final Configuration configuration,
			final ConcurrentMap<Integer, LookupEntity> lookupEntityMap) {
		super(configuration, lookupEntityMap);
		this.username = this.configuration.getStringProperty(JMSClientConnector.USERNAME);
		this.password = this.configuration.getStringProperty(JMSClientConnector.PASSWORD);
		this.uri = this.configuration.getStringProperty(JMSClientConnector.URI);
		this.jmsFactoryLookupName = this.configuration.getStringProperty(JMSClientConnector.FACTORY_LOOKUP_NAME);
	}

	/**
	 * Initialize the JMS connection to read from a JMS queue.
	 *
	 * @throws ConnectorDataTransmissionException
	 *             if any JMSException occurs
	 */
	@Override
	public void initialize() throws ConnectorDataTransmissionException {
		try {
			// setup connection
			final Hashtable<String, String> properties = new Hashtable<>(); // NOPMD NOCS
																			// (IllegalTypeCheck,
																			// InitialContext requires
																			// Hashtable)
			properties.put(Context.INITIAL_CONTEXT_FACTORY, this.jmsFactoryLookupName);
			properties.put(Context.PROVIDER_URL, this.uri);

			final Context context = new InitialContext(properties);
			final ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
			this.connection = factory.createConnection();

			final Session session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			final Destination destination = session.createQueue(JMSClientConnector.KIEKER_DATA_BRIDGE_READ_QUEUE);
			this.consumer = session.createConsumer(destination);

			this.connection.start();
		} catch (final NamingException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		} catch (final JMSException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		}
	}

	/**
	 * Close the JMS connection.
	 *
	 * @throws ConnectorDataTransmissionException
	 *             if any JMSException occurs
	 */
	@Override
	public void close() throws ConnectorDataTransmissionException {
		try {
			this.connection.stop();
		} catch (final JMSException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		}
	}

	/**
	 * Fetch a text or binary message from the JMS queue and use the correct
	 * deserializer for the received message.
	 *
	 * @return One new IMonitoringRecord
	 *
	 * @throws ConnectorDataTransmissionException
	 *             if the message type is neither binary nor text, or if a
	 *             JMSException occurs
	 * @throws ConnectorEndOfDataException
	 *             if the received message is null indicating that the consumer is
	 *             closed
	 */
	@Override
	public IMonitoringRecord deserializeNextRecord()
			throws ConnectorDataTransmissionException, ConnectorEndOfDataException {
		final Message message;
		try {
			message = this.consumer.receive();
			if (message != null) {
				if (message instanceof BytesMessage) {
					return this.deserialize((BytesMessage) message);
				} else if (message instanceof TextMessage) {
					return this.deserialize(((TextMessage) message).getText());
				} else {
					throw new ConnectorDataTransmissionException(
							"Unsupported message type " + message.getClass().getCanonicalName());
				}
			} else {
				throw new ConnectorEndOfDataException("No more records in the queue");
			}
		} catch (final JMSException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		}

	}

	/**
	 * deserialize BinaryMessages and store them in a IMonitoringRecord.
	 *
	 * @param message
	 *            a ByteMessage
	 * @return A monitoring record for the given ByteMessage
	 * @throws Exception
	 *             when the record id is unknown or the composition fails
	 */
	private IMonitoringRecord deserialize(final BytesMessage message)
			throws ConnectorDataTransmissionException, ConnectorEndOfDataException {
		final Integer id;
		try {
			id = message.readInt();
			final LookupEntity recordProperty = this.lookupEntityMap.get(id);
			if (recordProperty != null) {
				final Object[] values = new Object[recordProperty.getParameterTypes().length];

				for (int i = 0; i < recordProperty.getParameterTypes().length; i++) {
					final Class<?> parameterType = recordProperty.getParameterTypes()[i];
					if (boolean.class.equals(parameterType)) {
						values[i] = message.readBoolean();
					} else if (Boolean.class.equals(parameterType)) {
						values[i] = Boolean.valueOf(message.readBoolean());
					} else if (byte.class.equals(parameterType)) {
						values[i] = message.readByte();
					} else if (Byte.class.equals(parameterType)) {
						values[i] = Byte.valueOf(message.readByte());
					} else if (short.class.equals(parameterType)) { // NOPMD
						values[i] = message.readShort();
					} else if (Short.class.equals(parameterType)) {
						values[i] = Short.valueOf(message.readShort());
					} else if (int.class.equals(parameterType)) {
						values[i] = message.readInt();
					} else if (Integer.class.equals(parameterType)) {
						values[i] = Integer.valueOf(message.readInt());
					} else if (long.class.equals(parameterType)) {
						values[i] = message.readLong();
					} else if (Long.class.equals(parameterType)) {
						values[i] = Long.valueOf(message.readLong());
					} else if (float.class.equals(parameterType)) {
						values[i] = message.readFloat();
					} else if (Float.class.equals(parameterType)) {
						values[i] = Float.valueOf(message.readFloat());
					} else if (double.class.equals(parameterType)) {
						values[i] = message.readDouble();
					} else if (Double.class.equals(parameterType)) {
						values[i] = Double.valueOf(message.readDouble());
					} else if (String.class.equals(parameterType)) {
						final int bufLen = message.readInt();
						final int resultLen = message.readBytes(this.buffer, bufLen);
						if (resultLen == bufLen) {
							values[i] = new String(this.buffer, 0, bufLen, "UTF-8");
						} else {
							throw new ConnectorDataTransmissionException(
									bufLen + " bytes expected, but only " + resultLen + " bytes received.");
						}
					} else { // reference types
						throw new ConnectorDataTransmissionException("References are not yet supported.");
					}
				}
				return recordProperty.getConstructor().newInstance(values);
			} else {
				throw new ConnectorDataTransmissionException("Record type " + id + " is not registered.");
			}
		} catch (final JMSException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		} catch (final UnsupportedEncodingException e) {
			throw new ConnectorDataTransmissionException("Expected a string value in UTF-8", e);
		} catch (final InstantiationException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		} catch (final IllegalAccessException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		} catch (final IllegalArgumentException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		} catch (final InvocationTargetException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		}

	}

	/**
	 * deserialize String array and store it in a IMonitoringRecord.
	 *
	 * @param message
	 *            attributes of a text message
	 * @return A monitoring record for the given String array
	 * @throws Exception
	 *             when the record id is unknown or the composition fails
	 */
	private IMonitoringRecord deserialize(final String message)
			throws ConnectorDataTransmissionException, ConnectorEndOfDataException {
		final IValueDeserializer deserializer = TextValueDeserializer.create(CharBuffer.wrap(message.toCharArray()));
		/** get record id. */
		final Integer id = deserializer.getInt();
		final LookupEntity recordProperty = this.lookupEntityMap.get(id);
		if (recordProperty != null) {
			final Object[] values = new Object[recordProperty.getParameterTypes().length];

			for (int i = 0; i < recordProperty.getParameterTypes().length; i++) {
				final Class<?> parameterType = recordProperty.getParameterTypes()[i];
				if (boolean.class.equals(parameterType)) {
					values[i] = deserializer.getBoolean();
				} else if (parameterType.equals(Boolean.class)) {
					values[i] = deserializer.getBoolean();
				} else if (byte.class.equals(parameterType)) {
					values[i] = deserializer.getByte();
				} else if (Byte.class.equals(parameterType)) {
					values[i] = deserializer.getByte();
				} else if (short.class.equals(parameterType)) { // NOPMD
					values[i] = deserializer.getShort();
				} else if (Short.class.equals(parameterType)) {
					values[i] = deserializer.getShort();
				} else if (int.class.equals(parameterType)) {
					values[i] = deserializer.getInt();
				} else if (Integer.class.equals(parameterType)) {
					values[i] = deserializer.getInt();
				} else if (long.class.equals(parameterType)) {
					values[i] = deserializer.getLong();
				} else if (Long.class.equals(parameterType)) {
					values[i] = deserializer.getLong();
				} else if (float.class.equals(parameterType)) {
					values[i] = deserializer.getFloat();
				} else if (Float.class.equals(parameterType)) {
					values[i] = deserializer.getFloat();
				} else if (double.class.equals(parameterType)) {
					values[i] = deserializer.getDouble();
				} else if (Double.class.equals(parameterType)) {
					values[i] = deserializer.getDouble();
				} else if (String.class.equals(parameterType)) {
					values[i] = deserializer.getString();
				} else { // reference types
					throw new ConnectorDataTransmissionException("References are not yet supported.");
				}
			}
			try {
				return recordProperty.getConstructor().newInstance(values);
			} catch (final InstantiationException e) {
				throw new ConnectorDataTransmissionException(e.getMessage(), e);
			} catch (final IllegalAccessException e) {
				throw new ConnectorDataTransmissionException(e.getMessage(), e);
			} catch (final IllegalArgumentException e) {
				throw new ConnectorDataTransmissionException(e.getMessage(), e);
			} catch (final InvocationTargetException e) {
				throw new ConnectorDataTransmissionException(e.getMessage(), e);
			}
		} else {
			throw new ConnectorDataTransmissionException("Record type " + id + " is not registered.");
		}

	}
}
