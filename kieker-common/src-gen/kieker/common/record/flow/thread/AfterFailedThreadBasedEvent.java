/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
package kieker.common.record.flow.thread;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.flow.thread.AbstractThreadBasedEvent;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

import kieker.common.record.flow.IExceptionRecord;

/**
 * @author Christian Wulf
 *         API compatibility: Kieker 1.15.0
 * 
 * @since 1.13
 */
public class AfterFailedThreadBasedEvent extends AbstractThreadBasedEvent implements IExceptionRecord {
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			+ TYPE_SIZE_LONG // IThreadBasedRecord.threadId
			+ TYPE_SIZE_INT // IThreadBasedRecord.orderIndex
			+ TYPE_SIZE_STRING // IOperationSignature.operationSignature
			+ TYPE_SIZE_STRING // IClassSignature.classSignature
			+ TYPE_SIZE_STRING; // IExceptionRecord.cause

	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		long.class, // IThreadBasedRecord.threadId
		int.class, // IThreadBasedRecord.orderIndex
		String.class, // IOperationSignature.operationSignature
		String.class, // IClassSignature.classSignature
		String.class, // IExceptionRecord.cause
	};

	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"timestamp",
		"threadId",
		"orderIndex",
		"operationSignature",
		"classSignature",
		"cause",
	};

	/** default constants. */
	public static final String CAUSE = "";
	private static final long serialVersionUID = -561668403011752179L;

	/** property declarations. */
	private final String cause;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param threadId
	 *            threadId
	 * @param orderIndex
	 *            orderIndex
	 * @param operationSignature
	 *            operationSignature
	 * @param classSignature
	 *            classSignature
	 * @param cause
	 *            cause
	 */
	public AfterFailedThreadBasedEvent(final long timestamp, final long threadId, final int orderIndex, final String operationSignature, final String classSignature,
			final String cause) {
		super(timestamp, threadId, orderIndex, operationSignature, classSignature);
		this.cause = cause == null ? CAUSE : cause;
	}

	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException
	 *             when the record could not be deserialized
	 */
	public AfterFailedThreadBasedEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		super(deserializer);
		this.cause = deserializer.getString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp());
		serializer.putLong(this.getThreadId());
		serializer.putInt(this.getOrderIndex());
		serializer.putString(this.getOperationSignature());
		serializer.putString(this.getClassSignature());
		serializer.putString(this.getCause());
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

		final AfterFailedThreadBasedEvent castedRecord = (AfterFailedThreadBasedEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (this.getTimestamp() != castedRecord.getTimestamp()) {
			return false;
		}
		if (this.getThreadId() != castedRecord.getThreadId()) {
			return false;
		}
		if (this.getOrderIndex() != castedRecord.getOrderIndex()) {
			return false;
		}
		if (!this.getOperationSignature().equals(castedRecord.getOperationSignature())) {
			return false;
		}
		if (!this.getClassSignature().equals(castedRecord.getClassSignature())) {
			return false;
		}
		if (!this.getCause().equals(castedRecord.getCause())) {
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
		code += ((int) this.getThreadId());
		code += ((int) this.getOrderIndex());
		code += this.getOperationSignature().hashCode();
		code += this.getClassSignature().hashCode();
		code += this.getCause().hashCode();

		return code;
	}

	public final String getCause() {
		return this.cause;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "AfterFailedThreadBasedEvent: ";
		result += "timestamp = ";
		result += this.getTimestamp() + ", ";

		result += "threadId = ";
		result += this.getThreadId() + ", ";

		result += "orderIndex = ";
		result += this.getOrderIndex() + ", ";

		result += "operationSignature = ";
		result += this.getOperationSignature() + ", ";

		result += "classSignature = ";
		result += this.getClassSignature() + ", ";

		result += "cause = ";
		result += this.getCause() + ", ";

		return result;
	}
}
