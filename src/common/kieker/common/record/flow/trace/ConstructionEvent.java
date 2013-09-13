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

package kieker.common.record.flow.trace;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.flow.IObjectRecord;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.5
 */
public class ConstructionEvent extends AbstractTraceEvent implements IObjectRecord {
	public static final int SIZE = 28;
	public static final Class<?>[] TYPES = {
		long.class, // Event.timestamp
		long.class, // TraceEvent.traceId
		int.class, // TraceEvent.orderIndex
		String.class, // classSignature
		int.class, // objectId
	};

	/** This field should not be exported, because it makes little sense to have no associated class. */
	private static final String NO_CLASSNAME = "<no-classname>";

	private static final long serialVersionUID = -8575921293755671749L;

	private final String classSignature;
	private final int objectId;

	/**
	 * This constructor uses the given parameters to initialize the fields of this record.
	 * 
	 * @param timestamp
	 *            The timestamp.
	 * @param traceId
	 *            The trace ID.
	 * @param orderIndex
	 *            The order index.
	 * @param className
	 *            The class name.
	 * @param objectId
	 *            The object ID.
	 */
	public ConstructionEvent(final long timestamp, final long traceId, final int orderIndex, final String className, final int objectId) {
		super(timestamp, traceId, orderIndex);
		this.classSignature = (className == null) ? NO_CLASSNAME : className; // NOCS
		this.objectId = objectId;
	}

	/**
	 * This constructor converts the given array into a record. It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public ConstructionEvent(final Object[] values) { // NOPMD (values stored directly)
		super(values, TYPES); // values[0..2]
		this.classSignature = (String) values[3];
		this.objectId = (Integer) values[4];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param types
	 *            The types of the elements in the first array.
	 */
	protected ConstructionEvent(final Object[] values, final Class<?>[] types) { // NOPMD (values stored directly)
		super(values, types); // values[0..2]
		this.classSignature = (String) values[3];
		this.objectId = (Integer) values[4];
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
	public ConstructionEvent(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		super(buffer, stringRegistry);
		this.classSignature = stringRegistry.get(buffer.getInt());
		this.objectId = buffer.getInt();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] toArray() {
		return new Object[] { this.getTimestamp(), this.getTraceId(), this.getOrderIndex(), this.getClassSignature(), this.getObjectId(), };
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putLong(this.getTimestamp());
		buffer.putLong(this.getTraceId());
		buffer.putInt(this.getOrderIndex());
		buffer.putInt(stringRegistry.get(this.getClassSignature()));
		buffer.putInt(this.getObjectId());
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?>[] getValueTypes() {
		return TYPES; // NOPMD
	}

	/**
	 * {@inheritDoc}
	 */
	public int getSize() {
		return SIZE;
	}

	public final String getClassSignature() {
		return this.classSignature;
	}

	public final int getObjectId() {
		return this.objectId;
	}
}
