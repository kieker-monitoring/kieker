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

package kieker.monitoring.probe.aspectj.flow.concurrency;

import kieker.common.record.flow.trace.Trace;
import kieker.common.record.flow.trace.concurrency.SplitEvent;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.TraceRegistry;
import kieker.monitoring.probe.aspectj.AbstractAspectJProbe;
import kieker.monitoring.timer.ITimeSource;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @author Jan Waller
 */
@Aspect
public final class ThreadingAspect extends AbstractAspectJProbe {
	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = ThreadingAspect.CTRLINST.getTimeSource();
	private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;

	// TODO: what about other forms of executions? ThreadPoool, ...?
	// Must be @Before
	@Before("call(void java.lang.Thread.start()) && target(thread) && notWithinKieker()")
	public void beforeNewThread(final Thread thread) {
		if (!ThreadingAspect.CTRLINST.isMonitoringEnabled()) {
			return;
		}
		final Trace trace = ThreadingAspect.TRACEREGISTRY.getTrace();
		if (trace != null) { // ignore split if not inside of a trace!
			final long traceId = trace.getTraceId();
			final int orderId = trace.getNextOrderId();
			ThreadingAspect.TRACEREGISTRY.setParentTraceId(thread, traceId, orderId);
			ThreadingAspect.CTRLINST.newMonitoringRecord(new SplitEvent(ThreadingAspect.TIME.getTime(), traceId, orderId));
		}
	}
}
