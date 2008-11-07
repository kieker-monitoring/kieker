package kieker.tpmon.aspects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kieker.tpmon.KiekerExecutionRecord;
import kieker.tpmon.annotations.*;
import kieker.tpmon.*;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * kieker.tpmon.aspects.KiekerTpmonMonitoringFullServlet
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
public class KiekerTpmonMonitoringFullServlet extends AbstractKiekerTpmonMonitoringServlet {
    
    @Pointcut("execution(* *.do*(..)) && args(request,response) ")
    public void monitoredServletEntry(HttpServletRequest request, HttpServletResponse response) {
    }

    @Around("monitoredServletEntry(HttpServletRequest, HttpServletResponse)")
    public Object doServletEntryProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        return super.doServletEntryProfiling(thisJoinPoint);
    }

    @Pointcut("execution(* *.*(..)) && !execution(@TpmonInternal * *.*(..))")
    public void monitoredMethod() {
    }

    @Around("monitoredMethod()")
    public Object doBasicProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        if (!ctrlInst.isMonitoringEnabled()) {
            return thisJoinPoint.proceed();
        }
        KiekerExecutionRecord execData = this.initExecutionData(thisJoinPoint);
        String sessionId = ctrlInst.recallThreadLocalSessionId(); // may be null
        try{
            this.proceedAndMeasure(thisJoinPoint, execData);
        } catch (Exception e){
            throw e; // exceptions are forwarded          
        } finally {
            /* note that proceedAndMeasure(...) even sets the variable name
             * in case the execution of the joint point resulted in an
             * execpetion! */
            execData.sessionId = sessionId;
            ctrlInst.insertMonitoringDataNow(execData);
            // Since we didn't register the sessionId we won't unset it!
        }
        return execData.retVal;
    }
}
