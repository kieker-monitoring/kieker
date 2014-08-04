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

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;


/**
 * @author Generic Kieker
 * 
 * @since 1.10
 */
public class TraceMetadata extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {
	public static final int SIZE = 36; // serialization size (without variable part of strings)
	private static final long serialVersionUID = 6765437164004912631L;
	
	private static final Class<?>[] TYPES = {
		Long.class, // TraceMetadata.traceId
		Long.class, // TraceMetadata.threadId
		String.class, // TraceMetadata.sessionId
		String.class, // TraceMetadata.hostname
		Long.class, // TraceMetadata.parentTraceId
		Integer.class, // TraceMetadata.parentOrderId
	};
	
	public static final long TRACE_ID = 0L;
	public static final long THREAD_ID = 0L;
	public static final String SESSION_ID = "";
	public static final String HOSTNAME = "<nohost>";
	public static final long PARENT_TRACE_ID = 0L;
	public static final int PARENT_ORDER_ID = 0;
	
	private final long traceId;
	private final long threadId;
	private final String sessionId;
	private final String hostname;
	private final long parentTraceId;
	private final int parentOrderId;

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
		this.sessionId = sessionId;
		this.hostname = hostname;
		this.parentTraceId = parentTraceId;
		this.parentOrderId = parentOrderId;
	}

	/**
	 * This constructor converts the given array into a record. It is recommended to use the array which is the result of a call to {@link #toArray()}.
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
	 * This constructor converts the given array into a record.
	 * 
	 * @param buffer
	 *            The bytes for the record.
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
	public Class<?>[] getValueTypes() {
		return TYPES; // NOPMD
	}

	/**
	 * {@inheritDoc}
	 */
	public int getSize() {
		return SIZE;
	}
	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.BinaryFactory} mechanism. Hence, this method is not implemented.
	 */
	@Deprecated
	public void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		throw new UnsupportedOperationException();
	}

	public final long getTraceId() {
		return this.traceId;
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
	
}
