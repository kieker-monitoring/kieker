/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.probe.aspectj.flow.concurrency;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.concurrency.monitor.MonitorEntryEvent;
import kieker.common.record.flow.trace.concurrency.monitor.MonitorExitEvent;
import kieker.common.record.flow.trace.concurrency.monitor.MonitorRequestEvent;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.TraceRegistry;
import kieker.monitoring.probe.aspectj.AbstractAspectJProbe;
import kieker.monitoring.timer.ITimeSource;

/**
 * This probe requires "-Xjoinpoints:synchronization" in the aop.xml.
 *
 * @author Jan Waller
 *
 * @since 1.8
 */
@Aspect
public final class SynchronizedAspect extends AbstractAspectJProbe {
	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();
	private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;

	/**
	 * Default constructor.
	 */
	public SynchronizedAspect() {
		// empty default constructor
	}

	@Before("lock() && args(lock) && notWithinKieker()")
	public void beforeMonitorEntry(final Object lock) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		final TraceMetadata trace = TRACEREGISTRY.getTrace();
		if (trace != null) { // ignore monitorRequest if not inside of a trace!
			final long traceId = trace.getTraceId();
			final int orderId = trace.getNextOrderId();
			CTRLINST.newMonitoringRecord(new MonitorRequestEvent(TIME.getTime(), traceId, orderId, System.identityHashCode(lock)));
		}
	}

	@After("lock() && args(lock) && notWithinKieker()")
	public void afterMonitorEntry(final Object lock) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		final TraceMetadata trace = TRACEREGISTRY.getTrace();
		if (trace != null) { // ignore monitorEntry if not inside of a trace!
			final long traceId = trace.getTraceId();
			final int orderId = trace.getNextOrderId();
			CTRLINST.newMonitoringRecord(new MonitorEntryEvent(TIME.getTime(), traceId, orderId, System.identityHashCode(lock)));
		}
	}

	// better to get the note when requested to release lock, or it could happen, that it seems as if two threads have the same lock
	@Before("unlock() && args(lock) && notWithinKieker()")
	public void monitorExit(final Object lock) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		final TraceMetadata trace = TRACEREGISTRY.getTrace();
		if (trace != null) { // ignore monitorExit if not inside of a trace!
			final long traceId = trace.getTraceId();
			final int orderId = trace.getNextOrderId();
			CTRLINST.newMonitoringRecord(new MonitorExitEvent(TIME.getTime(), traceId, orderId, System.identityHashCode(lock)));
		}
	}
}
