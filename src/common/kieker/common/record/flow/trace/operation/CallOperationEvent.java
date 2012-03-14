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

import kieker.common.record.flow.trace.AbstractTraceEventVisitor;

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

	private final String calleeOperationSiganture;

	public CallOperationEvent(final long timestamp, final long traceId, final int orderIndex, final String callerOperationSiganture,
			final String calleeOperationSiganture) {
		super(timestamp, traceId, orderIndex, callerOperationSiganture);
		this.calleeOperationSiganture = calleeOperationSiganture;
	}

	public CallOperationEvent(final Object[] values) {
		super(values, CallOperationEvent.TYPES); // values[0..3]
		this.calleeOperationSiganture = (String) values[4];
	}

	@Override
	public final Object[] toArray() {
		return new Object[] { this.getTimestamp(), this.getTraceId(), this.getOrderIndex(), this.getCallerOperationName(), this.getCalleeOperationSiganture() };
	}

	@Override
	public final Class<?>[] getValueTypes() {
		return CallOperationEvent.TYPES.clone();
	}

	public final String getCallerOperationName() {
		return this.getOperationSignature();
	}

	public final String getCalleeOperationSiganture() {
		return this.calleeOperationSiganture;
	}

	public final boolean callsReferencedOperationOf(final AbstractOperationEvent event) {
		return this.getCalleeOperationSiganture().equals(event.getOperationSignature());
	}

	@Override
	public final void accept(final AbstractTraceEventVisitor visitor) {
		visitor.handleCallOperationEvent(this);
	}
}
