/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.common.record.flow.trace;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kicker.common.record.flow.AbstractEvent;
import kicker.common.record.flow.ITraceRecord;
import kicker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.5
 */
public abstract class AbstractTraceEvent extends AbstractEvent implements ITraceRecord {
	private static final long serialVersionUID = 1L;

	private final long traceId;
	private final int orderIndex;

	/**
	 * This constructor uses the given parameters to initialize the fields of this record.
	 * 
	 * @param timestamp
	 *            The timestamp.
	 * @param traceId
	 *            The trace ID.
	 * @param orderIndex
	 *            the order index.
	 */
	public AbstractTraceEvent(final long timestamp, final long traceId, final int orderIndex) {
		super(timestamp);
		this.traceId = traceId;
		this.orderIndex = orderIndex;
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected AbstractTraceEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes); // values[0]
		this.traceId = (Long) values[1];
		this.orderIndex = (Integer) values[2];
	}

	/**
	 * This constructor converts the given array into a record.
	 * 
	 * @param buffer
	 *            The bytes for the record.
	 * 
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public AbstractTraceEvent(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		super(buffer, stringRegistry);
		this.traceId = buffer.getLong();
		this.orderIndex = buffer.getInt();
	}

	@Override
	public final long getTraceId() {
		return this.traceId;
	}

	@Override
	public final int getOrderIndex() {
		return this.orderIndex;
	}
}
