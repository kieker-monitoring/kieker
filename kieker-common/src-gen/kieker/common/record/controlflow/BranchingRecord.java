/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;


/**
 * @author Andre van Hoorn, Jan Waller
 * API compatibility: Kieker 1.15.0
 * 
 * @since 1.2
 */
public class BranchingRecord extends AbstractMonitoringRecord  {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // BranchingRecord.timestamp
			 + TYPE_SIZE_INT // BranchingRecord.branchID
			 + TYPE_SIZE_INT; // BranchingRecord.branchingOutcome
	
	public static final Class<?>[] TYPES = {
		long.class, // BranchingRecord.timestamp
		int.class, // BranchingRecord.branchID
		int.class, // BranchingRecord.branchingOutcome
	};
	
	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"timestamp",
		"branchID",
		"branchingOutcome",
	};
	
	/** default constants. */
	public static final long TIMESTAMP = 0L;
	public static final int BRANCH_ID = 0;
	public static final int BRANCHING_OUTCOME = 0;
	private static final long serialVersionUID = 3957750090047819946L;
	
	/** property declarations. */
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
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public BranchingRecord(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.timestamp = deserializer.getLong();
		this.branchID = deserializer.getInt();
		this.branchingOutcome = deserializer.getInt();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp());
		serializer.putInt(this.getBranchID());
		serializer.putInt(this.getBranchingOutcome());
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
	public String[] getValueNames() {
		return VALUE_NAMES; // NOPMD
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
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		
		final BranchingRecord castedRecord = (BranchingRecord) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (this.getTimestamp() != castedRecord.getTimestamp()) {
			return false;
		}
		if (this.getBranchID() != castedRecord.getBranchID()) {
			return false;
		}
		if (this.getBranchingOutcome() != castedRecord.getBranchingOutcome()) {
			return false;
		}
		
		return true;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int code = 0;
		code += ((int)this.getTimestamp());
		code += ((int)this.getBranchID());
		code += ((int)this.getBranchingOutcome());
		
		return code;
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
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "BranchingRecord: ";
		result += "timestamp = ";
		result += this.getTimestamp() + ", ";
		
		result += "branchID = ";
		result += this.getBranchID() + ", ";
		
		result += "branchingOutcome = ";
		result += this.getBranchingOutcome() + ", ";
		
		return result;
	}
}
