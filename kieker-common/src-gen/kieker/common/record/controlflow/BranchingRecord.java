/***************************************************************************
 * Copyright 2016 Kieker Project (http://kieker-monitoring.net)
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
import kieker.common.util.Version;


/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.2
 */
public class BranchingRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // BranchingRecord.timestamp
			 + TYPE_SIZE_INT // BranchingRecord.branchID
			 + TYPE_SIZE_INT // BranchingRecord.branchingOutcome
	;
	private static final long serialVersionUID = -2795856778019853382L;
	
	public static final Class<?>[] TYPES = {
		long.class, // BranchingRecord.timestamp
		int.class, // BranchingRecord.branchID
		int.class, // BranchingRecord.branchingOutcome
	};
	
	/* user-defined constants */
	/* default constants */
	public static final long TIMESTAMP = 0L;
	public static final int BRANCH_ID = 0;
	public static final int BRANCHING_OUTCOME = 0;
	/* property declarations */
	private final long timestamp;
	private final int branchID;
	private final int branchingOutcome;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param branchID
	 *            branchID
	 * @param branchingOutcome
	 *            branchingOutcome
	 */
	public BranchingRecord(final long timestamp, final int branchID, final int branchingOutcome) {
		this.timestamp = timestamp;
		this.branchID = branchID;
		this.branchingOutcome = branchingOutcome;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
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
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected BranchingRecord(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
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
	public BranchingRecord(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.timestamp = buffer.getLong();
		this.branchID = buffer.getInt();
		this.branchingOutcome = buffer.getInt();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getTimestamp(),
			this.getBranchID(),
			this.getBranchingOutcome()
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putLong(this.getTimestamp());
		buffer.putInt(this.getBranchID());
		buffer.putInt(this.getBranchingOutcome());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?>[] getValueTypes() {
		return TYPES; // NOPMD
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSize() {
		return SIZE;
	}
	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.BinaryFactory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (obj.getClass() != this.getClass()) return false;
		
		final BranchingRecord castedRecord = (BranchingRecord) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTimestamp() != castedRecord.getTimestamp()) return false;
		if (this.getBranchID() != castedRecord.getBranchID()) return false;
		if (this.getBranchingOutcome() != castedRecord.getBranchingOutcome()) return false;
		return true;
	}

	public final long getTimestamp() {
		return this.timestamp;
	}
	
	public final int getBranchID() {
		return this.branchID;
	}
	
	public final int getBranchingOutcome() {
		return this.branchingOutcome;
	}
	
}
