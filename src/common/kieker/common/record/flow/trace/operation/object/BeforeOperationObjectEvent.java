/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.record.flow.trace.operation.object;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.flow.IObjectRecord;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public class BeforeOperationObjectEvent extends BeforeOperationEvent implements IObjectRecord {
	public static final int SIZE = (2 * TYPE_SIZE_LONG) + TYPE_SIZE_INT + (2 * TYPE_SIZE_STRING) + TYPE_SIZE_INT;
	public static final Class<?>[] TYPES = {
		long.class, // Event.timestamp
		long.class, // TraceEvent.traceId
		int.class, // TraceEvent.orderIndex
		String.class, // OperationEvent.operationSignature
		String.class, // OperationEvent.classSignature
		int.class, // objectId
	};

	private static final long serialVersionUID = -589804991590804902L;

	private final int objectId;

	/**
	 * This constructor uses the given parameters to initialize the fields of this record.
	 * 
	 * @param timestamp
	 *            The timestamp of this record.
	 * @param traceId
	 *            The trace ID.
	 * @param orderIndex
	 *            The order index.
	 * @param operationSignature
	 *            The operation signature. This parameter can be null.
	 * @param classSignature
	 *            The class signature. This parameter can be null.
	 * @param objectId
	 *            The object ID.
	 */
	public BeforeOperationObjectEvent(final long timestamp, final long traceId, final int orderIndex, final String operationSignature, final String classSignature,
			final int objectId) {
		super(timestamp, traceId, orderIndex, operationSignature, classSignature);
		this.objectId = objectId;
	}

	/**
	 * This constructor converts the given array into a record. It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public BeforeOperationObjectEvent(final Object[] values) { // NOPMD (values stored directly)
		super(values, TYPES); // values[0..4]
		this.objectId = (Integer) values[5];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param types
	 *            The types of the elements in the first array.
	 */
	protected BeforeOperationObjectEvent(final Object[] values, final Class<?>[] types) { // NOPMD (values stored directly)
		super(values, types); // values[0..4]
		this.objectId = (Integer) values[5];
	}

	/**
	 * This constructor converts the given array into a record.
	 * 
	 * @param buffer
	 *            The bytes for the record.
	 * 
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public BeforeOperationObjectEvent(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		super(buffer, stringRegistry);
		this.objectId = buffer.getInt();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] { this.getTimestamp(), this.getTraceId(), this.getOrderIndex(), this.getOperationSignature(), this.getClassSignature(),
			this.getObjectId(), };
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
	public int getSize() {
		return SIZE;
	}

	public final int getObjectId() {
		return this.objectId;
	}
}
