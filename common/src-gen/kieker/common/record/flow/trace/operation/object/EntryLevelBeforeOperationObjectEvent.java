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
package kieker.common.record.flow.trace.operation.object;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.flow.trace.operation.IPayloadCharacterization;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

/**
 * @author Reiner Jung
 *         API compatibility: Kieker 1.15.0
 *
 * @since 2.0.0
 */
public class EntryLevelBeforeOperationObjectEvent extends BeforeOperationObjectEvent implements IPayloadCharacterization {
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			+ TYPE_SIZE_LONG // ITraceRecord.traceId
			+ TYPE_SIZE_INT // ITraceRecord.orderIndex
			+ TYPE_SIZE_STRING // IOperationSignature.operationSignature
			+ TYPE_SIZE_STRING // IClassSignature.classSignature
			+ TYPE_SIZE_INT // IObjectRecord.objectId
			+ TYPE_SIZE_STRING // IPayloadCharacterization.parameters
			+ TYPE_SIZE_STRING // IPayloadCharacterization.values
			+ TYPE_SIZE_INT; // IPayloadCharacterization.requestType

	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		long.class, // ITraceRecord.traceId
		int.class, // ITraceRecord.orderIndex
		String.class, // IOperationSignature.operationSignature
		String.class, // IClassSignature.classSignature
		int.class, // IObjectRecord.objectId
		String[].class, // IPayloadCharacterization.parameters
		String[].class, // IPayloadCharacterization.values
		int.class, // IPayloadCharacterization.requestType
	};

	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"timestamp",
		"traceId",
		"orderIndex",
		"operationSignature",
		"classSignature",
		"objectId",
		"parameters",
		"values",
		"requestType",
	};

	private static final long serialVersionUID = 5159133527296016634L;

	/** property declarations. */
	private final String[] parameters;
	private final String[] values;
	private final int requestType;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param timestamp
	 *            timestamp
	 * @param traceId
	 *            traceId
	 * @param orderIndex
	 *            orderIndex
	 * @param operationSignature
	 *            operationSignature
	 * @param classSignature
	 *            classSignature
	 * @param objectId
	 *            objectId
	 * @param parameters
	 *            parameters
	 * @param values
	 *            values
	 * @param requestType
	 *            requestType
	 */
	public EntryLevelBeforeOperationObjectEvent(final long timestamp, final long traceId, final int orderIndex, final String operationSignature,
			final String classSignature, final int objectId, final String[] parameters, final String[] values, final int requestType) {
		super(timestamp, traceId, orderIndex, operationSignature, classSignature, objectId);
		this.parameters = parameters;
		this.values = values;
		this.requestType = requestType;
	}

	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException
	 *             when the record could not be deserialized
	 */
	public EntryLevelBeforeOperationObjectEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		super(deserializer);
		// load array sizes
		final int _parameters_size0 = deserializer.getInt();
		this.parameters = new String[_parameters_size0];
		for (int i0 = 0; i0 < _parameters_size0; i0++) {
			this.parameters[i0] = deserializer.getString();
		}

		// load array sizes
		final int _values_size0 = deserializer.getInt();
		this.values = new String[_values_size0];
		for (int i0 = 0; i0 < _values_size0; i0++) {
			this.values[i0] = deserializer.getString();
		}

		this.requestType = deserializer.getInt();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp());
		serializer.putLong(this.getTraceId());
		serializer.putInt(this.getOrderIndex());
		serializer.putString(this.getOperationSignature());
		serializer.putString(this.getClassSignature());
		serializer.putInt(this.getObjectId());
		// store array sizes
		final int _parameters_size0 = this.getParameters().length;
		serializer.putInt(_parameters_size0);
		for (int i0 = 0; i0 < _parameters_size0; i0++) {
			serializer.putString(this.getParameters()[i0]);
		}

		// store array sizes
		final int _values_size0 = this.getValues().length;
		serializer.putInt(_values_size0);
		for (int i0 = 0; i0 < _values_size0; i0++) {
			serializer.putString(this.getValues()[i0]);
		}

		serializer.putInt(this.getRequestType());
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

		final EntryLevelBeforeOperationObjectEvent castedRecord = (EntryLevelBeforeOperationObjectEvent) obj;
		if ((this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) || (this.getTimestamp() != castedRecord.getTimestamp()) || (this.getTraceId() != castedRecord.getTraceId()) || (this.getOrderIndex() != castedRecord.getOrderIndex())) {
			return false;
		}
		if (!this.getOperationSignature().equals(castedRecord.getOperationSignature())) {
			return false;
		}
		if (!this.getClassSignature().equals(castedRecord.getClassSignature())) {
			return false;
		}
		if (this.getObjectId() != castedRecord.getObjectId()) {
			return false;
		}
		// get array length
		final int _parameters_size0 = this.getParameters().length;
		if (_parameters_size0 != castedRecord.getParameters().length) {
			return false;
		}
		for (int i0 = 0; i0 < _parameters_size0; i0++) {
			if (!this.getParameters()[i0].equals(castedRecord.getParameters()[i0])) {
				return false;
			}
		}

		// get array length
		final int _values_size0 = this.getValues().length;
		if (_values_size0 != castedRecord.getValues().length) {
			return false;
		}
		for (int i0 = 0; i0 < _values_size0; i0++) {
			if (!this.getValues()[i0].equals(castedRecord.getValues()[i0])) {
				return false;
			}
		}

		if (this.getRequestType() != castedRecord.getRequestType()) {
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
		code += ((int) this.getTimestamp());
		code += ((int) this.getTraceId());
		code += (this.getOrderIndex());
		code += this.getOperationSignature().hashCode();
		code += this.getClassSignature().hashCode();
		code += (this.getObjectId());
		// get array length
		for (int i0 = 0; i0 < this.parameters.length; i0++) {
			for (int i1 = 0; i1 < this.parameters.length; i1++) {
				code += this.getParameters()[i0].hashCode();
			}
		}

		// get array length
		for (int i0 = 0; i0 < this.values.length; i0++) {
			for (int i1 = 0; i1 < this.values.length; i1++) {
				code += this.getValues()[i0].hashCode();
			}
		}

		code += (this.getRequestType());

		return code;
	}

	public final String[] getParameters() {
		return this.parameters;
	}

	public final String[] getValues() {
		return this.values;
	}

	public final int getRequestType() {
		return this.requestType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "EntryLevelBeforeOperationObjectEvent: ";
		result += "timestamp = ";
		result += this.getTimestamp() + ", ";

		result += "traceId = ";
		result += this.getTraceId() + ", ";

		result += "orderIndex = ";
		result += this.getOrderIndex() + ", ";

		result += "operationSignature = ";
		result += this.getOperationSignature() + ", ";

		result += "classSignature = ";
		result += this.getClassSignature() + ", ";

		result += "objectId = ";
		result += this.getObjectId() + ", ";

		result += "parameters = ";
		// store array sizes
		final int _parameters_size0 = this.getParameters().length;
		result += "{ ";
		for (int i0 = 0; i0 < _parameters_size0; i0++) {
			result += this.getParameters()[i0] + ", ";
		}
		result += " }";

		result += "values = ";
		// store array sizes
		final int _values_size0 = this.getValues().length;
		result += "{ ";
		for (int i0 = 0; i0 < _values_size0; i0++) {
			result += this.getValues()[i0] + ", ";
		}
		result += " }";

		result += "requestType = ";
		result += this.getRequestType() + ", ";

		return result;
	}
}
