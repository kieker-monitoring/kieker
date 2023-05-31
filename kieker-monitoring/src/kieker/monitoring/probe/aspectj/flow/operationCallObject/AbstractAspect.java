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

package kieker.monitoring.probe.aspectj.flow.operationCallObject;

import org.aspectj.lang.JoinPoint.EnclosingStaticPart;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.object.CallOperationObjectEvent;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.TraceRegistry;
import kieker.monitoring.probe.aspectj.AbstractAspectJProbe;
import kieker.monitoring.timer.ITimeSource;

/**
 * @author Jan Waller
 *
 * @since 1.6
 */
@Aspect
public abstract class AbstractAspect extends AbstractAspectJProbe { // NOPMD
	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();
	private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;

	/**
	 * This is the pointcut for the monitored operations. Inheriting classes should extend this pointcut to make sure that the correct joint points will be detected.
	 */
	@Pointcut
	public abstract void monitoredOperation();

	@Around("monitoredOperation() && this(thisObject) && target(targetObject) && notWithinKieker()")
	public Object member2memberOperation(final Object thisObject, final Object targetObject, final ProceedingJoinPoint thisJoinPoint,
			final EnclosingStaticPart thisEnclosingJoinPoint) throws Throwable { // NOCS
		if (!CTRLINST.isMonitoringEnabled()) {
			return thisJoinPoint.proceed();
		}
		final String callee = this.signatureToLongString(thisJoinPoint.getSignature());
		if (!CTRLINST.isProbeActivated(callee)) {
			return thisJoinPoint.proceed();
		}
		// common fields
		TraceMetadata trace = TRACEREGISTRY.getTrace();
		final boolean newTrace = trace == null;
		if (newTrace) {
			trace = TRACEREGISTRY.registerTrace();
			CTRLINST.newMonitoringRecord(trace);
		}
		final long traceId = trace.getTraceId();
		// caller
		final String caller = this.signatureToLongString(thisEnclosingJoinPoint.getSignature());
		final String callerClazz = thisObject.getClass().getName();
		final int callerObject = System.identityHashCode(thisObject);
		// callee
		final String calleeClazz = targetObject.getClass().getName();
		final int calleeObject = System.identityHashCode(targetObject);
		// measure before call
		CTRLINST.newMonitoringRecord(new CallOperationObjectEvent(TIME.getTime(), traceId, trace.getNextOrderId(), caller, callerClazz, callee, calleeClazz,
				callerObject, calleeObject));
		// call of the called method
		final Object retval;
		try {
			retval = thisJoinPoint.proceed();
		} finally {
			if (newTrace) { // close the trace
				TRACEREGISTRY.unregisterTrace();
			}
		}
		return retval;
	}

	@Around("monitoredOperation() && !this(java.lang.Object) && target(targetObject) && notWithinKieker()")
	public Object static2memberOperation(final Object targetObject, final ProceedingJoinPoint thisJoinPoint, final EnclosingStaticPart thisEnclosingJoinPoint)
			throws Throwable { // NOCS
		if (!CTRLINST.isMonitoringEnabled()) {
			return thisJoinPoint.proceed();
		}
		final String callee = this.signatureToLongString(thisJoinPoint.getSignature());
		if (!CTRLINST.isProbeActivated(callee)) {
			return thisJoinPoint.proceed();
		}
		// common fields
		TraceMetadata trace = TRACEREGISTRY.getTrace();
		final boolean newTrace = trace == null;
		if (newTrace) {
			trace = TRACEREGISTRY.registerTrace();
			CTRLINST.newMonitoringRecord(trace);
		}
		final long traceId = trace.getTraceId();
		// caller
		final Signature callerSig = thisEnclosingJoinPoint.getSignature();
		final String caller = this.signatureToLongString(callerSig);
		final String callerClazz = callerSig.getDeclaringTypeName();
		// callee
		final String calleeClazz = targetObject.getClass().getName();
		final int calleeObject = System.identityHashCode(targetObject);
		// measure before call
		CTRLINST.newMonitoringRecord(new CallOperationObjectEvent(TIME.getTime(), traceId, trace.getNextOrderId(), caller, callerClazz, callee, calleeClazz, 0,
				calleeObject));
		// call of the called method
		final Object retval;
		try {
			retval = thisJoinPoint.proceed();
		} finally {
			if (newTrace) { // close the trace
				TRACEREGISTRY.unregisterTrace();
			}
		}
		return retval;
	}

