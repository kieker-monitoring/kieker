/***************************************************************************
* Copyright 2018 Kieker Project (http://kieker-monitoring.net)
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE2.0
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
 * API compatibility: Kieker 1.14.0
 * 
 * @since 1.10
 */
public class ClassLoadingRecord extends AbstractJVMRecord  {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // AbstractJVMRecord.timestamp
			 + TYPE_SIZE_STRING // AbstractJVMRecord.hostname
			 + TYPE_SIZE_STRING // AbstractJVMRecord.vmName
			 + TYPE_SIZE_LONG // ClassLoadingRecord.totalLoadedClassCount
			 + TYPE_SIZE_INT // ClassLoadingRecord.loadedClassCount
			 + TYPE_SIZE_LONG; // ClassLoadingRecord.unloadedClassCount
	
	public static final Class<?>[] TYPES = {
		long.class, // AbstractJVMRecord.timestamp
		String.class, // AbstractJVMRecord.hostname
		String.class, // AbstractJVMRecord.vmName
		long.class, // ClassLoadingRecord.totalLoadedClassCount
		int.class, // ClassLoadingRecord.loadedClassCount
		long.class, // ClassLoadingRecord.unloadedClassCount
	};
	
	private static final long serialVersionUID = -5955568375346711225L;
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"timestamp",
		"hostname",
		"vmName",
		"totalLoadedClassCount",
		"loadedClassCount",
		"unloadedClassCount",
	};
	
	/** property declarations. */
	private final long totalLoadedClassCount;
	private final int loadedClassCount;
	private final long unloadedClassCount;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param hostname
	 *            hostname
	 * @param vmName
	 *            vmName
	 * @param totalLoadedClassCount
	 *            totalLoadedClassCount
	 * @param loadedClassCount
	 *            loadedClassCount
	 * @param unloadedClassCount
	 *            unloadedClassCount
	 */
	public ClassLoadingRecord(final long timestamp, final String hostname, final String vmName, final long totalLoadedClassCount, final int loadedClassCount, final long unloadedClassCount) {
		super(timestamp, hostname, vmName);
		this.totalLoadedClassCount = totalLoadedClassCount;
		this.loadedClassCount = loadedClassCount;
		this.unloadedClassCount = unloadedClassCount;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 *
	 * @deprecated to be removed 1.15
	 */
	@Deprecated
	public ClassLoadingRecord(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
		this.totalLoadedClassCount = (Long) values[3];
		this.loadedClassCount = (Integer) values[4];
		this.unloadedClassCount = (Long) values[5];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated to be removed 1.15
	 */
	@Deprecated
	protected ClassLoadingRecord(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.totalLoadedClassCount = (Long) values[3];
		this.loadedClassCount = (Integer) values[4];
		this.unloadedClassCount = (Long) values[5];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public ClassLoadingRecord(final IValueDeserializer deserializer) throws RecordInstantiationException {
		super(deserializer);
		this.totalLoadedClassCount = deserializer.getLong();
		this.loadedClassCount = deserializer.getInt();
		this.unloadedClassCount = deserializer.getLong();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @deprecated to be removed in 1.15
	 */
	@Override
	@Deprecated
	public Object[] toArray() {
		return new Object[] {
			this.getTimestamp(),
			this.getHostname(),
			this.getVmName(),
			this.getTotalLoadedClassCount(),
			this.getLoadedClassCount(),
			this.getUnloadedClassCount(),
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putLong(this.getTimestamp());
		serializer.putString(this.getHostname());
		serializer.putString(this.getVmName());
		serializer.putLong(this.getTotalLoadedClassCount());
		serializer.putInt(this.getLoadedClassCount());
		serializer.putLong(this.getUnloadedClassCount());
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
		return PROPERTY_NAMES; // NOPMD
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
	 * @deprecated to be rmeoved in 1.15
	 */
	@Override
	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
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
		
		final ClassLoadingRecord castedRecord = (ClassLoadingRecord) obj;
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
		if (this.getTotalLoadedClassCount() != castedRecord.getTotalLoadedClassCount()) {
			return false;
		}
		if (this.getLoadedClassCount() != castedRecord.getLoadedClassCount()) {
			return false;
		}
		if (this.getUnloadedClassCount() != castedRecord.getUnloadedClassCount()) {
			return false;
		}
		
		return true;
	}
	
	public final long getTotalLoadedClassCount() {
		return this.totalLoadedClassCount;
	}
	
	
	public final int getLoadedClassCount() {
		return this.loadedClassCount;
	}
	
	
	public final long getUnloadedClassCount() {
		return this.unloadedClassCount;
	}
	
}
