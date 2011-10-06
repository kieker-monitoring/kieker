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

import kieker.common.record.OperationExecutionRecord;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 * @author Andre van Hoorn
 */
public class OperationExecutionMethodInvocationInterceptor extends AbstractOperationExecutionMethodInvocationInterceptor {

	private static final Log LOG = LogFactory.getLog(OperationExecutionMethodInvocationInterceptor.class);

	/**
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */

	@Override
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		final long traceId = AbstractOperationExecutionMethodInvocationInterceptor.CF_REGISTRY.recallThreadLocalTraceId();
		// Only go on if a traceId has been registered before
		if ((traceId == -1) || !AbstractOperationExecutionMethodInvocationInterceptor.CONTROLLER.isMonitoringEnabled()) {
			return invocation.proceed();
		}

		final OperationExecutionRecord execData = this.initExecutionData(invocation);
		execData.eoi = AbstractOperationExecutionMethodInvocationInterceptor.CF_REGISTRY.incrementAndRecallThreadLocalEOI();
		/*
		 * this is executionOrderIndex-th execution in this trace
		 */
		execData.ess = AbstractOperationExecutionMethodInvocationInterceptor.CF_REGISTRY.recallAndIncrementThreadLocalESS();
		/*
		 * this is the height in the dynamic call tree of this execution
		 */

		try {
			this.proceedAndMeasure(invocation, execData);
			if ((execData.eoi == -1) || (execData.ess == -1)) {
				OperationExecutionMethodInvocationInterceptor.LOG.fatal("eoi and/or ess have invalid values:" + " eoi == " + execData.eoi + " ess == "
						+ execData.ess);
				OperationExecutionMethodInvocationInterceptor.LOG.fatal("Terminating Kieker.Monitoring!");
				AbstractOperationExecutionMethodInvocationInterceptor.CONTROLLER.terminateMonitoring();
			}
		} catch (final Exception e) {
			throw e; // exceptions are forwarded
		} finally {
			/*
			 * note that proceedAndMeasure(...) even sets the variable name in
			 * case the execution of the joint point resulted in an exception!
			 */
			AbstractOperationExecutionMethodInvocationInterceptor.CONTROLLER.newMonitoringRecord(execData);
			AbstractOperationExecutionMethodInvocationInterceptor.CF_REGISTRY.storeThreadLocalESS(execData.ess);
		}
		return execData.retVal;
	}
}
