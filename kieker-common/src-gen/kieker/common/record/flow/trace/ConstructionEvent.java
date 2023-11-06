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
package kieker.common.record.flow.trace;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;


/**
 * @author Jan Waller
 * API compatibility: Kieker 1.15.0
 * 
 * @since 1.5
 */
public class ConstructionEvent extends AbstractTraceEvent  {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			 + TYPE_SIZE_LONG // ITraceRecord.traceId
			 + TYPE_SIZE_INT // ITraceRecord.orderIndex
			 + TYPE_SIZE_STRING // ConstructionEvent.classSignature
			 + TYPE_SIZE_INT; // ConstructionEvent.objectId
	
	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		long.class, // ITraceRecord.traceId
		int.class, // ITraceRecord.orderIndex
		String.class, // ConstructionEvent.classSignature
		int.class, // ConstructionEvent.objectId
	};
	
	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"timestamp",
		"traceId",
		"orderIndex",
		"classSignature",
		"objectId",
	};
	
	/** default constants. */
	public static final String CLASS_SIGNATURE = "";
	public static final int OBJECT_ID = 0;
	private static final long serialVersionUID = 5435515970872711524L;
	
	/** property declarations. */
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
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public ConstructionEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		super(deserializer);
		this.classSignature = deserializer.getString();
		this.objectId = deserializer.getInt();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp());
		serializer.putLong(this.getTraceId());
		serializer.putInt(this.getOrderIndex());
		serializer.putString(this.getClassSignature());
		serializer.putInt(this.getObjectId());
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
		
		final ConstructionEvent castedRecord = (ConstructionEvent) obj;
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
		if (!this.getClassSignature().equals(castedRecord.getClassSignature())) {
			return false;
		}
		if (this.getObjectId() != castedRecord.getObjectId()) {
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
		code += ((int)this.getTimestamp());
		code += ((int)this.getTraceId());
		code += ((int)this.getOrderIndex());
		code += this.getClassSignature().hashCode();
		code += ((int)this.getObjectId());
		
		return code;
	}
	
	public final String getClassSignature() {
		return this.classSignature;
	}
	
	
	public final int getObjectId() {
		return this.objectId;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "ConstructionEvent: ";
		result += "timestamp = ";
		result += this.getTimestamp() + ", ";
		
		result += "traceId = ";
		result += this.getTraceId() + ", ";
		
		result += "orderIndex = ";
		result += this.getOrderIndex() + ", ";
		
		result += "classSignature = ";
		result += this.getClassSignature() + ", ";
		
		result += "objectId = ";
		result += this.getObjectId() + ", ";
		
		return result;
	}
}
