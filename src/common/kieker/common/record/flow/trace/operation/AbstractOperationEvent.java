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

import kieker.common.record.flow.trace.AbstractTraceEvent;

/**
 * @author Jan Waller
 */
public abstract class AbstractOperationEvent extends AbstractTraceEvent {
	private static final long serialVersionUID = 1L;

	/**
	 * This field should not be exported, because it makes little sense to have no associated class
	 */
	private static final String NO_OPERATIONSIGNATURE = "<no-operationSignature>";

	private final String operationSignature;

	public AbstractOperationEvent(final long timestamp, final long traceId, final int orderIndex, final String operationSignature) {
		super(timestamp, traceId, orderIndex);
		this.operationSignature = (operationSignature == null) ? AbstractOperationEvent.NO_OPERATIONSIGNATURE : operationSignature; // NOCS
	}

	public AbstractOperationEvent(final Object[] values, final Class<?>[] valueTypes) {
		super(values, valueTypes); // values[0..2]
		this.operationSignature = (String) values[3];
	}

	public final String getOperationSignature() {
		return this.operationSignature;
	}

	public boolean refersToSameOperationAs(final AbstractOperationEvent other) {
		return (this.getOperationSignature().equals(other.getOperationSignature()));
	}
}
