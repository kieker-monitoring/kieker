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

package kieker.monitoring.probe.spring.executions;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.IMonitoringProbe;
import kieker.monitoring.timer.ITimeSource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * This annotation marks methods that are exit points for remote calls
 * that go to an other virtual machine. The annotation tries to ensure
 * that the trace id is propagated to another Kieker.Monitoring instance 
 * in the other virtual machine.
 *
 * It provides the boolean property useRuntimeClassname to select whether
 * to use the declaring or the runtime classname of the instrumented methods.
 */

/**
 * @author Marco Luebcke, Andre van Hoorn, Jan Waller
 */
public class OperationExecutionMethodInvocationInterceptor implements MethodInterceptor, IMonitoringProbe {
	private static final Log LOG = LogFactory.getLog(OperationExecutionMethodInvocationInterceptor.class);

	private static final IMonitoringController CONTROLLER = MonitoringController.getInstance();
	private static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;
	private static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;
	private static final ITimeSource TIMESOURCE = CONTROLLER.getTimeSource();
	private static final String VM_NAME = CONTROLLER.getHostname();

	/**
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */

	public Object invoke(final MethodInvocation invocation) throws Throwable { // NOCS (IllegalThrowsCheck)
		final long traceId = CF_REGISTRY.recallThreadLocalTraceId(); // -1 if entry point
		// Only go on if a traceId has been registered before
		// TODO: this needs to be fixed!
		if ((traceId == -1) || !CONTROLLER.isMonitoringEnabled()) {
			return invocation.proceed();
		}
		final String signature = invocation.getMethod().toString();
		final String sessionId = SESSION_REGISTRY.recallThreadLocalSessionId();
		final String hostname = VM_NAME;
		final int eoi = CF_REGISTRY.incrementAndRecallThreadLocalEOI();
		final int ess = CF_REGISTRY.recallAndIncrementThreadLocalESS();
		if ((eoi == -1) || (ess == -1)) {
			LOG.error("eoi and/or ess have invalid values:" + " eoi == " + eoi + " ess == " + ess);
			CONTROLLER.terminateMonitoring();
		}
		final long tin = TIMESOURCE.getTime();
		final Object retval;
		try {
			retval = invocation.proceed();
		} finally {
			final long tout = TIMESOURCE.getTime();
			CONTROLLER.newMonitoringRecord(
					new OperationExecutionRecord(signature, sessionId, traceId, tin, tout, hostname, eoi, ess));
			CF_REGISTRY.storeThreadLocalESS(ess);
		}
		return retval;
	}
}
