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

package kieker.common.record.controlflow;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.2
 */
public class BranchingRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {
	public static final int SIZE = TYPE_SIZE_LONG + (2 * TYPE_SIZE_INT);
	public static final Class<?>[] TYPES = {
		long.class, // timestamp
		int.class, // branchId
		int.class, // branchingOutcome
	};

	private static final long serialVersionUID = 361952208062069707L;

	private final long timestamp;
	private final int branchID;
	private final int branchingOutcome;

	/**
	 * This constructor uses the given parameters to initialize the fields of the record.
	 * 
	 * @param timestamp
	 *            The time stamp.
	 * @param branchID
	 *            The branch ID.
	 * @param branchingOutcome
	 *            The branching outcome.
	 */
	public BranchingRecord(final long timestamp, final int branchID, final int branchingOutcome) {
		this.timestamp = timestamp;
		this.branchID = branchID;
		this.branchingOutcome = branchingOutcome;
	}

	/**
	 * This constructor converts the given array into a record. It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public BranchingRecord(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.timestamp = (Long) values[0];
		this.branchID = (Integer) values[1];
		this.branchingOutcome = (Integer) values[2];
	}

	/**
	 * This constructor converts the given array into a record.
	 * 
	 * @param buffer
	 *            The bytes for the record.
	 * 
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public BranchingRecord(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException { // NOPMD
		this.timestamp = buffer.getLong();
		this.branchID = buffer.getInt();
		this.branchingOutcome = buffer.getInt();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] toArray() {
		return new Object[] { this.getTimestamp(), this.getBranchID(), this.getBranchingOutcome(), };
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putLong(this.getTimestamp());
		buffer.putInt(this.getBranchID());
		buffer.putInt(this.getBranchingOutcome());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Deprecated
	public final void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.BinaryFactory} mechanism. Hence, this method is not implemented.
	 */
	@Deprecated
	public final void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?>[] getValueTypes() {
		return TYPES; // NOPMD
	}

	/**
	 * {@inheritDoc}
	 */
	public int getSize() {
		return SIZE;
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
