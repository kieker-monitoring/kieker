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

package kieker.common.record;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.BufferUnderflowException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import kieker.common.exception.MonitoringRecordException;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Andre van Hoorn, Jan Waller, Nils Christian Ehmke
 *
 * @since 1.2
 */
public abstract class AbstractMonitoringRecord implements IMonitoringRecord {

	public static final int TYPE_SIZE_INT = 4;
	public static final int TYPE_SIZE_LONG = 8;
	public static final int TYPE_SIZE_FLOAT = 4;
	public static final int TYPE_SIZE_DOUBLE = 8;
	public static final int TYPE_SIZE_SHORT = 2;
	public static final int TYPE_SIZE_BYTE = 1;
	public static final int TYPE_SIZE_CHARACTER = 2;
	public static final int TYPE_SIZE_STRING = 4;
	public static final int TYPE_SIZE_BOOLEAN = 1;

	private static final float VALID_VARIANCE_FLOAT = 0.00001f;
	private static final double VALID_VARIANCE_DOUBLE = 0.00001d;

	private static final String FAILED_TO_INSTANTIATE_NEW_MONITORING_RECORD_OF_TYPE = "Failed to instantiate new monitoring record of type ";
	private static final long serialVersionUID = 1L;

	private static final ConcurrentMap<String, Class<? extends IMonitoringRecord>> CACHED_KIEKERRECORDS = new ConcurrentHashMap<>(); // NOCS
	private static final ConcurrentMap<Class<? extends IMonitoringRecord>, Class<?>[]> CACHED_KIEKERRECORD_TYPES = new ConcurrentHashMap<>(); // NOCS

	// added by chw; differs only in the key type: from integer to string
	private static final ConcurrentMap<String, Constructor<? extends IMonitoringRecord>> CACHED_KIEKERRECORD_CONSTRUCTORS_BINARY_CHW = new ConcurrentHashMap<>(); // NOCS

	private volatile long loggingTimestamp = -1;

	static {
		CACHED_KIEKERRECORDS.put("kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord", kieker.common.record.controlflow.OperationExecutionRecord.class);
		CACHED_KIEKERRECORDS.put("kieker.common.record.CPUUtilizationRecord", kieker.common.record.system.CPUUtilizationRecord.class);
		CACHED_KIEKERRECORDS.put("kieker.common.record.MemSwapUsageRecord", kieker.common.record.system.MemSwapUsageRecord.class);
		CACHED_KIEKERRECORDS.put("kieker.common.record.ResourceUtilizationRecord", kieker.common.record.system.ResourceUtilizationRecord.class);
		CACHED_KIEKERRECORDS.put("kieker.common.record.OperationExecutionRecord", kieker.common.record.controlflow.OperationExecutionRecord.class);
		CACHED_KIEKERRECORDS.put("kieker.common.record.BranchingRecord", kieker.common.record.controlflow.BranchingRecord.class);
		CACHED_KIEKERRECORDS.put("kieker.monitoring.core.registry.RegistryRecord", kieker.common.record.misc.RegistryRecord.class);
		CACHED_KIEKERRECORDS.put("kieker.common.record.flow.trace.Trace", kieker.common.record.flow.trace.TraceMetadata.class);
	}

	/**
	 * Checks whether two double values are equal or not.
	 *
	 * @param x
	 *            first value
	 * @param y
	 *            second values
	 *
	 * @return returns true if the values differ
	 */
	protected static boolean isNotEqual(final double x, final double y) {
		final double diff = x - y;
		return (diff < (0.0D - VALID_VARIANCE_DOUBLE)) || (diff > VALID_VARIANCE_DOUBLE);
	}

	/**
	 * Checks whether two float values are equal or not.
	 *
	 * @param x
	 *            first value
	 * @param y
	 *            second values
	 *
	 * @return returns true if the values differ
	 */
	protected static boolean isNotEqual(final float x, final float y) {
		final float diff = x - y;
		return (diff < (0.0F - VALID_VARIANCE_FLOAT)) || (diff > VALID_VARIANCE_FLOAT);
	}

	@Override
	public final long getLoggingTimestamp() {
		return this.loggingTimestamp;
	}

