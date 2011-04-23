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
public class OperationExecutionMethodInvocationInterceptor extends
		AbstractOperationExecutionMethodInvocationInterceptor {

	private static final Log log = LogFactory
			.getLog(OperationExecutionMethodInvocationInterceptor.class);

	/**
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */

	@Override
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		final long traceId = AbstractOperationExecutionMethodInvocationInterceptor.cfRegistry
				.recallThreadLocalTraceId();
		// Only go on if a traceId has been registered before
		if ((traceId == -1)
				|| !AbstractOperationExecutionMethodInvocationInterceptor.controller
						.isMonitoringEnabled()) {
			return invocation.proceed();
		}

		final OperationExecutionRecord execData = this
				.initExecutionData(invocation);
		execData.eoi = AbstractOperationExecutionMethodInvocationInterceptor.cfRegistry
				.incrementAndRecallThreadLocalEOI();
		/*
		 * this is executionOrderIndex-th execution in this trace
		 */
		execData.ess = AbstractOperationExecutionMethodInvocationInterceptor.cfRegistry
				.recallAndIncrementThreadLocalESS();
		/*
		 * this is the height in the dynamic call tree of this execution
		 */

		try {
			this.proceedAndMeasure(invocation, execData);
			if ((execData.eoi == -1) || (execData.ess == -1)) {
				OperationExecutionMethodInvocationInterceptor.log
						.fatal("eoi and/or ess have invalid values:"
								+ " eoi == " + execData.eoi + " ess == "
								+ execData.ess);
				OperationExecutionMethodInvocationInterceptor.log
						.fatal("Terminating Kieker.Monitoring!");
				AbstractOperationExecutionMethodInvocationInterceptor.controller
						.terminateMonitoring();
			}
		} catch (final Exception e) {
			throw e; // exceptions are forwarded
		} finally {
			/*
			 * note that proceedAndMeasure(...) even sets the variable name in
			 * case the execution of the joint point resulted in an exception!
			 */
			AbstractOperationExecutionMethodInvocationInterceptor.controller
					.newMonitoringRecord(execData);
			AbstractOperationExecutionMethodInvocationInterceptor.cfRegistry
					.storeThreadLocalESS(execData.ess);
		}
		return execData.retVal;
	}
}
