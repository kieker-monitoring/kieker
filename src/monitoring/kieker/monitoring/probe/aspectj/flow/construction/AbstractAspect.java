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

package kieker.monitoring.probe.aspectj.flow.construction;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import kieker.common.record.flow.trace.ConstructionEvent;
import kieker.common.record.flow.trace.Trace;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.TraceRegistry;
import kieker.monitoring.probe.aspectj.AbstractAspectJProbe;
import kieker.monitoring.timer.ITimeSource;

/**
 * @author Jan Waller
 */
@Aspect
public abstract class AbstractAspect extends AbstractAspectJProbe {
	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();
	private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;

	@Pointcut
	public abstract void monitoredConstructor();

	// FIXME: this may be logged multiple times due to super constructor calls...
	@AfterReturning("monitoredConstructor() && this(thisObject) && notWithinKieker()")
	public void afterConstruction(final Object thisObject, final JoinPoint.StaticPart jp) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		// common fields
		Trace trace = TRACEREGISTRY.getTrace();
		final boolean newTrace = trace == null;
		if (newTrace) {
			trace = TRACEREGISTRY.registerTrace();
			CTRLINST.newMonitoringRecord(trace);
		}
		final ConstructionEvent crecord = new ConstructionEvent(TIME.getTime(), trace.getTraceId(), trace.getNextOrderId(),
				jp.getSignature().getDeclaringTypeName(), System.identityHashCode(thisObject));
		CTRLINST.newMonitoringRecord(crecord);
	}
}