	@Override
	public final void setLoggingTimestamp(final long timestamp) {
		this.loggingTimestamp = timestamp;
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
	@Override
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

	/**
	 * <p>
	 * Performs a null-check, a this-check, and a class-check. Moreover, it checks each attribute for equality.
	 * </p>
	 *
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj.getClass() != this.getClass()) {
			return false;
		}

		final AbstractMonitoringRecord castedRecord = (AbstractMonitoringRecord) obj;
		return this.getLoggingTimestamp() == castedRecord.getLoggingTimestamp();
	}

	@Override
	public int hashCode() {
		return Long.hashCode(this.getLoggingTimestamp());
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
			} else if (valueTypes[curIdx] == int.class) {
				if (values[curIdx] instanceof Integer) {
					continue;
				}
			} else if (valueTypes[curIdx] == long.class) {
				if (values[curIdx] instanceof Long) {
					continue;
				}
			} else if (valueTypes[curIdx] == String.class) {
				if (values[curIdx] instanceof String) {
					continue;
				}
			} else if (valueTypes[curIdx] == Integer.class) {
				if (values[curIdx] instanceof Integer) {
					continue;
				}
			} else if (valueTypes[curIdx] == Long.class) {
				if (values[curIdx] instanceof Long) {
					continue;
				}
			} else if (valueTypes[curIdx] == float.class) {
				if (values[curIdx] instanceof Float) {
					continue;
				}
			} else if (valueTypes[curIdx] == Float.class) {
				if (values[curIdx] instanceof Float) {
					continue;
				}
			} else if (valueTypes[curIdx] == double.class) {
				if (values[curIdx] instanceof Double) {
					continue;
				}
			} else if (valueTypes[curIdx] == Double.class) {
				if (values[curIdx] instanceof Double) {
					continue;
				}
			} else if (valueTypes[curIdx] == byte.class) {
				if (values[curIdx] instanceof Byte) {
					continue;
				}
			} else if (valueTypes[curIdx] == Byte.class) {
				if (values[curIdx] instanceof Byte) {
					continue;
				}
			} else if (valueTypes[curIdx] == short.class) { // NOPMD (short)
				if (values[curIdx] instanceof Short) {
					continue;
				}
			} else if (valueTypes[curIdx] == Short.class) {
				if (values[curIdx] instanceof Short) {
					continue;
				}
			} else if (valueTypes[curIdx] == boolean.class) {
				if (values[curIdx] instanceof Boolean) {
					continue;
				}
			} else if (valueTypes[curIdx] == Boolean.class) {
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
	public static final Object[] fromStringArrayToTypedArray2(final String[] recordFields, final Class<?>[] valueTypes) throws IllegalArgumentException {
		if (recordFields.length != valueTypes.length) {
			throw new IllegalArgumentException("Expected " + valueTypes.length + " record fields, but found " + recordFields.length);
		}
		final Object[] typedArray = new Object[recordFields.length]; // NOPMD
		for (int curIdx = 0; curIdx < typedArray.length; curIdx++) {
			if (valueTypes[curIdx] == String.class) {
				typedArray[curIdx] = recordFields[curIdx];
				continue;
			} else if (valueTypes[curIdx] == int.class) {
				typedArray[curIdx] = Integer.valueOf(recordFields[curIdx]);
				continue;
			} else if (valueTypes[curIdx] == long.class) {
				typedArray[curIdx] = Long.valueOf(recordFields[curIdx]);
				continue;
			} else if (valueTypes[curIdx] == Integer.class) {
				typedArray[curIdx] = Integer.valueOf(recordFields[curIdx]);
				continue;
			} else if (valueTypes[curIdx] == Long.class) {
				typedArray[curIdx] = Long.valueOf(recordFields[curIdx]);
				continue;
			} else if (valueTypes[curIdx] == float.class) {
				typedArray[curIdx] = Float.valueOf(recordFields[curIdx]);
				continue;
			} else if (valueTypes[curIdx] == Float.class) {
				typedArray[curIdx] = Float.valueOf(recordFields[curIdx]);
				continue;
			} else if (valueTypes[curIdx] == double.class) {
				typedArray[curIdx] = Double.valueOf(recordFields[curIdx]);
				continue;
			} else if (valueTypes[curIdx] == Double.class) {
				typedArray[curIdx] = Double.valueOf(recordFields[curIdx]);
				continue;
			} else if (valueTypes[curIdx] == byte.class) {
				typedArray[curIdx] = Byte.valueOf(recordFields[curIdx]);
				continue;
			} else if (valueTypes[curIdx] == Byte.class) {
				typedArray[curIdx] = Byte.valueOf(recordFields[curIdx]);
				continue;
			} else if (valueTypes[curIdx] == short.class) { // NOPMD (short)
				typedArray[curIdx] = Short.valueOf(recordFields[curIdx]); // NOPMD (short)
				continue;
			} else if (valueTypes[curIdx] == Short.class) { // NOPMD (short)
				typedArray[curIdx] = Short.valueOf(recordFields[curIdx]); // NOPMD (short)
				continue;
			} else if (valueTypes[curIdx] == boolean.class) {
				typedArray[curIdx] = Boolean.valueOf(recordFields[curIdx]);
				continue;
			} else if (valueTypes[curIdx] == Boolean.class) {
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
		Class<? extends IMonitoringRecord> clazz = CACHED_KIEKERRECORDS.get(classname);
		if (clazz == null) {
			try {
				clazz = Class.forName(classname).asSubclass(IMonitoringRecord.class);
				CACHED_KIEKERRECORDS.putIfAbsent(classname, clazz);
			} catch (final ClassNotFoundException | ClassCastException ex) {
				throw new MonitoringRecordException("Failed to get record type of name " + classname, ex);
			}
		}
		return clazz;
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
	 * @throws InvocationTargetException
	 */
	public static final Class<?>[] typesForClass(final Class<? extends IMonitoringRecord> clazz) throws MonitoringRecordException {
		Class<?>[] types = CACHED_KIEKERRECORD_TYPES.get(clazz);
		if (types == null) {
			try {
				if (IMonitoringRecord.class.isAssignableFrom(clazz)) {
					final Field typesField = clazz.getDeclaredField("TYPES");
					types = (Class<?>[]) typesField.get(null);
				} else {
					types = clazz.getDeclaredConstructor().newInstance().getValueTypes();
				}
				CACHED_KIEKERRECORD_TYPES.putIfAbsent(clazz, types);
			} catch (final SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException | InstantiationException
					| InvocationTargetException | NoSuchMethodException ex) {
				throw new MonitoringRecordException("Failed to get types for monitoring record of type " + clazz.getName(), ex);
			}
		}
		return types;
	}

	/**
	 * Create monitoring record based on class name and a given value deserializer.
	 *
	 * @param recordClassName
	 *            record class name
	 * @param deserializer
	 *            the derserializer
	 * @return a monitoring record
	 * @throws BufferUnderflowException
	 *             on buffer read errors
	 * @throws MonitoringRecordException
	 *             when no record could be constructed
	 */
	public static final IMonitoringRecord createFromDeserializer(final String recordClassName, final IValueDeserializer deserializer)
			throws BufferUnderflowException, MonitoringRecordException {
		try {
			Constructor<? extends IMonitoringRecord> constructor = CACHED_KIEKERRECORD_CONSTRUCTORS_BINARY_CHW.get(recordClassName);

			if (constructor == null) {
				final Class<? extends IMonitoringRecord> clazz = AbstractMonitoringRecord.classForName(recordClassName);
				if (IMonitoringRecord.class.isAssignableFrom(clazz)) {
					// Factory interface present
					constructor = clazz.getConstructor(IValueDeserializer.class);
					CACHED_KIEKERRECORD_CONSTRUCTORS_BINARY_CHW.putIfAbsent(recordClassName, constructor);
				} else {
					// Init methods are no longer supported
					throw new MonitoringRecordException("No factory for record type " + recordClassName + ".");
				}
			}

			return constructor.newInstance(deserializer);
		} catch (final SecurityException | NoSuchMethodException | IllegalArgumentException | InstantiationException | IllegalAccessException ex) {
			throw new MonitoringRecordException(FAILED_TO_INSTANTIATE_NEW_MONITORING_RECORD_OF_TYPE + recordClassName, ex);
		} catch (final InvocationTargetException ex) {
			final Throwable cause = ex.getCause();
			if (cause instanceof BufferUnderflowException) {
				throw (BufferUnderflowException) cause;
			}
			throw new MonitoringRecordException(FAILED_TO_INSTANTIATE_NEW_MONITORING_RECORD_OF_TYPE + recordClassName, ex);
		}
	}

}
