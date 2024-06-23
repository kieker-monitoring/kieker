/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.common.record.misc;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

/**
 * @author Reiner Jung
 *         API compatibility: Kieker 1.15.0
 *
 * @since 2.0.0
 */
public class OperationCallEvent extends AbstractMonitoringRecord {
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_STRING // OperationCallEvent.sourceComponent
			+ TYPE_SIZE_STRING // OperationCallEvent.sourceOperation
			+ TYPE_SIZE_STRING // OperationCallEvent.targetComponent
			+ TYPE_SIZE_STRING; // OperationCallEvent.targetOperation

	public static final Class<?>[] TYPES = {
		String.class, // OperationCallEvent.sourceComponent
		String.class, // OperationCallEvent.sourceOperation
		String.class, // OperationCallEvent.targetComponent
		String.class, // OperationCallEvent.targetOperation
	};

	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"sourceComponent",
		"sourceOperation",
		"targetComponent",
		"targetOperation",
	};

	/** default constants. */
	public static final String SOURCE_COMPONENT = "";
	public static final String SOURCE_OPERATION = "";
	public static final String TARGET_COMPONENT = "";
	public static final String TARGET_OPERATION = "";
	private static final long serialVersionUID = -5250152168812250464L;

	/** property declarations. */
	private final String sourceComponent;
	private final String sourceOperation;
	private final String targetComponent;
	private final String targetOperation;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param sourceComponent
	 *            sourceComponent
	 * @param sourceOperation
	 *            sourceOperation
	 * @param targetComponent
	 *            targetComponent
	 * @param targetOperation
	 *            targetOperation
	 */
	public OperationCallEvent(final String sourceComponent, final String sourceOperation, final String targetComponent, final String targetOperation) {
		this.sourceComponent = sourceComponent == null ? "" : sourceComponent;
		this.sourceOperation = sourceOperation == null ? "" : sourceOperation;
		this.targetComponent = targetComponent == null ? "" : targetComponent;
		this.targetOperation = targetOperation == null ? "" : targetOperation;
	}

	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException
	 *             when the record could not be deserialized
	 */
	public OperationCallEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.sourceComponent = deserializer.getString();
		this.sourceOperation = deserializer.getString();
		this.targetComponent = deserializer.getString();
		this.targetOperation = deserializer.getString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putString(this.getSourceComponent());
		serializer.putString(this.getSourceOperation());
		serializer.putString(this.getTargetComponent());
		serializer.putString(this.getTargetOperation());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?>[] getValueTypes() {
		return TYPES; // NOPMD
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getValueNames() {
		return VALUE_NAMES; // NOPMD
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSize() {
		return SIZE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != this.getClass()) {
			return false;
		}

		final OperationCallEvent castedRecord = (OperationCallEvent) obj;
		if ((this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) || !this.getSourceComponent().equals(castedRecord.getSourceComponent()) || !this.getSourceOperation().equals(castedRecord.getSourceOperation()) || !this.getTargetComponent().equals(castedRecord.getTargetComponent())) {
			return false;
		}
		if (!this.getTargetOperation().equals(castedRecord.getTargetOperation())) {
			return false;
		}

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int code = 0;
		code += this.getSourceComponent().hashCode();
		code += this.getSourceOperation().hashCode();
		code += this.getTargetComponent().hashCode();
		code += this.getTargetOperation().hashCode();

		return code;
	}

	public final String getSourceComponent() {
		return this.sourceComponent;
	}

	public final String getSourceOperation() {
		return this.sourceOperation;
	}

	public final String getTargetComponent() {
		return this.targetComponent;
	}

	public final String getTargetOperation() {
		return this.targetOperation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "OperationCallEvent: ";
		result += "sourceComponent = ";
		result += this.getSourceComponent() + ", ";

		result += "sourceOperation = ";
		result += this.getSourceOperation() + ", ";

		result += "targetComponent = ";
		result += this.getTargetComponent() + ", ";

		result += "targetOperation = ";
		result += this.getTargetOperation() + ", ";

		return result;
	}
}
