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

import kieker.common.record.flow.IObjectRecord;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.util.Bits;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public class AfterOperationObjectEvent extends AfterOperationEvent implements IObjectRecord {

	public static final Class<?>[] TYPES = {
		long.class, // Event.timestamp
		long.class, // TraceEvent.traceId
		int.class, // TraceEvent.orderIndex
		String.class, // OperationEvent.operationSignature
		String.class, // OperationEvent.classSignature
		int.class, // objectId
	};

	private static final long serialVersionUID = 2118146247161144658L;

	private final int objectId;

	/**
	 * This constructor initializes the fields of the record using the given parameters.
	 * 
	 * @param timestamp
	 *            The timestamp.
	 * @param traceId
	 *            The trace ID.
	 * @param orderIndex
	 *            The order index.
	 * @param operationSiganture
	 *            The operation signature. This parameter can be null.
	 * @param classSignature
	 *            The class signature. This parameter can be null.
	 * @param objectId
	 *            The object ID.
	 */
	public AfterOperationObjectEvent(final long timestamp, final long traceId, final int orderIndex, final String operationSiganture, final String classSignature,
			final int objectId) {
		super(timestamp, traceId, orderIndex, operationSiganture, classSignature);
		this.objectId = objectId;
	}

	/**
	 * This constructor converts the given array into a record. It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public AfterOperationObjectEvent(final Object[] values) { // NOPMD (values stored directly)
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
	protected AfterOperationObjectEvent(final Object[] values, final Class<?>[] types) { // NOPMD (values stored directly)
		super(values, types); // values[0..4]
		this.objectId = (Integer) values[5];
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
	public byte[] toByteArray() {
		final byte[] arr = new byte[8 + 8 + 4 + 8 + 8 + 4];
		Bits.putLong(arr, 0, this.getTimestamp());
		Bits.putLong(arr, 8, this.getTraceId());
		Bits.putInt(arr, 8 + 8, this.getOrderIndex());
		Bits.putString(arr, 8 + 8 + 4, this.getOperationSignature());
		Bits.putString(arr, 8 + 8 + 4 + 8, this.getClassSignature());
		Bits.putInt(arr, 8 + 8 + 4 + 8 + 8, this.getObjectId());
		return arr;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?>[] getValueTypes() {
		return TYPES; // NOPMD
	}

	public final int getObjectId() {
		return this.objectId;
	}
}
