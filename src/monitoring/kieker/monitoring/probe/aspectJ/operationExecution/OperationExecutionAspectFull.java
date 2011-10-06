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

package kieker.monitoring.probe.aspectJ.operationExecution;

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
public class OperationExecutionAspectFull extends AbstractOperationExecutionAspect {

	private static final Log LOG = LogFactory.getLog(OperationExecutionAspectFull.class);

	@Pointcut("execution(* *.*(..))")
	public void monitoredMethod() {}

	@Override
	@Around("monitoredMethod() && notWithinKieker()")
	public Object doBasicProfiling(final ProceedingJoinPoint thisJoinPoint) throws Throwable {
		if (!AbstractOperationExecutionAspect.CTRLINST.isMonitoringEnabled()) {
			return thisJoinPoint.proceed();
		}
		final OperationExecutionRecord execData = this.initExecutionData(thisJoinPoint);
		int eoi; // this is executionOrderIndex-th execution in this trace
		int ess; // this is the height in the dynamic call tree of this execution
		if (execData.isEntryPoint) {
			AbstractOperationExecutionAspect.CFREGISTRY.storeThreadLocalEOI(0);
			eoi = 0;
			AbstractOperationExecutionAspect.CFREGISTRY.storeThreadLocalESS(1);
			ess = 0;
		} else {
			eoi = AbstractOperationExecutionAspect.CFREGISTRY.incrementAndRecallThreadLocalEOI(); // ess > 1
			ess = AbstractOperationExecutionAspect.CFREGISTRY.recallAndIncrementThreadLocalESS(); // ess >= 0
		}
		try {
			this.proceedAndMeasure(thisJoinPoint, execData);
			if ((eoi == -1) || (ess == -1)) {
				OperationExecutionAspectFull.LOG.fatal("eoi and/or ess have invalid values:" + " eoi == " + eoi + " ess == " + ess);
				OperationExecutionAspectFull.LOG.fatal("Terminating!");
				AbstractOperationExecutionAspect.CTRLINST.terminateMonitoring();
			}
		} catch (final Exception e) { // NOPMD
			throw e; // exceptions are forwarded
		} finally {
			/*
			 * note that proceedAndMeasure(...) even sets the variable name in
			 * case the execution of the joint point resulted in an exception!
			 */
			execData.eoi = eoi;
			execData.ess = ess;
			AbstractOperationExecutionAspect.CTRLINST.newMonitoringRecord(execData);
			if (execData.isEntryPoint) {
				AbstractOperationExecutionAspect.CFREGISTRY.unsetThreadLocalEOI();
				AbstractOperationExecutionAspect.CFREGISTRY.unsetThreadLocalESS();
			} else {
				AbstractOperationExecutionAspect.CFREGISTRY.storeThreadLocalESS(ess);
			}
		}
		return execData.retVal;
	}
}
