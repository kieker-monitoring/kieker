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
package kieker.common.record;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import kieker.common.exception.MonitoringRecordException;
import kieker.common.record.control.StringMapRecord;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.ConnectorEndOfDataException;

/**
 * Factory for record construction
 * 
 * @author Reiner Jung
 * 
 * @since 1.9
 * 
 */
public class MonitoringRecordFactory {

	/** id of string map records */
	public static final int STRING_MAP_ID = -1;
	/** buffer for string de-serialization */
	public static int MAX_BUFFER_SIZE = 65535;
	/** API method string */
	private static final String TYPES = "TYPES";

	public static IMonitoringRecord derserializeRecordFromBuffer() {
		return null;
	}

	/**
	 * Read record information from a stream and de-serialize the information.
	 * 
	 * @param input
	 *            the binary input stream
	 * @param lookupEntity
	 *            the required parameter for data reconstruction
	 * @param stringMap
	 *            the string map for string de-serialization
	 * @return one IMonitoringRecord
	 * @throws ConnectorDataTransmissionException
	 *             if the record information and the data stream do not add up
	 * @throws ConnectorEndOfDataException
	 *             if the end of stream is reached
	 */
	public static IRecord derserializeRecordFromStream(final DataInputStream input, final byte[] buffer, final Map<Integer, LookupEntity> lookupEntityMap,
			final Map<Integer, String> stringMap)
			throws ConnectorDataTransmissionException, ConnectorEndOfDataException {
		try {
			// read record ID
			final Integer id = input.readInt();
			if (id < 0) { // system records
				if (id == MonitoringRecordFactory.STRING_MAP_ID) {
					final int key = input.readInt();
					final int length = input.readInt();
					input.readFully(buffer, 0, length);
					buffer[length] = 0;
					return new StringMapRecord(key, new String(buffer, 0, length, Charset.forName("UTF-8")));
				} else {
					throw new ConnectorDataTransmissionException("Unknown system record type " + id);
				}
			} else {
				final LookupEntity lookupEntity = lookupEntityMap.get(id);
				if (lookupEntity != null) {
					final Object[] values = new Object[lookupEntity.getParameterTypes().length];
					for (int i = 0; i < lookupEntity.getParameterTypes().length; i++) {
						final Class<?> parameterType = lookupEntity.getParameterTypes()[i];
						if (boolean.class.equals(parameterType)) {
							values[i] = input.readBoolean();
						} else if (Boolean.class.equals(parameterType)) {
							values[i] = input.readBoolean();
						} else if (byte.class.equals(parameterType)) {
							values[i] = input.readByte();
						} else if (Byte.class.equals(parameterType)) {
							values[i] = input.readByte();
						} else if (short.class.equals(parameterType)) { // NOPMD
							values[i] = input.readShort();
						} else if (Short.class.equals(parameterType)) {
							values[i] = input.readShort();
						} else if (int.class.equals(parameterType)) {
							values[i] = input.readInt();
						} else if (Integer.class.equals(parameterType)) {
							values[i] = input.readInt();
						} else if (long.class.equals(parameterType)) {
							values[i] = input.readLong();
						} else if (Long.class.equals(parameterType)) {
							values[i] = input.readLong();
						} else if (float.class.equals(parameterType)) {
							values[i] = input.readFloat();
						} else if (Float.class.equals(parameterType)) {
							values[i] = input.readFloat();
						} else if (double.class.equals(parameterType)) {
							values[i] = input.readDouble();
						} else if (Double.class.equals(parameterType)) {
							values[i] = input.readDouble();
						} else if (String.class.equals(parameterType)) {
							final int key = input.readInt();
							values[i] = stringMap.get(key);
						} else { // reference types
							throw new ConnectorDataTransmissionException("References are not yet supported.");
						}
					}

					return lookupEntity.getConstructor().newInstance(values);
				} else {
					throw new ConnectorDataTransmissionException("Record type not specified.");
				}
			}
		} catch (final java.net.SocketException e) {
			throw new ConnectorEndOfDataException("End of stream", e);
		} catch (final java.io.EOFException e) {
			throw new ConnectorEndOfDataException("End of stream during an read operation", e);
		} catch (final IOException e) {
			throw new ConnectorDataTransmissionException("Read error", e);
		} catch (final InstantiationException e) {
			throw new ConnectorDataTransmissionException("Instantiation error", e);
		} catch (final IllegalAccessException e) {
			throw new ConnectorDataTransmissionException("Access to fields are restricted", e);
		} catch (final IllegalArgumentException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		} catch (final InvocationTargetException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		}
	}

