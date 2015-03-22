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

package kieker.monitoring.probe.spring.executions;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.IMonitoringProbe;
import kieker.monitoring.timer.ITimeSource;

/**
 * @author Marco Luebcke, Andre van Hoorn, Jan Waller
 * 
 * @since 0.91
 */
public class OperationExecutionMethodInvocationInterceptor implements MethodInterceptor, IMonitoringProbe {
	private static final Log LOG = LogFactory.getLog(OperationExecutionMethodInvocationInterceptor.class);

	private static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;
	private static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;

	private final IMonitoringController monitoringCtrl;
	private final ITimeSource timeSource;
	private final String hostname;

	public OperationExecutionMethodInvocationInterceptor() {
		this(MonitoringController.getInstance());
	}

	/**
	 * This constructor is mainly used for testing, providing a custom {@link IMonitoringController} instead of using the singleton instance.
	 * 
	 * @param monitoringController
	 *            must not be null
	 */
	public OperationExecutionMethodInvocationInterceptor(final IMonitoringController monitoringController) {
		this.monitoringCtrl = monitoringController;
		this.timeSource = this.monitoringCtrl.getTimeSource();
		this.hostname = this.monitoringCtrl.getHostname();
	}

	/**
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	@Override
	public Object invoke(final MethodInvocation invocation) throws Throwable { // NOCS (IllegalThrowsCheck)
		if (!this.monitoringCtrl.isMonitoringEnabled()) {
			return invocation.proceed();
		}
		final String signature = invocation.getMethod().toString();
		if (!this.monitoringCtrl.isProbeActivated(signature)) {
			return invocation.proceed();
		}

		final String sessionId = SESSION_REGISTRY.recallThreadLocalSessionId();
		final int eoi; // this is executionOrderIndex-th execution in this trace
		final int ess; // this is the height in the dynamic call tree of this execution
		final boolean entrypoint;
		long traceId = CF_REGISTRY.recallThreadLocalTraceId(); // traceId, -1 if entry point
		if (traceId == -1) {
			entrypoint = true;
			traceId = CF_REGISTRY.getAndStoreUniqueThreadLocalTraceId();
			CF_REGISTRY.storeThreadLocalEOI(0);
			CF_REGISTRY.storeThreadLocalESS(1); // next operation is ess + 1
			eoi = 0;
			ess = 0;
		} else {
			entrypoint = false;
			eoi = CF_REGISTRY.incrementAndRecallThreadLocalEOI(); // ess > 1
			ess = CF_REGISTRY.recallAndIncrementThreadLocalESS(); // ess >= 0
			if ((eoi == -1) || (ess == -1)) {
				LOG.error("eoi and/or ess have invalid values:" + " eoi == " + eoi + " ess == " + ess);
				this.monitoringCtrl.terminateMonitoring();
			}
		}
		final long tin = this.timeSource.getTime();
		final Object retval;
		try {
			retval = invocation.proceed();
		} finally {
			final long tout = this.timeSource.getTime();
			this.monitoringCtrl.newMonitoringRecord(
					new OperationExecutionRecord(signature, sessionId, traceId, tin, tout, this.hostname, eoi, ess));
			// cleanup
			if (entrypoint) {
				CF_REGISTRY.unsetThreadLocalTraceId();
				CF_REGISTRY.unsetThreadLocalEOI();
				CF_REGISTRY.unsetThreadLocalESS();
			} else {
				CF_REGISTRY.storeThreadLocalESS(ess); // next operation is ess
			}
		}
		return retval;
	}
}
