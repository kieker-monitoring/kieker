/*
 *
 */
package kieker.tpmon.aspects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kieker.tpmon.ExecutionData;
import kieker.tpmon.*;
import kieker.tpmon.annotations.*;
import kieker.tpmon.asyncDbconnector.*;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 *
 * @author Andre
 */
@Aspect
public class KiekerTpmonMonitoringAnnotationRemoteServlet extends AbstractKiekerTpmonMonitoringServlet { 

   @Pointcut("execution(* *.do*(..)) " +
   "&& args(request,response) ")
    public void monitoredServletEntry(HttpServletRequest request, HttpServletResponse response) {
    }

    @Around("monitoredServletEntry(HttpServletRequest, HttpServletResponse)")
    public Object doServletEntryProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        return super.doServletEntryProfiling(thisJoinPoint);
    }
    
    @Pointcut("execution(@TpmonMonitoringProbe * *.*(..)) ")
    public void monitoredMethod() {
    }
   
    @Around("monitoredMethod()")
    public Object doBasicProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        if (!ctrlInst.isMonitoringEnabled()) {
            return thisJoinPoint.proceed();
        }

        ExecutionData execData = this.initExecutionData(thisJoinPoint);        
        String sessionId = ctrlInst.recallThreadLocalSessionId(); // may be null
        int eoi = 0; /* this is executionOrderIndex-th execution in this trace */
        int ess = 0; /* this is the height in the dynamic call tree of this execution */
        if (execData.isEntryPoint){
            ctrlInst.storeThreadLocalEOI(0);
            ctrlInst.storeThreadLocalESS(1);
        } else {
            eoi = ctrlInst.incrementAndRecallThreadLocalEOI();
            ess = ctrlInst.recallAndIncrementThreadLocalESS();
        }
        try{
            this.proceedAndMeasure(thisJoinPoint, execData);
        } catch (Exception e){
            throw e; // exceptions are forwarded          
        } finally {
            /* note that proceedAndMeasure(...) even sets the variable name
             * in case the execution of the joint point resulted in an
             * exception! */
            ctrlInst.insertMonitoringDataNow(execData.componentName, 
                    execData.opname, sessionId, execData.traceId, 
                    execData.tin, execData.tout,
                    eoi, ess);
            if (execData.isEntryPoint){
                // Since we didn't register the sessionId we won't unset it!
                ctrlInst.unsetThreadLocalEOI();
                ctrlInst.unsetThreadLocalESS();
            } else {
                ctrlInst.storeThreadLocalESS(ess);
            }
        }
        return execData.retVal;
    }
}