	/**
	 * Create on lookup entity for a IMonitoringRecord by extracting information from the TYPES array and the constructor.
	 * The resulting record is attached to the lookupEntityMap.
	 * 
	 * @param type
	 *            the record type
	 * 
	 * @return returns one lookup entity
	 * @throws MonitoringRecordException
	 *             when the reflection indicates an error
	 */
	public static LookupEntity createLookupEntity(final Class<? extends IMonitoringRecord> type) throws MonitoringRecordException {
		try {
			final Field parameterTypesField = type.getDeclaredField(TYPES);
			java.security.AccessController.doPrivileged(new PrivilegedAction<Object>() {
				public Object run() {
					parameterTypesField.setAccessible(true);
					return null;
				}
			});
			return new LookupEntity(type.getConstructor((Class<?>[]) parameterTypesField.get(null)), (Class<?>[]) parameterTypesField.get(null));

		} catch (final NoSuchFieldException e) {
			throw new MonitoringRecordException("Field " + TYPES + " does not exist.", e);
		} catch (final SecurityException e) {
			throw new MonitoringRecordException("Security exception.", e);
		} catch (final NoSuchMethodException e) {
			throw new MonitoringRecordException("Method not found. Should not occur, as we are not looking for any method.", e);
		} catch (final IllegalArgumentException e) {
			throw new MonitoringRecordException(e.getMessage(), e);
		} catch (final IllegalAccessException e) {
			throw new MonitoringRecordException(e.getMessage(), e);
		}
	}

	/**
	 * Create a complete lookup entity map.
	 * 
	 * @param createRecordMap
	 * @return
	 * @throws MonitoringRecordException
	 */
	public static ConcurrentMap<Integer, LookupEntity> createLookupEntityMap(final ConcurrentMap<Integer, Class<? extends IMonitoringRecord>> recordMap)
			throws MonitoringRecordException {
		final ConcurrentMap<Integer, LookupEntity> lookupMap = new ConcurrentHashMap<Integer, LookupEntity>();
		for (final int key : recordMap.keySet()) {
			final Class<? extends IMonitoringRecord> clazz = recordMap.get(key);
			lookupMap.put(key, MonitoringRecordFactory.createLookupEntity(clazz));
		}

		return lookupMap;
	}

	/**
	 * Create a string record map entry based on data from an binary JMS message and add it to the stringMap.
	 * 
	 * @param message
	 *            the binary message
	 * @param stringMap
	 *            the string map
	 * @param buffer
	 *            the buffer used to read in data
	 * @throws JMSException
	 *             when there is an error using the JMS functions
	 * @throws ConnectorDataTransmissionException
	 *             when there is an error with the string size
	 */
	public static void addStringLookupEntryFromJMSBytesMessage(final BytesMessage message, final Map<Integer, String> stringMap, final byte[] buffer)
			throws JMSException, ConnectorDataTransmissionException {
		final int key = message.readInt();
		final int length = message.readInt();
		final int resultLen = message.readBytes(buffer, length);
		if (resultLen == length) {
			stringMap.put(key, new String(buffer, Charset.forName("UTF-8")));
		} else {
			throw new ConnectorDataTransmissionException(length + " bytes expected, but only " + resultLen + " bytes received.");
		}

	}

