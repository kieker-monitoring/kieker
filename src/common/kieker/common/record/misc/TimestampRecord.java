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

package kieker.common.record.misc;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;

/**
 * Record type which can be used to store a timestamp.
 * 
 * @author Andre van Hoorn
 */
public final class TimestampRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory {
	private static final long serialVersionUID = -7166224794391621087L;
	private static final Class<?>[] TYPES = {
		long.class, // timestamp
	};

	private volatile long timestamp = -1;

	/**
	 * Constructs a new {@link TimestampRecord} with the
	 * without setting the current time value.
	 */
	public TimestampRecord() {
		// nothing to do
	};

	public TimestampRecord(final long timestamp) {
		this.timestamp = timestamp;
	}

	public TimestampRecord(final Object[] values) {
		final Object[] myValues = values.clone();
		AbstractMonitoringRecord.checkArray(myValues, TYPES);
		try {
			this.timestamp = (Long) myValues[0];
		} catch (final Exception exc) { // NOPMD NOCS (IllegalCatchCheck)
			throw new IllegalArgumentException("Failed to init", exc);
		}
	}

	public Object[] toArray() {
		return new Object[] { this.timestamp, };
	}

	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	public Class<?>[] getValueTypes() {
		return TYPES.clone();
	}

	/**
	 * Returns the current time.
	 * 
	 * @return the current time.
	 * @deprecated will be removed in Kieker 1.6; use {@link #getTimestamp()}
	 */
	@Deprecated
	public long getCurrentTime() {
		return this.timestamp;
	}

	/**
	 * @return the timestamp
	 */
	public final long getTimestamp() {
		return this.timestamp;
	}

	/**
	 * Sets the current time to the given value.
	 * 
	 * @param currentTime
	 * @deprecated will be removed in Kieker 1.6; record will become immutable
	 */
	@Deprecated
	public void setCurrentTime(final long currentTime) {
		this.timestamp = currentTime;
	}
}
