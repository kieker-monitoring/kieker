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
package kieker.common.record.misc;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;


/**
 * @author Christian Wulf
 * API compatibility: Kieker 1.16.0
 * 
 * @since 1.13
 */
public class ThreadMetaData extends AbstractMonitoringRecord  {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_STRING // ThreadMetaData.hostname
			 + TYPE_SIZE_LONG; // ThreadMetaData.threadId
	
	public static final Class<?>[] TYPES = {
		String.class, // ThreadMetaData.hostname
		long.class, // ThreadMetaData.threadId
	};
	
	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"hostname",
		"threadId",
	};
	
	/** default constants. */
	public static final String HOSTNAME = "";
	private static final long serialVersionUID = 4284309919791475271L;
	
	/** property declarations. */
	private final String hostname;
	private final long threadId;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param hostname
	 *            hostname
	 * @param threadId
	 *            threadId
	 */
	public ThreadMetaData(final String hostname, final long threadId) {
		this.hostname = hostname == null?"":hostname;
		this.threadId = threadId;
	}


	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public ThreadMetaData(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.hostname = deserializer.getString();
		this.threadId = deserializer.getLong();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putString(this.getHostname());
		serializer.putLong(this.getThreadId());
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
		
		final ThreadMetaData castedRecord = (ThreadMetaData) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (!this.getHostname().equals(castedRecord.getHostname())) {
			return false;
		}
		if (this.getThreadId() != castedRecord.getThreadId()) {
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
		code += this.getHostname().hashCode();
		code += ((int)this.getThreadId());
		
		return code;
	}
	
	public final String getHostname() {
		return this.hostname;
	}
	
	
	public final long getThreadId() {
		return this.threadId;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "ThreadMetaData: ";
		result += "hostname = ";
		result += this.getHostname() + ", ";
		
		result += "threadId = ";
		result += this.getThreadId() + ", ";
		
		return result;
	}
}
