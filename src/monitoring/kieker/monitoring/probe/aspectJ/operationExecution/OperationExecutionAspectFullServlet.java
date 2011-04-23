package kieker.monitoring.probe.aspectJ.operationExecution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kieker.common.record.OperationExecutionRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Andre van Hoorn
 */
@Aspect
public class OperationExecutionAspectFullServlet extends
		AbstractOperationExecutionAspectServlet {

	private static final Log log = LogFactory
			.getLog(OperationExecutionAspectAnnotation.class);

	@Pointcut("execution(* *.do*(..)) && args(request,response)")
	public void monitoredServletEntry(final HttpServletRequest request,
			final HttpServletResponse response) {
	}

	@Override
	@Around("monitoredServletEntry(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse) && notWithinKieker()")
	public Object doServletEntryProfiling(
			final ProceedingJoinPoint thisJoinPoint) throws Throwable {
		return super.doServletEntryProfiling(thisJoinPoint);
	}

	@Pointcut("execution(* *.*(..))")
	public void monitoredMethod() {
	}

	@Override
	@Around("monitoredMethod() && notWithinKieker()")
	public Object doBasicProfiling(final ProceedingJoinPoint thisJoinPoint)
			throws Throwable {
		if (!AbstractOperationExecutionAspect.ctrlInst.isMonitoringEnabled()) {
			return thisJoinPoint.proceed();
		}
		final OperationExecutionRecord execData = this
				.initExecutionData(thisJoinPoint);
		execData.sessionId = AbstractOperationExecutionAspectServlet.sessionRegistry
				.recallThreadLocalSessionId(); // may
		// be
		// null
		int eoi = 0; /* this is executionOrderIndex-th execution in this trace */
		int ess = 0; /*
					 * this is the height in the dynamic call tree of this
					 * execution
					 */
		if (execData.isEntryPoint) {
			AbstractOperationExecutionAspect.cfRegistry.storeThreadLocalEOI(0);
			/*
			 * current execution's eoi is 0
			 */
			AbstractOperationExecutionAspect.cfRegistry.storeThreadLocalESS(1);
			/*
			 * current execution's ess is 0
			 */
		} else {
			eoi = AbstractOperationExecutionAspect.cfRegistry
					.incrementAndRecallThreadLocalEOI(); // ess > 1
			ess = AbstractOperationExecutionAspect.cfRegistry
					.recallAndIncrementThreadLocalESS(); // ess >= 0
		}
		try {
			this.proceedAndMeasure(thisJoinPoint, execData);
			if ((eoi == -1) || (ess == -1)) {
				OperationExecutionAspectFullServlet.log
						.fatal("eoi and/or ess have invalid values:"
								+ " eoi == " + eoi + " ess == " + ess);
				OperationExecutionAspectFullServlet.log
						.fatal("Terminating!");
				AbstractOperationExecutionAspect.ctrlInst.terminateMonitoring();
			}
		} catch (final Exception e) {
			throw e; // exceptions are forwarded
		} finally {
			/*
			 * note that proceedAndMeasure(...) even sets the variable name in
			 * case the execution of the joint point resulted in an exception!
			 */
			execData.eoi = eoi;
			execData.ess = ess;
			AbstractOperationExecutionAspect.ctrlInst
					.newMonitoringRecord(execData);
			if (execData.isEntryPoint) {
				AbstractOperationExecutionAspect.cfRegistry
						.unsetThreadLocalEOI();
				AbstractOperationExecutionAspect.cfRegistry
						.unsetThreadLocalESS();
			} else {
				AbstractOperationExecutionAspect.cfRegistry
						.storeThreadLocalESS(ess);
			}
		}
		return execData.retVal;
	}
}
