/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
	private static final long serialVersionUID = -4734457252539987221L;
	private static final Class<?>[] TYPES = {
		long.class, // traceId
		long.class, // threadId
		String.class, // sessionId
		String.class, // hostname
		long.class, // parentTraceId
		int.class, // parentOrderId
	};

	/**
	 * Constant to be used if no sessionId required.
	 */
	public static final String NO_SESSION_ID = "<no-session-id>";

	/**
	 * Constant to be used if no hostname required.
	 */
	public static final String NO_HOSTNAME = "<default-host>";

	private final long traceId;
	private final long threadId;
	private final String sessionId;
	private final String hostname;
	private final long parentTraceId;
	private final int parentOrderId;
	private transient int nextOrderId; // used only thread local!

	public Trace(final long traceId, final long threadId, final String sessionId, final String hostname, final long parentTraceId, final int parentOrderId) {
		this.traceId = traceId;
		this.threadId = threadId;
		this.sessionId = (sessionId == null) ? Trace.NO_SESSION_ID : sessionId; // NOCS
		this.hostname = (hostname == null) ? Trace.NO_HOSTNAME : hostname; // NOCS
		this.parentTraceId = parentTraceId;
		this.parentOrderId = parentOrderId;
	}

	public Trace(final Object[] values) {
		AbstractMonitoringRecord.checkArray(values, Trace.TYPES);
		this.traceId = (Long) values[0];
		this.threadId = (Long) values[1];
		this.sessionId = (String) values[2];
		this.hostname = (String) values[3];
		this.parentTraceId = (Long) values[4];
		this.parentOrderId = (Integer) values[5];
	}

	@Override
	public final Object[] toArray() {
		return new Object[] { this.traceId, this.threadId, this.sessionId, this.hostname, this.parentTraceId, this.parentOrderId, };
	}

	@Override
	public final Class<?>[] getValueTypes() {
		return Trace.TYPES.clone();
	}

	@Override
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

	public String getSessionId() {
		return this.sessionId;
	}

	public String getHostname() {
		return this.hostname;
	}

	public long getParentTraceId() {
		return this.parentTraceId;
	}

	public int getParentOrderId() {
		return this.parentOrderId;
	}

	public int getNextOrderId() {
		return this.nextOrderId++;
	}
}
