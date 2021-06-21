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

package kieker.monitoring.probe.aspectj.flow.operationExecutionObjectInterface;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.object.AfterOperationFailedObjectEvent;
import kieker.common.record.flow.trace.operation.object.AfterOperationObjectEvent;
import kieker.common.record.flow.trace.operation.object.BeforeOperationObjectInterfaceEvent;
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
public abstract class AbstractAspect extends AbstractAspectJProbe {
	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();
	private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;

	/**
	 * This is the pointcut for the monitored operations. Inheriting classes should extend this pointcut to make sure that the correct joint points will be detected.
	 */
	@Pointcut
	public abstract void monitoredOperation();

	@Around("monitoredOperation() && this(thisObject) && notWithinKieker()")
	public Object operation(final Object thisObject, final ProceedingJoinPoint thisJoinPoint) throws Throwable { // NOCS (Throwable)
		if (!CTRLINST.isMonitoringEnabled()) {
			return thisJoinPoint.proceed();
		}
		final String operationSignature = this.signatureToLongString(thisJoinPoint.getSignature());
		if (!CTRLINST.isProbeActivated(operationSignature)) {
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
		final String clazz = thisObject.getClass().getName();
		final int objectId = System.identityHashCode(thisObject);
		// measure before execution
		CTRLINST.newMonitoringRecord(new BeforeOperationObjectInterfaceEvent(TIME.getTime(), traceId, trace.getNextOrderId(), operationSignature, clazz, objectId,
				AbstractAspect.getInterface(thisJoinPoint)));
		// execution of the called method
		final Object retval;
		try {
			retval = thisJoinPoint.proceed();
		} catch (final Throwable th) { // NOPMD NOCS (catch throw might ok here)
			// measure after failed execution
			CTRLINST.newMonitoringRecord(new AfterOperationFailedObjectEvent(TIME.getTime(), traceId, trace.getNextOrderId(), operationSignature, clazz,
					th.toString(), objectId));
			throw th;
		} finally {
			if (newTrace) { // close the trace
				TRACEREGISTRY.unregisterTrace();
			}
		}
		// measure after successful execution
		CTRLINST.newMonitoringRecord(new AfterOperationObjectEvent(TIME.getTime(), traceId, trace.getNextOrderId(), operationSignature, clazz, objectId));
		return retval;
	}

	private static final String getInterface(final ProceedingJoinPoint thisJoinPoint) {
		final Class<?>[] interfaces = thisJoinPoint.getSignature().getDeclaringType().getInterfaces();
		final StringBuilder sb = new StringBuilder();

		sb.append('[');
		if (interfaces.length == 0) {
			final Class<?> superClass = thisJoinPoint.getSignature().getDeclaringType()
					.getSuperclass();
			if (superClass != null) {
				final String superClassName = superClass.getName();
				if (!"java.lang.Object".equals(superClassName)) {
					sb.append(superClassName);
				}
			}
		} else {
			for (int idx = 0; idx < interfaces.length; idx++) {
				sb.append(interfaces[idx].getName());
				if (idx < (interfaces.length - 1)) {
					sb.append(", ");
				}
			}
		}
		sb.append(']');

		return sb.toString();
	}

	/**
	 * This advice is used around static operations.
	 *
	 * @param thisJoinPoint
	 *            The joint point of the advice.
	 *
	 * @return The return value of the joint point's {@code proceed} method.
	 *
	 * @throws Throwable
	 */
	@Around("monitoredOperation() && !this(java.lang.Object) && notWithinKieker()")
	public Object staticOperation(final ProceedingJoinPoint thisJoinPoint) throws Throwable { // NOCS (Throwable)
		if (!CTRLINST.isMonitoringEnabled()) {
			return thisJoinPoint.proceed();
		}
		final Signature sig = thisJoinPoint.getSignature();
		final String operationSignature = this.signatureToLongString(sig);
		if (!CTRLINST.isProbeActivated(operationSignature)) {
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
		final String clazz = sig.getDeclaringTypeName();
		// measure before execution
		CTRLINST.newMonitoringRecord(new BeforeOperationObjectInterfaceEvent(TIME.getTime(), traceId, trace.getNextOrderId(), operationSignature, clazz, 0,
				AbstractAspect.getInterface(thisJoinPoint)));
		// execution of the called method
		final Object retval;
		try {
			retval = thisJoinPoint.proceed();
		} catch (final Throwable th) { // NOPMD NOCS (catch throw might ok here)
			// measure after failed execution
			CTRLINST.newMonitoringRecord(new AfterOperationFailedObjectEvent(TIME.getTime(), traceId, trace.getNextOrderId(), operationSignature, clazz,
					th.toString(), 0));
			throw th;
		} finally {
			if (newTrace) { // close the trace
				TRACEREGISTRY.unregisterTrace();
			}
		}
		// measure after successful execution
		CTRLINST.newMonitoringRecord(new AfterOperationObjectEvent(TIME.getTime(), traceId, trace.getNextOrderId(), operationSignature, clazz, 0));
		return retval;
	}
}
