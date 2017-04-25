/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.jvm.AbstractJVMRecord;
import kieker.common.util.registry.IRegistry;


/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class ClassLoadingRecord extends AbstractJVMRecord  {
	private static final long serialVersionUID = -5955568375346711225L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // AbstractJVMRecord.timestamp
			 + TYPE_SIZE_STRING // AbstractJVMRecord.hostname
			 + TYPE_SIZE_STRING // AbstractJVMRecord.vmName
			 + TYPE_SIZE_LONG // ClassLoadingRecord.totalLoadedClassCount
			 + TYPE_SIZE_INT // ClassLoadingRecord.loadedClassCount
			 + TYPE_SIZE_LONG // ClassLoadingRecord.unloadedClassCount
	;
	
	public static final Class<?>[] TYPES = {
		long.class, // AbstractJVMRecord.timestamp
		String.class, // AbstractJVMRecord.hostname
		String.class, // AbstractJVMRecord.vmName
		long.class, // ClassLoadingRecord.totalLoadedClassCount
		int.class, // ClassLoadingRecord.loadedClassCount
		long.class, // ClassLoadingRecord.unloadedClassCount
	};
	
	
	
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
	private long totalLoadedClassCount;
	private int loadedClassCount;
	private long unloadedClassCount;
	
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
	 */
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
	 */
	protected ClassLoadingRecord(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.totalLoadedClassCount = (Long) values[3];
		this.loadedClassCount = (Integer) values[4];
		this.unloadedClassCount = (Long) values[5];
	}

	/**
	 * This constructor converts the given buffer into a record.
	 * 
	 * @param buffer
	 *            The bytes for the record
	 * @param stringRegistry
	 *            The string registry for deserialization
	 * 
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public ClassLoadingRecord(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		super(buffer, stringRegistry);
		this.totalLoadedClassCount = buffer.getLong();
		this.loadedClassCount = buffer.getInt();
		this.unloadedClassCount = buffer.getLong();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getTimestamp(),
			this.getHostname(),
			this.getVmName(),
			this.getTotalLoadedClassCount(),
			this.getLoadedClassCount(),
			this.getUnloadedClassCount()
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getHostname());
		stringRegistry.get(this.getVmName());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putLong(this.getTimestamp());
		buffer.putInt(stringRegistry.get(this.getHostname()));
		buffer.putInt(stringRegistry.get(this.getVmName()));
		buffer.putLong(this.getTotalLoadedClassCount());
		buffer.putInt(this.getLoadedClassCount());
		buffer.putLong(this.getUnloadedClassCount());
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
		
		final ClassLoadingRecord castedRecord = (ClassLoadingRecord) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTimestamp() != castedRecord.getTimestamp()) return false;
		if (!this.getHostname().equals(castedRecord.getHostname())) return false;
		if (!this.getVmName().equals(castedRecord.getVmName())) return false;
		if (this.getTotalLoadedClassCount() != castedRecord.getTotalLoadedClassCount()) return false;
		if (this.getLoadedClassCount() != castedRecord.getLoadedClassCount()) return false;
		if (this.getUnloadedClassCount() != castedRecord.getUnloadedClassCount()) return false;
		return true;
	}
	
	public final long getTotalLoadedClassCount() {
		return this.totalLoadedClassCount;
	}
	
	public final void setTotalLoadedClassCount(long totalLoadedClassCount) {
		this.totalLoadedClassCount = totalLoadedClassCount;
	}
	
	public final int getLoadedClassCount() {
		return this.loadedClassCount;
	}
	
	public final void setLoadedClassCount(int loadedClassCount) {
		this.loadedClassCount = loadedClassCount;
	}
	
	public final long getUnloadedClassCount() {
		return this.unloadedClassCount;
	}
	
	public final void setUnloadedClassCount(long unloadedClassCount) {
		this.unloadedClassCount = unloadedClassCount;
	}
}
