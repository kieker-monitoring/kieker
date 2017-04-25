/***************************************************************************
<<<<<<< HEAD
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
=======
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
>>>>>>> d690fb62e (committing fix for issue 1524 introducing a parameter names array.)
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
package kieker.common.record.flow.trace;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.flow.AbstractEvent;
import kieker.common.record.io.IValueDeserializer;

import kieker.common.record.flow.ITraceRecord;

/**
 * @author Jan Waller
 *         API compatibility: Kieker 1.15.0
 * 
 * @since 1.5
 */
public abstract class AbstractTraceEvent extends AbstractEvent implements ITraceRecord {

<<<<<<< HEAD
	/** default constants. */
	public static final long TRACE_ID = -1L;
	public static final int ORDER_INDEX = -1;
	private static final long serialVersionUID = 3123204150489927280L;

	/** property declarations. */
	private long traceId;
	private final int orderIndex;

=======
	
	
	/** default constants. */
	public static final long TRACE_ID = -1L;
	public static final int ORDER_INDEX = -1;
	
		
	/** property declarations. */
	private long traceId;
	private int orderIndex;
	
>>>>>>> d690fb62e (committing fix for issue 1524 introducing a parameter names array.)
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param traceId
	 *            traceId
	 * @param orderIndex
	 *            orderIndex
	 */
	public AbstractTraceEvent(final long timestamp, final long traceId, final int orderIndex) {
		super(timestamp);
		this.traceId = traceId;
		this.orderIndex = orderIndex;
	}

	/**
<<<<<<< HEAD
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException
	 *             when the record could not be deserialized
=======
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected AbstractTraceEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.traceId = (Long) values[1];
		this.orderIndex = (Integer) values[2];
	}

	/**
	 * This constructor converts the given buffer into a record.
	 * 
	 * @param buffer
	 *            The bytes for the record
	 * @param stringRegistry
	 *            The string registry for deserialization
	 * 
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
>>>>>>> d690fb62e (committing fix for issue 1524 introducing a parameter names array.)
	 */
	public AbstractTraceEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		super(deserializer);
		this.traceId = deserializer.getLong();
		this.orderIndex = deserializer.getInt();
	}
<<<<<<< HEAD
=======
	
>>>>>>> d690fb62e (committing fix for issue 1524 introducing a parameter names array.)

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

		final AbstractTraceEvent castedRecord = (AbstractTraceEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (this.getTimestamp() != castedRecord.getTimestamp()) {
			return false;
		}
		if (this.getTraceId() != castedRecord.getTraceId()) {
			return false;
		}
		if (this.getOrderIndex() != castedRecord.getOrderIndex()) {
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
		code += ((int) this.getOrderIndex());

		return code;
	}

	public final long getTraceId() {
		return this.traceId;
	}

	public final void setTraceId(long traceId) {
		this.traceId = traceId;
	}

	public final int getOrderIndex() {
		return this.orderIndex;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "AbstractTraceEvent: ";
		result += "timestamp = ";
		result += this.getTimestamp() + ", ";

		result += "traceId = ";
		result += this.getTraceId() + ", ";

		result += "orderIndex = ";
		result += this.getOrderIndex() + ", ";

		return result;
	}
<<<<<<< HEAD
=======
	
	public final long getTraceId() {
		return this.traceId;
	}
	
	public final void setTraceId(long traceId) {
		this.traceId = traceId;
	}
	
	public final int getOrderIndex() {
		return this.orderIndex;
	}
	
	public final void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}
>>>>>>> d690fb62e (committing fix for issue 1524 introducing a parameter names array.)
}
