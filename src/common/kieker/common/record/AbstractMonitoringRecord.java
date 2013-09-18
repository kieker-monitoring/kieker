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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import kieker.common.exception.MonitoringRecordException;
import kieker.common.util.registry.IRegistry;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.2
 */
public abstract class AbstractMonitoringRecord implements IMonitoringRecord {
	private static final long serialVersionUID = 1L;
	private static final ConcurrentMap<String, Class<? extends IMonitoringRecord>> OLD_KIEKERRECORDS =
			new ConcurrentHashMap<String, Class<? extends IMonitoringRecord>>();

	private volatile long loggingTimestamp = -1;

	static {
		OLD_KIEKERRECORDS.put("kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord", kieker.common.record.controlflow.OperationExecutionRecord.class);
		OLD_KIEKERRECORDS.put("kieker.common.record.CPUUtilizationRecord", kieker.common.record.system.CPUUtilizationRecord.class);
		OLD_KIEKERRECORDS.put("kieker.common.record.MemSwapUsageRecord", kieker.common.record.system.MemSwapUsageRecord.class);
		OLD_KIEKERRECORDS.put("kieker.common.record.ResourceUtilizationRecord", kieker.common.record.system.ResourceUtilizationRecord.class);
		OLD_KIEKERRECORDS.put("kieker.common.record.OperationExecutionRecord", kieker.common.record.controlflow.OperationExecutionRecord.class);
		OLD_KIEKERRECORDS.put("kieker.common.record.BranchingRecord", kieker.common.record.controlflow.BranchingRecord.class);
		OLD_KIEKERRECORDS.put("kieker.monitoring.core.registry.RegistryRecord", kieker.common.record.misc.RegistryRecord.class);
		OLD_KIEKERRECORDS.put("kieker.common.record.flow.trace.Trace", kieker.common.record.flow.trace.TraceMetadata.class);
	}

	public final long getLoggingTimestamp() {
		return this.loggingTimestamp;
	}

	public final void setLoggingTimestamp(final long timestamp) {
		this.loggingTimestamp = timestamp;
	}

	@Override
	public final String toString() {
		final Object[] recordVector = this.toArray();
		final StringBuilder sb = new StringBuilder();
		sb.append(this.loggingTimestamp);
		for (final Object curStr : recordVector) {
			sb.append(';');
			if (curStr != null) {
				sb.append(curStr.toString());
			} else {
				sb.append("null");
			}
		}
		return sb.toString();
	}

	/**
	 * Provides an ordering of IMonitoringRecords by the loggingTimestamp.
	 * Classes overriding the implementation should respect this ordering. (see #326)
	 * 
	 * @param otherRecord
	 *            The record to be compared.
	 * 
	 * @return -1 if this object is less than, +1 if it is greater than or 0 if it is equal to the specified record.
	 */
	public int compareTo(final IMonitoringRecord otherRecord) {
		final long timedifference = this.loggingTimestamp - otherRecord.getLoggingTimestamp();
		if (timedifference < 0L) {
			return -1;
		} else if (timedifference > 0L) {
			return 1;
		} else { // same timing
			// this should work except for rare hash collisions
			return this.hashCode() - otherRecord.hashCode();
		}
	}

