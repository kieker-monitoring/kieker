/***************************************************************************
 * Copyright 2018 iObserve Project (https://iobserve-devops.net)
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
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;


/**
 * @author Christian Wulf
 * API compatibility: Kieker 1.14.0
 * 
 * @since 1.13
 */
public class ThreadMetaData extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_STRING // ThreadMetaData.hostname
			 + TYPE_SIZE_LONG; // ThreadMetaData.threadId
	
	public static final Class<?>[] TYPES = {
		String.class, // ThreadMetaData.hostname
		long.class, // ThreadMetaData.threadId
	};
	
	/** default constants. */
	public static final String HOSTNAME = "";
	private static final long serialVersionUID = 4284309919791475271L;
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"hostname",
		"threadId",
	};
	
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
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 *
	 * @deprecated since 1.13. Use {@link #ThreadMetaData(IValueDeserializer)} instead.
	 */
	@Deprecated
	public ThreadMetaData(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.hostname = (String) values[0];
		this.threadId = (Long) values[1];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated since 1.13. Use {@link #ThreadMetaData(IValueDeserializer)} instead.
	 */
	@Deprecated
	protected ThreadMetaData(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.hostname = (String) values[0];
		this.threadId = (Long) values[1];
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
	 *
	 * @deprecated since 1.13. Use {@link #serialize(IValueSerializer)} with an array serializer instead.
	 */
	@Override
	@Deprecated
	public Object[] toArray() {
		return new Object[] {
			this.getHostname(),
			this.getThreadId(),
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
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
	
	public final String getHostname() {
		return this.hostname;
	}
	
	
	public final long getThreadId() {
		return this.threadId;
	}
	
}
