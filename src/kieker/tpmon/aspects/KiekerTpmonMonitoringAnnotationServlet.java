/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
 *
 * @author avanhoorn
 */
@Aspect
public class KiekerTpmonMonitoringAnnotationServlet extends AbstractKiekerTpmonMonitoringServlet {
    

   @Pointcut("execution(* *.do*(..)) " +
   "&& args(request,response) ")
    public void monitoredServletEntry(HttpServletRequest request, HttpServletResponse response) {
    }

    @Around("monitoredServletEntry(HttpServletRequest, HttpServletResponse)")
    public Object doServletEntryProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        return super.doServletEntryProfiling(thisJoinPoint);
    }
   
    @Pointcut("execution(@TpmonMonitoringProbe * *.*(..)) && !execution(@TpmonInternal * *.*(..))")
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
