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

package kieker.common.record.flow.trace.operation.constructor;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.flow.IConstructorRecord;
import kieker.common.record.flow.trace.operation.CallOperationEvent;
import kieker.common.util.IString4UniqueId;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public class CallConstructorEvent extends CallOperationEvent implements IConstructorRecord {
	public static final int SIZE = CallOperationEvent.SIZE;
	public static final Class<?>[] TYPES = CallOperationEvent.TYPES;

	private static final long serialVersionUID = -2264901642077299543L;

	/**
	 * This constructor uses the given parameters to initialize the fields of this record.
	 * 
	 * @param timestamp
	 *            The timestamp of this record.
	 * @param traceId
	 *            The trace ID.
	 * @param orderIndex
	 *            The order index.
	 * @param callerOperationSignature
	 *            The caller operation signature. This parameter can be null.
	 * @param callerClassSignature
	 *            The caller class signature. This parameter can be null.
	 * @param calleeOperationSignature
	 *            The callee operation signature. This parameter can be null.
	 * @param calleeClassSignature
	 *            The callee class signature. This parameter can be null.
	 */
	public CallConstructorEvent(final long timestamp, final long traceId, final int orderIndex,
			final String callerOperationSignature, final String callerClassSignature,
			final String calleeOperationSignature, final String calleeClassSignature) {
		super(timestamp, traceId, orderIndex, callerOperationSignature, callerClassSignature, calleeOperationSignature, calleeClassSignature);
	}

	/**
	 * This constructor converts the given array into a record. It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public CallConstructorEvent(final Object[] values) {
		super(values, TYPES); // values[0..6]
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param types
	 *            The types of the elements in the first array.
	 */
	protected CallConstructorEvent(final Object[] values, final Class<?>[] types) {
		super(values, types); // values[0..6]
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
	public CallConstructorEvent(final ByteBuffer buffer, final IString4UniqueId stringRegistry) throws BufferUnderflowException {
		super(buffer, stringRegistry);
	}
}
