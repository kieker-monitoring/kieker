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

package kieker.common.record.flow.trace.operation;

/**
 * @author Jan Waller
 */
public class BeforeOperationEvent extends AbstractOperationEvent {
	private static final long serialVersionUID = -129094268144134877L;
	private static final Class<?>[] TYPES = {
		long.class, // Event.timestamp
		long.class, // TraceEvent.traceId
		int.class, // TraceEvent.orderIndex
		String.class, // OperationEvent.operationSignature
		String.class, // OperationEvent.classSignature
	};

	public BeforeOperationEvent(final long timestamp, final long traceId, final int orderIndex, final String operationSiganture, final String classSignature) {
		super(timestamp, traceId, orderIndex, operationSiganture, classSignature);
	}

	public BeforeOperationEvent(final Object[] values) {
		super(values, TYPES); // values[0..4]
	}

	protected BeforeOperationEvent(final Object[] values, final Class<?>[] types) {
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
	public Class<?>[] getValueTypes() {
		return TYPES.clone();
	}
}
