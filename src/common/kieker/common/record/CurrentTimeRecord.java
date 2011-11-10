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
 * Record type which can be used to store the current time in the field {@link #currentTime}.
 * 
 * @author Andre van Hoorn
 */
public final class CurrentTimeRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory {
	private static final long serialVersionUID = -7166224794391621087L;
	private static final Class<?>[] TYPES = {
		long.class, // timestamp
	};

	private volatile long currentTime = -1;

	/**
	 * Constructs a new {@link CurrentTimeRecord} with the
	 * without setting the current time value.
	 */
	public CurrentTimeRecord() {
		// nothing to do
	};

	public CurrentTimeRecord(final long timestamp) {
		this.currentTime = timestamp;
	}

	public CurrentTimeRecord(final Object[] values) {
		AbstractMonitoringRecord.checkArray(values, CurrentTimeRecord.TYPES);
		try {
			this.currentTime = (Long) values[0];
		} catch (final Exception exc) { // NOCS (IllegalCatchCheck) // NOPMD
			throw new IllegalArgumentException("Failed to init", exc);
		}
	}

	@Override
	public Object[] toArray() {
		return new Object[] { this.currentTime, };
	}

	@Override
	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<?>[] getValueTypes() {
		return CurrentTimeRecord.TYPES.clone();
	}

	/**
	 * Returns the current time.
	 * 
	 * @return the current time.
	 */
	public long getCurrentTime() {
		return this.currentTime;
	}

	/**
	 * Sets the current time to the given value.
	 * 
	 * @param currentTime
	 */
	public void setCurrentTime(final long currentTime) {
		this.currentTime = currentTime;
	}
}
