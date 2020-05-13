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
package kieker.common.record.flow.trace;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;


/**
 * @author Felix Eichhorst
 * API compatibility: Kieker 1.15.0
 * 
 * @since 1.14
 */
public class BeforeSentRemoteEvent extends AbstractMonitoringRecord  {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // BeforeSentRemoteEvent.timestamp
			 + TYPE_SIZE_LONG // BeforeSentRemoteEvent.traceId
			 + TYPE_SIZE_INT // BeforeSentRemoteEvent.orderIndex
			 + TYPE_SIZE_STRING; // BeforeSentRemoteEvent.technology
	
	public static final Class<?>[] TYPES = {
		long.class, // BeforeSentRemoteEvent.timestamp
		long.class, // BeforeSentRemoteEvent.traceId
		int.class, // BeforeSentRemoteEvent.orderIndex
		String.class, // BeforeSentRemoteEvent.technology
	};
	
	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"timestamp",
		"traceId",
		"orderIndex",
		"technology",
	};
	
	/** default constants. */
	public static final long TIMESTAMP = -1L;
	public static final long TRACE_ID = -1L;
	public static final int ORDER_INDEX = -1;
	public static final String TECHNOLOGY = "<default-technology>";
	private static final long serialVersionUID = 1817999525650163947L;
	
	/** property declarations. */
	private final long timestamp;
	private final long traceId;
	private final int orderIndex;
	private final String technology;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param traceId
	 *            traceId
	 * @param orderIndex
	 *            orderIndex
	 * @param technology
	 *            technology
	 */
	public BeforeSentRemoteEvent(final long timestamp, final long traceId, final int orderIndex, final String technology) {
		this.timestamp = timestamp;
		this.traceId = traceId;
		this.orderIndex = orderIndex;
		this.technology = technology == null?TECHNOLOGY:technology;
	}


	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public BeforeSentRemoteEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.timestamp = deserializer.getLong();
		this.traceId = deserializer.getLong();
		this.orderIndex = deserializer.getInt();
		this.technology = deserializer.getString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp());
		serializer.putLong(this.getTraceId());
		serializer.putInt(this.getOrderIndex());
		serializer.putString(this.getTechnology());
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
		
		final BeforeSentRemoteEvent castedRecord = (BeforeSentRemoteEvent) obj;
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
		if (!this.getTechnology().equals(castedRecord.getTechnology())) {
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
		code += this.getTechnology().hashCode();
		
		return code;
	}
	
	public final long getTimestamp() {
		return this.timestamp;
	}
	
	
	public final long getTraceId() {
		return this.traceId;
	}
	
	
	public final int getOrderIndex() {
		return this.orderIndex;
	}
	
	
	public final String getTechnology() {
		return this.technology;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "BeforeSentRemoteEvent: ";
		result += "timestamp = ";
		result += this.getTimestamp() + ", ";
		
		result += "traceId = ";
		result += this.getTraceId() + ", ";
		
		result += "orderIndex = ";
		result += this.getOrderIndex() + ", ";
		
		result += "technology = ";
		result += this.getTechnology() + ", ";
		
		return result;
	}
}
