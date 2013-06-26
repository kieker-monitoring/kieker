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

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.Map;
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

import org.apache.activemq.ActiveMQConnectionFactory;

import kieker.common.record.IMonitoringRecord;
import kieker.tools.bridge.LookupEntity;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.ConnectorEndOfDataException;
import kieker.tools.bridge.connector.IServiceConnector;
import kieker.tools.bridge.connector.ServiceConnectorFactory;

/**
 * Implements a connector for JMS which supports text and binary messages.
 * 
 * @author Reiner Jung
 * @since 1.8
 */
public class JMSClientConnector implements IServiceConnector {

	private static final int BUF_LEN = 65536;

	private final ConcurrentMap<Integer, Class<IMonitoringRecord>> recordMap;
	private final String username;
	private final String password;
	private final URI uri;

	private MessageConsumer consumer;
	private Map<Integer, LookupEntity> lookupEntityMap;
	private final byte[] buffer = new byte[BUF_LEN];
	private Connection connection;

	/**
	 * @param recordMap
	 *            map from type ids to class types
	 * @param username
	 *            JMSService login user name
	 * @param password
	 *            JMSService login password
	 * @param uri
	 *            JMSService URI
	 */
	public JMSClientConnector(final ConcurrentMap<Integer, Class<IMonitoringRecord>> recordMap, final String username, final String password, final URI uri) {
		this.recordMap = recordMap;
		this.username = username;
		this.password = password;
		this.uri = uri;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.tools.bridge.connector.IServiceConnector#initialize()
	 */
	public void initialize() throws ConnectorDataTransmissionException {
		// setup value lookup
		this.lookupEntityMap = ServiceConnectorFactory.createLookupEntityMap(this.recordMap);
		try {
			// setup connection
			ConnectionFactory factory;
			if ((this.username != null) && (this.password != null)) {
				factory = new ActiveMQConnectionFactory(this.username, this.password, this.uri);
			} else {
				factory = new ActiveMQConnectionFactory(this.uri);
			}
			this.connection = factory.createConnection();

			final Session session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			final Destination destination = session.createQueue("de.cau.cs.se.kieker.service");
			this.consumer = session.createConsumer(destination);

			this.connection.start();
		} catch (final JMSException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.tools.bridge.connector.IServiceConnector#close()
	 */
	public void close() throws ConnectorDataTransmissionException {
		try {
			this.connection.stop();
		} catch (final JMSException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.tools.bridge.connector.IServiceConnector#deserializeNextRecord()
	 */
	public IMonitoringRecord deserializeNextRecord() throws ConnectorDataTransmissionException, ConnectorEndOfDataException {
		Message message;
		try {
			message = this.consumer.receive();
			if (message != null) {
				if (message instanceof BytesMessage) {
					return this.deserialize((BytesMessage) message);
				} else if (message instanceof TextMessage) {
					return this.deserialize(((TextMessage) message).getText().split(";"));
				} else {
					throw new ConnectorDataTransmissionException("Unsupported message type " + message.getClass().getCanonicalName());
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
	private IMonitoringRecord deserialize(final BytesMessage message) throws ConnectorDataTransmissionException, ConnectorEndOfDataException {
		Integer id;
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
							throw new ConnectorDataTransmissionException(bufLen + " bytes expected, but only " + resultLen + " bytes received.");
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
	 * @param attributes
	 *            attributes of a text message
	 * @return A monitoring record for the given String array
	 * @throws Exception
	 *             when the record id is unknown or the composition fails
	 */
	private IMonitoringRecord deserialize(final String[] attributes) throws ConnectorDataTransmissionException, ConnectorEndOfDataException {
		if (attributes.length > 0) {
			final Integer id = Integer.parseInt(attributes[0]);
			final LookupEntity recordProperty = this.lookupEntityMap.get(id);
			if (recordProperty != null) {
				final Object[] values = new Object[recordProperty.getParameterTypes().length];

				for (int i = 0; i < recordProperty.getParameterTypes().length; i++) {
					final Class<?> parameterType = recordProperty.getParameterTypes()[i];
					if (boolean.class.equals(parameterType)) {
						values[i] = "t".equals(attributes[i + 1]);
					} else if (parameterType.equals(Boolean.class)) {
						values[i] = Boolean.valueOf("t".equals(attributes[i + 1]));
					} else if (byte.class.equals(parameterType)) {
						values[i] = Byte.parseByte(attributes[i + 1]);
					} else if (Byte.class.equals(parameterType)) {
						values[i] = Byte.valueOf(Byte.parseByte(attributes[i + 1]));
					} else if (short.class.equals(parameterType)) { // NOPMD
						values[i] = Short.parseShort(attributes[i + 1]);
					} else if (Short.class.equals(parameterType)) {
						values[i] = Short.valueOf(Short.parseShort(attributes[i + 1]));
					} else if (int.class.equals(parameterType)) {
						values[i] = Integer.parseInt(attributes[i + 1]);
					} else if (Integer.class.equals(parameterType)) {
						values[i] = Integer.valueOf(Integer.parseInt(attributes[i + 1]));
					} else if (long.class.equals(parameterType)) {
						values[i] = Long.parseLong(attributes[i + 1]);
					} else if (Long.class.equals(parameterType)) {
						values[i] = Long.valueOf(Long.parseLong(attributes[i + 1]));
					} else if (float.class.equals(parameterType)) {
						values[i] = Float.parseFloat(attributes[i + 1]);
					} else if (Float.class.equals(parameterType)) {
						values[i] = Float.valueOf(Float.parseFloat(attributes[i + 1]));
					} else if (double.class.equals(parameterType)) {
						values[i] = Double.parseDouble(attributes[i + 1]);
					} else if (Double.class.equals(parameterType)) {
						values[i] = Double.valueOf(Double.parseDouble(attributes[i + 1]));
					} else if (String.class.equals(parameterType)) {
						values[i] = attributes[i + 1];
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
		} else {
			throw new ConnectorDataTransmissionException("Record structure is corrupt");
		}

	}
}
