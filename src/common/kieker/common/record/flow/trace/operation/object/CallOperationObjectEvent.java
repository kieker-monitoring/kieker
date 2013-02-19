/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

import kieker.common.record.flow.ICallObjectRecord;
import kieker.common.record.flow.trace.operation.CallOperationEvent;

/**
 * @author Jan Waller
 */
public final class CallOperationObjectEvent extends CallOperationEvent implements ICallObjectRecord {
	private static final long serialVersionUID = 5099289901643589844L;
	private static final Class<?>[] TYPES = {
		long.class, // Event.timestamp
		long.class, // TraceEvent.traceId
		int.class, // TraceEvent.orderIndex
		String.class, // OperationEvent.operationSiganture
		String.class, // OperationEvent.classSignature
		String.class, // CallOperationEvent.calleeOperationSignature
		String.class, // CallOperationEvent.calleeClassSiganture
		int.class, // Caller objectId
		int.class, // Callee objectId
	};

	private final int callerObjectId;
	private final int calleeObjectId;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            The timestamp.
	 * @param traceId
	 *            The trace ID.
	 * @param orderIndex
	 *            The order index.
	 * @param callerOperationSignature
	 *            The operation signature of the caller.
	 * @param callerClassSignature
	 *            The class signature of the caller.
	 * @param calleeOperationSignature
	 *            The operation signature of the callee.
	 * @param calleeClassSignature
	 *            The class signature of the callee.
	 * @param callerObjectId
	 *            The ID of the caller object.
	 * @param calleeObjectId
	 *            The ID of the callee object.
	 */
	public CallOperationObjectEvent(final long timestamp, final long traceId, final int orderIndex,
			final String callerOperationSignature, final String callerClassSignature,
			final String calleeOperationSignature, final String calleeClassSignature,
			final int callerObjectId, final int calleeObjectId) {
		super(timestamp, traceId, orderIndex, callerOperationSignature, callerClassSignature, calleeOperationSignature, calleeClassSignature);
		this.callerObjectId = callerObjectId;
		this.calleeObjectId = calleeObjectId;
	}

	/**
	 * 
	 * Creates a new instance of this class using the given array. The array should be the one resulting in a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The array containing the values.
	 */
	public CallOperationObjectEvent(final Object[] values) { // NOPMD (values stored directly)
		super(values, TYPES); // values[0..6]
		this.callerObjectId = (Integer) values[7];
		this.calleeObjectId = (Integer) values[8];
	}

	/**
	 * 
	 * Creates a new instance of this class using the given array but with modified types.
	 * 
	 * @param values
	 *            The array containing the values.
	 * @param types
	 *            The types of the array objects.
	 */
	protected CallOperationObjectEvent(final Object[] values, final Class<?>[] types) { // NOPMD (values stored directly)
		super(values, types); // values[0..6]
		this.callerObjectId = (Integer) values[7];
		this.calleeObjectId = (Integer) values[8];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] { this.getTimestamp(), this.getTraceId(), this.getOrderIndex(),
			this.getCallerOperationSignature(), this.getCallerClassSignature(),
			this.getCalleeOperationSignature(), this.getCalleeClassSignature(),
			this.callerObjectId, this.calleeObjectId, };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?>[] getValueTypes() {
		return TYPES.clone();
	}

	/**
	 * Delivers the ID of the caller object.
	 * 
	 * @return The ID of the caller.
	 */
	public int getObjectId() {
		return this.callerObjectId;
	}

	/**
	 * Delivers the ID of the caller object.
	 * 
	 * @return The ID of the caller.
	 */
	public final int getCallerObjectId() {
		return this.callerObjectId;
	}

	/**
	 * Delivers the ID of the callee object.
	 * 
	 * @return The ID of the callee.
	 */
	public final int getCalleeObjectId() {
		return this.calleeObjectId;
	}

	// TODO include objectIds?
	// @Override
	// public final boolean callsReferencedOperationOf(final IOperationRecord record) {
	// return this.getCalleeOperationSignature().equals(record.getOperationSignature()) && this.getCalleeClassSignature().equals(record.getClassSignature());
	// }
}
