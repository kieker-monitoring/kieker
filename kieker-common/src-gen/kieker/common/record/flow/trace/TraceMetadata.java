/***************************************************************************
 * Copyright 2018 iObserve Project (https://www.iobserve-devops.net)
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
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

import kieker.common.record.flow.IFlowRecord;

/**
 * @author Jan Waller
 * API compatibility: Kieker 1.15.0
 * 
 * @since 1.5
 */
public class TraceMetadata extends AbstractMonitoringRecord implements IFlowRecord {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // TraceMetadata.traceId
			 + TYPE_SIZE_LONG // TraceMetadata.threadId
			 + TYPE_SIZE_STRING // TraceMetadata.sessionId
			 + TYPE_SIZE_STRING // TraceMetadata.hostname
			 + TYPE_SIZE_LONG // TraceMetadata.parentTraceId
			 + TYPE_SIZE_INT; // TraceMetadata.parentOrderId
	
	public static final Class<?>[] TYPES = {
		long.class, // TraceMetadata.traceId
		long.class, // TraceMetadata.threadId
		String.class, // TraceMetadata.sessionId
		String.class, // TraceMetadata.hostname
		long.class, // TraceMetadata.parentTraceId
		int.class, // TraceMetadata.parentOrderId
	};
	
	/** user-defined constants. */
	public static final long NO_PARENT_TRACEID = -1L;
	public static final int NO_PARENT_ORDER_INDEX = -1;
	public static final String NO_SESSION_ID = "<no-session-id>";
	public static final String NO_HOSTNAME = "<default-host>";
	/** default constants. */
	public static final long TRACE_ID = 0L;
	public static final long THREAD_ID = 0L;
	public static final String SESSION_ID = NO_SESSION_ID;
	public static final String HOSTNAME = NO_HOSTNAME;
	public static final long PARENT_TRACE_ID = NO_PARENT_TRACEID;
	public static final int PARENT_ORDER_ID = NO_PARENT_ORDER_INDEX;
	public static final int NEXT_ORDER_ID = 0;
	private static final long serialVersionUID = 2517933148667588979L;
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"traceId",
		"threadId",
		"sessionId",
		"hostname",
		"parentTraceId",
		"parentOrderId",
	};
	
	/** property declarations. */
	private long traceId;
	private final long threadId;
	private final String sessionId;
	private final String hostname;
	private final long parentTraceId;
	private final int parentOrderId;
	private int nextOrderId = 0;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param traceId
	 *            traceId
	 * @param threadId
	 *            threadId
	 * @param sessionId
	 *            sessionId
	 * @param hostname
	 *            hostname
	 * @param parentTraceId
	 *            parentTraceId
	 * @param parentOrderId
	 *            parentOrderId
	 */
	public TraceMetadata(final long traceId, final long threadId, final String sessionId, final String hostname, final long parentTraceId, final int parentOrderId) {
		this.traceId = traceId;
		this.threadId = threadId;
		this.sessionId = sessionId == null?NO_SESSION_ID:sessionId;
		this.hostname = hostname == null?NO_HOSTNAME:hostname;
		this.parentTraceId = parentTraceId;
		this.parentOrderId = parentOrderId;
	}



	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public TraceMetadata(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.traceId = deserializer.getLong();
		this.threadId = deserializer.getLong();
		this.sessionId = deserializer.getString();
		this.hostname = deserializer.getString();
		this.parentTraceId = deserializer.getLong();
		this.parentOrderId = deserializer.getInt();
		this.nextOrderId = 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putLong(this.getTraceId());
		serializer.putLong(this.getThreadId());
		serializer.putString(this.getSessionId());
		serializer.putString(this.getHostname());
		serializer.putLong(this.getParentTraceId());
		serializer.putInt(this.getParentOrderId());
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
		return PROPERTY_NAMES; // NOPMD
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
		
		final TraceMetadata castedRecord = (TraceMetadata) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (this.getTraceId() != castedRecord.getTraceId()) {
			return false;
		}
		if (this.getThreadId() != castedRecord.getThreadId()) {
			return false;
		}
		if (!this.getSessionId().equals(castedRecord.getSessionId())) {
			return false;
		}
		if (!this.getHostname().equals(castedRecord.getHostname())) {
			return false;
		}
		if (this.getParentTraceId() != castedRecord.getParentTraceId()) {
			return false;
		}
		if (this.getParentOrderId() != castedRecord.getParentOrderId()) {
			return false;
		}
		if (this.getNextOrderId() != castedRecord.getNextOrderId()) {
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
		code += ((int)this.getTraceId());
		code += ((int)this.getThreadId());
		code += this.getSessionId().hashCode();
		code += this.getHostname().hashCode();
		code += ((int)this.getParentTraceId());
		code += ((int)this.getParentOrderId());
		code += ((int)this.getNextOrderId());
		
		return code;
	}
	
	public final long getTraceId() {
		return this.traceId;
	}
	
	public final void setTraceId(long traceId) {
		this.traceId = traceId;
	}
	
	public final long getThreadId() {
		return this.threadId;
	}
	
	
	public final String getSessionId() {
		return this.sessionId;
	}
	
	
	public final String getHostname() {
		return this.hostname;
	}
	
	
	public final long getParentTraceId() {
		return this.parentTraceId;
	}
	
	
	public final int getParentOrderId() {
		return this.parentOrderId;
	}
	
	
	public final int getNextOrderId() {
		return this.nextOrderId++;
	}
	
}
