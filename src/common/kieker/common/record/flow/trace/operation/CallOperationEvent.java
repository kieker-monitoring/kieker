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

package kieker.common.record.flow.trace.operation;

/**
 * @author Andre van Hoorn, Holger Knoche, Jan Waller
 */
public class CallOperationEvent extends AbstractOperationEvent {
	private static final long serialVersionUID = -3767410839664324783L;
	private static final Class<?>[] TYPES = {
		long.class, // Event.timestamp
		long.class, // TraceEvent.traceId
		int.class, // TraceEvent.orderIndex
		String.class, // OperationEvent.operationSiganture
		String.class, // OperationEvent.classSignature
		String.class, // calleeOperationSignature
		String.class, // calleeClassSiganture
	};

	/**
	 * This field should not be exported, because it makes little sense to have no associated class
	 */
	private static final String NO_CALLEEOPERATIONSIGANTURE = "<no-calleeOperationSiganture>";
	private static final String NO_CALLEECLASSSIGANTURE = ""; // default is empty

	private final String calleeOperationSignature;
	private final String calleeClassSignature;

	public CallOperationEvent(final long timestamp, final long traceId, final int orderIndex,
			final String callerOperationSignature, final String callerClassSignature,
			final String calleeOperationSignature, final String calleeClassSignature) {
		super(timestamp, traceId, orderIndex, callerOperationSignature, callerClassSignature);
		this.calleeOperationSignature = (calleeOperationSignature == null) ? NO_CALLEEOPERATIONSIGANTURE : calleeOperationSignature; // NOCS
		this.calleeClassSignature = (calleeClassSignature == null) ? NO_CALLEECLASSSIGANTURE : calleeClassSignature; // NOCS
	}

	public CallOperationEvent(final Object[] values) { // NOPMD (values stored directly)
		super(values, TYPES); // values[0..4]
		this.calleeOperationSignature = (String) values[5];
		this.calleeClassSignature = (String) values[6];
	}

	protected CallOperationEvent(final Object[] values, final Class<?>[] types) { // NOPMD (values stored directly)
		super(values, types); // values[0..4]
		this.calleeOperationSignature = (String) values[5];
		this.calleeClassSignature = (String) values[6];
	}

	public Object[] toArray() {
		return new Object[] { this.getTimestamp(), this.getTraceId(), this.getOrderIndex(),
			this.getCallerOperationSignature(), this.getCallerClassSignature(),
			this.getCalleeOperationSignature(), this.getCalleeClassSignature(), };
	}

	public Class<?>[] getValueTypes() {
		return TYPES.clone();
	}

	public final String getCallerOperationSignature() {
		return this.getOperationSignature();
	}

	public final String getCallerClassSignature() {
		return this.getClassSignature();
	}

	public final String getCalleeOperationSignature() {
		return this.calleeOperationSignature;
	}

	public final String getCalleeClassSignature() {
		return this.calleeClassSignature;
	}

	public final boolean callsReferencedOperationOf(final AbstractOperationEvent event) {
		return (this.getCalleeOperationSignature().equals(event.getOperationSignature()) && this.getCalleeClassSignature().equals(event.getClassSignature()));
	}
}
