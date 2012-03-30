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

package kieker.common.record.controlflow;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;

/**
 * @author Andre van Hoorn
 */
public final class BranchingRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory {
	private static final long serialVersionUID = -4569387448110349137L;
	private static final Class<?>[] TYPES = {
		long.class, // timestamp
		int.class, // branchId
		int.class, // branchingOutcome
	};

	private volatile long timestamp = -1;
	private volatile int branchID = -1;
	private volatile int branchingOutcome = -1;

	public BranchingRecord() {
		// nothing to do
	};

	public BranchingRecord(final long timestamp, final int branchID, final int branchingOutcome) {
		this.timestamp = timestamp;
		this.branchID = branchID;
		this.branchingOutcome = branchingOutcome;
	}

	public BranchingRecord(final Object[] values) {
		final Object[] myValues = values.clone();
		AbstractMonitoringRecord.checkArray(myValues, TYPES);
		try {
			this.timestamp = (Long) myValues[0];
			this.branchID = (Integer) myValues[1];
			this.branchingOutcome = (Integer) myValues[2];
		} catch (final Exception exc) { // NOPMD NOCS (IllegalCatchCheck)
			throw new IllegalArgumentException("Failed to init", exc);
		}
	}

	public Object[] toArray() {
		return new Object[] { this.timestamp, this.branchID, this.branchingOutcome };
	}

	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	public Class<?>[] getValueTypes() {
		return TYPES.clone();
	}

	/**
	 * @return the timestamp
	 */
	public final long getTimestamp() {
		return this.timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public final void setTimestamp(final long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the branchID
	 */
	public final int getBranchID() {
		return this.branchID;
	}

	/**
	 * @param branchID
	 *            the branchID to set
	 */
	public final void setBranchID(final int branchID) {
		this.branchID = branchID;
	}

	/**
	 * @return the branchingOutcome
	 */
	public final int getBranchingOutcome() {
		return this.branchingOutcome;
	}

	/**
	 * @param branchingOutcome
	 *            the branchingOutcome to set
	 */
	public final void setBranchingOutcome(final int branchingOutcome) {
		this.branchingOutcome = branchingOutcome;
	}
}
