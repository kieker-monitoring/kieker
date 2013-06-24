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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

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
import kieker.tools.bridge.ConnectorDataTransmissionException;
import kieker.tools.bridge.LookupEntity;
import kieker.tools.bridge.connector.ConnectorEndOfDataException;
import kieker.tools.bridge.connector.IServiceConnector;

/**
 * 
 * 
 * @author Reiner Jung
 * @since 1.8
 */
public class JMSClientConnector implements IServiceConnector {

	private static final int BUF_LEN = 65536;

	private final Map<Integer, Class<IMonitoringRecord>> recordMap;
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
	 * @param lookupEntityMap
	 *            IMonitoringRecord to id map
	 * @param username
	 *            JMSService login user name
	 * @param password
	 *            JMSService login password
	 * @param uri
	 *            JMSService URI
	 */
	public JMSClientConnector(final Map<Integer, Class<IMonitoringRecord>> recordMap, final String username, final String password, final URI uri) {
		this.recordMap = recordMap;
		this.username = username;
		this.password = password;
		this.uri = uri;
	}

	public IMonitoringRecord deserializeNextRecord() throws ConnectorDataTransmissionException, ConnectorEndOfDataException {
		Message message;
		try {
			message = this.consumer.receive();
			if (message != null) {
				if (message instanceof BytesMessage) {
					return this.deserialize((BytesMessage) message);
				} else if (message instanceof TextMessage) {
					// TODO support ; escaping
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
				final Object[] values = new Object[recordProperty.parameterTypes.length];

				int i = 0;
				for (final Class<?> parameterType : recordProperty.parameterTypes) {
					if (boolean.class.equals(parameterType)) {
						values[i] = message.readBoolean();
					} else if (Boolean.class.equals(parameterType)) {
						values[i] = message.readBoolean() ? Boolean.TRUE : Boolean.FALSE;
					} else if (byte.class.equals(parameterType)) {
						values[i] = message.readByte();
					} else if (Byte.class.equals(parameterType)) {
						values[i] = new Byte(message.readByte());
					} else if (short.class.equals(parameterType)) {
						values[i] = message.readShort();
					} else if (Short.class.equals(parameterType)) {
						values[i] = new Short(message.readShort());
					} else if (int.class.equals(parameterType)) {
						values[i] = message.readInt();
					} else if (Integer.class.equals(parameterType)) {
						values[i] = new Integer(message.readInt());
					} else if (long.class.equals(parameterType)) {
						values[i] = message.readLong();
					} else if (Long.class.equals(parameterType)) {
						values[i] = new Long(message.readLong());
					} else if (float.class.equals(parameterType)) {
						values[i] = message.readFloat();
					} else if (Float.class.equals(parameterType)) {
						values[i] = new Float(message.readFloat());
					} else if (double.class.equals(parameterType)) {
						values[i] = message.readDouble();
					} else if (Double.class.equals(parameterType)) {
						values[i] = new Double(message.readDouble());
					} else if (String.class.equals(parameterType)) {
						final int bufLen = message.readInt();
						message.readBytes(this.buffer, bufLen);
						values[i] = new String(this.buffer, 0, bufLen, "UTF-8");
					} else { // reference types
						throw new ConnectorDataTransmissionException("References are not yet supported.");
					}
					i++; // TODO: use a real for loop and access both arrays with the index?
				}
				return recordProperty.constructor.newInstance(new Object[] { values }); // TODO: why repack the array?
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
				final Object[] values = new Object[recordProperty.parameterTypes.length];

				int i = 0;
				for (final Class<?> parameterType : recordProperty.parameterTypes) {
					if (boolean.class.equals(parameterType)) {
						values[i] = "t".equals(attributes[i + 1]);
					} else if (parameterType.equals(Boolean.class)) {
						values[i] = attributes[i + 1].equals("t") ? Boolean.TRUE : Boolean.FALSE;
					} else if (byte.class.equals(parameterType)) {
						values[i] = Byte.parseByte(attributes[i + 1]);
					} else if (Byte.class.equals(parameterType)) {
						values[i] = new Byte(Byte.parseByte(attributes[i + 1]));
					} else if (short.class.equals(parameterType)) {
						values[i] = Short.parseShort(attributes[i + 1]);
					} else if (Short.class.equals(parameterType)) {
						values[i] = new Short(Short.parseShort(attributes[i + 1]));
					} else if (int.class.equals(parameterType)) {
						values[i] = Integer.parseInt(attributes[i + 1]);
					} else if (Integer.class.equals(parameterType)) {
						values[i] = new Integer(Integer.parseInt(attributes[i + 1]));
					} else if (long.class.equals(parameterType)) {
						values[i] = Long.parseLong(attributes[i + 1]);
					} else if (Long.class.equals(parameterType)) {
						values[i] = new Long(Long.parseLong(attributes[i + 1]));
					} else if (float.class.equals(parameterType)) {
						values[i] = Float.parseFloat(attributes[i + 1]);
					} else if (Float.class.equals(parameterType)) {
						values[i] = new Float(Float.parseFloat(attributes[i + 1]));
					} else if (double.class.equals(parameterType)) {
						values[i] = Double.parseDouble(attributes[i + 1]);
					} else if (Double.class.equals(parameterType)) {
						values[i] = new Double(Double.parseDouble(attributes[i + 1]));
					} else if (String.class.equals(parameterType)) {
						values[i] = attributes[i + 1];
					} else { // reference types
						throw new ConnectorDataTransmissionException("References are not yet supported."); // TODO see above ...
					}
					i++; // TODO see above ...
				}
				try {
					return recordProperty.constructor.newInstance(new Object[] { values });
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

	public void setup() throws ConnectorDataTransmissionException {
		// setup value lookup
		this.lookupEntityMap = new HashMap<Integer, LookupEntity>();
		for (final int key : this.recordMap.keySet()) {
			final Class<IMonitoringRecord> type = this.recordMap.get(key);
			// TODO: use existing methods?
			try {
				final Field parameterTypesField = type.getDeclaredField("TYPES");
				java.security.AccessController.doPrivileged(new PrivilegedAction<Object>() {
					public Object run() {
						parameterTypesField.setAccessible(true);
						return null;
					}
				});

				final LookupEntity entity = new LookupEntity(type.getConstructor(Object[].class), (Class<?>[]) parameterTypesField.get(null));
				this.lookupEntityMap.put(key, entity);

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
			} catch (final NoSuchFieldException e) {
				throw new ConnectorDataTransmissionException(e.getMessage(), e);
			} catch (final SecurityException e) {
				throw new ConnectorDataTransmissionException(e.getMessage(), e);
			} catch (final NoSuchMethodException e) {
				throw new ConnectorDataTransmissionException(e.getMessage(), e);
			} catch (final IllegalArgumentException e) {
				throw new ConnectorDataTransmissionException(e.getMessage(), e);
			} catch (final IllegalAccessException e) {
				throw new ConnectorDataTransmissionException(e.getMessage(), e);
			} catch (final JMSException e) {
				throw new ConnectorDataTransmissionException(e.getMessage(), e);
			}
		}

	}

	public void close() throws ConnectorDataTransmissionException {
		try {
			this.connection.stop();
		} catch (final JMSException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		}
	}
}
