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
 * @author Jan Waller
 */
public final class AfterOperationEvent extends AbstractOperationEvent {
	private static final long serialVersionUID = -631724968913053878L;
	private static final Class<?>[] TYPES = {
		long.class, // Event.timestamp
		long.class, // TraceEvent.traceId
		int.class, // TraceEvent.orderIndex
		String.class, // OperationEvent.operationSiganture
	};

	public AfterOperationEvent(final long timestamp, final long traceId, final int orderIndex, final String operationSiganture) {
		super(timestamp, traceId, orderIndex, operationSiganture);
	}

	public AfterOperationEvent(final Object[] values) {
		super(values, AfterOperationEvent.TYPES); // values[0..3]
	}

	@Override
	public final Object[] toArray() {
		return new Object[] { this.getTimestamp(), this.getTraceId(), this.getOrderIndex(), this.getOperationSignature(), };
	}

	@Override
	public final Class<?>[] getValueTypes() {
		return AfterOperationEvent.TYPES.clone();
	}

	@Override
	public final void accept(final IAbstractTraceEventVisitor visitor) {
		visitor.handleAfterOperationEvent(this);
	}
}
