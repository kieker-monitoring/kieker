/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
 * API compatibility: Kieker 2.0.0
 * 
 * @since 1.10
 */
public class ThreadsStatusRecord extends AbstractJVMRecord  {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // AbstractJVMRecord.timestamp
			 + TYPE_SIZE_STRING // AbstractJVMRecord.hostname
			 + TYPE_SIZE_STRING // AbstractJVMRecord.vmName
			 + TYPE_SIZE_LONG // ThreadsStatusRecord.threadCount
			 + TYPE_SIZE_LONG // ThreadsStatusRecord.daemonThreadCount
			 + TYPE_SIZE_LONG // ThreadsStatusRecord.peakThreadCount
			 + TYPE_SIZE_LONG; // ThreadsStatusRecord.totalStartedThreadCount
	
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
	public static final String[] VALUE_NAMES = {
		"timestamp",
		"hostname",
		"vmName",
		"threadCount",
		"daemonThreadCount",
		"peakThreadCount",
		"totalStartedThreadCount",
	};
	
	private static final long serialVersionUID = -9176980438135391329L;
	
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
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public ThreadsStatusRecord(final IValueDeserializer deserializer) throws RecordInstantiationException {
		super(deserializer);
		this.threadCount = deserializer.getLong();
		this.daemonThreadCount = deserializer.getLong();
		this.peakThreadCount = deserializer.getLong();
		this.totalStartedThreadCount = deserializer.getLong();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
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
		
		final ThreadsStatusRecord castedRecord = (ThreadsStatusRecord) obj;
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
		if (this.getThreadCount() != castedRecord.getThreadCount()) {
			return false;
		}
		if (this.getDaemonThreadCount() != castedRecord.getDaemonThreadCount()) {
			return false;
		}
		if (this.getPeakThreadCount() != castedRecord.getPeakThreadCount()) {
			return false;
		}
		if (this.getTotalStartedThreadCount() != castedRecord.getTotalStartedThreadCount()) {
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
		code += ((int)this.getThreadCount());
		code += ((int)this.getDaemonThreadCount());
		code += ((int)this.getPeakThreadCount());
		code += ((int)this.getTotalStartedThreadCount());
		
		return code;
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
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "ThreadsStatusRecord: ";
		result += "timestamp = ";
		result += this.getTimestamp() + ", ";
		
		result += "hostname = ";
		result += this.getHostname() + ", ";
		
		result += "vmName = ";
		result += this.getVmName() + ", ";
		
		result += "threadCount = ";
		result += this.getThreadCount() + ", ";
		
		result += "daemonThreadCount = ";
		result += this.getDaemonThreadCount() + ", ";
		
		result += "peakThreadCount = ";
		result += this.getPeakThreadCount() + ", ";
		
		result += "totalStartedThreadCount = ";
		result += this.getTotalStartedThreadCount() + ", ";
		
		return result;
	}
}
