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

package kieker.monitoring.core.registry;

import java.security.SecureRandom;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import kieker.common.record.flow.trace.Trace;
import kieker.monitoring.core.controller.MonitoringController;

/**
 * @author Jan Waller
 */
public enum TraceRegistry { // Singleton (Effective Java #3)
	INSTANCE;

	private final AtomicInteger nextTraceId = new AtomicInteger(0);
	private final long unique = ((long) new SecureRandom().nextInt()) << 32;
	/** the hostname is final after the instantiation of the monitoring controller */
	private final String hostname = MonitoringController.getInstance().getHostName();

	/** the current trace; null if new trace */
	private final ThreadLocal<Trace> traceStorage = new ThreadLocal<Trace>();
	/** store the parent Trace */
	private final WeakHashMap<Thread, TracePoint> parentTrace = new WeakHashMap<Thread, TracePoint>();

	private final long getId() {
		return this.unique | this.nextTraceId.getAndIncrement();
	}

	/**
	 * Gets a Trace for the current thread. If no trace is active, null is returned.
	 * 
	 * @return
	 *         Trace object or null
	 */
	public final Trace getTrace() {
		return this.traceStorage.get();
	}

	/**
	 * This creates a new unique Trace object and registers it.
	 * Should only be called if no trace was registered before (getTrace() returns null).
	 * 
	 * @return
	 *         Trace object
	 */
	public final Trace registerTrace() {
		if (this.traceStorage.get() != null) {
			throw new IllegalStateException("Tried to register a new trace, but found active trace.");
		}
		final Thread thread = Thread.currentThread();
		final TracePoint tp = this.getParentTraceId(thread);
		final long traceId = this.getId();
		final long parentTraceId;
		final int parentOrderId;
		if (tp != null) {
			parentTraceId = tp.traceId;
			parentOrderId = tp.orderId;
		} else {
			parentTraceId = traceId;
			parentOrderId = -1;
		}
		final String sessionId = SessionRegistry.INSTANCE.recallThreadLocalSessionId();
		final Trace trace = new Trace(traceId, thread.getId(), sessionId, this.hostname, parentTraceId, parentOrderId);
		this.traceStorage.set(trace);
		return trace;
	}

	/**
	 * Unregisters the current Trace object. Future calls of getTrace() will return null.
	 */
	public final void unregisterTrace() {
		this.traceStorage.remove();
	}

	private static final class TracePoint {
		public final long traceId;
		public final int orderId;

		public TracePoint(final long traceId, final int orderId) {
			this.traceId = traceId;
			this.orderId = orderId;
		}
	}

	private final TracePoint getParentTraceId(final Thread t) {
		synchronized (this) {
			return this.parentTrace.get(t);
		}
	}

	public final void setParentTraceId(final Thread t, final long traceId, final int orderId) {
		synchronized (this) {
			this.parentTrace.put(t, new TracePoint(traceId, orderId));
		}
	}
}
