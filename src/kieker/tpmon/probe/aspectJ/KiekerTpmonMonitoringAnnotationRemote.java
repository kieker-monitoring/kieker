package kieker.tpmon.probe.aspectJ;

import kieker.tpmon.monitoringRecord.KiekerExecutionRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * kieker.tpmon.aspects.KiekerTpmonMonitoringAnnotationRemote
 *
 * ==================LICENCE=========================
 * Copyright 2006-2008 Matthias Rohr and the Kieker Project
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
public class KiekerTpmonMonitoringAnnotationRemote extends AbstractKiekerTpmonMonitoring { 

    private static final Log log = LogFactory.getLog(KiekerTpmonMonitoringAnnotationRemote.class);

    @Pointcut("execution(@kieker.tpmon.annotation.TpmonMonitoringProbe * *.*(..))"+
              " && !execution(@kieker.tpmon.annotation.TpmonInternal * *.*(..))")
    public void monitoredMethod() {
    }
   
    @Around("monitoredMethod()")
    public Object doBasicProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        if (!ctrlInst.isMonitoringEnabled()) {
            return thisJoinPoint.proceed();
        }
        KiekerExecutionRecord execData = this.initExecutionData(thisJoinPoint);
        int eoi = 0; /* this is executionOrderIndex-th execution in this trace */
        int ess = 0; /* this is the height in the dynamic call tree of this execution */
        if (execData.isEntryPoint){
            ctrlInst.storeThreadLocalEOI(0); // current execution's eoi is 0
            ctrlInst.storeThreadLocalESS(1); // *current* execution's ess is 0
        } else {
            eoi = ctrlInst.incrementAndRecallThreadLocalEOI(); // ess > 1
            ess = ctrlInst.recallAndIncrementThreadLocalESS(); // ess >= 0
        }
        try{
            this.proceedAndMeasure(thisJoinPoint, execData);
            if (eoi == -1 || ess == -1){
                log.fatal("eoi and/or ess have invalid values:" +
                        " eoi == " + eoi +
                        " ess == " + ess);
                log.fatal("Terminating Tpmon!");
                ctrlInst.terminateMonitoring();
            }
        } catch (Exception e){
            throw e; // exceptions are forwarded          
        } finally {
            /* note that proceedAndMeasure(...) even sets the variable name
             * in case the execution of the joint point resulted in an
             * exception! */
            execData.eoi = eoi;
            execData.ess = ess;
            ctrlInst.insertMonitoringDataNow(execData);
            if (execData.isEntryPoint){
                ctrlInst.unsetThreadLocalEOI();
                ctrlInst.unsetThreadLocalESS();
            } else {
                ctrlInst.storeThreadLocalESS(ess);
            }
        }
        return execData.retVal;
    }
}
