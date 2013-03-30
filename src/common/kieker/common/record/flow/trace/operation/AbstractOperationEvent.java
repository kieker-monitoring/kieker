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

package kieker.common.record.flow.trace.operation;

import kieker.common.record.flow.IOperationRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;

/**
 * @author Jan Waller
 * 
 * @since 1.5
 */
public abstract class AbstractOperationEvent extends AbstractTraceEvent implements IOperationRecord {
	private static final long serialVersionUID = 1L;

	/**
	 * This field should not be exported, because it makes little sense to have no associated class.
	 */
	private static final String NO_OPERATIONSIGNATURE = "<no-operationSignature>";
	private static final String NO_CLASSSIGNATURE = ""; // default is empty

	private final String operationSignature;
	private final String classSignature;

	public AbstractOperationEvent(final long timestamp, final long traceId, final int orderIndex, final String operationSignature, final String classSignature) {
		super(timestamp, traceId, orderIndex);
		this.operationSignature = (operationSignature == null) ? NO_OPERATIONSIGNATURE : operationSignature; // NOCS
		this.classSignature = (classSignature == null) ? NO_CLASSSIGNATURE : classSignature; // NOCS
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected AbstractOperationEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes); // values[0..2]
		this.operationSignature = (String) values[3];
		this.classSignature = (String) values[4];
	}

	public final String getOperationSignature() {
		return this.operationSignature;
	}

	public final String getClassSignature() {
		return this.classSignature;
	}

	public final boolean refersToSameOperationAs(final IOperationRecord record) {
		return this.getOperationSignature().equals(record.getOperationSignature()) && this.getClassSignature().equals(record.getClassSignature());
	}
}
