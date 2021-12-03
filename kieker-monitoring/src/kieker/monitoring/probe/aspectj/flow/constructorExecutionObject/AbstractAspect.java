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

package kieker.monitoring.probe.aspectj.flow.constructorExecutionObject;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.constructor.object.AfterConstructorFailedObjectEvent;
import kieker.common.record.flow.trace.operation.constructor.object.AfterConstructorObjectEvent;
import kieker.common.record.flow.trace.operation.constructor.object.BeforeConstructorObjectEvent;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.TraceRegistry;
import kieker.monitoring.probe.aspectj.AbstractAspectJProbe;
import kieker.monitoring.probe.aspectj.beforeafter.onlycallee.Counter;
import kieker.monitoring.timer.ITimeSource;

/**
 * @author Jan Waller
 *
 * @since 1.10
 */
@Aspect
public abstract class AbstractAspect extends AbstractAspectJProbe { // NOPMD
	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();
	private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;

	/**
	 * The pointcut for the monitored constructors. Inheriting classes should extend the pointcut in order to find the correct constructor executions (e.g. all
	 * constructors or only constructors with specific annotations).
	 */
	@Pointcut
	public abstract void monitoredConstructor();

	private final ThreadLocal<Counter> currentStackIndex = new ThreadLocal<Counter>() {
		@Override
		protected Counter initialValue() {
			return new Counter();
		}
	};
	
	@Before("monitoredConstructor() && this(thisObject) && notWithinKieker()")
	public void beforeConstructor(final Object thisObject, final JoinPoint thisJoinPoint) throws Throwable { // NOCS
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		final String operationSignature = this.signatureToLongString(thisJoinPoint.getSignature());
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}
		// common fields
		TraceMetadata trace = TRACEREGISTRY.getTrace();
		final boolean newTrace = trace == null;
		if (newTrace) {
			trace = TRACEREGISTRY.registerTrace();
			CTRLINST.newMonitoringRecord(trace);
		}
		currentStackIndex.get().incrementValue();
		final long traceId = trace.getTraceId();
		final String clazz = thisJoinPoint.getSignature().getDeclaringTypeName();
		// measure before execution
		final int objectId = System.identityHashCode(thisObject);
		CTRLINST.newMonitoringRecord(new BeforeConstructorObjectEvent(TIME.getTime(), traceId, trace.getNextOrderId(), operationSignature, clazz, objectId));
	}
	
	@AfterReturning("monitoredConstructor() && this(thisObject) && notWithinKieker()")
	public void afterConstructor(final Object thisObject, final JoinPoint thisJoinPoint) throws Throwable { // NOCS
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		final String operationSignature = this.signatureToLongString(thisJoinPoint.getSignature());
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}
		
		TraceMetadata trace = TRACEREGISTRY.getTrace();
		final long traceId = trace.getTraceId();
		final String clazz = thisJoinPoint.getSignature().getDeclaringTypeName();
		final int objectId = System.identityHashCode(thisObject);
		
		// measure after successful execution
		CTRLINST.newMonitoringRecord(
				new AfterConstructorObjectEvent(TIME.getTime(), traceId, trace.getNextOrderId(), operationSignature, clazz, objectId));
	}

	@AfterThrowing(pointcut="monitoredConstructor() && this(thisObject) && notWithinKieker()", throwing="th")
	public void afterConstructorThrowing(final Object thisObject, final JoinPoint thisJoinPoint, final Throwable th)
			throws Throwable { // NOCS (Throwable)
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		final String operationSignature = this.signatureToLongString(thisJoinPoint.getSignature());
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}
		
		TraceMetadata trace = TRACEREGISTRY.getTrace();
		final long traceId = trace.getTraceId();
		final String clazz = thisJoinPoint.getSignature().getDeclaringTypeName();
		final int objectId = System.identityHashCode(thisObject);
		
		CTRLINST.newMonitoringRecord(new AfterConstructorFailedObjectEvent(TIME.getTime(), traceId, trace.getNextOrderId(),
				operationSignature, clazz, th.toString(), objectId));
	}
	
	@After("monitoredConstructor() && notWithinKieker()")
	public void afterOperation(final JoinPoint thisJoinPoint) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}

		final String operationSignature = this.signatureToLongString(thisJoinPoint.getSignature());
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}

		final int stackIndex = this.currentStackIndex.get().decrementValue();
		if (stackIndex == 1) {
			TRACEREGISTRY.unregisterTrace();
		}
	}
}
