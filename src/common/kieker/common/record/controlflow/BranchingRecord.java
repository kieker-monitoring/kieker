/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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
 * @author Andre van Hoorn, Jan Waller
 */
public final class BranchingRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory {
	private static final long serialVersionUID = 1949567386494340839L;
	private static final Class<?>[] TYPES = {
		long.class, // timestamp
		int.class, // branchId
		int.class, // branchingOutcome
	};

	private final long timestamp;
	private final int branchID;
	private final int branchingOutcome;

	public BranchingRecord(final long timestamp, final int branchID, final int branchingOutcome) {
		this.timestamp = timestamp;
		this.branchID = branchID;
		this.branchingOutcome = branchingOutcome;
	}

	public BranchingRecord(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.timestamp = (Long) values[0];
		this.branchID = (Integer) values[1];
		this.branchingOutcome = (Integer) values[2];
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] toArray() {
		return new Object[] { this.timestamp, this.branchID, this.branchingOutcome, };
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
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
	 * @return the branchID
	 */
	public final int getBranchID() {
		return this.branchID;
	}

	/**
	 * @return the branchingOutcome
	 */
	public final int getBranchingOutcome() {
		return this.branchingOutcome;
	}
}
