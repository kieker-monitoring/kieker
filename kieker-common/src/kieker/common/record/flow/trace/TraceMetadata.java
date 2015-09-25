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

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 *
 * @since 1.5
 */
public class TraceMetadata extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory, IFlowRecord {
	public static final int SIZE = (2 * TYPE_SIZE_LONG) + (2 * TYPE_SIZE_STRING) + TYPE_SIZE_LONG + TYPE_SIZE_INT;
	public static final Class<?>[] TYPES = {
		long.class, // traceId
		long.class, // threadId
		String.class, // sessionId
		String.class, // hostname
		long.class, // parentTraceId
		int.class, // parentOrderId
	};

	/** Constant to be used if no sessionId required. */
	public static final String NO_SESSION_ID = "<no-session-id>";
	/** Constant to be used if no hostname required. */
	public static final String NO_HOSTNAME = "<default-host>";
	/** Constant to be used if no trace parent ID required. */
	public static final long NO_PARENT_TRACEID = -1;
	/** Constant to be used if no trace parent order index required. */
	public static final int NO_PARENT_ORDER_INDEX = -1;

	private static final long serialVersionUID = 5676916633570574411L;

	private final long traceId;
	private final long threadId;
	private final String sessionId;
	private final String hostname;
	private final long parentTraceId;
	private final int parentOrderId;
	private transient int nextOrderId = 0; // used only thread local! // NOPMD (transient init)

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param traceId
	 *            The trace ID.
	 * @param threadId
	 *            The thread ID.
	 * @param sessionId
	 *            the session ID; use {@link #NO_SESSION_ID} if no session ID desired.
	 * @param hostname
	 *            the host name; use {@link #NO_HOSTNAME} if no host name desired.
	 * @param parentTraceId
	 *            the ID of the parent trace; use {@link #NO_PARENT_TRACEID} if not desired.
	 * @param parentOrderId
	 *            the order index within the parent trace; use {@link #NO_PARENT_ORDER_INDEX} if not desired.
	 */
	public TraceMetadata(final long traceId, final long threadId, final String sessionId, final String hostname, final long parentTraceId, final int parentOrderId) {
		this.traceId = traceId;
		this.threadId = threadId;
		this.sessionId = (sessionId == null) ? NO_SESSION_ID : sessionId; // NOCS
		this.hostname = (hostname == null) ? NO_HOSTNAME : hostname; // NOCS
		this.parentTraceId = parentTraceId;
		this.parentOrderId = parentOrderId;
	}

	/**
	 * Creates a new instance of this class using the given parameter.
	 *
	 * @param values
	 *            The array containing the values for the fields of this class. This should normally be the array resulting in a call to {@link #toArray()}.
	 */
	public TraceMetadata(final Object[] values) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, TYPES);
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
	@Override
	public Object[] toArray() {
		return new Object[] { this.getTraceId(), this.getThreadId(), this.getSessionId(), this.getHostname(), this.getParentTraceId(), this.getParentOrderId(), };
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
	 *
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public final void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.BinaryFactory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public final void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		throw new UnsupportedOperationException();
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
	 */
	@Override
	protected boolean equalsInternal(final kieker.common.record.IMonitoringRecord record) {
		final TraceMetadata castedRecord = (TraceMetadata) record;
		if (this.traceId != castedRecord.traceId) {
			return false;
		}
		if (this.threadId != castedRecord.threadId) {
			return false;
		}
		if (!this.sessionId.equals(castedRecord.sessionId)) {
			return false;
		}
		if (!this.hostname.equals(castedRecord.hostname)) {
			return false;
		}
		if (this.parentTraceId != castedRecord.parentTraceId) {
			return false;
		}
		if (this.parentOrderId != castedRecord.parentOrderId) {
			return false;
		}
		if (this.nextOrderId != castedRecord.nextOrderId) {
			return false;
		}
		return super.equalsInternal(castedRecord);
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

	public final int getNextOrderId() {
		return this.nextOrderId++;
	}
}