	@Around("monitoredOperation() && this(thisObject) && !target(java.lang.Object) && notWithinKieker()")
	public Object member2staticOperation(final Object thisObject, final ProceedingJoinPoint thisJoinPoint,
			final EnclosingStaticPart thisEnclosingJoinPoint) throws Throwable { // NOCS
		if (!CTRLINST.isMonitoringEnabled()) {
			return thisJoinPoint.proceed();
		}
		final Signature calleeSig = thisJoinPoint.getSignature();
		final String callee = this.signatureToLongString(calleeSig);
		if (!CTRLINST.isProbeActivated(callee)) {
			return thisJoinPoint.proceed();
		}
		// common fields
		TraceMetadata trace = TRACEREGISTRY.getTrace();
		final boolean newTrace = trace == null;
		if (newTrace) {
			trace = TRACEREGISTRY.registerTrace();
			CTRLINST.newMonitoringRecord(trace);
		}
		final long traceId = trace.getTraceId();
		// caller
		final String caller = this.signatureToLongString(thisEnclosingJoinPoint.getSignature());
		final String callerClazz = thisObject.getClass().getName();
		final int callerObject = System.identityHashCode(thisObject);
		// callee
		final String calleeClazz = calleeSig.getDeclaringTypeName();
		// measure before call
		CTRLINST.newMonitoringRecord(new CallOperationObjectEvent(TIME.getTime(), traceId, trace.getNextOrderId(), caller, callerClazz, callee, calleeClazz,
				callerObject, 0));
		// call of the called method
		final Object retval;
		try {
			retval = thisJoinPoint.proceed();
		} finally {
			if (newTrace) { // close the trace
				TRACEREGISTRY.unregisterTrace();
			}
		}
		return retval;
	}

	@Around("monitoredOperation() && !this(java.lang.Object) && !target(java.lang.Object) && notWithinKieker()")
	public Object static2staticOperation(final ProceedingJoinPoint thisJoinPoint, final EnclosingStaticPart thisEnclosingJoinPoint)
			throws Throwable { // NOCS
		if (!CTRLINST.isMonitoringEnabled()) {
			return thisJoinPoint.proceed();
		}
		final Signature calleeSig = thisJoinPoint.getSignature();
		final String callee = this.signatureToLongString(calleeSig);
		if (!CTRLINST.isProbeActivated(callee)) {
			return thisJoinPoint.proceed();
		}
		// common fields
		TraceMetadata trace = TRACEREGISTRY.getTrace();
		final boolean newTrace = trace == null;
		if (newTrace) {
			trace = TRACEREGISTRY.registerTrace();
			CTRLINST.newMonitoringRecord(trace);
		}
		final long traceId = trace.getTraceId();
		// caller
		final Signature callerSig = thisEnclosingJoinPoint.getSignature();
		final String caller = this.signatureToLongString(callerSig);
		final String callerClazz = callerSig.getDeclaringTypeName();
		// callee
		final String calleeClazz = calleeSig.getDeclaringTypeName();
		// measure before call
		CTRLINST.newMonitoringRecord(new CallOperationObjectEvent(TIME.getTime(), traceId, trace.getNextOrderId(), caller, callerClazz, callee, calleeClazz, 0, 0));
		// call of the called method
		final Object retval;
		try {
			retval = thisJoinPoint.proceed();
		} finally {
			if (newTrace) { // close the trace
				TRACEREGISTRY.unregisterTrace();
			}
		}
		return retval;
	}
}
