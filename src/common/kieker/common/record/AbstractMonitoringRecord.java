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

/**
 * @author Andre van Hoorn, Jan Waller
 */
public abstract class AbstractMonitoringRecord implements IMonitoringRecord {
	private static final long serialVersionUID = 1L;

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

	@Override
	public final int compareTo(final IMonitoringRecord otherRecord) {
		final long timedifference = this.loggingTimestamp - otherRecord.getLoggingTimestamp();
		if (timedifference < Integer.MIN_VALUE) {
			return Integer.MIN_VALUE;
		} else if (timedifference > Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		} else {
			return (int) timedifference;
		}
	}

	public static final Object[] fromStringArrayToTypedArray(final String[] recordFields, final Class<?>[] valueTypes) throws IllegalArgumentException {
		if (recordFields.length != valueTypes.length) {
			throw new IllegalArgumentException("Expected " + valueTypes.length + " record fields, but found " + recordFields.length);
		}
		final Object[] typedArray = new Object[recordFields.length];
		int curIdx = -1;
		for (final Class<?> clazz : valueTypes) {
			curIdx++;
			if (clazz == String.class) {
				typedArray[curIdx] = recordFields[curIdx];
				continue;
			}
			if ((clazz == int.class) || (clazz == Integer.class)) {
				typedArray[curIdx] = Integer.valueOf(recordFields[curIdx]);
				continue;
			}
			if ((clazz == long.class) || (clazz == Long.class)) {
				typedArray[curIdx] = Long.valueOf(recordFields[curIdx]);
				continue;
			}
			if ((clazz == float.class) || (clazz == Float.class)) {
				typedArray[curIdx] = Float.valueOf(recordFields[curIdx]);
				continue;
			}
			if ((clazz == double.class) || (clazz == Double.class)) {
				typedArray[curIdx] = Double.valueOf(recordFields[curIdx]);
				continue;
			}
			if ((clazz == byte.class) || (clazz == Byte.class)) {
				typedArray[curIdx] = Byte.valueOf(recordFields[curIdx]);
				continue;
			}
			if ((clazz == short.class) || (clazz == Short.class)) { // NOPMD
				typedArray[curIdx] = Short.valueOf(recordFields[curIdx]); // NOPMD
				continue;
			}
			if ((clazz == boolean.class) || (clazz == Boolean.class)) {
				typedArray[curIdx] = Boolean.valueOf(recordFields[curIdx]);
				continue;
			}
			throw new IllegalArgumentException("Unsupported type: " + clazz.getName());
		}
		return typedArray;
	}
}
