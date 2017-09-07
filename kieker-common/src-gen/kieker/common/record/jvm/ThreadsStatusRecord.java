/***************************************************************************
 * Copyright 2017 iObserve Project
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

import kieker.common.record.jvm.AbstractJVMRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.util.registry.IRegistry;


/**
 * @author Nils Christian Ehmke
 * API compatibility: Kieker 1.13.0
 * 
 * @since 1.10
 */
public class ThreadsStatusRecord extends AbstractJVMRecord  {
	private static final long serialVersionUID = -9176980438135391329L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // AbstractJVMRecord.timestamp
			 + TYPE_SIZE_STRING // AbstractJVMRecord.hostname
			 + TYPE_SIZE_STRING // AbstractJVMRecord.vmName
			 + TYPE_SIZE_LONG // ThreadsStatusRecord.threadCount
			 + TYPE_SIZE_LONG // ThreadsStatusRecord.daemonThreadCount
			 + TYPE_SIZE_LONG // ThreadsStatusRecord.peakThreadCount
			 + TYPE_SIZE_LONG // ThreadsStatusRecord.totalStartedThreadCount
	;
	
	public static final Class<?>[] TYPES = {
		long.class, // AbstractJVMRecord.timestamp
		String.class, // AbstractJVMRecord.hostname
		String.class, // AbstractJVMRecord.vmName
		long.class, // ThreadsStatusRecord.threadCount
		long.class, // ThreadsStatusRecord.daemonThreadCount
		long.class, // ThreadsStatusRecord.peakThreadCount
		long.class, // ThreadsStatusRecord.totalStartedThreadCount
	};
	
	
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"timestamp",
		"hostname",
		"vmName",
		"threadCount",
		"daemonThreadCount",
		"peakThreadCount",
		"totalStartedThreadCount",
	};
	
	/** property declarations. */
	private final long threadCount;
	private final long daemonThreadCount;
	private final long peakThreadCount;
	private final long totalStartedThreadCount;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param hostname
	 *            hostname
	 * @param vmName
	 *            vmName
	 * @param threadCount
	 *            threadCount
	 * @param daemonThreadCount
	 *            daemonThreadCount
	 * @param peakThreadCount
	 *            peakThreadCount
	 * @param totalStartedThreadCount
	 *            totalStartedThreadCount
	 */
	public ThreadsStatusRecord(final long timestamp, final String hostname, final String vmName, final long threadCount, final long daemonThreadCount, final long peakThreadCount, final long totalStartedThreadCount) {
		super(timestamp, hostname, vmName);
		this.threadCount = threadCount;
		this.daemonThreadCount = daemonThreadCount;
		this.peakThreadCount = peakThreadCount;
		this.totalStartedThreadCount = totalStartedThreadCount;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 *
	 * @deprecated since 1.13. Use {@link #ThreadsStatusRecord(IValueDeserializer)} instead.
	 */
	@Deprecated
	public ThreadsStatusRecord(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
		this.threadCount = (Long) values[3];
		this.daemonThreadCount = (Long) values[4];
		this.peakThreadCount = (Long) values[5];
		this.totalStartedThreadCount = (Long) values[6];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated since 1.13. Use {@link #ThreadsStatusRecord(IValueDeserializer)} instead.
	 */
	@Deprecated
	protected ThreadsStatusRecord(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.threadCount = (Long) values[3];
		this.daemonThreadCount = (Long) values[4];
		this.peakThreadCount = (Long) values[5];
		this.totalStartedThreadCount = (Long) values[6];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 */
	public ThreadsStatusRecord(final IValueDeserializer deserializer) {
		super(deserializer);
		this.threadCount = deserializer.getLong();
		this.daemonThreadCount = deserializer.getLong();
		this.peakThreadCount = deserializer.getLong();
		this.totalStartedThreadCount = deserializer.getLong();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @deprecated since 1.13. Use {@link #serialize(IValueSerializer)} with an array serializer instead.
	 */
	@Override
	@Deprecated
	public Object[] toArray() {
		return new Object[] {
			this.getTimestamp(),
			this.getHostname(),
			this.getVmName(),
			this.getThreadCount(),
			this.getDaemonThreadCount(),
			this.getPeakThreadCount(),
			this.getTotalStartedThreadCount()
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
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putLong(this.getTimestamp());
		serializer.putString(this.getHostname());
		serializer.putString(this.getVmName());
		serializer.putLong(this.getThreadCount());
		serializer.putLong(this.getDaemonThreadCount());
		serializer.putLong(this.getPeakThreadCount());
		serializer.putLong(this.getTotalStartedThreadCount());
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
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (obj.getClass() != this.getClass()) return false;
		
		final ThreadsStatusRecord castedRecord = (ThreadsStatusRecord) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTimestamp() != castedRecord.getTimestamp()) return false;
		if (!this.getHostname().equals(castedRecord.getHostname())) return false;
		if (!this.getVmName().equals(castedRecord.getVmName())) return false;
		if (this.getThreadCount() != castedRecord.getThreadCount()) return false;
		if (this.getDaemonThreadCount() != castedRecord.getDaemonThreadCount()) return false;
		if (this.getPeakThreadCount() != castedRecord.getPeakThreadCount()) return false;
		if (this.getTotalStartedThreadCount() != castedRecord.getTotalStartedThreadCount()) return false;
		return true;
	}
	
	public final long getThreadCount() {
		return this.threadCount;
	}
	
	
	public final long getDaemonThreadCount() {
		return this.daemonThreadCount;
	}
	
	
	public final long getPeakThreadCount() {
		return this.peakThreadCount;
	}
	
	
	public final long getTotalStartedThreadCount() {
		return this.totalStartedThreadCount;
	}
	
}
