/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

import kieker.common.record.flow.trace.operation.CallOperationEvent;

/**
 * @author Jan Waller
 */
public final class CallObjectOperationEvent extends CallOperationEvent {
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

	public CallObjectOperationEvent(final long timestamp, final long traceId, final int orderIndex,
			final String callerOperationSignature, final String callerClassSignature,
			final String calleeOperationSignature, final String calleeClassSignature,
			final int callerObjectId, final int calleeObjectId) {
		super(timestamp, traceId, orderIndex, callerOperationSignature, callerClassSignature, calleeOperationSignature, calleeClassSignature);
		this.callerObjectId = callerObjectId;
		this.calleeObjectId = calleeObjectId;
	}

	public CallObjectOperationEvent(final Object[] values) {
		super(values, TYPES); // values[0..6]
		this.callerObjectId = (Integer) values[7];
		this.calleeObjectId = (Integer) values[8];
	}

	protected CallObjectOperationEvent(final Object[] values, final Class<?>[] types) {
		super(values, types); // values[0..6]
		this.callerObjectId = (Integer) values[7];
		this.calleeObjectId = (Integer) values[8];
	}

	@Override
	public Object[] toArray() {
		return new Object[] { this.getTimestamp(), this.getTraceId(), this.getOrderIndex(),
			this.getCallerOperationSignature(), this.getCallerClassSignature(),
			this.getCalleeOperationSignature(), this.getCalleeClassSignature(),
			this.callerObjectId, this.calleeObjectId, };
	}

	@Override
	public Class<?>[] getValueTypes() {
		return TYPES.clone();
	}

	public final int getCallerObjectId() {
		return this.callerObjectId;
	}

	public final int getCalleeObjectId() {
		return this.calleeObjectId;
	}
}
