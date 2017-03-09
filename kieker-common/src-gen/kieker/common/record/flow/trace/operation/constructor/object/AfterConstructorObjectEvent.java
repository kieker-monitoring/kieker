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
package kieker.common.record.flow.trace.operation.constructor.object;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.flow.trace.operation.constructor.AfterConstructorEvent;
import kieker.common.util.registry.IRegistry;

import kieker.common.record.flow.IObjectRecord;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public class AfterConstructorObjectEvent extends AfterConstructorEvent implements IObjectRecord {
	private static final long serialVersionUID = 1569131691754173288L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			 + TYPE_SIZE_LONG // ITraceRecord.traceId
			 + TYPE_SIZE_INT // ITraceRecord.orderIndex
			 + TYPE_SIZE_STRING // IOperationSignature.operationSignature
			 + TYPE_SIZE_STRING // IClassSignature.classSignature
			 + TYPE_SIZE_INT // IObjectRecord.objectId
	;
	
	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		long.class, // ITraceRecord.traceId
		int.class, // ITraceRecord.orderIndex
		String.class, // IOperationSignature.operationSignature
		String.class, // IClassSignature.classSignature
		int.class, // IObjectRecord.objectId
	};
	
	/** user-defined constants. */
	
	/** default constants. */
	public static final int OBJECT_ID = 0;
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"timestamp",
		"traceId",
		"orderIndex",
		"operationSignature",
		"classSignature",
		"objectId",
	};
	
	/** property declarations. */
	private final int objectId;
	
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
	 * @param objectId
	 *            objectId
	 */
	public AfterConstructorObjectEvent(final long timestamp, final long traceId, final int orderIndex, final String operationSignature, final String classSignature, final int objectId) {
		super(timestamp, traceId, orderIndex, operationSignature, classSignature);
		this.objectId = objectId;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public AfterConstructorObjectEvent(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
		this.objectId = (Integer) values[5];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected AfterConstructorObjectEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.objectId = (Integer) values[5];
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
	public AfterConstructorObjectEvent(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		super(buffer, stringRegistry);
		this.objectId = buffer.getInt();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getTimestamp(),
			this.getTraceId(),
			this.getOrderIndex(),
			this.getOperationSignature(),
			this.getClassSignature(),
			this.getObjectId()
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getOperationSignature());
		stringRegistry.get(this.getClassSignature());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putLong(this.getTimestamp());
		buffer.putLong(this.getTraceId());
		buffer.putInt(this.getOrderIndex());
		buffer.putInt(stringRegistry.get(this.getOperationSignature()));
		buffer.putInt(stringRegistry.get(this.getClassSignature()));
		buffer.putInt(this.getObjectId());
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
		
		final AfterConstructorObjectEvent castedRecord = (AfterConstructorObjectEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTimestamp() != castedRecord.getTimestamp()) return false;
		if (this.getTraceId() != castedRecord.getTraceId()) return false;
		if (this.getOrderIndex() != castedRecord.getOrderIndex()) return false;
		if (!this.getOperationSignature().equals(castedRecord.getOperationSignature())) return false;
		if (!this.getClassSignature().equals(castedRecord.getClassSignature())) return false;
		if (this.getObjectId() != castedRecord.getObjectId()) return false;
		return true;
	}
	
	public final int getObjectId() {
		return this.objectId;
	}	
}
