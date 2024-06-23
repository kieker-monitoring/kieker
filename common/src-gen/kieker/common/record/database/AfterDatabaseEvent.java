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
package kieker.common.record.database;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.flow.IClassSignature;
import kieker.common.record.flow.IEventRecord;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.ITraceRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

/**
 * @author Christian Zirkelbach (czi@informatik.uni-kiel.de)
 *         API compatibility: Kieker 1.15.0
 *
 * @since 1.14
 */
public class AfterDatabaseEvent extends AbstractMonitoringRecord implements IEventRecord, IFlowRecord, IClassSignature, ITraceRecord {
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			+ TYPE_SIZE_STRING // IClassSignature.classSignature
			+ TYPE_SIZE_LONG // ITraceRecord.traceId
			+ TYPE_SIZE_INT // ITraceRecord.orderIndex
			+ TYPE_SIZE_STRING // AfterDatabaseEvent.returnType
			+ TYPE_SIZE_STRING; // AfterDatabaseEvent.returnValue

	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		String.class, // IClassSignature.classSignature
		long.class, // ITraceRecord.traceId
		int.class, // ITraceRecord.orderIndex
		String.class, // AfterDatabaseEvent.returnType
		String.class, // AfterDatabaseEvent.returnValue
	};

	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"timestamp",
		"classSignature",
		"traceId",
		"orderIndex",
		"returnType",
		"returnValue",
	};

	/** default constants. */
	public static final long TIMESTAMP = 0L;
	public static final String CLASS_SIGNATURE = "";
	public static final long TRACE_ID = -1L;
	public static final int ORDER_INDEX = -1;
	public static final String RETURN_TYPE = "";
	public static final String RETURN_VALUE = "";
	private static final long serialVersionUID = -6739598592315516823L;

	/** property declarations. */
	private long timestamp;
	private final String classSignature;
	private long traceId;
	private final int orderIndex;
	private final String returnType;
	private final String returnValue;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param timestamp
	 *            timestamp
	 * @param classSignature
	 *            classSignature
	 * @param traceId
	 *            traceId
	 * @param orderIndex
	 *            orderIndex
	 * @param returnType
	 *            returnType
	 * @param returnValue
	 *            returnValue
	 */
	public AfterDatabaseEvent(final long timestamp, final String classSignature, final long traceId, final int orderIndex, final String returnType,
			final String returnValue) {
		this.timestamp = timestamp;
		this.classSignature = classSignature == null ? CLASS_SIGNATURE : classSignature;
		this.traceId = traceId;
		this.orderIndex = orderIndex;
		this.returnType = returnType == null ? "" : returnType;
		this.returnValue = returnValue == null ? "" : returnValue;
	}

	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException
	 *             when the record could not be deserialized
	 */
	public AfterDatabaseEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.timestamp = deserializer.getLong();
		this.classSignature = deserializer.getString();
		this.traceId = deserializer.getLong();
		this.orderIndex = deserializer.getInt();
		this.returnType = deserializer.getString();
		this.returnValue = deserializer.getString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp());
		serializer.putString(this.getClassSignature());
		serializer.putLong(this.getTraceId());
		serializer.putInt(this.getOrderIndex());
		serializer.putString(this.getReturnType());
		serializer.putString(this.getReturnValue());
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

		final AfterDatabaseEvent castedRecord = (AfterDatabaseEvent) obj;
		if ((this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) || (this.getTimestamp() != castedRecord.getTimestamp()) || !this.getClassSignature().equals(castedRecord.getClassSignature()) || (this.getTraceId() != castedRecord.getTraceId())) {
			return false;
		}
		if (this.getOrderIndex() != castedRecord.getOrderIndex()) {
			return false;
		}
		if (!this.getReturnType().equals(castedRecord.getReturnType())) {
			return false;
		}
		if (!this.getReturnValue().equals(castedRecord.getReturnValue())) {
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
		code += this.getClassSignature().hashCode();
		code += ((int) this.getTraceId());
		code += (this.getOrderIndex());
		code += this.getReturnType().hashCode();
		code += this.getReturnValue().hashCode();

		return code;
	}

	public final long getTimestamp() {
		return this.timestamp;
	}

	public final void setTimestamp(final long timestamp) {
		this.timestamp = timestamp;
	}

	public final String getClassSignature() {
		return this.classSignature;
	}

	public final long getTraceId() {
		return this.traceId;
	}

	public final void setTraceId(final long traceId) {
		this.traceId = traceId;
	}

	public final int getOrderIndex() {
		return this.orderIndex;
	}

	public final String getReturnType() {
		return this.returnType;
	}

	public final String getReturnValue() {
		return this.returnValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "AfterDatabaseEvent: ";
		result += "timestamp = ";
		result += this.getTimestamp() + ", ";

		result += "classSignature = ";
		result += this.getClassSignature() + ", ";

		result += "traceId = ";
		result += this.getTraceId() + ", ";

		result += "orderIndex = ";
		result += this.getOrderIndex() + ", ";

		result += "returnType = ";
		result += this.getReturnType() + ", ";

		result += "returnValue = ";
		result += this.getReturnValue() + ", ";

		return result;
	}
}
