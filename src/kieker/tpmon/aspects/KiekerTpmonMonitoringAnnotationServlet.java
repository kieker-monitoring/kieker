/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpmon.aspects;

import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kieker.tpmon.ExecutionData;
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
public class KiekerTpmonMonitoringAnnotationServlet extends AbstractKiekerTpmonMonitoring {
    
   @Pointcut("execution(* *.do*(..)) " +
   "&& args(request,response) ")
    public void monitoredServletEntry(HttpServletRequest request, HttpServletResponse response) {
    }

    public static AtomicInteger reqs = new AtomicInteger(0);
    @Around("monitoredServletEntry(HttpServletRequest, HttpServletResponse)")
    public Object doEntryProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        if (!ctrlInst.isMonitoringEnabled()) {
            return thisJoinPoint.proceed();
        }
        
        HttpServletRequest req = (HttpServletRequest)thisJoinPoint.getArgs()[0];
        String sessionId = (req!=null)?req.getSession(true).getId():null;
        Object retVal = null;
        ctrlInst.storeThreadLocalSessionId("Sess"+sessionId);
        try{
            retVal = thisJoinPoint.proceed(); // does pass the args!
        } catch (Exception exc){
            throw exc;
        } finally {
            ctrlInst.unsetThreadLocalSessionId();
        }
        return retVal;
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
        try{
            this.proceedAndMeasure(thisJoinPoint, execData);
        } catch (Exception e){
            throw e; // exceptions are forwarded          
        } finally {
            /* note that proceedAndMeasure(...) even sets the variable name
             * in case the execution of the joint point resulted in an
             * execpetion! */
            ctrlInst.insertMonitoringDataNow(execData.componentName, 
                    execData.opname, sessionId, execData.traceId, 
                    execData.tin, execData.tout);
            if (execData.isEntryPoint){
                // note that the traceId has been unset within proceedAndMeasure
                ctrlInst.unsetThreadLocalSessionId();
            }
        }
        return execData.retVal;
    }
}
