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

package kieker.common.record.flow.trace.operation;

import kieker.common.util.Bits;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public class AfterOperationEvent extends AbstractOperationEvent {
	private static final long serialVersionUID = -1072024765497556058L;
	private static final Class<?>[] TYPES = {
		long.class, // Event.timestamp
		long.class, // TraceEvent.traceId
		int.class, // TraceEvent.orderIndex
		String.class, // OperationEvent.operationSignature
		String.class, // OperationEvent.classSignature
	};

	/**
	 * This constructor initializes the fields of the record using the given parameters.
	 * 
	 * @param timestamp
	 *            The timestamp.
	 * @param traceId
	 *            The trace ID.
	 * @param orderIndex
	 *            The order index.
	 * @param operationSignature
	 *            The operation signature. This parameter can be null.
	 * @param classSignature
	 *            The class signature. This parameter can be null.
	 */
	public AfterOperationEvent(final long timestamp, final long traceId, final int orderIndex, final String operationSignature, final String classSignature) {
		super(timestamp, traceId, orderIndex, operationSignature, classSignature);
	}

	/**
	 * This constructor converts the given array into a record. It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public AfterOperationEvent(final Object[] values) {
		super(values, TYPES); // values[0..4]
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param types
	 *            The types of the elements in the first array.
	 */
	protected AfterOperationEvent(final Object[] values, final Class<?>[] types) {
		super(values, types); // values[0..4]
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] toArray() {
		return new Object[] { this.getTimestamp(), this.getTraceId(), this.getOrderIndex(), this.getOperationSignature(), this.getClassSignature(), };
	}

	/**
	 * {@inheritDoc}
	 */
	public byte[] toByteArray() {
		final byte[] arr = new byte[8 + 8 + 4 + 8 + 8];
		Bits.putLong(arr, 0, this.getTimestamp());
		Bits.putLong(arr, 8, this.getTraceId());
		Bits.putInt(arr, 8 + 8, this.getOrderIndex());
		Bits.putString(arr, 8 + 8 + 4, this.getOperationSignature());
		Bits.putString(arr, 8 + 8 + 4 + 8, this.getClassSignature());
		return arr;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?>[] getValueTypes() {
		return TYPES; // NOPMD
	}
}
