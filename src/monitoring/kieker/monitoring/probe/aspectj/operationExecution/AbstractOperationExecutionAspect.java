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

package kieker.monitoring.probe.aspectj.operationExecution;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.probe.aspectj.AbstractAspectJProbe;
import kieker.monitoring.timer.ITimeSource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author Andre van Hoorn
 */
@Aspect
public abstract class AbstractOperationExecutionAspect extends AbstractAspectJProbe {

	protected static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	protected static final String VMNAME = AbstractOperationExecutionAspect.CTRLINST.getHostName();
	protected static final ControlFlowRegistry CFREGISTRY = ControlFlowRegistry.INSTANCE;
	protected static final ITimeSource TIMESOURCE = AbstractOperationExecutionAspect.CTRLINST.getTimeSource();

	protected OperationExecutionRecord initExecutionData(final ProceedingJoinPoint thisJoinPoint) {

		final OperationExecutionRecord execData = new OperationExecutionRecord(thisJoinPoint.getSignature().toLongString(),
				AbstractOperationExecutionAspect.CFREGISTRY.recallThreadLocalTraceId() /* traceId, -1 if entry point */);

		execData.setEntryPoint(false);
		// execData.traceId = ctrlInst.recallThreadLocalTraceId(); // -1 if entry point
		if (execData.getTraceId() == -1) {
			execData.setTraceId(AbstractOperationExecutionAspect.CFREGISTRY.getAndStoreUniqueThreadLocalTraceId());
			execData.setEntryPoint(true);
		}
		execData.setHostName(AbstractOperationExecutionAspect.VMNAME);
		execData.setExperimentId(AbstractOperationExecutionAspect.CTRLINST.getExperimentId());
		return execData;
	}

	public abstract Object doBasicProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable; // NOPMD // NOCS (IllegalThrowsCheck)

	protected void proceedAndMeasure(final ProceedingJoinPoint thisJoinPoint, final OperationExecutionRecord execData) throws Throwable { // NOCS
		execData.setTin(AbstractOperationExecutionAspect.TIMESOURCE.getTime()); // startint stopwatch
		try {
			execData.setRetVal(thisJoinPoint.proceed());
		} catch (final Throwable e) { // NOPMD // NOCS (IllegalThrowsCheck)
			throw e; // exceptions are forwarded
		} finally {
			execData.setTout(AbstractOperationExecutionAspect.TIMESOURCE.getTime());
			if (execData.isEntryPoint()) {
				AbstractOperationExecutionAspect.CFREGISTRY.unsetThreadLocalTraceId();
			}
		}
	}
}
