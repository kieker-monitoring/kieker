/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpmon.aspects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kieker.tpmon.ExecutionData;
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

    @Around("monitoredServletEntry(HttpServletRequest, HttpServletResponse)")
    public Object doEntryProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        if (!ctrlInst.isMonitoringEnabled()) {
            return thisJoinPoint.proceed();
        }
        return thisJoinPoint.proceed();
    }
   
   @Pointcut("execution(@TpmonMonitoringProbe * *.*(..)) " +
    "&& !execution(@TpmonInternal * *.*(..)) " +
    "&& !execution(* Dbconnector.*(..)) " +
    "&& !execution(* DbWriter.*(..)) " +
    "&& !execution(* AsyncDbconnector.*(..)) " +
    "&& !execution(* TpmonController.*(..)) " +
    "&& !execution(* FileSystemWriter.*(..))")
    public void monitoredMethod() {
    }
      
    @Around("monitoredMethod()")
    public Object doBasicProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        if (!ctrlInst.isMonitoringEnabled()) {
            return thisJoinPoint.proceed();
        }
        
        ExecutionData execData = new ExecutionData();
        String sessionId = ctrlInst.recallThreadLocalSessionId(); // may be null
        try{
            execData = this.proceedAndMeasure(thisJoinPoint, execData);
        } catch (Exception e){
            throw e; // exceptions are forwarded          
        } finally {
            /* not that proceedAndMeasure(...) even sets the variable name
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
