/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
import java.util.Stack;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.record.flow.trace.ApplicationTraceMetadata;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.monitoring.core.controller.MonitoringController;

/**
 * @author Jan Waller
 *
 * @since 1.5
 */
public enum TraceRegistry { // Singleton (Effective Java #3)
	/** The singleton instance. */
	INSTANCE;

	private static final Logger LOGGER = LoggerFactory.getLogger(TraceRegistry.class); // NOPMD (enum logger)

	private final AtomicInteger nextTraceId = new AtomicInteger(0);
	private final long unique = MonitoringController.getInstance().isDebug() ? 0 : ((long) new SecureRandom().nextInt()) << 32; // NOCS
	/** the hostname is final after the instantiation of the monitoring controller. */
	private final String hostname = MonitoringController.getInstance().getHostname();

	/** the current trace; null if new trace. */
	private final ThreadLocal<TraceMetadata> traceStorage = new ThreadLocal<>();

	/** used to store the stack of enclosing traces; null if no sub trace created yet. */
	private final ThreadLocal<Stack<TraceMetadata>> enclosingTraceStack = new ThreadLocal<>();

	/** store the parent Trace. */
	private final WeakHashMap<Thread, TracePoint> parentTrace = new WeakHashMap<>();

	private final long getNewId() {
		return this.unique | this.nextTraceId.getAndIncrement();
	}

	/**
	 * Gets a Trace for the current thread. If no trace is active, null is returned.
	 *
	 * @return
	 *         Trace object or null
	 */
	public final TraceMetadata getTrace() {
		return this.traceStorage.get();
	}

	/**
	 * This creates a new unique Trace object and registers it.
	 *
	 * @return
	 *         Trace object
	 */
	public final ApplicationTraceMetadata registerTrace() {
		final TraceMetadata enclosingTrace = this.getTrace();
		if (enclosingTrace != null) { // we create a subtrace
			Stack<TraceMetadata> localTraceStack = this.enclosingTraceStack.get();
			if (localTraceStack == null) {
				localTraceStack = new Stack<>();
				this.enclosingTraceStack.set(localTraceStack);
			}
			localTraceStack.push(enclosingTrace);
		}
		final Thread thread = Thread.currentThread();
		final TracePoint tp = this.getAndRemoveParentTraceId(thread);
		final long traceId = this.getNewId();
		final long parentTraceId;
		final int parentOrderId;
		if (tp != null) { // we have a known split point
			if ((enclosingTrace != null) && (enclosingTrace.getTraceId() != tp.traceId)) {
				LOGGER.error("Enclosing trace does not match split point. Found: {} expected: {}", enclosingTrace.getTraceId(), enclosingTrace.getTraceId());
			}
			parentTraceId = tp.traceId;
			parentOrderId = tp.orderId;
		} else if (enclosingTrace != null) { // we create a sub trace without a known split point
			parentTraceId = enclosingTrace.getTraceId();
			parentOrderId = -1; // we could instead get the last orderId ... But this would make it harder to distinguish from known split points
		} else { // we create a new trace without a parent
			parentTraceId = traceId;
			parentOrderId = -1;
		}
		final String sessionId = SessionRegistry.INSTANCE.recallThreadLocalSessionId();
		final String applicationName = MonitoringController.getInstance().getApplicationName();
		final ApplicationTraceMetadata trace = new ApplicationTraceMetadata(traceId, thread.getId(), sessionId, this.hostname, parentTraceId, parentOrderId,
				applicationName);
		this.traceStorage.set(trace);
		return trace;
	}

	/**
	 * Unregisters the current Trace object.
	 *
	 * Future calls of getTrace() will either return null or the enclosing trace object.
	 */
	public final void unregisterTrace() {
		final Stack<TraceMetadata> localTraceStack = this.enclosingTraceStack.get();
		if (localTraceStack != null) { // we might have an enclosing trace and and are able to restore it
			if (!localTraceStack.isEmpty()) { // we actually found something
				this.traceStorage.set(localTraceStack.pop());
			} else {
				this.enclosingTraceStack.remove();
				this.traceStorage.remove();
			}
		} else {
			this.traceStorage.remove();
		}
	}

	private final TracePoint getAndRemoveParentTraceId(final Thread t) {
		synchronized (this) {
			return this.parentTrace.remove(t);
		}
	}

	/**
	 * Sets the parent for the next created trace inside this thread.
	 * This method should be used by probes in connection with SpliEvents.
	 *
	 * @param t
	 *            the thread the new trace belongs to
	 * @param traceId
	 *            the parent trace id
	 * @param orderId
	 *            the parent order id
	 */
	public final void setParentTraceId(final Thread t, final long traceId, final int orderId) {
		synchronized (this) {
			this.parentTrace.put(t, new TracePoint(traceId, orderId));
		}
	}

	/**
	 * @author Jan Waller
	 */
	private static final class TracePoint {
		public final long traceId; // NOCS (public no setters or getters)
		public final int orderId; // NOCS (public no setters or getters)

		public TracePoint(final long traceId, final int orderId) {
			this.traceId = traceId;
			this.orderId = orderId;
		}
	}
}
