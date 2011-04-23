package kieker.monitoring.probe.aspectJ.operationExecution;

import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.probe.aspectJ.AbstractAspectJProbe;
import kieker.monitoring.timer.ITimeSource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author Andre van Hoorn
 */
@Aspect
public abstract class AbstractOperationExecutionAspect extends AbstractAspectJProbe {

	protected static final IMonitoringController ctrlInst = MonitoringController.getInstance();
	protected static final String vmName = ctrlInst.getHostName();
	protected static final ControlFlowRegistry cfRegistry = ControlFlowRegistry.getInstance();
	protected static final ITimeSource timesource = ctrlInst.getTimeSource();

	protected OperationExecutionRecord initExecutionData(final ProceedingJoinPoint thisJoinPoint) {
		final String methodname = thisJoinPoint.getSignature().getName();
		String paramList = thisJoinPoint.getSignature().toLongString();
		final int paranthIndex = paramList.lastIndexOf('(');
		paramList = paramList.substring(paranthIndex); // paramList is now e.g., "()"

		final OperationExecutionRecord execData = new OperationExecutionRecord(thisJoinPoint.getSignature().getDeclaringTypeName() /* component */, methodname
				+ paramList /* operation */, AbstractOperationExecutionAspect.cfRegistry.recallThreadLocalTraceId() /* traceId, -1 if entry point */);

		execData.isEntryPoint = false;
		// execData.traceId = ctrlInst.recallThreadLocalTraceId(); // -1 if entry point
		if (execData.traceId == -1) {
			execData.traceId = AbstractOperationExecutionAspect.cfRegistry.getAndStoreUniqueThreadLocalTraceId();
			execData.isEntryPoint = true;
		}
		execData.hostName = AbstractOperationExecutionAspect.vmName;
		execData.experimentId = AbstractOperationExecutionAspect.ctrlInst.getExperimentId();
		return execData;
	}

	public abstract Object doBasicProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable;

	protected void proceedAndMeasure(final ProceedingJoinPoint thisJoinPoint, final OperationExecutionRecord execData) throws Throwable {
		execData.tin = timesource.getTime(); // startint stopwatch
		try {
			execData.retVal = thisJoinPoint.proceed();
		} catch (final Exception e) {
			throw e; // exceptions are forwarded
		} finally {
			execData.tout = timesource.getTime();
			if (execData.isEntryPoint) {
				AbstractOperationExecutionAspect.cfRegistry.unsetThreadLocalTraceId();
			}
		}
	}
}