	@Override
	public final boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		} else if (this == obj) {
			return true;
		} else if (obj.getClass() != this.getClass()) {
			return false;
		} else if (this.loggingTimestamp != ((AbstractMonitoringRecord) obj).getLoggingTimestamp()) {
			return false;
		}
		return Arrays.equals(((AbstractMonitoringRecord) obj).toArray(), this.toArray());
	}

	@Override
	public final int hashCode() {
		return (31 * Arrays.hashCode(this.toArray())) + (int) (this.loggingTimestamp ^ (this.loggingTimestamp >>> 32));
	}

	/**
	 * This method checks the given arrays, making sure that they have the same length and that the value elements are compatible with the given value types. If the
	 * arrays are not compatible, this method throws an {@link IllegalArgumentException}.
	 * 
	 * @param values
	 *            The values.
	 * @param valueTypes
	 *            The value types.
	 */
	public static final void checkArray(final Object[] values, final Class<?>[] valueTypes) {
		if (values.length != valueTypes.length) {
			throw new IllegalArgumentException("Expecting array with " + valueTypes.length + " elements but found " + values.length + " elements.");
		}
		for (int curIdx = 0; curIdx < valueTypes.length; curIdx++) {
			if (values[curIdx] == null) {
				throw new IllegalArgumentException("Expecting " + valueTypes[curIdx].getName() + " but found null at position " + curIdx + " of the array.");
			} else if ((valueTypes[curIdx] == int.class) || (valueTypes[curIdx] == Integer.class)) {
				if (values[curIdx] instanceof Integer) {
					continue;
				}
			} else if ((valueTypes[curIdx] == long.class) || (valueTypes[curIdx] == Long.class)) {
				if (values[curIdx] instanceof Long) {
					continue;
				}
			} else if ((valueTypes[curIdx] == float.class) || (valueTypes[curIdx] == Float.class)) {
				if (values[curIdx] instanceof Float) {
					continue;
				}
			} else if ((valueTypes[curIdx] == double.class) || (valueTypes[curIdx] == Double.class)) {
				if (values[curIdx] instanceof Double) {
					continue;
				}
			} else if ((valueTypes[curIdx] == byte.class) || (valueTypes[curIdx] == Byte.class)) {
				if (values[curIdx] instanceof Byte) {
					continue;
				}
			} else if ((valueTypes[curIdx] == short.class) || (valueTypes[curIdx] == Short.class)) { // NOPMD (short)
				if (values[curIdx] instanceof Short) {
					continue;
				}
			} else if ((valueTypes[curIdx] == boolean.class) || (valueTypes[curIdx] == Boolean.class)) {
				if (values[curIdx] instanceof Boolean) {
					continue;
				}
			} else if (valueTypes[curIdx].equals(values[curIdx].getClass())) {
				continue;
			}
			throw new IllegalArgumentException("Expecting " + valueTypes[curIdx].getName() + " but found " + values[curIdx].getClass().getName()
					+ " at position " + curIdx + " of the array.");
		}
	}

	/**
	 * This helper method converts the given array with string objects into an array containing objects from the specified type. (e.g. via the {@code valueOf}
	 * methods).
	 * 
	 * @param recordFields
	 *            The array containing the string objects.
	 * @param valueTypes
	 *            The array containing the types the new array will have.
	 * 
	 * @return An array of objects, converted from the given string array.
	 * 
	 * @throws IllegalArgumentException
	 *             If one or more of the given types are not supported.
	 */
	public static final Object[] fromStringArrayToTypedArray(final String[] recordFields, final Class<?>[] valueTypes) throws IllegalArgumentException {
		if (recordFields.length != valueTypes.length) {
			throw new IllegalArgumentException("Expected " + valueTypes.length + " record fields, but found " + recordFields.length);
		}
		final Object[] typedArray = new Object[recordFields.length];
		for (int curIdx = 0; curIdx < typedArray.length; curIdx++) {
			if (valueTypes[curIdx] == String.class) {
				typedArray[curIdx] = recordFields[curIdx];
				continue;
			}
			if ((valueTypes[curIdx] == int.class) || (valueTypes[curIdx] == Integer.class)) {
				typedArray[curIdx] = Integer.valueOf(recordFields[curIdx]);
				continue;
			}
			if ((valueTypes[curIdx] == long.class) || (valueTypes[curIdx] == Long.class)) {
				typedArray[curIdx] = Long.valueOf(recordFields[curIdx]);
				continue;
			}
			if ((valueTypes[curIdx] == float.class) || (valueTypes[curIdx] == Float.class)) {
				typedArray[curIdx] = Float.valueOf(recordFields[curIdx]);
				continue;
			}
			if ((valueTypes[curIdx] == double.class) || (valueTypes[curIdx] == Double.class)) {
				typedArray[curIdx] = Double.valueOf(recordFields[curIdx]);
				continue;
			}
			if ((valueTypes[curIdx] == byte.class) || (valueTypes[curIdx] == Byte.class)) {
				typedArray[curIdx] = Byte.valueOf(recordFields[curIdx]);
				continue;
			}
			if ((valueTypes[curIdx] == short.class) || (valueTypes[curIdx] == Short.class)) { // NOPMD (short)
				typedArray[curIdx] = Short.valueOf(recordFields[curIdx]); // NOPMD (short)
				continue;
			}
			if ((valueTypes[curIdx] == boolean.class) || (valueTypes[curIdx] == Boolean.class)) {
				typedArray[curIdx] = Boolean.valueOf(recordFields[curIdx]);
				continue;
			}
			throw new IllegalArgumentException("Unsupported type: " + valueTypes[curIdx].getName());
		}
		return typedArray;
	}

	/**
	 * This method tries to find a monitoring record class with the given name.
	 * 
	 * @param classname
	 *            The name of the class.
	 * 
	 * @return A {@link Class} instance corresponding to the given name, if it exists.
	 * 
	 * @throws MonitoringRecordException
	 *             If either a class with the given name could not be found or if the class doesn't implement {@link IMonitoringRecord}.
	 */
	public static final Class<? extends IMonitoringRecord> classForName(final String classname) throws MonitoringRecordException {
		final Class<? extends IMonitoringRecord> clazz = OLD_KIEKERRECORDS.get(classname);
		if (clazz != null) {
			return clazz;
		} else {
			try {
				return Class.forName(classname).asSubclass(IMonitoringRecord.class);
			} catch (final ClassNotFoundException ex) {
				throw new MonitoringRecordException("Failed to get record type of name " + classname, ex);
			} catch (final ClassCastException ex) {
				throw new MonitoringRecordException("Failed to get record type of name " + classname, ex);
			}
		}
	}

	/**
	 * This method delivers the types array of the given class, either by finding the declared field (in case of a factory record) or via the {@code getValueTypes}
	 * method.
	 * 
	 * @param clazz
	 *            The record class.
	 * 
	 * @return The value types of the specified record.
	 * 
	 * @throws MonitoringRecordException
	 *             If this method failed to access the value types.
	 */
	public static final Class<?>[] typesForClass(final Class<? extends IMonitoringRecord> clazz) throws MonitoringRecordException {
		try {
			if (IMonitoringRecord.Factory.class.isAssignableFrom(clazz)) {
				final Field types = clazz.getDeclaredField("TYPES");
				return ((Class<?>[]) types.get(null));
			} else {
				return clazz.newInstance().getValueTypes();
			}
		} catch (final SecurityException ex) {
			throw new MonitoringRecordException("Failed to get types for monitoring record of type " + clazz.getName(), ex);
		} catch (final NoSuchFieldException ex) {
			throw new MonitoringRecordException("Failed to get types for monitoring record of type " + clazz.getName(), ex);
		} catch (final IllegalArgumentException ex) {
			throw new MonitoringRecordException("Failed to get types for monitoring record of type " + clazz.getName(), ex);
		} catch (final IllegalAccessException ex) {
			throw new MonitoringRecordException("Failed to get types for monitoring record of type " + clazz.getName(), ex);
		} catch (final InstantiationException ex) {
			throw new MonitoringRecordException("Failed to get types for monitoring record of type " + clazz.getName(), ex);
		}
	}

	/**
	 * This method creates a new monitoring record from the given data.
	 * 
	 * @param clazz
	 *            The class of the monitoring record.
	 * @param values
	 *            The array which will be used to initialize the fields of the record.
	 * 
	 * @return An initialized record instance.
	 * 
	 * @throws MonitoringRecordException
	 *             If this method failed to create the record for some reason.
	 */
	public static final IMonitoringRecord createFromArray(final Class<? extends IMonitoringRecord> clazz, final Object[] values) throws MonitoringRecordException {
		try {
			if (IMonitoringRecord.Factory.class.isAssignableFrom(clazz)) {
				// Factory interface present
				final Constructor<? extends IMonitoringRecord> constructor = clazz.getConstructor(Object[].class);
				return constructor.newInstance((Object) values);
			} else {
				// try ordinary method
				final IMonitoringRecord record = clazz.newInstance();
				record.initFromArray(values);
				return record;
			}
		} catch (final SecurityException ex) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getName(), ex);
		} catch (final NoSuchMethodException ex) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getName(), ex);
		} catch (final IllegalArgumentException ex) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getName(), ex);
		} catch (final InstantiationException ex) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getName(), ex);
		} catch (final IllegalAccessException ex) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getName(), ex);
		} catch (final InvocationTargetException ex) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getName(), ex);
		}
	}

	public static final IMonitoringRecord createFromByteBuffer(final String clazzname, final ByteBuffer buffer, final IRegistry<String> stringRegistry)
			throws MonitoringRecordException, BufferUnderflowException {
		final Class<? extends IMonitoringRecord> clazz = AbstractMonitoringRecord.classForName(clazzname);
		try {
			if (IMonitoringRecord.BinaryFactory.class.isAssignableFrom(clazz)) {
				// Factory interface present
				final Constructor<? extends IMonitoringRecord> constructor = clazz.getConstructor(ByteBuffer.class, IRegistry.class);
				return constructor.newInstance(buffer, stringRegistry);
			} else {
				// try ordinary method
				final IMonitoringRecord record = clazz.newInstance();
				record.initFromBytes(buffer, stringRegistry);
				return record;
			}
		} catch (final SecurityException ex) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getName(), ex);
		} catch (final NoSuchMethodException ex) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getName(), ex);
		} catch (final IllegalArgumentException ex) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getName(), ex);
		} catch (final InstantiationException ex) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getName(), ex);
		} catch (final IllegalAccessException ex) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getName(), ex);
		} catch (final InvocationTargetException ex) {
			final Throwable cause = ex.getCause();
			if (cause instanceof BufferUnderflowException) {
				throw (BufferUnderflowException) cause;
			}
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getName(), ex);
		}
	}

	public static final IMonitoringRecord createFromStringArray(final Class<? extends IMonitoringRecord> clazz, final String[] values) throws
			MonitoringRecordException {
		try {
			if (IMonitoringRecord.Factory.class.isAssignableFrom(clazz)) {
				// Factory interface present
				final Constructor<? extends IMonitoringRecord> constructor = clazz.getConstructor(Object[].class);
				final Field types = clazz.getDeclaredField("TYPES");
				return constructor.newInstance((Object) AbstractMonitoringRecord.fromStringArrayToTypedArray(values, (Class<?>[]) types.get(null)));
			} else {
				// try ordinary method
				final IMonitoringRecord record = clazz.newInstance();
				record.initFromArray(AbstractMonitoringRecord.fromStringArrayToTypedArray(values, record.getValueTypes()));
				return record;
			}
		} catch (final SecurityException ex) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getName(), ex);
		} catch (final NoSuchMethodException ex) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getName(), ex);
		} catch (final NoSuchFieldException ex) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getName(), ex);
		} catch (final IllegalArgumentException ex) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getName(), ex);
		} catch (final InstantiationException ex) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getName(), ex);
		} catch (final IllegalAccessException ex) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getName(), ex);
		} catch (final InvocationTargetException ex) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getName(), ex);
		}
	}
}
