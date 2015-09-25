/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

import kieker.common.util.registry.IRegistry;
import kieker.common.util.Version;

import kieker.common.record.flow.trace.AbstractTraceEvent;

/**
 * @author Jan Waller
 * 
 * @since 1.5
 */
public class ConstructionEvent extends AbstractTraceEvent  {
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			 + TYPE_SIZE_LONG // ITraceRecord.traceId
			 + TYPE_SIZE_INT // ITraceRecord.orderIndex
			 + TYPE_SIZE_STRING // ConstructionEvent.classSignature
			 + TYPE_SIZE_INT // ConstructionEvent.objectId
	;
	private static final long serialVersionUID = -3519441499400826996L;
	
	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		long.class, // ITraceRecord.traceId
		int.class, // ITraceRecord.orderIndex
		String.class, // ConstructionEvent.classSignature
		int.class, // ConstructionEvent.objectId
	};
	
	/* user-defined constants */
	/* default constants */
	public static final String CLASS_SIGNATURE = "";
	public static final int OBJECT_ID = 0;
	/* property declarations */
	private final String classSignature;
	private final int objectId;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param traceId
	 *            traceId
	 * @param orderIndex
	 *            orderIndex
	 * @param classSignature
	 *            classSignature
	 * @param objectId
	 *            objectId
	 */
	public ConstructionEvent(final long timestamp, final long traceId, final int orderIndex, final String classSignature, final int objectId) {
		super(timestamp, traceId, orderIndex);
		this.classSignature = classSignature == null?CLASS_SIGNATURE:classSignature;
		this.objectId = objectId;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public ConstructionEvent(final Object[] values) { // NOPMD (direct store of values)
		super(values, TYPES);
		this.classSignature = (String) values[3];
		this.objectId = (Integer) values[4];
	}
	
	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected ConstructionEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
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
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getTimestamp(),
			this.getTraceId(),
			this.getOrderIndex(),
			this.getClassSignature(),
			this.getObjectId()
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getClassSignature());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
	@Override
	public Class<?>[] getValueTypes() {
		return TYPES; // NOPMD
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
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.BinaryFactory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean equalsInternal(final kieker.common.record.IMonitoringRecord record) {
		final ConstructionEvent castedRecord = (ConstructionEvent) record;
		if (!this.classSignature.equals(castedRecord.classSignature)) return false;
		if (this.objectId != castedRecord.objectId) return false;
		return super.equalsInternal(castedRecord);
	}

	public final String getClassSignature() {
		return this.classSignature;
	}
	
	public final int getObjectId() {
		return this.objectId;
	}
	
}
