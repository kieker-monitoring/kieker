package kieker.monitoring.probe.spring.executions;

import kieker.common.record.OperationExecutionRecord;
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

	protected static final IMonitoringController tpmonController = MonitoringController.getInstance();
	protected static final SessionRegistry sessionRegistry = SessionRegistry.getInstance();
	protected static final ControlFlowRegistry cfRegistry = ControlFlowRegistry.getInstance();
	protected static final ITimeSource timesource = tpmonController.getTimeSource();
	protected static final String vmName = AbstractOperationExecutionMethodInvocationInterceptor.tpmonController.getHostName();

	/**
	 * Iff true, the name of the runtime class is used, iff false, the name of
	 * the declaring class (interface) is used
	 */
	protected boolean useRuntimeClassname = true;

	public boolean getUseRuntimeClassname() {
		return this.useRuntimeClassname;
	}

	public void setUseRuntimeClassname(final boolean useRuntimeClassname) {
		this.useRuntimeClassname = useRuntimeClassname;
	}

	protected OperationExecutionRecord initExecutionData(final MethodInvocation invocation) {
		final long traceId = AbstractOperationExecutionMethodInvocationInterceptor.cfRegistry.recallThreadLocalTraceId();

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
		execData.isEntryPoint = false;
		// execData.traceId = ctrlInst.recallThreadLocalTraceId(); // -1 if
		// entry point
		if (execData.traceId == -1) {
			execData.traceId = AbstractOperationExecutionMethodInvocationInterceptor.cfRegistry.getAndStoreUniqueThreadLocalTraceId();
			execData.isEntryPoint = true;
		}
		// here we can collect the sessionid, which may for instance be
		// registered before by
		// a explicity call registerSessionIdentifier(String sessionid, long
		// threadid) from a method
		// that knowns the request object (e.g. a servlet or a spring MVC
		// controller).
		execData.sessionId = AbstractOperationExecutionMethodInvocationInterceptor.sessionRegistry.recallThreadLocalSessionId();
		execData.experimentId = AbstractOperationExecutionMethodInvocationInterceptor.tpmonController.getExperimentId();
		execData.hostName = AbstractOperationExecutionMethodInvocationInterceptor.vmName;
		return execData;
	}

	/**
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */

	@Override
	public abstract Object invoke(MethodInvocation invocation) throws Throwable;

	protected void proceedAndMeasure(final MethodInvocation invocation, final OperationExecutionRecord execData) throws Throwable {
		execData.tin = timesource.currentTimeNanos();
		try {
			// executing the intercepted method call
			execData.retVal = invocation.proceed();
		} catch (final Exception e) {
			throw e; // exceptions are forwarded
		} finally {
			execData.tout = timesource.currentTimeNanos();
			if (execData.isEntryPoint) {
				AbstractOperationExecutionMethodInvocationInterceptor.cfRegistry.unsetThreadLocalTraceId();
			}
		}
	}
}
