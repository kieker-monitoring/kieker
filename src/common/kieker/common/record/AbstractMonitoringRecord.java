/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import kieker.common.exception.MonitoringRecordException;

/**
 * @author Andre van Hoorn, Jan Waller
 */
public abstract class AbstractMonitoringRecord implements IMonitoringRecord {
	private static final long serialVersionUID = 1L;

	private static final ConcurrentMap<String, Class<? extends IMonitoringRecord>> OLD_KIEKERRECORDS = new ConcurrentHashMap<String, Class<? extends IMonitoringRecord>>();
	static {
		AbstractMonitoringRecord.OLD_KIEKERRECORDS.put("kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord", OperationExecutionRecord.class);
	}

	private volatile long loggingTimestamp = -1;

	@Override
	public final long getLoggingTimestamp() {
		return this.loggingTimestamp;
	}

	@Override
	public final void setLoggingTimestamp(final long timestamp) {
		this.loggingTimestamp = timestamp;
	}

	@Override
	public final String toString() {
		final Object[] recordVector = this.toArray();
		final StringBuilder sb = new StringBuilder();
		sb.append(this.loggingTimestamp);
		for (final Object curStr : recordVector) {
			sb.append(";");
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

	@Override
	public final boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		if (this.loggingTimestamp != ((AbstractMonitoringRecord) obj).getLoggingTimestamp()) {
			return false;
		}
		return Arrays.equals(((AbstractMonitoringRecord) obj).toArray(), this.toArray());
	}

	@Override
	public final int hashCode() {
		return (31 * Arrays.hashCode(this.toArray())) + (int) (this.loggingTimestamp ^ (this.loggingTimestamp >>> 32)); // NOCS (magic number)
	}

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
			if ((valueTypes[curIdx] == short.class) || (valueTypes[curIdx] == Short.class)) { // NOPMD
				typedArray[curIdx] = Short.valueOf(recordFields[curIdx]); // NOPMD
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

	public static final Class<? extends IMonitoringRecord> getClass(final String classname) throws MonitoringRecordException {
		final Class<? extends IMonitoringRecord> clazz = AbstractMonitoringRecord.OLD_KIEKERRECORDS.get(classname);
		if (clazz != null) {
			return clazz;
		} else {
			try {
				return Class.forName(classname).asSubclass(IMonitoringRecord.class);
			} catch (final Exception ex) {
				throw new MonitoringRecordException("Failed to get record of name " + classname, ex);
			}
		}
	}

	public static final IMonitoringRecord createFromStringArray(final Class<? extends IMonitoringRecord> clazz, final String[] values)
			throws MonitoringRecordException {
		if (IMonitoringRecord.Factory.class.isAssignableFrom(clazz)) {
			// Factory interface present
			try {
				final Constructor<? extends IMonitoringRecord> constructor = clazz.getConstructor(Object[].class);
				final Class<?>[] typeArray = (Class<?>[]) clazz.getDeclaredField("TYPES").get(null);
				return constructor.newInstance((Object) AbstractMonitoringRecord.fromStringArrayToTypedArray(values, typeArray));
			} catch (final Exception ex) {
				throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getName(), ex);
			}
		} else {
			// try ordinary method
			try {
				final IMonitoringRecord record = clazz.newInstance();
				record.initFromArray(AbstractMonitoringRecord.fromStringArrayToTypedArray(values, record.getValueTypes()));
				return record;
			} catch (final Exception ex) {
				throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getName(), ex);
			}
		}
	}
}
