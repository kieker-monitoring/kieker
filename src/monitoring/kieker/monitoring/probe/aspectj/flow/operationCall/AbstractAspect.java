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

package kieker.monitoring.probe.aspectj.flow.operationCall;

import kieker.common.record.flow.trace.Trace;
import kieker.common.record.flow.trace.operation.CallOperationEvent;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.TraceRegistry;
import kieker.monitoring.probe.aspectj.AbstractAspectJProbe;
import kieker.monitoring.timer.ITimeSource;

import org.aspectj.lang.JoinPoint.EnclosingStaticPart;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Jan Waller
 */
@Aspect
public abstract class AbstractAspect extends AbstractAspectJProbe {
	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = AbstractAspect.CTRLINST.getTimeSource();
	private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;

	@Pointcut
	public abstract void monitoredOperation();

	@Around("monitoredOperation() && notWithinKieker()")
	public Object operation(final ProceedingJoinPoint thisJoinPoint, final EnclosingStaticPart thisEnclosingJoinPoint) throws Throwable {
		if (!AbstractAspect.CTRLINST.isMonitoringEnabled()) {
			return thisJoinPoint.proceed();
		}
		// common fields
		Trace trace = AbstractAspect.TRACEREGISTRY.getTrace();
		final boolean newTrace = (trace == null);
		if (newTrace) {
			trace = AbstractAspect.TRACEREGISTRY.registerTrace();
			AbstractAspect.CTRLINST.newMonitoringRecord(trace);
		}
		final long traceId = trace.getTraceId();
		final String callee = thisJoinPoint.getSignature().toLongString();
		final String caller = thisEnclosingJoinPoint.getSignature().toLongString();
		// measure before call
		AbstractAspect.CTRLINST.newMonitoringRecord(new CallOperationEvent(AbstractAspect.TIME.getTime(), traceId, trace.getNextOrderId(), caller, callee));
		// call of the called method
		final Object retval;
		try {
			retval = thisJoinPoint.proceed();
		} catch (final Throwable th) {
			throw th;
		} finally {
			if (newTrace) { // close the trace
				AbstractAspect.TRACEREGISTRY.unregisterTrace();
			}
		}
		return retval;
	}
}
