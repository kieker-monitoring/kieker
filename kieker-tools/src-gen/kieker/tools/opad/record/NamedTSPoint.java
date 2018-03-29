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
package kieker.tools.opad.record;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;


/**
 * @author Tillmann Carlos Bielefeld
 * API compatibility: Kieker 1.14.0
 * 
 * @since 1.10
 */
public class NamedTSPoint extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // NamedTSPoint.timestamp
			 + TYPE_SIZE_DOUBLE // NamedTSPoint.value
			 + TYPE_SIZE_STRING; // NamedTSPoint.name
	
	public static final Class<?>[] TYPES = {
		long.class, // NamedTSPoint.timestamp
		double.class, // NamedTSPoint.value
		String.class, // NamedTSPoint.name
	};
	
	/** default constants. */
	public static final String NAME = "";
	private static final long serialVersionUID = 4302229080791508406L;
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"timestamp",
		"value",
		"name",
	};
	
	/** property declarations. */
	private final long timestamp;
	private final double value;
	private final String name;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param value
	 *            value
	 * @param name
	 *            name
	 */
	public NamedTSPoint(final long timestamp, final double value, final String name) {
		this.timestamp = timestamp;
		this.value = value;
		this.name = name == null?"":name;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 *
	 * @deprecated since 1.13. Use {@link #NamedTSPoint(IValueDeserializer)} instead.
	 */
	@Deprecated
	public NamedTSPoint(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.timestamp = (Long) values[0];
		this.value = (Double) values[1];
		this.name = (String) values[2];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated since 1.13. Use {@link #NamedTSPoint(IValueDeserializer)} instead.
	 */
	@Deprecated
	protected NamedTSPoint(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.timestamp = (Long) values[0];
		this.value = (Double) values[1];
		this.name = (String) values[2];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public NamedTSPoint(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.timestamp = deserializer.getLong();
		this.value = deserializer.getDouble();
		this.name = deserializer.getString();
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
			this.getValue(),
			this.getName(),
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putLong(this.getTimestamp());
		serializer.putDouble(this.getValue());
		serializer.putString(this.getName());
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
		
		final NamedTSPoint castedRecord = (NamedTSPoint) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (this.getTimestamp() != castedRecord.getTimestamp()) {
			return false;
		}
		if (isNotEqual(this.getValue(), castedRecord.getValue())) {
			return false;
		}
		if (!this.getName().equals(castedRecord.getName())) {
			return false;
		}
		
		return true;
	}
	
	public final long getTimestamp() {
		return this.timestamp;
	}
	
	
	public final double getValue() {
		return this.value;
	}
	
	
	public final String getName() {
		return this.name;
	}
	
}
