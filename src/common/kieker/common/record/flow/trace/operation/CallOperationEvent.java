/***************************************************************************
 * Copyright 2011 by
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

package kieker.common.record.flow.trace.operation;

import kieker.common.record.flow.trace.IAbstractTraceEventVisitor;

/**
 * @author Andre van Hoorn, Holger Knoche, Jan Waller
 */
public final class CallOperationEvent extends AbstractOperationEvent {
	private static final long serialVersionUID = -63172423466638L;
	private static final Class<?>[] TYPES = {
		long.class, // Event.timestamp
		long.class, // TraceEvent.traceId
		int.class, // TraceEvent.orderIndex
		String.class, // OperationEvent.operationSiganture
		String.class, // calleeOperationSiganture
	};

	/**
	 * This field should not be exported, because it makes little sense to have no associated class
	 */
	private static final String NO_CALLEEOPERATIONSIGANTURE = "<no-calleeOperationSiganture>";

	private final String calleeOperationSignature;

	public CallOperationEvent(final long timestamp, final long traceId, final int orderIndex, final String callerOperationSignature,
			final String calleeOperationSignature) {
		super(timestamp, traceId, orderIndex, callerOperationSignature);
		this.calleeOperationSignature = (calleeOperationSignature == null) ? CallOperationEvent.NO_CALLEEOPERATIONSIGANTURE : calleeOperationSignature; // NOCS
	}

	public CallOperationEvent(final Object[] values) {
		super(values, CallOperationEvent.TYPES); // values[0..3]
		this.calleeOperationSignature = (String) values[4];
	}

	public final Object[] toArray() {
		return new Object[] { this.getTimestamp(), this.getTraceId(), this.getOrderIndex(), this.getCallerOperationSignature(), this.getCalleeOperationSignature() };
	}

	public final Class<?>[] getValueTypes() {
		return CallOperationEvent.TYPES.clone();
	}

	public final String getCallerOperationSignature() {
		return this.getOperationSignature();
	}

	public final String getCalleeOperationSignature() {
		return this.calleeOperationSignature;
	}

	public final boolean callsReferencedOperationOf(final AbstractOperationEvent event) {
		return this.getCalleeOperationSignature().equals(event.getOperationSignature());
	}

	@Override
	public final void accept(final IAbstractTraceEventVisitor visitor) {
		visitor.handleCallOperationEvent(this);
	}
}
