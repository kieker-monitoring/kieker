package kieker.tpmon.probe.aspectJ.executions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * kieker.tpmon.aspects.KiekerTpmonMonitoringAnnotationRemoteServlet
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
 *
 * @author Andre van Hoorn
 */
@Aspect
public class KiekerTpmonMonitoringFullRemoteServlet extends AbstractKiekerTpmonMonitoringServlet {

    private static final Log log = LogFactory.getLog(KiekerTpmonMonitoringAnnotationRemote.class);

    @Pointcut("execution(* *.do*(..)) && args(request,response) ")
    public void monitoredServletEntry(HttpServletRequest request, HttpServletResponse response) {
    }

    @Around("monitoredServletEntry(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)")
    public Object doServletEntryProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        return super.doServletEntryProfiling(thisJoinPoint);
    }

    @Pointcut("execution(* *.*(..))"+
              " && !execution(@kieker.tpmon.annotation.TpmonInternal * *.*(..))")
    public void monitoredMethod() {
    }

    @Around("monitoredMethod()")
    public Object doBasicProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        if (!ctrlInst.isMonitoringEnabled()) {
            return thisJoinPoint.proceed();
        }
        KiekerExecutionRecord execData = this.initExecutionData(thisJoinPoint);
        execData.sessionId = sessionRegistry.recallThreadLocalSessionId(); // may be null
        execData.eoi = cfRegistry.incrementAndRecallThreadLocalEOI(); /* this is executionOrderIndex-th execution in this trace */
        execData.ess = cfRegistry.recallAndIncrementThreadLocalESS(); /* this is the height in the dynamic call tree of this execution */

        try {
            this.proceedAndMeasure(thisJoinPoint, execData);
            if (execData.eoi == -1 || execData.ess == -1) {
                log.fatal("current operation: "+execData.componentName+"."+execData.opname);
                log.fatal("eoi and/or ess have invalid values:" +
                        " eoi == " + execData.eoi +
                        " ess == " + execData.ess);
                log.fatal("Terminating Tpmon!");
                ctrlInst.terminateMonitoring();
            }
        } catch (Exception e) {
            throw e; // exceptions are forwarded          
        } finally {
            /* note that proceedAndMeasure(...) even sets the variable name
             * in case the execution of the joint point resulted in an
             * exception! */
            ctrlInst.logMonitoringRecord(execData);
            cfRegistry.storeThreadLocalESS(execData.ess);
        }
        return execData.retVal;
    }
}
