/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.monitoring.probe.aspectj.beforeafter.onlycallee;

import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.TraceRegistry;
import kieker.monitoring.probe.aspectj.AbstractAspectJProbe;
import kieker.monitoring.timer.ITimeSource;

/**
 * This aspect spawns before and after events by foregoing an around advice.
 * Instead, it uses before and after advices only so that "cflow" can be used when specifying its pointcut.
 *
 * This implementation uses <code>JoinPoint.StaticPart</code> instead of <code>JoinPoint</code> in the advices for performance reasons:
 * <blockquote>If you only need the static information about the join point, you may access the static part of the join point directly with the special variable
 * thisJoinPointStaticPart.
 * Using thisJoinPointStaticPart will avoid the run-time creation of the join point object that may be necessary when using thisJoinPoint directly.
 * </blockquote>
 *
 * @author Christian Wulf (chw)
 *
 * @since 1.13
 *
 */
@Aspect
public abstract class AbstractAspect extends AbstractAspectJProbe { // NOPMD

	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();
	private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;

	// private final ThreadLocal<Counter> currentOrderIndex = new ThreadLocal<Counter>() {
	// @Override
	// protected Counter initialValue() {
	// return new Counter();
	// }
	// };
	private final ThreadLocal<Counter> currentStackIndex = new ThreadLocal<Counter>() {
		@Override
		protected Counter initialValue() {
			return new Counter();
		}
	};

	/**
	 * The pointcut for the monitored operations. Inheriting classes should
	 * extend the pointcut in order to find the correct executions of the
	 * methods (e.g. all methods or only methods with specific annotations).
	 */
	@Pointcut
	public abstract void monitoredOperation();

	@Before("monitoredOperation() && notWithinKieker()")
	public void beforeOperation(final StaticPart jpStaticPart) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		final String operationSignature = this.signatureToLongString(jpStaticPart.getSignature());
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}

		TraceMetadata trace = TRACEREGISTRY.getTrace();
		final boolean newTrace = trace == null;
		if (newTrace) {
			trace = TRACEREGISTRY.registerTrace(); // TO-DO parent trace is never used, so reduce impl. (chw)
			CTRLINST.newMonitoringRecord(trace);
		}

		// long threadId = Thread.currentThread().getId();
		// int orderIndex = currentOrderIndex.get().incrementValue();
		// int stackIndex =
		this.currentStackIndex.get().incrementValue();

		final long traceId = trace.getTraceId();
		final String typeName = jpStaticPart.getSignature().getDeclaringTypeName();
		// measure before execution
		CTRLINST.newMonitoringRecord(
				new BeforeOperationEvent(TIME.getTime(), traceId, trace.getNextOrderId(), operationSignature, typeName));
	}

	@AfterReturning("monitoredOperation() && notWithinKieker()")
	public void afterReturningOperation(final StaticPart jpStaticPart) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}

		final String operationSignature = this.signatureToLongString(jpStaticPart.getSignature());
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}

		final TraceMetadata trace = TRACEREGISTRY.getTrace();
		final String typeName = jpStaticPart.getSignature().getDeclaringTypeName();

		CTRLINST.newMonitoringRecord(new AfterOperationEvent(TIME.getTime(), trace.getTraceId(), trace.getNextOrderId(), operationSignature, typeName));
	}

	@AfterThrowing(pointcut = "monitoredOperation() && notWithinKieker()", throwing = "th")
	public void afterThrowing(final StaticPart jpStaticPart, final Throwable th) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}

		final String operationSignature = this.signatureToLongString(jpStaticPart.getSignature());
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}

		final TraceMetadata trace = TRACEREGISTRY.getTrace();
		final String typeName = jpStaticPart.getSignature().getDeclaringTypeName();

		CTRLINST.newMonitoringRecord(
				new AfterOperationFailedEvent(TIME.getTime(), trace.getTraceId(),
						trace.getNextOrderId(), operationSignature, typeName, th.toString()));
	}

	@After("monitoredOperation() && notWithinKieker()")
	public void afterOperation(final StaticPart jpStaticPart) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}

		final String operationSignature = this.signatureToLongString(jpStaticPart.getSignature());
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}

		final int stackIndex = this.currentStackIndex.get().decrementValue();
		if (stackIndex == 1) {
			TRACEREGISTRY.unregisterTrace();
		}
	}
}
