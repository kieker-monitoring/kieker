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
 * @author Andre van Hoorn
 * @author Marco Luebcke
 */
public abstract class AbstractOperationExecutionMethodInvocationInterceptor implements MethodInterceptor, IMonitoringProbe {

	protected static final IMonitoringController CONTROLLER = MonitoringController.getInstance();
	protected static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;
	protected static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;
	protected static final ITimeSource TIMESOURCE = AbstractOperationExecutionMethodInvocationInterceptor.CONTROLLER.getTimeSource();
	protected static final String VM_NAME = AbstractOperationExecutionMethodInvocationInterceptor.CONTROLLER.getHostName();

	/**
	 * Iff true, the name of the runtime class is used, iff false, the name of
	 * the declaring class (interface) is used
	 */
	protected boolean useRuntimeClassname = true;

	public boolean isUseRuntimeClassname() {
		return this.useRuntimeClassname;
	}

	public void setUseRuntimeClassname(final boolean useRuntimeClassname) {
		this.useRuntimeClassname = useRuntimeClassname;
	}

	protected OperationExecutionRecord initExecutionData(final MethodInvocation invocation) {
		final long traceId = AbstractOperationExecutionMethodInvocationInterceptor.CF_REGISTRY.recallThreadLocalTraceId();

		final StringBuilder sb = new StringBuilder().append(invocation.getMethod().getName());
		sb.append("(");
		boolean first = true;
		for (final Class<?> clazz : invocation.getMethod().getParameterTypes()) {
			if (!first) {
				sb.append(",");
			} else {
				first = false;
			}
			sb.append(clazz.getSimpleName());
		}
		sb.append(")");
		final String opname = sb.toString();

		String componentName;
		if (this.useRuntimeClassname) {
			/* Use the name of the runtime class */
			componentName = invocation.getThis().getClass().getName();
		} else {
			/* Use the name of the declaring class or the interface */
			componentName = invocation.getMethod().getDeclaringClass().getName();
		}

		final OperationExecutionRecord execData = new OperationExecutionRecord(componentName /* component */, opname /* operation */, traceId /*
																																			 * -1
																																			 * if
																																			 * entry
																																			 * point
																																			 */);
		execData.setEntryPoint(false);
		if (execData.getTraceId() == -1) { // -1 if entry points
			execData.setTraceId(AbstractOperationExecutionMethodInvocationInterceptor.CF_REGISTRY.getAndStoreUniqueThreadLocalTraceId());
			execData.setEntryPoint(true);
		}
		// here we can collect the sessionid, which may for instance be
		// registered before by
		// a explicity call registerSessionIdentifier(String sessionid, long
		// threadid) from a method
		// that knowns the request object (e.g. a servlet or a spring MVC
		// controller).
		execData.setSessionId(AbstractOperationExecutionMethodInvocationInterceptor.SESSION_REGISTRY.recallThreadLocalSessionId());
		execData.setExperimentId(AbstractOperationExecutionMethodInvocationInterceptor.CONTROLLER.getExperimentId());
		execData.setHostName(AbstractOperationExecutionMethodInvocationInterceptor.VM_NAME);
		return execData;
	}

	/**
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */

	@Override
	public abstract Object invoke(MethodInvocation invocation) throws Throwable; // NOCS (IllegalThrowsCheck)

	protected void proceedAndMeasure(final MethodInvocation invocation, final OperationExecutionRecord execData) throws Throwable { // NOCS (IllegalThrowsCheck)
		execData.setTin(AbstractOperationExecutionMethodInvocationInterceptor.TIMESOURCE.getTime());
		try {
			// executing the intercepted method call
			execData.setRetVal(invocation.proceed());
		} catch (final Exception e) { // NOPMD // NOCS (IllegalCatchCheck)
			throw e; // exceptions are forwarded
		} finally {
			execData.setTout(AbstractOperationExecutionMethodInvocationInterceptor.TIMESOURCE.getTime());
			if (execData.isEntryPoint()) {
				AbstractOperationExecutionMethodInvocationInterceptor.CF_REGISTRY.unsetThreadLocalTraceId();
			}
		}
	}
}
