/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.common.record.flow.trace.concurrency.monitor;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

/**
 * @author Jan Waller
 *         API compatibility: Kieker 1.15.0
 *
 * @since 1.8
 */
public class MonitorWaitEvent extends AbstractMonitorEvent {
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			+ TYPE_SIZE_LONG // ITraceRecord.traceId
			+ TYPE_SIZE_INT // ITraceRecord.orderIndex
			+ TYPE_SIZE_INT; // AbstractMonitorEvent.lockId

	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		long.class, // ITraceRecord.traceId
		int.class, // ITraceRecord.orderIndex
		int.class, // AbstractMonitorEvent.lockId
	};

	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"timestamp",
		"traceId",
		"orderIndex",
		"lockId",
	};

	private static final long serialVersionUID = -4024730304031335972L;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param timestamp
	 *            timestamp
	 * @param traceId
	 *            traceId
	 * @param orderIndex
	 *            orderIndex
	 * @param lockId
	 *            lockId
	 */
	public MonitorWaitEvent(final long timestamp, final long traceId, final int orderIndex, final int lockId) {
		super(timestamp, traceId, orderIndex, lockId);
	}

	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException
	 *             when the record could not be deserialized
	 */
	public MonitorWaitEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		super(deserializer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp());
		serializer.putLong(this.getTraceId());
		serializer.putInt(this.getOrderIndex());
		serializer.putInt(this.getLockId());
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

		final MonitorWaitEvent castedRecord = (MonitorWaitEvent) obj;
		if ((this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) || (this.getTimestamp() != castedRecord.getTimestamp()) || (this.getTraceId() != castedRecord.getTraceId()) || (this.getOrderIndex() != castedRecord.getOrderIndex())) {
			return false;
		}
		if (this.getLockId() != castedRecord.getLockId()) {
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
		code += (this.getLockId());

		return code;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "MonitorWaitEvent: ";
		result += "timestamp = ";
		result += this.getTimestamp() + ", ";

		result += "traceId = ";
		result += this.getTraceId() + ", ";

		result += "orderIndex = ";
		result += this.getOrderIndex() + ", ";

		result += "lockId = ";
		result += this.getLockId() + ", ";

		return result;
	}
}