	/**
	 * Process a JMS Message and return the correct IRecord structure.
	 * 
	 * @param consumer
	 *            the source of the message
	 * @param buffer
	 *            a buffer for string de-serialization
	 * @param lookupEntityMap
	 *            the record lookup map
	 * @param stringMap
	 *            the string lookup map
	 * @return returns one IRecord object
	 * 
	 * @throws ConnectorDataTransmissionException
	 *             on communication errors
	 * @throws ConnectorEndOfDataException
	 *             when end of data is reached
	 */
	public static IRecord derserializeRecordFromJMS(final MessageConsumer consumer, final byte[] buffer, final Map<Integer, LookupEntity> lookupEntityMap,
			final Map<Integer, String> stringMap) throws ConnectorDataTransmissionException, ConnectorEndOfDataException {
		try {
			final Message message = consumer.receive();
			if (message != null) {
				if (message instanceof BytesMessage) {
					return MonitoringRecordFactory.deserializeJMSBytesMessage((BytesMessage) message, buffer, lookupEntityMap, stringMap);
				} else if (message instanceof TextMessage) {
					return MonitoringRecordFactory.deserializeJMSTextMessage((TextMessage) message, buffer, lookupEntityMap, stringMap);
				} else if (message instanceof ObjectMessage) {
					return MonitoringRecordFactory.deserializeObjectMessage(((ObjectMessage) message));
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
	 * Receive an IRecord object as ObjectMessage.
	 * 
	 * @param message
	 *            the ObjectMessage
	 * @return returns one IRecord
	 * @throws ConnectorDataTransmissionException
	 *             when object of another type is received or a JMS error occurs
	 */
	private static IRecord deserializeObjectMessage(final ObjectMessage message) throws ConnectorDataTransmissionException {
		try {
			final Object object = message.getObject();
			if (object instanceof IRecord) {
				return (IRecord) object;
			} else {
				throw new ConnectorDataTransmissionException("Unsupported data type " + object.getClass().getCanonicalName());
			}
		} catch (final JMSException e) {
			throw new ConnectorDataTransmissionException("Error while receiving data via JMS", e);
		}
	}

	/**
	 * Receive a TextMessage an create a record from that data.
	 * 
	 * @param message
	 *            the TextMessage
	 * @param buffer
	 *            buffer for String de-serialization
	 * @param lookupEntityMap
	 *            the record lookup map
	 * @param stringMap
	 *            the string map
	 * @return returns one IRecord
	 * @throws ConnectorDataTransmissionException
	 *             when an instantiation error occurs, an id lookup fails, or generally the record structure is malformed
	 */
	private static IRecord deserializeJMSTextMessage(final TextMessage message, final byte[] buffer, final Map<Integer, LookupEntity> lookupEntityMap,
			final Map<Integer, String> stringMap) throws ConnectorDataTransmissionException {
		try {
			final String[] attributes = message.getText().split(";");
			if (attributes.length > 0) {
				final Integer id = Integer.parseInt(attributes[0]);
				if (id < 0) { // system record
					return new StringMapRecord(Integer.parseInt(attributes[1]), attributes[2]);
				} else {
					final LookupEntity recordProperty = lookupEntityMap.get(id);
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
				}
			} else {
				throw new ConnectorDataTransmissionException("Record structure is corrupt");
			}
		} catch (final JMSException e1) {
			throw new ConnectorDataTransmissionException(e1.getMessage(), e1);
		}
	}

	/**
	 * De-serialize a byte record.
	 * 
	 * @param message
	 *            the BytesMessage
	 * @param buffer
	 *            string conversion buffer
	 * @param lookupEntityMap
	 *            lookupEntityMap
	 * @param stringMap
	 *            map for strings
	 * @return returns on IRecord entity
	 * @throws ConnectorDataTransmissionException
	 *             when any communication error was detected
	 */
	private static IRecord deserializeJMSBytesMessage(final BytesMessage message, final byte[] buffer, final Map<Integer, LookupEntity> lookupEntityMap,
			final Map<Integer, String> stringMap) throws ConnectorDataTransmissionException {
		try {
			// read record ID
			final Integer id = message.readInt();
			if (id < 0) { // system records
				if (id == MonitoringRecordFactory.STRING_MAP_ID) {
					final int key = message.readInt();
					final int length = message.readInt();
					message.readBytes(buffer, length);
					return new StringMapRecord(key, new String(buffer, Charset.forName("UTF-8")));
				} else {
					throw new ConnectorDataTransmissionException("Unknown system record type " + id);
				}
			} else {
				final LookupEntity lookupEntity = lookupEntityMap.get(id);
				if (lookupEntity != null) {
					final Object[] values = new Object[lookupEntity.getParameterTypes().length];

					for (int i = 0; i < lookupEntity.getParameterTypes().length; i++) {
						final Class<?> parameterType = lookupEntity.getParameterTypes()[i];
						if (boolean.class.equals(parameterType)) {
							values[i] = message.readBoolean();
						} else if (Boolean.class.equals(parameterType)) {
							values[i] = message.readBoolean();
						} else if (byte.class.equals(parameterType)) {
							values[i] = message.readByte();
						} else if (Byte.class.equals(parameterType)) {
							values[i] = message.readByte();
						} else if (short.class.equals(parameterType)) { // NOPMD
							values[i] = message.readShort();
						} else if (Short.class.equals(parameterType)) {
							values[i] = message.readShort();
						} else if (int.class.equals(parameterType)) {
							values[i] = message.readInt();
						} else if (Integer.class.equals(parameterType)) {
							values[i] = message.readInt();
						} else if (long.class.equals(parameterType)) {
							values[i] = message.readLong();
						} else if (Long.class.equals(parameterType)) {
							values[i] = message.readLong();
						} else if (float.class.equals(parameterType)) {
							values[i] = message.readFloat();
						} else if (Float.class.equals(parameterType)) {
							values[i] = message.readFloat();
						} else if (double.class.equals(parameterType)) {
							values[i] = message.readDouble();
						} else if (Double.class.equals(parameterType)) {
							values[i] = message.readDouble();
						} else if (String.class.equals(parameterType)) {
							values[i] = stringMap.get(message.readInt());
						} else { // reference types
							throw new ConnectorDataTransmissionException("References are not yet supported.");
						}
					}

					return lookupEntity.getConstructor().newInstance(values);
				} else {
					throw new ConnectorDataTransmissionException("Record type not specified.");
				}
			}
		} catch (final InstantiationException e) {
			throw new ConnectorDataTransmissionException("Instantiation error", e);
		} catch (final IllegalAccessException e) {
			throw new ConnectorDataTransmissionException("Access to fields are restricted", e);
		} catch (final IllegalArgumentException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		} catch (final InvocationTargetException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		} catch (final JMSException e) {
			throw new ConnectorDataTransmissionException(e.getMessage(), e);
		}
	}
}
