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
package kieker.common.record.flow.trace;

import java.nio.BufferOverflowException;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.util.registry.IRegistry;


/**
 * @author Felix Eichhorst
 * API compatibility: Kieker 1.13.0
 * 
 * @since 1.14
 */
public class BeforeSentRemoteEvent extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {
	private static final long serialVersionUID = 1817999525650163947L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // BeforeSentRemoteEvent.timestamp
			 + TYPE_SIZE_LONG // BeforeSentRemoteEvent.traceId
			 + TYPE_SIZE_INT // BeforeSentRemoteEvent.orderIndex
			 + TYPE_SIZE_STRING // BeforeSentRemoteEvent.technology
	;
	
	public static final Class<?>[] TYPES = {
		long.class, // BeforeSentRemoteEvent.timestamp
		long.class, // BeforeSentRemoteEvent.traceId
		int.class, // BeforeSentRemoteEvent.orderIndex
		String.class, // BeforeSentRemoteEvent.technology
	};
	
	
	/** default constants. */
	public static final long TIMESTAMP = -1L;
	public static final long TRACE_ID = -1L;
	public static final int ORDER_INDEX = -1;
	public static final String TECHNOLOGY = "<default-technology>";
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"timestamp",
		"traceId",
		"orderIndex",
		"technology",
	};
	
	/** property declarations. */
	private final long timestamp;
	private final long traceId;
	private final int orderIndex;
	private final String technology;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param traceId
	 *            traceId
	 * @param orderIndex
	 *            orderIndex
	 * @param technology
	 *            technology
	 */
	public BeforeSentRemoteEvent(final long timestamp, final long traceId, final int orderIndex, final String technology) {
		this.timestamp = timestamp;
		this.traceId = traceId;
		this.orderIndex = orderIndex;
		this.technology = technology == null?TECHNOLOGY:technology;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 *
	 * @deprecated since 1.13. Use {@link #BeforeSentRemoteEvent(IValueDeserializer)} instead.
	 */
	@Deprecated
	public BeforeSentRemoteEvent(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.timestamp = (Long) values[0];
		this.traceId = (Long) values[1];
		this.orderIndex = (Integer) values[2];
		this.technology = (String) values[3];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated since 1.13. Use {@link #BeforeSentRemoteEvent(IValueDeserializer)} instead.
	 */
	@Deprecated
	protected BeforeSentRemoteEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.timestamp = (Long) values[0];
		this.traceId = (Long) values[1];
		this.orderIndex = (Integer) values[2];
		this.technology = (String) values[3];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 */
	public BeforeSentRemoteEvent(final IValueDeserializer deserializer) {
		this.timestamp = deserializer.getLong();
		this.traceId = deserializer.getLong();
		this.orderIndex = deserializer.getInt();
		this.technology = deserializer.getString();
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
			this.getTraceId(),
			this.getOrderIndex(),
			this.getTechnology()
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getTechnology());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putLong(this.getTimestamp());
		serializer.putLong(this.getTraceId());
		serializer.putInt(this.getOrderIndex());
		serializer.putString(this.getTechnology());
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
		
		final BeforeSentRemoteEvent castedRecord = (BeforeSentRemoteEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTimestamp() != castedRecord.getTimestamp()) return false;
		if (this.getTraceId() != castedRecord.getTraceId()) return false;
		if (this.getOrderIndex() != castedRecord.getOrderIndex()) return false;
		if (!this.getTechnology().equals(castedRecord.getTechnology())) return false;
		return true;
	}
	
	public final long getTimestamp() {
		return this.timestamp;
	}
	
	
	public final long getTraceId() {
		return this.traceId;
	}
	
	
	public final int getOrderIndex() {
		return this.orderIndex;
	}
	
	
	public final String getTechnology() {
		return this.technology;
	}
	
}
