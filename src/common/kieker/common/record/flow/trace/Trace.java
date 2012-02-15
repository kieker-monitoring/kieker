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
		long.class, // parentTraceId
		int.class, // parentOrderId
	};

	// TODO: sessionid, host ?

	private final long traceId;
	private final long threadId;
	private final long parentTraceId;
	private final int parentOrderId;
	private transient int nextOrderId;

	public Trace(final long traceId, final long threadId, final long parentTraceId, final int parentOrderId) {
		this.traceId = traceId;
		this.threadId = threadId;
		this.parentTraceId = parentTraceId;
		this.parentOrderId = parentOrderId;
	}

	public Trace(final Object[] values) {
		AbstractMonitoringRecord.checkArray(values, Trace.TYPES);
		this.traceId = (Long) values[0];
		this.threadId = (Long) values[1];
		this.parentTraceId = (Long) values[2];
		this.parentOrderId = (Integer) values[3];
	}

	@Override
	public final Object[] toArray() {
		return new Object[] { this.traceId, this.threadId, this.parentTraceId, this.parentOrderId, };
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
