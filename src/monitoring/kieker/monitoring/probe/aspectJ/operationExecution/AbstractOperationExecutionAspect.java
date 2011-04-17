package kieker.monitoring.probe.aspectJ.operationExecution;

import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.ControllerFactory;
import kieker.monitoring.core.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.probe.aspectJ.AbstractAspectJProbe;
import kieker.monitoring.timer.ITimeSource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */
/**
 * @author Andre van Hoorn
 */
@Aspect
public abstract class AbstractOperationExecutionAspect extends AbstractAspectJProbe {

	protected static final MonitoringController ctrlInst = ControllerFactory.getInstance();
	protected static final String vmName = ctrlInst.getHostName();
	protected static final ControlFlowRegistry cfRegistry = ControlFlowRegistry.getInstance();
	protected static final ITimeSource timesource = ctrlInst.getTimeSource();

	protected OperationExecutionRecord initExecutionData(final ProceedingJoinPoint thisJoinPoint) {
		final String methodname = thisJoinPoint.getSignature().getName();
		String paramList = thisJoinPoint.getSignature().toLongString();
		final int paranthIndex = paramList.lastIndexOf('(');
		paramList = paramList.substring(paranthIndex); // paramList is now e.g., "()"

		final OperationExecutionRecord execData = new OperationExecutionRecord(thisJoinPoint.getSignature()
				.getDeclaringTypeName() /* component */, methodname + paramList /* operation */,
				AbstractOperationExecutionAspect.cfRegistry.recallThreadLocalTraceId() /* traceId, -1 if entry point */);

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

	protected void proceedAndMeasure(final ProceedingJoinPoint thisJoinPoint, final OperationExecutionRecord execData)
	throws Throwable {
		execData.tin = timesource.currentTimeNanos(); // startint stopwatch
		try {
			execData.retVal = thisJoinPoint.proceed();
		} catch (final Exception e) {
			throw e; // exceptions are forwarded
		} finally {
			execData.tout = timesource.currentTimeNanos();
			if (execData.isEntryPoint) {
				AbstractOperationExecutionAspect.cfRegistry.unsetThreadLocalTraceId();
			}
		}
	}
}
