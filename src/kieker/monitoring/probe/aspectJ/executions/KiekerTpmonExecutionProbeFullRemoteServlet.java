package kieker.monitoring.probe.aspectJ.executions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kieker.common.record.OperationExecutionRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 *
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
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
 * ==================================================
 */

/**
 * @author Andre van Hoorn
 */
@Aspect
public class KiekerTpmonExecutionProbeFullRemoteServlet extends AbstractKiekerTpmonExecutionProbeServlet {

    private static final Log log = LogFactory.getLog(KiekerTpmonExecutionProbeAnnotationRemote.class);

    @Pointcut("execution(* *.do*(..)) && args(request,response) ")
    public void monitoredServletEntry(HttpServletRequest request, HttpServletResponse response) {
    }

    @Around("monitoredServletEntry(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)")
    public Object doServletEntryProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        return super.doServletEntryProfiling(thisJoinPoint);
    }

    @Pointcut("execution(* *.*(..))")
    public void monitoredMethod() {
    }

    @Around("monitoredMethod()")
    public Object doBasicProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        if (!ctrlInst.isMonitoringEnabled()) {
            return thisJoinPoint.proceed();
        }
        OperationExecutionRecord execData = this.initExecutionData(thisJoinPoint);
        execData.sessionId = sessionRegistry.recallThreadLocalSessionId(); // may be null
        int eoi = 0; /* this is executionOrderIndex-th execution in this trace */
        int ess = 0; /* this is the height in the dynamic call tree of this execution */
        if (execData.isEntryPoint) {
            cfRegistry.storeThreadLocalEOI(0); // current execution's eoi is 0
            cfRegistry.storeThreadLocalESS(1); // *current* execution's ess is 0
        } else {
            eoi = cfRegistry.incrementAndRecallThreadLocalEOI(); // ess > 1
            ess = cfRegistry.recallAndIncrementThreadLocalESS(); // ess >= 0
        }
        try {
            this.proceedAndMeasure(thisJoinPoint, execData);
            if (eoi == -1 || ess == -1) {
                log.fatal("eoi and/or ess have invalid values:" +
                        " eoi == " + eoi +
                        " ess == " + ess);
                log.fatal("Terminating Tpmon!");
                ctrlInst.terminate();
            }
        } catch (Exception e) {
            throw e; // exceptions are forwarded
        } finally {
            /* note that proceedAndMeasure(...) even sets the variable name
             * in case the execution of the joint point resulted in an
             * exception! */
            execData.eoi = eoi;
            execData.ess = ess;
            ctrlInst.newMonitoringRecord(execData);
            if (execData.isEntryPoint) {
                cfRegistry.unsetThreadLocalEOI();
                cfRegistry.unsetThreadLocalESS();
            } else {
                cfRegistry.storeThreadLocalESS(ess);
            }
        }
        return execData.retVal;
    }
}
