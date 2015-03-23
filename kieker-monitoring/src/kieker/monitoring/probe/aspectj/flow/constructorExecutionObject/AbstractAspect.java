/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.constructor.object.AfterConstructorFailedObjectEvent;
import kieker.common.record.flow.trace.operation.constructor.object.AfterConstructorObjectEvent;
import kieker.common.record.flow.trace.operation.constructor.object.BeforeConstructorObjectEvent;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.TraceRegistry;
import kieker.monitoring.probe.aspectj.AbstractAspectJProbe;
import kieker.monitoring.timer.ITimeSource;

/**
 * @author Jan Waller
 *
 * @since 1.10
 */
@Aspect
public abstract class AbstractAspect extends AbstractAspectJProbe {
	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();
	private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;

	/**
	 * The pointcut for the monitored constructors. Inheriting classes should extend the pointcut in order to find the correct constructor executions (e.g. all
	 * constructors or only constructors with specific annotations).
	 */
	@Pointcut
	public abstract void monitoredConstructor();

	/**
	 * The advice used around the constructor executions.
	 *
	 * @param thisObject
	 * @param thisJoinPoint
	 *
	 * @return The result of the joint point's {@code proceed} method.
	 *
	 * @throws Throwable
	 */
	@Around("monitoredConstructor() && this(thisObject) && notWithinKieker()")
	public Object constructor(final Object thisObject, final ProceedingJoinPoint thisJoinPoint) throws Throwable { // NOCS (Throwable)
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
		CTRLINST.newMonitoringRecord(new BeforeConstructorObjectEvent(TIME.getTime(), traceId, trace.getNextOrderId(), operationSignature, clazz, objectId));
		// execution of the called method
		final Object retval;
		try {
			retval = thisJoinPoint.proceed();
		} catch (final Throwable th) { // NOPMD NOCS (catch throw might ok here)
			// measure after failed execution
			CTRLINST.newMonitoringRecord(
					new AfterConstructorFailedObjectEvent(TIME.getTime(), traceId, trace.getNextOrderId(), operationSignature, clazz, th.toString(), objectId));
			throw th;
		} finally {
			if (newTrace) { // close the trace
				TRACEREGISTRY.unregisterTrace();
			}
		}
		// measure after successful execution
		CTRLINST.newMonitoringRecord(new AfterConstructorObjectEvent(TIME.getTime(), traceId, trace.getNextOrderId(), operationSignature, clazz, objectId));
		return retval;
	}
}
