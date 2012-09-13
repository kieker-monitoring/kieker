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

package kieker.common.record.flow.trace.concurrency.monitor;

import kieker.common.record.flow.trace.AbstractTraceEvent;

/**
 * @author Jan Waller
 */
public abstract class AbstractMonitorEvent extends AbstractTraceEvent {
	private static final long serialVersionUID = -1L;
	private static final Class<?>[] TYPES = {
		long.class, // Event.timestamp
		long.class, // TraceEvent.traceId
		int.class, // TraceEvent.orderIndex
		int.class, // lockId
	};

	private final int lockId;

	public AbstractMonitorEvent(final long timestamp, final long traceId, final int orderIndex, final int lockId) {
		super(timestamp, traceId, orderIndex);
		this.lockId = lockId;
	}

	protected AbstractMonitorEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes); // values[0..2]
		this.lockId = (Integer) values[3];
	}

	public Object[] toArray() {
		return new Object[] { this.getTimestamp(), this.getTraceId(), this.getOrderIndex(), this.lockId, };
	}

	public Class<?>[] getValueTypes() {
		return TYPES.clone();
	}

	public final int getLockId() {
		return this.lockId;
	}
}
