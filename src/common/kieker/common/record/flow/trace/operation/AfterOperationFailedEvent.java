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

/**
 * @author Jan Waller
 */
public final class AfterOperationFailedEvent extends AbstractOperationEvent {

	/**
	 * Constant to be used if no cause required.
	 */
	public static final String NO_CAUSE = "<no-cause>";

	private static final long serialVersionUID = 3331883608930487185L;
	private static final Class<?>[] TYPES = {
		long.class, // Event.timestamp
		long.class, // TraceEvent.traceId
		int.class, // TraceEvent.orderIndex
		String.class, // OperationEvent.operationSiganture
		String.class, // Exception
	};

	private final String cause;

	public AfterOperationFailedEvent(final long timestamp, final long traceId, final int orderIndex, final String operationSiganture, final String cause) {
		super(timestamp, traceId, orderIndex, operationSiganture);
		this.cause = (cause == null) ? NO_CAUSE : cause; // NOCS
	}

	public AfterOperationFailedEvent(final Object[] values) { // NOPMD (values stored directly)
		super(values, TYPES); // values[0..3]
		this.cause = (String) values[4];
	}

	public final Object[] toArray() {
		return new Object[] { this.getTimestamp(), this.getTraceId(), this.getOrderIndex(), this.getOperationSignature(), this.cause };
	}

	public final Class<?>[] getValueTypes() {
		return TYPES.clone();
	}

	public final String getCause() {
		return this.cause;
	}
}
