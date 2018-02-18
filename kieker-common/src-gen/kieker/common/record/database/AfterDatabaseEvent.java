/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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
package kieker.common.record.database;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.util.registry.IRegistry;

import kieker.common.record.flow.IEventRecord;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.IClassSignature;
import kieker.common.record.flow.ITraceRecord;

/**
 * @author Christian Zirkelbach (czi@informatik.uni-kiel.de)
 * API compatibility: Kieker 1.13.0
 * 
 * @since 1.14
 */
public class AfterDatabaseEvent extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory, IEventRecord, IFlowRecord, IClassSignature, ITraceRecord {
	private static final long serialVersionUID = -3115006839098290523L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			 + TYPE_SIZE_STRING // IClassSignature.classSignature
			 + TYPE_SIZE_LONG // ITraceRecord.traceId
			 + TYPE_SIZE_INT // ITraceRecord.orderIndex
			 + TYPE_SIZE_STRING // AfterDatabaseEvent.returnType
			 + TYPE_SIZE_STRING // AfterDatabaseEvent.returnValue
	;
	
	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		String.class, // IClassSignature.classSignature
		long.class, // ITraceRecord.traceId
		int.class, // ITraceRecord.orderIndex
		String.class, // AfterDatabaseEvent.returnType
		String.class, // AfterDatabaseEvent.returnValue
	};
	
	
	/** default constants. */
	public static final long TIMESTAMP = 0L;
	public static final String CLASS_SIGNATURE = "";
	public static final long TRACE_ID = -1L;
	public static final int ORDER_INDEX = -1;
	public static final String RETURN_TYPE = "";
	public static final String RETURN_VALUE = "";
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"timestamp",
		"classSignature",
		"traceId",
		"orderIndex",
		"returnType",
		"returnValue",
	};
	
	/** property declarations. */
	private final long timestamp;
	private final String classSignature;
	private long traceId;
	private final int orderIndex;
	private final String returnType;
	private final String returnValue;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param classSignature
	 *            classSignature
	 * @param traceId
	 *            traceId
	 * @param orderIndex
	 *            orderIndex
	 * @param returnType
	 *            returnType
	 * @param returnValue
	 *            returnValue
	 */
	public AfterDatabaseEvent(final long timestamp, final String classSignature, final long traceId, final int orderIndex, final String returnType, final String returnValue) {
		this.timestamp = timestamp;
		this.classSignature = classSignature == null?CLASS_SIGNATURE:classSignature;
		this.traceId = traceId;
		this.orderIndex = orderIndex;
		this.returnType = returnType == null?"":returnType;
		this.returnValue = returnValue == null?"":returnValue;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 *
	 * @deprecated since 1.13. Use {@link #AfterDatabaseEvent(IValueDeserializer)} instead.
	 */
	@Deprecated
	public AfterDatabaseEvent(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.timestamp = (Long) values[0];
		this.classSignature = (String) values[1];
		this.traceId = (Long) values[2];
		this.orderIndex = (Integer) values[3];
		this.returnType = (String) values[4];
		this.returnValue = (String) values[5];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated since 1.13. Use {@link #AfterDatabaseEvent(IValueDeserializer)} instead.
	 */
	@Deprecated
	protected AfterDatabaseEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.timestamp = (Long) values[0];
		this.classSignature = (String) values[1];
		this.traceId = (Long) values[2];
		this.orderIndex = (Integer) values[3];
		this.returnType = (String) values[4];
		this.returnValue = (String) values[5];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 */
	public AfterDatabaseEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.timestamp = deserializer.getLong();
		this.classSignature = deserializer.getString();
		this.traceId = deserializer.getLong();
		this.orderIndex = deserializer.getInt();
		this.returnType = deserializer.getString();
		this.returnValue = deserializer.getString();
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
			this.getClassSignature(),
			this.getTraceId(),
			this.getOrderIndex(),
			this.getReturnType(),
			this.getReturnValue()
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getClassSignature());
		stringRegistry.get(this.getReturnType());
		stringRegistry.get(this.getReturnValue());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putLong(this.getTimestamp());
		serializer.putString(this.getClassSignature());
		serializer.putLong(this.getTraceId());
		serializer.putInt(this.getOrderIndex());
		serializer.putString(this.getReturnType());
		serializer.putString(this.getReturnValue());
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
		
		final AfterDatabaseEvent castedRecord = (AfterDatabaseEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTimestamp() != castedRecord.getTimestamp()) return false;
		if (!this.getClassSignature().equals(castedRecord.getClassSignature())) return false;
		if (this.getTraceId() != castedRecord.getTraceId()) return false;
		if (this.getOrderIndex() != castedRecord.getOrderIndex()) return false;
		if (!this.getReturnType().equals(castedRecord.getReturnType())) return false;
		if (!this.getReturnValue().equals(castedRecord.getReturnValue())) return false;
		return true;
	}
	
	public final long getTimestamp() {
		return this.timestamp;
	}
	
	
	public final String getClassSignature() {
		return this.classSignature;
	}
	
	
	public final long getTraceId() {
		return this.traceId;
	}
	
	public final void setTraceId(long traceId) {
		this.traceId = traceId;
	}
	
	public final int getOrderIndex() {
		return this.orderIndex;
	}
	
	
	public final String getReturnType() {
		return this.returnType;
	}
	
	
	public final String getReturnValue() {
		return this.returnValue;
	}
	
}
