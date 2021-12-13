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

package kieker.monitoring.probe.spring.flow;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.TraceRegistry;
import kieker.monitoring.probe.IMonitoringProbe;
import kieker.monitoring.timer.ITimeSource;

/**
 * @author Nils Christian Ehmke, Jan Waller
 *
 * @since 1.8
 */
public class OperationExecutionMethodInvocationInterceptor implements MethodInterceptor, IMonitoringProbe {

	private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;

	private final IMonitoringController monitoringCtrl;
	private final ITimeSource timeSource;

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
		// common fields
		TraceMetadata trace = TRACEREGISTRY.getTrace();
		final boolean newTrace = trace == null;
		if (newTrace) {
			trace = TRACEREGISTRY.registerTrace();
			this.monitoringCtrl.newMonitoringRecord(trace);
		}
		final long traceId = trace.getTraceId();
		final String clazz = invocation.getThis().getClass().getName();

		// measure before execution
		this.monitoringCtrl.newMonitoringRecord(new BeforeOperationEvent(this.timeSource.getTime(), traceId, trace.getNextOrderId(), signature, clazz));
		// execution of the called method
		final Object retval;
		try {
			retval = invocation.proceed();
		} catch (final Throwable th) { // NOPMD NOCS (catch throw might ok here)
			// measure after failed execution
			this.monitoringCtrl.newMonitoringRecord(new AfterOperationFailedEvent(this.timeSource.getTime(), traceId, trace.getNextOrderId(), signature, clazz,
					th.toString()));
			throw th;
		} finally {
			if (newTrace) { // close the trace
				TRACEREGISTRY.unregisterTrace();
			}
		}
		// measure after successful execution
		this.monitoringCtrl.newMonitoringRecord(new AfterOperationEvent(this.timeSource.getTime(), traceId, trace.getNextOrderId(), signature, clazz));
		return retval;
	}
}
