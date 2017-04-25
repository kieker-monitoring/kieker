/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;

import kieker.common.record.flow.IFlowRecord;

/**
 * @author Jan Waller
 * 
 * @since 1.5
 */
public class TraceMetadata extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory, IFlowRecord {
	private static final long serialVersionUID = 2517933148667588979L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // TraceMetadata.traceId
			 + TYPE_SIZE_LONG // TraceMetadata.threadId
			 + TYPE_SIZE_STRING // TraceMetadata.sessionId
			 + TYPE_SIZE_STRING // TraceMetadata.hostname
			 + TYPE_SIZE_LONG // TraceMetadata.parentTraceId
			 + TYPE_SIZE_INT // TraceMetadata.parentOrderId
	;
	
	public static final Class<?>[] TYPES = {
		long.class, // TraceMetadata.traceId
		long.class, // TraceMetadata.threadId
		String.class, // TraceMetadata.sessionId
		String.class, // TraceMetadata.hostname
		long.class, // TraceMetadata.parentTraceId
		int.class, // TraceMetadata.parentOrderId
	};
	
	/** user-defined constants. */
	public static final String NO_SESSION_ID = "<no-session-id>";
	public static final String NO_HOSTNAME = "<default-host>";
	public static final long NO_PARENT_TRACEID = -1L;
	public static final int NO_PARENT_ORDER_INDEX = -1;
	
	/** default constants. */
	public static final long TRACE_ID = 0L;
	public static final long THREAD_ID = 0L;
	public static final String SESSION_ID = NO_SESSION_ID;
	public static final String HOSTNAME = NO_HOSTNAME;
	public static final long PARENT_TRACE_ID = NO_PARENT_TRACEID;
	public static final int PARENT_ORDER_ID = NO_PARENT_ORDER_INDEX;
	public static final int NEXT_ORDER_ID = 0;
	
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
	private long threadId;
	private String sessionId;
	private String hostname;
	private long parentTraceId;
	private int parentOrderId;
	private int nextOrderId;
	
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
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public TraceMetadata(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.traceId = (Long) values[0];
		this.threadId = (Long) values[1];
		this.sessionId = (String) values[2];
		this.hostname = (String) values[3];
		this.parentTraceId = (Long) values[4];
		this.parentOrderId = (Integer) values[5];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected TraceMetadata(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.traceId = (Long) values[0];
		this.threadId = (Long) values[1];
		this.sessionId = (String) values[2];
		this.hostname = (String) values[3];
		this.parentTraceId = (Long) values[4];
		this.parentOrderId = (Integer) values[5];
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
	 */
	public TraceMetadata(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.traceId = buffer.getLong();
		this.threadId = buffer.getLong();
		this.sessionId = stringRegistry.get(buffer.getInt());
		this.hostname = stringRegistry.get(buffer.getInt());
		this.parentTraceId = buffer.getLong();
		this.parentOrderId = buffer.getInt();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getTraceId(),
			this.getThreadId(),
			this.getSessionId(),
			this.getHostname(),
			this.getParentTraceId(),
			this.getParentOrderId()
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getSessionId());
		stringRegistry.get(this.getHostname());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putLong(this.getTraceId());
		buffer.putLong(this.getThreadId());
		buffer.putInt(stringRegistry.get(this.getSessionId()));
		buffer.putInt(stringRegistry.get(this.getHostname()));
		buffer.putLong(this.getParentTraceId());
		buffer.putInt(this.getParentOrderId());
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
	public boolean equals(final Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (obj.getClass() != this.getClass()) return false;
		
		final TraceMetadata castedRecord = (TraceMetadata) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTraceId() != castedRecord.getTraceId()) return false;
		if (this.getThreadId() != castedRecord.getThreadId()) return false;
		if (!this.getSessionId().equals(castedRecord.getSessionId())) return false;
		if (!this.getHostname().equals(castedRecord.getHostname())) return false;
		if (this.getParentTraceId() != castedRecord.getParentTraceId()) return false;
		if (this.getParentOrderId() != castedRecord.getParentOrderId()) return false;
		if (this.getNextOrderId() != castedRecord.getNextOrderId()) return false;
		return true;
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
	
	public final void setThreadId(long threadId) {
		this.threadId = threadId;
	}
	
	public final String getSessionId() {
		return this.sessionId;
	}
	
	public final void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public final String getHostname() {
		return this.hostname;
	}
	
	public final void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	public final long getParentTraceId() {
		return this.parentTraceId;
	}
	
	public final void setParentTraceId(long parentTraceId) {
		this.parentTraceId = parentTraceId;
	}
	
	public final int getParentOrderId() {
		return this.parentOrderId;
	}
	
	public final void setParentOrderId(int parentOrderId) {
		this.parentOrderId = parentOrderId;
	}
	
	public final int getNextOrderId() {
		return this.nextOrderId++;
	}
	
	public final void setNextOrderId(int nextOrderId) {
		this.nextOrderId = nextOrderId;
	}
}
