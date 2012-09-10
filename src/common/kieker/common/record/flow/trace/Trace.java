/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.IFlowRecord;

/**
 * @author Jan Waller
 */
public final class Trace extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IFlowRecord {

	/**
	 * Constant to be used if no sessionId required.
	 */
	public static final String NO_SESSION_ID = "<no-session-id>";

	/**
	 * Constant to be used if no hostname required.
	 */
	public static final String NO_HOSTNAME = "<default-host>";

	/**
	 * Constant to be used if no trace parent ID required.
	 */
	public static final long NO_PARENT_TRACEID = -1;

	/**
	 * Constant to be used if no trace parent order index required.
	 */
	public static final int NO_PARENT_ORDER_INDEX = -1;

	private static final long serialVersionUID = -4734457252539987221L;
	private static final Class<?>[] TYPES = {
		long.class, // traceId
		long.class, // threadId
		String.class, // sessionId
		String.class, // hostname
		long.class, // parentTraceId
		int.class, // parentOrderId
	};

	private final long traceId;
	private final long threadId;
	private final String sessionId;
	private final String hostname;
	private final long parentTraceId;
	private final int parentOrderId;
	private transient int nextOrderId = 0; // used only thread local! // NOPMD (transient init)

	/**
	 * 
	 * @param traceId
	 * @param threadId
	 * @param sessionId
	 *            the session ID; use {@link #NO_SESSION_ID} if no session ID desired.
	 * @param hostname
	 *            the host name; use {@link #NO_HOSTNAME} if no host name desired.
	 * @param parentTraceId
	 *            the ID of the parent trace; use {@link #NO_PARENT_TRACEID} if not desired.
	 * @param parentOrderId
	 *            the order index within the parent trace; use {@link #NO_PARENT_ORDER_INDEX} if not desired.
	 */
	public Trace(final long traceId, final long threadId, final String sessionId, final String hostname, final long parentTraceId, final int parentOrderId) {
		this.traceId = traceId;
		this.threadId = threadId;
		this.sessionId = (sessionId == null) ? NO_SESSION_ID : sessionId; // NOCS
		this.hostname = (hostname == null) ? NO_HOSTNAME : hostname; // NOCS
		this.parentTraceId = parentTraceId;
		this.parentOrderId = parentOrderId;
	}

	public Trace(final Object[] values) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.traceId = (Long) values[0];
		this.threadId = (Long) values[1];
		this.sessionId = (String) values[2];
		this.hostname = (String) values[3];
		this.parentTraceId = (Long) values[4];
		this.parentOrderId = (Integer) values[5];
	}

	public final Object[] toArray() {
		return new Object[] { this.traceId, this.threadId, this.sessionId, this.hostname, this.parentTraceId, this.parentOrderId, };
	}

	public final Class<?>[] getValueTypes() {
		return TYPES.clone();
	}

	@Deprecated
	public final void initFromArray(final Object[] values) {
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

	public final int getNextOrderId() {
		return this.nextOrderId++;
	}
}
