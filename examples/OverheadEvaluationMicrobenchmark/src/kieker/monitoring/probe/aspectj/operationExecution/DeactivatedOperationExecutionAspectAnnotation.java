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

package kieker.monitoring.probe.aspectj.operationExecution;

import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.timer.ITimeSource;

/**
 * TODO: this is a slightly altered clone of AbstractOperationExecutionAspect. Include something similar into default implementation!
 * 
 * @author Andre van Hoorn, Jan Waller
 */
@Aspect
public final class DeactivatedOperationExecutionAspectAnnotation extends AbstractOperationExecutionAspect {
	private static final Log LOG = LogFactory.getLog(DeactivatedOperationExecutionAspectAnnotation.class);

	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = DeactivatedOperationExecutionAspectAnnotation.CTRLINST.getTimeSource();
	private static final String VMNAME = DeactivatedOperationExecutionAspectAnnotation.CTRLINST.getHostname();
	private static final ControlFlowRegistry CFREGISTRY = ControlFlowRegistry.INSTANCE;
	private static final SessionRegistry SESSIONREGISTRY = SessionRegistry.INSTANCE;

	private static final ConcurrentHashMap<String, Boolean> DEACTIVATEDPROBES = new ConcurrentHashMap<String, Boolean>();

	static {
		final int mapSize = 10000;
		for (int i = 0; i < (mapSize / 2); i++) {
			DeactivatedOperationExecutionAspectAnnotation.DEACTIVATEDPROBES.put(Long.toHexString(Double.doubleToLongBits(Math.random())), Boolean.TRUE);
		}
		DeactivatedOperationExecutionAspectAnnotation.DEACTIVATEDPROBES.put("long kieker.evaluation.monitoredApplication.MonitoredClass.monitoredMethod(long, int)",
				Boolean.TRUE);
		for (int i = 0; i < (mapSize / 2); i++) {
			DeactivatedOperationExecutionAspectAnnotation.DEACTIVATEDPROBES.put(Long.toHexString(Double.doubleToLongBits(Math.random())), Boolean.TRUE);
		}
	}

	public DeactivatedOperationExecutionAspectAnnotation() {
		// empty default constructor
	}

	@Override
	@Pointcut("execution(@kieker.monitoring.annotation.OperationExecutionMonitoringProbe * *(..)) || execution(@kieker.monitoring.annotation.OperationExecutionMonitoringProbe new(..))")
	public final void monitoredOperation() {
		// Aspect Declaration (MUST be empty)
	}

	@Override
	@Around("monitoredOperation() && notWithinKieker()")
	public final Object operation(final ProceedingJoinPoint thisJoinPoint) throws Throwable { // NOCS (Throwable)
		if (!DeactivatedOperationExecutionAspectAnnotation.CTRLINST.isMonitoringEnabled()
				|| DeactivatedOperationExecutionAspectAnnotation.DEACTIVATEDPROBES.containsKey(thisJoinPoint.getStaticPart().getSignature().toString())) {
			return thisJoinPoint.proceed();
		}
		// collect data
		final String signature = thisJoinPoint.getSignature().toLongString();
		final boolean entrypoint;
		final String hostname = DeactivatedOperationExecutionAspectAnnotation.VMNAME;
		final String sessionId = DeactivatedOperationExecutionAspectAnnotation.SESSIONREGISTRY.recallThreadLocalSessionId();
		final int eoi; // this is executionOrderIndex-th execution in this trace
		final int ess; // this is the height in the dynamic call tree of this execution
		long traceId = DeactivatedOperationExecutionAspectAnnotation.CFREGISTRY.recallThreadLocalTraceId(); // traceId, -1 if entry point
		if (traceId == -1) {
			entrypoint = true;
			traceId = DeactivatedOperationExecutionAspectAnnotation.CFREGISTRY.getAndStoreUniqueThreadLocalTraceId();
			DeactivatedOperationExecutionAspectAnnotation.CFREGISTRY.storeThreadLocalEOI(0);
			DeactivatedOperationExecutionAspectAnnotation.CFREGISTRY.storeThreadLocalESS(1); // next operation is ess + 1
			eoi = 0;
			ess = 0;
		} else {
			entrypoint = false;
			eoi = DeactivatedOperationExecutionAspectAnnotation.CFREGISTRY.incrementAndRecallThreadLocalEOI(); // ess > 1
			ess = DeactivatedOperationExecutionAspectAnnotation.CFREGISTRY.recallAndIncrementThreadLocalESS(); // ess >= 0
			if ((eoi == -1) || (ess == -1)) {
				DeactivatedOperationExecutionAspectAnnotation.LOG.error("eoi and/or ess have invalid values:" + " eoi == " + eoi + " ess == " + ess);
				DeactivatedOperationExecutionAspectAnnotation.CTRLINST.terminateMonitoring();
			}
		}
		// measure before
		final long tin = DeactivatedOperationExecutionAspectAnnotation.TIME.getTime();
		// execution of the called method
		final Object retval;
		try {
			retval = thisJoinPoint.proceed();
		} finally {
			// measure after
			final long tout = DeactivatedOperationExecutionAspectAnnotation.TIME.getTime();
			DeactivatedOperationExecutionAspectAnnotation.CTRLINST.newMonitoringRecord(
					new OperationExecutionRecord(signature, sessionId, traceId, tin, tout, hostname, eoi, ess));
			// cleanup
			if (entrypoint) {
				DeactivatedOperationExecutionAspectAnnotation.CFREGISTRY.unsetThreadLocalTraceId();
				DeactivatedOperationExecutionAspectAnnotation.CFREGISTRY.unsetThreadLocalEOI();
				DeactivatedOperationExecutionAspectAnnotation.CFREGISTRY.unsetThreadLocalESS();
			} else {
				DeactivatedOperationExecutionAspectAnnotation.CFREGISTRY.storeThreadLocalESS(ess); // next operation is ess
			}
		}
		return retval;
	}
}
