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
 * Record type which can be used to store the current time 
 * in the field {@link #currentTime}.
 * 
 * @author Andre van Hoorn
 */
public class CurrentTimeRecord extends AbstractMonitoringRecord {

    private static final long serialVersionUID = 112213L;
    private static int numRecordFields = 1;
    private volatile long currentTime = -1;

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

	/**
	 * Constructs a new {@link CurrentTimeRecord} with the 
	 * without setting the current time value. 
	 */
	public CurrentTimeRecord () { };

    public CurrentTimeRecord (final long timestamp) {
        this.currentTime = timestamp;
    }

    @Override
	public Class<?>[] getValueTypes() {
        return new Class[] {
          long.class, // timestamp
        };
    }

    @Override
	public void initFromArray(final Object[] values) throws IllegalArgumentException { // NOPMD by jwa on 20.09.11 14:24
        try {
            if (values.length != CurrentTimeRecord.numRecordFields) {
                throw new IllegalArgumentException("Expecting vector with "
                        + CurrentTimeRecord.numRecordFields + " elements but found:" + values.length);
            }
            this.currentTime = (Long) values[0];
        } catch (final Exception exc) {
            throw new IllegalArgumentException("Failed to init", exc);
        }
    }

    @Override
	public Object[] toArray() {
        return new Object[] {
            this.currentTime,
        };
    }
}
