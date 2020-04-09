/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
package kieker.common.record.jvm;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.jvm.AbstractJVMRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;


/**
 * @author Nils Christian Ehmke
 * API compatibility: Kieker 1.15.0
 * 
 * @since 1.10
 */
public class GCRecord extends AbstractJVMRecord  {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // AbstractJVMRecord.timestamp
			 + TYPE_SIZE_STRING // AbstractJVMRecord.hostname
			 + TYPE_SIZE_STRING // AbstractJVMRecord.vmName
			 + TYPE_SIZE_STRING // GCRecord.gcName
			 + TYPE_SIZE_LONG // GCRecord.collectionCount
			 + TYPE_SIZE_LONG; // GCRecord.collectionTimeMS
	
	public static final Class<?>[] TYPES = {
		long.class, // AbstractJVMRecord.timestamp
		String.class, // AbstractJVMRecord.hostname
		String.class, // AbstractJVMRecord.vmName
		String.class, // GCRecord.gcName
		long.class, // GCRecord.collectionCount
		long.class, // GCRecord.collectionTimeMS
	};
	
	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"timestamp",
		"hostname",
		"vmName",
		"gcName",
		"collectionCount",
		"collectionTimeMS",
	};
	
	/** default constants. */
	public static final String GC_NAME = "";
	private static final long serialVersionUID = -314644197119857213L;
	
	/** property declarations. */
	private final String gcName;
	private final long collectionCount;
	private final long collectionTimeMS;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param hostname
	 *            hostname
	 * @param vmName
	 *            vmName
	 * @param gcName
	 *            gcName
	 * @param collectionCount
	 *            collectionCount
	 * @param collectionTimeMS
	 *            collectionTimeMS
	 */
	public GCRecord(final long timestamp, final String hostname, final String vmName, final String gcName, final long collectionCount, final long collectionTimeMS) {
		super(timestamp, hostname, vmName);
		this.gcName = gcName == null?"":gcName;
		this.collectionCount = collectionCount;
		this.collectionTimeMS = collectionTimeMS;
	}


	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public GCRecord(final IValueDeserializer deserializer) throws RecordInstantiationException {
		super(deserializer);
		this.gcName = deserializer.getString();
		this.collectionCount = deserializer.getLong();
		this.collectionTimeMS = deserializer.getLong();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp());
		serializer.putString(this.getHostname());
		serializer.putString(this.getVmName());
		serializer.putString(this.getGcName());
		serializer.putLong(this.getCollectionCount());
		serializer.putLong(this.getCollectionTimeMS());
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
		
		final GCRecord castedRecord = (GCRecord) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (this.getTimestamp() != castedRecord.getTimestamp()) {
			return false;
		}
		if (!this.getHostname().equals(castedRecord.getHostname())) {
			return false;
		}
		if (!this.getVmName().equals(castedRecord.getVmName())) {
			return false;
		}
		if (!this.getGcName().equals(castedRecord.getGcName())) {
			return false;
		}
		if (this.getCollectionCount() != castedRecord.getCollectionCount()) {
			return false;
		}
		if (this.getCollectionTimeMS() != castedRecord.getCollectionTimeMS()) {
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
		code += this.getHostname().hashCode();
		code += this.getVmName().hashCode();
		code += this.getGcName().hashCode();
		code += ((int)this.getCollectionCount());
		code += ((int)this.getCollectionTimeMS());
		
		return code;
	}
	
	public final String getGcName() {
		return this.gcName;
	}
	
	
	public final long getCollectionCount() {
		return this.collectionCount;
	}
	
	
	public final long getCollectionTimeMS() {
		return this.collectionTimeMS;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "GCRecord: ";
		result += "timestamp = ";
		result += this.getTimestamp() + ", ";
		
		result += "hostname = ";
		result += this.getHostname() + ", ";
		
		result += "vmName = ";
		result += this.getVmName() + ", ";
		
		result += "gcName = ";
		result += this.getGcName() + ", ";
		
		result += "collectionCount = ";
		result += this.getCollectionCount() + ", ";
		
		result += "collectionTimeMS = ";
		result += this.getCollectionTimeMS() + ", ";
		
		return result;
	}
}
