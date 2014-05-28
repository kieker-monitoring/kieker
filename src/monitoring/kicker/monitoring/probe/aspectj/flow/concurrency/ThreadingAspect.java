/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.monitoring.probe.aspectj.flow.concurrency;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import kicker.common.record.flow.trace.TraceMetadata;
import kicker.common.record.flow.trace.concurrency.SplitEvent;
import kicker.monitoring.core.controller.IMonitoringController;
import kicker.monitoring.core.controller.MonitoringController;
import kicker.monitoring.core.registry.TraceRegistry;
import kicker.monitoring.probe.aspectj.AbstractAspectJProbe;
import kicker.monitoring.timer.ITimeSource;

/**
 * @author Jan Waller
 * 
 * @since 1.5
 */
@Aspect
public class ThreadingAspect extends AbstractAspectJProbe {
	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();
	private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;

	/**
	 * Default constructor.
	 */
	public ThreadingAspect() {
		// empty default constructor
	}

	/**
	 * This method represents the advice which is used before the actual start of a thread.
	 * 
	 * @param thread
	 *            The thread.
	 */
	// Must be @Before
	@Before("call(void java.lang.Thread.start()) && target(thread) && notWithinKicker()")
	public void beforeNewThread(final Thread thread) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		if (!CTRLINST.isProbeActivated("public synchronized void java.lang.Thread.start()")) {
			return;
		}
		final TraceMetadata trace = TRACEREGISTRY.getTrace();
		if (trace != null) { // ignore split if not inside of a trace!
			final long traceId = trace.getTraceId();
			final int orderId = trace.getNextOrderId();
			TRACEREGISTRY.setParentTraceId(thread, traceId, orderId);
			CTRLINST.newMonitoringRecord(new SplitEvent(TIME.getTime(), traceId, orderId));
		}
	}
}
