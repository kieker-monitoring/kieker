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
package kieker.common.record.flow.trace.operation;

import java.nio.BufferOverflowException;

import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.util.registry.IRegistry;

import kieker.common.record.flow.IExceptionRecord;

/**
 * @author Jan Waller
 * API compatibility: Kieker 1.13.0
 * 
 * @since 1.5
 */
public class AfterOperationFailedEvent extends AfterOperationEvent implements IExceptionRecord {
	private static final long serialVersionUID = -1887200574513819573L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			 + TYPE_SIZE_LONG // ITraceRecord.traceId
			 + TYPE_SIZE_INT // ITraceRecord.orderIndex
			 + TYPE_SIZE_STRING // IOperationSignature.operationSignature
			 + TYPE_SIZE_STRING // IClassSignature.classSignature
			 + TYPE_SIZE_STRING // IExceptionRecord.cause
	;
	
	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		long.class, // ITraceRecord.traceId
		int.class, // ITraceRecord.orderIndex
		String.class, // IOperationSignature.operationSignature
		String.class, // IClassSignature.classSignature
		String.class, // IExceptionRecord.cause
	};
	
	
	/** default constants. */
	public static final String CAUSE = "";
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"timestamp",
		"traceId",
		"orderIndex",
		"operationSignature",
		"classSignature",
		"cause",
	};
	
	/** property declarations. */
	private final String cause;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param traceId
	 *            traceId
	 * @param orderIndex
	 *            orderIndex
	 * @param operationSignature
	 *            operationSignature
	 * @param classSignature
	 *            classSignature
	 * @param cause
	 *            cause
	 */
	public AfterOperationFailedEvent(final long timestamp, final long traceId, final int orderIndex, final String operationSignature, final String classSignature, final String cause) {
		super(timestamp, traceId, orderIndex, operationSignature, classSignature);
		this.cause = cause == null?CAUSE:cause;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 *
	 * @deprecated since 1.13. Use {@link #AfterOperationFailedEvent(IValueDeserializer)} instead.
	 */
	@Deprecated
	public AfterOperationFailedEvent(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
		this.cause = (String) values[5];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated since 1.13. Use {@link #AfterOperationFailedEvent(IValueDeserializer)} instead.
	 */
	@Deprecated
	protected AfterOperationFailedEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.cause = (String) values[5];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 */
	public AfterOperationFailedEvent(final IValueDeserializer deserializer) {
		super(deserializer);
		this.cause = deserializer.getString();
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
			this.getOperationSignature(),
			this.getClassSignature(),
			this.getCause()
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getOperationSignature());
		stringRegistry.get(this.getClassSignature());
		stringRegistry.get(this.getCause());
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
		serializer.putString(this.getOperationSignature());
		serializer.putString(this.getClassSignature());
		serializer.putString(this.getCause());
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
		
		final AfterOperationFailedEvent castedRecord = (AfterOperationFailedEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTimestamp() != castedRecord.getTimestamp()) return false;
		if (this.getTraceId() != castedRecord.getTraceId()) return false;
		if (this.getOrderIndex() != castedRecord.getOrderIndex()) return false;
		if (!this.getOperationSignature().equals(castedRecord.getOperationSignature())) return false;
		if (!this.getClassSignature().equals(castedRecord.getClassSignature())) return false;
		if (!this.getCause().equals(castedRecord.getCause())) return false;
		return true;
	}
	
	public final String getCause() {
		return this.cause;
	}
	
}
