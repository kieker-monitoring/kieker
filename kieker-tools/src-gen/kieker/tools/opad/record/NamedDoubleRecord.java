/***************************************************************************
 * Copyright 2018 iObserve Project (https://www.iobserve-devops.net)
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
package kieker.tools.opad.record;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;


/**
 * @author Tom Frotscher
 * API compatibility: Kieker 1.14.0
 * 
 * @since 1.10
 */
public class NamedDoubleRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_STRING // NamedDoubleRecord.applicationName
			 + TYPE_SIZE_LONG // NamedDoubleRecord.timestamp
			 + TYPE_SIZE_DOUBLE; // NamedDoubleRecord.responseTime
	
	public static final Class<?>[] TYPES = {
		String.class, // NamedDoubleRecord.applicationName
		long.class, // NamedDoubleRecord.timestamp
		double.class, // NamedDoubleRecord.responseTime
	};
	
	/** default constants. */
	public static final String APPLICATION_NAME = "";
	private static final long serialVersionUID = 3508131536785781597L;
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"applicationName",
		"timestamp",
		"responseTime",
	};
	
	/** property declarations. */
	private final String applicationName;
	private final long timestamp;
	private final double responseTime;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param applicationName
	 *            applicationName
	 * @param timestamp
	 *            timestamp
	 * @param responseTime
	 *            responseTime
	 */
	public NamedDoubleRecord(final String applicationName, final long timestamp, final double responseTime) {
		this.applicationName = applicationName == null?"":applicationName;
		this.timestamp = timestamp;
		this.responseTime = responseTime;
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
	public NamedDoubleRecord(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.applicationName = (String) values[0];
		this.timestamp = (Long) values[1];
		this.responseTime = (Double) values[2];
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
	protected NamedDoubleRecord(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.applicationName = (String) values[0];
		this.timestamp = (Long) values[1];
		this.responseTime = (Double) values[2];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public NamedDoubleRecord(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.applicationName = deserializer.getString();
		this.timestamp = deserializer.getLong();
		this.responseTime = deserializer.getDouble();
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
			this.getApplicationName(),
			this.getTimestamp(),
			this.getResponseTime(),
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putString(this.getApplicationName());
		serializer.putLong(this.getTimestamp());
		serializer.putDouble(this.getResponseTime());
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
		
		final NamedDoubleRecord castedRecord = (NamedDoubleRecord) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (!this.getApplicationName().equals(castedRecord.getApplicationName())) {
			return false;
		}
		if (this.getTimestamp() != castedRecord.getTimestamp()) {
			return false;
		}
		if (isNotEqual(this.getResponseTime(), castedRecord.getResponseTime())) {
			return false;
		}
		
		return true;
	}
	
	public final String getApplicationName() {
		return this.applicationName;
	}
	
	
	public final long getTimestamp() {
		return this.timestamp;
	}
	
	
	public final double getResponseTime() {
		return this.responseTime;
	}
	
}
